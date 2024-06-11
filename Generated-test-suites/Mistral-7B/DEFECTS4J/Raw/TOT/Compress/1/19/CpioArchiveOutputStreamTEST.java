package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;

import org.junit.Test;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    /**
     * Tests that a new {@link CpioArchiveOutputStream} is created correctly.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testConstructor() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        assertEquals(CpioConstants.FORMAT_NEW, cpioStream.getFormat());
        assertTrue(cpioStream.isWriteMode());
        assertFalse(cpioStream.isReadMode());
        assertNull(cpioStream.getCurrentEntry());
        assertFalse(cpioStream.isFinished());
        assertFalse(cpioStream.isClosed());
    }

    /**
     * Tests that {@link CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)}
     * sets the current entry correctly and writes the header.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testPutNextEntry() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final String testFileName = "test_file.txt";
        final CpioArchiveEntry testEntry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        testEntry.setName(testFileName);

        cpioStream.putNextEntry(testEntry);

        final byte[] expectedHeader = new byte[] {
                MAGIC_NEW[0], MAGIC_NEW[1], MAGIC_NEW[2],
                MAGIC_NEW[3], MAGIC_NEW[4], MAGIC_NEW[5],
                testFileName.getBytes()[0], testFileName.getBytes()[1],
                testFileName.getBytes()[2], testFileName.getBytes()[3],
                testFileName.getBytes()[4], testFileName.getBytes()[5],
                testFileName.getBytes()[6], testFileName.getBytes()[7],
                (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78,
                (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) testFileName.length(), (byte) 0x00
        };

        assertEquals(testEntry, cpioStream.getCurrentEntry());
        assertEquals(expectedHeader.length, out.size());
        assertTrue(Arrays.equals(expectedHeader, out.toByteArray()));

        final Map<String, ArchiveEntry> entries = new HashMap<>();
        entries.put(testFileName, testEntry);
        assertEquals(entries, cpioStream.getEntries());
    }

    /**
     * Tests that {@link CpioArchiveOutputStream#write(byte[], int, int)} writes
     * the content of the current entry.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testWrite() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final String testFileName = "test_file.txt";
        final CpioArchiveEntry testEntry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        testEntry.setName(testFileName);
        testEntry.setSize(7);

        cpioStream.putNextEntry(testEntry);

        final byte[] testData = new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77 };
        cpioStream.write(testData, 0, testData.length);

        final byte[] expectedContent = new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77 };

        final Map<String, ArchiveEntry> entries = new HashMap<>();
        entries.put(testFileName, testEntry);
        assertEquals(entries, cpioStream.getEntries());
        assertEquals(expectedContent.length, cpioStream.size());
        assertTrue(Arrays.equals(expectedContent, cpioStream.getInputStream().readAllBytes()));
    }

    /**
     * Tests that {@link CpioArchiveOutputStream#finish()} writes the trailer
     * and closes the underlying stream.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testFinish() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final String testFileName1 = "test_file1.txt";
        final CpioArchiveEntry testEntry1 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        testEntry1.setName(testFileName1);
        testEntry1.setSize(10);

        final String testFileName2 = "test_file2.txt";
        final CpioArchiveEntry testEntry2 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        testEntry2.setName(testFileName2);
        testEntry2.setSize(15);

        cpioStream.putNextEntry(testEntry1);
        cpioStream.write(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88 }, 0, 8);
        cpioStream.putNextEntry(testEntry2);
        cpioStream.write(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88, 0x99 }, 0, 9);
        cpioStream.finish();

        final byte[] expectedTrailer = new byte[] {
                MAGIC_NEW[0], MAGIC_NEW[1], MAGIC_NEW[2],
                MAGIC_NEW[3], MAGIC_NEW[4], MAGIC_NEW[5],
                "TRAILER!!!".getBytes()[0], "TRAILER!!!".getBytes()[1],
                "TRAILER!!!".getBytes()[2], "TRAILER!!!".getBytes()[3],
                "TRAILER!!!".getBytes()[4], "TRAILER!!!".getBytes()[5],
                (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78
        };

        final Map<String, ArchiveEntry> entries = new HashMap<>();
        entries.put(testFileName1, testEntry1);
        entries.put(testFileName2, testEntry2);
        assertEquals(entries, cpioStream.getEntries());
        assertTrue(Arrays.equals(expectedTrailer, out.toByteArray()));
    }

    /**
     * Tests that {@link CpioArchiveOutputStream#close()} closes the underlying
     * stream and sets all fields to their initial state.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testClose() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        cpioStream.close();

        assertEquals(CpioConstants.FORMAT_NEW, cpioStream.getFormat());
        assertFalse(cpioStream.isWriteMode());
        assertFalse(cpioStream.isReadMode());
        assertNull(cpioStream.getCurrentEntry());
        assertFalse(cpioStream.isFinished());
        assertFalse(cpioStream.isClosed());
    }

    /**
     * Tests that {@link CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)}
     * throws an {@link IOException} if a new entry is attempted to be written
     * after the stream has been finished or closed.
     */
    @Test
    public void testPutNextEntryAfterFinishedOrClosed() {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        cpioStream.finish();

        assertThrows(IOException.class, () -> cpioStream.putNextEntry(new CpioArchiveEntry(CpioConstants.FORMAT_NEW)));

        cpioStream.close();

        assertThrows(IOException.class, () -> cpioStream.putNextEntry(new CpioArchiveEntry(CpioConstants.FORMAT_NEW)));
    }

    /**
     * Tests that {@link CpioArchiveOutputStream#write(int)} throws an
     * {@link IOException} if a new byte is attempted to be written after the
     * stream has been finished or closed.
     */
    @Test
    public void testWriteAfterFinishedOrClosed() {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        cpioStream.finish();

        assertThrows(IOException.class, () -> cpioStream.write(0x11));

        cpioStream.close();

        assertThrows(IOException.class, () -> cpioStream.write(0x11));
    }
}