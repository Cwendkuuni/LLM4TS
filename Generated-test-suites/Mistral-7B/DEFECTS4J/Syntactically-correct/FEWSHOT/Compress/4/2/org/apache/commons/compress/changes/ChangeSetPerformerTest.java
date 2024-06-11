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
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private ArchiveEntry entry1, entry2, entry3, entry4;
    private InputStream input1, input2, input3;
    private ArchiveInputStream inputStream;
    private ArchiveOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        changeSet = mock(ChangeSet.class);
        performer = new ChangeSetPerformer(changeSet);

        input1 = new ByteArrayInputStream(new byte[0]);
        input2 = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        input3 = new ByteArrayInputStream(new byte[] { 4, 5, 6 });

        entry1 = mock(ArchiveEntry.class);
        when(entry1.getName()).thenReturn("entry1");
        entry2 = mock(ArchiveEntry.class);
        when(entry2.getName()).thenReturn("entry2");
        entry3 = mock(ArchiveEntry.class);
        when(entry3.getName()).thenReturn("entry3");
        entry4 = mock(ArchiveEntry.class);
        when(entry4.getName()).thenReturn("entry4");

        outputStream = mock(ArchiveOutputStream.class);
        when(outputStream.putArchiveEntry(entry1)).thenReturn(entry1);
        when(outputStream.putArchiveEntry(entry2)).thenReturn(entry2);
        when(outputStream.putArchiveEntry(entry3)).thenReturn(entry3);
        when(outputStream.putArchiveEntry(entry4)).thenReturn(entry4);
        when(outputStream.closeArchiveEntry()).thenReturn(null);

        inputStream = mock(ArchiveInputStream.class);
        when(inputStream.getNextEntry()).thenAnswer(i -> {
            if (i.getArgument(0) == 0) {
                return entry1;
            } else if (i.getArgument(0) == 1) {
                return entry2;
            } else if (i.getArgument(0) == 2) {
                return entry3;
            } else if (i.getArgument(0) == 3) {
                return null;
            }
            return entry4;
        });
    }

    @Test
    public void testPerform_addFile_withoutReplaceMode() throws IOException {
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(new Change("entry1", Change.TYPE_ADD, null, null, null, false));
        when(changeSet.getChanges()).thenReturn(changes);

        ChangeSetResults results = performer.perform(inputStream, outputStream);
        assertTrue(results.hasBeenAdded("entry1"));
    }

    @Test
    public void testPerform_addFile_withReplaceMode() throws IOException {
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(new Change("entry1", Change.TYPE_ADD, null, null, null, true));

        when(changeSet.getChanges()).thenReturn(changes);
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });

        ChangeSetResults results = performer.perform(inputStream, outputStream);
        assertTrue(results.hasBeenAdded("entry1"));
        assertFalse(inputStream.hasRemaining());
        assertTrue(outputStream.getBufferedData().length > 0);
    }

    @Test
    public void testPerform_deleteFile() throws IOException {
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(new Change("entry1", Change.TYPE_DELETE, null, null, null, null));

        when(changeSet.getChanges()).thenReturn(changes);

        ChangeSetResults results = performer.perform(inputStream, outputStream);
        assertTrue(results.deleted("entry1"));
        assertNull(entry1);
    }

    @Test
    public void testPerform_deleteDirectory() throws IOException {
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(new Change("entry1", Change.TYPE_DELETE_DIR, null, null, null, null));

        when(changeSet.getChanges()).thenReturn(changes);

        ChangeSetResults results = performer.perform(inputStream, outputStream);
        assertTrue(results.deleted("entry1/entry2"));
        assertTrue(results.deleted("entry1/entry3"));
        assertNull(entry1);
    }

    @Test
    public void testPerform_noOperation() throws IOException {
        Set<Change> changes = new LinkedHashSet<>();

        when(changeSet.getChanges()).thenReturn(changes);

        ChangeSetResults results = performer.perform(inputStream, outputStream);
        assertTrue(results.isEmpty());
        assertFalse(inputStream.hasRemaining());
        assertTrue(outputStream.getBufferedData().length == 0);
    }
}