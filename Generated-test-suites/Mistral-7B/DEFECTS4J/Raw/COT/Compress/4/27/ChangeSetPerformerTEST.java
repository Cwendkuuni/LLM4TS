package org.apache.commons.compress.changes;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.test.TestArchiveInputStream;
import org.apache.commons.compress.archivers.test.TestArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

    @Test
    public void testChangeSetPerformer_constructor() {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        assertNotNull(performer);
    }

    @Test
    public void testPerform_noChanges() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveOutputStream out = new TestArchiveOutputStream();

        ChangeSetResults results = performer.perform(new TestArchiveInputStream(new byte[0][]), out);
        assertEquals(Collections.emptySet(), results.getAddedFromChangeSet());
        assertEquals(Collections.emptySet(), results.getDeleted());
        assertEquals(Collections.emptySet(), results.getAddedFromStream());
    }

    @Test
    public void testPerform_add() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        Change addChange = new Change("test.txt", "test_input.txt", Change.TYPE_ADD, true);
        changeSet.addChange(addChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveOutputStream out = new TestArchiveOutputStream();

        ChangeSetResults results = performer.perform(new TestArchiveInputStream(new byte[0][]), out);
        Set<String> expectedAdded = new LinkedHashSet<>(Arrays.asList("test.txt"));
        assertEquals(expectedAdded, results.getAddedFromChangeSet());
        assertEquals(Collections.emptySet(), results.getDeleted());
        assertEquals(expectedAdded, results.getAddedFromStream());

        assertArrayEquals(new byte[0][], out.getContents());
    }

    @Test
    public void testPerform_delete() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        Change deleteChange = new Change("test.txt", null, Change.TYPE_DELETE, false);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream in = new ByteArrayInputStream(new byte[0][]);
        ArchiveOutputStream out = new TestArchiveOutputStream();

        ArchiveEntry testEntry = new TestArchiveEntry("test.txt");
        in.mark(0);
        out.putNextEntry(testEntry);
        IOUtils.write("test content", out, 0, testEntry.getSize());
        out.finishEntry();

        ChangeSetResults results = performer.perform(new TestArchiveInputStream(in), out);
        Set<String> expectedDeleted = new LinkedHashSet<>(Arrays.asList("test.txt"));
        assertEquals(Collections.emptySet(), results.getAddedFromChangeSet());
        assertEquals(expectedDeleted, results.getDeleted());
        assertEquals(Collections.emptySet(), results.getAddedFromStream());

        assertArrayEquals(new byte[0][], out.getContents());
    }

    @Test
    public void testPerform_addAndDelete() throws IOException {
        ChangeSet changeSet = new ChangeSet();

        Change addChange = new Change("test.txt", "test_input.txt", Change.TYPE_ADD, true);
        changeSet.addChange(addChange);

        Change deleteChange = new Change("test.txt", null, Change.TYPE_DELETE, false);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream in = new ByteArrayInputStream(new byte[0][]);
        ArchiveOutputStream out = new TestArchiveOutputStream();

        ArchiveEntry testEntry = new TestArchiveEntry("test.txt");
        in.mark(0);
        out.putNextEntry(testEntry);
        IOUtils.write("test content", out, 0, testEntry.getSize());
        out.finishEntry();

        ChangeSetResults results = performer.perform(new TestArchiveInputStream(in), out);
        Set<String> expectedAdded = new LinkedHashSet<>(Arrays.asList());
        Set<String> expectedDeleted = new LinkedHashSet<>(Arrays.asList("test.txt"));
        assertEquals(expectedAdded, results.getAddedFromChangeSet());
        assertEquals(expectedDeleted, results.getDeleted());
        assertEquals(Collections.emptySet(), results.getAddedFromStream());

        assertArrayEquals(new byte[0][], out.getContents());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_nullInputStream() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(null, new TestArchiveOutputStream());
    }

    @Test(expected = IOException.class)
    public void testPerform_nullArchiveOutputStream() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        InputStream in = new ByteArrayInputStream(new byte[0][]);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(in, null);
    }

    @Test(expected = IOException.class)
    public void testPerform_inputStreamClosed() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        InputStream in = new ByteArrayInputStream(new byte[0][]);
        in.close();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(in, new TestArchiveOutputStream());
    }

    @Test(expected = IOException.class)
    public void testPerform_archiveOutputStreamClosed() throws IOException {
        ChangeSet changeSet = new ChangeSet();
        InputStream in = new ByteArrayInputStream(new byte[0][]);
        ArchiveOutputStream out = new TestArchiveOutputStream();
        out.close();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(in, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsDeletedLater_nullWorkingSet() {
        ChangeSetPerformer performer = new ChangeSetPerformer(null);
        ArchiveEntry entry = new TestArchiveEntry("test.txt");
        boolean result = performer.isDeletedLater(null, entry);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsDeletedLater_nullEntry() {
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        Set workingSet = new LinkedHashSet<>(Arrays.asList(new Change("test.txt", null, Change.TYPE_DELETE, false)));
        boolean result = performer.isDeletedLater(workingSet, null);
        assertFalse(result);
    }

    @Test
    public void testIsDeletedLater_noDeleteLater() {
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        Set workingSet = new LinkedHashSet<>(Arrays.asList(new Change("test.txt", null, Change.TYPE_ADD, false),
                new Change("test.txt", null, Change.TYPE_DELETE, false)));
        ArchiveEntry entry = new TestArchiveEntry("test.txt");
        boolean result = performer.isDeletedLater(workingSet, entry);
        assertFalse(result);
    }

    @Test
    public void testIsDeletedLater_deleteLater() {
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        Set workingSet = new LinkedHashSet<>(Arrays.asList(new Change("test.txt", null, Change.TYPE_ADD, false),
                new Change("test.txt", null, Change.TYPE_DELETE, false),
                new Change("test.txt", null, Change.TYPE_DELETE, false)));
        ArchiveEntry entry = new TestArchiveEntry("test.txt");
        boolean result = performer.isDeletedLater(workingSet, entry);
        assertTrue(result);
    }
}