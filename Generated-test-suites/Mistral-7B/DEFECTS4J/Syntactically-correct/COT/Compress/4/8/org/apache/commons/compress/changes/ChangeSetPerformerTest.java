import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.ChangeSet;
import org.apache.commons.compress.Change;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

 private ChangeSet changeSet;
 private ChangeSetPerformer performer;

 @Before
 public void setup() {
 Change addChange = new Change("add", "entry1.txt", null, null);
 Change replaceChange = new Change("replace", "entry2.txt", "entry1.txt", null);
 Change deleteChange = new Change("delete", null, "entry3.txt", null);
 Change deleteDirChange = new Change("delete_dir", null, "dir1/", null);
 Change set = new ChangeSet();
 set.addChange(addChange);
 set.addChange(replaceChange);
 set.addChange(deleteChange);
 set.addChange(deleteDirChange);
 changeSet = set;
 performer = new ChangeSetPerformer(changeSet);
 }

 @Test
 public void testConstructor() {
 Set changes = performer.getChanges();
 assertEquals(4, changes.size());
 }

 @Test
 public void testPerform_NoChanges() throws IOException {
 ArchiveInputStream in = new MockArchiveInputStream();
 ArchiveOutputStream out = new MockArchiveOutputStream();
 ChangeSetResults result = performer.perform(in, out);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testPerform_AddChange() throws IOException {
 ArchiveEntry entry = new MockArchiveEntry("entry1.txt");
 InputStream input = new ByteArrayInputStream("Test content".getBytes());
 ArchiveInputStream in = new MockArchiveInputStream(new MockChangeSetInputStream(input, new MockArchiveEntry("entry1.txt")));
 ArchiveOutputStream out = new MockArchiveOutputStream();
 ChangeSetResults result = performer.perform(in, out);
 assertEquals(1, result.getAddedFromChangeSet().size());
 assertEquals("entry1.txt", result.getAddedFromChangeSet().iterator().next());
 }

 @Test
 public void testPerform_ReplaceChange() throws IOException {
 ArchiveEntry oldEntry = new MockArchiveEntry("entry1.txt");
 ArchiveEntry newEntry = new MockArchiveEntry("entry2.txt");
 InputStream oldInput = new ByteArrayInputStream("Old content".getBytes());
 InputStream newInput = new ByteArrayInputStream("New content".getBytes());
 ArchiveInputStream in = new MockArchiveInputStream(new MockChangeSetInputStream(oldInput, oldEntry), new MockChangeSetInputStream(newInput, newEntry));
 ArchiveOutputStream out = new MockArchiveOutputStream();
 ChangeSetResults result = performer.perform(in, out);
 assertTrue(result.isEmpty());
 // Check that old and new file are not present in the output
 assertFalse(out.getNextEntry() != null);
 // Check that old file is deleted
 assertEquals(1, result.getDeleted().size());
 assertEquals("entry1.txt", result.getDeleted().iterator().next());
 }

 @Test
 public void testPerform_DeleteChange() throws IOException {
 ArchiveEntry entry = new MockArchiveEntry("entry3.txt");
 ArchiveInputStream in = new MockArchiveInputStream(new MockChangeSetInputStream(null, entry));
 ArchiveOutputStream out = new MockArchiveOutputStream();
 ChangeSetResults result = performer.perform(in, out);
 assertTrue(result.isEmpty());
 // Check that file is deleted
 assertEquals(1, result.getDeleted().size());
 assertEquals("entry3.txt", result.getDeleted().iterator().next());
 }

 @Test
 public void testPerform_DeleteDirChange() throws IOException {
 ArchiveEntry entry = new MockArchiveEntry("dir1/");
 ArchiveInputStream in = new MockArchiveInputStream();
 in.setArchive(new MockArchive(new MockArchiveEntry[] { new MockArchiveEntry("dir1/entry1.txt"), new MockArchiveEntry("dir1/entry2.txt") }));
 ArchiveOutputStream out = new MockArchiveOutputStream();
 ChangeSetResults result = performer.perform(in, out);
 assertTrue(result.isEmpty());
 // Check that dir is deleted
 assertEquals(1, result.getDeleted().size());
 assertEquals("dir1/", result.getDeleted().iterator().next());
 }

 @Test(expected = IOException.class)
 public void testPerform_ConcurrentModification() throws IOException {
 Set changes = performer.getChanges();
 changes.remove(changes.iterator().next());
 performer.perform(null, null);
 }

 @Test(expected = IOException.class)
 public void testPerform_NullInput() throws IOException {
 performer.perform(null, null);
 }

 @Test(expected = IOException.class)
 public void testPerform_NullOutput() throws IOException {
 performer.perform(new MockArchiveInputStream(), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerform_NullChangeSet() throws IOException {
 performer = new ChangeSetPerformer(null);
 performer.perform(new MockArchiveInputStream(), new MockArchiveOutputStream());
 }
}

class MockArchiveInputStream extends ArchiveInputStream {
 @Override
 public ArchiveEntry getNextEntry() {
 return null;
 }

 @Override
 public void setArchive(Archive archive) {
 // Do nothing
 }
}

class MockArchiveOutputStream extends ArchiveOutputStream {
 @Override
 public void putArchiveEntry(ArchiveEntry entry) {
 // Do nothing
 }

 @Override
 public void closeArchiveEntry() {
 // Do nothing
 }
}

class MockChangeSetInputStream extends InputStream {
 private final InputStream input;
 private final ArchiveEntry entry;

 public MockChangeSetInputStream(InputStream input, ArchiveEntry entry) {
 this.input = input;
 this.entry = entry;
 }

 @Override
 public int read() throws IOException {
 return input.read();
 }

 @Override
 public ArchiveEntry getEntry() {
 return entry;
 }
}

class MockArchive extends Archive {
 private final ArchiveEntry[] entries;

 public MockArchive(ArchiveEntry[] entries) {
 this.entries = entries;
 }

 @Override
 public ArchiveEntry getEntry(String name) {
 return Arrays.stream(entries).filter(ea -> ea.getName().equals(name)).findFirst().orElse(null);
 }
}