
package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;

    @Before
    public void setUp() throws Exception {
        ChangeSet changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_addReplaceMode() throws IOException {
        // Prepare test data
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        ArchiveOutputStream out = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new TarArchiveEntry("testFile");
        Change change = new Change(Change.TYPE_ADD, entry);
        change.setReplaceMode(true);
        change.setInput(in);
        changeSet.add(change);

        performer = new ChangeSetPerformer(changeSet);

        // Perform the operation
        ArchiveEntry result = performer.perform(
                new TarArchiveInputStream(new ByteArrayInputStream("".getBytes())),
                (TarArchiveOutputStream) out).getAddedFromChangeSet().iterator().next();

        // Verify the result
        assertEquals("testFile", result.getName());
    }

    @Test
    public void testPerform_addNotReplaceMode() throws IOException {
        // Prepare test data
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        ArchiveOutputStream out = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new TarArchiveEntry("testFile");
        Change change = new Change(Change.TYPE_ADD, entry);
        change.setReplaceMode(false);
        change.setInput(in);
        changeSet.add(change);

        performer = new ChangeSetPerformer(changeSet);

        // Perform the operation
        ArchiveEntry result = performer.perform(
                new TarArchiveInputStream(new ByteArrayInputStream("".getBytes())),
                (TarArchiveOutputStream) out).getAddedFromChangeSet().iterator().next();

        // Verify the result
        assertEquals("testFile", result.getName());
    }

    @Test
    public void testPerform_deleteFile() throws IOException {
        // Prepare test data
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        ArchiveOutputStream out = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry = new TarArchiveEntry("testFile");
        Change addChange = new Change(Change.TYPE_ADD, entry);
        addChange.setReplaceMode(false);
        addChange.setInput(in);
        changeSet.add(addChange);

        ArchiveEntry deleteEntry = new TarArchiveEntry("testFile");
        Change deleteChange = new Change(Change.TYPE_DELETE, deleteEntry);
        changeSet.add(deleteChange);

        performer = new ChangeSetPerformer(changeSet);

        // Perform the operation
        performer.perform(
                new TarArchiveInputStream(new ByteArrayInputStream("".getBytes())),
                (TarArchiveOutputStream) out);

        // Verify the result
        assertEquals(true, changeSet.getChanges().isEmpty());
    }

    @Test
    public void testPerform_deleteDirectory() throws IOException {
        // Prepare test data
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        ArchiveOutputStream out = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ChangeSet changeSet = new ChangeSet();
        ArchiveEntry entry1 = new TarArchiveEntry("testDir/testFile");
        Change addChange1 = new Change(Change.TYPE_ADD, entry1);
        addChange1.setReplaceMode(false);
        addChange1.setInput(in);
        changeSet.add(addChange1);

        ArchiveEntry entry2 = new TarArchiveEntry("testDir/testFile2");
        Change addChange2 = new Change(Change.TYPE_ADD, entry2);
        addChange2.setReplaceMode(false);
        addChange2.setInput(in);
        changeSet.add(addChange2);

        ArchiveEntry deleteEntry = new TarArchiveEntry("testDir");
        Change deleteChange = new Change(Change.TYPE_DELETE_DIR, deleteEntry);
        changeSet.add(deleteChange);

        performer = new ChangeSetPerformer(changeSet);

        // Perform the operation
        performer.perform(
                new TarArchiveInputStream(new ByteArrayInputStream("".getBytes())),
                (TarArchiveOutputStream) out);

        // Verify the result
        assertEquals(true, changeSet.getChanges().isEmpty());
    }
}
