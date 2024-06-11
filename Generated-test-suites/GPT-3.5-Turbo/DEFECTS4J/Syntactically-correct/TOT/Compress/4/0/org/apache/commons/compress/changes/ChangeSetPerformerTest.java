import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class ChangeSetPerformerTest {

  private ChangeSetPerformer performer;

  @Before
  public void setup() {
    ChangeSet changeSet = new ChangeSet();
    performer = new ChangeSetPerformer(changeSet);
  }

  @Test
  public void testPerform_AddReplaceMode_EntryDoesNotExist_OutputStreamHasEntry() throws Exception {
    // Setup
    Change change = new Change();
    change.setType(Change.TYPE_ADD);
    change.setReplaceMode(true);
    ArchiveEntry entry = new ArchiveEntry();
    entry.setName("test-entry");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    performer.getChanges().add(change);

    // Execute
    performer.perform(new ArchiveInputStream(), out);

    // Verify
    Assert.assertTrue(out.containsEntry("test-entry"));
  }

  @Test
  public void testPerform_AddReplaceMode_EntryExists_OutputStreamUpdated() throws Exception {
    // Setup
    Change change = new Change();
    change.setType(Change.TYPE_ADD);
    change.setReplaceMode(true);
    ArchiveEntry entry = new ArchiveEntry();
    entry.setName("test-entry");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    out.putArchiveEntry(entry);
    out.closeArchiveEntry();
    performer.getChanges().add(change);

    // Execute
    performer.perform(new ArchiveInputStream(), out);

    // Verify
    Assert.assertFalse(out.containsEntry("test-entry"));
    Assert.assertTrue(out.containsEntry("test-entry (1)"));
  }

  @Test
  public void testPerform_Delete_EntryExists_OutputStreamUpdated() throws Exception {
    // Setup
    Change change = new Change();
    change.setType(Change.TYPE_DELETE);
    change.setTargetFile("test-entry");
    ArchiveEntry entry = new ArchiveEntry();
    entry.setName("test-entry");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    out.putArchiveEntry(entry);
    out.closeArchiveEntry();
    performer.getChanges().add(change);

    // Execute
    performer.perform(new ArchiveInputStream(), out);

    // Verify
    Assert.assertFalse(out.containsEntry("test-entry"));
  }

  @Test
  public void testPerform_DeleteDir_EntryExists_OutputStreamUpdated() throws Exception {
    // Setup
    Change change = new Change();
    change.setType(Change.TYPE_DELETE_DIR);
    change.setTargetFile("test-dir");
    ArchiveEntry entry1 = new ArchiveEntry();
    entry1.setName("test-dir/");
    ArchiveEntry entry2 = new ArchiveEntry();
    entry2.setName("test-dir/test-entry");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    out.putArchiveEntry(entry1);
    out.closeArchiveEntry();
    out.putArchiveEntry(entry2);
    out.closeArchiveEntry();
    performer.getChanges().add(change);

    // Execute
    performer.perform(new ArchiveInputStream(), out);

    // Verify
    Assert.assertFalse(out.containsEntry("test-dir/"));
    Assert.assertFalse(out.containsEntry("test-dir/test-entry"));
  }

  @Test
  public void testPerform_Copy_NoDeletion_NoAddition_EntryCopied() throws Exception {
    // Setup
    Change change1 = new Change();
    change1.setType(Change.TYPE_DELETE);
    change1.setTargetFile("test-entry");
    Change change2 = new Change();
    change2.setType(Change.TYPE_ADD);
    change2.setReplaceMode(false);
    ArchiveEntry entry1 = new ArchiveEntry();
    entry1.setName("test-entry");
    ArchiveEntry entry2 = new ArchiveEntry();
    entry2.setName("test-entry");
    ByteArrayInputStream in = new ByteArrayInputStream("test-entry-content".getBytes());
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    out.putArchiveEntry(entry1);
    out.write("test-entry-content".getBytes());
    out.closeArchiveEntry();
    performer.getChanges().add(change1);
    performer.getChanges().add(change2);

    // Execute
    performer.perform(new ArchiveInputStream(in), out);

    // Verify
    Assert.assertTrue(out.containsEntry("test-entry"));
    Assert.assertTrue(out.containsEntryWithContent("test-entry-content"));
  }
}