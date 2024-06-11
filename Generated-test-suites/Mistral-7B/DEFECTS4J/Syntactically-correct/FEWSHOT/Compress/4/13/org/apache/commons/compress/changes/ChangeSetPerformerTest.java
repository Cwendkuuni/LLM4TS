package org.apache.commons.compress.changes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.Testutils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ArchiveInputStream input;
    private ArchiveOutputStream output;
    private ChangeSet changeSet;
    private ChangeSetPerformer performer;

    @Before
    public void setUp() throws IOException {
        // Initialize test data
        changeSet = Testutils.newChangeSet("test.txt", "test2.txt", "test3.txt");
        performer = new ChangeSetPerformer(changeSet);
        input = mock(ArchiveInputStream.class);
        output = mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_ADD_ReplaceMode() throws IOException {
        // Prepare test data
        when(input.getNextEntry()).thenReturn(mockEntry("test.txt"));
        when(input.readAllBytes()).thenReturn(new byte[0]);

        // Test
        ChangeSetResults results = performer.perform(input, output);

        // Verify results
        assertTrue(results.hasBeenAdded("test.txt"));
        verifyNoMoreEntries(input);
    }

    @Test
    public void testPerform_ADD_NotReplaceMode() throws IOException {
        // Prepare test data
        when(input.getNextEntry()).thenReturn(mockEntry("test.txt"));
        when(input.readAllBytes()).thenReturn(new byte[0]);

        // Test
        ChangeSetPerformer performer2 = new ChangeSetPerformer(changeSet);
        ChangeSetResults results = performer2.perform(input, output);

        // Verify results
        assertTrue(results.hasBeenAdded("test.txt"));
        verifyNoMoreEntries(input);
    }

    @Test
    public void testPerform_DELETE() throws IOException {
        // Prepare test data
        when(input.getNextEntry()).thenReturn(mockEntry("test.txt"));
        when(input.readAllBytes()).thenReturn(new byte[0]);

        // Test
        ChangeSetResults results = performer.perform(input, output);

        // Verify results
        assertFalse(results.hasBeenAdded("test.txt"));
        verifyNoMoreEntries(input);
    }

    @Test
    public void testPerform_DELETE_Directory() throws IOException {
        // Prepare test data
        String testDir = "test/";
        when(input.getNextEntry()).thenReturn(mockEntry(testDir + "test.txt"));
        when(input.getNextEntry()).thenReturn(mockEntry(testDir + "test2.txt"));
        when(input.readAllBytes()).thenAnswer(i -> new byte[0]);

        // Test
        ChangeSetResults results = performer.perform(input, output);

        // Verify results
        verifyNoMoreEntries(input);
        assertTrue(results.deleted(testDir + "test.txt"));
        assertTrue(results.deleted(testDir + "test2.txt"));
    }

    @Test
    public void testPerform_ADD_Later() throws IOException {
        // Prepare test data
        when(input.getNextEntry()).thenReturn(mockEntry("test.txt"));
        when(input.readAllBytes()).thenReturn(new byte[0]);

        performer.perform(input, output);

        when(input.getNextEntry()).thenReturn(mockEntry("test3.txt"));
        ChangeSetResults results = performer.perform(input, output);

        // Verify results
        assertTrue(results.hasBeenAdded("test3.txt"));
        verifyNoMoreEntries(input);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }

    private ArchiveEntry mockEntry(String name) {
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn(name);
        return entry;
    }

    private void verifyNoMoreEntries(ArchiveInputStream input) {
        assertFalse(input.hasNextEntry());
    }
}