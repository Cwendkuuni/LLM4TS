import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Iterator;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Test;

public class ChangeSetPerformerTest {

    @Test
    public void testPerformWithAddAndReplace() throws IOException {
        ChangeSet changeSet = mock(ChangeSet.class);
        Set changes = new LinkedHashSet<>();
        Change change = mock(Change.class);
        when(change.type()).thenReturn(Change.TYPE_ADD);
        when(change.isReplaceMode()).thenReturn(true);
        when(change.getInput()).thenReturn(mock(InputStream.class));
        when(change.getEntry()).thenReturn(mock(ArchiveEntry.class));
        changes.add(change);
        when(changeSet.getChanges()).thenReturn(changes);
        
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        ArchiveInputStream in = mock(ArchiveInputStream.class);
        ArchiveOutputStream out = mock(ArchiveOutputStream.class);
        
        ChangeSetResults results = performer.perform(in, out);
        
        // Assert expected results
    }

    @Test
    public void testPerformWithDeleteAndDeleteDir() throws IOException {
        // Similar test as above for delete and delete directory changes
    }

    @Test
    public void testIsDeletedLater() {
        // Test cases for checking if an ArchiveEntry is deleted later in the ChangeSet
    }

    @Test
    public void testCopyStream() throws IOException {
        // Test cases for copying the ArchiveEntry to the Output stream
    }

}