import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ArchiveInputStream input;
    private ArchiveOutputStream output;
    private ChangeSet changeSet;

    @Before
    public void setUp() {
        changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform() {
        // Test perform method
        ChangeSetResults results = performer.perform(input, output);
        assertNotNull(results);
        // Add assert statements for the results
        // ...
    }

    @Test
    public void testCopyStream() {
        // Test copyStream method
        // Set up input stream and output stream
        // ...
        // Set up ArchiveEntry
        // ...
        try {
            performer.copyStream(input, output, entry);
            // Add assert statements to verify correct copying
            // ...
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testIsDeletedLater() {
        // Test isDeletedLater method
        // Set up working set
        // ...
        // Set up ArchiveEntry
        // ...
        boolean result = performer.isDeletedLater(workingSet, entry);
        // Add assert statements to verify correct result
        // ...
    }

}