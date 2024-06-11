package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;

/**
 * Test cases for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    /**
     * Test that a {@code CpioArchiveOutputStream} can be constructed with a
     * specified format.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testCtorWithFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_OLD_ASCII);
        assertEquals(CpioConstants.FORMAT_OLD_ASCII, outputStream.getFormat());
    }

    /**
     * Test that a {@code CpioArchiveOutputStream} can be constructed without a
     * specified format.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testCtorWithoutFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        assertEquals(CpioConstants.FORMAT_NEW, outputStream.getFormat());
    }

    /**
     * Test that {@code CpioArchiveOutputStream#ensureOpen()} throws an
     * {@code IOException} if the stream is closed.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test(expected = IOException.class)
    public void testEnsureOpenWhenClosed() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        outputStream.close();
        outputStream.ensureOpen();
    }

    /**
     * Test that {@code CpioArchiveOutputStream#ensureOpen()} does not throw an
     * {@code IOException} if the stream is open.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testEnsureOpenWhenOpen() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        outputStream.ensureOpen();
    }

    /**
     * Test that {@code CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)}
     * writes a new entry and positions the stream to the start of the entry
     * data.
     *
     * @throws IOException if an I/O error has occurred or if a CPIO file error
     *                    has occurred
     */
    @Test
    public void testPutNextEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(12345);
        outputStream.putNextEntry(entry);
        byte[] expectedMagic = CpioConstants.MAGIC_NEW;
        byte[] expectedHeader = entry.getHeaderBytes();
        byte[] expectedData = "12345".getBytes();
        byte[] expectedTrailer = CpioConstants.TRAILER_MAGIC;
        byte[] output = outputStream.toByteArray();
        assertArrayEquals(expectedMagic, output, 0, 8);
        assertArrayEquals(expectedHeader, output, 8, expectedHeader.length);
        assertArrayEquals(expectedData, output, expectedHeader.length + 8,
                expectedData.length);
        assertArrayEquals(expectedTrailer, output,
                expectedHeader.length + expectedData.length + 8, 8);
    }

    /**
     * Test that {@code CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)}
     * throws an {@code IOException} if an entry with the same name already
     * exists.
     *
     * @throws IOException if an I/O error has occurred or if a CPIO file error
     *                    has occurred
     */
    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");
        outputStream.putNextEntry(entry1);
        outputStream.putNextEntry(entry2);
    }

    /**
     * Test that {@code CpioArchiveOutputStream#write(byte[], int, int)} writes
     * data to the current entry.
     *
     * @throws IOException if an I/O error has occurred or if a CPIO file error
     *                    has occurred
     */
    @Test
    public void testWrite() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(12345);
        outputStream.putNextEntry(entry);
        byte[] data = "12345".getBytes();
        outputStream.write(data, 0, data.length);
        byte[] expected = entry.getHeaderBytes();
        byte[] actual = outputStream.toByteArray();
        assertArrayEquals(expected, actual, 0, expected.length + data.length);
    }

    /**
     * Test that {@code CpioArchiveOutputStream#finish()} writes the trailer
     * and closes the stream.
     *
     * @throws IOException if an I/O error has occurred or if a CPIO file error
     *                    has occurred
     */
    @Test
    public void testFinish() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(12345);
        outputStream.putNextEntry(entry);
        outputStream.finish();
        byte[] expected = CpioConstants.TRAILER_MAGIC;
        byte[] actual = outputStream.toByteArray();
        assertArrayEquals(expected, actual, 0, 8);
    }

    /**
     * Test that {@code CpioArchiveOutputStream#close()} closes the underlying
     * output stream.
     *
     * @throws IOException if an I/O error has occurred or if a CPIO file error
     *                    has occurred
     */
    @Test
    public void testClose() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        outputStream.close();
        assertFalse(outputStream.isOpen());
    }

    /**
     * Test that {@code CpioArchiveOutputStream#getName()} returns the name of
     * the underlying output stream.
     */
    @Test
    public void testGetName() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        assertEquals(out.toString(), outputStream.getName());
    }

    /**
     * Test that {@code CpioArchiveOutputStream#getFormat()} returns the format
     * of the CPIO stream.
     */
    @Test
    public void testGetFormat() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        assertEquals(CpioConstants.FORMAT_NEW, outputStream.getFormat());
    }

    /**
     * Test that {@code CpioArchiveOutputStream#getSize()} returns the size of
     * the CPIO stream.
     *
     * @throws IOException if an I/O error has occurred or if a CPIO file error
     *                    has occurred
     */
    @Test
    public void testGetSize() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(12345);
        outputStream.putNextEntry(entry);
        outputStream.write(new byte[] { 0x01, 0x02, 0x03 }, 0, 3);
        outputStream.close();
        assertEquals(12348, outputStream.getSize());
    }

    /**
     * Test that {@code CpioArchiveOutputStream#getEntries()} returns the names
     * of all entries in the CPIO stream.
     *
     * @throws IOException if an I/O error has occurred or if a CPIO file error
     *                    has occurred
     */
    @Test
    public void testGetEntries() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile1");
        entry1.setSize(12345);
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2");
        entry2.setSize(6789);
        outputStream.putNextEntry(entry1);
        outputStream.putNextEntry(entry2);
        outputStream.finish();
        Map<String, CpioArchiveEntry> expected = new HashMap<>();
        expected.put("testfile1", entry1);
        expected.put("testfile2", entry2);
        Map<String, CpioArchiveEntry> actual = outputStream.getEntries();
        assertEquals(expected, actual);
    }

    /**
     * Test that {@code CpioArchiveOutputStream#getEntry(String)} returns the
     * entry with the given name.
     *
     * @throws IOException if an I/O error has occurred or if a CPIO file error
     *                    has occurred
     */
    @Test
    public void testGetEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile1");
        entry1.setSize(12345);
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2");
        entry2.setSize(6789);
        outputStream.putNextEntry(entry1);
        outputStream.putNextEntry(entry2);
        outputStream.finish();
        CpioArchiveEntry actual = outputStream.getEntry("testfile1");
        assertEquals(entry1, actual);
        actual = outputStream.getEntry("testfile2");
        assertEquals(entry2, actual);
        try {
            actual = outputStream.getEntry("nonexistent");
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test that {@code CpioArchiveOutputStream#getComment()} returns the comment
     * of the CPIO stream.
     *
     * @throws IOException if an I/O error has occurred or if a CPIO file error
     *                    has occurred
     */
    @Test
    public void testGetComment() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setComment("This is a test comment.");
        outputStream.putNextEntry(entry);
        String expected = "This is a test comment.";
        String actual = outputStream.getComment();
        assertEquals(expected, actual);
        assertNull(outputStream.getComment());
    }
}