package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {
    
    private ChangeSetPerformer performer;
    private ChangeSet changeSet;
    private Set<Change> changes;
    
    @Before
    public void setUp() {
        changes = new LinkedHashSet<>();
        changeSet = mock(ChangeSet.class);
        when(changeSet.getChanges()).thenReturn(changes);
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform() throws IOException {
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        ChangeSetResults results = performer.perform(in, out);
        
        // Verify perform method was called on changes in add replace mode
        verify(change, times(1)).type();
        verify(change, times(1)).isReplaceMode();
        verify(change, times(1)).getInput();
        verify(change, times(1)).getEntry();
        
        // Verify copyStream method was called once for added files in replace mode
        verify(performer, times(1)).copyStream(any(InputStream.class), eq(out), eq(change.getEntry()));
        
        // Verify getNextEntry method on input stream was called multiple times
        verify(in, atLeastOnce()).getNextEntry();
        
        // Verify copyStream method was called for files not deleted or added
        verify(performer, atLeastOnce()).copyStream(eq(in), eq(out), eq(entry));
        
        // Verify copyStream method was called for added files not in replace mode
        verify(performer, times(1)).copyStream(any(InputStream.class), eq(out), eq(change.getEntry()));
        
        // Verify closeArchiveEntry method on output stream was called at least once
        verify(out, atLeastOnce()).closeArchiveEntry();
        
        // Verify methods on results object were called
        verify(results, times(1)).addedFromChangeSet(eq(change.getEntry().getName()));
        verify(results, times(1)).deleted(eq(entry.getName()));
        verify(results, times(1)).addedFromStream(eq(entry.getName()));
        verify(results, times(1)).hasBeenAdded(eq(entry.getName()));
    }

    @Test
    public void testIsDeletedLater() {
        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("testfile.txt");
        
        // Test when workingSet is empty
        assertFalse(performer.isDeletedLater(new LinkedHashSet<>(), entry));
        
        // Test when workingSet contains a deletion change with same source
        Set<Change> workingSet = new LinkedHashSet<>();
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("testfile.txt");
        workingSet.add(change);
        assertTrue(performer.isDeletedLater(workingSet, entry));
        
        // Test when workingSet contains a deletion change with same source and slash at the end
        workingSet = new LinkedHashSet<>();
        change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change.targetFile()).thenReturn("directory");
        workingSet.add(change);
        assertTrue(performer.isDeletedLater(workingSet, entry));
        
        // Test when workingSet contains a deletion change with different source
        workingSet = new LinkedHashSet<>();
        change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("otherfile.txt");
        workingSet.add(change);
        assertFalse(performer.isDeletedLater(workingSet, entry));
    }

    @Test
    public void testCopyStream() throws IOException {
        InputStream in = mock(InputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        performer.copyStream(in, out, entry);
        
        // Verify putArchiveEntry method on output stream was called
        verify(out, times(1)).putArchiveEntry(entry);
        
        // Verify copy method on IOUtils was called
        verify(IOUtils, times(1)).copy(eq(in), eq(out));
        
        // Verify closeArchiveEntry method on output stream was called
        verify(out, times(1)).closeArchiveEntry();
    }
}