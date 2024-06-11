import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class ChangeSetPerformerTest {
    
    private ChangeSetPerformer changeSetPerformer;
    private ChangeSet changeSet;
    private ArchiveInputStream in;
    private ArchiveOutputStream out;
    
    @Before
    public void setUp() {
        changeSet = Mockito.mock(ChangeSet.class);
        changeSetPerformer = new ChangeSetPerformer(changeSet);
        in = Mockito.mock(ArchiveInputStream.class);
        out = Mockito.mock(ArchiveOutputStream.class);
    }
    
    @Test
    public void testPerform_addChangeWithReplaceMode_shouldCopyStreamAndAddToResults() throws IOException {
        Change change = Mockito.mock(Change.class);
        InputStream input = Mockito.mock(InputStream.class);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(change.type()).thenReturn(Change.TYPE_ADD);
        Mockito.when(change.isReplaceMode()).thenReturn(true);
        Mockito.when(change.getInput()).thenReturn(input);
        Mockito.when(change.getEntry()).thenReturn(entry);
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change);
        Mockito.when(changeSet.getChanges()).thenReturn(changes);
        
        ChangeSetResults results = changeSetPerformer.perform(in, out);
        
        Mockito.verify(out).putArchiveEntry(entry);
        Mockito.verify(IOUtils).copy(input, out);
        Mockito.verify(out).closeArchiveEntry();
        assertTrue(results.hasBeenAdded(entry.getName()));
    }
    
    @Test
    public void testPerform_deleteChange_shouldRemoveEntryAndAddToResults() throws IOException {
        Change change = Mockito.mock(Change.class);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(change.type()).thenReturn(Change.TYPE_DELETE);
        Mockito.when(change.targetFile()).thenReturn("file.txt");
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change);
        Mockito.when(changeSet.getChanges()).thenReturn(changes);
        Mockito.when(in.getNextEntry()).thenReturn(entry);
        
        ChangeSetResults results = changeSetPerformer.perform(in, out);
        
        Mockito.verify(change, Mockito.times(1)).type();
        Mockito.verify(change, Mockito.times(1)).targetFile();
        Mockito.verify(results).deleted("file.txt");
    }
    
    @Test
    public void testPerform_deleteDirChange_shouldRemoveEntryAndAddToResults() throws IOException {
        Change change = Mockito.mock(Change.class);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Mockito.when(change.type()).thenReturn(Change.TYPE_DELETE_DIR);
        Mockito.when(change.targetFile()).thenReturn("dir");
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change);
        Mockito.when(changeSet.getChanges()).thenReturn(changes);
        Mockito.when(in.getNextEntry()).thenReturn(entry);
        
        ChangeSetResults results = changeSetPerformer.perform(in, out);
        
        Mockito.verify(change, Mockito.times(1)).type();
        Mockito.verify(change, Mockito.times(1)).targetFile();
        Mockito.verify(results).deleted(entry.getName());
    }
    
    @Test
    public void testPerform_entryNotDeleted_shouldCopyStreamAndAddToResults() throws IOException {
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Set<Change> changes = new LinkedHashSet<>();
        Mockito.when(changeSet.getChanges()).thenReturn(changes);
        Mockito.when(in.getNextEntry()).thenReturn(entry);
        
        ChangeSetResults results = changeSetPerformer.perform(in, out);
        
        Mockito.verify(IOUtils).copy(in, out);
        Mockito.verify(out).putArchiveEntry(entry);
        Mockito.verify(out).closeArchiveEntry();
        assertTrue(results.hasBeenAdded(entry.getName()));
    }
    
    @Test
    public void testPerform_entryDeletedLater_shouldReturnTrue() {
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Change change = Mockito.mock(Change.class);
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change);
        Mockito.when(change.type()).thenReturn(Change.TYPE_DELETE);
        Mockito.when(change.targetFile()).thenReturn("file.txt");
        Mockito.when(changeSet.getChanges()).thenReturn(changes);
        
        boolean result = changeSetPerformer.isDeletedLater(changes, entry);
        
        assertTrue(result);
    }
    
    @Test
    public void testCopyStream_shouldPutArchiveEntryAndCopyData() throws IOException {
        InputStream input = Mockito.mock(InputStream.class);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        
        changeSetPerformer.copyStream(input, out, entry);
        
        Mockito.verify(out).putArchiveEntry(entry);
        Mockito.verify(IOUtils).copy(input, out);
        Mockito.verify(out).closeArchiveEntry();
    }
}