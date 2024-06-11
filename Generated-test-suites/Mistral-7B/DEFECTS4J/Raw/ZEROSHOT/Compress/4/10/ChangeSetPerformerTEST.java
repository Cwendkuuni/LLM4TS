package org.apache.commons.compress.changes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ArchiveInputStream inputMock;
    private ArchiveOutputStream outputMock;

    @Before
    public void setUp() throws IOException {
        changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);

        inputMock = mock(ArchiveInputStream.class);
        outputMock = mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_EmptyChangeSet() throws IOException {
        ChangeSetResults result = performer.perform(inputMock, outputMock);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPerform_AddOneFile() throws IOException {
        // given
        ArchiveEntry entryToAdd = createTestEntry("testFile.txt");
        Change changeToAdd = new Change(Change.TYPE_ADD, "testFile.txt", null, null);
        changeSet.addChange(changeToAdd);

        // when
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // then
        verify(outputMock, times(1)).putArchiveEntry(entryToAdd);
        verifyNoMoreInteractions(outputMock);
        assertEquals(1, result.size());
        assertEquals("testFile.txt", result.get(0).getName());
    }

    @Test
    public void testPerform_AddOneFileWithReplaceMode() throws IOException {
        // given
        ArchiveEntry entryToAdd = createTestEntry("testFile.txt");
        Change changeToAdd = new Change(Change.TYPE_ADD, "testFile.txt", null, null, true);
        changeSet.addChange(changeToAdd);

        // when
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // then
        verify(outputMock, times(1)).putArchiveEntry(entryToAdd);
        verifyNoMoreInteractions(outputMock);
        assertEquals(1, result.size());
        assertEquals("testFile.txt", result.get(0).getName());
    }

    @Test
    public void testPerform_DeleteOneFile() throws IOException {
        // given
        ArchiveEntry entryToDelete = createTestEntry("testFile.txt");
        Change changeToDelete = new Change(Change.TYPE_DELETE, null, "testFile.txt");
        changeSet.addChange(changeToDelete);

        // when
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // then
        verifyZeroInteractions(outputMock);
        assertEquals(1, result.size());
        assertEquals("testFile.txt", result.get(0).getName());
        assertEquals(Change.Action.DELETE, result.get(0).getAction());
    }

    @Test
    public void testPerform_DeleteOneDirectory() throws IOException {
        // given
        ArchiveEntry directoryEntryToDelete = createTestEntry("testDir");
        Change changeToDelete = new Change(Change.TYPE_DELETE_DIR, null, "testDir");
        changeSet.addChange(changeToDelete);

        // when
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // then
        verifyZeroInteractions(outputMock);
        assertEquals(1, result.size());
        assertEquals("testDir", result.get(0).getName());
        assertEquals(Change.Action.DELETE_DIR, result.get(0).getAction());
    }

    @Test
    public void testPerform_AddOneFileAndDeleteLater() throws IOException {
        // given
        ArchiveEntry entryToAdd = createTestEntry("testFile.txt");
        Change changeToAdd = new Change(Change.TYPE_ADD, "testFile.txt", null, null);
        changeSet.addChange(changeToAdd);

        ArchiveEntry entryToDelete = createTestEntry("testFile.txt");
        Change changeToDelete = new Change(Change.TYPE_DELETE, null, "testFile.txt");
        changeSet.addChange(changeToDelete);

        // when
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // then
        verifyZeroInteractions(outputMock);
        assertEquals(0, result.size());
    }

    @Test
    public void testPerform_AddOneFileAndAddLater() throws IOException {
        // given
        ArchiveEntry entryToAddFirst = createTestEntry("testFile1.txt");
        Change changeToAddFirst = new Change(Change.TYPE_ADD, "testFile1.txt", null, null);
        changeSet.addChange(changeToAddFirst);

        ArchiveEntry entryToAddSecond = createTestEntry("testFile2.txt");
        Change changeToAddSecond = new Change(Change.TYPE_ADD, "testFile2.txt", null, null);
        changeSet.addChange(changeToAddSecond);

        // when
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // then
        verify(outputMock, times(2)).putArchiveEntry(any(ArchiveEntry.class));
        assertEquals(2, result.size());
        assertEquals("testFile1.txt", result.get(0).getName());
        assertEquals("testFile2.txt", result.get(1).getName());
    }

    @Test
    public void testPerform_DeleteOneFileAndAddLater() throws IOException {
        // given
        ArchiveEntry entryToDelete = createTestEntry("testFile.txt");
        Change changeToDelete = new Change(Change.TYPE_DELETE, null, "testFile.txt");
        changeSet.addChange(changeToDelete);

        ArchiveEntry entryToAdd = createTestEntry("testFile.txt");
        Change changeToAdd = new Change(Change.TYPE_ADD, "testFile.txt", null, null);
        changeSet.addChange(changeToAdd);

        // when
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // then
        verifyZeroInteractions(outputMock);
        assertEquals(1, result.size());
        assertEquals("testFile.txt", result.get(0).getName());
        assertEquals(Change.Action.ADD, result.get(0).getAction());
    }

    @Test(expected = IllegalStateException.class)
    public void testPerform_AlreadyFinished() throws IOException {
        // given
        ChangeSetResults result = performer.perform(inputMock, outputMock);

        // when
        performer.perform(inputMock, outputMock);

        // then
        fail("Expected IllegalStateException");
    }

    @Test
    public void testIsDeletedLater() throws IOException {
        // given
        ArchiveEntry entryToAdd = createTestEntry("testFile.txt");
        Change changeToAdd = new Change(Change.TYPE_ADD, "testFile.txt", null, null);
        changeSet.addChange(changeToAdd);

        ArchiveEntry entryToDelete = createTestEntry("testFile.txt");
        Change changeToDelete = new Change(Change.TYPE_DELETE, null, "testFile.txt");
        changeSet.addChange(changeToDelete);

        // when
        boolean result = performer.isDeletedLater(changeSet.getChanges(), entryToAdd);

        // then
        assertFalse(result);

        // given
        Set<Change> changes = changeSet.getChanges();
        changes.remove(changeToAdd);

        // when
        result = performer.isDeletedLater(changes, entryToAdd);

        // then
        assertTrue(result);
    }

    @Test
    public void testCopyStream() throws IOException {
        // given
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ArchiveEntry entry = mock(ArchiveEntry.class);

        // when
        performer.copyStream(input, output, entry);

        // then
        byte[] outputBytes = output.toByteArray();
        assertArrayEquals(new byte[]{1, 2, 3}, outputBytes);
    }

    private ArchiveEntry createTestEntry(String name) {
        return new TarArchiveEntry(name);
    }

    private List<Change> createTestChangeSet() {
        List<Change> changeSet = new ArrayList<>();
        changeSet.add(new Change(Change.TYPE_ADD, null, "testFile1.txt"));
        changeSet.add(new Change(Change.TYPE_DELETE, null, "testFile1.txt"));
        changeSet.add(new Change(Change.TYPE_ADD, null, "testFile2.txt"));
        return changeSet;
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        byte[] copyOfExpected = Arrays.copyOf(expected, expected.length);
        byte[] copyOfActual = Arrays.copyOf(actual, actual.length);
        Arrays.sort(copyOfExpected);
        Arrays.sort(copyOfActual);
        assertArrayEquals(copyOfExpected, copyOfActual);
    }
}