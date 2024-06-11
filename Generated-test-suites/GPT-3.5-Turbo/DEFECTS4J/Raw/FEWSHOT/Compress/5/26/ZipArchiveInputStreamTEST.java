
package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testGetNextZipEntry() throws IOException {
        byte[] data = generateDummyZipFileData();
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(data));
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();

        assertNotNull(entry);
        assertEquals("dummy.txt", entry.getName());
        assertEquals(10, entry.getSize());
        assertEquals(0, entry.getMethod());

        zipInputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = generateDummyZipFileData();
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(data));
        zipInputStream.getNextZipEntry();

        byte[] buffer = new byte[10];
        int bytesRead = zipInputStream.read(buffer, 0, buffer.length);

        assertEquals(10, bytesRead);
        assertEquals("HelloWorld", new String(buffer));

        zipInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = generateDummyZipFileData();
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(data));
        zipInputStream.close();

        // Verify that closing the stream works without throwing an exception
    }

    private byte[] generateDummyZipFileData() throws IOException {
        byte[] data = new byte[118];
        data[0] = 0x50;
        data[1] = 0x4B;
        data[2] = 0x03;
        data[3] = 0x04;
        data[30] = 0x09; // file size (LSB)
        data[31] = 0x00; // file size (MSB)
        data[32] = 0x00; // method (no compression)
        data[33] = 0x00; // method (no compression)
        data[34] = 'd';
        data[35] = 'u';
        data[36] = 'm';
        data[37] = 'm';
        data[38] = 'y';
        data[39] = '.';
        data[40] = 't';
        data[41] = 'x';
        data[42] = 't';
        data[43] = 0x68; // CRC32 (LSB)
        data[44] = 0xFA; // CRC32
        data[45] = 0x4C; // CRC32
        data[46] = 0x48; // CRC32 (MSB)
        data[47] = 0x0A; // file size (LSB)
        data[48] = 0x00; // file size (MSB)
        data[77] = 'H';
        data[78] = 'e';
        data[79] = 'l';
        data[80] = 'l';
        data[81] = 'o';
        data[82] = 'W';
        data[83] = 'o';
        data[84] = 'r';
        data[85] = 'l';
        data[86] = 'd';

        return data;
    }
}
