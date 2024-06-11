package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the CpioArchiveOutputStream class
 */
public class CpioArchiveOutputStreamTest {

    private File temporaryFile;
    private OutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        temporaryFile = File.createTempFile("test", null);
        outputStream = new FileOutputStream(temporaryFile);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        if (outputStream != null) {
            outputStream.close();
        }
        if (temporaryFile != null && temporaryFile.exists()) {
            Files.delete(Paths.get(temporaryFile.getAbsolutePath()));
        }
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        CpioArchiveOutputStream cpioArchiveOutputStream = (CpioArchiveOutputStream) this.outputStream;

        ArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        assertNotNull(cpioArchiveOutputStream.getNextEntry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullEntry() throws Exception {
        CpioArchiveOutputStream cpioArchiveOutputStream = (CpioArchiveOutputStream) this.outputStream;
        cpioArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws Exception {
        CpioArchiveOutputStream cpioArchiveOutputStream = (CpioArchiveOutputStream) this.outputStream;

        ArchiveEntry entry1 = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry1);
        ArchiveEntry entry2 = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry2);
    }

    @Test
    public void testWriteWithNullBytes() throws Exception {
        CpioArchiveOutputStream cpioArchiveOutputStream = (CpioArchiveOutputStream) this.outputStream;
        cpioArchiveOutputStream.write(null, 0, 0);
    }

    @Test
    public void testWrite() throws Exception {
        CpioArchiveOutputStream cpioArchiveOutputStream = (CpioArchiveOutputStream) this.outputStream;

        byte[] testBytes = "abcdefg".getBytes();
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);
        ArchiveEntry nextEntry = cpioArchiveOutputStream.getNextEntry();
        assertEquals(testBytes.length, nextEntry.getSize());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteWithInvalidOffset() throws Exception {
        CpioArchiveOutputStream cpioArchiveOutputStream = (CpioArchiveOutputStream) this.outputStream;

        byte[] testBytes = "abcdefg".getBytes();
        cpioArchiveOutputStream.write(testBytes, 5, testBytes.length);
    }

    @Test
    public void testWriteWithZeroLength() throws Exception {
        CpioArchiveOutputStream cpioArchiveOutputStream = (CpioArchiveOutputStream) this.outputStream;

        cpioArchiveOutputStream.write(new byte[0], 0, 0);
        ArchiveEntry nextEntry = cpioArchiveOutputStream.getNextEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testFinish() throws Exception {
        CpioArchiveOutputStream cpioArchiveOutputStream = (CpioArchiveOutputStream) this.outputStream;

        byte[] testBytes = "abcdefg".getBytes();
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry("test.txt"));
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);
        cpioArchiveOutputStream.finish();

        File file = new File(temporaryFile.getAbsolutePath());
        FileInputStream inputStream = new FileInputStream(file);
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);
        ArchiveEntry nextEntry = archiveInputStream.getNextEntry();

        assertEquals("test.txt", nextEntry.getName());
        assertEquals(testBytes.length, nextEntry.getSize());

        InputStream inputStreamEntry = archiveInputStream.getInputStream();
        byte[] readBytes = new byte[testBytes.length];
        inputStreamEntry.read(readBytes, 0, testBytes.length);
        assertArrayEquals(testBytes, readBytes);
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws Exception {
        CpioArchiveOutputStream cpioArchiveOutputStream = (CpioArchiveOutputStream) this.outputStream;

        byte[] testBytes = "abcdefg".getBytes();
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry("test.txt"));
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);

        ArchiveEntry nextEntry = cpioArchiveOutputStream.getNextEntry();
        assertNull(nextEntry);

        cpioArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testCloseWithoutFinish() throws Exception {
        CpioArchiveOutputStream cpioArchiveOutputStream = (CpioArchiveOutputStream) this.outputStream;

        cpioArchiveOutputStream.close();
    }

    @Test
    public void testClose() throws Exception {
        CpioArchiveOutputStream cpioArchiveOutputStream = (CpioArchiveOutputStream) this.outputStream;

        byte[] testBytes = "abcdefg".getBytes();
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry("test.txt"));
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        File file = new File(temporaryFile.getAbsolutePath());
        FileInputStream inputStream = new FileInputStream(file);
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);
        ArchiveEntry nextEntry = archiveInputStream.getNextEntry();

        assertEquals("test.txt", nextEntry.getName());
        assertEquals(testBytes.length, nextEntry.getSize());

        InputStream inputStreamEntry = archiveInputStream.getInputStream();
        byte[] readBytes = new byte[testBytes.length];
        inputStreamEntry.read(readBytes, 0, testBytes.length);
        assertArrayEquals(testBytes, readBytes);

        assertNull(archiveInputStream.getNextEntry());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveEntry() throws Exception {
        CpioArchiveOutputStream cpioArchiveOutputStream = (CpioArchiveOutputStream) this.outputStream;

        File inputFile = new File("testInput.txt");
        Files.write(Paths.get(inputFile.getAbsolutePath()), "testInput".getBytes());

        ArchiveEntry archiveEntry = cpioArchiveOutputStream.createArchiveEntry(inputFile, "testInput.txt");
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        File file = new File(temporaryFile.getAbsolutePath());
        FileInputStream inputStream = new FileInputStream(file);
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);

        ArchiveEntry nextEntry = archiveInputStream.getNextEntry();
        assertEquals("testInput.txt", nextEntry.getName());
        assertEquals(inputFile.length(), nextEntry.getSize());

        InputStream inputStreamEntry = archiveInputStream.getInputStream();
        byte[] readBytes = new byte[inputFile.length()];
        inputStreamEntry.read(readBytes, 0, readBytes.length);
        assertArrayEquals(Files.readAllBytes(Paths.get(inputFile.getAbsolutePath())), readBytes);

        assertNull(archiveInputStream.getNextEntry());
        archiveInputStream.close();
    }
}