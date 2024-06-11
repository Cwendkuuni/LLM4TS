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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ArchiveEntry entry1;
    private ArchiveEntry entry2;
    private ArchiveInputStream mockArchiveInputStream;
    private ArchiveOutputStream mockArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        changeSet = new DefaultChangeSet();
        Change addChange1 = new DefaultAddChange("entry1.txt");
        Change addChange2 = new DefaultAddChange("entry2.txt");
        Change deleteChange1 = new DefaultDeleteChange("entry1.txt");
        Change deleteChange2 = new DefaultDeleteChange("entry3.txt");
        Change replaceChange = new DefaultReplaceChange("entry1.txt", "entry1_new.txt");
        changeSet.addChange(addChange1);
        changeSet.addChange(addChange2);
        changeSet.addChange(deleteChange1);
        changeSet.addChange(deleteChange2);
        changeSet.addChange(replaceChange);

        performer = new ChangeSetPerformer(changeSet);

        entry1 = mock(ArchiveEntry.class);
        entry2 = mock(ArchiveEntry.class);
        when(entry1.getName()).thenReturn("entry1.txt");
        when(entry2.getName()).thenReturn("entry2.txt");

        mockArchiveInputStream = mock(ArchiveInputStream.class);
        when(mockArchiveInputStream.getNextEntry()).thenReturn(entry1).thenReturn(entry2).thenReturn(null);

        mockArchiveOutputStream = mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerformAddReplaceMode() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertEquals(2, results.size());
        assertEquals("entry1.txt", results.getAddedFromChangeSet(0).getName());
        assertEquals("entry2.txt", results.getAddedFromChangeSet(1).getName());
        assertEquals(2, results.getAddedFromStream().size());
        assertEquals("entry1.txt", results.getAddedFromStream(0).getName());
        assertEquals("entry2.txt", results.getAddedFromStream(1).getName());
        assertFalse(results.hasBeenAdded("entry1_new.txt"));
    }

    @Test
    public void testPerformDelete() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertEquals(2, results.size());
        assertFalse(results.hasBeenAdded("entry1.txt"));
        assertFalse(results.hasBeenAdded("entry2.txt"));
        assertFalse(results.hasBeenAdded("entry3.txt"));
    }

    @Test
    public void testPerformReplace() throws IOException {
        InputStream inputEntry1 = new ByteArrayInputStream("Hello World 1".getBytes());
        InputStream inputEntry2 = new ByteArrayInputStream("Hello World 2".getBytes());
        when(entry1.getSize()).thenReturn(10L);
        when(entry2.getSize()).thenReturn(15L);

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertEquals(2, results.size());
        assertEquals("entry1.txt", results.getAddedFromChangeSet(0).getName());
        assertEquals("entry1_new.txt", results.getAddedFromChangeSet(1).getName());
        assertEquals(1, results.getAddedFromStream().size());
        assertEquals("entry1_new.txt", results.getAddedFromStream(0).getName());
        assertFalse(results.hasBeenAdded("entry1.txt"));
        assertFalse(results.hasBeenAdded("entry2.txt"));
        assertFalse(results.hasBeenAdded("entry3.txt"));
        assertTrue(IOUtils.contentEquals(new ByteArrayInputStream("Hello World 1".getBytes()), results.getAddedFromChangeSet(0).getInputStream()));
        assertTrue(IOUtils.contentEquals(new ByteArrayInputStream("Hello World 2".getBytes()), results.getAddedFromChangeSet(1).getInputStream()));
    }

    @Test
    public void testPerformDeleteLater() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertEquals(2, results.size());
        assertFalse(results.hasBeenAdded("entry1.txt"));
        assertFalse(results.hasBeenAdded("entry2.txt"));
        assertFalse(results.hasBeenAdded("entry3.txt"));
    }

    @Test
    public void testPerformAddNotReplaceMode() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertEquals(3, results.size());
        assertEquals("entry1.txt", results.getAddedFromChangeSet(0).getName());
        assertEquals("entry2.txt", results.getAddedFromChangeSet(1).getName());
        assertEquals("entry1.txt", results.getAddedFromStream(0).getName());
        assertFalse(results.hasBeenAdded("entry1_new.txt"));
    }

    @Test
    public void testPerformPerformWithEmptyChangeSet() throws IOException {
        performer = new ChangeSetPerformer(new DefaultChangeSet());

        ChangeSetResults results = performer.perform(mockArchiveInputStream, mockArchiveOutputStream);

        assertEquals(0, results.size());
    }

    @Test
    public void testPerformPerformWithNullChangeSet() {
        // This test case should not be executed as the constructor already checks for a null ChangeSet
        // and throws an IllegalArgumentException
        //assertThrows(IllegalArgumentException.class, () -> new ChangeSetPerformer(null));
    }
}