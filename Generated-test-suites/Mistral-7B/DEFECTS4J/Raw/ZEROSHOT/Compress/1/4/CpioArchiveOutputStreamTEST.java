package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStream() throws IOException {
        // Test construction with default format
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(outputStream);
        assertNotNull(cpioStream);

        // Test construction with specified format
        OutputStream outputStream2 = new ByteArrayOutputStream();
        short format = CpioConstants.FORMAT_OLD_ASCII;
        CpioArchiveOutputStream cpioStream2 = new CpioArchiveOutputStream(outputStream2, format);
        assertEquals(format, cpioStream2.getFormat());

        // Test putNextEntry method
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioStream.putNextEntry(entry);
        assertNotNull(cpioStream.getCpioEntry());
        assertEquals("testfile", cpioStream.getCpioEntry().getName());

        // Test write method
        byte[] testBytes = "12345".getBytes();
        cpioStream.write(testBytes, 0, testBytes.length);
        assertEquals(testBytes.length, cpioStream.getWritten());

        // Test closeArchiveEntry method
        cpioStream.closeArchiveEntry();
        assertNull(cpioStream.getCpioEntry());

        // Test finish method
        cpioStream.finish();
        assertTrue(cpioStream.isFinished());

        // Test close method
        cpioStream.close();
        assertTrue(cpioStream.isClosed());

        // Test write multiple entries
        byte[] testBytes2 = "testfile2".getBytes();
        cpioStream = new CpioArchiveOutputStream(outputStream2);
        entry = new CpioArchiveEntry();
        entry.setName("testfile1");
        cpioStream.putNextEntry(entry);
        cpioStream.write(testBytes, 0, testBytes.length);
        cpioStream.closeArchiveEntry();

        entry = new CpioArchiveEntry();
        entry.setName("testfile2");
        cpioStream.putNextEntry(entry);
        cpioStream.write(testBytes2, 0, testBytes2.length);
        cpioStream.closeArchiveEntry();

        cpioStream.finish();
        cpioStream.close();

        // Test read back the archive
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream2;
        byte[] expectedBytes = new byte[byteArrayOutputStream.size()];
        System.arraycopy(byteArrayOutputStream.toByteArray(), 0, expectedBytes, 0, byteArrayOutputStream.size());

        CpioArchiveInputStream cpioInputStream = new CpioArchiveInputStream(new ByteArrayInputStream(expectedBytes));
        ArchiveEntry entryRead = cpioInputStream.getNextEntry();
        assertNotNull(entryRead);
        assertEquals("testfile1", entryRead.getName());
        entryRead = cpioInputStream.getNextEntry();
        assertNotNull(entryRead);
        assertEquals("testfile2", entryRead.getName());
        cpioInputStream.close();

        // Test duplicate entry
        cpioStream = new CpioArchiveOutputStream(outputStream2);
        entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioStream.putNextEntry(entry);
        cpioStream.putNextEntry(entry);
        try {
            cpioStream.finish();
            fail("Expected IOException for duplicate entry");
        } catch (IOException e) {
            // expected
        }
        cpioStream.close();
    }
}