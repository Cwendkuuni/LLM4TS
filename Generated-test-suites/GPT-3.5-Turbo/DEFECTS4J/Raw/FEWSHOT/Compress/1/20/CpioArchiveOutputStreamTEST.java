package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new NullOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        out.putNextEntry(entry);
        out.write(contents.getBytes());
        out.closeArchiveEntry();

        assertEquals(1, out.names.size());
        assertTrue(out.names.containsKey("testfile"));

        out.finish();
        CpioArchiveEntry trailer = out.cpioEntry;
        assertNotNull(trailer);
        assertEquals("TRAILER!!!", trailer.getName());
        assertEquals(1, trailer.getNumberOfLinks());
        assertEquals(0, trailer.getMode());

        out.close();
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryTwice() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new NullOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile1");
        out.putNextEntry(entry);

        // This line should throw an IOException
        out.putNextEntry(new CpioArchiveEntry());
    }

    // Write additional comprehensive test cases for all methods...
}