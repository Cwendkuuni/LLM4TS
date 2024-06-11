package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    private static final byte[] MAGIC_NEW = ArchiveUtils.toByteArray(CpioConstants.MAGIC_NEW);
    private static final byte[] MAGIC_NEW_CRC = ArchiveUtils.toByteArray(CpioConstants.MAGIC_NEW_CRC);
    private static final byte[] MAGIC_OLD_ASCII = ArchiveUtils.toByteArray(CpioConstants.MAGIC_OLD_ASCII);
    private static final byte[] MAGIC_OLD_BINARY = ArchiveUtils.toByteArray(CpioConstants.MAGIC_OLD_BINARY);

    @Test
    public void testPutArchiveEntry() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final File testFile = new File("testFile.txt");
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, "testFile");

        cpioOut.putArchiveEntry(entry);
        cpioOut.write("This is a test file".getBytes());
        cpioOut.closeArchiveEntry();
        cpioOut.finish();

        final byte[] expectedOutput = createExpectedOutput("testFile.txt", "This is a test file");

        assertEquals(expectedOutput.length, out.size());
        assertArrayEquals(expectedOutput, out.toByteArray());

        cpioOut.close();
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_INVALID);

        final ArchiveEntry invalidEntry = new CpioArchiveEntry();
        assertThrows(IllegalArgumentException.class, () -> cpioOut.putArchiveEntry(invalidEntry));
        cpioOut.close();
    }

    @Test
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        assertThrows(NullPointerException.class, () -> cpioOut.putArchiveEntry(null));
        cpioOut.close();
    }

    @Test
    public void testPutArchiveEntryWithNullFileName() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final CpioArchiveEntry entry = new CpioArchiveEntry();
        assertThrows(NullPointerException.class, () -> cpioOut.putArchiveEntry(entry));
        cpioOut.close();
    }

    @Test
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final File testFile1 = new File("testFile1.txt");
        final File testFile2 = new File("testFile1.txt");
        final CpioArchiveEntry entry1 = new CpioArchiveEntry(testFile1, "testFile1");
        final CpioArchiveEntry entry2 = new CpioArchiveEntry(testFile2, "testFile1");

        cpioOut.putArchiveEntry(entry1);
        cpioOut.write("This is testFile1".getBytes());
        cpioOut.closeArchiveEntry();

        assertThrows(IOException.class, () -> cpioOut.putArchiveEntry(entry2));
        cpioOut.close();
    }

    @Test
    public void testWrite() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final byte[] data = "This is a test file".getBytes();

        cpioOut.write(data, 0, data.length);
        cpioOut.close();

        final byte[] expectedOutput = createExpectedOutput("testFile", "This is a test file");

        assertEquals(expectedOutput.length, out.size());
        assertArrayEquals(expectedOutput, out.toByteArray());
    }

    @Test
    public void testWriteWithInvalidOffset() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final byte[] data = "This is a test file".getBytes();

        assertThrows(IndexOutOfBoundsException.class, () -> cpioOut.write(data, -1, data.length));
        assertThrows(IndexOutOfBoundsException.class, () -> cpioOut.write(data, data.length, data.length));
        cpioOut.close();
    }

    @Test
    public void testWriteWithZeroLengthData() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final byte[] data = new byte[0];

        cpioOut.write(data, 0, 0);
        cpioOut.close();

        final byte[] expectedOutput = createExpectedOutput("testFile", "");

        assertEquals(expectedOutput.length, out.size());
        assertArrayEquals(expectedOutput, out.toByteArray());
    }

    @Test
    public void testWriteWithOpenEntry() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final File testFile = new File("testFile.txt");
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, "testFile");
        cpioOut.putArchiveEntry(entry);

        final byte[] data = "This is a test file".getBytes();

        cpioOut.write(data, 0, data.length);
        cpioOut.closeArchiveEntry();

        final byte[] expectedOutput = createExpectedOutput("testFile.txt", "This is a test file");

        assertEquals(expectedOutput.length, out.size());
        assertArrayEquals(expectedOutput, out.toByteArray());

        cpioOut.close();
    }

    @Test
    public void testWriteWithClosedEntry() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final File testFile = new File("testFile.txt");
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, "testFile");
        cpioOut.putArchiveEntry(entry);
        cpioOut.closeArchiveEntry();

        final byte[] data = "This is a test file".getBytes();

        assertThrows(IllegalStateException.class, () -> cpioOut.write(data, 0, data.length));
        cpioOut.close();
    }

    @Test
    public void testWriteWithUnclosedStream() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final File testFile = new File("testFile.txt");
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, "testFile");
        cpioOut.putArchiveEntry(entry);

        final byte[] data = "This is a test file".getBytes();

        cpioOut.write(data, 0, data.length);
        cpioOut.close();

        final byte[] expectedOutput = createExpectedOutput("testFile.txt", "This is a test file");

        assertEquals(expectedOutput.length, out.size());
        assertArrayEquals(expectedOutput, out.toByteArray());

        cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        assertThrows(IOException.class, () -> cpioOut.putArchiveEntry(entry));
        cpioOut.close();
    }

    @Test
    public void testCloseArchiveEntryWithOpenStream() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final File testFile = new File("testFile.txt");
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, "testFile");
        cpioOut.putArchiveEntry(entry);

        cpioOut.closeArchiveEntry();
        cpioOut.close();

        assertNull(cpioOut.getNextEntry());
    }

    @Test
    public void testCloseArchiveEntryWithClosedStream() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final File testFile = new File("testFile.txt");
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, "testFile");
        cpioOut.putArchiveEntry(entry);
        cpioOut.closeArchiveEntry();
        cpioOut.close();

        assertNull(cpioOut.getNextEntry());
    }

    @Test
    public void testCloseArchiveEntryWithNullEntry() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        assertThrows(NullPointerException.class, () -> cpioOut.closeArchiveEntry());
        cpioOut.close();
    }

    @Test
    public void testFinishWithOpenEntry() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final File testFile = new File("testFile.txt");
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, "testFile");
        cpioOut.putArchiveEntry(entry);

        cpioOut.finish();
        cpioOut.write("This is the CPIO trailer".getBytes());
        cpioOut.closeArchiveEntry();
        cpioOut.close();

        final byte[] expectedOutput = createExpectedOutput("testFile.txt", "This is a test file")
                + CpioConstants.CPIO_TRAILER;

        assertEquals(expectedOutput.length, out.size());
        assertArrayEquals(expectedOutput, out.toByteArray());
    }

    @Test
    public void testFinishWithClosedEntry() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final File testFile = new File("testFile.txt");
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, "testFile");
        cpioOut.putArchiveEntry(entry);
        cpioOut.closeArchiveEntry();

        cpioOut.finish();
        cpioOut.write("This is the CPIO trailer".getBytes());
        cpioOut.close();

        final byte[] expectedOutput = createExpectedOutput("testFile.txt", "")
                + CpioConstants.CPIO_TRAILER;

        assertEquals(expectedOutput.length, out.size());
        assertArrayEquals(expectedOutput, out.toByteArray());
    }

    @Test
    public void testFinishWithNullEntry() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        assertThrows(NullPointerException.class, () -> cpioOut.finish());
        cpioOut.close();
    }

    @Test
    public void testFinishWithClosedStream() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        cpioOut.finish();
        cpioOut.close();

        assertTrue(cpioOut.isClosed());
    }

    @Test
    public void testFinishWithOpenStream() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        cpioOut.finish();

        assertFalse(cpioOut.isClosed());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final File testFile = new File("testFile.txt");
        final ArchiveEntry entry = cpioOut.createArchiveEntry(testFile, "testFile");

        assertEquals("testFile", entry.getName());
        assertEquals(testFile.getAbsolutePath(), entry.getFileName());
        assertEquals(CpioConstants.FORMAT_NEW, entry.getFormat());

        cpioOut.close();
    }

    @Test
    public void testCreateArchiveEntryWithNullFile() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        assertThrows(NullPointerException.class, () -> cpioOut.createArchiveEntry(null, "testFile"));
        cpioOut.close();
    }

    @Test
    public void testCreateArchiveEntryWithNullFileName() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        final File testFile = new File("testFile.txt");
        assertThrows(NullPointerException.class, () -> cpioOut.createArchiveEntry(testFile, null));
        cpioOut.close();
    }

    private byte[] createExpectedOutput(String fileName, String fileContents) {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(output, CpioConstants.FORMAT_NEW);

        final File testFile = new File(fileName);
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, fileName);
        try {
            cpioOut.putArchiveEntry(entry);
            cpioOut.write(fileContents.getBytes());
            cpioOut.closeArchiveEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cpioOut.finish();

        return output.toByteArray();
    }
}