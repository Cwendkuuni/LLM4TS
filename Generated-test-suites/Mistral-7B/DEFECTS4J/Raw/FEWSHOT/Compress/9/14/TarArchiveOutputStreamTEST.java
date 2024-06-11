package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream output;

    @Before
    public void setUp() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        output = new TarArchiveOutputStream(os, TarBuffer.DEFAULT_BLKSIZE,
                                            TarBuffer.DEFAULT_RCDSIZE);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        // Test setting longFileMode to LONGFILE_ERROR
        output.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR,
                     output.getLongFileMode());

        // Test setting longFileMode to LONGFILE_TRUNCATE
        output.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE,
                     output.getLongFileMode());

        // Test setting longFileMode to LONGFILE_GNU
        output.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU,
                     output.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() throws IOException {
        output.setLongFileMode(-1);
    }

    @Test
    public void testFinish() throws IOException {
        // Test finish with no entries
        output.finish();
        ByteArrayOutputStream outStream = (ByteArrayOutputStream) output.getOutputStream();
        byte[] expected = new byte[1024 * 2];
        assertArrayEquals(expected, outStream.toByteArray());

        // Test finish with an entry
        output.putArchiveEntry(new TarArchiveEntry("test.txt"));
        output.finish();
        outStream = (ByteArrayOutputStream) output.getOutputStream();
        assertArrayEquals(new byte[1024 * 3], outStream.toByteArray());

        // Test finish twice
        output.putArchiveEntry(new TarArchiveEntry("test.txt"));
        output.finish();
        output.finish();
        outStream = (ByteArrayOutputStream) output.getOutputStream();
        assertArrayEquals(new byte[1024 * 4], outStream.toByteArray());
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishWithOpenEntry() throws IOException {
        output.putArchiveEntry(new TarArchiveEntry("test.txt"));
        output.finish();
        output.putArchiveEntry(new TarArchiveEntry("test2.txt"));
    }

    @Test
    public void testClose() throws IOException {
        output.putArchiveEntry(new TarArchiveEntry("test.txt"));
        output.close();
        ByteArrayOutputStream outStream = (ByteArrayOutputStream) output.getOutputStream();
        assertArrayEquals(new byte[1024 * 3], outStream.toByteArray());
    }

    @Test
    public void testWriteToStream() throws IOException {
        output.putArchiveEntry(new TarArchiveEntry("test.txt"));
        String testString = "This is a test.";
        byte[] testBytes = testString.getBytes();
        output.write(testBytes, 0, testBytes.length);
        output.close();
        ByteArrayOutputStream outStream = (ByteArrayOutputStream) output.getOutputStream();
        byte[] expected = new byte[testBytes.length + 1024];
        System.arraycopy(testBytes, 0, expected, 0, testBytes.length);
        assertArrayEquals(expected, outStream.toByteArray());
    }

    @Test
    public void testWriteToStreamWithAssemble() throws IOException {
        output.putArchiveEntry(new TarArchiveEntry("test.txt"));
        String testString = "This is a test.";
        byte[] testBytes = testString.getBytes();
        output.write(testBytes, 0, testBytes.length / 2);
        output.write(testBytes, testBytes.length / 2, testBytes.length / 2);
        output.write(testBytes, testBytes.length / 2, testBytes.length / 2);
        output.write(testBytes, testBytes.length / 2, testBytes.length / 2);
        output.write(testBytes, testBytes.length / 2, testBytes.length / 2);
        output.write(testBytes, testBytes.length / 2, testBytes.length / 2);
        output.write(testBytes, testBytes.length / 2, testBytes.length / 2);
        output.write(testBytes, testBytes.length / 2, testBytes.length / 2);
        output.write(testBytes, testBytes.length / 2, testBytes.length / 2);
        output.write(testBytes, testBytes.length / 2, testBytes.length / 2);
        output.write(testBytes, testBytes.length / 2, testBytes.length / 2);
        output.close();
        ByteArrayOutputStream outStream = (ByteArrayOutputStream) output.getOutputStream();
        byte[] expected = new byte[testBytes.length * 2 + 1024];
        System.arraycopy(testBytes, 0, expected, 0, testBytes.length);
        assertArrayEquals(expected, outStream.toByteArray());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        output.putArchiveEntry(new TarArchiveEntry("test.txt"));
        String testString = "This is a test.";
        byte[] testBytes = testString.getBytes();
        output.write(testBytes, 0, testBytes.length);
        output.closeArchiveEntry();
        output.write(testBytes, 0, testBytes.length);
        output.close();
        ByteArrayOutputStream outStream = (ByteArrayOutputStream) output.getOutputStream();
        byte[] expected = new byte[testBytes.length * 2 + 1024];
        System.arraycopy(testBytes, 0, expected, 0, testBytes.length);
        assertArrayEquals(expected, outStream.toByteArray());
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        output.write(new byte[1024], 0, 1024);
        output.closeArchiveEntry();
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishWithOpenEntry() throws IOException {
        output.putArchiveEntry(new TarArchiveEntry("test.txt"));
        output.finish();
        output.putArchiveEntry(new TarArchiveEntry("test2.txt"));
    }

    @Test
    public void testGetRecordSize() throws IOException {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, output.getRecordSize());
    }
}