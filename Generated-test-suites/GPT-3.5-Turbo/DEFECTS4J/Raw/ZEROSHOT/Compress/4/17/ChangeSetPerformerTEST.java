import static org.junit.Assert.*;
import org.junit.Test;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform() {
        // Test case 1: Perform changes on empty InputStream and empty OutputStream
        ArchiveInputStream emptyInputStream = new ArchiveInputStream(null);
        ArchiveOutputStream emptyOutputStream = new ArchiveOutputStream(null);
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        ChangeSetResults results = performer.perform(emptyInputStream, emptyOutputStream);
        assertEquals(0, results.getAddedFromChangeSet().size());
        assertEquals(0, results.getAddedFromStream().size());
        assertEquals(0, results.getDeleted().size());
        
        // Test case 2: Perform changes on non-empty InputStream and empty OutputStream
        ArchiveInputStream nonEmptyInputStream = new ArchiveInputStream("testData");
        emptyOutputStream.reset();
        performer = new ChangeSetPerformer(new ChangeSet());
        results = performer.perform(nonEmptyInputStream, emptyOutputStream);
        assertEquals(0, results.getAddedFromChangeSet().size());
        assertEquals(0, results.getAddedFromStream().size());
        assertEquals(0, results.getDeleted().size());
        
        // Test case 3: Perform changes on empty InputStream and non-empty OutputStream
        emptyInputStream.reset();
        ArchiveOutputStream nonEmptyOutputStream = new ArchiveOutputStream("outputData");
        performer = new ChangeSetPerformer(new ChangeSet());
        results = performer.perform(emptyInputStream, nonEmptyOutputStream);
        assertEquals(0, results.getAddedFromChangeSet().size());
        assertEquals(0, results.getAddedFromStream().size());
        assertEquals(0, results.getDeleted().size());
        
        // Test case 4: Perform changes on non-empty InputStream and non-empty OutputStream
        nonEmptyInputStream.reset();
        nonEmptyOutputStream.reset();
        performer = new ChangeSetPerformer(new ChangeSet());
        results = performer.perform(nonEmptyInputStream, nonEmptyOutputStream);
        assertEquals(0, results.getAddedFromChangeSet().size());
        assertEquals(0, results.getAddedFromStream().size());
        assertEquals(0, results.getDeleted().size());
    }

    @Test
    public void testIsDeletedLater() {
        // Test case 1: workingSet is empty
        Set workingSet = new LinkedHashSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        ArchiveEntry entry = new ArchiveEntry("filePath");
        boolean result = performer.isDeletedLater(workingSet, entry);
        assertFalse(result);
        
        // Test case 2: workingSet contains one deletion change for same entry
        workingSet.add(new Change(Change.TYPE_DELETE, "filePath"));
        result = performer.isDeletedLater(workingSet, entry);
        assertTrue(result);
        
        // Test case 3: workingSet contains one deletion change for different entry
        workingSet.add(new Change(Change.TYPE_DELETE, "otherPath"));
        result = performer.isDeletedLater(workingSet, entry);
        assertFalse(result);
        
        // Test case 4: workingSet contains multiple deletion changes including same entry
        workingSet.clear();
        workingSet.add(new Change(Change.TYPE_DELETE, "filePath"));
        workingSet.add(new Change(Change.TYPE_DELETE, "otherPath"));
        result = performer.isDeletedLater(workingSet, entry);
        assertTrue(result);
    }

    @Test
    public void testCopyStream() {
        // Test case 1: Copy empty InputStream to empty OutputStream
        InputStream emptyInputStream = new InputStream();
        ArchiveOutputStream emptyOutputStream = new ArchiveOutputStream(null);
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        ArchiveEntry entry = new ArchiveEntry("filePath");
        try {
            performer.copyStream(emptyInputStream, emptyOutputStream, entry);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals(0, emptyOutputStream.getData().length);
        
        // Test case 2: Copy non-empty InputStream to empty OutputStream
        InputStream nonEmptyInputStream = new InputStream("testData");
        emptyOutputStream.reset();
        try {
            performer.copyStream(nonEmptyInputStream, emptyOutputStream, entry);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("testData", emptyOutputStream.getData());

        // Test case 3: Copy empty InputStream to non-empty OutputStream
        emptyInputStream.reset();
        ArchiveOutputStream nonEmptyOutputStream = new ArchiveOutputStream("outputData");
        try {
            performer.copyStream(emptyInputStream, nonEmptyOutputStream, entry);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("outputData", nonEmptyOutputStream.getData());
        
        // Test case 4: Copy non-empty InputStream to non-empty OutputStream
        nonEmptyInputStream.reset();
        nonEmptyOutputStream.reset();
        try {
            performer.copyStream(nonEmptyInputStream, nonEmptyOutputStream, entry);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("testDataoutputData", nonEmptyOutputStream.getData());
    }

}