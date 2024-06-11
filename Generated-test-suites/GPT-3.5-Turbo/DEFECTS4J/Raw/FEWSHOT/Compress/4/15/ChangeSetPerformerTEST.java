package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ArchiveInputStream mockArchiveInputStream;
    private ArchiveOutputStream mockArchiveOutputStream;

    @Before
    public void setup() {
        changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
        mockArchiveInputStream = new MockArchiveInputStream();
        mockArchiveOutputStream = new MockArchiveOutputStream();
    }

    @Test
    public void testPerform_AddReplace() throws Exception {
        ArchiveEntry entry = new ArchiveEntryImpl("test.txt");
        InputStream is = new ByteArrayInputStream("test".getBytes());
        Change change = new ChangeImpl(Change.TYPE_ADD, true, entry, is);
        changeSet.addChange(change);

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertTrue(results.hasBeenAdded("test.txt"));
        assertEquals("test.txt", ((MockArchiveEntry) mockArchiveOutputStream.getEntries().get(0)).getName());
        assertEquals("test", new String(((MockArchiveOutputStream) mockArchiveOutputStream).getContent()));
    }

    @Test
    public void testPerform_AddNoReplace() throws Exception {
        ArchiveEntry entry = new ArchiveEntryImpl("test.txt");
        InputStream is = new ByteArrayInputStream("test".getBytes());
        Change change = new ChangeImpl(Change.TYPE_ADD, false, entry, is);
        changeSet.addChange(change);

        ChangeSetResults results = performer.setChanges(changeSet).perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertTrue(results.hasBeenAdded("test.txt"));
        assertEquals("test.txt", ((MockArchiveEntry) mockArchiveOutputStream.getEntries().get(0)).getName());
        assertEquals("test", new String(((MockArchiveOutputStream) mockArchiveOutputStream).getContent()));
    }

    @Test
    public void testPerform_Delete() throws Exception {
        ArchiveEntry entry = new ArchiveEntryImpl("test.txt");
        InputStream is = new ByteArrayInputStream("test".getBytes());
        Change addChange = new ChangeImpl(Change.TYPE_ADD, false, entry, is);
        changeSet.addChange(addChange);

        Change deleteChange = new ChangeImpl(Change.TYPE_DELETE, false, entry);
        changeSet.addChange(deleteChange);

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertTrue(results.hasBeenDeleted("test.txt"));
    }

    @Test
    public void testPerform_DeleteDirectory() throws Exception {
        Set<ArchiveEntry> entries = new LinkedHashSet<>();
        entries.add(new ArchiveEntryImpl("test1.txt"));
        entries.add(new ArchiveEntryImpl("test2.txt"));

        InputStream is1 = new ByteArrayInputStream("test1".getBytes());
        InputStream is2 = new ByteArrayInputStream("test2".getBytes());

        for (ArchiveEntry entry : entries) {
            Change addChange = new ChangeImpl(Change.TYPE_ADD, false, entry, is1);
            changeSet.addChange(addChange);
            is1.reset();
        }

        Change deleteChange = new ChangeImpl(Change.TYPE_DELETE_DIR, false, new ArchiveEntryImpl("test"));
        changeSet.addChange(deleteChange);

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertTrue(results.hasBeenDeleted("test1.txt"));
        assertTrue(results.hasBeenDeleted("test2.txt"));
    }

    @Test
    public void testPerform_Copy() throws Exception {
        Set<ArchiveEntry> entries = new LinkedHashSet<>();
        entries.add(new ArchiveEntryImpl("test1.txt"));
        entries.add(new ArchiveEntryImpl("test2.txt"));

        InputStream is1 = new ByteArrayInputStream("test1".getBytes());
        InputStream is2 = new ByteArrayInputStream("test2".getBytes());

        for (ArchiveEntry entry : entries) {
            Change addChange = new ChangeImpl(Change.TYPE_ADD, true, entry, is1);
            changeSet.addChange(addChange);
            is1.reset();
        }

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertTrue(results.hasBeenAdded("test1.txt"));
        assertTrue(results.hasBeenAdded("test2.txt"));
    }

    @Test
    public void testPerform_DeleteAfterCopy() throws Exception {
        Set<ArchiveEntry> entries = new LinkedHashSet<>();
        entries.add(new ArchiveEntryImpl("test1.txt"));
        entries.add(new ArchiveEntryImpl("test2.txt"));

        InputStream is = new ByteArrayInputStream("test".getBytes());

        for (ArchiveEntry entry : entries) {
            Change addChange = new ChangeImpl(Change.TYPE_ADD, true, entry, is);
            changeSet.addChange(addChange);
            is.reset();
        }

        Change deleteChange = new ChangeImpl(Change.TYPE_DELETE, false, new ArchiveEntryImpl("test2.txt"));
        changeSet.addChange(deleteChange);

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertTrue(results.hasBeenAdded("test1.txt"));
        assertTrue(results.hasBeenDeleted("test2.txt"));
    }

    @Test
    public void testPerform_DeleteDirectoryAfterCopy() throws Exception {
        Set<ArchiveEntry> entries = new LinkedHashSet<>();
        entries.add(new ArchiveEntryImpl("test/test1.txt"));
        entries.add(new ArchiveEntryImpl("test/test2.txt"));

        InputStream is = new ByteArrayInputStream("test".getBytes());

        for (ArchiveEntry entry : entries) {
            Change addChange = new ChangeImpl(Change.TYPE_ADD, true, entry, is);
            changeSet.addChange(addChange);
            is.reset();
        }

        Change deleteChange = new ChangeImpl(Change.TYPE_DELETE_DIR, false, new ArchiveEntryImpl("test"));
        changeSet.addChange(deleteChange);

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertTrue(results.hasBeenDeleted("test/test1.txt"));
        assertTrue(results.hasBeenDeleted("test/test2.txt"));
    }

    private class MockArchiveInputStream extends ArchiveInputStream {

        @Override
        public ArchiveEntry getNextEntry() {
            return null;
        }

        @Override
        public int read() {
            return -1;
        }

    }

    private class MockArchiveOutputStream extends ArchiveOutputStream {

        private ByteArrayOutputStream outputStream;
        private Set<ArchiveEntry> entries;

        public MockArchiveOutputStream() {
            outputStream = new ByteArrayOutputStream();
            entries = new LinkedHashSet<>();
        }

        @Override
        public void putArchiveEntry(ArchiveEntry entry) {
            entries.add(entry);
        }

        @Override
        public void closeArchiveEntry() {
        }

        @Override
        public void write(byte[] b, int off, int len) {
            outputStream.write(b, off, len);
        }

        @Override
        public void close() {
        }

        public Set<ArchiveEntry> getEntries() {
            return entries;
        }

        public byte[] getContent() {
            return outputStream.toByteArray();
        }

    }

    private class ArchiveEntryImpl implements ArchiveEntry {

        private String name;

        public ArchiveEntryImpl(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        // implement other methods if needed

    }

    private class ChangeImpl implements Change {

        private int type;
        private boolean replaceMode;
        private ArchiveEntry entry;
        private InputStream input;

        public ChangeImpl(int type, boolean replaceMode, ArchiveEntry entry) {
            this.type = type;
            this.replaceMode = replaceMode;
            this.entry = entry;
        }

        public ChangeImpl(int type, boolean replaceMode, ArchiveEntry entry, InputStream input) {
            this.type = type;
            this.replaceMode = replaceMode;
            this.entry = entry;
            this.input = input;
        }

        @Override
        public int type() {
            return type;
        }

        @Override
        public boolean isReplaceMode() {
            return replaceMode;
        }

        @Override
        public ArchiveEntry getEntry() {
            return entry;
        }

        @Override
        public InputStream getInput() {
            return input;
        }

        // implement other methods if needed

    }

}