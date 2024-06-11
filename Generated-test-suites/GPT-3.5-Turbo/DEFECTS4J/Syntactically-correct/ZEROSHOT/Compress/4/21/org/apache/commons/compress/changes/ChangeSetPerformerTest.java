import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

  private ChangeSetPerformer performer;
  private ChangeSet changeSet;
  private ArchiveInputStream in;
  private ArchiveOutputStream out;

  @Before
  public void setUp() {
    changeSet = new ChangeSet();
    performer = new ChangeSetPerformer(changeSet);
    in = null; // initialize input stream
    out = null;  // initialize output stream
  }

  @Test
  public void testPerform() throws IOException {
    ChangeSetResults results = performer.perform(in, out);

    // assert statements
    assertNotNull(results);
  }

  @Test
  public void testIsDeletedLater() {
    // prepare test data
    Set<Change> workingSet = new LinkedHashSet<>();
    ArchiveEntry entry = null; // initialize entry

    // call the method
    boolean result = performer.isDeletedLater(workingSet, entry);

    // assert statements
    assertFalse(result);
  }

  @Test
  public void testCopyStream() throws IOException {
    // prepare test data
    InputStream in = null; // initialize input stream
    ArchiveOutputStream out = null; // initialize output stream
    ArchiveEntry entry = null; // initialize entry

    // call the method
    performer.copyStream(in, out, entry);

    // assert statements
    // add relevant assertions here      
  }
}