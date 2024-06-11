import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class ChangeSetPerformerTest {
    @Test
    public void testPerform() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, "file1", true));
        changeSet.addChange(new Change(Change.TYPE_ADD, "file2", true));
        changeSet.addChange(new Change(Change.TYPE_DELETE, "file3", false));
        
        byte[] inputBytes = "test data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        
        ArchiveInputStream in = new MockArchiveInputStream(input);
        ArchiveOutputStream out = new MockArchiveOutputStream();
        
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, out);
        
        assertEquals("file1", results.getAddedFromChangeSet().get(0));
        assertEquals("file2", results.getAddedFromChangeSet().get(1));
        assertEquals("file3", results.getDeleted().get(0));
        
        // Ensure the output stream contains the correct data
        ByteArrayOutputStream output = ((MockArchiveOutputStream) out).getOutputStream();
        byte[] outputBytes = output.toByteArray();
        assertArrayEquals(inputBytes, outputBytes);
    }
    
    @Test
    public void testIsDeletedLater() {
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(new Change(Change.TYPE_DELETE, "file1", false));
        workingSet.add(new Change(Change.TYPE_DELETE_DIR, "dir1", false));
        
        ArchiveEntry entry1 = new MockArchiveEntry("file1");
        ArchiveEntry entry2 = new MockArchiveEntry("dir1/file2");
        ArchiveEntry entry3 = new MockArchiveEntry("file3");
        ArchiveEntry entry4 = new MockArchiveEntry("dir2/file4");
        
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        
        assertTrue(performer.isDeletedLater(workingSet, entry1));
        assertTrue(performer.isDeletedLater(workingSet, entry2));
        assertFalse(performer.isDeletedLater(workingSet, entry3));
        assertFalse(performer.isDeletedLater(workingSet, entry4));
    }
    
    @Test
    public void testCopyStream() throws IOException {
        byte[] inputBytes = "test data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ArchiveEntry entry = new MockArchiveEntry("file1");
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream out = new MockArchiveOutputStream(output);
        
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        performer.copyStream(input, out, entry);
        
        byte[] outputBytes = output.toByteArray();
        assertArrayEquals(inputBytes, outputBytes);
    }
    
    private class MockArchiveInputStream implements ArchiveInputStream {
        private InputStream input;
        
        public MockArchiveInputStream(InputStream input) {
            this.input = input;
        }
        
        @Override
        public ArchiveEntry getNextEntry() {
            return null;
        }
    
        @Override
        public int read() {
            return 0;
        }
    }
    
    private class MockArchiveOutputStream implements ArchiveOutputStream {
        private ByteArrayOutputStream output;
        
        public MockArchiveOutputStream() {
            output = new ByteArrayOutputStream();
        }
        
        public MockArchiveOutputStream(ByteArrayOutputStream output) {
            this.output = output;
        }
        
        @Override
        public void putArchiveEntry(ArchiveEntry entry) throws IOException {
            
        }
    
        @Override
        public void closeArchiveEntry() throws IOException {
            
        }
    
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            
        }
    
        @Override
        public void close() throws IOException {
            
        }
        
        public ByteArrayOutputStream getOutputStream() {
            return output;
        }
    }
    
    private class MockArchiveEntry implements ArchiveEntry {
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
            return 0;
        }
    }
}