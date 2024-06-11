package org.apache.commons.compress.changes;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

    private ArchiveInputStream inputStream;
    private ArchiveOutputStream outputStream;
    private ChangeSetResults results;
    private ChangeSet changeSet;
    private Set<Change> changes;

    @Before
    public void setUp() throws Exception {
        changeSet = new ChangeSet();
        changes = new LinkedHashSet<>();

        ArchiveEntry entry1 = createTestEntry("test1.txt");
        ArchiveEntry entry2 = createTestEntry("test2.txt");
        ArchiveEntry entry3 = createTestEntry("test3.txt");

        Change addChange1 = new Change(Change.TYPE_ADD, null, null, entry1);
        Change addChange2 = new Change(Change.TYPE_ADD, null, null, entry2);
        Change addChange3 = new Change(Change.TYPE_ADD, null, null, entry3);
        Change replaceChange1 = new Change(Change.TYPE_ADD, entry1, entry1, entry1);
        replaceChange1.setReplaceMode(true);
        Change deleteChange1 = new Change(Change.TYPE_DELETE, null, null, entry1);
        Change deleteDirChange1 = new Change(Change.TYPE_DELETE_DIR, null, null, "test/");

        changes.add(addChange1);
        changes.add(addChange2);
        changes.add(addChange3);
        changes.add(replaceChange1);
        changes.add(deleteChange1);
        changes.add(deleteDirChange1);

        changeSet.setChanges(changes);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ByteArrayOutputStream outputStreamTemp = new ByteArrayOutputStream();
        this.outputStream = new ArchiveOutputStream("output.tar", null, outputStreamTemp);
        this.inputStream = new ArchiveInputStream(new ByteArrayInputStream(getTestInput().toByteArray()), null);
        results = performer.perform(inputStream, this.outputStream);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testPerform() throws IOException {
        assertEquals(3, results.getAddedFromChangeSet().size());
        assertEquals(2, results.getAddedFromStream().size());
        assertEquals(1, results.getDeleted().size());
        assertEquals(1, results.getDeletedDir().size());
    }

    @Test
    public void testIsDeletedLater() throws IOException {
        Set workingSet = new LinkedHashSet<>(changes);
        ArchiveEntry entry1 = createTestEntry("test1.txt");
        ArchiveEntry entry4 = createTestEntry("test4.txt");

        Change deleteChange1 = new Change(Change.TYPE_DELETE, null, null, entry1);
        Change addChange4 = new Change(Change.TYPE_ADD, null, null, entry4);

        workingSet.add(deleteChange1);
        workingSet.add(addChange4);

        assertTrue(ChangeSetPerformer.class.getInstance().isDeletedLater(workingSet, entry1));
        assertFalse(ChangeSetPerformer.class.getInstance().isDeletedLater(workingSet, entry4));
    }

    @Test(expected = IOException.class)
    public void testPerformIOException() throws IOException {
        ByteArrayOutputStream outputStreamTemp = new ByteArrayOutputStream();
        this.outputStream = new ArchiveOutputStream("output.tar", null, outputStreamTemp);
        ArchiveInputStream inputStreamTemp = new ArchiveInputStream(new ByteArrayInputStream(new byte[0]), null);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(inputStreamTemp, this.outputStream);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPerformUnsupportedOperationException() throws IOException {
        ByteArrayOutputStream outputStreamTemp = new ByteArrayOutputStream();
        this.outputStream = new ArchiveOutputStream("output.tar", null, outputStreamTemp);
        InputStream inputStreamTemp = new ByteArrayInputStream(new byte[0]);
        ChangeSetPerformer performer = new ChangeSetPerformer(null);
        performer.perform(inputStreamTemp, this.outputStream);
    }

    @Test
    public void testCopyStream() throws IOException {
        ByteArrayOutputStream inputStreamTempOutput = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStreamTempOutput = new ByteArrayOutputStream();
        InputStream inputStreamTemp = new ByteArrayInputStream(new byte[0]);
        ArchiveOutputStream outputStreamTemp = new ArchiveOutputStream("output.tar", null, outputStreamTempOutput);
        ArchiveEntry entry = createTestEntry("test.txt");

        ChangeSetPerformer.class.getInstance().copyStream(inputStreamTemp, outputStreamTemp, entry);

        byte[] input = inputStreamTempOutput.toByteArray();
        byte[] expectedOutput = new byte[input.length + 512]; // ArchiveOutputStream header
        byte[] output = outputStreamTempOutput.toByteArray();

        System.arraycopy(input, 0, expectedOutput, 0, input.length);
        assertArrayEquals(expectedOutput, output);
    }

    private ChangeSet getTestChangeSet() {
        return changeSet;
    }

    private InputStream getTestInput() {
        ByteArrayOutputStream outputStreamTemp = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveOutputStream("output.tar", null, outputStreamTemp);

        ArchiveEntry testEntry1 = createTestEntry("test1.txt");
        ArchiveEntry testEntry2 = createTestEntry("test2.txt");
        ArchiveEntry testEntry3 = createTestEntry("test3.txt");

        archiveOutputStream.putNextEntry(testEntry1);
        archiveOutputStream.putNextEntry(testEntry2);
        archiveOutputStream.putNextEntry(testEntry3);
        archiveOutputStream.close();

        return new ByteArrayInputStream(outputStreamTemp.toByteArray());
    }

    private ArchiveEntry createTestEntry(String name) {
        return new TestArchiveEntry(name);
    }

    private static class TestArchiveEntry implements ArchiveEntry {
        private final String name;

        public TestArchiveEntry(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getSize() {
            return 0;
        }

        @Override
        public boolean isDirectory() {
            return false;
        }

        @Override
        public String getComment() {
            return null;
        }
    }
}