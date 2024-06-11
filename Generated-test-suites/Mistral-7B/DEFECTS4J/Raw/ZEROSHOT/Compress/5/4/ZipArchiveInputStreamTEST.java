package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test for {@link ZipArchiveInputStream}.
 */
public class ZipArchiveInputStreamTest {

    private static final byte[] TEST_FILE_HEADER = new byte[] {
        0x04, 0x03, 0x4b, 0x50, // signature
        0x12, 0x00, 0x00, 0x00, // version made by
        0x02, 0x08, 0x00, 0x00, // version needed to extract
        0x04, 0x00, 0x00, 0x00, // general purpose bit flag
        0x08, 0x00,               // compression method
        0x3b, 0x2f, 0x10, 0x00, // last mod file time
        0x9d, 0x6e, 0x14, 0x00, // last mod file date
        0x5a, 0x4e, 0x2f, 0x53, // crc-32
        0x00, 0x00, 0x00, 0x00, // compressed size
        0x00, 0x00, 0x00, 0x02, // uncompressed size
        12,                      // file name length
        0x00, 0x00,               // extra field length
        0x54, 0x68, 't', 'e', 's', 't',  // file name
        0x00, 0x00, 0x00, 0x00    // comment length (0)
    };

    private static final byte[] TEST_ENTRY_HEADER = new byte[] {
        0x04, 0x03, 0x4b, 0x50, // signature
        0x14, 0x00, 0x00, 0x00, // version made by
        0x02, 0x08, 0x00, 0x00, // version needed to extract
        0x08, 0x00,               // compression method
        0x3b, 0x2f, 0x10, 0x00, // last mod file time
        0x9d, 0x6e, 0x14, 0x00, // last mod file date
        0x5a, 0x4e, 0x2f, 0x53, // crc-32
        0x00, 0x00, 0x00, 0x02, // compressed size
        0x00, 0x00, 0x00, 0x05, // uncompressed size
        12,                      // file name length
        0x00, 0x00,               // extra field length
        0x54, 0x68, 't', 'e', 's', 't',  // file name
        0x00, 0x00, 0x00, 0x02, // comment length
        0x41, 0x74, 0x68, 0x69, 0x73, 0x73, 0x20, // comment
        0x63, 0x61, 0x6d, 0x65, 0x6d,  // "append.txt"
        0x73, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x20, // "store.zip"
        0x00, 0x00, 0x00, 0x00    // comment length (0)
    };

    /**
     * Test for {@link ZipArchiveInputStream#getNextZipEntry()}.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testGetNextZipEntry() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_FILE_HEADER);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
            inputStream);

        // first entry
        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertSame(ZipArchiveEntry.class, entry.getClass());
        assertEquals("test", entry.getName());
        assertFalse(entry.hasComment());
        assertNull(entry.getExtra());

        // second entry
        entry = zipArchiveInputStream.getNextZipEntry();
        assertEquals("append.txt", entry.getName());
        assertTrue(entry.hasComment());
        assertEquals("store.zip\nappend.txt", entry.getComment());
        assertArrayEquals(TEST_ENTRY_HEADER, zipArchiveInputStream.getBuffer(), 0,
            TEST_ENTRY_HEADER.length);

        assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    /**
     * Test for {@link ZipArchiveInputStream#read(byte[], int, int)}.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_ENTRY_HEADER);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
            inputStream, null, false);

        byte[] buffer = new byte[10];
        int length = zipArchiveInputStream.read(buffer);
        assertEquals(TEST_ENTRY_HEADER[0], buffer[0]);
        assertEquals(5, length);

        length = zipArchiveInputStream.read(buffer, 5, 5);
        assertEquals(TEST_ENTRY_HEADER[5], buffer[5]);
        assertEquals(5, length);

        length = zipArchiveInputStream.read(buffer, 10, 5);
        assertEquals(-1, length);

        zipArchiveInputStream.close();
    }

    /**
     * Test for {@link ZipArchiveInputStream#close()}.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_FILE_HEADER);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
            inputStream);
        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.close();
        entry = zipArchiveInputStream.getNextZipEntry();
        assertNull(entry);
    }

    /**
     * Test for {@link ZipArchiveInputStream#skip(long)}.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testSkip() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_FILE_HEADER);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
            inputStream);

        long length = zipArchiveInputStream.skip(10L);
        assertEquals(10, length);

        length = zipArchiveInputStream.skip(-10L);
        assertEquals(0, length);

        length = zipArchiveInputStream.skip(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, length);

        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertTrue(entry != null);
        zipArchiveInputStream.skip(entry.getSize());
        assertNull(zipArchiveInputStream.getNextZipEntry());

        zipArchiveInputStream.close();
    }

    /**
     * Test for {@link ZipArchiveInputStream#matches(byte[], int)}.
     */
    @org.junit.Test
    public void testMatches() {
        assertTrue(ZipArchiveInputStream.matches(TEST_FILE_HEADER,
            TEST_FILE_HEADER.length));
    }

    /**
     * Test for {@link ZipArchiveInputStream#getBuffer()}.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testGetBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_FILE_HEADER);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
            inputStream);

        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        byte[] buffer = zipArchiveInputStream.getBuffer();
        assertEquals(TEST_ENTRY_HEADER.length, buffer.length);
        assertArrayEquals(TEST_ENTRY_HEADER, buffer, 0, TEST_ENTRY_HEADER.length);

        zipArchiveInputStream.close();
    }

    /**
     * Test for {@link ZipArchiveInputStream#count(int)}.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testCount() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_FILE_HEADER);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
            inputStream);

        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        int bytesRead = zipArchiveInputStream.count(entry.getSize());
        assertEquals(entry.getSize(), bytesRead);

        zipArchiveInputStream.close();
    }

    /**
     * Test for {@link ZipArchiveInputStream#closeEntry()}.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testCloseEntry() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_FILE_HEADER);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
            inputStream);

        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.closeEntry();
        assertNull(zipArchiveInputStream.getNextZipEntry());

        zipArchiveInputStream.close();
    }

    /**
     * Test for {@link ZipArchiveInputStream#fill()}.
     *
     * @throws IOException if an error occurs
     */
    @org.junit.Test
    public void testFill() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_FILE_HEADER);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
            inputStream);

        zipArchiveInputStream.fill();
        assertTrue(zipArchiveInputStream.inf.needsInput());

        zipArchiveInputStream.close();
    }
}