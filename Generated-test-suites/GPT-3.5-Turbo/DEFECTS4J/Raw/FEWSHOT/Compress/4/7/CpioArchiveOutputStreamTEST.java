
package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CpioArchiveOutputStreamTest {
    @Test
    public void testPutArchiveEntry() {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(null, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setMode(CpioConstants.C_ISREG);
        try {
            outputStream.putArchiveEntry(entry);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(null, CpioConstants.FORMAT_NEW);
        try {
            outputStream.closeArchiveEntry();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testWrite() {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(null, CpioConstants.FORMAT_NEW);
        byte[] data = { 1, 2, 3, 4, 5 };
        try {
            outputStream.putArchiveEntry(new CpioArchiveEntry());
            outputStream.write(data, 0, data.length);
            outputStream.closeArchiveEntry();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testFinish() {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(null, CpioConstants.FORMAT_NEW);
        try {
            outputStream.finish();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testClose() {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(null, CpioConstants.FORMAT_NEW);
        try {
            outputStream.close();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(null, CpioConstants.FORMAT_NEW);
        File inputFile = new File("test.txt");
        String entryName = "testfile";
        try {
            outputStream.createArchiveEntry(inputFile, entryName);
        } catch (Exception e) {
            assertNull(e);
        }
    }
}
