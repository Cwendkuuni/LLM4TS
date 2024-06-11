
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform_AddReplaceMode() throws IOException {
        // Create a test ChangeSet with an ADD change in replace mode
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_ADD);
        byte[] inputData = "Test Input Data".getBytes();
        change.setInput(new ByteArrayInputStream(inputData));
        change.setEntry(new ArchiveEntry("testEntry"));
        changeSet.add(change);

        // Create input and output stream
        byte[] inputStreamData = "Previous Input Data".getBytes();
        ArchiveInputStream in = new ArchiveInputStream(new ByteArrayInputStream(inputStreamData));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Perform the change
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, new ArchiveOutputStream(out));

        // Check the output stream
        out.close();
        Assert.assertEquals(new String(inputData), new String(out.toByteArray()));

        // Check the results
        Assert.assertTrue(results.hasBeenAdded("testEntry"));
        Assert.assertFalse(results.hasBeenDeleted("testEntry"));
        Assert.assertEquals(0, results.getDeletedEntries().size());
    }

    @Test
    public void testPerform_UpdateMode() throws IOException {
        // Create a test ChangeSet with an ADD change in update mode
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_ADD);
        byte[] inputData = "Test Input Data".getBytes();
        change.setInput(new ByteArrayInputStream(inputData));
        change.setEntry(new ArchiveEntry("testEntry"));
        change.setReplaceMode(false);
        changeSet.add(change);

        // Create input and output stream
        byte[] inputStreamData = "Previous Input Data".getBytes();
        ArchiveInputStream in = new ArchiveInputStream(new ByteArrayInputStream(inputStreamData));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Perform the change
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, new ArchiveOutputStream(out));

        // Check the output stream
        out.close();
        Assert.assertEquals(new String(inputStreamData) + new String(inputData), new String(out.toByteArray()));

        // Check the results
        Assert.assertTrue(results.hasBeenAdded("testEntry"));
        Assert.assertFalse(results.hasBeenDeleted("testEntry"));
        Assert.assertEquals(0, results.getDeletedEntries().size());
    }

    @Test
    public void testPerform_DeleteFile() throws IOException {
        // Create a test ChangeSet with a DELETE change for a file
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_DELETE);
        change.setTargetFile("testEntry");
        changeSet.add(change);

        // Create input and output stream
        byte[] inputStreamData = "Previous Input Data".getBytes();
        ArchiveInputStream in = new ArchiveInputStream(new ByteArrayInputStream(inputStreamData));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Perform the change
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, new ArchiveOutputStream(out));

        // Check the output stream
        out.close();
        Assert.assertEquals(new String(inputStreamData), new String(out.toByteArray()));

        // Check the results
        Assert.assertFalse(results.hasBeenAdded("testEntry"));
        Assert.assertTrue(results.hasBeenDeleted("testEntry"));
        Assert.assertEquals(1, results.getDeletedEntries().size());
        Assert.assertTrue(results.getDeletedEntries().contains("testEntry"));
    }

    @Test
    public void testPerform_DeleteDirectory() throws IOException {
        // Create a test ChangeSet with a DELETE_DIR change for a directory
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_DELETE_DIR);
        change.setTargetFile("testDir");
        changeSet.add(change);

        // Create input and output stream
        ArchiveEntry fileEntry = new ArchiveEntry("testDir/file1");
        byte[] inputData = "Test File Data 1".getBytes();
        fileEntry.setInput(new ByteArrayInputStream(inputData));

        ArchiveEntry directoryEntry = new ArchiveEntry("testDir");
        directoryEntry.setInput(new ByteArrayInputStream(new byte[0]));

        ArchiveInputStream in = new ArchiveInputStream();
        in.addEntry(fileEntry);
        in.addEntry(directoryEntry);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Perform the change
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, new ArchiveOutputStream(out));

        // Check the output stream
        out.close();
        Assert.assertEquals(0, out.toByteArray().length);

        // Check the results
        Assert.assertFalse(results.hasBeenDeleted("testDir/file1"));
        Assert.assertTrue(results.hasBeenDeleted("testDir"));
        Assert.assertEquals(2, results.getDeletedEntries().size());
        Assert.assertTrue(results.getDeletedEntries().contains("testDir/file1"));
        Assert.assertTrue(results.getDeletedEntries().contains("testDir"));
    }

    @Test
    public void testPerform_InvalidTargetFile() throws IOException {
        // Create a test ChangeSet with a DELETE change for an invalid target file
        ChangeSet changeSet = new ChangeSet();
        Change change = new Change(Change.TYPE_DELETE);
        change.setTargetFile("invalidFile");
        changeSet.add(change);

        // Create input and output stream
        ArchiveInputStream in = new ArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Perform the change
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer.perform(in, new ArchiveOutputStream(out));

        // Check the output stream
        out.close();
        Assert.assertEquals(0, out.toByteArray().length);

        // Check the results
        Assert.assertFalse(results.hasBeenDeleted("invalidFile"));
        Assert.assertEquals(0, results.getDeletedEntries().size());
    }
}

