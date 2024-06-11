package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ArArchiveInputStream}.
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() throws Exception {
        String testData = "test data\n" +
                "0102030405060708090a0b0c0d0e0f\n" +
                "02030405060708090a0b0c0d0e0f10111213\n" +
                "030405060708090a0b0c0d0e0f10111213141516\n";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(ArArchiveInputStreamTest.createArHeader()));
        IOUtils.copy(gzip, outputStream);
        outputStream.write(testData.getBytes());
        outputStream.close();

        byte[] arData = outputStream.toByteArray();
        this.inputStream = new ByteArrayInputStream(arData);
        this.arInputStream = new ArArchiveInputStream(this.inputStream);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(this.arInputStream);
        IOUtils.closeQuietly(this.inputStream);
    }

    @Test
    public void testMatches() {
        byte[] arHeader = ArArchiveInputStreamTest.createArHeader();
        assertTrue(ArArchiveInputStream.matches(arHeader, 0));
        assertTrue(ArArchiveInputStream.matches(arHeader, arHeader.length));
        assertFalse(ArArchiveInputStream.matches(new byte[16], 16));
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveEntry entry1 = this.arInputStream.getNextArEntry();
        assertEquals("test data", entry1.getName());
        assertEquals(10L, entry1.getSize());

        ArArchiveEntry entry2 = this.arInputStream.getNextArEntry();
        assertEquals("02030405060708090a0b0c0d0e0f10111213", entry2.getName());
        assertEquals(22L, entry2.getSize());

        ArArchiveEntry entry3 = this.arInputStream.getNextArEntry();
        assertEquals("030405060708090a0b0c0d0e0f10111213141516", entry3.getName());
        assertEquals(32L, entry3.getSize());

        ArArchiveEntry entry4 = this.arInputStream.getNextArEntry();
        assertNull(entry4);
    }

    @Test
    public void testGetNextEntryGzipped() throws IOException {
        String testDataGzipped = "H4sIAAAAAAAAAF0cEhR8V1U1X19rAAAA";
        ByteArrayOutputStream gzippedOutput = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(gzippedOutput);
        gzip.write(ArArchiveInputStreamTest.createArHeader());
        gzip.write(testData.getBytes());
        gzip.finish();
        gzip.close();

        byte[] gzippedData = gzippedOutput.toByteArray();
        InputStream gzippedInputStream = new ByteArrayInputStream(gzippedData);
        ArArchiveInputStream gzippedArInputStream = new ArArchiveInputStream(gzippedInputStream);

        ArArchiveEntry entry1 = gzippedArInputStream.getNextArEntry();
        assertEquals("test data", entry1.getName());
        assertEquals(10L, entry1.getSize());

        ArArchiveEntry entry2 = gzippedArInputStream.getNextArEntry();
        assertEquals("02030405060708090a0b0c0d0e0f10111213", entry2.getName());
        assertEquals(22L, entry2.getSize());

        ArArchiveEntry entry3 = gzippedArInputStream.getNextArEntry();
        assertEquals("030405060708090a0b0c0d0e0f10111213141516", entry3.getName());
        assertEquals(32L, entry3.getSize());

        ArArchiveEntry entry4 = gzippedArInputStream.getNextArEntry();
        assertNull(entry4);

        IOUtils.closeQuietly(gzippedArInputStream);
        IOUtils.closeQuietly(gzippedInputStream);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[16];

        int bytesRead = this.arInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"t", "e", "s", "t", " ", "d", "a", "t", "a", 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x09}, buffer, 0, bytesRead);

        bytesRead = this.arInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "1", "0", "2", "0", "3", "0", "4", "0", "5", "0", "6", "0", "7", "0", "8", "0"}, buffer, 0, bytesRead);

        bytesRead = this.arInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "2", "0", "3", "0", "4", "0", "5", "0", "6", "0", "7", "0", "8", "0", "9", "0"}, buffer, 0, bytesRead);

        bytesRead = this.arInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"a", "b", 0x00, 0x0b, 0x0c, 0x0d, 0x0e, "0", "f", 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16}, buffer, 0, bytesRead);

        bytesRead = this.arInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testReadGzipped() throws IOException {
        byte[] buffer = new byte[16];

        int bytesRead = this.arInputStreamGzipped.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"t", "e", "s", "t", " ", "d", "a", "t", "a", 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x09}, buffer, 0, bytesRead);

        bytesRead = this.arInputStreamGzipped.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "1", "0", "2", "0", "3", "0", "4", "0", "5", "0", "6", "0", "7", "0", "8", "0"}, buffer, 0, bytesRead);

        bytesRead = this.arInputStreamGzipped.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "2", "0", "3", "0", "4", "0", "5", "0", "6", "0", "7", "0", "8", "0", "9", "0"}, buffer, 0, bytesRead);

        bytesRead = this.arInputStreamGzipped.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"a", "b", 0x00, 0x0b, 0x0c, 0x0d, 0x0e, "0", "f", 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16}, buffer, 0, bytesRead);

        bytesRead = this.arInputStreamGzipped.read(buffer, 0, buffer.length);
        assertEquals(-1, bytesRead);
    }

    private static byte[] createArHeader() {
        byte[] header = new byte[16 + 16 + 6 + 6 + 8 + 10];
        System.arraycopy(ArArchiveInputStreamTest.AR_MAGIC_NUMBER, 0, header, 0, AR_MAGIC_NUMBER.length);
        System.arraycopy(new byte[]{0x02, 0x00}, 0, header, 16, 2);
        System.arraycopy(new byte[]{0x00, 0x14}, 18, header, 18, 2);
        System.arraycopy(new byte[]{0x00, 0x08}, 20, header, 20, 2);
        System.arraycopy(new byte[]{(byte) 0x43, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x74, (byte) 0x75}, 22, header, 22, 6);
        System.arraycopy(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, 28, header, 28, 16);
        System.arraycopy(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, 44, header, 44, 8);
        System.arraycopy(new byte[]{0x00, 0x0a}, 52, header, 52, 2);
        return header;
    }

    private static final byte[] AR_MAGIC_NUMBER = new byte[]{0x3c, (byte) 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e};

    private ArArchiveInputStream arInputStreamGzipped;

    @Before
    public void setUpGzipped() throws IOException {
        this.arInputStreamGzipped = new ArArchiveInputStream(new GZIPInputStream(this.inputStream));
    }
}