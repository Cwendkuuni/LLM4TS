package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        // Create input stream with AR archive entries
        String archiveData = "!<arch>\n" +
                            "dummyentry1              0000000000  0000000000  100644  0       `\n" +
                            "dummyentry2              0000000000  0000000000  100644  0       `\n";
        InputStream input = new ByteArrayInputStream(archiveData.getBytes());
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        // Read first entry
        ArArchiveEntry entry1 = arInput.getNextArEntry();
        assertEquals("dummyentry1", entry1.getName());
        assertEquals(0, entry1.getSize());

        // Read second entry
        ArArchiveEntry entry2 = arInput.getNextArEntry();
        assertEquals("dummyentry2", entry2.getName());
        assertEquals(0, entry2.getSize());

        // No more entries, should return null
        ArArchiveEntry entry3 = arInput.getNextArEntry();
        assertNull(entry3);

        input.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        // Create input stream with AR archive entries
        String archiveData = "!<arch>\n" +
                            "dummyentry1              0000000000  0000000000  100644  0       `\n" +
                            "dummyentry2              0000000000  0000000000  100644  0       `\n";
        InputStream input = new ByteArrayInputStream(archiveData.getBytes());
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        // Read first entry
        ArchiveEntry entry1 = arInput.getNextEntry();
        assertEquals("dummyentry1", entry1.getName());
        assertEquals(0, entry1.getSize());

        // Read second entry
        ArchiveEntry entry2 = arInput.getNextEntry();
        assertEquals("dummyentry2", entry2.getName());
        assertEquals(0, entry2.getSize());

        // No more entries, should return null
        ArchiveEntry entry3 = arInput.getNextEntry();
        assertNull(entry3);

        input.close();
    }

    @Test
    public void testClose() throws IOException {
        // Create input stream with AR archive entries
        String archiveData = "!<arch>\n" +
                            "dummyentry1              0000000000  0000000000  100644  0       `\n" +
                            "dummyentry2              0000000000  0000000000  100644  0       `\n";
        InputStream input = new ByteArrayInputStream(archiveData.getBytes());
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        // Close the input stream
        arInput.close();

        // Verify that the input stream is closed
        // Try to read an entry, should throw IOException
        try {
            arInput.getNextArEntry();
        } catch (IOException e) {
            // Expected exception
        }

        input.close();
    }

    @Test
    public void testRead_singleByte() throws IOException {
        // Create input stream with AR archive entries
        String archiveData = "!<arch>\n" +
                            "dummyentry1              0000000000  0000000000  100644  0       `\n" +
                            "dummyentry2              0000000000  0000000000  100644  0       `\n";
        InputStream input = new ByteArrayInputStream(archiveData.getBytes());
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        // Read the first byte
        int firstByte = arInput.read();
        assertEquals('!', firstByte);

        // Read the second byte
        int secondByte = arInput.read();
        assertEquals('<', secondByte);

        input.close();
    }

    @Test
    public void testRead_byteArray() throws IOException {
        // Create input stream with AR archive entries
        String archiveData = "!<arch>\n" +
                            "dummyentry1              0000000000  0000000000  100644  0       `\n" +
                            "dummyentry2              0000000000  0000000000  100644  0       `\n";
        InputStream input = new ByteArrayInputStream(archiveData.getBytes());
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        // Create a byte array to read into
        byte[] buffer = new byte[10];

        // Read bytes into the buffer
        int bytesRead = arInput.read(buffer);
        assertEquals(10, bytesRead);
        assertEquals("!<arch>\ndu", new String(buffer));

        input.close();
    }

    @Test
    public void testRead_byteArray_offset() throws IOException {
        // Create input stream with AR archive entries
        String archiveData = "!<arch>\n" +
                            "dummyentry1              0000000000  0000000000  100644  0       `\n" +
                            "dummyentry2              0000000000  0000000000  100644  0       `\n";
        InputStream input = new ByteArrayInputStream(archiveData.getBytes());
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        // Create a byte array to read into
        byte[] buffer = new byte[20];

        // Read bytes into the buffer, starting from offset 5
        int bytesRead = arInput.read(buffer, 5, 10);
        assertEquals(10, bytesRead);
        assertEquals("     !<arch>", new String(buffer));

        input.close();
    }

    @Test
    public void testMatches() {
        // Create a signature that matches
        byte[] signature1 = {'!', '<', 'a', 'r', 'c', 'h', '>', '\n'};
        assertEquals(true, ArArchiveInputStream.matches(signature1, signature1.length));

        // Create a signature that does not match
        byte[] signature2 = {'!', '<', 'x', 'x', 'x', 'x', '>', '\n'};
        assertEquals(false, ArArchiveInputStream.matches(signature2, signature2.length));
    }

}