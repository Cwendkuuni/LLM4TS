import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ArchiveInputStream mockInput;
    private ArchiveOutputStream mockOutput;
    private ChangeSet changeSet;
    private Set<Change> changes;

    @Before
    public void setUp() {
        mockInput = mock(ArchiveInputStream.class);
        mockOutput = mock(ArchiveOutputStream.class);
        changeSet = mock(ChangeSet.class);
        changes = new LinkedHashSet<Change>();
    }

    @Test
    public void testPerform() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        // Mocking changes
        Change change1 = mock(Change.class);
        Change change2 = mock(Change.class);
        changes.add(change1);
        changes.add(change2);
        when(changeSet.getChanges()).thenReturn(changes);

        // Mocking methods in Change class
        when(change1.type()).thenReturn(Change.TYPE_ADD);
        when(change1.isReplaceMode()).thenReturn(true);
        InputStream mockInput1 = mock(InputStream.class);
        ArchiveEntry mockEntry1 = mock(ArchiveEntry.class);
        when(change1.getInput()).thenReturn(mockInput1);
        when(change1.getEntry()).thenReturn(mockEntry1);

        when(change2.type()).thenReturn(Change.TYPE_DELETE);
        when(change2.targetFile()).thenReturn("fileName");

        // Mocking methods in ArchiveInputStream
        ArchiveEntry mockEntry2 = mock(ArchiveEntry.class);
        when(mockInput.getNextEntry()).thenReturn(mockEntry2).thenReturn(null);
        when(mockEntry2.getName()).thenReturn("fileName");

        // Mocking methods in ArchiveOutputStream
        doNothing().when(mockOutput).putArchiveEntry(any(ArchiveEntry.class));
        doNothing().when(mockOutput).closeArchiveEntry();

        ChangeSetResults results = performer.perform(mockInput, mockOutput);

        // Verify calls in ChangeSet
        verify(changeSet).getChanges();

        // Verify calls in Change
        verify(change1).type();
        verify(change1).isReplaceMode();
        verify(change1).getInput();
        verify(change1).getEntry();
        verify(change2).type();
        verify(change2).targetFile();

        // Verify calls in ArchiveInputStream
        verify(mockInput, times(2)).getNextEntry();
        verify(mockEntry2).getName();

        // Verify calls in ArchiveOutputStream
        verify(mockOutput).putArchiveEntry(any(ArchiveEntry.class));
        verify(mockOutput).closeArchiveEntry();

        // Verify calls in IOUtils
        verify(IOUtils).copy(mockInput1, mockOutput);

        // Verify results
        assertSame(results, performer.perform(mockInput, mockOutput));
    }

    @Test
    public void testIsDeletedLater() {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        Set<Change> workingSet = new LinkedHashSet<Change>();
        Change change1 = mock(Change.class);
        Change change2 = mock(Change.class);
        workingSet.add(change1);
        workingSet.add(change2);

        ArchiveEntry entry = mock(ArchiveEntry.class);
        when(entry.getName()).thenReturn("fileName");

        when(change1.type()).thenReturn(Change.TYPE_DELETE);
        when(change1.targetFile()).thenReturn("fileName");

        when(change2.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change2.targetFile()).thenReturn("dir");

        boolean result = performer.isDeletedLater(workingSet, entry);

        // Verify calls in ArchiveEntry
        verify(entry).getName();

        // Verify calls in Change
        verify(change1).type();
        verify(change1, times(2)).targetFile();
        verify(change2).type();
        verify(change2, times(2)).targetFile();

        // Verify result
        assertTrue(result);
    }

    @Test
    public void testCopyStream() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream mockInput = mock(InputStream.class);
        ArchiveOutputStream mockOutput = mock(ArchiveOutputStream.class);
        ArchiveEntry mockEntry = mock(ArchiveEntry.class);

        doNothing().when(mockOutput).putArchiveEntry(any(ArchiveEntry.class));
        doNothing().when(mockOutput).closeArchiveEntry();

        performer.copyStream(mockInput, mockOutput, mockEntry);

        // Verify calls in ArchiveOutputStream
        verify(mockOutput).putArchiveEntry(any(ArchiveEntry.class));
        verify(mockOutput).closeArchiveEntry();

        // Verify calls in IOUtils
        verify(IOUtils).copy(mockInput, mockOutput);
    }

}