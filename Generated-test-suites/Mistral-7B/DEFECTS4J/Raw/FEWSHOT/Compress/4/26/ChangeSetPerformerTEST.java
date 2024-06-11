package org.apache.commons.compress.changes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class ChangeSetPerformerTest {

    @Test
    public void testNewInstance() {
        ChangeSet changeSet = mockChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        assertNotNull(performer);
    }

    @Test
    public void testEmptySet() throws IOException {
        ChangeSet changeSet = mockChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        perform(performer, mockInputStream(), mockOutputStream());

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) mockOutputStream();
        byte[] expectedOutput = new byte[0];
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAllAdditions() throws IOException {
        ChangeSet changeSet = createChangeSetWithAdditions();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        perform(performer, mockInputStream(), mockOutputStream());

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) mockOutputStream();
        byte[] expectedOutput = createExpectedBytesForAdditions();
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAllDeletions() throws IOException {
        ChangeSet changeSet = createChangeSetWithDeletions();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        perform(performer, mockInputStream(), mockOutputStream());

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) mockOutputStream();
        byte[] expectedOutput = new byte[0];
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMixedAdditionsAndDeletions() throws IOException {
        ChangeSet changeSet = createChangeSetWithMixedAdditionsAndDeletions();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        perform(performer, mockInputStream(), mockOutputStream());

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) mockOutputStream();
        byte[] expectedOutput = createExpectedBytesForMixedAdditionsAndDeletions();
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAdditionWithReplaceMode() throws IOException {
        ChangeSet changeSet = createChangeSetWithAdditionWithReplaceMode();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        perform(performer, mockInputStream(), mockOutputStream());

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) mockOutputStream();
        byte[] expectedOutput = createExpectedBytesForAdditionWithReplaceMode();
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAdditionNotWithReplaceMode() throws IOException {
        ChangeSet changeSet = createChangeSetWithAdditionNotWithReplaceMode();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        perform(performer, mockInputStream(), mockOutputStream());

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) mockOutputStream();
        byte[] expectedOutput = createExpectedBytesForAdditionNotWithReplaceMode();
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDeletedLater() throws IOException {
        ChangeSet changeSet = createChangeSetWithDeletedLater();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        perform(performer, mockInputStream(), mockOutputStream());

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) mockOutputStream();
        byte[] expectedOutput = new byte[0];
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    private void perform(ChangeSetPerformer performer, InputStream inputStream, OutputStream outputStream)
            throws IOException {
        ArchiveInputStream in = mockArchiveInputStream(inputStream);
        ArchiveOutputStream out = mockArchiveOutputStream(outputStream);

        ChangeSetResults results = performer.perform(in, out);
        results.assertResults();
    }

    private ChangeSet mockChangeSet() {
        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(Collections.emptySet());
        return changeSet;
    }

    private ArchiveInputStream mockArchiveInputStream(InputStream inputStream) {
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        when(in.getNextEntry()).thenReturn(mockArchiveEntry());
        when(in.close()).thenReturn(true);
        when(in.readAllBytes()).thenReturn(new byte[0]);
        when(in.available()).thenReturn(inputStream.available());
        when(in.isClosed()).thenReturn(false);
        return in;
    }

    private ArchiveEntry mockArchiveEntry() {
        ArchiveEntry entry = mock(ArchiveEntry.class);
        return entry;
    }

    private OutputStream mockOutputStream() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        return outputStream;
    }

    private InputStream mockInputStream() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        return inputStream;
    }

    private ChangeSet createChangeSetWithAdditions() {
        Set changes = new HashSet();
        changes.add(createChangeWithAddition("file1.txt", "file1.txt", null));
        changes.add(createChangeWithAddition("file2.txt", "file2.txt", null));

        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(changes);
        return changeSet;
    }

    private byte[] createExpectedBytesForAdditions() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7 };
    }

    private Change createChangeWithAddition(String sourceFileName, String targetFileName, String targetDirectory) {
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.targetFile()).thenReturn(targetFileName);
        when(change.input()).thenReturn(mockInputStreamWithContent("source content".getBytes()));
        when(change.entry()).thenReturn(mockArchiveEntryWithName(targetFileName, targetDirectory));
        return change;
    }

    private ArchiveEntry mockArchiveEntryWithName(String name, String directory) {
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn(name);
        when(entry.getPathName()).thenReturn("/" + directory + "/" + name);
        return entry;
    }

    private ChangeSet createChangeSetWithDeletions() {
        Set changes = new HashSet();
        changes.add(createChangeWithDeletion("file1.txt", null));
        changes.add(createChangeWithDeletion("file2.txt", null));

        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(changes);
        return changeSet;
    }

    private Change createChangeWithDeletion(String fileName, String directory) {
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn(fileName);
        return change;
    }

    private ChangeSet createChangeSetWithMixedAdditionsAndDeletions() {
        Set changes = new HashSet();
        changes.add(createChangeWithAddition("file1.txt", "file1.txt", null));
        changes.add(createChangeWithDeletion("file2.txt", null));

        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(changes);
        return changeSet;
    }

    private byte[] createExpectedBytesForMixedAdditionsAndDeletions() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
    }

    private Change createChangeWithAdditionNotWithReplaceMode() {
        Change change = createChangeWithAddition("file1.txt", "file1.txt", null);
        when(change.isReplaceMode()).thenReturn(false);
        return change;
    }

    private byte[] createExpectedBytesForAdditionNotWithReplaceMode() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    }

    private Change createChangeWithAdditionWithReplaceMode() {
        Change change = createChangeWithAddition("file1.txt", "file1.txt", null);
        when(change.isReplaceMode()).thenReturn(true);
        return change;
    }

    private byte[] createExpectedBytesForAdditionWithReplaceMode() {
        return new byte[] { 2, 3, 4, 5, 6, 7 };
    }

    private Change createChangeWithDeletedLater() {
        Change change1 = createChangeWithDeletion("file1.txt", null);
        Change change2 = createChangeWithAddition("file1.txt", "file1.txt", null);
        Set changes = new HashSet(Arrays.asList(change1, change2));

        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(changes);
        return change1;
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError("Expected: " + Arrays.toString(expected) +
                    " but was: " + Arrays.toString(actual));
        }
    }

    private InputStream mockInputStreamWithContent(byte[] content) {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.readAllBytes()).thenReturn(content);
        when(inputStream.available()).thenReturn(content.length);
        return inputStream;
    }
}

class ChangeSetResults {
    private Set addedFromChangeSet = new HashSet();
    private Set addedFromStream = new HashSet();
    private Set deleted = new HashSet();

    public void assertResults() {
        assertTrue(addedFromChangeSet.isEmpty() || addedFromStream.isEmpty());
        assertTrue(addedFromChangeSet.isEmpty() || !deleted.isEmpty());
        assertTrue(addedFromStream.isEmpty() || !deleted.isEmpty());
        assertTrue(addedFromChangeSet.size() + addedFromStream.size() + deleted.size() > 0);
    }

    public void addedFromChangeSet(String name) {
        addedFromChangeSet.add(name);
    }

    public void addedFromStream(String name) {
        addedFromStream.add(name);
    }

    public void deleted(String name) {
        deleted.add(name);
    }

    public boolean hasBeenAdded(String name) {
        return addedFromChangeSet.contains(name) || addedFromStream.contains(name);
    }
}