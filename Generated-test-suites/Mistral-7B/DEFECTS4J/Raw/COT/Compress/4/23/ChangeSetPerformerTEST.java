package org.apache.commons.compress.changes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarInputStream;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;

    @Before
    public void setUp() {
        Change addChange = new Change("file1.txt", Change.TYPE_ADD, new ByteArrayInputStream(new byte[0]));
        Change addReplaceChange = new Change("file2.txt", Change.TYPE_ADD, new ByteArrayInputStream(new byte[0]), true);
        Change deleteChange = new Change("file1.txt", Change.TYPE_DELETE, null);
        Change deleteDirChange = new Change("dir1", Change.TYPE_DELETE_DIR, null);

        Set<Change> changes = new LinkedHashSet<>();
        changes.add(addChange);
        changes.add(addReplaceChange);
        changes.add(deleteChange);
        changes.add(deleteDirChange);

        changeSet = new ChangeSet(changes);
        performer = new ChangeSetPerformer(changeSet);
    }

    @After
    public void tearDown() throws IOException {
        if (performer != null) {
            performer.close();
        }
    }

    /**
     * Test for constructor
     */
    @Test
    public void testConstructor() {
        assertTrue(performer != null);
    }

    /**
     * Test for perform method with valid inputs
     */
    @Test
    public void testPerformWithValidInputs() throws IOException {
        ByteArrayOutputStream inStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        InputStream in = new TarInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveInputStream archiveIn = new ArchiveInputStream(in);
        ArchiveOutputStream archiveOut = new TarOutputStream(outStream);

        performer.perform(archiveIn, archiveOut);

        TarInputStream tarIn = new TarInputStream(new ByteArrayInputStream(outStream.toByteArray()));
        ArchiveEntry entry;
        while ((entry = tarIn.getNextEntry()) != null) {
            assertTrue(entry.isDirectory() ^ Arrays.equals(entry.getName().split("/"), new String[]{"", "file1.txt"}));
            assertFalse(entry.isDirectory() ^ Arrays.equals(entry.getName().split("/"), new String[]{"dir1", ""}));
            tarIn.close();
        }

        archiveIn.close();
        archiveOut.finish();
        archiveOut.close();
    }

    /**
     * Test for perform method with empty change set
     */
    @Test
    public void testPerformWithEmptyChangeSet() throws IOException {
        ByteArrayOutputStream inStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        InputStream in = new TarInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveInputStream archiveIn = new ArchiveInputStream(in);
        ArchiveOutputStream archiveOut = new TarOutputStream(outStream);

        performer = new ChangeSetPerformer(new ChangeSet(new LinkedHashSet<>()));
        performer.perform(archiveIn, archiveOut);

        archiveIn.close();
        archiveOut.finish();
        archiveOut.close();

        ByteArrayInputStream testIn = new ByteArrayInputStream(outStream.toByteArray());
        ArchiveInputStream testArchiveIn = new ArchiveInputStream(testIn);
        ArchiveEntry testEntry;
        assertTrue(testArchiveIn.getNextEntry() == null);
        testArchiveIn.close();
    }

    /**
     * Test for perform method with null inputs
     */
    @Test(expected = NullPointerException.class)
    public void testPerformWithNullInputs() {
        performer.perform(null, null);
    }

    /**
     * Test for isDeletedLater method with valid inputs
     */
    @Test
    public void testIsDeletedLaterWithValidInputs() {
        Set<Change> workingSet = new LinkedHashSet<>(Arrays.asList(addChange, deleteChange));
        assertTrue(performer.isDeletedLater(workingSet, new ArchiveEntry("file1.txt")));
    }

    /**
     * Test for isDeletedLater method with empty working set
     */
    @Test
    public void testIsDeletedLaterWithEmptyWorkingSet() {
        Set<Change> workingSet = new LinkedHashSet<>();
        assertFalse(performer.isDeletedLater(workingSet, new ArchiveEntry("file1.txt")));
    }

    /**
     * Test for isDeletedLater method with null entry
     */
    @Test(expected = NullPointerException.class)
    public void testIsDeletedLaterWithNullEntry() {
        Set<Change> workingSet = new LinkedHashSet<>(Arrays.asList(addChange, deleteChange));
        performer.isDeletedLater(workingSet, null);
    }

    /**
     * Test for copyStream method with valid inputs
     */
    @Test
    public void testCopyStreamWithValidInputs() throws IOException {
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream in = new ByteArrayInputStream(inputStream.toByteArray());
        ArchiveOutputStream out = new TarOutputStream(outputStream);
        ArchiveEntry entry = new TarEntry("file1.txt");

        performer.copyStream(in, out, entry);

        TarInputStream tarIn = new TarInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        ArchiveEntry readEntry = tarIn.getNextEntry();
        assertTrue(Arrays.equals(readEntry.getName().split("/"), new String[]{"file1.txt"}));
        tarIn.close();

        outputStream.close();
        in.close();
    }

    /**
     * Test for copyStream method with null inputs
     */
    @Test(expected = NullPointerException.class)
    public void testCopyStreamWithNullInputs() {
        performer.copyStream(null, null, null);
    }
}