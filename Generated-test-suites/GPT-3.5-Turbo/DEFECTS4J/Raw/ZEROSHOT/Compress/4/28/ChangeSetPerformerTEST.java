import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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

  private ChangeSetPerformer changeSetPerformer;

  @Before
  public void setUp() {
    ChangeSet changeSet = mock(ChangeSet.class);
    Set<Change> changes = new LinkedHashSet<>();
    changeSetPerformer = new ChangeSetPerformer(changeSet);

    when(changeSet.getChanges()).thenReturn(changes);
  }

  @Test
  public void testPerform() throws IOException {
    ArchiveEntry entry = mock(ArchiveEntry.class);
    ArchiveInputStream in = mock(ArchiveInputStream.class);
    ArchiveOutputStream out = mock(ArchiveOutputStream.class);
    ChangeSetResults results = mock(ChangeSetResults.class);
    Iterator it = mock(Iterator.class);

    when(in.getNextEntry()).thenReturn(entry, null);
    when(changeSetPerformer.perform(in, out)).thenReturn(results);
    when(it.hasNext()).thenReturn(true, false);
    when(it.next()).thenReturn(mock(Change.class));
    when(entry.getName()).thenReturn("testEntry");
    when(results.hasBeenAdded(any(String.class))).thenReturn(false);
    when(changeSetPerformer.isDeletedLater(any(Set.class), any(ArchiveEntry.class))).thenReturn(false);

    ChangeSetResults actualResult = changeSetPerformer.perform(in, out);

    assertNotNull(actualResult);
    verify(changeSetPerformer, times(1)).perform(in, out);
    verify(in, times(1)).getNextEntry();
    verify(results, times(1)).hasBeenAdded("testEntry");
    verify(changeSetPerformer, times(0)).isDeletedLater(any(Set.class), any(ArchiveEntry.class));
  }

  @Test
  public void testIsDeletedLater() {
    Set workingSet = new LinkedHashSet<>();
    ArchiveEntry entry = mock(ArchiveEntry.class);
    Change change = mock(Change.class);

    when(change.type()).thenReturn(Change.TYPE_DELETE_DIR);
    when(change.targetFile()).thenReturn("testDir");
    when(entry.getName()).thenReturn("testDir/entry");

    workingSet.add(change);

    boolean actualResult = changeSetPerformer.isDeletedLater(workingSet, entry);

    assertTrue(actualResult);
    verify(change, times(3)).type();
    verify(change, times(1)).targetFile();
    verify(change, times(1)).type();
  }

  @Test
  public void testCopyStream() throws IOException {
    InputStream in = mock(InputStream.class);
    ArchiveOutputStream out = mock(ArchiveOutputStream.class);
    ArchiveEntry entry = mock(ArchiveEntry.class);

    changeSetPerformer.copyStream(in, out, entry);

    verify(out, times(1)).putArchiveEntry(entry);
    verify(in, times(1)).copy(in, out);
    verify(out, times(1)).closeArchiveEntry();
  }
}