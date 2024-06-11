package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Test class for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private CpioArchiveOutputStream archiveOutputStream;
    private CpioArchiveEntry entry;

    /**
     * Setup method for each test.
     *
     * @throws IOException if initialization failed
     */
    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    /**
     * Test if putting a new entry with correct format initializes the header
     * correctly.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testPutNextEntryWithCorrectFormat() throws IOException {
        entry = new CpioArchiveEntry();
        archiveOutputStream.putNextEntry(entry);
        byte[] header = outputStream.toByteArray();

        assertEquals(CpioConstants.MAGIC_NEW.length, header.length);
        assertEquals(CpioConstants.MAGIC_NEW[0], header[0]);
        assertEquals(CpioConstants.MAGIC_NEW[1], header[1]);
    }

    /**
     * Test if putting an entry with incorrect format throws an exception.
     *
     * @throws IOException if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryWithIncorrectFormat() throws IOException {
        entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY + 1);
        archiveOutputStream.putNextEntry(entry);
    }

    /**
     * Test if putting multiple entries writes their headers correctly.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testPutNextEntryMultipleTimes() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        archiveOutputStream.putNextEntry(entry1);
        archiveOutputStream.putNextEntry(entry2);

        byte[] header1 = outputStream.toByteArray();
        byte[] header2 = outputStream.toByteArray(outputStream.size());

        assertEquals(CpioConstants.MAGIC_NEW.length, header1.length);
        assertEquals(CpioConstants.MAGIC_NEW[0], header1[0]);
        assertEquals(CpioConstants.MAGIC_NEW[1], header1[1]);

        assertEquals(CpioConstants.MAGIC_NEW.length, header2.length);
        assertEquals(CpioConstants.MAGIC_NEW[0], header2[0]);
        assertEquals(CpioConstants.MAGIC_NEW[1], header2[1]);

        // Check if names are unique
        assertEquals(entry1.getName(), entry2.getName());
        assertNull(archiveOutputStream.names.get(entry1.getName()));
    }

    /**
     * Test if closeArchiveEntry writes the correct data to the output stream.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        archiveOutputStream.putNextEntry(entry);

        byte[] header = outputStream.toByteArray();
        assertEquals(CpioConstants.MAGIC_NEW.length, header.length);
        assertEquals(CpioConstants.MAGIC_NEW[0], header[0]);
        assertEquals(CpioConstants.MAGIC_NEW[1], header[1]);

        archiveOutputStream.closeArchiveEntry();
        byte[] data = outputStream.toByteArray(outputStream.size());

        assertEquals(entry.getSize(), data.length);
    }

    /**
     * Test if finish writes the correct trailer data to the output stream.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testFinish() throws IOException {
        archiveOutputStream.finish();
        byte[] trailer = outputStream.toByteArray(outputStream.size());

        byte[] expectedTrailer = new byte[CpioConstants.TRAILER_SIZE];
        System.arraycopy(CpioConstants.TRAILER, 0, expectedTrailer, 0,
                CpioConstants.TRAILER.length);

        assertEquals(expectedTrailer.length, trailer.length);
        assertArrayEquals(expectedTrailer, trailer);
    }

    /**
     * Test if the class closes properly if an error occurs.
     *
     * @throws IOException if an error occurs
     */
    @Test(expected = IOException.class)
    public void testCloseOnException() throws IOException {
        OutputStream out = new FilterOutputStream(new ByteArrayOutputStream()) {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }
        };
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        stream.putNextEntry(new CpioArchiveEntry());
        stream.close();
    }
}