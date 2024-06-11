import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.changes.*;

public class ChangeSetPerformerTest {
  
  @Test
  public void testPerform_AddAndReplaceMode() throws IOException {
    ChangeSet changeSet = new ChangeSet();
    Change change = new Change();
    change.setType(Change.TYPE_ADD);
    change.setReplaceMode(true);
    change.setInput(new ByteArrayInputStream("Test".getBytes()));
    change.setEntry(ArchiveStreamFactory.createArchiveEntry("test.txt"));
    changeSet.addChange(change);
    
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
    ArchiveInputStream in = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream("Old".getBytes()));
    ArchiveOutputStream out = ArchiveStreamFactory.createArchiveOutputStream(new ByteArrayOutputStream());
    
    ChangeSetResults results = performer.perform(in, out);
    
    assertTrue(results.hasBeenAdded("test.txt"));
  }
  
  @Test
  public void testPerform_DeleteType() throws IOException {
    ChangeSet changeSet = new ChangeSet();
    Change change = new Change();
    change.setType(Change.TYPE_DELETE);
    change.setTargetFile("test.txt");
    changeSet.addChange(change);
    
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
    ArchiveInputStream in = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream("Old".getBytes()));
    ArchiveOutputStream out = ArchiveStreamFactory.createArchiveOutputStream(new ByteArrayOutputStream());
    
    ChangeSetResults results = performer.perform(in, out);
    
    assertTrue(results.hasBeenDeleted("test.txt"));
  }
  
  @Test
  public void testPerform_DeleteDirType() throws IOException {
    ChangeSet changeSet = new ChangeSet();
    Change change = new Change();
    change.setType(Change.TYPE_DELETE_DIR);
    change.setTargetFile("dir");
    changeSet.addChange(change);
    
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
    ArchiveInputStream in = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream("Old".getBytes()));
    ArchiveOutputStream out = ArchiveStreamFactory.createArchiveOutputStream(new ByteArrayOutputStream());
    
    ChangeSetResults results = performer.perform(in, out);
    
    assertTrue(results.hasBeenDeleted("dir/file.txt"));
  }
  
  @Test
  public void testPerform_AddNotInReplaceMode() throws IOException {
    ChangeSet changeSet = new ChangeSet();
    Change change = new Change();
    change.setType(Change.TYPE_ADD);
    change.setReplaceMode(false);
    change.setInput(new ByteArrayInputStream("Test".getBytes()));
    change.setEntry(ArchiveStreamFactory.createArchiveEntry("test.txt"));
    changeSet.addChange(change);
    
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
    ArchiveInputStream in = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream("Old".getBytes()));
    ArchiveOutputStream out = ArchiveStreamFactory.createArchiveOutputStream(new ByteArrayOutputStream());
    
    ChangeSetResults results = performer.perform(in, out);
    
    assertTrue(results.hasBeenAdded("test.txt"));
  }
  
  @Test
  public void testPerform_DeletedLater() throws IOException {
    ChangeSet changeSet = new ChangeSet();
    Change change1 = new Change();
    change1.setType(Change.TYPE_ADD);
    change1.setEntry(ArchiveStreamFactory.createArchiveEntry("test.txt"));
    changeSet.addChange(change1);
    
    Change change2 = new Change();
    change2.setType(Change.TYPE_DELETE);
    change2.setTargetFile("test.txt");
    changeSet.addChange(change2);
    
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
    ArchiveInputStream in = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream("Old".getBytes()));
    ArchiveOutputStream out = ArchiveStreamFactory.createArchiveOutputStream(new ByteArrayOutputStream());
    
    ChangeSetResults results = performer.perform(in, out);
    
    assertTrue(results.hasBeenDeleted("test.txt"));
  }
}