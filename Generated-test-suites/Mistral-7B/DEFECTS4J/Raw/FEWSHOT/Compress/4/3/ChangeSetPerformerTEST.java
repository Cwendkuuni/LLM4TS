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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.test.TestArchiveOutputStream;
import org.apache.commons.compress.archivers.test.TestArchiveStreamProvider;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private Set changes;
    private ArchiveEntry entry1;
    private ArchiveEntry entry2;
    private ArchiveEntry entry3;
    private ArchiveEntry entry4;
    private ArchiveEntry entry5;
    private ArchiveInputStream inputStream;
    private ArchiveOutputStream outputStream;

    @Before
    public void setUp() {
        changeSet = mock(ChangeSet.class);
        changes = new LinkedHashSet<>();
        performer = new ChangeSetPerformer(changeSet);

        entry1 = mockArchiveEntry("entry1.txt");
        entry2 = mockArchiveEntry("entry2.txt");
        entry3 = mockArchiveEntry("entry3.txt");
        entry4 = mockArchiveEntry("entry4.txt");
        entry5 = mockArchiveEntry("entry5.txt");

        changes.add(new Change(Change.TYPE_ADD, entry1, null, true));
        changes.add(new Change(Change.TYPE_DELETE, null, "entry2.txt", false));
        changes.add(new Change(Change.TYPE_ADD, entry3, null, false));
        changes.add(new Change(Change.TYPE_DELETE, null, "entry3.txt", false));
        changes.add(new Change(Change.TYPE_ADD, entry4, null, false));
        changes.add(new Change(Change.TYPE_ADD, entry5, null, true));

        when(changeSet.getChanges()).thenReturn(changes);
    }

    @Test
    public void shouldPerformAddAndReplaceChanges() throws IOException {
        //Given
        inputStream = createTestArchiveInputStream(Arrays.asList(entry1, entry5));
        outputStream = new TestArchiveOutputStream();

        //When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        //Then
        assertEquals(Arrays.asList("entry1.txt", "entry5.txt"), results.getAddedFromChangeSet());
        assertEquals(Arrays.asList("entry2.txt"), results.getDeleted());
    }

    @Test
    public void shouldPerformAddChangesWithoutReplaceMode() throws IOException {
        //Given
        inputStream = createTestArchiveInputStream(Arrays.asList(entry1, entry3, entry5));
        outputStream = new TestArchiveOutputStream();

        //When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        //Then
        assertEquals(Arrays.asList("entry1.txt", "entry3.txt", "entry5.txt"), results.getAddedFromChangeSet());
        assertEquals(Arrays.asList(), results.getDeleted());
    }

    @Test
    public void shouldPerformDeleteChanges() throws IOException {
        //Given
        inputStream = createTestArchiveInputStream(Arrays.asList(entry1, entry2, entry3, entry4));
        outputStream = new TestArchiveOutputStream();

        //When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        //Then
        assertEquals(Arrays.asList("entry1.txt", "entry3.txt", "entry5.txt"), results.getAddedFromChangeSet());
        assertEquals(Arrays.asList("entry2.txt"), results.getDeleted());
    }

    @Test
    public void shouldNotPerformDeleteChangesIfEntryIsAddedLater() throws IOException {
        //Given
        inputStream = createTestArchiveInputStream(Arrays.asList(entry1, entry2, entry3, entry4, entry5));
        outputStream = new TestArchiveOutputStream();

        //When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        //Then
        assertEquals(Arrays.asList("entry1.txt", "entry3.txt", "entry5.txt"), results.getAddedFromChangeSet());
        assertEquals(Arrays.asList(), results.getDeleted());
    }

    @Test
    public void shouldCopyStreamsIfEntryIsNotDeleted() throws IOException {
        //Given
        inputStream = createTestArchiveInputStream(Arrays.asList(entry1, entry2, entry3, entry4, entry5));
        outputStream = new TestArchiveOutputStream();

        //When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        //Then
        assertEquals(Arrays.asList("entry1.txt", "entry3.txt", "entry5.txt"), results.getAddedFromChangeSet());
        assertEquals(Arrays.asList(), results.getDeleted());

        //Verify that all entries were copied
        byte[] expectedContent = "expectedContent".getBytes();
        ByteArrayInputStream inputEntry1 = new ByteArrayInputStream(expectedContent);
        ByteArrayOutputStream outputEntry1 = new ByteArrayOutputStream();
        performer.copyStream(inputEntry1, outputStream, entry1);
        byte[] actualContent1 = outputStream.toByteArray();
        assertArrayEquals(expectedContent, actualContent1);

        ByteArrayInputStream inputEntry3 = new ByteArrayInputStream(expectedContent);
        ByteArrayOutputStream outputEntry3 = new ByteArrayOutputStream();
        performer.copyStream(inputEntry3, outputStream, entry3);
        byte[] actualContent3 = outputStream.toByteArray();
        assertArrayEquals(expectedContent, actualContent3);

        ByteArrayInputStream inputEntry5 = new ByteArrayInputStream(expectedContent);
        ByteArrayOutputStream outputEntry5 = new ByteArrayOutputStream();
        performer.copyStream(inputEntry5, outputStream, entry5);
        byte[] actualContent5 = outputStream.toByteArray();
        assertArrayEquals(expectedContent, actualContent5);
    }

    @Test
    public void shouldReturnNullIfInputIsNull() throws IOException {
        //Given
        inputStream = null;
        outputStream = new TestArchiveOutputStream();

        //When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        //Then
        assertNull(results);
    }

    @Test
    public void shouldReturnNullIfOutputIsNull() throws IOException {
        //Given
        inputStream = createTestArchiveInputStream(Arrays.asList(entry1, entry3, entry5));
        outputStream = null;

        //When
        ChangeSetResults results = performer.perform(inputStream, outputStream);

        //Then
        assertNull(results);
    }

    private ArchiveInputStream createTestArchiveInputStream(List<ArchiveEntry> entries) throws IOException {
        TestArchiveStreamProvider provider = new TestArchiveStreamProvider();
        ArchiveInputStream inputStream = new ArchiveInputStream(provider.getInputStream());

        ArchiveOutputStream outputStream = new TestArchiveOutputStream();
        InputStream testInputStream = createTestArchiveInputStreamWithEntries(Arrays.asList(entries.toArray(new ArchiveEntry[0])));
        provider.addArchiveEntry(entry1);
        provider.addArchiveEntry(entry3);
        provider.addArchiveEntry(entry5);
        IOUtils.copy(testInputStream, inputStream);
        testInputStream.close();

        return inputStream;
    }

    private InputStream createTestArchiveInputStreamWithEntries(List<ArchiveEntry> entries) throws IOException {
        ArchiveOutputStream outputStream = new TestArchiveOutputStream();
        for (ArchiveEntry entry : entries) {
            outputStream.putArchiveEntry(entry);
            IOUtils.copy(new ByteArrayInputStream("Test content".getBytes()), outputStream);
            outputStream.closeArchiveEntry();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private ArchiveEntry mockArchiveEntry(String name) {
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn(name);
        return entry;
    }
}