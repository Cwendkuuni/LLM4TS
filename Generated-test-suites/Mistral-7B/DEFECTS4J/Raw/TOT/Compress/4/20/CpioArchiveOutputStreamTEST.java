package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CpioArchiveOutputStream} class.
 */
public class CpioArchiveOutputStreamTest {

    private File tempFile;
    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteStream;
    private OutputStream out;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("junit-test-", null);
        byteStream = new ByteArrayOutputStream();
        out = new FilterOutputStream(new FileOutputStream(tempFile));
        outputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (outputStream != null) {
            outputStream.close();
            outputStream = null;
        }
        if (byteStream != null) {
            byteStream.close();
            byteStream = null;
        }
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        final String testFileName = "testfile.txt";
        final String testFileContent = "Test content.";

        outputStream.putArchiveEntry(new CpioArchiveEntry(testFileName));
        outputStream.write(testFileContent.getBytes());
        outputStream.closeArchiveEntry();

        assertEquals(1, outputStream.getNextEntry().getName().length());
        assertEquals("testfile.txt", outputStream.getNextEntry().getName());
        assertEquals(CpioConstants.C_ISREG, outputStream.getNextEntry().getMode());
        assertEquals(-1, outputStream.getNextEntry().getTime());
        assertEquals(testFileContent.length(), outputStream.getNextEntry().getSize());

        outputStream = new CpioArchiveOutputStream(byteStream, CpioConstants.FORMAT_NEW);
        final ArchiveEntry archiveEntry = new CpioArchiveEntry(testFileName);
        outputStream.putArchiveEntry(archiveEntry);
        outputStream.write(testFileContent.getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        final byte[] expectedOutput = new byte[] {
                (byte) 0660, (byte) 0, (byte) 'C', (byte) 'P', (byte) 'I', (byte) 'O',
                (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 044, (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) 'f',
                (byte) 'i', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'x',
                (byte) 't', (byte) 0, (byte) 0,
                (byte) 32, (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) ' ',
                (byte) 'c', (byte) 'o', (byte) 'n', (byte) 't', (byte) 'e', (byte) 'n',
                (byte) 't', (byte) '.', (byte) 0, (byte) 0,
                (byte) 020, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 11, (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) ' ',
                (byte) 'c', (byte) 'o', (byte) 'n', (byte) 't', (byte) 'e', (byte) 'n',
                (byte) 't', (byte) '.', (byte) 0, (byte) 0,
                (byte) 016, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 010, (byte) '\0'};

        assertEquals(expectedOutput.length, byteStream.size());
        assertArrayEquals(expectedOutput, byteStream.toByteArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithWrongFormat() throws IOException {
        outputStream = new CpioArchiveOutputStream(out, 3);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        outputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        outputStream.putArchiveEntry(new CpioArchiveEntry("testfile.txt"));
        outputStream.putArchiveEntry(new CpioArchiveEntry("testfile.txt"));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNonExistingFile() throws IOException {
        outputStream.putArchiveEntry(new CpioArchiveEntry("nonExistingFile.txt"));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullFileName() throws IOException {
        outputStream.putArchiveEntry(new CpioArchiveEntry(null));
    }

    @Test
    public void testWrite() throws IOException {
        final String testFileName = "testfile.txt";
        final String testFileContent = "Test content.";

        outputStream.putArchiveEntry(new CpioArchiveEntry(testFileName));
        outputStream.write(testFileContent.getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        final File tempFileExpected = new File(tempFile.getAbsolutePath() +
                "_expected");
        FileUtils.copyFile(new File(tempFile.getAbsolutePath()), tempFileExpected);
        final byte[] expectedOutput = FileUtils.readFileToByteArray(tempFileExpected);

        outputStream = new CpioArchiveOutputStream(new ByteArrayInputStream(
                byteStream.toByteArray()), CpioConstants.FORMAT_NEW);
        byteStream = new ByteArrayOutputStream();
        outputStream.putNextEntry(new CpioArchiveEntry("testfile.txt"));
        outputStream.write(testFileContent.getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        final byte[] actualOutput = byteStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullInputStream() throws IOException {
        outputStream.write(null);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeLength() throws IOException {
        outputStream.write(new byte[] {"test".getBytes()}, -1, 1);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeOffset() throws IOException {
        outputStream.write(new byte[] {"test".getBytes()}, -1, 1);
    }

    @Test
    public void testFinish() throws IOException {
        outputStream.finish();
        outputStream.close();

        byteStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteStream, CpioConstants.FORMAT_NEW);
        outputStream.finish();
        outputStream.close();

        assertEquals(0, byteStream.size());
    }

    @Test(expected = IOException.class)
    public void testFinishWithNonFinishedArchive() throws IOException {
        outputStream.finish();
        outputStream.putArchiveEntry(new CpioArchiveEntry("testfile.txt"));
        outputStream.write(new byte[1]);

        outputStream.finish();
        outputStream.close();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        final String testFileName = "testfile.txt";

        outputStream.putArchiveEntry(new CpioArchiveEntry(testFileName));
        outputStream.write(new byte[1]);
        outputStream.closeArchiveEntry();

        assertNull(outputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithNonClosedArchive() throws IOException {
        outputStream.putArchiveEntry(new CpioArchiveEntry("testfile.txt"));
        outputStream.write(new byte[1]);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        outputStream.closeArchiveEntry();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        final File testFile = new File("testfile.txt");
        final String testFileName = "testfile.txt";

        ArchiveEntry archiveEntry = outputStream.createArchiveEntry(testFile, testFileName);
        assertEquals(testFileName, archiveEntry.getName());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateArchiveEntryWithNonExistingFile() throws IOException {
        outputStream.createArchiveEntry(new File("nonExistingFile.txt"), "nonExistingFile.txt");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateArchiveEntryWithNullFile() throws IOException {
        outputStream.createArchiveEntry(null, "testfile.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryWithNullFileName() throws IOException {
        outputStream.createArchiveEntry(new File("testfile.txt"), null);
    }
}