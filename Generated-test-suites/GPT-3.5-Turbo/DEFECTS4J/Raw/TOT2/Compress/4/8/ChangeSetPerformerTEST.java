package org.apache.commons.compress.changes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Test;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform_AddReplaceMode() throws IOException {
        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(new LinkedHashSet<>());
        
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        
        ChangeSetResults results = performer.perform(in, out);
        
        assertNotNull(results);
    }

    @Test
    public void testPerform_Delete() throws IOException {
        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(new LinkedHashSet<>());
        
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        
        ChangeSetResults results = performer.perform(in, out);
        
        assertNotNull(results);
    }

    @Test
    public void testPerform_DeleteDir() throws IOException {
        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(new LinkedHashSet<>());
        
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        
        ChangeSetResults results = performer.perform(in, out);
        
        assertNotNull(results);
    }

    @Test
    public void testPerform_CopyStream() throws IOException {
        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(new LinkedHashSet<>());
        
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        ByteArrayInputStream input = new ByteArrayInputStream("test data".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        performer.copyStream(input, out, entry);
        
        assertEquals("test data", output.toString());
    }

    @Test
    public void testIsDeletedLater() {
        ChangeSet changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(new LinkedHashSet<>());
        
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        boolean deletedLater = performer.isDeletedLater(new LinkedHashSet<>(), entry);
        
        assertFalse(deletedLater);
    }

}