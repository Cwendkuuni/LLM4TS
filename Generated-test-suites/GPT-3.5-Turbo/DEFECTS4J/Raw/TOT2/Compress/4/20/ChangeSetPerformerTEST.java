import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ChangeSet changeSet;

    @Before
    public void setUp() {
        changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_AddReplaceEntry() throws IOException {
        // Prepare data
        ByteArrayInputStream input = new ByteArrayInputStream("Test data".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("test", input);
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("test", output);

        Change addChange = new Change("example.zip", Change.TYPE_ADD, true);
        addChange.setInput(input);
        addChange.setEntry(new ArchiveEntry("entry1"));
        changeSet.add(addChange);

        // Perform change
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Check results
        assertTrue(results.hasBeenAdded("entry1"));
    }

    @Test
    public void testPerform_DeleteEntry() throws IOException {
        // Prepare data
        ByteArrayInputStream input = new ByteArrayInputStream("Test data".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("test", input);
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("test", output);

        Change deleteChange = new Change("example.zip", Change.TYPE_DELETE);
        deleteChange.setTargetFile("test_entry");
        changeSet.add(deleteChange);

        // Perform change
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Check results
        assertTrue(results.getDeleted().contains("test_entry"));
    }

    @Test
    public void testPerform_AddWithReplaceMode() throws IOException {
        // Prepare data
        ByteArrayInputStream input1 = new ByteArrayInputStream("Data 1".getBytes());
        ByteArrayInputStream input2 = new ByteArrayInputStream("Data 2".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("test", input1);
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("test", output);

        Change addChange1 = new Change("example.zip", Change.TYPE_ADD, true);
        addChange1.setInput(input1);
        addChange1.setEntry(new ArchiveEntry("entry1"));
        changeSet.add(addChange1);

        Change addChange2 = new Change("example.zip", Change.TYPE_ADD, true);
        addChange2.setInput(input2);
        addChange2.setEntry(new ArchiveEntry("entry1"));
        changeSet.add(addChange2);

        // Perform change
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Check results
        assertTrue(results.hasBeenAdded("entry1"));
    }

    // Add more test cases for edge cases and error scenarios as needed

}