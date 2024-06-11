package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {
    
    private ChangeSetPerformer performer;
    
    @Before
    public void setUp() {
        ChangeSet changeSet = new ChangeSet();
        performer = new ChangeSetPerformer(changeSet);
    }
    
    @Test
    public void testPerform() throws IOException {
        // Prepare input data
        String input = "Hello, world!";
        byte[] inputData = input.getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(inputData);
        
        // Prepare output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOut = null;
        
        try {
            // Perform the changes
            ChangeSetResults results = performer.perform(createMockArchiveInputStream(in), archiveOut);
            
            // Check the result of performing the changes
            assertEquals(0, results.getAddedFromChangeSet().size());
            assertEquals(0, results.getDeleted().size());
            assertEquals(0, results.getAddedFromStream().size());
            
            // Check the output data
            byte[] outputData = out.toByteArray();
            String output = new String(outputData);
            assertEquals(input, output);
        } finally {
            if (archiveOut != null) {
                archiveOut.close();
            }
        }
    }
    
    @Test
    public void testIsDeletedLater() {
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        // Create a set of changes
        Change change1 = new Change();
        change1.setType(Change.TYPE_DELETE);
        change1.setTargetFile("file1.txt");
        
        Change change2 = new Change();
        change2.setType(Change.TYPE_ADD);
        change2.setReplaceMode(false);
        change2.getEntry().setName("file2.txt");
        
        Change change3 = new Change();
        change3.setType(Change.TYPE_DELETE);
        change3.setTargetFile("file3.txt");
        
        Set changes = new LinkedHashSet();
        changes.add(change1);
        changes.add(change2);
        changes.add(change3);
        
        // Set the changes in the performer
        changeSet.setChanges(changes);
        
        // Create an ArchiveEntry to check
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("file2.txt");
        
        // Check if entry is deleted later in the workingSet
        boolean deletedLater = performer.isDeletedLater(changeSet.getChanges(), entry);
        
        assertTrue(deletedLater);
    }
    
    @Test
    public void testCopyStream() throws IOException {
        // Prepare input data
        String input = "Hello, world!";
        byte[] inputData = input.getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(inputData);
        
        // Prepare output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOut = null;
        
        try {
            // Create an ArchiveEntry to copy
            ArchiveEntry entry = new ArchiveEntry();
            entry.setName("file.txt");
            
            // Copy the stream
            performer.copyStream(in, archiveOut, entry);
            
            // Check the output data
            byte[] outputData = out.toByteArray();
            String output = new String(outputData);
            assertEquals(input, output);
        } finally {
            if (archiveOut != null) {
                archiveOut.close();
            }
        }
    }
    
    private ArchiveInputStream createMockArchiveInputStream(InputStream in) {
        // Implement a mock ArchiveInputStream for testing purposes
        return new ArchiveInputStream() {
            public ArchiveEntry getNextEntry() {
                return null;
            }
            public void close() throws IOException {
            }
            public int read(byte[] b) throws IOException {
                return 0;
            }
            public int read(byte[] b, int off, int len) throws IOException {
                return 0;
            }
            public long skip(long n) throws IOException {
                return 0;
            }
            public void closeArchiveEntry() throws IOException {
            }
        };
    }
}