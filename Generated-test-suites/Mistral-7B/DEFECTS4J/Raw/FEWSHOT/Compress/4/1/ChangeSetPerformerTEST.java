package org.apache.commons.compress.changes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ZipOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ZipFile testZipFile;
    private ZipOutputStream testZipOutputStream;
    private ChangeSet testChangeSet;
    private ChangeSetPerformer testChangeSetPerformer;

    @Before
    public void setUp() throws IOException {
        testZipFile = new ZipFile("test.zip");
        testZipOutputStream = new ZipOutputStream(new ByteArrayOutputStream());
        testChangeSet = new ChangeSet();

        // Add test entries
        testChangeSet.addChange(new Change(Change.TYPE_ADD, "test/test1.txt", new ByteArrayInputStream(new byte[0]), null));
        testChangeSet.addChange(new Change(Change.TYPE_ADD, "test/test2.txt", new ByteArrayInputStream(new byte[0]), null));
        testChangeSet.addChange(new Change(Change.TYPE_DELETE, null, null, "test/test1.txt"));
        testChangeSet.addChange(new Change(Change.TYPE_DELETE_DIR, null, null, "test/test3/"));

        testChangeSetPerformer = new ChangeSetPerformer(testChangeSet);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(testZipOutputStream);
        IOUtils.closeQuietly(testZipFile);
    }

    @Test
    public void testIsDeletedLater() {
        // Test empty set
        assertFalse(ChangeSetPerformer.isDeletedLater(Collections.emptySet(), mock(ArchiveEntry.class)));

        // Test with no deletion
        assertTrue(ChangeSetPerformer.isDeletedLater(Collections.singleton(new Change(Change.TYPE_ADD, "test/test1.txt", new ByteArrayInputStream(new byte[0]), null)), mock(ArchiveEntry.class)));

        // Test with deletion
        assertTrue(ChangeSetPerformer.isDeletedLater(Arrays.asList(
                new Change(Change.TYPE_ADD, "test/test1.txt", new ByteArrayInputStream(new byte[0]), null),
                new Change(Change.TYPE_DELETE, null, null, "test/test1.txt")), mock(ArchiveEntry.class)));

        // Test with deletion of directory
        assertTrue(ChangeSetPerformer.isDeletedLater(Arrays.asList(
                new Change(Change.TYPE_ADD, "test/test1.txt", new ByteArrayInputStream(new byte[0]), null),
                new Change(Change.TYPE_DELETE_DIR, null, null, "test/test3/")), new ZipEntry("test/test3/test4.txt")));

        // Test with multiple deletions
        assertTrue(ChangeSetPerformer.isDeletedLater(Arrays.asList(
                new Change(Change.TYPE_ADD, "test/test1.txt", new ByteArrayInputStream(new byte[0]), null),
                new Change(Change.TYPE_DELETE, null, null, "test/test1.txt"),
                new Change(Change.TYPE_DELETE, null, null, "test/test1.txt")), mock(ArchiveEntry.class)));
    }

    @Test
    public void testPerform() throws IOException {
        // Test no changes
        ArchiveInputStream testIn = new ZipInputStream(testZipFile.getInputStream(new ZipEntry("test.zip")));
        ChangeSetResults testResults = testChangeSetPerformer.perform(testIn, new ZipOutputStream(testZipOutputStream));
        assertNull(testResults.getAddedFromChangeSet());
        assertNull(testResults.getDeleted());
        assertNull(testResults.getAddedFromStream());

        // Test add only
        testChangeSetPerformer = new ChangeSetPerformer(testChangeSet);
        testZipOutputStream.putNextEntry(new ZipEntry("test/test1.txt"));
        testZipOutputStream.write(new byte[0]);
        testZipOutputStream.closeEntry();
        testZipOutputStream.finish();
        testIn = new ZipInputStream(new ByteArrayInputStream(testZipOutputStream.toByteArray()));
        testResults = testChangeSetPerformer.perform(testIn, new ZipOutputStream(testZipOutputStream));
        assertEquals("test/test1.txt", testResults.getAddedFromChangeSet()[0]);
        assertArrayEquals(new String[]{"test/test1.txt"}, testResults.getAddedFromStream());
        assertEquals(2, testChangeSet.size());

        // Test delete only
        testChangeSet.clear();
        testChangeSet.addChange(new Change(Change.TYPE_DELETE, null, null, "test/test1.txt"));
        testChangeSetPerformer = new ChangeSetPerformer(testChangeSet);
        testZipOutputStream.putNextEntry(new ZipEntry("test/test1.txt"));
        testZipOutputStream.write(new byte[0]);
        testZipOutputStream.closeEntry();
        testZipOutputStream.finish();
        testIn = new ZipInputStream(new ByteArrayInputStream(testZipOutputStream.toByteArray()));
        testResults = testChangeSetPerformer.perform(testIn, new ZipOutputStream(testZipOutputStream));
        assertArrayEquals(new String[]{"test/test1.txt"}, testResults.getDeleted());
        assertEquals(1, testChangeSet.size());

        // Test add and delete
        testChangeSet.clear();
        testChangeSet.addChange(new Change(Change.TYPE_ADD, "test/test1.txt", new ByteArrayInputStream(new byte[0]), null));
        testChangeSet.addChange(new Change(Change.TYPE_DELETE, null, null, "test/test1.txt"));
        testChangeSetPerformer = new ChangeSetPerformer(testChangeSet);
        testZipOutputStream.putNextEntry(new ZipEntry("test/test1.txt"));
        testZipOutputStream.write(new byte[0]);
        testZipOutputStream.closeEntry();
        testZipOutputStream.finish();
        testIn = new ZipInputStream(new ByteArrayInputStream(testZipOutputStream.toByteArray()));
        testResults = testChangeSetPerformer.perform(testIn, new ZipOutputStream(testZipOutputStream));
        assertNull(testResults.getAddedFromChangeSet());
        assertArrayEquals(new String[]{}, testResults.getAddedFromStream());
        assertArrayEquals(new String[]{"test/test1.txt"}, testResults.getDeleted());
        assertEquals(1, testChangeSet.size());

        // Test multiple add and delete
        testChangeSet.clear();
        testChangeSet.addChange(new Change(Change.TYPE_ADD, "test/test1.txt", new ByteArrayInputStream(new byte[0]), null));
        testChangeSet.addChange(new Change(Change.TYPE_DELETE, null, null, "test/test1.txt"));
        testChangeSet.addChange(new Change(Change.TYPE_ADD, "test/test2.txt", new ByteArrayInputStream(new byte[0]), null));
        testChangeSetPerformer = new ChangeSetPerformer(testChangeSet);
        testZipOutputStream.putNextEntry(new ZipEntry("test/test1.txt"));
        testZipOutputStream.write(new byte[0]);
        testZipOutputStream.closeEntry();
        testZipOutputStream.putNextEntry(new ZipEntry("test/test2.txt"));
        testZipOutputStream.write(new byte[0]);
        testZipOutputStream.closeEntry();
        testZipOutputStream.finish();
        testIn = new ZipInputStream(new ByteArrayInputStream(testZipOutputStream.toByteArray()));
        testResults = testChangeSetPerformer.perform(testIn, new ZipOutputStream(testZipOutputStream));
        assertArrayEquals(new String[]{"test/test2.txt"}, testResults.getAddedFromChangeSet());
        assertArrayEquals(new String[]{"test/test1.txt"}, testResults.getDeleted());
        assertEquals(3, testChangeSet.size());

        // Test replace mode
        testChangeSet.clear();
        testChangeSet.addChange(new Change(Change.TYPE_ADD, "test/test1.txt", new ByteArrayInputStream(new byte[0]), null, true));
        testChangeSet.addChange(new Change(Change.TYPE_DELETE, null, null, "test/test1.txt"));
        testChangeSetPerformer = new ChangeSetPerformer(testChangeSet);
        testZipOutputStream.putNextEntry(new ZipEntry("test/test1.txt"));
        testZipOutputStream.write(new byte[0]);
        testZipOutputStream.closeEntry();
        testZipOutputStream.finish();
        testIn = new ZipInputStream(new ByteArrayInputStream(testZipOutputStream.toByteArray()));
        testResults = testChangeSetPerformer.perform(testIn, new ZipOutputStream(testZipOutputStream));
        assertNull(testResults.getAddedFromChangeSet());
        assertArrayEquals(new String[]{"test/test1.txt"}, testResults.getAddedFromStream());
        assertNull(testResults.getDeleted());
        assertEquals(1, testChangeSet.size());

        // Test replacement of deleted file
        testChangeSet.clear();
        testChangeSet.addChange(new Change(Change.TYPE_DELETE, null, null, "test/test1.txt"));
        testChangeSet.addChange(new Change(Change.TYPE_ADD, "test/test1.txt", new ByteArrayInputStream(new byte[0]), null, true));
        testChangeSetPerformer = new ChangeSetPerformer(testChangeSet);
        testZipOutputStream.putNextEntry(new ZipEntry("test/test1.txt"));
        testZipOutputStream.write(new byte[0]);
        testZipOutputStream.closeEntry();
        testZipOutputStream.finish();
        testIn = new ZipInputStream(new ByteArrayInputStream(testZipOutputStream.toByteArray()));
        testResults = testChangeSetPerformer.perform(testIn, new ZipOutputStream(testZipOutputStream));
        assertNull(testResults.getAddedFromChangeSet());
        assertNull(testResults.getAddedFromStream());
        assertNull(testResults.getDeleted());
        assertEquals(1, testChangeSet.size());
    }

    @Test
    public void testPerformWithConflictingChanges() throws IOException {
        testChangeSet.clear();
        testChangeSet.addChange(new Change(Change.TYPE_ADD, "test/test1.txt", new ByteArrayInputStream(new byte[0]), null));
        testChangeSet.addChange(new Change(Change.TYPE_DELETE, null, null, "test/test1.txt"));
        testChangeSetPerformer = new ChangeSetPerformer(testChangeSet);
        testZipOutputStream.putNextEntry(new ZipEntry("test/test1.txt"));
        testZipOutputStream.write(new byte[0]);
        testZipOutputStream.closeEntry();
        testZipOutputStream.finish();
        testIn = new ZipInputStream(new ByteArrayInputStream(testZipOutputStream.toByteArray()));
        testResults = testChangeSetPerformer.perform(testIn, new ZipOutputStream(testZipOutputStream));
        assertNull(testResults.getAddedFromChangeSet());
        assertArrayEquals(new String[]{"test/test1.txt"}, testResults.getAddedFromStream());
        assertNull(testResults.getDeleted());
        assertEquals(1, testChangeSet.size());

        // Test replacement of deleted file
        testChangeSet.clear();
        testChangeSet.addChange(new Change(Change.TYPE_DELETE, null, null, "test/test1.txt"));
        testChangeSet.addChange(new Change(Change.TYPE_ADD, "test/test1.txt", new ByteArrayInputStream(new byte[0]), null, true));
        testChangeSetPerformer = new ChangeSetPerformer(testChangeSet);
        testZipOutputStream.putNextEntry(new ZipEntry("test/test1.txt"));
        testZipOutputStream.write(new byte[0]);
        testZipOutputStream.closeEntry();
        testZipOutputStream.finish();
        testIn = new ZipInputStream(new ByteArrayInputStream(testZipOutputStream.toByteArray()));
        testResults = testChangeSetPerformer.perform(testIn, new ZipOutputStream(testZipOutputStream));
        assertNull(testResults.getAddedFromChangeSet());
        assertNull(testResults.getAddedFromStream());
        assertNull(testResults.getDeleted());
        assertEquals(1, testChangeSet.size());

        // Test replacement of deleted directory with a file
        testChangeSet.clear();
        testChangeSet.addChange(new Change(Change.TYPE_DELETE_DIR, null, null, "test/test3/"));
        testChangeSet.addChange(new Change(Change.TYPE_ADD, "test/test3/test1.txt", new ByteArrayInputStream(new byte[0]), null, true));
        testChangeSetPerformer = new ChangeSetPerformer(testChangeSet);
        testZipOutputStream.putNextEntry(new ZipEntry("test/test3/test1.txt"));
        testZipOutputStream.write(new byte[0]);
        testZipOutputStream.closeEntry();
        testZipOutputStream.finish();
        testIn = new ZipInputStream(new ByteArrayInputStream(testZipOutputStream.toByteArray()));
        testResults = testChangeSetPerformer.perform(testIn, new ZipOutputStream(testZipOutputStream));
        assertNull(testResults.getAddedFromChangeSet());
        assertArrayEquals(new String[]{"test/test3/test1.txt"}, testResults.getAddedFromStream());
        assertNull(testResults.getDeleted());
        assertEquals(2, testChangeSet.size());

        // Test replacement of deleted directory with a directory
        testChangeSet.clear();
        testChangeSet.addChange(new Change(Change.TYPE_DELETE_DIR, null, null, "test/test3/"));
        testChangeSet.addChange(new Change(Change.TYPE_ADD, "test/test3/test1.txt", new ByteArrayInputStream(new byte[0]), null, true));
        testChangeSet.addChange(new Change(Change.TYPE_ADD, "test/test3/test2.txt", new ByteArrayInputStream(new byte[0]), null, true));
        testChangeSetPerformer = new ChangeSetPerformer(testChangeSet);
        testZipOutputStream.putNextEntry(new ZipEntry("test/test3/test1.txt"));
        testZipOutputStream.putNextEntry(new ZipEntry("test/test3/test2.txt"));
        testZipOutputStream.write(new byte[0]);
        testZipOutputStream.closeEntry();
        testZipOutputStream.finish();
        testIn = new ZipInputStream(new ByteArrayInputStream(testZipOutputStream.toByteArray()));
        testResults = testChangeSetPerformer.perform(testIn, new ZipOutputStream(testZipOutputStream));
        assertNull(testResults.getAddedFromChangeSet());
        assertArrayEquals(new String[]{"test/test3/test1.txt", "test/test3/test2.txt"}, testResults.getAddedFromStream());
        assertNull(testResults.getDeleted());
        assertEquals(3, testChangeSet.size());
    }
}