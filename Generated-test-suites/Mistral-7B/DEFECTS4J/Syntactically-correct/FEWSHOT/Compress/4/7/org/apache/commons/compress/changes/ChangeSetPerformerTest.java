package org.apache.commons.compress.changes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.archivers.tar.TarInputStream;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.cli.Change;
import org.apache.commons.cli.ChangeSet;
import org.apache.commons.cli.ChangeSetPerformer;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private InputStream inputStream;
    private ArchiveInputStream archiveInputStream;
    private ArchiveOutputStream archiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ChangeSetPerformer performer;

    @Before
    public void setUp() throws IOException {
        changeSet = new ChangeSet();

        // Add changes to the ChangeSet
        Change addChange = new Change(Change.TYPE_ADD, "testAdd.txt", null, null);
        Change addReplaceChange = new Change(Change.TYPE_ADD, "testAddReplace.txt", null, null, true);
        Change deleteChange = new Change(Change.TYPE_DELETE, "testDelete.txt", null, null);
        Change deleteDirChange = new Change(Change.TYPE_DELETE_DIR, "testDeleteDir/", null, null);

        changeSet.addChange(addChange);
        changeSet.addChange(addReplaceChange);
        changeSet.addChange(deleteChange);
        changeSet.addChange(deleteDirChange);

        inputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = new TarInputStream(inputStream);
        archiveOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        byteArrayOutputStream = new ByteArrayOutputStream();

        performer = new ChangeSetPerformer(changeSet);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(archiveInputStream);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(archiveOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);
    }

    @Test
    public void testPerform_EmptyInput() throws IOException {
        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testPerform_AddChange() throws IOException {
        // Set up input stream with testAdd.txt
        byteArrayOutputStream.write("Test add file".getBytes());
        byteArrayOutputStream.close();
        InputStream testAddInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        TarEntry testAddEntry = new TarEntry("testAdd.txt");
        testAddEntry.setSize(byteArrayOutputStream.size());
        archiveInputStream.addEntry(testAddEntry);

        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        ArchiveEntry resultEntry = archiveOutputStream.getNextEntry();
        assertSame(testAddEntry, resultEntry);
        assertTrue(results.addedFromChangeSet(testAddEntry.getName()));
    }

    @Test
    public void testPerform_AddReplaceChange() throws IOException {
        // Set up input stream with testAddReplace.txt
        byteArrayOutputStream.write("Test add replace file".getBytes());
        byteArrayOutputStream.close();
        InputStream testAddReplaceInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        TarEntry testAddReplaceEntry = new TarEntry("testAddReplace.txt");
        testAddReplaceEntry.setSize(byteArrayOutputStream.size());

        // Set up input stream with testAdd.txt
        byteArrayOutputStream.write("Test add file".getBytes());
        byteArrayOutputStream.close();
        InputStream testAddInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        TarEntry testAddEntry = new TarEntry("testAdd.txt");
        testAddEntry.setSize(byteArrayOutputStream.size());

        // Set up input stream with testAddReplace.txt as the last entry
        TarInputStream testAddReplaceTarInputStream = new TarInputStream(testAddReplaceInputStream);
        TarEntry testAddReplaceLastEntry = testAddReplaceTarInputStream.getNextEntry();
        assertNull(testAddReplaceLastEntry);
        testAddReplaceTarInputStream.close();

        // Add testAdd.txt to the archive input stream
        archiveInputStream.addEntry(testAddEntry);

        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        ArchiveEntry resultEntry = archiveOutputStream.getNextEntry();
        assertSame(testAddReplaceEntry, resultEntry);
        assertTrue(results.addedFromChangeSet(testAddReplaceEntry.getName()));
        assertFalse(results.addedFromStream(testAddEntry.getName()));
    }

    @Test
    public void testPerform_DeleteChange() throws IOException {
        // Set up input stream with testDelete.txt
        byteArrayOutputStream.write("Test delete file".getBytes());
        byteArrayOutputStream.close();
        InputStream testDeleteInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        TarEntry testDeleteEntry = new TarEntry("testDelete.txt");
        testDeleteEntry.setSize(byteArrayOutputStream.size());
        archiveInputStream.addEntry(testDeleteEntry);

        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Check that testDelete.txt was not added to the output stream
        ArchiveEntry resultEntry = archiveOutputStream.getNextEntry();
        assertNull(resultEntry);
        assertTrue(results.deleted("testDelete.txt"));
    }

    @Test
    public void testPerform_DeleteDirChange() throws IOException {
        // Set up input stream with testDeleteDir/testDeleteDirFile.txt
        byteArrayOutputStream.write("Test delete dir file".getBytes());
        byteArrayOutputStream.close();
        InputStream testDeleteDirInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        TarEntry testDeleteDirEntry = new TarEntry("testDeleteDir/testDeleteDirFile.txt");
        testDeleteDirEntry.setSize(byteArrayOutputStream.size());
        archiveInputStream.addEntry(testDeleteDirEntry);

        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        // Check that testDeleteDir/testDeleteDirFile.txt was not added to the output stream
        ArchiveEntry resultEntry = archiveOutputStream.getNextEntry();
        assertNull(resultEntry);
        assertTrue(results.deleted("testDeleteDir/testDeleteDirFile.txt"));

        // Check that testDeleteDir/ was deleted
        resultEntry = archiveOutputStream.getNextEntry();
        assertSame(testDeleteDirEntry, resultEntry);
        assertTrue(results.deleted("testDeleteDir/"));
    }

    @Test
    public void testPerform_MultipleChanges() throws IOException {
        // Set up input stream with testAdd.txt and testDelete.txt
        byteArrayOutputStream.write("Test add file".getBytes());
        byteArrayOutputStream.close();
        InputStream testAddInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        TarEntry testAddEntry = new TarEntry("testAdd.txt");
        testAddEntry.setSize(byteArrayOutputStream.size());

        byteArrayOutputStream.write("Test delete file".getBytes());
        byteArrayOutputStream.close();
        InputStream testDeleteInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        TarEntry testDeleteEntry = new TarEntry("testDelete.txt");
        testDeleteEntry.setSize(byteArrayOutputStream.size());

        archiveInputStream.addEntry(testAddEntry);
        archiveInputStream.addEntry(testDeleteEntry);

        ChangeSetResults results = performer.perform(archiveInputStream, archiveOutputStream);

        List<String> addedEntries = new ArrayList<>();
        List<String> deletedEntries = new ArrayList<>();

        ArchiveEntry entry;
        while ((entry = archiveOutputStream.getNextEntry()) != null) {
            String entryName = entry.getName();
            if (entryName.endsWith("/")) {
                deletedEntries.add(entryName);
            } else {
                addedEntries.add(entryName);
            }
        }

        // Check that testAdd.txt was added
        assertTrue(addedEntries.contains("testAdd.txt"));

        // Check that testDelete.txt was not added and testDeleteDir/ was deleted
        assertTrue(deletedEntries.contains("testDelete.txt"));
        assertTrue(deletedEntries.contains("testDeleteDir/"));
        assertFalse(addedEntries.contains("testDelete.txt"));

        // Check that all other entries are unchanged
        List<String> unchangedEntries = new ArrayList<>();
        for (Change change : changeSet.getChanges()) {
            if (change.type() != Change.TYPE_ADD && change.type() != Change.TYPE_DELETE) {
                TestUtils.assertContains(unchangedEntries, change.getEntry().getName());
            }
        }

        for (ArchiveEntry entry : TestUtils.getEntries(archiveOutputStream)) {
            String entryName = entry.getName();
            if (!unchangedEntries.contains(entryName)) {
                throw new AssertionError("Unexpected entry: " + entryName);
            }
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPerform_PerformerIsImmutable() throws IOException {
        performer.perform(archiveInputStream, archiveOutputStream);
        performer.perform(archiveInputStream, archiveOutputStream);
    }
}