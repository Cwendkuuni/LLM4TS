import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.ChangeSet;
import org.apache.commons.compress.Change;
import org.apache.commons.compress.ChangeSetResults;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

 @Test
 public void testPerform() throws IOException {
 ChangeSet changeSet = new ChangeSet();
 Change addChange = new Change(Change.TYPE_ADD, null, "testFile.txt", "testData".getBytes(), false, null);
 Change addReplaceChange = new Change(Change.TYPE_ADD, null, "testFile.txt", "testData".getBytes(), true, null);
 Change deleteChange = new Change(Change.TYPE_DELETE, null, "testFile.txt", null, false, null);
 Change deleteDirChange = new Change(Change.TYPE_DELETE_DIR, null, "testDir/", null, false, null);
 Change set = changeSet;
 set.addChange(addChange);
 set.addChange(addReplaceChange);
 set.addChange(deleteChange);
 set.addChange(deleteDirChange);

 ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

 ByteArrayOutputStream inputStreamOutput = new ByteArrayOutputStream();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ArchiveInputStream archiveInputStream = new ArchiveInputStream(inputStream);
 ArchiveOutputStream archiveOutputStream = new ArchiveOutputStream(outputStream);

 ChangeSetResults result = performer.perform(archiveInputStream, archiveOutputStream);

 assertEquals(2, result.getAddedFromChangeSet().size());
 assertEquals(1, result.getAddedFromStream().size());
 assertEquals(2, result.getDeleted().size());

 ArchiveEntry entry = archiveOutputStream.getNextEntry();
 assertEquals("testFile.txt", entry.getName());
 assertNotNull(entry.getSize());
 assertNotNull(entry.getCompressedSize());
 assertNotNull(entry.getCRC());

 inputStream = new ByteArrayInputStream(addChange.getInput().readAllBytes());
 assertEquals(addChange.getEntry().getName(), entry.getName());
 assertArrayEquals(addChange.getInput().readAllBytes(), IOUtils.toByteArray(inputStream));

 archiveInputStream = new ArchiveInputStream(new ByteArrayInputStream(addReplaceChange.getInput().readAllBytes()));
 assertEquals(addReplaceChange.getEntry().getName(), entry.getName());
 assertArrayEquals(addReplaceChange.getInput().readAllBytes(), IOUtils.toByteArray(archiveInputStream));

 entry = archiveInputStream.getNextEntry();
 assertEquals("testFile.txt", entry.getName());
 assertNull(entry.getSize());
 assertNull(entry.getCompressedSize());
 assertNull(entry.getCRC());

 assertEquals("testDir/", deleteDirChange.getTargetFile());
 assertTrue(result.isDeleted("testDir/"));

 assertFalse(result.isDeleted("testFile.txt"));
 assertFalse(result.isAdded("testFile.txt"));

 assertFalse(result.isAdded("testDir/"));
 assertFalse(result.isAdded("testFile.txt/"));
 }
}