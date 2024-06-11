package org.apache.commons.compress.changes;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import static org.mockito.Mockito.when;

public class ChangeSetPerformerTest {
  
  @Test
  public void testPerform() throws IOException {
    ChangeSet changeSet = new ChangeSet();
    ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
    ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
    ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
    ChangeSetResults results = new ChangeSetResults();
    
    Change change1 = Mockito.mock(Change.class);
    when(change1.type()).thenReturn(Change.TYPE_ADD);
    when(change1.isReplaceMode()).thenReturn(true);
    InputStream input1 = Mockito.mock(InputStream.class);
    ArchiveEntry entry1 = Mockito.mock(ArchiveEntry.class);
    when(change1.getInput()).thenReturn(input1);
    when(change1.getEntry()).thenReturn(entry1);
    
    Change change2 = Mockito.mock(Change.class);
    when(change2.type()).thenReturn(Change.TYPE_DELETE);
    when(change2.targetFile()).thenReturn("file1.txt");
    
    Change change3 = Mockito.mock(Change.class);
    when(change3.type()).thenReturn(Change.TYPE_ADD);
    when(change3.isReplaceMode()).thenReturn(false);
    InputStream input3 = Mockito.mock(InputStream.class);
    ArchiveEntry entry3 = Mockito.mock(ArchiveEntry.class);
    when(change3.getInput()).thenReturn(input3);
    when(change3.getEntry()).thenReturn(entry3);
    
    Set changes = new LinkedHashSet();
    changes.add(change1);
    changes.add(change2);
    changes.add(change3);
    FieldUtils.setField(performer, "changes", changes);
    
    ChangeSetResults expectedResults = new ChangeSetResults();
    expectedResults.addedFromChangeSet("entry1.txt");
    expectedResults.deleted("file1.txt");
    expectedResults.addedFromChangeSet("entry3.txt");
    
    performer.perform(in, out);
    
    assertEquals(expectedResults, results);
  }

  @Test
  public void testIsDeletedLater() {
    ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
    ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
    Set workingSet = new LinkedHashSet();
    
    Change change1 = Mockito.mock(Change.class);
    when(change1.type()).thenReturn(Change.TYPE_DELETE);
    when(change1.targetFile()).thenReturn("file1.txt");
    
    Change change2 = Mockito.mock(Change.class);
    when(change2.type()).thenReturn(Change.TYPE_DELETE_DIR);
    when(change2.targetFile()).thenReturn("dir1");
    
    workingSet.add(change1);
    workingSet.add(change2);
    FieldUtils.setField(performer, "workingSet", workingSet);
    
    assertTrue(performer.isDeletedLater(workingSet, entry));
    
    when(entry.getName()).thenReturn("file2.txt");
    assertFalse(performer.isDeletedLater(workingSet, entry));
    
    when(entry.getName()).thenReturn("dir1/");
    assertTrue(performer.isDeletedLater(workingSet, entry));
  }

  @Test
  public void testCopyStream() throws IOException {
    ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
    InputStream in = Mockito.mock(InputStream.class);
    ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
    ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
    
    performer.copyStream(in, out, entry);
    
    Mockito.verify(out).putArchiveEntry(entry);
    Mockito.verify(IOUtils).copy(in, out);
    Mockito.verify(out).closeArchiveEntry();
  }
}