import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {
    private ChangeSetPerformer performer;
    
    @Before
    public void setUp() {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, "file1.txt", "input1", false));
        changeSet.addChange(new Change(Change.TYPE_ADD, "file2.txt", "input2", true));
        changeSet.addChange(new Change(Change.TYPE_DELETE, "file3.txt", null, false));
        changeSet.addChange(new Change(Change.TYPE_DELETE_DIR, "dir1", null, false));
        
        performer = new ChangeSetPerformer(changeSet);
    }
    
    @Test
    public void testPerform() throws IOException {
        // Prepare input
        List<ArchiveEntry> inEntries = new ArrayList<>();
        inEntries.add(createArchiveEntry("file1.txt", false));
        inEntries.add(createArchiveEntry("file2.txt", true));
        inEntries.add(createArchiveEntry("file3.txt", false));
        inEntries.add(createArchiveEntry("dir1/file4.txt", false));
        inEntries.add(createArchiveEntry("dir1/file5.txt", false));
        
        // Perform operation
        ArchiveInputStream inStream = createArchiveInputStream(inEntries);
        ArchiveOutputStream outStream = createArchiveOutputStream();
        ChangeSetResults results = performer.perform(inStream, outStream);
        
        // Verify results
        assertEquals(2, results.getAddedFromChangeSet().size());
        assertTrue(results.getAddedFromChangeSet().contains("file1.txt"));
        assertTrue(results.getAddedFromChangeSet().contains("file2.txt"));
        
        assertEquals(1, results.getDeleted().size());
        assertTrue(results.getDeleted().contains("file3.txt"));
        
        assertEquals(1, results.getAddedFromStream().size());
        assertTrue(results.getAddedFromStream().contains("dir1/file4.txt"));
        
        // Verify output
        List<ArchiveEntry> outEntries = readArchiveEntries(outStream);
        assertEquals(3, outEntries.size());
        assertEquals("file1.txt", outEntries.get(0).getName());
        assertEquals("file2.txt", outEntries.get(1).getName());
        assertEquals("dir1/file4.txt", outEntries.get(2).getName());
    }
    
    private ArchiveEntry createArchiveEntry(String name, boolean isDirectory) {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName(name);
        entry.setDirectory(isDirectory);
        return entry;
    }
    
    private ArchiveInputStream createArchiveInputStream(List<ArchiveEntry> entries) {
        final Iterator<ArchiveEntry> iterator = entries.iterator();
        return new ArchiveInputStream() {
            @Override
            public ArchiveEntry getNextEntry() throws IOException {
                return iterator.hasNext() ? iterator.next() : null;
            }
            
            @Override
            public int read() throws IOException {
                return -1;
            }
            
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return -1;
            }
            
            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };
    }
    
    private ArchiveOutputStream createArchiveOutputStream() {
        return new ArchiveOutputStream() {
            @Override
            public void putArchiveEntry(ArchiveEntry entry) throws IOException {
                // Do nothing
            }
            
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
            
            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                // Do nothing
            }
            
            @Override
            public void closeArchiveEntry() throws IOException {
                // Do nothing
            }
            
            @Override
            public void finish() throws IOException {
                // Do nothing
            }
            
            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };
    }
    
    private List<ArchiveEntry> readArchiveEntries(ArchiveOutputStream outStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        outStream.finish();
        baos.flush();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ArchiveInputStream inStream = new ArchiveInputStream() {
            @Override
            public ArchiveEntry getNextEntry() throws IOException {
                return null;
            }
            
            @Override
            public int read() throws IOException {
                return -1;
            }
            
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return -1;
            }
            
            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };
        List<ArchiveEntry> entries = new ArrayList<>();
        ArchiveEntry entry = null;
        while ((entry = inStream.getNextEntry()) != null) {
            entries.add(entry);
        }
        return entries;
    }
}