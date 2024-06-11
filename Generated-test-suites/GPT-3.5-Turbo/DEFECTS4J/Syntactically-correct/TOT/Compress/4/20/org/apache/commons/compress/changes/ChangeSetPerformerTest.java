import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;

    @Before
    public void setUp() {
        changeSet = Mockito.mock(ChangeSet.class);
    }

    @Test
    public void testPerform_withReplaceModeAndAddedFromChangeSet() throws IOException {
        // Setup
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = new ChangeSetResults();
        Change change1 = Mockito.mock(Change.class);
        Change change2 = Mockito.mock(Change.class);
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change1);
        changes.add(change2);
        when(changeSet.getChanges()).thenReturn(changes);
        InputStream input = Mockito.mock(InputStream.class);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        when(change1.type()).thenReturn(Change.TYPE_ADD);
        when(change1.isReplaceMode()).thenReturn(true);
        when(change1.getInput()).thenReturn(input);
        when(change1.getEntry()).thenReturn(entry);

        // Execute
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults actualResults = performer.perform(in, out);

        // Verify
        verify(out, times(1)).putArchiveEntry(entry);
        verify(in, times(0)).getNextEntry();
        verify(out, times(1)).closeArchiveEntry();
        assertEquals(results, actualResults);
    }

    @Test
    public void testPerform_withTypeDeleteAndTargetFileMatch() throws IOException {
        // Setup
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = new ChangeSetResults();
        Change change1 = Mockito.mock(Change.class);
        Change change2 = Mockito.mock(Change.class);
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change1);
        changes.add(change2);
        when(changeSet.getChanges()).thenReturn(changes);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        when(change1.type()).thenReturn(Change.TYPE_DELETE);
        when(change1.targetFile()).thenReturn("file1");
        when(entry.getName()).thenReturn("file1");

        // Execute
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults actualResults = performer.perform(in, out);

        // Verify
        verify(out, times(0)).putArchiveEntry(entry);
        verify(in, times(0)).getNextEntry();
        verify(out, times(0)).closeArchiveEntry();
        assertEquals(results, actualResults);
    }

    @Test
    public void testPerform_withTypeDeleteDirAndTargetFileMatch() throws IOException {
        // Setup
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = new ChangeSetResults();
        Change change1 = Mockito.mock(Change.class);
        Change change2 = Mockito.mock(Change.class);
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change1);
        changes.add(change2);
        when(changeSet.getChanges()).thenReturn(changes);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        when(change1.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change1.targetFile()).thenReturn("dir1");
        when(entry.getName()).thenReturn("dir1/file1");

        // Execute
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults actualResults = performer.perform(in, out);

        // Verify
        verify(out, times(0)).putArchiveEntry(entry);
        verify(in, times(0)).getNextEntry();
        verify(out, times(0)).closeArchiveEntry();
        assertEquals(results, actualResults);
    }

    @Test
    public void testPerform_withTypicalUseCases() throws IOException {
        // Setup
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = new ChangeSetResults();
        Change change1 = Mockito.mock(Change.class);
        Change change2 = Mockito.mock(Change.class);
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change1);
        changes.add(change2);
        when(changeSet.getChanges()).thenReturn(changes);
        InputStream input = Mockito.mock(InputStream.class);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        when(change1.type()).thenReturn(Change.TYPE_ADD);
        when(change1.isReplaceMode()).thenReturn(false);
        when(change1.getEntry()).thenReturn(entry);
        when(change1.getInput()).thenReturn(input);

        // Execute
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults actualResults = performer.perform(in, out);

        // Verify
        verify(out, times(1)).putArchiveEntry(entry);
        verify(in, times(1)).getNextEntry();
        verify(out, times(1)).closeArchiveEntry();
        assertEquals(results, actualResults);
    }

    @Test
    public void testPerform_withEdgeCases() throws IOException {
        // Setup
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = new ChangeSetResults();
        Set<Change> changes = new LinkedHashSet<>();
        when(changeSet.getChanges()).thenReturn(changes);

        // Execute
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults actualResults = performer.perform(in, out);

        // Verify
        verify(out, times(0)).putArchiveEntry(any());
        verify(in, times(0)).getNextEntry();
        verify(out, times(0)).closeArchiveEntry();
        assertEquals(results, actualResults);
    }

    @Test
    public void testPerform_withAddedFromChangeSet() throws IOException {
        // Setup
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = Mockito.mock(ChangeSetResults.class);
        Change change1 = Mockito.mock(Change.class);
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change1);
        when(changeSet.getChanges()).thenReturn(changes);
        InputStream input = Mockito.mock(InputStream.class);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        when(change1.type()).thenReturn(Change.TYPE_ADD);
        when(change1.isReplaceMode()).thenReturn(false);
        when(change1.getEntry()).thenReturn(entry);
        when(change1.getInput()).thenReturn(input);

        // Execute
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults actualResults = performer.perform(in, out);

        // Verify
        verify(out, times(1)).putArchiveEntry(entry);
        verify(in, times(1)).getNextEntry();
        verify(out, times(1)).closeArchiveEntry();
        assertEquals(results, actualResults);
    }

    @Test
    public void testPerform_withAddedFromStream() throws IOException {
        // Setup
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = new ChangeSetResults();
        Change change1 = Mockito.mock(Change.class);
        Change change2 = Mockito.mock(Change.class);
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change1);
        changes.add(change2);
        when(changeSet.getChanges()).thenReturn(changes);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        when(change1.type()).thenReturn(Change.TYPE_DELETE);
        when(entry.getName()).thenReturn("file1");

        // Execute
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults actualResults = performer.perform(in, out);

        // Verify
        verify(out, times(0)).putArchiveEntry(entry);
        verify(in, times(1)).getNextEntry();
        verify(out, times(1)).closeArchiveEntry();
        assertEquals(results, actualResults);
    }

    @Test
    public void testPerform_withTypeAddAndIsNotReplaceModeAndHasBeenAdded() throws IOException {
        // Setup
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = Mockito.mock(ChangeSetResults.class);
        Change change1 = Mockito.mock(Change.class);
        Set<Change> changes = new LinkedHashSet<>();
        changes.add(change1);
        when(changeSet.getChanges()).thenReturn(changes);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        when(change1.type()).thenReturn(Change.TYPE_ADD);
        when(change1.isReplaceMode()).thenReturn(false);
        when(change1.getEntry()).thenReturn(entry);
        when(results.hasBeenAdded(entry.getName())).thenReturn(true);

        // Execute
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults actualResults = performer.perform(in, out);

        // Verify
        verify(out, times(0)).putArchiveEntry(entry);
        verify(in, times(0)).getNextEntry();
        verify(out, times(0)).closeArchiveEntry();
        assertEquals(results, actualResults);
    }

    @Test
    public void testIsDeletedLater_withEmptyWorkingSet() {
        // Setup
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Set<Change> workingSet = new LinkedHashSet<>();

        // Execute
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        boolean actualResult = performer.isDeletedLater(workingSet, entry);

        // Verify
        assertFalse(actualResult);
    }

    @Test
    public void testIsDeletedLater_withTypeDeleteAndSourceMatchTarget() {
        // Setup
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Set<Change> workingSet = new LinkedHashSet<>();
        Change change1 = Mockito.mock(Change.class);
        when(change1.type()).thenReturn(Change.TYPE_DELETE);
        when(change1.targetFile()).thenReturn("file1");
        workingSet.add(change1);
        when(entry.getName()).thenReturn("file1");

        // Execute
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        boolean actualResult = performer.isDeletedLater(workingSet, entry);

        // Verify
        assertTrue(actualResult);
    }

    @Test
    public void testIsDeletedLater_withTypeDeleteDirAndSourceStartsWithTarget() {
        // Setup
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Set<Change> workingSet = new LinkedHashSet<>();
        Change change1 = Mockito.mock(Change.class);
        when(change1.type()).thenReturn(Change.TYPE_DELETE_DIR);
        when(change1.targetFile()).thenReturn("dir1");
        workingSet.add(change1);
        when(entry.getName()).thenReturn("dir1/file1");

        // Execute
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        boolean actualResult = performer.isDeletedLater(workingSet, entry);

        // Verify
        assertTrue(actualResult);
    }

    @Test
    public void testIsDeletedLater_withNoMatch() {
        // Setup
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        Set<Change> workingSet = new LinkedHashSet<>();
        Change change1 = Mockito.mock(Change.class);
        when(change1.type()).thenReturn(Change.TYPE_DELETE);
        when(change1.targetFile()).thenReturn("file1");
        workingSet.add(change1);
        when(entry.getName()).thenReturn("file2");

        // Execute
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        boolean actualResult = performer.isDeletedLater(workingSet, entry);

        // Verify
        assertFalse(actualResult);
    }

    @Test
    public void testCopyStream() throws IOException {
        // Setup
        InputStream in = Mockito.mock(InputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);

        // Execute
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.copyStream(in, out, entry);

        // Verify
        verify(out, times(1)).putArchiveEntry(entry);
        verify(in, times(1)).close();
        verify(out, times(1)).closeArchiveEntry();
    }

}