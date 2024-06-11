package org.apache.commons.compress.changes;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class ChangeSetPerformerTest {

 private ChangeSet changeSet;
 private ChangeSetPerformer performer;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 changeSet = new ChangeSet();
 changeSet.addChange(new Change(Change.TYPE_ADD, "file1.txt", null, null));
 changeSet.addChange(new Change(Change.TYPE_DELETE, "file1.txt", null, null));
 changeSet.addChange(new Change(Change.TYPE_ADD, "file2.txt", null, null, true));
 changeSet.addChange(new Change(Change.TYPE_ADD, "dir1/file3.txt", null, null, true));
 changeSet.addChange(new Change(Change.TYPE_DELETE_DIR, "dir1", null, null));
 performer = new ChangeSetPerformer(changeSet);
 }

 @Test
 public void testPerformWithAddAndReplaceMode() throws Exception {
 ArchiveInputStream in = new MockArchiveInputStream();
 ArchiveOutputStream out = new MockArchiveOutputStream();
 ChangeSetResults result = performer.perform(in, out);
 assertFalse(result.hasBeenAdded("file1.txt"));
 assertTrue(result.hasBeenAdded("file2.txt"));
 }

 @Test(expected = IOException.class)
 public void testPerformWithDeleteAndNonExistingFile() throws Exception {
 ArchiveInputStream in = new MockArchiveInputStream();
 ArchiveOutputStream out = new MockArchiveOutputStream();
 performer.perform(in, out);
 fail("Expected IOException");
 }

 @Test
 public void testPerformWithAddAndNonReplaceMode() throws Exception {
 ArchiveInputStream in = new MockArchiveInputStream();
 ArchiveOutputStream out = new MockArchiveOutputStream();
 ChangeSetResults result = performer.perform(in, out);
 assertTrue(result.hasBeenAdded("file1.txt"));
 }

 @Test
 public void testPerformWithDeleteAndExistingFile() throws Exception {
 ArchiveInputStream in = new MockArchiveInputStreamWithEntry("file1.txt");
 ArchiveOutputStream out = new MockArchiveOutputStream();
 performer.perform(in, out);
 assertTrue(in.getNextEntry() == null);
 }

 @Test
 public void testPerformWithAddAndDeleteLater() throws Exception {
 ArchiveInputStream in = new MockArchiveInputStreamWithEntry("file1.txt");
 ArchiveOutputStream out = new MockArchiveOutputStream();
 ChangeSetResults result = performer.perform(in, out);
 assertFalse(result.hasBeenAdded("file1.txt"));
 }

 @Test
 public void testPerformWithAddAndDeleteSameFileNameButDifferentPath() throws Exception {
 ArchiveInputStream in = new MockArchiveInputStreamWithEntry("file1.txt");
 ArchiveOutputStream out = new MockArchiveOutputStream();
 ChangeSetResults result = performer.perform(in, out);
 assertFalse(result.hasBeenAdded("file1.txt"));
 }

 @Test
 public void testPerformWithDeleteDir() throws Exception {
 ArchiveInputStream in = new MockArchiveInputStream();
 ArchiveOutputStream out = new MockArchiveOutputStream();
 performer.perform(in, out);
 assertFalse(in.getNextEntry() != null);
 }

 @Test
 public void testPerformWithAddAndDeleteDirLater() throws Exception {
 ArchiveInputStream in = new MockArchiveInputStreamWithEntry("dir1/file3.txt");
 ArchiveOutputStream out = new MockArchiveOutputStream();
 ChangeSetResults result = performer.perform(in, out);
 assertTrue(result.hasBeenAdded("file3.txt"));
 }

 @Test
 public void testPerformWithAddAndDeleteSameDirNameButDifferentPath() throws Exception {
 ArchiveInputStream in = new MockArchiveInputStreamWithEntry("dir1/file3.txt");
 ArchiveOutputStream out = new MockArchiveOutputStream();
 ChangeSetResults result = performer.perform(in, out);
 assertFalse(result.hasBeenAdded("file3.txt"));
 }

}

public class MockArchiveInputStream extends ArchiveInputStream {
 @Override
 public ArchiveEntry getNextEntry() throws IOException {
 return null;
 }
}

public class MockArchiveOutputStream extends ArchiveOutputStream {
 @Override
 public void putArchiveEntry(ArchiveEntry entry) throws IOException {
 super.putNextEntry(entry);
 }
}

public class MockArchiveInputStreamWithEntry extends MockArchiveInputStream {
 private final ArchiveEntry entry;

 public MockArchiveInputStreamWithEntry(String name) {
 entry = new MockArchiveEntry(name);
 }

 @Override
 public ArchiveEntry getNextEntry() throws IOException {
 return entry;
 }
}

public class MockArchiveEntry implements ArchiveEntry {
 private final String name;

 public MockArchiveEntry(String name) {
 this.name = name;
 }

 @Override
 public String getName() {
 return name;
 }
}