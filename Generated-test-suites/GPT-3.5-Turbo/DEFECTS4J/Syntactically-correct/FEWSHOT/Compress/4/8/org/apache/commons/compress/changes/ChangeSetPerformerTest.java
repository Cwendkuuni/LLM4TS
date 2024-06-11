package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

public class ChangeSetPerformerTest {
    @Test
    public void testPerform() {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, "file1", false));
        changeSet.addChange(new Change(Change.TYPE_ADD, "file2", true));
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        ArchiveInputStream archiveIn = null;
        ArchiveOutputStream archiveOut = null;
        
        try {
            archiveIn = new ZipArchiveInputStream(in);
            archiveOut = new ZipArchiveOutputStream(out);
            
            ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
            ChangeSetResults results = performer.perform(archiveIn, archiveOut);
            
            assertTrue(results.hasBeenAdded("file1"));
            assertTrue(results.hasBeenAdded("file2"));
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        } finally {
            try {
                if (archiveIn != null) {
                    archiveIn.close();
                }
                if (archiveOut != null) {
                    archiveOut.close();
                }
            } catch (IOException e) {
                fail("IOException thrown: " + e.getMessage());
            }
        }
    }
    
    @Test
    public void testPerformWithDeleteChanges() {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_DELETE, "file1", false));
        changeSet.addChange(new Change(Change.TYPE_DELETE_DIR, "dir1", false));
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        ArchiveInputStream archiveIn = null;
        ArchiveOutputStream archiveOut = null;
        
        try {
            archiveIn = new TarArchiveInputStream(in);
            archiveOut = new TarArchiveOutputStream(out);
            
            ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
            ChangeSetResults results = performer.perform(archiveIn, archiveOut);
            
            assertTrue(results.deleted("file1"));
            assertTrue(results.deleted("dir1"));
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        } finally {
            try {
                if (archiveIn != null) {
                    archiveIn.close();
                }
                if (archiveOut != null) {
                    archiveOut.close();
                }
            } catch (IOException e) {
                fail("IOException thrown: " + e.getMessage());
            }
        }
    }
    
    @Test
    public void testPerformWithEntryAlreadyAdded() {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, "file1", false));
        changeSet.addChange(new Change(Change.TYPE_ADD, "file2", true));
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        ArchiveInputStream archiveIn = null;
        ArchiveOutputStream archiveOut = null;
        
        try {
            archiveIn = new ZipArchiveInputStream(in);
            archiveOut = new ZipArchiveOutputStream(out);
            
            // Add a file to the output stream
            ZipArchiveEntry entry = new ZipArchiveEntry("file1");
            archiveOut.putArchiveEntry(entry);
            archiveOut.closeArchiveEntry();
            
            ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
            ChangeSetResults results = performer.perform(archiveIn, archiveOut);
            
            assertTrue(results.hasBeenAdded("file2"));
            assertFalse(results.hasBeenAdded("file1"));
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        } finally {
            try {
                if (archiveIn != null) {
                    archiveIn.close();
                }
                if (archiveOut != null) {
                    archiveOut.close();
                }
            } catch (IOException e) {
                fail("IOException thrown: " + e.getMessage());
            }
        }
    }

    @Test
    public void testIsDeletedLater() {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_DELETE, "file1", false));
        changeSet.addChange(new Change(Change.TYPE_DELETE_DIR, "dir1", false));
        
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        ArchiveEntry entry1 = new TarArchiveEntry("file1");
        ArchiveEntry entry2 = new TarArchiveEntry("dir1/file2");
        ArchiveEntry entry3 = new TarArchiveEntry("dir1/file3");
        
        Set<Change> workingSet = new LinkedHashSet<>(changeSet.getChanges());
        
        boolean isDeletedLater1 = performer.isDeletedLater(workingSet, entry1);
        boolean isDeletedLater2 = performer.isDeletedLater(workingSet, entry2);
        boolean isDeletedLater3 = performer.isDeletedLater(workingSet, entry3);
        
        assertTrue(isDeletedLater1);
        assertTrue(isDeletedLater2);
        assertFalse(isDeletedLater3);
    }
    
    @Test
    public void testCopyStream(){
        ChangeSetPerformer performer = new ChangeSetPerformer();
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        ArchiveEntry entry = new TarArchiveEntry("file");
        
        try {
            performer.copyStream(in, out, entry);
            byte[] result = out.toByteArray();
            assertEquals(0, result.length);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                fail("IOException thrown: " + e.getMessage());
            }
        }
    }
}