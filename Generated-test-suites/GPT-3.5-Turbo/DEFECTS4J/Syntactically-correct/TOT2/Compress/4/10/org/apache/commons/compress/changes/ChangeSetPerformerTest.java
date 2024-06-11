import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.changes.ChangeSetResults;

public class ChangeSetPerformerTest {
  
  private ChangeSetPerformer performer;
  private ArchiveInputStream in;
  private ArchiveOutputStream out;

  @Before
  public void setUp() {
    ChangeSet changeSet = new ChangeSet();
    Change change = new Change();
    change.setEntry(new ArchiveEntry());
    changeSet.addChange(change);
    performer = new ChangeSetPerformer(changeSet);
    in = new ArchiveInputStream(new ByteArrayInputStream("Test".getBytes()));
    out = new ArchiveOutputStream(new ByteArrayOutputStream());
  }

  @After
  public void tearDown() {
    // Clean up resources
  }

  @Test
  public void testPerform() throws IOException {
    ChangeSetResults results = performer.perform(in, out);
    assertEquals(0, results.getAddedFromChangeSet().size());
    assertEquals(0, results.getDeletedEntries().size());
    assertEquals(0, results.getAddedFromStream().size());
  }

  @Test
  public void testIsDeletedLater() {
    Set workingSet = new LinkedHashSet();
    ArchiveEntry entry = new ArchiveEntry();
    entry.setName("test");
    assertFalse(performer.isDeletedLater(workingSet, entry));
  }

  @Test
  public void testCopyStream() throws IOException {
    ByteArrayInputStream input = new ByteArrayInputStream("Test".getBytes());
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    ArchiveEntry entry = new ArchiveEntry();
    entry.setName("test");
    performer.copyStream(input, new ArchiveOutputStream(output), entry);
    assertEquals("Test", new String(output.toByteArray()));
  }
}