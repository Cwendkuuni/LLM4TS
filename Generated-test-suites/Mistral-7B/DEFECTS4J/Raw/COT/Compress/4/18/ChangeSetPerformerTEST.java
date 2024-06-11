package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.apache.commons.compress.utils.io.CompressUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ArchiveEntry entry1;
    private ArchiveEntry entry2;
    private ArchiveEntry entry3;
    private ArchiveInputStream inputStream;
    private ArchiveOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        byte[] inputData1 = TestUtils.getTestData("ChangeSetPerformerTest/input1.tar");
        byte[] inputData2 = TestUtils.getTestData("ChangeSetPerformerTest/input2.tar");
        byte[] expectedOutputData = TestUtils.getTestData("ChangeSetPerformerTest/expectedOutput.tar");

        InputStream inputStream1 = new ByteArrayInputStream(inputData1);
        InputStream inputStream2 = new ByteArrayInputStream(inputData2);
        ByteArrayOutputStream outputStreamTemp = new ByteArrayOutputStream();

        changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, "file1.txt", null, new ByteArrayInputStream(new byte[0]), null));
        changeSet.addChange(new Change(Change.TYPE_DELETE, "file1.txt", null, null, null));
        changeSet.addChange(new Change(Change.TYPE_ADD, "file2.txt", null, new ByteArrayInputStream(new byte[0]), null));
        changeSet.addChange(new Change(Change.TYPE_ADD, "dir1/file3.txt", null, new ByteArrayInputStream(new byte[0]), null));

        performer = new ChangeSetPerformer(changeSet);

        entry1 = new TarArchiveEntry("file1.txt");
        entry2 = new TarArchiveEntry("dir1/file3.txt");
        entry3 = new TarArchiveEntry("file2.txt");

        ArchiveOutputStream outputStreamTemp2 = new ArchiveOutputStream(outputStreamTemp, CompressUtils.getTarWriter());
        outputStream = new ArchiveOutputStream(outputStreamTemp2);

        inputStream = new TarArchiveInputStream(new ByteArrayInputStream(inputData1));
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(inputStream1);
        IOUtils.closeQuietly(inputStream2);
    }

    /**
     * Test method for {@link ChangeSetPerformer#ChangeSetPerformer(ChangeSet)}
     */
    @Test
    public void testChangeSetPerformerConstructor() {
        assertNull(performer);
        performer = new ChangeSetPerformer(changeSet);
        assertTrue(performer != null);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     */
    @Test
    public void testPerform() throws IOException {
        ChangeSetResults result = performer.perform(inputStream, outputStream);

        TarArchiveEntry expectedEntry1 = new TarArchiveEntry("file2.txt");
        TarArchiveEntry expectedEntry2 = new TarArchiveEntry("dir1/file3.txt");

        assertEquals(2, result.getNumberOfChangesApplied());
        assertEquals(Arrays.asList("file2.txt", "dir1/file3.txt"), result.getNamesOfAddedFiles());
        assertEquals("file1.txt", result.getNameOfDeletedFile());

        ArchiveEntry actualEntry1 = outputStream.getNextEntry();
        ArchiveEntry actualEntry2 = outputStream.getNextEntry();

        assertEquals(expectedEntry1, actualEntry1);
        assertEquals(expectedEntry2, actualEntry2);

        InputStream inputStreamResult = new ByteArrayInputStream(outputStreamTemp.toByteArray());
        ArchiveInputStream inputStreamResult2 = new TarArchiveInputStream(inputStreamResult);

        ArchiveEntry resultEntry1 = inputStreamResult2.getNextEntry();
        ArchiveEntry resultEntry2 = inputStreamResult2.getNextEntry();

        assertEquals(expectedEntry1, resultEntry1);
        assertEquals(expectedEntry2, resultEntry2);
    }

    /**
     * Test method for {@link ChangeSetPerformer#isDeletedLater(Set, ArchiveEntry)}
     */
    @Test
    public void testIsDeletedLater() {
        Set<Change> workingSet = new LinkedHashSet<>(changeSet.getChanges());

        assertTrue(performer.isDeletedLater(workingSet, entry1));
        assertFalse(performer.isDeletedLater(workingSet, entry2));
        assertFalse(performer.isDeletedLater(workingSet, entry3));

        workingSet.add(new Change(Change.TYPE_DELETE, "file1.txt", null, null, null));

        assertFalse(performer.isDeletedLater(workingSet, entry1));
        assertTrue(performer.isDeletedLater(workingSet, entry1));
    }

    /**
     * Test method for {@link ChangeSetPerformer#copyStream(InputStream, ArchiveOutputStream, ArchiveEntry)}
     */
    @Test
    public void testCopyStream() throws IOException {
        performer.copyStream(inputStream1, outputStream, entry1);
        performer.copyStream(inputStream2, outputStream, entry2);

        byte[] actualOutputData = IOUtils.toByteArray(new ByteArrayInputStream(outputStreamTemp.toByteArray()));

        assertEquals(expectedOutputData, actualOutputData);
    }
}