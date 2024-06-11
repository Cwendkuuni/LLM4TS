import org.junit.Test;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.apache.commons.compress.utils.IOUtils;

public class ChangeSetPerformerTest {

  @Test
  public void testConstructorAndGetChanges() {
    Set<Change> changes = new LinkedHashSet<>();
    ChangeSet changeSet = mock(ChangeSet.class);
    when(changeSet.getChanges()).thenReturn(changes);

    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

    assertSame(changes, performer.getChanges());
  }

  @Test
  public void testPerform_AddReplaceMode_InputStreamNotAdded() throws IOException {
    ChangeSet changeSet = mock(ChangeSet.class);
    setChanges(changeSet, createAddChange(Change.TYPE_ADD, true));
    ArchiveInputStream in = mock(ArchiveInputStream.class);
    ArchiveOutputStream out = mock(ArchiveOutputStream.class);
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

    performer.perform(in, out);

    verify(out, times(1)).putArchiveEntry(any(ArchiveEntry.class));
    verify(out, times(1)).closeArchiveEntry();
  }

  @Test
  public void testPerform_AddReplaceMode_InputStreamAdded() throws IOException {
    ChangeSet changeSet = mock(ChangeSet.class);
    setChanges(changeSet, createAddChange(Change.TYPE_ADD, true));
    ArchiveInputStream in = mock(ArchiveInputStream.class);
    ArchiveOutputStream out = mock(ArchiveOutputStream.class);
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
    ChangeSetResults results = performer.perform(in, out);

    assert(results.hasBeenAdded("testEntry"));
  }

  @Test
  public void testPerform_AddNotReplaceMode_InputStreamNotAdded() throws IOException {
    ChangeSet changeSet = mock(ChangeSet.class);
    setChanges(changeSet, createAddChange(Change.TYPE_ADD, false));
    ArchiveInputStream in = mock(ArchiveInputStream.class);
    ArchiveOutputStream out = mock(ArchiveOutputStream.class);
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

    performer.perform(in, out);

    verify(out, times(1)).putArchiveEntry(any(ArchiveEntry.class));
    verify(out, times(1)).closeArchiveEntry();
  }

  @Test
  public void testPerform_AddNotReplaceMode_InputStreamAdded() throws IOException {
    ChangeSet changeSet = mock(ChangeSet.class);
    setChanges(changeSet, createAddChange(Change.TYPE_ADD, false));
    ArchiveInputStream in = mock(ArchiveInputStream.class);
    ArchiveOutputStream out = mock(ArchiveOutputStream.class);
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
    ChangeSetResults results = performer.perform(in, out);

    assert(results.hasBeenAdded("testEntry"));
  }

  @Test
  public void testPerform_DeleteFile() throws IOException {
    ChangeSet changeSet = mock(ChangeSet.class);
    setChanges(changeSet, createDeleteChange(Change.TYPE_DELETE));
    ArchiveInputStream in = mock(ArchiveInputStream.class);
    ArchiveOutputStream out = mock(ArchiveOutputStream.class);
    ArchiveEntry entry = mock(ArchiveEntry.class);
    when(entry.getName()).thenReturn("testEntry");
    when(in.getNextEntry()).thenReturn(entry).thenReturn(null);
    when(out.putArchiveEntry(any(ArchiveEntry.class))).thenReturn(true);
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
    ChangeSetResults results = performer.perform(in, out);

    verify(out, times(1)).closeArchiveEntry();
    assert(results.getDeleted().contains("testEntry"));
  }

  @Test
  public void testPerform_DeleteDirectory() throws IOException {
    ChangeSet changeSet = mock(ChangeSet.class);
    setChanges(changeSet, createDeleteChange(Change.TYPE_DELETE_DIR));
    ArchiveInputStream in = mock(ArchiveInputStream.class);
    ArchiveOutputStream out = mock(ArchiveOutputStream.class);
    ArchiveEntry entry = mock(ArchiveEntry.class);
    when(entry.getName()).thenReturn("testDirectory/entry");
    when(in.getNextEntry()).thenReturn(entry).thenReturn(null);
    when(out.putArchiveEntry(any(ArchiveEntry.class))).thenReturn(true);
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
    ChangeSetResults results = performer.perform(in, out);

    verify(out, times(1)).closeArchiveEntry();
    assert(results.getDeleted().contains("testDirectory/entry"));
  }

  @Test
  public void testPerform_CopyEntryNotDeleted() throws IOException {
    ChangeSet changeSet = mock(ChangeSet.class);
    setChanges(changeSet, createAddChange(Change.TYPE_ADD, true));
    ArchiveInputStream in = mock(ArchiveInputStream.class);
    ArchiveOutputStream out = mock(ArchiveOutputStream.class);
    ArchiveEntry entry = mock(ArchiveEntry.class);
    when(entry.getName()).thenReturn("testEntry");
    when(in.getNextEntry()).thenReturn(entry).thenReturn(null);
    when(out.putArchiveEntry(any(ArchiveEntry.class))).thenReturn(true);
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
    ChangeSetResults results = performer.perform(in, out);

    verify(out, times(1)).closeArchiveEntry();
    assert(results.hasBeenAdded("testEntry"));
  }

  @Test
  public void testPerform_CopyEntryDeleted() throws IOException {
    ChangeSet changeSet = mock(ChangeSet.class);
    setChanges(changeSet, createAddChange(Change.TYPE_ADD, true));
    ArchiveInputStream in = mock(ArchiveInputStream.class);
    ArchiveOutputStream out = mock(ArchiveOutputStream.class);
    ArchiveEntry entry = mock(ArchiveEntry.class);
    when(entry.getName()).thenReturn("testEntry");
    when(in.getNextEntry()).thenReturn(entry).thenReturn(null);
    when(out.putArchiveEntry(any(ArchiveEntry.class))).thenReturn(true);
    Set<Change> workingSet = new HashSet<>();
    workingSet.add(new Change(Change.TYPE_DELETE, "testEntry", null));
    when(changeSet.getChanges()).thenReturn(workingSet);
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
    ChangeSetResults results = performer.perform(in, out);

    verify(out, times(1)).closeArchiveEntry();
    assert(!results.hasBeenAdded("testEntry"));
  }

  @Test
  public void testPerform_CopyEntryAdded() throws IOException {
    ChangeSet changeSet = mock(ChangeSet.class);
    setChanges(changeSet, createAddChange(Change.TYPE_ADD, true));
    ArchiveInputStream in = mock(ArchiveInputStream.class);
    ArchiveOutputStream out = mock(ArchiveOutputStream.class);
    ArchiveEntry entry = mock(ArchiveEntry.class);
    when(entry.getName()).thenReturn("testEntry");
    when(in.getNextEntry()).thenReturn(entry).thenReturn(null);
    when(out.putArchiveEntry(any(ArchiveEntry.class))).thenReturn(true);
    Set<Change> workingSet = new HashSet<>();
    workingSet.add(new Change(Change.TYPE_ADD, "testEntry", null));
    when(changeSet.getChanges()).thenReturn(workingSet);
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
    ChangeSetResults results = performer.perform(in, out);

    verify(out, times(1)).closeArchiveEntry();
    assert(results.hasBeenAdded("testEntry"));
  }

  @Test
  public void testPerform_CopyEntryNotAdded() throws IOException {
    ChangeSet changeSet = mock(ChangeSet.class);
    setChanges(changeSet, createAddChange(Change.TYPE_ADD, false));
    ArchiveInputStream in = mock(ArchiveInputStream.class);
    ArchiveOutputStream out = mock(ArchiveOutputStream.class);
    ArchiveEntry entry = mock(ArchiveEntry.class);
    when(entry.getName()).thenReturn("testEntry");
    when(in.getNextEntry()).thenReturn(entry).thenReturn(null);
    when(out.putArchiveEntry(any(ArchiveEntry.class))).thenReturn(true);
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
    ChangeSetResults results = performer.perform(in, out);

    verify(out, times(1)).closeArchiveEntry();
    assert(results.hasBeenAdded("testEntry"));
  }

  private ChangeSet createAddChange(int type, boolean isReplaceMode) {
    try {
    InputStream input = mock(InputStream.class);
    ArchiveEntry entry = mock(ArchiveEntry.class);
    return new Change(type, "testEntry", null, input, entry, isReplaceMode);
    } catch(IOException e) {
      return null;
    }
  }

  private ChangeSet createDeleteChange(int type) {
    return new Change(type, "testEntry", null);
  }

  private void setChanges(ChangeSet changeSet, Change... changes) {
    Set<Change> changeSetList = new LinkedHashSet<>();
    for (Change change : changes) {
      changeSetList.add(change);
    }
    when(changeSet.getChanges()).thenReturn(changeSetList);
  }

}