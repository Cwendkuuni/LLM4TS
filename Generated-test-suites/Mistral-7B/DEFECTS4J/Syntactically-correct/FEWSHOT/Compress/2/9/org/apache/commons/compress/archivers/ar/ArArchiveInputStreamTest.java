package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.test.TestInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {
    @Test
    public void testMatches() {
        byte[] signature = new byte[16];
        signature[0] = (byte) 0x21;
        signature[1] = (byte) 0x3c;
        signature[2] = (byte) 0x61;
        signature[3] = (byte) 0x72;
        signature[4] = (byte) 0x63;
        signature[5] = (byte) 0x68;
        signature[6] = (byte) 0x3e;
        signature[7] = (byte) 0x0a;

        byte[] buffer = new byte[1024];
        InputStream input = new ByteArrayInputStream(new byte[0]);

        assertTrue(ArArchiveInputStream.matches(signature, 8));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(signature);
        output.write(ArArchiveEntry.HEADER);
        output.write(ArArchiveEntry.TRAILER);

        InputStream testInput = new TestInputStream(output.toByteArray());
        ArArchiveInputStream arStream = new ArArchiveInputStream(testInput);

        ArArchiveEntry entry = arStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("test", entry.getName());
        assertEquals(123L, entry.getSize());

        entry = arStream.getNextArEntry();
        assertNull(entry);

        arStream.close();
    }

    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        assertEquals(-1, arStream.read());

        byte[] data = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x61, 0x72, 0x63, 0x68, 0x65, 0x78, 0x61, 0x6d, 0x70, 0x6c, 0x65, 0x2E, 0x61, 0x70, 0x70, 0x65, 0x3e, 0x0a};
        input = new ByteArrayInputStream(data);
        arStream = new ArArchiveInputStream(input);

        assertEquals(0x21, arStream.read());
        assertEquals(0x3c, arStream.read());
        // ... read all bytes from header and trailer
        assertEquals(-1, arStream.read());

        arStream.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        assertNull(arStream.getNextEntry());

        byte[] data = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x61, 0x72, 0x63, 0x68, 0x65, 0x78, 0x61, 0x6d, 0x70, 0x6c, 0x65, 0x2E, 0x61, 0x70, 0x70, 0x65, 0x3e, 0x0a, 0x61, 0x72, 0x63, 0x68, 0x65, 0x78, 0x61, 0x6d, 0x70, 0x6c, 0x65, 0x2E, 0x61, 0x70, 0x70, 0x65, 0x3e, 0x0a};
        input = new ByteArrayInputStream(data);
        arStream = new ArArchiveInputStream(input);

        ArArchiveEntry entry = arStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        assertEquals(17L, entry.getSize());

        entry = arStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("test2.bin", entry.getName());
        assertEquals(23L, entry.getSize());

        entry = arStream.getNextEntry();
        assertNull(entry);

        arStream.close();
    }
}