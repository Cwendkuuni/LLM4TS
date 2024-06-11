package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    /**
     * Test method for {@link CpioArchiveOutputStream#CpioArchiveOutputStream(OutputStream, short)}
     * @throws IOException
     */
    @org.junit.Test
    public void testCpioArchiveOutputStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        assertNull(cpioStream.getEntry());
        assertFalse(cpioStream.isClosed());
        assertFalse(cpioStream.isFinished());
        assertEquals(CpioConstants.FORMAT_NEW, cpioStream.getFormat());
        assertEquals(out, cpioStream.getOutputStream());
        assertSame(cpioStream, cpioStream.putArchiveEntry(createArchiveEntry("test.txt", "test.txt")));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putArchiveEntry(ArchiveEntry)}
     * @throws IOException
     */
    @org.junit.Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        File fileToWrite = new File("test.txt");
        ArchiveEntry entry = createArchiveEntry(fileToWrite, "test.txt");
        ArchiveEntry entryWritten = cpioStream.putArchiveEntry(entry);
        assertSame(entry, entryWritten);
        assertSame(cpioStream.getEntry(), entryWritten);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)}
     * @throws IOException
     */
    @org.junit.Test
    public void testWrite() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        File fileToWrite = new File("test.txt");
        ArchiveEntry entry = createArchiveEntry(fileToWrite, "test.txt");
        cpioStream.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        int offset = 0;
        int length = data.length;
        cpioStream.write(data, offset, length);
        byte[] actualData = ((ByteArrayOutputStream)out).toByteArray();
        byte[] expectedData = new byte[length + entry.getSize()];
        System.arraycopy(entry.getName().getBytes(), 0, expectedData, 0, entry.getName().length());
        System.arraycopy(data, offset, expectedData, entry.getName().length(), data.length);
        assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#finish()}
     * @throws IOException
     */
    @org.junit.Test
    public void testFinish() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        File fileToWrite = new File("test.txt");
        ArchiveEntry entry = createArchiveEntry(fileToWrite, "test.txt");
        cpioStream.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        int offset = 0;
        int length = data.length;
        cpioStream.write(data, offset, length);
        cpioStream.finish();
        byte[] actualData = ((ByteArrayOutputStream)out).toByteArray();
        byte[] expectedData = new byte[length + entry.getSize() + 32]; // Trailer size
        System.arraycopy(entry.getName().getBytes(), 0, expectedData, 0, entry.getName().length());
        System.arraycopy(data, offset, expectedData, entry.getName().length() + 8, data.length);
        assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#close()}
     * @throws IOException
     */
    @org.junit.Test
    public void testClose() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        File fileToWrite = new File("test1.txt");
        ArchiveEntry entry1 = createArchiveEntry(fileToWrite, "test1.txt");
        File fileToWrite2 = new File("test2.txt");
        ArchiveEntry entry2 = createArchiveEntry(fileToWrite2, "test2.txt");
        cpioStream.putArchiveEntry(entry1);
        byte[] data1 = "test1 data".getBytes();
        int offset1 = 0;
        int length1 = data1.length;
        cpioStream.write(data1, offset1, length1);
        cpioStream.putArchiveEntry(entry2);
        byte[] data2 = "test2 data".getBytes();
        int offset2 = 0;
        int length2 = data2.length;
        cpioStream.write(data2, offset2, length2);
        cpioStream.finish();
        byte[] actualData = ((ByteArrayOutputStream)out).toByteArray();
        byte[] expectedData = new byte[length1 + entry1.getSize() + length2 + entry2.getSize() + 32]; // Trailer size
        System.arraycopy(entry1.getName().getBytes(), 0, expectedData, 0, entry1.getName().length());
        System.arraycopy(data1, offset1, expectedData, entry1.getName().length() + 8, data1.length);
        System.arraycopy(entry2.getName().getBytes(), 0, expectedData, length1 + entry1.getName().length() + 8, entry2.getName().length());
        System.arraycopy(data2, offset2, expectedData, length1 + entry1.getName().length() + entry2.getName().length() + 8, data2.length);
        assertArrayEquals(expectedData, actualData);
        cpioStream.close();
        assertEquals(true, cpioStream.isClosed());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#createArchiveEntry(File, String)}
     * @throws IOException
     */
    private ArchiveEntry createArchiveEntry(File file, String name) throws IOException {
        return new CpioArchiveEntry(file, name);
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

}