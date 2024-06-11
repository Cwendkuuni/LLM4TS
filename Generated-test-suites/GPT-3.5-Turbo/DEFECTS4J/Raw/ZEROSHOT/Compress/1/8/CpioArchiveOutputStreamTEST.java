package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() {
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        try {
            cpioOut.putNextEntry(entry);
            cpioOut.write(contents.getBytes());
            cpioOut.closeEntry();
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
        // TODO add assertions for checking the file contents
    }

    @Test
    public void testCloseEntry() {
        // TODO implement test case
    }

    @Test
    public void testWrite() {
        // TODO implement test case
    }

    @Test
    public void testFinish() {
        // TODO implement test case
    }

    @Test
    public void testClose() {
        // TODO implement test case
    }

}