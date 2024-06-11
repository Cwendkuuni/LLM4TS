package org.apache.commons.compress.changes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private Set<Change> changeSet;
    private ChangeSetPerformer performer;

    @Before
    public void setUp() {
        changeSet = new LinkedHashSet<>();
        InputStream input = new ByteArrayInputStream(TestUtils.getTestData("testdata.bin"));
        ArchiveInputStream archiveInput = new ArchiveInputStream(input);
        ArchiveEntry entry1 = archiveInput.getNextEntry();
        Change add1 = new Change(Change.TYPE_ADD, null, null, null, null, null, null, false);
        add1.setEntry(entry1);
        changeSet.add(add1);

        ArchiveEntry entry2 = archiveInput.getNextEntry();
        Change add2 = new Change(Change.TYPE_ADD, null, null, null, null, null, null, false);
        add2.setEntry(entry2);
        changeSet.add(add2);

        ArchiveEntry entry3 = archiveInput.getNextEntry();
        Change delete1 = new Change(Change.TYPE_DELETE, null, null, null, null, null, null, false);
        delete1.setTargetFile("/dir/testfile.txt");
        changeSet.add(delete1);

        ArchiveEntry entry4 = archiveInput.getNextEntry();
        Change delete2 = new Change(Change.TYPE_DELETE_DIR, null, null, null, null, null, null, false);
        delete2.setTargetFile("dir");
        changeSet.add(delete2);

        ArchiveEntry entry5 = archiveInput.getNextEntry();
        Change add3 = new Change(Change.TYPE_ADD, null, null, null, null, null, null, false);
        add3.setEntry(entry5);
        changeSet.add(add3);

        performer = new ChangeSetPerformer(new ChangeSet(changeSet.toArray(new Change[0])));
    }

    @Test
    public void testPerformAddAndDelete() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutput = new ArchiveOutputStream(out);
        ChangeSetResults results = performer.perform(mock(ArchiveInputStream.class), archiveOutput);

        assertEquals(3, results.getAddedFromChangeSet().size());
        assertEquals(2, results.getAddedFromStream().size());
        assertEquals(1, results.getDeleted().size());

        Set actualChanges = performer.getChanges();
        Set expectedChanges = new LinkedHashSet<>(changeSet);
        expectedChanges.removeAll(Collections.singleton(delete1));
        expectedChanges.removeAll(Collections.singleton(delete2));
        assertArrayEquals(expectedChanges.toArray(new Change[0]), actualChanges.toArray(new Change[0]));

        ByteArrayInputStream actualOut = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream actualArchiveInput = new ArchiveInputStream(actualOut);

        assertEquals(2, actualArchiveInput.getNextEntry().getSize());
        assertFalse(actualArchiveInput.getNextEntry().getName().equals("dir/testfile.txt"));
        assertEquals("testfile.txt", actualArchiveInput.getNextEntry().getName());
        assertEquals(1, actualArchiveInput.getNextEntry().getSize());
        assertEquals("", actualArchiveInput.getNextEntry().getName());
    }

    @Test
    public void testPerformAddReplaceMode() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutput = new ArchiveOutputStream(out);
        ChangeSetResults results = performer.perform(mock(ArchiveInputStream.class), archiveOutput);

        assertEquals(3, results.getAddedFromChangeSet().size());
        assertEquals(2, results.getAddedFromStream().size());
        assertEquals(1, results.getDeleted().size());

        InputStream input = new ByteArrayInputStream(TestUtils.getTestData("testdata.bin"));
        ArchiveInputStream archiveInput = new ArchiveInputStream(input);
        ArchiveEntry entry1 = archiveInput.getNextEntry();
        Change add1 = new Change(Change.TYPE_ADD, null, null, null, null, null, null, true);
        add1.setEntry(entry1);
        changeSet.add(add1);

        performer = new ChangeSetPerformer(new ChangeSet(changeSet.toArray(new Change[0])));

        ChangeSetResults results2 = performer.perform(mock(ArchiveInputStream.class), archiveOutput);

        assertEquals(2, results2.getAddedFromChangeSet().size());
        assertEquals(2, results2.getAddedFromStream().size());
        assertEquals(1, results2.getDeleted().size());

        ByteArrayInputStream actualOut = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream actualArchiveInput = new ArchiveInputStream(actualOut);

        assertEquals(2, actualArchiveInput.getNextEntry().getSize());
        assertSame(entry1, actualArchiveInput.getNextEntry());
        assertEquals("testfile.txt", actualArchiveInput.getNextEntry().getName());
        assertEquals(1, actualArchiveInput.getNextEntry().getSize());
        assertEquals("", actualArchiveInput.getNextEntry().getName());
    }

    @Test
    public void testPerformDeleteLater() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutput = new ArchiveOutputStream(out);
        ChangeSetResults results = performer.perform(mock(ArchiveInputStream.class), archiveOutput);

        assertEquals(3, results.getAddedFromChangeSet().size());
        assertEquals(2, results.getAddedFromStream().size());
        assertEquals(1, results.getDeleted().size());

        InputStream input = new ByteArrayInputStream(TestUtils.getTestData("testdata.bin"));
        ArchiveInputStream archiveInput = new ArchiveInputStream(input);
        ArchiveEntry entry1 = archiveInput.getNextEntry();
        Change add1 = new Change(Change.TYPE_ADD, null, null, null, null, null, null, false);
        add1.setEntry(entry1);
        changeSet.add(add1);

        Change add2 = new Change(Change.TYPE_ADD, null, null, null, null, null, null, false);
        add2.setEntry(archiveInput.getNextEntry());
        changeSet.add(add2);

        Change delete = new Change(Change.TYPE_DELETE, null, null, null, null, null, null, false);
        delete.setTargetFile("/dir/testfile.txt");
        changeSet.add(delete);

        performer = new ChangeSetPerformer(new ChangeSet(changeSet.toArray(new Change[0])));

        ChangeSetResults results2 = performer.perform(mock(ArchiveInputStream.class), archiveOutput);

        assertEquals(3, results2.getAddedFromChangeSet().size());
        assertEquals(2, results2.getAddedFromStream().size());
        assertEquals(1, results2.getDeleted().size());

        ByteArrayInputStream actualOut = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream actualArchiveInput = new ArchiveInputStream(actualOut);

        assertEquals(2, actualArchiveInput.getNextEntry().getSize());
        assertSame(entry1, actualArchiveInput.getNextEntry());
        assertNull(actualArchiveInput.getNextEntry());
    }

    @Test(expected = IllegalStateException.class)
    public void testPerformAfterFinished() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutput = new ArchiveOutputStream(out);
        ChangeSetResults results = performer.perform(mock(ArchiveInputStream.class), archiveOutput);

        InputStream input = new ByteArrayInputStream(TestUtils.getTestData("testdata.bin"));
        ArchiveInputStream archiveInput = new ArchiveInputStream(input);
        ArchiveEntry entry = archiveInput.getNextEntry();

        performer.perform(mock(ArchiveInputStream.class), archiveOutput);
    }
}