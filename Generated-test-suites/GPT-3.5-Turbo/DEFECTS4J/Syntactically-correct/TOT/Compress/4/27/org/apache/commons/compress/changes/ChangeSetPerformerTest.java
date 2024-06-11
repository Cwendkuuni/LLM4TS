import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {
    
    private static final String TEST_FILE_NAME = "testfile.txt";
    
    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    
    @Before
    public void setUp() {
        changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
    }
    
    @After
    public void tearDown() {
        changeSet = null;
        performer = null;
    }
    
    @Test
    public void testPerform_AllChangesAddedFromChangeSet_SuccessfullyPerformed() throws IOException {
        ArchiveInputStream in = createArchiveInputStream();
        ArchiveOutputStream out = createArchiveOutputStream();
        
        // Add changes to the change set
        List<Change> changes = createChanges();
        changeSet.setChanges(changes);
        
        // Perform the changes
        ChangeSetResults results = performer.perform(in, out);
        
        // Close the streams
        in.close();
        out.close();
        
        // Verify that all changes from the change set were performed
        assertEquals(changes.size(), results.getAddedFromChangeSet().size());
        for (Change change : changes) {
            assertTrue(results.getAddedFromChangeSet().contains(change.getEntry().getName()));
        }
        
        // Verify that no changes were added from the input stream
        assertTrue(results.getAddedFromStream().isEmpty());
        
        // Verify that no deletions were performed
        assertTrue(results.getDeleted().isEmpty());
    }
    
    @Test
    public void testPerform_DeletionChangePerformed_SuccessfullyDeleted() throws IOException {
        ArchiveInputStream in = createArchiveInputStream();
        ArchiveOutputStream out = createArchiveOutputStream();
        
        // Add a deletion change to the change set
        Change change = new Change(Change.TYPE_DELETE, TEST_FILE_NAME, null, null);
        changeSet.addChange(change);
        
        // Perform the changes
        ChangeSetResults results = performer.perform(in, out);
        
        // Close the streams
        in.close();
        out.close();
        
        // Verify that the deletion change was performed
        assertEquals(1, results.getDeleted().size());
        assertTrue(results.getDeleted().contains(TEST_FILE_NAME));
        
        // Verify that no changes were added from the change set
        assertTrue(results.getAddedFromChangeSet().isEmpty());
        
        // Verify that no changes were added from the input stream
        assertTrue(results.getAddedFromStream().isEmpty());
    }
    
    @Test
    public void testPerform_DeletionDirChangePerformed_SuccessfullyDeleted() throws IOException {
        ArchiveInputStream in = createArchiveInputStream();
        ArchiveOutputStream out = createArchiveOutputStream();
        
        // Add a deletion directory change to the change set
        Change change = new Change(Change.TYPE_DELETE_DIR, TEST_FILE_NAME, null, null);
        changeSet.addChange(change);
        
        // Perform the changes
        ChangeSetResults results = performer.perform(in, out);
        
        // Close the streams
        in.close();
        out.close();
        
        // Verify that the deletion directory change was performed
        assertEquals(1, results.getDeleted().size());
        assertTrue(results.getDeleted().contains(TEST_FILE_NAME));
        
        // Verify that no changes were added from the change set
        assertTrue(results.getAddedFromChangeSet().isEmpty());
        
        // Verify that no changes were added from the input stream
        assertTrue(results.getAddedFromStream().isEmpty());
    }
    
    @Test
    public void testPerform_AdditionChangePerformed_SuccessfullyAddedFromChangeSet() throws IOException {
        ArchiveInputStream in = createArchiveInputStream();
        ArchiveOutputStream out = createArchiveOutputStream();
        
        // Add an addition change to the change set
        Change change = new Change(Change.TYPE_ADD, TEST_FILE_NAME, createInputStream(), null);
        changeSet.addChange(change);
        
        // Perform the changes
        ChangeSetResults results = performer.perform(in, out);
        
        // Close the streams
        in.close();
        out.close();
        
        // Verify that the addition change was performed
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertTrue(results.getAddedFromChangeSet().contains(TEST_FILE_NAME));
        
        // Verify that no changes were added from the input stream
        assertTrue(results.getAddedFromStream().isEmpty());
        
        // Verify that no deletions were performed
        assertTrue(results.getDeleted().isEmpty());
    }
    
    @Test
    public void testPerform_AdditionChangeWithReplaceModePerformed_SuccessfullyAddedFromChangeSet() throws IOException {
        ArchiveInputStream in = createArchiveInputStream();
        ArchiveOutputStream out = createArchiveOutputStream();
        
        // Add an addition change with replace mode to the change set
        Change change = new Change(Change.TYPE_ADD, TEST_FILE_NAME, createInputStream(), null);
        change.setReplaceMode(true);
        changeSet.addChange(change);
        
        // Perform the changes
        ChangeSetResults results = performer.perform(in, out);
        
        // Close the streams
        in.close();
        out.close();
        
        // Verify that the addition change with replace mode was performed
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertTrue(results.getAddedFromChangeSet().contains(TEST_FILE_NAME));
        
        // Verify that no changes were added from the input stream
        assertTrue(results.getAddedFromStream().isEmpty());
        
        // Verify that no deletions were performed
        assertTrue(results.getDeleted().isEmpty());
    }
    
    @Test
    public void testPerform_AdditionChangeNotAddedFromChangeSet_SuccessfullyAddedFromInputStream() throws IOException {
        ArchiveInputStream in = createArchiveInputStream();
        ArchiveOutputStream out = createArchiveOutputStream();
        
        // Add an addition change to the change set, but don't perform it
        Change change = new Change(Change.TYPE_ADD, TEST_FILE_NAME, createInputStream(), null);
        changeSet.addChange(change);
        
        // Perform the changes
        ChangeSetResults results = performer.perform(in, out);
        
        // Close the streams
        in.close();
        out.close();
        
        // Verify that no changes were added from the change set
        assertTrue(results.getAddedFromChangeSet().isEmpty());
        
        // Verify that the addition change was performed from the input stream
        assertEquals(1, results.getAddedFromStream().size());
        assertTrue(results.getAddedFromStream().contains(TEST_FILE_NAME));
        
        // Verify that no deletions were performed
        assertTrue(results.getDeleted().isEmpty());
    }
    
    @Test
    public void testPerform_AdditionChangeWithReplaceModeNotAddedFromChangeSet_SuccessfullyAddedFromInputStream() throws IOException {
        ArchiveInputStream in = createArchiveInputStream();
        ArchiveOutputStream out = createArchiveOutputStream();
        
        // Add an addition change with replace mode to the change set, but don't perform it
        Change change = new Change(Change.TYPE_ADD, TEST_FILE_NAME, createInputStream(), null);
        change.setReplaceMode(true);
        changeSet.addChange(change);
        
        // Perform the changes
        ChangeSetResults results = performer.perform(in, out);
        
        // Close the streams
        in.close();
        out.close();
        
        // Verify that no changes were added from the change set
        assertTrue(results.getAddedFromChangeSet().isEmpty());
        
        // Verify that the addition change with replace mode was performed from the input stream
        assertEquals(1, results.getAddedFromStream().size());
        assertTrue(results.getAddedFromStream().contains(TEST_FILE_NAME));
        
        // Verify that no deletions were performed
        assertTrue(results.getDeleted().isEmpty());
    }
    
    private ArchiveInputStream createArchiveInputStream() throws IOException {
        // Create a dummy input stream
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        
        // Create an archive input stream based on the dummy input stream
        ArchiveInputStream in = new ArchiveInputStream() {
            @Override
            public ArchiveEntry getNextEntry() throws IOException {
                return null;
            }
            
            @Override
            public int read(byte[] buf, int off, int len) throws IOException {
                return 0;
            }
            
            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };
        
        return in;
    }
    
    private ArchiveOutputStream createArchiveOutputStream() throws IOException {
        // Create a dummy output stream
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        // Create an archive output stream based on the dummy output stream
        ArchiveOutputStream out = new ArchiveOutputStream() {
            @Override
            public void putArchiveEntry(ArchiveEntry entry) throws IOException {
                // Do nothing
            }
            
            @Override
            public void write(byte[] buf, int off, int len) throws IOException {
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
        
        return out;
    }
    
    private ByteArrayInputStream createInputStream() {
        return new ByteArrayInputStream(new byte[0]);
    }
    
    private List<Change> createChanges() {
        List<Change> changes = new ArrayList<>();
        
        // Add a deletion change
        Change deletionChange = new Change(Change.TYPE_DELETE, TEST_FILE_NAME, null, null);
        changes.add(deletionChange);
        
        // Add an addition change
        Change additionChange = new Change(Change.TYPE_ADD, TEST_FILE_NAME, createInputStream(), null);
        changes.add(additionChange);
        
        // Add an addition change with replace mode
        Change additionChangeWithReplace = new Change(Change.TYPE_ADD, TEST_FILE_NAME, createInputStream(), null);
        additionChangeWithReplace.setReplaceMode(true);
        changes.add(additionChangeWithReplace);
        
        return changes;
    }
}