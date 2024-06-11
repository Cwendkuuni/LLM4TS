package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioOutputStream;
import org.apache.commons.compress.utils.CpioUtil;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testCtorWithFormat() {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);
        assertEquals(CpioConstants.FORMAT_NEW, outputStream.getFormat());
    }

    @Test
    public void testCtorWithoutFormat() {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out);
        assertEquals(CpioConstants.FORMAT_NEW, outputStream.getFormat());
    }

    @Test
    public void testEnsureOpen() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out);
        outputStream.ensureOpen();
    }

    @Test(expected = IOException.class)
    public void testEnsureOpenClosedStream() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out);
        outputStream.finish();
        outputStream.ensureOpen();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out);

        final CpioArchiveEntry entry = new CpioArchiveEntry();
        outputStream.putNextEntry(entry);

        assertNull(outputStream.getCpioEntry());
        assertEquals(entry, outputStream.getCpioEntry());
        assertFalse(outputStream.isFinished());
        assertFalse(outputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryClosedStream() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out);
        outputStream.finish();
        outputStream.putNextEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryDuplicateEntry() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out);

        final CpioArchiveEntry entry1 = new CpioArchiveEntry();
        final CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry1.setName("test.txt");
        outputStream.putNextEntry(entry1);
        outputStream.putNextEntry(entry2);
    }

    @Test
    public void testPutNextEntryWithNameAndFormat() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out);

        final CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        outputStream.putNextEntry(entry);

        assertSame(entry, outputStream.getCpioEntry());
        assertEquals(CpioConstants.FORMAT_OLD_ASCII,
                outputStream.getCpioEntry().getFormat());
    }

    @Test
    public void testWrite() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out);

        final byte[] data = "Test data".getBytes();
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write(data);
        outputStream.closeArchiveEntry();

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(
                out.toByteArray());
        final byte[] expected = new byte[data.length];
        inputStream.read(expected);
        assertArrayEquals(data, expected);
    }

    @Test
    public void testWriteWithSwappedHalfWords() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_OLD_BINARY);

        final byte[] data = "Test data".getBytes();
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write(data);
        outputStream.closeArchiveEntry();

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(
                out.toByteArray());
        final byte[] expected = new byte[data.length];
        CpioUtil.byteArraySwapHalfWords(expected);
        final int bytesRead = inputStream.read(expected);
        assertEquals(data.length, bytesRead);
        assertArrayEquals(data, expected);
    }

    @Test
    public void testFinish() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out);

        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write("Test data".getBytes());
        outputStream.finish();

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(
                out.toByteArray());
        final byte[] expected = new byte[1 + "Test data".length() + 1];
        inputStream.read(expected);

        assertEquals(expected[0], MAGIC_OLD_BINARY[0]);
        assertEquals(expected[1], MAGIC_OLD_BINARY[1]);
        assertEquals(expected[2], (byte) 0);
        assertEquals(expected[3], (byte) 0);
        assertEquals(expected[4], (byte) 0);
        assertEquals(expected[5], (byte) 0);
        assertEquals(expected[6], (byte) 0);
        assertEquals(expected[7], (byte) 0);

        final int len = "Test data".length();
        for (int i = 0; i < len; ++i) {
            assertEquals(expected[8 + i], "Test data".charAt(i));
        }

        assertEquals(expected[len + 8], 0);
        assertEquals(expected[len + 9], 0);
        assertEquals(expected[len + 10], 0);
        assertEquals(expected[len + 11], 0);
    }

    @Test
    public void testFinishWithSwappedHalfWords() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_OLD_BINARY);

        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write("Test data".getBytes());
        outputStream.finish();

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(
                out.toByteArray());
        final byte[] expected = new byte[1 + "Test data".length() + 1];
        CpioUtil.byteArraySwapHalfWords(expected);

        final int bytesRead = inputStream.read(expected);
        assertEquals(expected.length, bytesRead);

        assertEquals(expected[0], MAGIC_OLD_BINARY[0]);
        assertEquals(expected[1], MAGIC_OLD_BINARY[1]);
        assertEquals(expected[2], (byte) 0);
        assertEquals(expected[3], (byte) 0);
        assertEquals(expected[4], (byte) 0);
        assertEquals(expected[5], (byte) 0);
        assertEquals(expected[6], (byte) 0);
        assertEquals(expected[7], (byte) 0);

        final int len = "Test data".length();
        for (int i = 0; i < len; ++i) {
            assertEquals(expected[8 + i], "Test data".charAt(i));
        }

        assertEquals(expected[len + 8], 0);
        assertEquals(expected[len + 9], 0);
        assertEquals(expected[len + 10], 0);
        assertEquals(expected[len + 11], 0);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out);

        final byte[] data = "Test data".getBytes();
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write(data);
        outputStream.closeArchiveEntry();

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(
                out.toByteArray());
        final byte[] expected = new byte[data.length];
        inputStream.read(expected);
        assertArrayEquals(data, expected);
    }

    @Test
    public void testCloseArchiveEntryWithSwappedHalfWords() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_OLD_BINARY);

        final byte[] data = "Test data".getBytes();
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write(data);
        outputStream.closeArchiveEntry();

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(
                out.toByteArray());
        final byte[] expected = new byte[data.length];
        CpioUtil.byteArraySwapHalfWords(expected);

        final int bytesRead = inputStream.read(expected);
        assertEquals(data.length, bytesRead);

        assertArrayEquals(data, expected);
    }

    @Test
    public void testGetNames(){
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                out);
        final Map<String, CpioArchiveEntry> names = new HashMap<>();

        final CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test1.txt");
        outputStream.putNextEntry(entry1);

        final CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("test2.txt");
        outputStream.putNextEntry(entry2);

        outputStream.finish();

        assertEquals(2, outputStream.getNames().size());
        assertEquals("test1.txt", outputStream.getNames().get("test1.txt"));
        assertEquals("test2.txt", outputStream.getNames().get("test2.txt"));
    }
}