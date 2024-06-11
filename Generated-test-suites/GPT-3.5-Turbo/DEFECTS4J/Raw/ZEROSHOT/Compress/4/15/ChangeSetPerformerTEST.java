import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform() throws IOException {
        // Set up test data
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream in = null;
        ArchiveOutputStream out = null;
        ChangeSetResults results = null;

        // Test perform method with valid input streams
        try {
            in = createMockInputStream();
            out = createMockOutputStream();
            results = performer.perform(in, out);
        } finally {
            // Close input and output streams
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }

        // Verify the generated results
        Assert.assertNotNull(results);
        Assert.assertEquals(expectedAddedFromChangeSet, results.getAddedFromChangeSet());
        Assert.assertEquals(expectedAddedFromStream, results.getAddedFromStream());
        Assert.assertEquals(expectedDeleted, results.getDeleted());
    }

    @Test
    public void testIsDeletedLater() {
        // Set up test data
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        Set<Change> workingSet = new LinkedHashSet<>();
        ArchiveEntry entry = createMockArchiveEntry();

        // Test isDeletedLater method with a workingSet that contains a deletion change
        workingSet.add(createDeletionChange());
        boolean isDeleted = performer.isDeletedLater(workingSet, entry);
        Assert.assertTrue(isDeleted);

        // Test isDeletedLater method with a workingSet that does not contain a deletion change
        workingSet.remove(createDeletionChange());
        boolean isNotDeleted = performer.isDeletedLater(workingSet, entry);
        Assert.assertFalse(isNotDeleted);
    }

    @Test
    public void testCopyStream() {
        // Set up test data
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        InputStream in = createMockInputStream();
        ArchiveOutputStream out = createMockOutputStream();
        ArchiveEntry entry = createMockArchiveEntry();

        // Test copyStream method
        try {
            performer.copyStream(in, out, entry);
        } catch (IOException e) {
            Assert.fail("IOException thrown while copying stream");
        }

        // Verify that archive entry was added and closed
        Assert.assertTrue(out.hasPutArchiveEntry());
        Assert.assertTrue(out.hasCloseArchiveEntry());
    }

    private InputStream createMockInputStream() {
        return null;  // Replace with actual implementation
    }

    private ArchiveOutputStream createMockOutputStream() {
        return null;  // Replace with actual implementation
    }

    private ArchiveEntry createMockArchiveEntry() {
        return null;  // Replace with actual implementation
    }

    private Change createDeletionChange() {
        return null;  // Replace with actual implementation
    }

    private Set<String> expectedAddedFromChangeSet = new LinkedHashSet<>();
    private Set<String> expectedAddedFromStream = new LinkedHashSet<>();
    private Set<String> expectedDeleted = new LinkedHashSet<>();

}