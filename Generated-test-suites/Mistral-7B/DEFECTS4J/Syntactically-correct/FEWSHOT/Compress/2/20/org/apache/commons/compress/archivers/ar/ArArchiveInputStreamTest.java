package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] header = ArArchiveEntry.HEADER.getBytes();
        byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
        byte[] name = "test.txt".getBytes();
        byte[] length = Long.toString(10).getBytes();

        InputStream testStream = new ByteArrayInputStream(new byte[] {
                // header
                header[0], header[1], header[2], header[3], header[4], header[5], header[6], header[7],
                name[0], name[1], name[2], name[3], name[4], name[5], name[6], name[7], name[8], name[9], name[10], name[11], name[12], name[13], name[14],
                length[0], length[1], length[2], length[3], length[4], length[5], length[6], length[7], length[8], length[9],
                trailer[0], trailer[1], trailer[2], trailer[3], trailer[4], trailer[5], trailer[6], trailer[7], trailer[8], trailer[9], trailer[10], trailer[11], trailer[12], trailer[13], trailer[14]
        });

        ArArchiveInputStream testStreamWrapper = new ArArchiveInputStream(testStream);

        ArArchiveEntry entry = testStreamWrapper.getNextArEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        assertEquals(10, entry.getSize());

        entry = testStreamWrapper.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        this.testGetNextArEntry();
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[16];
        signature[0] = 0x3c;
        signature[1] = 0x21;
        signature[2] = 0x61;
        signature[3] = 0x72;
        signature[4] = 0x63;
        signature[5] = 0x68;
        signature[6] = 0x3e;
        signature[7] = 0x0a;

        assert(ArArchiveInputStream.matches(signature, 8));
        assertFalse(ArArchiveInputStream.matches(new byte[]{1, 2, 3}, 3));
    }
}