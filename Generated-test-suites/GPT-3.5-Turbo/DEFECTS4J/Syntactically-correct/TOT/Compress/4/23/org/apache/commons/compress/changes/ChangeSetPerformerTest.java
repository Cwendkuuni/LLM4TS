import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {
    
    private ChangeSetPerformer performer;
    private ChangeSet changeSet;
    private ArchiveInputStream in;
    private ArchiveOutputStream out;
    
    @Before
    public void setup() {
        changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
        in = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        out = new ZipArchiveOutputStream(new ByteArrayOutputStream());
    }
    
    @Test
    public void testPerform_AddReplaceModeCopyStream() throws Exception {
        // Arrange
        ArchiveEntry entry = new ZipArchiveEntry("test.txt");
        changeSet.add(new Change(Change.TYPE_ADD, entry, new ByteArrayInputStream(new byte[0]), true));
        
        // Act
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert
        assertFalse(results.hasBeenAdded(entry.getName()));
    }
    
    @Test
    public void testPerform_AddReplaceModeCopyStream_ExceptionThrown() throws Exception {
        // Arrange
        ArchiveEntry entry = new ZipArchiveEntry("test.txt");
        changeSet.add(new Change(Change.TYPE_ADD, entry, new ByteArrayInputStream(new byte[0]), true));
        ArchiveInputStream faultyIn = new ZipArchiveInputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Test Exception");
            }
        });
        
        // Act & Assert
        try {
            performer.perform(faultyIn, out);
            fail("IOException not thrown");
        } catch (IOException e) {
            // Exception expected
            assertEquals("Test Exception", e.getMessage());
        }
    }
    
    @Test
    public void testPerform_Delete() throws Exception {
        // Arrange
        ArchiveEntry entry = new ZipArchiveEntry("test.txt");
        in.putArchiveEntry(entry);
        in.closeArchiveEntry();
        changeSet.add(new Change(Change.TYPE_DELETE, "test.txt"));
        
        // Act
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert
        assertTrue(results.hasBeenDeleted("test.txt"));
    }
    
    @Test
    public void testPerform_DeleteDir() throws Exception {
        // Arrange
        ArchiveEntry entry1 = new ZipArchiveEntry("dir/test1.txt");
        ArchiveEntry entry2 = new ZipArchiveEntry("dir/test2.txt");
        in.putArchiveEntry(entry1);
        in.closeArchiveEntry();
        in.putArchiveEntry(entry2);
        in.closeArchiveEntry();
        changeSet.add(new Change(Change.TYPE_DELETE_DIR, "dir"));
        
        // Act
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert
        assertTrue(results.hasBeenDeleted("dir/test1.txt"));
        assertTrue(results.hasBeenDeleted("dir/test2.txt"));
    }
    
    @Test
    public void testPerform_CopyStream() throws Exception {
        // Arrange
        ArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ArchiveEntry existingEntry = new ZipArchiveEntry("existing.txt");
        in.putArchiveEntry(existingEntry);
        in.closeArchiveEntry();
        in.putArchiveEntry(entry);
        in.closeArchiveEntry();
        
        // Act
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert
        assertTrue(results.hasBeenAdded("test.txt"));
    }
    
    @Test
    public void testPerform_CopyStream_EntryDeletedLater() throws Exception {
        // Arrange
        ArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ArchiveEntry laterDeletedEntry = new ZipArchiveEntry("laterDeleted.txt");
        in.putArchiveEntry(entry);
        in.closeArchiveEntry();
        changeSet.add(new Change(Change.TYPE_DELETE, "laterDeleted.txt"));
        
        // Act
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert
        assertTrue(results.hasBeenAdded("test.txt"));
    }
    
    @Test
    public void testPerform_CopyStream_EntryDeletedLaterOverridden() throws Exception {
        // Arrange
        ArchiveEntry entry = new ZipArchiveEntry("laterDeleted.txt");
        ArchiveEntry overriddenEntry = new ZipArchiveEntry("laterDeleted.txt");
        in.putArchiveEntry(entry);
        in.closeArchiveEntry();
        changeSet.add(new Change(Change.TYPE_DELETE, "laterDeleted.txt"));
        changeSet.add(new Change(Change.TYPE_ADD, overriddenEntry, new ByteArrayInputStream(new byte[0])));
        
        // Act
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert
        assertFalse(results.hasBeenAdded("laterDeleted.txt"));
    }
    
    @Test
    public void testPerform_CopyStream_AddExistingEntryNotReplaced() throws Exception {
        // Arrange
        ArchiveEntry entry = new ZipArchiveEntry("existing.txt");
        ArchiveEntry existingEntry = new ZipArchiveEntry("existing.txt");
        in.putArchiveEntry(existingEntry);
        in.closeArchiveEntry();
        changeSet.add(new Change(Change.TYPE_ADD, entry, new ByteArrayInputStream(new byte[0]), false));
        
        // Act
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert
        assertFalse(results.hasBeenAdded("existing.txt"));
    }
    
    @Test
    public void testPerform_CopyStream_AddExistingEntryReplaced() throws Exception {
        // Arrange
        ArchiveEntry entry = new ZipArchiveEntry("existing.txt");
        ArchiveEntry existingEntry = new ZipArchiveEntry("existing.txt");
        in.putArchiveEntry(existingEntry);
        in.closeArchiveEntry();
        changeSet.add(new Change(Change.TYPE_ADD, entry, new ByteArrayInputStream(new byte[0]), true));
        
        // Act
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert
        assertTrue(results.hasBeenAdded("existing.txt"));
    }
}