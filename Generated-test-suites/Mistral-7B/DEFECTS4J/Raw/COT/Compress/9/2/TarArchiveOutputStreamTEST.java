package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream out;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        out = new TarArchiveOutputStream(new CountingOutputStream(outputStream, 1024));
    }

    @After
    public void tearDown() throws IOException {
        out.finish();
        out.close();
    }

    @Test
    public void testEmptyConstructor() {
        out = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream(), 1024));
        assertNull(out.getArchiveEntry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOutputStream() {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        out.putArchiveEntry(createTestEntry("test.txt"));
        byte[] expectedHeader = getExpectedHeader("test.txt");
        assertEquals(expectedHeader.length, out.getBuffer().getPosition());
        ArchiveEntry actualEntry = out.getArchiveEntry();
        TarArchiveEntry tarEntry = (TarArchiveEntry) actualEntry;
        assertEquals("test.txt", tarEntry.getName());
        assertEquals(10, tarEntry.getSize());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "b";
        out.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        out.putArchiveEntry(createTestEntry(longFileName));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeError() throws IOException {
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "b";
        out.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        out.putArchiveEntry(createTestEntry(longFileName));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        out.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNonTarEntry() throws IOException {
        out.putArchiveEntry(new byte[0]);
    }

    @Test(expected = IOException.class)
    public void testFinishWithoutCloseArchiveEntry() throws IOException {
        out.putArchiveEntry(createTestEntry("test.txt"));
        out.finish();
    }

    @Test(expected = IOException.class)
    public void testCloseWithoutFinish() throws IOException {
        out.close();
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        out.putArchiveEntry(createTestEntry("test.txt"));
        out.finish();
        out.close();
        out.finish();
        out.close();
    }

    @Test
    public void testWrite() throws IOException {
        out.putArchiveEntry(createTestEntry("test.txt"));
        byte[] testData = "Hello, world!".getBytes();
        out.write(testData, 0, testData.length);
        byte[] expectedData = new byte[testData.length + 11]; // 11 bytes header
        System.arraycopy(getExpectedHeader("test.txt"), 0, expectedData, 0, 11);
        System.arraycopy(testData, 0, expectedData, 11, testData.length);
        byte[] actualData = outputStream.toByteArray();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testWriteWithPartialData() throws IOException {
        out.putArchiveEntry(createTestEntry("test.txt"));
        byte[] testData = "Hello, world!".getBytes();
        out.write(testData, 0, testData.length / 2);
        out.closeArchiveEntry();
        byte[] expectedData = new byte[testData.length /
                (testData.length / 2 + 1) + 11]; // 11 bytes header and 2 records
        System.arraycopy(getExpectedHeader("test.txt"), 0, expectedData, 0, 11);
        System.arraycopy(testData, 0, expectedData, 11, testData.length / 2);
        byte[] actualData = outputStream.toByteArray();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testFlush() throws IOException {
        out.putArchiveEntry(createTestEntry("test.txt"));
        byte[] testData = "Hello, world!".getBytes();
        out.write(testData, 0, testData.length);
        out.flush();
        byte[] actualData = outputStream.toByteArray();
        byte[] expectedData = new byte[testData.length + 11]; // 11 bytes header
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testCreateArchiveEntryWithFile() throws IOException {
        out.putArchiveEntry(out.createArchiveEntry(new File("test.txt")));
        byte[] expectedHeader = getExpectedHeader("test.txt");
        assertEquals(expectedHeader.length, out.getBuffer().getPosition());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateArchiveEntryWithNonExistentFile() throws IOException {
        out.putArchiveEntry(out.createArchiveEntry(new File("non_existent.txt")));
    }

    private TarArchiveEntry createTestEntry(String name) {
        return new TarArchiveEntry(new File(name), name);
    }

    private byte[] getExpectedHeader(String name) {
        return TarConstants.HEADER_MAGIC.concat(
                Long.toHexString(0L).substring(1) + TarConstants.HEADER_VERSION +
                        Long.toHexString(name.length() + 1) +
                        TarConstants.HEADER_FILE_TYPE +
                        Long.toHexString(10) +
                        Long.toHexString(name.length() + 1) +
                        name)
                .getBytes(StandardCharsets.US_ASCII);
    }
}