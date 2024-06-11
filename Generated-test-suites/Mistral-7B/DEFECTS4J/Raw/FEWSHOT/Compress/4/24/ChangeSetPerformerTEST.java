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
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private InputStream inputStream;
    private ArchiveInputStream archiveInputStream;
    private ArchiveOutputStream archiveOutputStream;
    private ChangeSet changeSet;
    private ChangeSetPerformer changeSetPerformer;

    @Before
    public void setUp() throws IOException {
        changeSet = new ChangeSet();

        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = mock(ArchiveInputStream.class);
        when(archiveInputStream.getNextEntry()).thenReturn(new TarArchiveEntry("entry1.txt"));
        when(archiveInputStream.readAllBytes()).thenReturn(new byte[0]);

        archiveOutputStream = mock(ArchiveOutputStream.class);

        changeSetPerformer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_EmptyChangeSet() throws IOException {
        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testPerform_InputStreamEmpty() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testPerform_InputArchiveEmpty() throws IOException {
        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testPerform_OneAddChange() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveOutputStream("output.tar.gz", new GzipCompressorOutputStream(outputStream));

        Change addChange = new Change(Change.TYPE_ADD, new File("input.txt"), null, "/output/input.txt", false);
        changeSet.addChange(addChange);

        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        TarArchiveEntry expectedEntry = new TarArchiveEntry("/output/input.txt");
        expectedEntry.setSize(inputStream.available());
        TarArchiveEntry actualEntry = results.getAddedEntries().get(0);
        assertSame(expectedEntry, actualEntry);

        byte[] expectedContent = new byte[inputStream.available()];
        inputStream.read(expectedContent);
        byte[] actualContent = IOUtils.toByteArray(archiveOutputStream);
        assertArrayEquals(expectedContent, actualContent);

        assertTrue(results.isSuccess());
    }

    @Test
    public void testPerform_OneAddChange_StreamAlreadyAdded() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveOutputStream("output.tar.gz", new GzipCompressorOutputStream(outputStream));

        Change addChange = new Change(Change.TYPE_ADD, new File("input.txt"), null, "/output/input.txt", false);
        changeSet.addChange(addChange);
        changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        assertFalse(results.isSuccess());
        assertEquals(1, results.getFailedAdditions().size());
        Change failedAddChange = results.getFailedAdditions().get(0);
        assertEquals(Change.TYPE_ADD, failedAddChange.type());
        assertEquals("/output/input.txt", failedAddChange.targetFile());
    }

    @Test
    public void testPerform_OneDeleteChange() throws IOException {
        Change deleteChange = new Change(Change.TYPE_DELETE, null, new File("output/input.txt"), null, false);
        changeSet.addChange(deleteChange);

        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        TarArchiveEntry expectedEntry = new TarArchiveEntry("output/input.txt");
        assertSame(expectedEntry, results.getDeletedEntries().get(0));

        assertTrue(results.isSuccess());
    }

    @Test
    public void testPerform_OneDeleteChange_EntryNotDeleted() throws IOException {
        Change deleteChange = new Change(Change.TYPE_DELETE, null, new File("output/input.txt"), null, false);
        changeSet.addChange(deleteChange);

        TarArchiveEntry entry = new TarArchiveEntry("output/input.txt");
        when(archiveInputStream.getNextEntry()).thenReturn(entry);

        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        assertFalse(results.isSuccess());
        assertEquals(1, results.getFailedDeletions().size());
        Change failedDeleteChange = results.getFailedDeletions().get(0);
        assertEquals(Change.TYPE_DELETE, failedDeleteChange.type());
        assertEquals("/output/input.txt", failedDeleteChange.targetFile());
    }

    @Test
    public void testPerform_OneAddChangeWithReplaceMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveOutputStream("output.tar.gz", new GzipCompressorOutputStream(outputStream));

        Change addChange = new Change(Change.TYPE_ADD, new File("input.txt"), null, "/output/input.txt", true);
        changeSet.addChange(addChange);

        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        TarArchiveEntry expectedEntry = new TarArchiveEntry("/output/input.txt");
        expectedEntry.setSize(inputStream.available());
        TarArchiveEntry actualEntry = results.getAddedEntries().get(0);
        assertSame(expectedEntry, actualEntry);

        byte[] expectedContent = new byte[inputStream.available()];
        inputStream.read(expectedContent);
        byte[] actualContent = IOUtils.toByteArray(archiveOutputStream);
        assertArrayEquals(expectedContent, actualContent);

        assertTrue(results.isSuccess());

        InputStream inputStream2 = new ByteArrayInputStream(new byte[inputStream.available()]);
        ChangeSetResults results2 = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        assertTrue(results2.isEmpty());
    }

    @Test
    public void testPerform_MultipleChanges() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveOutputStream("output.tar.gz", new GzipCompressorOutputStream(outputStream));

        Change addChange1 = new Change(Change.TYPE_ADD, new File("input1.txt"), null, "/output/input1.txt", false);
        changeSet.addChange(addChange1);

        Change addChange2 = new Change(Change.TYPE_ADD, new File("input2.txt"), null, "/output/input2.txt", false);
        changeSet.addChange(addChange2);

        Change deleteChange = new Change(Change.TYPE_DELETE, null, new File("output/input1.txt"), null, false);
        changeSet.addChange(deleteChange);

        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        TarArchiveEntry expectedEntry1 = new TarArchiveEntry("/output/input1.txt");
        TarArchiveEntry actualEntry1 = results.getDeletedEntries().get(0);
        assertSame(expectedEntry1, actualEntry1);

        TarArchiveEntry expectedEntry2 = new TarArchiveEntry("/output/input2.txt");
        TarArchiveEntry actualEntry2 = results.getAddedEntries().get(0);
        assertSame(expectedEntry2, actualEntry2);

        byte[] expectedContent1 = new byte[inputStream.available()];
        inputStream.read(expectedContent1);
        byte[] actualContent1 = IOUtils.toByteArray(archiveOutputStream);
        assertArrayEquals(expectedContent1, actualContent1);

        byte[] expectedContent2 = new byte[inputStream.available()];
        inputStream.read(expectedContent2);
        byte[] actualContent2 = IOUtils.toByteArray(archiveOutputStream);
        assertArrayEquals(expectedContent2, actualContent2);

        assertTrue(results.isSuccess());
    }

    @Test
    public void testPerform_MultipleChanges_DuplicateAddition() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveOutputStream("output.tar.gz", new GzipCompressorOutputStream(outputStream));

        Change addChange1 = new Change(Change.TYPE_ADD, new File("input1.txt"), null, "/output/input1.txt", false);
        changeSet.addChange(addChange1);

        Change addChange2 = new Change(Change.TYPE_ADD, new File("input1.txt"), null, "/output/input1.txt", false);
        changeSet.addChange(addChange2);

        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        TarArchiveEntry expectedEntry1 = new TarArchiveEntry("/output/input1.txt");
        TarArchiveEntry actualEntry1 = results.getAddedEntries().get(0);
        assertSame(expectedEntry1, actualEntry1);

        byte[] expectedContent1 = new byte[inputStream.available()];
        inputStream.read(expectedContent1);
        byte[] actualContent1 = IOUtils.toByteArray(archiveOutputStream);
        assertArrayEquals(expectedContent1, actualContent1);

        assertTrue(results.isSuccess());

        assertEquals(1, results.getAddedEntries().size());
    }

    @Test
    public void testPerform_MultipleChanges_DuplicateDeletion() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveOutputStream("output.tar.gz", new GzipCompressorOutputStream(outputStream));

        Change deleteChange1 = new Change(Change.TYPE_DELETE, null, new File("output/input1.txt"), null, false);
        changeSet.addChange(deleteChange1);

        Change deleteChange2 = new Change(Change.TYPE_DELETE, null, new File("output/input1.txt"), null, false);
        changeSet.addChange(deleteChange2);

        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        assertFalse(results.isSuccess());
        assertEquals(1, results.getFailedDeletions().size());
        Change failedDeleteChange = results.getFailedDeletions().get(0);
        assertEquals(Change.TYPE_DELETE, failedDeleteChange.type());
        assertEquals("/output/input1.txt", failedDeleteChange.targetFile());
    }

    @Test
    public void testPerform_MultipleChanges_DuplicateDeleteDirectory() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveOutputStream("output.tar.gz", new GzipCompressorOutputStream(outputStream));

        Change deleteDirectoryChange1 = new Change(Change.TYPE_DELETE_DIR, null, new File("output/deletedir"), null, false);
        changeSet.addChange(deleteDirectoryChange1);

        Change deleteDirectoryChange2 = new Change(Change.TYPE_DELETE_DIR, null, new File("output/deletedir"), null, false);
        changeSet.addChange(deleteDirectoryChange2);

        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        assertFalse(results.isSuccess());
        assertEquals(1, results.getFailedDeletions().size());
        Change failedDeleteChange = results.getFailedDeletions().get(0);
        assertEquals(Change.TYPE_DELETE_DIR, failedDeleteChange.type());
        assertEquals("/output/deletedir", failedDeleteChange.targetFile());
    }

    @Test
    public void testPerform_MultipleChanges_DeleteDirectoryWithChildren() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveOutputStream("output.tar.gz", new GzipCompressorOutputStream(outputStream));

        Change deleteDirectoryChange = new Change(Change.TYPE_DELETE_DIR, null, new File("output/deletedir"), null, false);
        changeSet.addChange(deleteDirectoryChange);

        Change addChange1 = new Change(Change.TYPE_ADD, new File("input1.txt"), null, "/output/deletedir/input1.txt", false);
        changeSet.addChange(addChange1);

        Change addChange2 = new Change(Change.TYPE_ADD, new File("input2.txt"), null, "/output/deletedir/input2.txt", false);
        changeSet.addChange(addChange2);

        ChangeSetResults results = changeSetPerformer.perform(archiveInputStream, archiveOutputStream);

        assertFalse(results.isSuccess());
        assertEquals(2, results.getFailedAdditions().size());
        Change failedAddChange1 = results.getFailedAdditions().get(0);
        assertEquals("/output/deletedir/input1.txt", failedAddChange1.targetFile());
        Change failedAddChange2 = results.getFailedAdditions().get(1);
        assertEquals("/output/deletedir/input2.txt", failedAddChange2.targetFile());
    }
}