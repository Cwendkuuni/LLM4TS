package org.apache.commons.compress.changes;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

    @Test
    public void testEmptyChangeSet() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream input = createTestArchive();
        ArchiveOutputStream output = new ArchiveOutputStream("testOutput.tar.gz");

        ChangeSetResults results = performer.perform(input, output);
        Assert.assertFalse("Expected no changes but got " + results.size() + " changes", results.size() > 0);
    }

    @Test
    public void testSingleAddChange() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change("testAdd.txt", Change.TYPE_ADD, "testAdd.txt".getBytes(), null, "testAdd.txt".getBytes());
        changeSet.addChange(addChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream input = createTestArchive();
        ArchiveOutputStream output = new ArchiveOutputStream("testOutput.tar.gz");

        ChangeSetResults results = performer.perform(input, output);
        Assert.assertEquals("Expected one change but got " + results.size() + " changes", 1, results.size());
        Assert.assertArrayEquals("Expected testAdd.txt to be added", addChange.getTarget(), results.getEntry("testAdd.txt").getName().getBytes());
    }

    @Test
    public void testSingleDeleteChange() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change deleteChange = new Change("testDelete.txt", Change.TYPE_DELETE, null, null, "testDelete.txt".getBytes());
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream input = new ArchiveInputStream(new ByteArrayInputStream(createTestArchiveBytes()));
        ArchiveOutputStream output = new ArchiveOutputStream("testOutput.tar.gz");

        ChangeSetResults results = performer.perform(input, output);
        Assert.assertEquals("Expected one change but got " + results.size() + " changes", 1, results.size());
        Assert.assertEquals("Expected testDelete.txt to be deleted", "testDelete.txt", results.getDeleted()[0]);
    }

    @Test
    public void testAddAndDeleteChange() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change("testAdd.txt", Change.TYPE_ADD, "testAdd.txt".getBytes(), null, "testAdd.txt".getBytes());
        Change deleteChange = new Change("testAdd.txt", Change.TYPE_DELETE, null, null, "testAdd.txt".getBytes());
        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream input = createTestArchive();
        ArchiveOutputStream output = new ArchiveOutputStream("testOutput.tar.gz");

        ChangeSetResults results = performer.perform(input, output);
        Assert.assertEquals("Expected no changes but got " + results.size() + " changes", 0, results.size());
    }

    @Test
    public void testDeleteLaterChange() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change("testAdd.txt", Change.TYPE_ADD, "testAdd.txt".getBytes(), null, "testAdd.txt".getBytes());
        Change deleteChange = new Change("testAdd.txt", Change.TYPE_DELETE, null, null, "testAdd.txt".getBytes());
        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream input = new ArchiveInputStream(new ByteArrayInputStream(createTestArchiveBytes()));
        ArchiveOutputStream output = new ArchiveOutputStream("testOutput.tar.gz");

        ChangeSetResults results = performer.perform(input, output);
        Assert.assertEquals("Expected no changes but got " + results.size() + " changes", 0, results.size());
    }

    @Test
    public void testAddReplaceChange() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        Change addReplaceChange = new Change("testReplace.txt", Change.TYPE_ADD, "testReplace.txt".getBytes(), "testReplace.txt".getBytes(), "testReplace.txt".getBytes());
        changeSet.addChange(addReplaceChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream input = createTestArchive();
        ArchiveOutputStream output = new ArchiveOutputStream("testOutput.tar.gz");

        ChangeSetResults results = performer.perform(input, output);
        Assert.assertEquals("Expected one change but got " + results.size() + " changes", 1, results.size());
        Assert.assertArrayEquals("Expected testReplace.txt to be replaced", addReplaceChange.getTarget(), results.getEntry("testReplace.txt").getName().getBytes());
    }

    @Test(expected = IOException.class)
    public void testPerformThrowsIOException() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream input = new ArchiveInputStream(new ByteArrayInputStream(createTestArchiveBytes()));
        ArchiveOutputStream output = new ArchiveOutputStream("testOutput.tar.gz");

        performer.perform(input, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullChangeSet() {
        ChangeSetPerformer performer = new ChangeSetPerformer(null);
        throw new AssertionError("Should throw an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullChange() {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.addChange(null);
        throw new AssertionError("Should throw an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNameAddChange() {
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change(null, Change.TYPE_ADD, "testAdd.txt".getBytes(), null, "testAdd.txt".getBytes());
        changeSet.addChange(addChange);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        throw new AssertionError("Should throw an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNameDeleteChange() {
        ChangeSet changeSet = new ChangeSet();
        Change deleteChange = new Change("testDelete.txt", Change.TYPE_DELETE, null, null, "testDelete.txt".getBytes());
        changeSet.addChange(deleteChange);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        throw new AssertionError("Should throw an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTargetAddChange() {
        ChangeSet changeSet = new ChangeSet();
        Change addChange = new Change("testAdd.txt", Change.TYPE_ADD, null, null, null);
        changeSet.addChange(addChange);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        throw new AssertionError("Should throw an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTargetDeleteChange() {
        ChangeSet changeSet = new ChangeSet();
        Change deleteChange = new Change("testDelete.txt", Change.TYPE_DELETE, null, null, null);
        changeSet.addChange(deleteChange);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        throw new AssertionError("Should throw an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(null, null);
        throw new AssertionError("Should throw an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOutput() {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(new ArchiveInputStream(new ByteArrayInputStream(createTestArchiveBytes())), null);
        throw new AssertionError("Should throw an IllegalArgumentException");
    }

    private byte[] createTestArchiveBytes() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream aOut = new ArchiveOutputStream(outputStream, "tar.gz");
        aOut.putNextEntry(new ArchiveEntry("testAdd.txt"));
        aOut.write("testAdd.txt".getBytes(), 0, "testAdd.txt".getBytes().length);
        aOut.closeArchiveEntry();
        aOut.finish();
        return outputStream.toByteArray();
    }

    private ArchiveInputStream createTestArchive() throws IOException {
        return new ArchiveInputStream(new ByteArrayInputStream(createTestArchiveBytes()));
    }
}