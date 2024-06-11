
package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    
    @Before
    public void setup() {
        changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
    }
    
    @Test
    public void testPerform_emptySet() {
        try (InputStream in = new ByteArrayInputStream(new byte[0]);
             ByteArrayOutputStream out = new ByteArrayOutputStream();
             ArchiveInputStream archiveIn = new ZipArchiveInputStream(in);
             ArchiveOutputStream archiveOut = new ZipArchiveOutputStream(out)) {
            
            changeSet.add(new AddChange("file.txt", new byte[0]));
            
            performer.perform(archiveIn, archiveOut);
            
            assertFalse(archiveOut.getRemaining() == 0);
            assertEquals(1, archiveOut.getRemaining());
            assertTrue(archiveOut.canWriteEntryData());
            
            ArchiveEntry entry = archiveOut.getNextEntry();
            assertEquals("file.txt", entry.getName());
            assertTrue(entry instanceof ZipArchiveEntry);
            
            byte[] data = new byte[10];
            int len = archiveOut.read(data);
            assertEquals(-1, len);
            
            archiveOut.closeArchiveEntry();
            
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testPerform_addExistingFileWithReplaceMode() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(out)) {
            
            zipOut.putNextEntry(new ZipEntry("file.txt"));
            zipOut.write("original".getBytes());
            zipOut.closeEntry();
            
            byte[] zipData = out.toByteArray();
            
            try (ByteArrayInputStream in = new ByteArrayInputStream(zipData);
                 ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                 ZipArchiveInputStream archiveIn = new ZipArchiveInputStream(in);
                 ArchiveOutputStream archiveOut = new ZipArchiveOutputStream(out2)) {
                
                changeSet.add(new AddChange("file.txt", "new".getBytes(), true));
                
                performer.perform(archiveIn, archiveOut);
                
                assertFalse(archiveOut.getRemaining() == 0);
                assertEquals(1, archiveOut.getRemaining());
                assertTrue(archiveOut.canWriteEntryData());
                
                ArchiveEntry entry = archiveOut.getNextEntry();
                assertEquals("file.txt", entry.getName());
                assertTrue(entry instanceof ZipArchiveEntry);
                
                byte[] data = new byte[10];
                int len = archiveOut.read(data);
                assertEquals(-1, len);
                
                archiveOut.closeArchiveEntry();
                
            } catch (IOException e) {
                fail("IOException thrown: " + e.getMessage());
            }
            
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testPerform_addExistingFileWithoutReplaceMode() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(out)) {
            
            zipOut.putNextEntry(new ZipEntry("file.txt"));
            zipOut.write("original".getBytes());
            zipOut.closeEntry();
            
            byte[] zipData = out.toByteArray();
            
            try (ByteArrayInputStream in = new ByteArrayInputStream(zipData);
                 ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                 ZipArchiveInputStream archiveIn = new ZipArchiveInputStream(in);
                 ArchiveOutputStream archiveOut = new ZipArchiveOutputStream(out2)) {
                
                changeSet.add(new AddChange("file.txt", "new".getBytes(), false));
                
                performer.perform(archiveIn, archiveOut);
                
                assertFalse(archiveOut.getRemaining() == 0);
                assertEquals(1, archiveOut.getRemaining());
                assertTrue(archiveOut.canWriteEntryData());
                
                ArchiveEntry entry = archiveOut.getNextEntry();
                assertEquals("file.txt", entry.getName());
                assertTrue(entry instanceof ZipArchiveEntry);
                
                byte[] data = new byte[10];
                int len = archiveOut.read(data);
                assertEquals(-1, len);
                
                archiveOut.closeArchiveEntry();
                
            } catch (IOException e) {
                fail("IOException thrown: " + e.getMessage());
            }
            
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testPerform_deleteFile() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(out)) {
            
            zipOut.putNextEntry(new ZipEntry("file.txt"));
            zipOut.write("original".getBytes());
            zipOut.closeEntry();
            
            byte[] zipData = out.toByteArray();
            
            try (ByteArrayInputStream in = new ByteArrayInputStream(zipData);
                 ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                 ZipArchiveInputStream archiveIn = new ZipArchiveInputStream(in);
                 ArchiveOutputStream archiveOut = new ZipArchiveOutputStream(out2)) {
                
                changeSet.add(new DeleteChange("file.txt"));
                
                performer.perform(archiveIn, archiveOut);
                
                assertFalse(archiveOut.getRemaining() == 0);
                assertEquals(0, archiveOut.getRemaining());
                
                ArchiveEntry entry = archiveOut.getNextEntry();
                assertNull(entry);
                
                byte[] data = new byte[10];
                int len = archiveOut.read(data);
                assertEquals(-1, len);
                
                archiveOut.closeArchiveEntry();
                
            } catch (IOException e) {
                fail("IOException thrown: " + e.getMessage());
            }
            
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testPerform_deleteDirectory() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(out)) {
            
            zipOut.putNextEntry(new ZipEntry("dir/file.txt"));
            zipOut.write("original".getBytes());
            zipOut.closeEntry();
            
            byte[] zipData = out.toByteArray();
            
            try (ByteArrayInputStream in = new ByteArrayInputStream(zipData);
                 ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                 ZipArchiveInputStream archiveIn = new ZipArchiveInputStream(in);
                 ArchiveOutputStream archiveOut = new ZipArchiveOutputStream(out2)) {
                
                changeSet.add(new DeleteChange("dir"));
                
                performer.perform(archiveIn, archiveOut);
                
                assertFalse(archiveOut.getRemaining() == 0);
                assertEquals(0, archiveOut.getRemaining());
                
                ArchiveEntry entry = archiveOut.getNextEntry();
                assertNull(entry);
                
                byte[] data = new byte[10];
                int len = archiveOut.read(data);
                assertEquals(-1, len);
                
                archiveOut.closeArchiveEntry();
                
            } catch (IOException e) {
                fail("IOException thrown: " + e.getMessage());
            }
            
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testPerform_multipleChanges() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(out)) {
            
            zipOut.putNextEntry(new ZipEntry("file.txt"));
            zipOut.write("original".getBytes());
            zipOut.closeEntry();
            
            byte[] zipData = out.toByteArray();
            
            try (ByteArrayInputStream in = new ByteArrayInputStream(zipData);
                 ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                 ZipArchiveInputStream archiveIn = new ZipArchiveInputStream(in);
                 ArchiveOutputStream archiveOut = new ZipArchiveOutputStream(out2)) {
                
                changeSet.add(new ReplaceChange("file.txt", "new".getBytes()));
                changeSet.add(new DeleteChange("delete.txt"));
                changeSet.add(new AddChange("file2.txt", "new".getBytes()));
                changeSet.add(new DeleteChange("dir/"));
                
                performer.perform(archiveIn, archiveOut);
                
                assertFalse(archiveOut.getRemaining() == 0);
                assertEquals(1, archiveOut.getRemaining());
                
                ArchiveEntry entry = archiveOut.getNextEntry();
                assertEquals("file2.txt", entry.getName());
                assertTrue(entry instanceof ZipArchiveEntry);
                
                byte[] data = new byte[10];
                int len = archiveOut.read(data);
                assertEquals(-1, len);
                
                archiveOut.closeArchiveEntry();
                
            } catch (IOException e) {
                fail("IOException thrown: " + e.getMessage());
            }
            
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testIsDeletedLater_entryNotDeleted() {
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(new DeleteChange("delete.txt"));
        
        ZipArchiveEntry entry = new ZipArchiveEntry("file.txt");
        
        assertFalse(performer.isDeletedLater(workingSet, entry));
    }
    
    @Test
    public void testIsDeletedLater_entryDeletedSameName() {
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(new DeleteChange("file.txt"));
        
        ZipArchiveEntry entry = new ZipArchiveEntry("file.txt");
        
        assertTrue(performer.isDeletedLater(workingSet, entry));
    }
    
    @Test
    public void testIsDeletedLater_entryDeletedDifferentName() {
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(new DeleteChange("file.txt"));
        
        ZipArchiveEntry entry = new ZipArchiveEntry("file2.txt");
        
        assertFalse(performer.isDeletedLater(workingSet, entry));
    }
    
    @Test
    public void testIsDeletedLater_entryDeletedSubdirectory() {
        Set<Change> workingSet = new LinkedHashSet<>();
        workingSet.add(new DeleteChange("dir/"));
        
        ZipArchiveEntry entry = new ZipArchiveEntry("dir/file.txt");
        
        assertTrue(performer.isDeletedLater(workingSet, entry));
    }
    
    @Test
    public void testCopyStream() {
        try (ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
             ByteArrayOutputStream out = new ByteArrayOutputStream();
             ZipArchiveOutputStream archiveOut = new ZipArchiveOutputStream(out)) {
            
            ArchiveEntry entry = new ZipArchiveEntry("file.txt");
            
            performer.copyStream(in, archiveOut, entry);
            
            byte[] zipData = out.toByteArray();
            assertNotNull(zipData);
            assertTrue(zipData.length == 0);
            
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

}
