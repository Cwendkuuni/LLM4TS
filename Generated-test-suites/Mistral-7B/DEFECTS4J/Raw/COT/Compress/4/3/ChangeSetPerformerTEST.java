package org.apache.commons.compress.changes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    /**
     * Test method for {@link ChangeSetPerformer#ChangeSetPerformer(ChangeSet)}
     */
    @org.junit.Test
    public void testChangeSetPerformerConstruction() {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        assertNotNull(performer);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     */
    @org.junit.Test
    public void testPerform_ValidArguments() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03 };
        byte[] expectedOutputData = new byte[] { 0x04, 0x05, 0x06 };

        Change addChange = new Change(Change.TYPE_ADD, "test.txt", null, new ByteArrayInputStream(inputData), null);
        changeSet.addChange(addChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = new MockArchiveInputStream();
        ArchiveOutputStream out = new MockArchiveOutputStream();

        performer.perform(in, out);

        byte[] actualOutputData = IOUtils.toByteArray(out.getOutputStream());

        assertArrayEquals(expectedOutputData, actualOutputData);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * with duplicate file names
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testPerform_DuplicateFileName() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        Change addChange1 = new Change(Change.TYPE_ADD, "test.txt", null, null, null);
        changeSet.addChange(addChange1);

        Change addChange2 = new Change(Change.TYPE_ADD, "test.txt", null, null, null);
        changeSet.addChange(addChange2);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = new MockArchiveInputStream();
        ArchiveOutputStream out = new MockArchiveOutputStream();

        performer.perform(in, out);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * with missing input streams
     */
    @org.junit.Test(expected = IOException.class)
    public void testPerform_MissingInputStream() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        Change addChange = new Change(Change.TYPE_ADD, "test.txt", null, null, null);
        changeSet.addChange(addChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = new MockArchiveInputStream();
        ArchiveOutputStream out = new MockArchiveOutputStream();

        performer.perform(in, out);

        Change deleteChange = new Change(Change.TYPE_DELETE, "test.txt", null, null, null);
        changeSet.addChange(deleteChange);

        performer.perform(in, out);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * with missing target entries
     */
    @org.junit.Test(expected = IOException.class)
    public void testPerform_MissingTargetEntry() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        byte[] inputData = new byte[] { 0x01, 0x02, 0x03 };

        Change addChange = new Change(Change.TYPE_ADD, "test.txt", null, new ByteArrayInputStream(inputData), null);
        changeSet.addChange(addChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = new MockArchiveInputStream();
        ArchiveOutputStream out = new MockArchiveOutputStream();

        performer.perform(in, out);

        Change deleteChange = new Change(Change.TYPE_DELETE, "test.txt", null, null, null);
        changeSet.addChange(deleteChange);

        performer.perform(in, out);
    }

    /**
     * Test method for {@link ChangeSetPerformer#perform(ArchiveInputStream, ArchiveOutputStream)}
     * with missing output stream
     */
    @org.junit.Test(expected = IOException.class)
    public void testPerform_MissingOutputStream() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        byte[] inputData = new byte[] { 0x01, 0x02, 0x03 };

        Change addChange = new Change(Change.TYPE_ADD, "test.txt", null, new ByteArrayInputStream(inputData), null);
        changeSet.addChange(addChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = new MockArchiveInputStream();
        ArchiveOutputStream out = new MockArchiveOutputStream();

        performer.perform(in, out);

        Change deleteChange = new Change(Change.TYPE_DELETE, "test.txt", null, null, null);
        changeSet.addChange(deleteChange);

        performer.perform(in, null);
    }

    /**
     * Test method for {@link ChangeSetPerformer#isDeletedLater(Set, ArchiveEntry)}
     */
    @org.junit.Test
    public void testIsDeletedLater_NoLaterDeletion() {
        Set changes = new HashSet();
        Change addChange = new Change(Change.TYPE_ADD, "test.txt", null, null, null);
        changes.add(addChange);

        ArchiveEntry entry = new MockArchiveEntry("test.txt");

        assertTrue(ChangeSetPerformer.isDeletedLater(changes, entry));
    }

    /**
     * Test method for {@link ChangeSetPerformer#isDeletedLater(Set, ArchiveEntry)}
     */
    @org.junit.Test
    public void testIsDeletedLater_LaterDeletion() {
        Set changes = new HashSet();
        Change addChange = new Change(Change.TYPE_ADD, "test.txt", null, null, null);
        changes.add(addChange);

        Change deleteChange = new Change(Change.TYPE_DELETE, "test.txt", null, null, null);
        changes.add(deleteChange);

        ArchiveEntry entry = new MockArchiveEntry("test.txt");

        assertFalse(ChangeSetPerformer.isDeletedLater(changes, entry));
    }

    /**
     * Test method for {@link ChangeSetPerformer#isDeletedLater(Set, ArchiveEntry)}
     */
    @org.junit.Test
    public void testIsDeletedLater_LaterDeletionDirectory() {
        Set changes = new HashSet();
        Change addChange = new Change(Change.TYPE_ADD, "testdir/test.txt", null, null, null);
        changes.add(addChange);

        Change deleteChange = new Change(Change.TYPE_DELETE_DIR, "testdir", null, null, null);
        changes.add(deleteChange);

        ArchiveEntry entry = new MockArchiveEntry("testdir/test.txt");

        assertFalse(ChangeSetPerformer.isDeletedLater(changes, entry));
    }

    /**
     * Test method for {@link ChangeSetPerformer#isDeletedLater(Set, ArchiveEntry)}
     */
    @org.junit.Test
    public void testIsDeletedLater_LaterDeletionDirectoryEntry() {
        Set changes = new HashSet();
        Change addChange = new Change(Change.TYPE_ADD, "test.txt", null, null, null);
        changes.add(addChange);

        Change deleteChange = new Change(Change.TYPE_DELETE_DIR, "testdir", null, null, null);
        changes.add(deleteChange);

        ArchiveEntry entry = new MockArchiveEntry("testdir/test.txt");

        assertTrue(ChangeSetPerformer.isDeletedLater(changes, entry));
    }

    /**
     * Test method for {@link ChangeSetPerformer#copyStream(InputStream, ArchiveOutputStream, ArchiveEntry)}
     */
    @org.junit.Test
    public void testCopyStream() throws IOException {
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03 };
        byte[] expectedOutputData = new byte[] { 0x04, 0x05, 0x06 };

        InputStream inputStream = new ByteArrayInputStream(inputData);
        ArchiveOutputStream outputStream = new MockArchiveOutputStream();
        ArchiveEntry entry = new MockArchiveEntry("test.txt");

        ChangeSetPerformer.copyStream(inputStream, outputStream, entry);

        byte[] actualOutputData = IOUtils.toByteArray(outputStream.getOutputStream());

        assertArrayEquals(expectedOutputData, actualOutputData);
    }

    /**
     * Test method for {@link ChangeSetPerformer#copyStream(InputStream, ArchiveOutputStream, ArchiveEntry)}
     * with empty input stream
     */
    @org.junit.Test
    public void testCopyStream_EmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveOutputStream outputStream = new MockArchiveOutputStream();
        ArchiveEntry entry = new MockArchiveEntry("test.txt");

        ChangeSetPerformer.copyStream(inputStream, outputStream, entry);

        assertTrue(outputStream.getOutputStream().close());
    }

    /**
     * Test class for MockArchiveInputStream
     */
    public static class MockArchiveInputStream implements ArchiveInputStream {

        private int nextEntryIndex = 0;
        private ArchiveEntry[] entries = { new MockArchiveEntry("test.txt"), new MockArchiveEntry("test2.txt") };

        @Override
        public ArchiveEntry getNextEntry() throws IOException {
            if (nextEntryIndex >= entries.length) {
                return null;
            }
            return entries[nextEntryIndex++];
        }

        @Override
        public void closeEntry() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }
    }

    /**
     * Test class for MockArchiveOutputStream
     */
    public static class MockArchiveOutputStream implements ArchiveOutputStream {

        private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        @Override
        public void putNextEntry(ArchiveEntry entry) throws IOException {
            entry.writeTo(outputStream);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            outputStream.write(b, off, len);
        }

        @Override
        public void closeArchiveEntry() throws IOException {
        }

        @Override
        public void close() throws IOException {
            outputStream.close();
        }

        @Override
        public ArchiveEntry getNextEntry() throws IOException {
            return null;
        }

        @Override
        public void setLevel(int level) {
        }

        public ArchiveOutputStream getOutputStream() {
            return outputStream;
        }
    }

    /**
     * Test class for MockArchiveEntry
     */
    public static class MockArchiveEntry implements ArchiveEntry {

        private String name;

        public MockArchiveEntry(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getSize() {
            return 1;
        }

        @Override
        public long getCRC() {
            return 0;
        }

        @Override
        public String getMethod() {
            return "";
        }
    }
}

class ChangeSetResults {

    private List addedFromChangeSet = new ArrayList();
    private List addedFromStream = new ArrayList();

    public void addedFromChangeSet(String name) {
        addedFromChangeSet.add(name);
    }

    public void addedFromStream(String name) {
        addedFromStream.add(name);
    }

    public List getAddedFromChangeSet() {
        return addedFromChangeSet;
    }

    public List getAddedFromStream() {
        return addedFromStream;
    }

    public boolean hasBeenAdded(String name) {
        return addedFromChangeSet.contains(name) || addedFromStream.contains(name);
    }

    public void deleted(String name) {
    }
}