import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ChangeSetPerformerTest {
    
    private ChangeSetPerformer performer;
    
    @Before
    public void setUp() {
        ChangeSet changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
    }
    
    @Test
    public void testPerform() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        ArchiveInputStream archiveIn = new TarArchiveInputStream(in);
        ArchiveOutputStream archiveOut = new TarArchiveOutputStream(out);
        
        ChangeSetResults results = performer.perform(archiveIn, archiveOut);
        
        assertEquals(0, results.getDeletedEntries().size());
        assertEquals(0, results.getAddedEntriesFromChangeSet().size());
        assertEquals(0, results.getAddedEntriesFromStream().size());
        
        archiveIn.close();
        archiveOut.close();
    }
    
    @Test
    public void testPerformWithAddReplaceChange() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        ArchiveInputStream archiveIn = new ZipArchiveInputStream(in);
        ArchiveOutputStream archiveOut = new ZipArchiveOutputStream(out);
        
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Change addReplaceChange = new Change(Change.TYPE_ADD, true, entry);
        changeSet.addChange(addReplaceChange);
        
        performer = new ChangeSetPerformer(changeSet);
        
        ChangeSetResults results = performer.perform(archiveIn, archiveOut);
        
        assertEquals(1, results.getAddedEntriesFromChangeSet().size());
        assertTrue(results.getAddedEntriesFromChangeSet().contains("test.txt"));
        assertEquals(0, results.getDeletedEntries().size());
        assertEquals(0, results.getAddedEntriesFromStream().size());
        
        archiveIn.close();
        archiveOut.close();
    }
    
    @Test
    public void testPerformWithDeleteChange() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        ArchiveInputStream archiveIn = new TarArchiveInputStream(in);
        ArchiveOutputStream archiveOut = new TarArchiveOutputStream(out);
        
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, false, new TarArchiveEntry("test.txt")));
        changeSet.addChange(new Change(Change.TYPE_DELETE, false, "test.txt"));
        
        performer = new ChangeSetPerformer(changeSet);
        
        ChangeSetResults results = performer.perform(archiveIn, archiveOut);
        
        assertEquals(0, results.getAddedEntriesFromChangeSet().size());
        assertEquals(1, results.getDeletedEntries().size());
        assertTrue(results.getDeletedEntries().contains("test.txt"));
        assertEquals(0, results.getAddedEntriesFromStream().size());
        
        archiveIn.close();
        archiveOut.close();
    }
    
    @Test
    public void testPerformWithDeleteDirChange() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        ArchiveInputStream archiveIn = new ZipArchiveInputStream(in);
        ArchiveOutputStream archiveOut = new ZipArchiveOutputStream(out);
        
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, false, new ZipArchiveEntry("dir/")));
        changeSet.addChange(new Change(Change.TYPE_DELETE_DIR, false, "dir"));
        
        performer = new ChangeSetPerformer(changeSet);
        
        ChangeSetResults results = performer.perform(archiveIn, archiveOut);
        
        assertEquals(0, results.getAddedEntriesFromChangeSet().size());
        assertEquals(1, results.getDeletedEntries().size());
        assertTrue(results.getDeletedEntries().contains("dir/"));
        assertEquals(0, results.getAddedEntriesFromStream().size());
        
        archiveIn.close();
        archiveOut.close();
    }
    
    @Test
    public void testPerformWithNotDeletedEntry() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        ArchiveInputStream archiveIn = new TarArchiveInputStream(in);
        ArchiveOutputStream archiveOut = new TarArchiveOutputStream(out);
        
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, false, new TarArchiveEntry("test.txt")));
        changeSet.addChange(new Change(Change.TYPE_DELETE, false, "other.txt"));
        
        performer = new ChangeSetPerformer(changeSet);
        
        ChangeSetResults results = performer.perform(archiveIn, archiveOut);
        
        assertEquals(0, results.getAddedEntriesFromChangeSet().size());
        assertEquals(0, results.getDeletedEntries().size());
        assertEquals(1, results.getAddedEntriesFromStream().size());
        assertTrue(results.getAddedEntriesFromStream().contains("test.txt"));
        
        archiveIn.close();
        archiveOut.close();
    }
    
    @Test
    public void testPerformWithAddedNotDeletedEntry() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        ArchiveInputStream archiveIn = new ZipArchiveInputStream(in);
        ArchiveOutputStream archiveOut = new ZipArchiveOutputStream(out);
        
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, false, new ZipArchiveEntry("test.txt")));
        changeSet.addChange(new Change(Change.TYPE_DELETE, false, "test.txt"));
        changeSet.addChange(new Change(Change.TYPE_ADD, false, new ZipArchiveEntry("other.txt")));
        
        performer = new ChangeSetPerformer(changeSet);
        
        ChangeSetResults results = performer.perform(archiveIn, archiveOut);
        
        assertEquals(1, results.getAddedEntriesFromChangeSet().size());
        assertTrue(results.getAddedEntriesFromChangeSet().contains("test.txt"));
        assertEquals(1, results.getDeletedEntries().size());
        assertTrue(results.getDeletedEntries().contains("test.txt"));
        assertEquals(0, results.getAddedEntriesFromStream().size());
        
        archiveIn.close();
        archiveOut.close();
    }
    
    @Test
    public void testPerformWithAddedNotDeletedReplaceEntry() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        ArchiveInputStream archiveIn = new TarArchiveInputStream(in);
        ArchiveOutputStream archiveOut = new TarArchiveOutputStream(out);
        
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, true, new TarArchiveEntry("test.txt")));
        changeSet.addChange(new Change(Change.TYPE_DELETE, false, "test.txt"));
        changeSet.addChange(new Change(Change.TYPE_ADD, true, new TarArchiveEntry("other.txt")));
        
        performer = new ChangeSetPerformer(changeSet);
        
        ChangeSetResults results = performer.perform(archiveIn, archiveOut);
        
        assertEquals(1, results.getAddedEntriesFromChangeSet().size());
        assertTrue(results.getAddedEntriesFromChangeSet().contains("test.txt"));
        assertEquals(1, results.getDeletedEntries().size());
        assertTrue(results.getDeletedEntries().contains("test.txt"));
        assertEquals(0, results.getAddedEntriesFromStream().size());
        
        archiveIn.close();
        archiveOut.close();
    }
    
    @Test
    public void testIsDeletedLaterWithDeletedEntry() {
        Set workingSet = new LinkedHashSet();
        workingSet.add(new Change(Change.TYPE_DELETE, false, "test.txt"));
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        
        boolean result = performer.isDeletedLater(workingSet, entry);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsDeletedLaterWithNotDeletedEntry() {
        Set workingSet = new LinkedHashSet();
        workingSet.add(new Change(Change.TYPE_DELETE, false, "other.txt"));
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        
        boolean result = performer.isDeletedLater(workingSet, entry);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsDeletedLaterWithDeletedDirectory() {
        Set workingSet = new LinkedHashSet();
        workingSet.add(new Change(Change.TYPE_DELETE_DIR, false, "dir"));
        ArchiveEntry entry = new TarArchiveEntry("dir/test.txt");
        
        boolean result = performer.isDeletedLater(workingSet, entry);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsDeletedLaterWithNotDeletedDirectory() {
        Set workingSet = new LinkedHashSet();
        workingSet.add(new Change(Change.TYPE_DELETE_DIR, false, "other"));
        ArchiveEntry entry = new TarArchiveEntry("dir/test.txt");
        
        boolean result = performer.isDeletedLater(workingSet, entry);
        
        assertFalse(result);
    }
    
    @Test
    public void testCopyStream() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        ArchiveOutputStream archiveOut = new TarArchiveOutputStream(out);
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        
        performer.copyStream(in, archiveOut, entry);
        
        archiveOut.close();
        
        byte[] result = out.toByteArray();
        String resultString = new String(result);
        
        assertEquals("Test", resultString);
    }
}