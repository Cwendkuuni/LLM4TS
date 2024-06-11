package org.apache.commons.compress.changes;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static org.mockito.Mockito.*;

public class ChangeSetPerformerTest {
    private ChangeSetPerformer performer;
    private ChangeSet changeSet;
    private ArchiveInputStream in;
    private ArchiveOutputStream out;
    
    @Before
    public void setup() {
        changeSet = mock(ChangeSet.class);
        in = mock(ArchiveInputStream.class);
        out = mock(ArchiveOutputStream.class);
        performer = new ChangeSetPerformer(changeSet);
    }
    
    @Test
    public void testPerform_AddAndReplaceMode() throws IOException {
        ChangeSetResults expectedResults = new ChangeSetResults();
        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        InputStream input = mock(InputStream.class);
        
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(true);
        when(change.getInput()).thenReturn(input);
        when(change.getEntry()).thenReturn(entry);
        
        when(changeSet.getChanges()).thenReturn(singletonSet(change));
        when(change.getEntry().getName()).thenReturn("test.txt");
        
        performer.perform(in, out);
        
        verify(out).putArchiveEntry(entry);
        verify(out).closeArchiveEntry();
        verify(changeSet).getChanges();
        verify(change).type();
        verify(change).isReplaceMode();
        verify(change).getInput();
        verify(change).getEntry();
        verify(change, never()).targetFile();
        
        expectedResults.addedFromChangeSet("test.txt");
    }
    
    @Test
    public void testPerform_Delete() throws IOException {
        ChangeSetResults expectedResults = new ChangeSetResults();
        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        when(change.type()).thenReturn(Change.TYPE_DELETE);
        when(change.targetFile()).thenReturn("test.txt");
        
        when(changeSet.getChanges()).thenReturn(singletonSet(change));
        when(entry.getName()).thenReturn("test.txt");
        
        performer.perform(in, out);
        
        verify(in).getNextEntry();
        verify(out, never()).putArchiveEntry(entry);
        verify(out, never()).closeArchiveEntry();
        verify(changeSet).getChanges();
        verify(change).type();
        verify(change, never()).isReplaceMode();
        verify(change, never()).getInput();
        verify(change, never()).getEntry();
        verify(change).targetFile();
        
        expectedResults.deleted("test.txt");
    }
    
    @Test
    public void testPerform_DeleteDir() throws IOException {
        ChangeSetResults expectedResults = new ChangeSetResults();
        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);

        when(change.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change.targetFile()).thenReturn("dir");
        
        when(changeSet.getChanges()).thenReturn(singletonSet(change));
        when(entry.getName()).thenReturn("dir/file.txt");
        
        performer.perform(in, out);
        
        verify(in).getNextEntry();
        verify(out, never()).putArchiveEntry(entry);
        verify(out, never()).closeArchiveEntry();
        verify(changeSet).getChanges();
        verify(change).type();
        verify(change, never()).isReplaceMode();
        verify(change, never()).getInput();
        verify(change, never()).getEntry();
        verify(change).targetFile();
        
        expectedResults.deleted("dir/file.txt");
    }
    
    public void testPerform_Copy() throws IOException {
        ChangeSetResults expectedResults = new ChangeSetResults();
        Change change = mock(Change.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);
        
        when(change.type()).thenReturn(Change.TYPE_COPY);
        when(change.targetFile()).thenReturn("test.txt");
        
        when(changeSet.getChanges()).thenReturn(singletonSet(change));
        when(entry.getName()).thenReturn("test.txt");
        
        performer.perform(in, out);
        
        verify(in).getNextEntry();
        verify(out).putArchiveEntry(entry);
        verify(out).closeArchiveEntry();
        verify(changeSet).getChanges();
        verify(change).type();
        verify(change, never()).isReplaceMode();
        verify(change, never()).getInput();
        verify(change, never()).getEntry();
        verify(change).targetFile();
        
        expectedResults.addedFromStream("test.txt");
    }
    
    private Set singletonSet(Object object) {
        Set set = new LinkedHashSet();
        set.add(object);
        return set;
    }
}