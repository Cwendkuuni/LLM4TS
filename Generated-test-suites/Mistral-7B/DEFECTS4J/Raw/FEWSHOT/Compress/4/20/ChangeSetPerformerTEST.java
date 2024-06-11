package org.apache.commons.compress.changes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ArchiveEntry addEntry;
    private ArchiveEntry deleteEntry;
    private ArchiveEntry addAndDeleteEntry;
    private ArchiveEntry addAndDeleteDirEntry;
    private ArchiveInputStream inputStreamMock;
    private ArchiveOutputStream outputStreamMock;

    @Before
    public void setup() {
        changeSet = new ChangeSet();

        addEntry = new MockArchiveEntry("addEntry");
        deleteEntry = new MockArchiveEntry("deleteEntry");
        addAndDeleteEntry = new MockArchiveEntry("addAndDeleteEntry");
        addAndDeleteDirEntry = new MockArchiveEntry("addAndDeleteDirEntry/subEntry");

        List<Change> changes = new ArrayList<>();
        changes.add(new AddChange("addEntry", addEntry, addEntry.getName().getBytes()));
        changes.add(new DeleteChange("deleteEntry", deleteEntry.getName().getBytes()));
        changes.add(new AddDeleteChange("addAndDeleteEntry", addAndDeleteEntry, deleteEntry.getName().getBytes()));
        changes.add(new AddDeleteDirChange("addAndDeleteDirEntry", addAndDeleteDirEntry.getName().getBytes()));
        changeSet.setChanges(changes);

        performer = new ChangeSetPerformer(changeSet);

        inputStreamMock = mock(ArchiveInputStream.class);
        outputStreamMock = mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_addEntry() throws IOException {
        when(inputStreamMock.getNextEntry()).thenReturn(addEntry);
        when(inputStreamMock.available()).thenReturn(addEntry.getSize());

        InputStream inputStream = inputStreamMock;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = outputStreamMock;

        ChangeSetResults result = performer.perform(inputStream, archiveOutputStream);

        assertTrue(result.hasBeenAdded(addEntry.getName()));
        assertEquals(addEntry.getSize(), result.getSize());
        verifyAddEntry(addEntry, result);
    }

    @Test
    public void testPerform_deleteEntry() throws IOException {
        when(inputStreamMock.getNextEntry()).thenReturn(deleteEntry);

        InputStream inputStream = inputStreamMock;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = outputStreamMock;

        ChangeSetResults result = performer.perform(inputStream, archiveOutputStream);

        assertTrue(result.isDeleted(deleteEntry.getName()));
        verifyDeleteEntry(deleteEntry, result);
    }

    @Test
    public void testPerform_addAndDeleteEntry() throws IOException {
        when(inputStreamMock.getNextEntry()).thenReturn(addAndDeleteEntry).thenReturn(deleteEntry);

        InputStream inputStream = inputStreamMock;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = outputStreamMock;

        ChangeSetResults result = performer.perform(inputStream, archiveOutputStream);

        assertTrue(result.isDeleted(addAndDeleteEntry.getName()));
        assertFalse(result.hasBeenAdded(addAndDeleteEntry.getName()));
        verifyAddAndDeleteEntry(addAndDeleteEntry, result);
    }

    @Test
    public void testPerform_addAndDeleteDirEntry() throws IOException {
        when(inputStreamMock.getNextEntry()).thenReturn(addAndDeleteDirEntry);

        InputStream inputStream = inputStreamMock;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = outputStreamMock;

        ChangeSetResults result = performer.perform(inputStream, archiveOutputStream);

        assertTrue(result.isDeleted(addAndDeleteDirEntry.getName()));
        assertFalse(result.hasBeenAdded(addAndDeleteDirEntry.getName()));
        verifyAddAndDeleteDirEntry(addAndDeleteDirEntry, result);
    }

    @Test
    public void testPerform_addEntryAlreadyAdded() throws IOException {
        when(inputStreamMock.getNextEntry()).thenReturn(addEntry);
        when(inputStreamMock.available()).thenReturn(addEntry.getSize());

        InputStream inputStream = inputStreamMock;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = outputStreamMock;

        ChangeSetResults result = performer.perform(inputStream, archiveOutputStream);

        assertTrue(result.hasBeenAdded(addEntry.getName()));
        assertEquals(addEntry.getSize(), result.getSize());
        verifyAddEntry(addEntry, result);

        when(inputStreamMock.getNextEntry()).thenReturn(addEntry);
        when(inputStreamMock.available()).thenReturn(addEntry.getSize());

        ChangeSetResults result2 = performer.perform(inputStream, archiveOutputStream);

        assertTrue(result2.hasBeenAdded(addEntry.getName()));
        assertEquals(addEntry.getSize(), result2.getSize());
        verifyAddEntry(addEntry, result2);
    }

    @Test(expected = IllegalStateException.class)
    public void testPerform_entryAlreadyDeleted() throws IOException {
        when(inputStreamMock.getNextEntry()).thenReturn(deleteEntry);

        InputStream inputStream = inputStreamMock;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = outputStreamMock;

        performer.perform(inputStream, archiveOutputStream);

        when(inputStreamMock.getNextEntry()).thenReturn(deleteEntry);

        ChangeSetResults result = performer.perform(inputStream, archiveOutputStream);

        assertFalse(result.hasBeenAdded(deleteEntry.getName()));
        assertTrue(result.isDeleted(deleteEntry.getName()));
    }

    @Test(expected = IllegalStateException.class)
    public void testPerform_entryAlreadyAddedAndDeleted() throws IOException {
        when(inputStreamMock.getNextEntry()).thenReturn(addAndDeleteEntry).thenReturn(deleteEntry);

        InputStream inputStream = inputStreamMock;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = outputStreamMock;

        performer.perform(inputStream, archiveOutputStream);

        ChangeSetResults result = performer.perform(inputStream, archiveOutputStream);

        assertFalse(result.hasBeenAdded(addAndDeleteEntry.getName()));
        assertTrue(result.isDeleted(addAndDeleteEntry.getName()));
    }

    @Test
    public void testPerform_noChangeSetEntries() throws IOException {
        when(inputStreamMock.getNextEntry()).thenReturn(null);

        InputStream inputStream = inputStreamMock;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = outputStreamMock;

        ChangeSetResults result = performer.perform(inputStream, archiveOutputStream);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testPerform_noStreamEntries() throws IOException {
        when(inputStreamMock.getNextEntry()).thenReturn(null);

        InputStream inputStream = inputStreamMock;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = outputStreamMock;

        ChangeSetResults result = performer.perform(inputStream, archiveOutputStream);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testPerform_noChanges() {
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());

        assertEquals(ChangeSetResults.EMPTY, performer.perform(null, null));
    }

    @Test
    public void testIsDeletedLater() {
        Set set = new LinkedHashSet<>();
        set.add(new AddChange("add1", addEntry, addEntry.getName().getBytes()));
        set.add(new DeleteChange("delete1", deleteEntry.getName().getBytes()));
        set.add(new AddChange("add2", addEntry, addEntry.getName().getBytes()));
        set.add(new DeleteChange("delete2", deleteEntry.getName().getBytes()));

        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(set));

        assertTrue(performer.isDeletedLater(set, addEntry));
        assertFalse(performer.isDeletedLater(set, deleteEntry));
        assertFalse(performer.isDeletedLater(set, addAndDeleteEntry));
        assertFalse(performer.isDeletedLater(set, addAndDeleteDirEntry));

        Set set2 = new LinkedHashSet<>(set);
        set2.remove(new DeleteChange("delete1", deleteEntry.getName().getBytes()));

        ChangeSetPerformer performer2 = new ChangeSetPerformer(new ChangeSet(set2));

        assertFalse(performer2.isDeletedLater(set, addEntry));
        assertFalse(performer2.isDeletedLater(set, deleteEntry));
        assertFalse(performer2.isDeletedLater(set, addAndDeleteEntry));
        assertFalse(performer2.isDeletedLater(set, addAndDeleteDirEntry));
        assertTrue(performer2.isDeletedLater(set2, addEntry));
    }

    private void verifyAddEntry(ArchiveEntry addEntry, ChangeSetResults result) {
        ArgumentCaptor<ArchiveEntry> archiveEntryCaptor = ArgumentCaptor.forClass(ArchiveEntry.class);
        verify(outputStreamMock).putArchiveEntry(archiveEntryCaptor.capture());

        ArchiveEntry copiedEntry = archiveEntryCaptor.getValue();
        assertEquals(addEntry.getName(), copiedEntry.getName());
    }

    private void verifyDeleteEntry(ArchiveEntry deleteEntry, ChangeSetResults result) {
        verifyNoMoreInteractions(outputStreamMock);
    }

    private void verifyAddAndDeleteEntry(ArchiveEntry addAndDeleteEntry, ChangeSetResults result) {
        verifyNoInteractions(outputStreamMock);
        assertTrue(result.isDeleted(addAndDeleteEntry.getName()));
    }

    private void verifyAddAndDeleteDirEntry(ArchiveEntry addAndDeleteDirEntry, ChangeSetResults result) {
        verifyNoInteractions(outputStreamMock);
        assertTrue(result.isDeleted(addAndDeleteDirEntry.getName()));
    }

    private static class MockArchiveEntry implements ArchiveEntry {
        private final String name;

        public MockArchiveEntry(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getSize() {
            return TestUtils.SIZE_ONE_BYTE;
        }

        @Override
        public boolean isDirectory() {
            return this instanceof AddDeleteDirChange;
        }
    }
}