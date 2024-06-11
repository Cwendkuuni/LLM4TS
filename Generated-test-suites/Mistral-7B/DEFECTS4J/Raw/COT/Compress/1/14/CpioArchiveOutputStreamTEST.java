package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.CpioConstants;
import org.junit.Test;

/**
 * Test class for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    /**
     * Tests constructor with default format.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testDefaultFormatConstruction() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outputStream);
        assertEquals(CpioConstants.FORMAT_NEW, cpioOut.getFormat());
    }

    /**
     * Tests constructor with specified format.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testFormatSpecifiedConstruction() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_BINARY);
        assertEquals(CpioConstants.FORMAT_OLD_BINARY, cpioOut.getFormat());
    }

    /**
     * Tests {@link #putNextEntry(CpioArchiveEntry)} with valid parameters.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testPutNextEntryValid() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOut.putNextEntry(entry);

        assertNull(cpioOut.getCpioEntry());
        assertEquals(entry, cpioOut.getCpioEntry());
        assertEquals(0, cpioOut.getWritten());
        assertEquals(1, cpioOut.names.size());
    }

    /**
     * Tests {@link #putNextEntry(CpioArchiveEntry)} with null entry.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testPutNextEntryNullEntry() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);

        assertThrows(NullPointerException.class, () -> cpioOut.putNextEntry(null));
    }

    /**
     * Tests {@link #putNextEntry(CpioArchiveEntry)} with duplicate entry.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testPutNextEntryDuplicateEntry() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        cpioOut.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");

        assertThrows(IllegalArgumentException.class, () -> cpioOut.putNextEntry(entry2));
    }

    /**
     * Tests {@link #write(byte[], int, int)} with valid parameters.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testWriteValid() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOut.putNextEntry(entry);

        byte[] data = "test data".getBytes();
        cpioOut.write(data, 0, data.length);

        assertEquals(data.length, cpioOut.getWritten());
        assertEquals(data, Arrays.copyOfRange(outStream.toByteArray(), 0, data.length));
    }

    /**
     * Tests {@link #write(byte[], int, int)} with negative offset.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testWriteNegativeOffset() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOut.putNextEntry(entry);

        byte[] data = "test data".getBytes();

        assertThrows(IndexOutOfBoundsException.class,
                () -> cpioOut.write(data, -1, data.length));
    }

    /**
     * Tests {@link #write(byte[], int, int)} with negative length.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testWriteNegativeLength() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOut.putNextEntry(entry);

        byte[] data = "test data".getBytes();

        assertThrows(NegativeArraySizeException.class,
                () -> cpioOut.write(data, data.length, -1));
    }

    /**
     * Tests {@link #write(byte[], int, int)} with invalid data length.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testWriteInvalidDataLength() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOut.putNextEntry(entry);

        byte[] data = "test data".getBytes();

        assertThrows(IndexOutOfBoundsException.class,
                () -> cpioOut.write(data, data.length + 1, data.length));
    }

    /**
     * Tests {@link #finish()} before writing any data.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testFinishBeforeWritingData() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);
        cpioOut.finish();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOut.putNextEntry(entry);

        assertThrows(IllegalStateException.class, () -> cpioOut.write(new byte[0][], 0, 0));
    }

    /**
     * Tests {@link #finish()} after writing some data.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testFinishAfterWritingData() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOut.putNextEntry(entry);
        cpioOut.write("test data".getBytes());
        cpioOut.finish();

        byte[] expectedData = "test data".getBytes();
        assertEquals(expectedData.length, cpioOut.getWritten());
        assertEquals(expectedData, Arrays.copyOfRange(outStream.toByteArray(), 0, expectedData.length));
    }

    /**
     * Tests {@link #close()} before writing any data.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testCloseBeforeWritingData() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);
        cpioOut.close();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOut.putNextEntry(entry);

        assertThrows(IllegalStateException.class, () -> cpioOut.write(new byte[0][], 0, 0));
    }

    /**
     * Tests {@link #close()} after writing some data.
     *
     * @throws IOException if an I/O error has occurred
     */
    @Test
    public void testCloseAfterWritingData() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOut.putNextEntry(entry);
        cpioOut.write("test data".getBytes());
        cpioOut.close();

        byte[] expectedData = "test data".getBytes();
        assertEquals(expectedData.length, cpioOut.getWritten());
        assertEquals(expectedData, Arrays.copyOfRange(outStream.toByteArray(), 0, expectedData.length));
        assertFalse(cpioOut.isOpen());
    }
}