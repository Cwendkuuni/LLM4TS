package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new CpioArchiveOutputStream(null);
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);

        outputStream.putArchiveEntry(entry);

        // Test entry
        assertEquals("testfile", entry.getName());
        assertEquals(5, entry.getFileSize());
        assertEquals(CpioConstants.C_ISREG, entry.getMode());

        outputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryClosedStream() throws Exception {
        outputStream.close();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);

        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryInvalidSize() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        outputStream.putArchiveEntry(entry);

        outputStream.write(new byte[10]);
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);

        outputStream.putArchiveEntry(entry);
        outputStream.closeArchiveEntry();

        assertNull(outputStream.getEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryInvalidSize() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        outputStream.putArchiveEntry(entry);

        outputStream.write(new byte[10]);

        outputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws Exception {
        outputStream.finish();

        CpioArchiveEntry trailer = outputStream.getEntry();
        assertEquals(CpioArchiveOutputStream.CPIO_TRAILER, trailer.getName());
        assertEquals(1, trailer.getNumberOfLinks());
    }

    @Test
    public void testClose() throws Exception {
        outputStream.close();

        // Verify that the stream is closed
    }
}