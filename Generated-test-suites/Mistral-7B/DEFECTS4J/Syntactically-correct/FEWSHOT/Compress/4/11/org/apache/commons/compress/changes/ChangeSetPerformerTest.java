package org.apache.commons.compress.changes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private InputStream inputStream;
    private ArchiveInputStream archiveInput;
    private ArchiveOutputStream archiveOutput;
    private ChangeSet changeSet;
    private ChangeSetPerformer performer;

    @Before
    public void setUp() throws Exception {
        inputStream = new ByteArrayInputStream(TestUtils.getTestData("testdata.bin"));
        archiveInput = new ArchiveInputStream(inputStream);
        archiveOutput = new ArchiveOutputStream("testOutput.tar.gz");
        changeSet = new DefaultChangeSet();
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_NoChanges() throws IOException {
        ChangeSetResults results = performer.perform(archiveInput, archiveOutput);
        assertEquals(0, results.getAddedFromChangeSet().size());
        assertEquals(0, results.getAddedFromStream().size());
        assertEquals(0, results.getDeleted().size());
    }

    @Test
    public void testPerform_OneAddChange() throws IOException {
        Change addChange = createChange("testfile.txt", Change.TYPE_ADD, true);
        changeSet.addChange(addChange);

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput);

        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getAddedFromStream().size());
        assertTrue(results.getAddedFromChangeSet().contains("testfile.txt"));
        assertTrue(results.getAddedFromStream().contains("testfile.txt"));
    }

    @Test
    public void testPerform_OneDeleteChange() throws IOException {
        Change deleteChange = createChange("testfile.txt", Change.TYPE_DELETE, false);
        changeSet.addChange(deleteChange);

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput);

        assertEquals(0, results.getAddedFromChangeSet().size());
        assertEquals(0, results.getAddedFromStream().size());
        assertEquals(1, results.getDeleted().size());
        assertTrue(results.getDeleted().contains("testfile.txt"));
    }

    @Test
    public void testPerform_OneAddAndOneDeleteChange() throws IOException {
        Change addChange = createChange("testfile.txt", Change.TYPE_ADD, true);
        changeSet.addChange(addChange);

        Change deleteChange = createChange("testfile.txt", Change.TYPE_DELETE, false);
        changeSet.addChange(deleteChange);

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput);

        assertEquals(0, results.getAddedFromChangeSet().size());
        assertEquals(0, results.getAddedFromStream().size());
        assertEquals(1, results.getDeleted().size());
        assertTrue(results.getDeleted().contains("testfile.txt"));

        InputStream testOutputStream = new ByteArrayInputStream(archiveOutput.toByteArray());
        ArchiveInputStream testOutputArchive = new ArchiveInputStream(testOutputStream);
        assertTrue(testOutputArchive.getNextEntry() == null);
    }

    @Test
    public void testPerform_OneAddChangeWithReplaceMode() throws IOException {
        Change addChange = createChange("testfile.txt", Change.TYPE_ADD, true);
        changeSet.addChange(addChange);

        ArchiveEntry entry = createEntry("testfile.txt");
        when(archiveInput.getNextEntry()).thenReturn(entry);

        ChangeSetResults results = performer.perform(archiveInput, archiveOutput);

        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getAddedFromStream().size());
        assertTrue(results.getAddedFromChangeSet().contains("testfile.txt"));
        assertTrue(results.getAddedFromStream().contains("testfile.txt"));

        verify(archiveOutput, times(1)).putArchiveEntry(entry);
        verifyNoMoreInteractions(archiveOutput);
    }

    @Test(expected = IOException.class)
    public void testPerform_IOException() throws IOException {
        when(archiveInput.getNextEntry()).thenThrow(new IOException("Test Exception"));

        performer.perform(archiveInput, archiveOutput);
    }

    @Test
    public void testIsDeletedLater() {
        Set<Change> changes = new LinkedHashSet<>();

        Change addChange1 = createChange("testfile1.txt", Change.TYPE_ADD, true);
        Change addChange2 = createChange("testfile2.txt", Change.TYPE_ADD, true);
        Change deleteChange = createChange("testfile1.txt", Change.TYPE_DELETE, false);

        changes.add(addChange1);
        changes.add(addChange2);
        changes.add(deleteChange);

        ChangeSet changeSet = new DefaultChangeSet(changes);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        assertTrue(performer.isDeletedLater(changes, createEntry("testfile1.txt")));
        assertFalse(performer.isDeletedLater(changes, createEntry("testfile2.txt")));
    }

    @Test
    public void testCopyStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveOutputStream outputStream = new ArchiveOutputStream("testOutput.tar.gz");
        ArchiveEntry entry = createEntry("testfile.txt");

        performer.copyStream(inputStream, outputStream, entry);

        InputStream testOutputStream = new ByteArrayInputStream(outputStream.toByteArray());
        assertEquals(inputStream.available(), testOutputStream.available());
    }

    private Change createChange(String fileName, int type, boolean replaceMode) {
        return new DefaultChange(type, fileName, replaceMode);
    }

    private ArchiveEntry createEntry(String fileName) {
        return mock(ArchiveEntry.class);
    }
}