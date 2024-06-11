package org.apache.commons.compress.changes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    private Set<Change> changes;
    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ArchiveEntry entry;
    private ArchiveInputStream in;
    private ArchiveOutputStream out;
    private ByteArrayOutputStream outputStream;
    private InputStream inputStream;

    @Before
    public void setUp() {
        changes = new LinkedHashSet<>();
        entry = mock(ArchiveEntry.class);
        in = mock(ArchiveInputStream.class);
        out = mock(ArchiveOutputStream.class);
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(TestUtils.createTestData());

        changeSet = new ChangeSet("testChangeSet");
        performer = new ChangeSetPerformer(changeSet);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(out);
        IOUtils.closeQuietly(in);
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        assertNull(performer.getChanges());
    }

    /**
     * Test method perform
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testPerform() throws IOException {
        performer.perform(in, out);
        assertTrue(performer.getChanges().isEmpty());
    }

    /**
     * Test method perform with empty ChangeSet
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testPerformEmptyChangeSet() throws IOException {
        changes.add(mockChange(Change.TYPE_ADD, "test.txt", null, null));
        changeSet.setChanges(changes);
        performer = new ChangeSetPerformer(changeSet);
        performer.perform(in, out);

        assertTrue(performer.getChanges().isEmpty());
    }

    /**
     * Test method perform with only add changes
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testPerformAddChanges() throws IOException {
        Change addChange1 = mockChange(Change.TYPE_ADD, "test1.txt", null, null);
        Change addChange2 = mockChange(Change.TYPE_ADD, "test2.txt", null, null);
        changes.add(addChange1);
        changes.add(addChange2);
        changeSet.setChanges(changes);
        performer = new ChangeSetPerformer(changeSet);

        when(in.getNextEntry()).thenReturn(entry).thenReturn(null);
        when(entry.getName()).thenReturn("test1.txt");
        when(entry.getData()).thenReturn(TestUtils.createTestData());

        performer.perform(in, out);

        Set expectedChanges = new LinkedHashSet<>( Arrays.asList( addChange1, addChange2 ) );
        assertTrue(changes.equals(expectedChanges));
        verifyEntryHasBeenAdded("test1.txt", outputStream, performer);
        verifyEntryHasBeenAdded("test2.txt", outputStream, performer);
    }

    /**
     * Test method perform with only delete changes
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testPerformDeleteChanges() throws IOException {
        Change deleteChange1 = mockChange(Change.TYPE_DELETE, "test1.txt", null, null);
        Change deleteChange2 = mockChange(Change.TYPE_DELETE_DIR, "testDir", null, null);
        changes.add(deleteChange1);
        changes.add(deleteChange2);
        changeSet.setChanges(changes);
        performer = new ChangeSetPerformer(changeSet);

        when(in.getNextEntry()).thenReturn(entry).thenReturn(null);
        when(entry.getName()).thenReturn("test1.txt");

        performer.perform(in, out);

        Set expectedChanges = new LinkedHashSet<>( Arrays.asList( deleteChange1, deleteChange2 ) );
        assertTrue(changes.equals(expectedChanges));
        assertFalse(outputStream.toByteArray().length > 0);
    }

    /**
     * Test method perform with add and delete changes
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testPerformAddAndDeleteChanges() throws IOException {
        Change addChange1 = mockChange(Change.TYPE_ADD, "test1.txt", null, null);
        Change addChange2 = mockChange(Change.TYPE_ADD, "test2.txt", null, null);
        Change deleteChange1 = mockChange(Change.TYPE_DELETE, "test1.txt", null, null);
        changes.add(addChange1);
        changes.add(addChange2);
        changes.add(deleteChange1);
        changeSet.setChanges(changes);
        performer = new ChangeSetPerformer(changeSet);

        when(in.getNextEntry()).thenReturn(entry).thenReturn(null);
        when(entry.getName()).thenReturn("test1.txt");
        when(entry.getData()).thenReturn(TestUtils.createTestData());

        performer.perform(in, out);

        Set expectedChanges = new LinkedHashSet<>( Arrays.asList( addChange2, deleteChange1 ) );
        assertTrue(changes.equals(expectedChanges));
        verifyEntryHasBeenAdded("test2.txt", outputStream, performer);
        assertFalse(outputStream.toByteArray().length > 0);
    }

    /**
     * Test getChanges
     */
    @Test
    public void testGetChanges() {
        Set expectedChanges = new LinkedHashSet<>();
        expectedChanges.add(mockChange(Change.TYPE_ADD, "test.txt", null, null));
        expectedChanges.add(mockChange(Change.TYPE_DELETE, "test.txt", null, null));
        changeSet.setChanges(expectedChanges);
        performer = new ChangeSetPerformer(changeSet);

        Set actualChanges = performer.getChanges();
        assertEquals(expectedChanges, actualChanges);
    }

    /**
     * Test getChanges when ChangeSet is empty
     */
    @Test
    public void testGetChangesEmptyChangeSet() {
        changeSet.setChanges(Collections.emptySet());
        performer = new ChangeSetPerformer(changeSet);

        Set actualChanges = performer.getChanges();
        assertNull(actualChanges);
    }

    /**
     * Test isDeletedLater
     */
    @Test
    public void testIsDeletedLater() {
        Change addChange1 = mockChange(Change.TYPE_ADD, "test1.txt", null, null);
        Change deleteChange1 = mockChange(Change.TYPE_DELETE, "test1.txt", null, null);
        changes.add(addChange1);
        changes.add(deleteChange1);

        when(entry.getName()).thenReturn("test1.txt");

        assertTrue(performer.isDeletedLater(changes, entry));
    }

    /**
     * Test isDeletedLater when there are no changes later
     */
    @Test
    public void testIsDeletedLaterNoChangesLater() {
        Change addChange1 = mockChange(Change.TYPE_ADD, "test1.txt", null, null);
        changes.add(addChange1);

        assertFalse(performer.isDeletedLater(changes, entry));
    }

    /**
     * Test copyStream
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testCopyStream() throws IOException {
        performer.copyStream(inputStream, out, entry);

        byte[] expectedData = TestUtils.createTestData();
        byte[] actualData = IOUtils.toByteArray(out);
        assertArrayEquals(expectedData, actualData);
    }

    /**
     * Helper method for testing if an entry has been added to the output stream
     *
     * @param name name of the entry
     * @param outputStream output stream to check
     * @param performer performer to check
     * @throws IOException in case an error occurs
     */
    private void verifyEntryHasBeenAdded(String name, OutputStream outputStream, ChangeSetPerformer performer)
            throws IOException {
        ArchiveEntry expectedEntry = mock(ArchiveEntry.class);
        when(expectedEntry.getName()).thenReturn(name);

        ArchiveEntry actualEntry = null;
        while ((actualEntry = performer.getNextEntry(out)) != null) {
            if (actualEntry.getName().equals(name)) {
                break;
            }
        }

        assertTrue(actualEntry != null);
    }

    /**
     * Helper method for creating a mock Change object
     *
     * @param type type of Change
     * @param target target fileName
     * @param input input stream to the file data
     * @param entry ArchiveEntry for the file
     * @return the mock Change object
     */
    private Change mockChange(int type, String target, InputStream input, ArchiveEntry entry) {
        Change mockChange = mock(Change.class);
        when(mockChange.getType()).thenReturn(type);
        when(mockChange.getTargetFile()).thenReturn(target);
        when(mockChange.getEntry()).thenReturn(entry);
        when(mockChange.getInput()).thenReturn(input);

        return mockChange;
    }
}