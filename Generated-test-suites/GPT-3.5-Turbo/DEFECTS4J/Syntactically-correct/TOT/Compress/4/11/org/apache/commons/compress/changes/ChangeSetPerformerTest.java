import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;
    private byte[] input;
    
    @Before
    public void setUp() throws IOException {
        // Prepare the input stream
        ByteArrayOutputStream inputOutputStream = new ByteArrayOutputStream();
        try (ZipArchiveOutputStream inputArchive = new ZipArchiveOutputStream(inputOutputStream)) {
            ZipArchiveEntry entry = new ZipArchiveEntry("file.txt");
            inputArchive.putArchiveEntry(entry);
            inputArchive.write("Hello, World!".getBytes());
            inputArchive.closeArchiveEntry();
        }
        input = inputOutputStream.toByteArray();
        
        // Prepare the change set with a delete operation
        changeSet = new ChangeSet();
        changeSet.delete("file.txt");
        
        // Initialize the performer
        performer = new ChangeSetPerformer(changeSet);
    }
    
    @Test
    public void testPerform_DeletesEntry() throws IOException {
        // Create output stream to capture the modifications
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (ZipArchiveOutputStream outArchive = new ZipArchiveOutputStream(output)) {
            // Create input stream for the input data
            try (ZipArchiveInputStream inArchive = new ZipArchiveInputStream(new ByteArrayInputStream(input))) {
                // Perform the change set on the input stream
                performer.perform(inArchive, outArchive);
            }
        }
        
        // Verify the output
        byte[] outputBytes = output.toByteArray();
        try (ZipArchiveInputStream outArchive = new ZipArchiveInputStream(new ByteArrayInputStream(outputBytes))) {
            ZipArchiveEntry entry = outArchive.getNextZipEntry();
            assertNull("The entry should have been deleted", entry);
        }
    }
    
    @Test
    public void testPerform_AddsEntry() throws IOException {
        // Add an add operation to the change set
        changeSet.add("newfile.txt", new ByteArrayInputStream("New file content".getBytes()));
        
        // Create output stream to capture the modifications
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (ZipArchiveOutputStream outArchive = new ZipArchiveOutputStream(output)) {
            // Create input stream for the input data
            try (ZipArchiveInputStream inArchive = new ZipArchiveInputStream(new ByteArrayInputStream(input))) {
                // Perform the change set on the input stream
                performer.perform(inArchive, outArchive);
            }
        }
        
        // Verify the output
        byte[] outputBytes = output.toByteArray();
        try (ZipArchiveInputStream outArchive = new ZipArchiveInputStream(new ByteArrayInputStream(outputBytes))) {
            ZipArchiveEntry entry = outArchive.getNextZipEntry();
            assertNotNull("The entry should have been added", entry);
            assertEquals("newfile.txt", entry.getName());
            assertEquals("New file content", extractContent(outArchive));
        }
    }
    
    @Test
    public void testPerform_ReplacesEntry() throws IOException {
        // Add a replace operation to the change set
        changeSet.add("file.txt", new ByteArrayInputStream("Modified content".getBytes()));
        
        // Create output stream to capture the modifications
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (ZipArchiveOutputStream outArchive = new ZipArchiveOutputStream(output)) {
            // Create input stream for the input data
            try (ZipArchiveInputStream inArchive = new ZipArchiveInputStream(new ByteArrayInputStream(input))) {
                // Perform the change set on the input stream
                performer.perform(inArchive, outArchive);
            }
        }
        
        // Verify the output
        byte[] outputBytes = output.toByteArray();
        try (ZipArchiveInputStream outArchive = new ZipArchiveInputStream(new ByteArrayInputStream(outputBytes))) {
            ZipArchiveEntry entry = outArchive.getNextZipEntry();
            assertNotNull("The entry should have been added", entry);
            assertEquals("file.txt", entry.getName());
            assertEquals("Modified content", extractContent(outArchive));
        }
    }
    
    @Test
    public void testPerform_ErrorOnInvalidChangeSet() throws IOException {
        // Add an invalid operation to the change set
        changeSet.add("newfile.txt", null);
        
        // Create output stream to capture the modifications
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (ZipArchiveOutputStream outArchive = new ZipArchiveOutputStream(output)) {
            // Create input stream for the input data
            try (ZipArchiveInputStream inArchive = new ZipArchiveInputStream(new ByteArrayInputStream(input))) {
                // Perform the change set on the input stream
                try {
                    performer.perform(inArchive, outArchive);
                    fail("Should have thrown an exception");
                } catch (IOException e) {
                    // Expected exception
                }
            }
        }
        
        // Verify that no modifications were made
        byte[] outputBytes = output.toByteArray();
        assertEquals("The output should be the same as input", input.length, outputBytes.length);
    }
    
    private String extractContent(ZipArchiveInputStream archive) throws IOException {
        ByteArrayOutputStream entryContent = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = archive.read(buffer)) != -1) {
            entryContent.write(buffer, 0, read);
        }
        return entryContent.toString();
    }
    
}