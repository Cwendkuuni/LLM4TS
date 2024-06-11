import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream archiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        archiveOutputStream.close();
    }

    /**
     * Tests that the constructor with the specified format is successful.
     */
    @Test
    public void testCpioArchiveOutputStreamNewFormat() {
        assertNotNull(archiveOutputStream);
    }

    /**
     * Tests that the constructor with the default format is successful.
     */
    @Test
    public void testCpioArchiveOutputStreamDefaultFormat() {
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream);
        assertNotNull(archiveOutputStream);
    }

    /**
     * Tests that {@code putArchiveEntry} with a valid entry is successful.
     *
     * @throws IOException if an I/O error occurs or if the entry format does not match the output format.
     */
    @Test
    public void testPutArchiveEntrySuccess() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        archiveOutputStream.putArchiveEntry(entry);
        assertTrue(archiveOutputStream.getNextEntry() instanceof CpioArchiveEntry);
        assertEquals("testfile", ((CpioArchiveEntry) archiveOutputStream.getNextEntry()).getName());
        archiveOutputStream.closeArchiveEntry();
        assertNull(archiveOutputStream.getNextEntry());
    }

    /**
     * Tests that {@code putArchiveEntry} with a null entry throws an IOException.
     */
    @Test(expected = IOException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        archiveOutputStream.putArchiveEntry(null);
    }

    /**
     * Tests that {@code putArchiveEntry} with an entry with an incorrect format
     * throws an IOException.
     */
    @Test(expected = IOException.class)
    public void testPutArchiveEntryIncorrectFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        archiveOutputStream.putArchiveEntry(entry);
    }

    /**
     * Tests that {@code write} writes data to the current entry correctly.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        archiveOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[]{'a', 'b', 'c'};
        int offset = 0;
        int length = data.length;
        archiveOutputStream.write(data, offset, length);
        assertEquals(length, archiveOutputStream.getNextEntry().getSize());

        archiveOutputStream.closeArchiveEntry();
        assertNull(archiveOutputStream.getNextEntry());
    }

    /**
     * Tests that {@code finish} finishes the CPIO output stream correctly.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        archiveOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[]{'a', 'b', 'c'};
        int offset = 0;
        int length = data.length;
        archiveOutputStream.write(data, offset, length);
        archiveOutputStream.finish();

        byte[] outputBytes = outputStream.toByteArray();
        CpioArchiveEntry trailerEntry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW, CPIO_TRAILER);
        assertTrue(Arrays.equals(CpioArchiveUtils.getTrailerBytes(trailerEntry), Arrays.copyOfRange(outputBytes, 0, CpioArchiveUtils.TRAILER_BYTES_LENGTH)));

        archiveOutputStream.close();
    }

    /**
     * Tests that {@code close} closes the CPIO output stream correctly.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        archiveOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[]{'a', 'b', 'c'};
        int offset = 0;
        int length = data.length;
        archiveOutputStream.write(data, offset, length);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        assertFalse(archiveOutputStream.isOpen());
    }

    /**
     * Tests that {@code putNextEntry} with a null entry and a non-empty stream
     * throws an IOException.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(expected = IOException.class)
    public void testPutNextEntryNullEntryWithNonEmptyStream() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(new FileOutputStream(new File("test.cpio")), CpioConstants.FORMAT_NEW);
        outputStream.write("testdata".getBytes());
        outputStream.putNextEntry(null);
    }
}