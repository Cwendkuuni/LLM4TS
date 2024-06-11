package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Test class for {@link DumpArchiveInputStream}.
 */
public class DumpArchiveInputStreamTest {

    private static final ZipEncoding ENCODING = ZipEncodingHelper.getZipEncoding(null);
    private static final byte[] DUMMY_DATA = "DumpArchiveInputStreamTest".getBytes(StandardCharsets.UTF_8);
    private static final int DUMMY_ENTRY_SIZE = DUMMY_DATA.length;

    private InputStream inputStream;
    private DumpArchiveInputStream archiveInputStream;

    @Rule
    public void testSetup() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        zipOutputStream.setEncoding(ENCODING.getName());
        zipOutputStream.putNextEntry(new ZipEntry("test.txt"));
        zipOutputStream.write(DUMMY_DATA, 0, DUMMY_ENTRY_SIZE);
        zipOutputStream.closeEntry();
        zipOutputStream.finish();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        archiveInputStream = new DumpArchiveInputStream(inputStream, ENCODING.getName());
    }

    @Test
    public void testMatchesMagicValue() throws Exception {
        byte[] headerBytes = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(headerBytes, (byte) 0x4e);
        assertTrue(DumpArchiveInputStream.matches(headerBytes, headerBytes.length));
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidMagicValue() throws Exception {
        byte[] headerBytes = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(headerBytes, (byte) 0x4f);
        new DumpArchiveInputStream(new ByteArrayInputStream(headerBytes), ENCODING.getName());
    }

    @Test
    public void testGetNextEntry() throws Exception {
        DumpArchiveEntry entry = archiveInputStream.getNextEntry();
        assertEquals("test.txt", entry.getName());
        assertEquals("test.txt", entry.getSimpleName());
        assertEquals(0, entry.getOffset());
        assertNull(entry.getNextEntry());
    }

    @Test
    public void testRead() throws Exception {
        byte[] buffer = new byte[DUMMY_ENTRY_SIZE];
        int len = archiveInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(DUMMY_DATA, buffer);
        len = archiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, len);
    }

    @Test
    public void testClose() throws Exception {
        archiveInputStream.close();
        assertFalse(archiveInputStream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testClosedStream() throws Exception {
        archiveInputStream.close();
        byte[] buffer = new byte[1];
        archiveInputStream.read(buffer, 0, buffer.length);
    }

    @Test
    public void testPeekAtEndOfStream() throws Exception {
        archiveInputStream.close();
        inputStream = new ByteArrayInputStream(inputStream.readAllBytes());
        archiveInputStream = new DumpArchiveInputStream(inputStream, ENCODING.getName());
        byte[] buffer = new byte[1];
        assertEquals(-1, archiveInputStream.read(buffer, 0, buffer.length));
    }

    @Test
    public void testReadPastEndOfStream() throws Exception {
        byte[] buffer = new byte[DUMMY_ENTRY_SIZE * 2];
        int len = archiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, len);
    }

    @Test
    public void testReadAllData() throws Exception {
        byte[] buffer = new byte[DUMMY_ENTRY_SIZE * 2];
        int len;

        len = archiveInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(DUMMY_DATA, Arrays.copyOfRange(buffer, 0, DUMMY_ENTRY_SIZE));

        len = archiveInputStream.read(buffer, DUMMY_ENTRY_SIZE, buffer.length - DUMMY_ENTRY_SIZE);
        assertEquals(-1, len);
    }

    @Test
    public void testReadPartialData() throws Exception {
        byte[] buffer = new byte[3];
        int len;

        len = archiveInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, 0, 0}, buffer);

        len = archiveInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, 0, 72}, buffer);

        len = archiveInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"T", "e", (byte) 0x64}, buffer);

        len = archiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, len);
    }

    @Test
    public void testReadDataWithOffset() throws Exception {
        byte[] buffer = new byte[3];
        int len;

        archiveInputStream.skip(2);

        len = archiveInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"e", "s", (byte) 0x65}, buffer);

        len = archiveInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, 0, 76}, buffer);

        len = archiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, len);
    }

    @Test
    public void testReadDataWithLength() throws Exception {
        byte[] buffer = new byte[3];
        int len;

        len = archiveInputStream.read(buffer, 1, 2);
        assertArrayEquals(new byte[]{"s", 0}, buffer);

        len = archiveInputStream.read(buffer, 1, 2);
        assertArrayEquals(new byte[]{"p", "."}, buffer);

        len = archiveInputStream.read(buffer, 1, 2);
        assertEquals(-1, len);
    }

    @Test
    public void testReadPastEndOfData() throws Exception {
        byte[] buffer = new byte[5];
        int len;

        len = archiveInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, 0, 72, 0, 0}, buffer);

        len = archiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, len);

        len = archiveInputStream.read(buffer, 1, buffer.length - 1);
        assertArrayEquals(new byte[]{0, 0, 72, 0}, buffer);
    }

    @Test
    public void testReadAtBeginning() throws Exception {
        archiveInputStream.mark(DumpArchiveConstants.TP_SIZE);
        byte[] buffer = new byte[DUMMY_ENTRY_SIZE];
        int len = archiveInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(DUMMY_DATA, buffer);
        archiveInputStream.reset();
    }

    @Test
    public void testReadAtEnd() throws Exception {
        byte[] buffer = new byte[DUMMY_ENTRY_SIZE];
        int len;

        archiveInputStream.skip(archiveInputStream.available());
        len = archiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, len);
    }

    @Test(expected = EOFException.class)
    public void testReadAtEOF() throws Exception {
        byte[] buffer = new byte[1];
        archiveInputStream.read(buffer, 0, buffer.length);
        archiveInputStream.read(buffer, 0, buffer.length);
    }

    @Test(expected = IOException.class)
    public void testReadWithNullBuffer() throws Exception {
        archiveInputStream.read(null, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithNegativeLength() throws Exception {
        archiveInputStream.read(new byte[0], 1, -1);
    }

    @Test(expected = IOException.class)
    public void testReadWithNegativeOffset() throws Exception {
        archiveInputStream.read(new byte[0], 1, -1);
    }

    @Test
    public void testReadAllBytes() throws Exception {
        byte[] allData = IOUtils.toByteArray(inputStream);
        byte[] buffer = new byte[allData.length];
        int len = archiveInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(allData, buffer);
    }
}