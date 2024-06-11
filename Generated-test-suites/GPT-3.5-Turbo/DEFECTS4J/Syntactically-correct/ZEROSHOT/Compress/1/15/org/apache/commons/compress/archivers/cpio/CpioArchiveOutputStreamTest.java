package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() {
        // Create the CpioArchiveOutputStream
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null);

        // Create a CpioArchiveEntry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        // Test putNextEntry method
        out.putNextEntry(entry);
        
        // Verify that the entry is successfully added
        assertEquals(entry, out.cpioEntry);
    }

    @Test
    public void testWrite() {
        // Create the CpioArchiveOutputStream
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null);

        // Create a CpioArchiveEntry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        // Add the entry to the output stream
        out.putNextEntry(entry);

        // Test write method
        byte[] content = "12345".getBytes();
        try {
            out.write(content);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        // Verify that the content is written correctly
        assertEquals(content.length, out.written);
    }

    @Test
    public void testFinish() {
        // Create the CpioArchiveOutputStream
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null);

        // Create a CpioArchiveEntry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        // Add the entry to the output stream
        out.putNextEntry(entry);

        // Test finish method
        try {
            out.finish();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        // Verify that the trailer entry is added correctly
        assertNotNull(out.cpioEntry);
        assertEquals("TRAILER!!!", out.cpioEntry.getName());
        assertEquals(1, out.cpioEntry.getNumberOfLinks());
    }

    @Test
    public void testClose() {
        // Create the CpioArchiveOutputStream
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null);

        // Test close method
        try {
            out.close();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        // Verify that the output stream is closed
        assertTrue(out.closed);
    }

}