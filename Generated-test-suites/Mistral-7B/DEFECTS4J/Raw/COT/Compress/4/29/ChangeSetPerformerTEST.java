package org.apache.commons.compress.changes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ZipOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.test.TestUtils;
import org.apache.commons.compress.utils.zip.ZipEntryContent;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;

/**
 * Test class for ChangeSetPerformer.
 */
public class ChangeSetPerformerTest {

    private static final int SIZE_OF_EMPTY_BUFFER = 0;
    private static final int SIZE_OF_ARRAY = 10;

    private ChangeSet changeSet;

    @Before
    public void setUp() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change(Change.TYPE_ADD, "testFile.txt", SIZE_OF_ARRAY, null, null, false);
        Change deleteChange = new Change(Change.TYPE_DELETE, "testFile.txt", null, null, null, false);
        Change addAndReplaceChange = new Change(Change.TYPE_ADD, "testFile2.txt", SIZE_OF_ARRAY, null, null, true);
        Change deleteAndReplaceChange = new Change(Change.TYPE_DELETE, "testFile.txt", null, null, null, true);
        Change deleteDirectoryChange = new Change(Change.TYPE_DELETE_DIR, "/testDir/", null, null, null, false);

        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);
        changeSet.addChange(addAndReplaceChange);
        changeSet.addChange(deleteAndReplaceChange);
        changeSet.addChange(deleteDirectoryChange);
        this.changeSet = changeSet;
    }

    @Test
    public void testChangeSetPerformerConstructor() {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        assertNotNull(performer);
        Set changes = performer.getChanges();
        assertEquals(changeSet.getChanges().size(), changes.size());
    }

    @Test
    public void testPerform() throws Exception {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream in = createMockArchiveInputStream();
        ArchiveOutputStream out = createMockArchiveOutputStream();

        ChangeSetResults results = performer.perform(in, out);
        assertNotNull(results);
        Set expectedAddedFromChangeSet = new HashSet(Arrays.asList(new String[]{"testFile.txt", "testFile2.txt"}));
        Set actualAddedFromChangeSet = results.addedFromChangeSet();
        assertEquals(expectedAddedFromChangeSet, actualAddedFromChangeSet);
        Set expectedDeleted = new HashSet(Arrays.asList(new String[]{"testFile.txt"}));
        Set actualDeleted = results.deleted();
        assertEquals(expectedDeleted, actualDeleted);

        InputStream inputStream = createMockInputStream("testFile.txt".getBytes());
        ArchiveInputStream mockArchiveInputStream = new ArchiveInputStream(inputStream);
        ArchiveOutputStream mockArchiveOutputStream = new ArchiveOutputStream(new ByteArrayOutputStream());
        ChangeSetResults results2 = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);
        Set expectedAddedFromStream = new HashSet(Arrays.asList(new String[]{"testFile2.txt"}));
        Set actualAddedFromStream = results2.addedFromStream();
        assertEquals(expectedAddedFromStream, actualAddedFromStream);
        Set actualAddedFromChangeSet2 = results2.addedFromChangeSet();
        Set expectedAddedFromChangeSet2 = new HashSet(Collections.singletonList("testFile.txt"));
        assertEquals(expectedAddedFromChangeSet2, actualAddedFromChangeSet2);

        InputStream inputStream2 = createMockInputStream("testFile2.txt".getBytes());
        ArchiveInputStream mockArchiveInputStream2 = new ArchiveInputStream(inputStream2);
        ArchiveOutputStream mockArchiveOutputStream2 = new ArchiveOutputStream(new ByteArrayOutputStream());
        ChangeSetResults results3 = performer.perform(mockArchiveInputStream2, mockArchiveOutputStream2);
        Set expectedAddedFromStream2 = new HashSet(Collections.emptyList());
        Set actualAddedFromStream2 = results3.addedFromStream();
        assertEquals(expectedAddedFromStream2, actualAddedFromStream2);
        Set expectedAddedFromChangeSet3 = Collections.emptySet();
        Set actualAddedFromChangeSet3 = results3.addedFromChangeSet();
        assertEquals(expectedAddedFromChangeSet3, actualAddedFromChangeSet3);
    }

    @Test(expected = IOException.class)
    public void testPerformWithNullInputOrOutput() throws Exception {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(null, null);
    }

    @Test(expected = IOException.class)
    public void testPerformWithIOException() throws Exception {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream in = createMockArchiveInputStreamWithIOException();
        ArchiveOutputStream out = createMockArchiveOutputStream();
        performer.perform(in, out);
    }

    @Test
    public void testIsDeletedLater() {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveEntry entry = new ZipEntry("testFile.txt");

        assertTrue(performer.isDeletedLater(new HashSet(), entry));
        Set workingSet = new HashSet();
        workingSet.add(changeSet.getChange(1));
        assertFalse(performer.isDeletedLater(workingSet, entry));

        workingSet.add(changeSet.getChange(3));
        assertTrue(performer.isDeletedLater(workingSet, entry));

        workingSet.add(changeSet.getChange(4));
        assertTrue(performer.isDeletedLater(workingSet, new ZipEntry("/testDir/testFile.txt")));
    }

    @Test
    public void testCopyStream() throws Exception {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        InputStream inputStream = createMockInputStream("testFile.txt".getBytes());
        ArchiveOutputStream mockArchiveOutputStream = new ArchiveOutputStream(result);

        performer.copyStream(inputStream, mockArchiveOutputStream, null);
        byte[] expected = TestUtils.getBytesFromResource("/testFile.txt");
        byte[] actual = result.toByteArray();
        assertArrayEquals(expected, actual);
    }

    private ArchiveInputStream createMockArchiveInputStream() {
        ArchiveInputStream archiveInputStream = mock(ArchiveInputStream.class);
        when(archiveInputStream.getNextEntry()).thenReturn(new ZipEntry("testFile.txt"));
        when(archiveInputStream.getNextEntry()).thenReturn(new ZipEntry("testFile2.txt"));
        when(archiveInputStream.getNextEntry()).thenReturn(null);
        return archiveInputStream;
    }

    private ArchiveInputStream createMockArchiveInputStreamWithIOException() {
        ArchiveInputStream archiveInputStream = mock(ArchiveInputStream.class);
        when(archiveInputStream.getNextEntry()).thenThrow(new IOException());
        return archiveInputStream;
    }

    private ArchiveOutputStream createMockArchiveOutputStream() {
        ArchiveOutputStream archiveOutputStream = mock(ArchiveOutputStream.class);
        return archiveOutputStream;
    }

    private InputStream createMockInputStream(byte[] bytesToRead) {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.readAllBytes()).thenReturn(bytesToRead);
        return inputStream;
    }
}