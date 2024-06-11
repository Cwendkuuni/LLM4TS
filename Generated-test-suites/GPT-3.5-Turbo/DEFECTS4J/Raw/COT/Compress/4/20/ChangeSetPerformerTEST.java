
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform() throws Exception {
        // Test case 1: Test perform method when the ChangeSet is empty
        ChangeSet emptyChangeSet = new ChangeSet();
        ChangeSetPerformer emptyPerformer = new ChangeSetPerformer(emptyChangeSet);
        ArchiveInputStream emptyInputStream = null; // provide empty input stream
        ArchiveOutputStream emptyOutputStream = null; // provide empty output stream
        ChangeSetResults emptyPerformResults = emptyPerformer.perform(emptyInputStream, emptyOutputStream);
        assertNotNull("ChangeSetResults should not be null", emptyPerformResults);
        assertTrue("ChangeSetResults should be empty", emptyPerformResults.isEmpty());

        // Test case 2: Test perform method when there is one ADD change in the ChangeSet
        ChangeSet addChangeSet = new ChangeSet();
        Change addChange = new Change(Change.TYPE_ADD, "file1");
        String addChangeInput = "Test file content";
        addChange.setInput(new ByteArrayInputStream(addChangeInput.getBytes()));
        addChangeSet.add(addChange);
        ChangeSetPerformer addPerformer = new ChangeSetPerformer(addChangeSet);
        ArchiveInputStream addInputStream = null; // provide empty input stream
        ArchiveOutputStream addOutputStream = null; // provide empty output stream
        ChangeSetResults addPerformResults = addPerformer.perform(addInputStream, addOutputStream);
        assertNotNull("ChangeSetResults should not be null", addPerformResults);
        assertTrue("ChangeSetResults should contain 1 added entry", addPerformResults.getAddedFromChangeSet().contains("file1"));

        // Test case 3: Test perform method when there is one DELETE change in the ChangeSet
        ChangeSet deleteChangeSet = new ChangeSet();
        Change deleteChange = new Change(Change.TYPE_DELETE, "file2");
        deleteChangeSet.add(deleteChange);
        ChangeSetPerformer deletePerformer = new ChangeSetPerformer(deleteChangeSet);
        ArchiveInputStream deleteInputStream = null; // provide empty input stream
        ArchiveOutputStream deleteOutputStream = null; // provide empty output stream
        ChangeSetResults deletePerformResults = deletePerformer.perform(deleteInputStream, deleteOutputStream);
        assertNotNull("ChangeSetResults should not be null", deletePerformResults);
        assertTrue("ChangeSetResults should contain 1 deleted entry", deletePerformerResults.getDeleted().contains("file2"));

    }
}

