package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestFiles;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * TarArchiveOutputStreamTest class.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        if (tarArchiveOutputStream != null) {
            tarArchiveOutputStream.close();
        }
    }

    /**
     * Test constructor TarArchiveOutputStream.
     * @throws IOException on error
     */
    @Test
    public void testConstructor() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(tarArchiveOutputStream);
    }

    /**
     * Test constructor TarArchiveOutputStream with OutputStream.
     * @throws IOException on error
     */
    @Test
    public void testConstructorOutputStream() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(tarArchiveOutputStream);
    }

    /**
     * Test constructor TarArchiveOutputStream with OutputStream and blockSize.
     * @throws IOException on error
     */
    @Test
    public void testConstructorOutputStreamBlockSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        assertNotNull(tarArchiveOutputStream);
    }

    /**
     * Test constructor TarArchiveOutputStream with OutputStream, blockSize and recordSize.
     * @throws IOException on error
     */
    @Test
    public void testConstructorOutputStreamBlockSizeRecordSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        assertNotNull(tarArchiveOutputStream);
    }

    /**
     * Test setLongFileMode LONGFILE_ERROR.
     * @throws IOException on error
     */
    @Test
    public void testSetLongFileModeLONGFILE_ERROR() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());
    }

    /**
     * Test setLongFileMode LONGFILE_TRUNCATE.
     * @throws IOException on error
     */
    @Test
    public void testSetLongFileModeLONGFILE_TRUNCATE() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    /**
     * Test setLongFileMode LONGFILE_GNU.
     * @throws IOException on error
     */
    @Test
    public void testSetLongFileModeLONGFILE_GNU() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    /**
     * Test finish method with empty archive.
     * @throws IOException on error
     */
    @Test
    public void testFinishEmptyArchive() throws IOException {
        tarArchiveOutputStream.finish();
        assertTrue(tarArchiveOutputStream.isFinished());
    }

    /**
     * Test finish method with archive entry.
     * @throws IOException on error
     */
    @Test
    public void testFinishWithEntry() throws IOException {
        File testFile = TestFiles.createTestFile("testFile.txt", "Test file content.");
        ArchiveEntry archiveEntry = new TarArchiveEntry(testFile);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test file content.".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedBytes = TestFiles.readAllBytes(testFile);
        byte[] actualBytes = IOUtils.toByteArray(new ByteArrayInputStream(outputStream.getCountData()));

        assertTrue(tarArchiveOutputStream.isFinished());
        assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test close method with empty archive.
     * @throws IOException on error
     */
    @Test
    public void testCloseEmptyArchive() throws IOException {
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    /**
     * Test close method with archive entry.
     * @throws IOException on error
     */
    @Test
    public void testCloseWithEntry() throws IOException {
        File testFile = TestFiles.createTestFile("testFile.txt", "Test file content.");
        ArchiveEntry archiveEntry = new TarArchiveEntry(testFile);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test file content.".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        assertTrue(tarArchiveOutputStream.isClosed());
    }

    /**
     * Test putArchiveEntry method with null archiveEntry.
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    /**
     * Test putArchiveEntry method with file as archiveEntry.
     * @throws IOException on error
     */
    @Test
    public void testPutArchiveEntryFile() throws IOException {
        File testFile = TestFiles.createTestFile("testFile.txt", "Test file content.");
        ArchiveEntry archiveEntry = new TarArchiveEntry(testFile);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        String expectedName = testFile.getName();
        String actualName = tarArchiveOutputStream.getCurrentName();
        assertEquals(expectedName, actualName);
    }

    /**
     * Test putArchiveEntry method with TarArchiveEntry as archiveEntry.
     * @throws IOException on error
     */
    @Test
    public void testPutArchiveEntryTarArchiveEntry() throws IOException {
        File testFile = TestFiles.createTestFile("testFile.txt", "Test file content.");
        ArchiveEntry archiveEntry = new TarArchiveEntry(testFile, "testFolder/testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        String expectedName = "testFolder/testFile.txt";
        String actualName = tarArchiveOutputStream.getCurrentName();
        assertEquals(expectedName, actualName);
    }

    /**
     * Test write method with empty buffer.
     * @throws IOException on error
     */
    @Test
    public void testWriteEmptyBuffer() throws IOException {
        byte[] buffer = new byte[0];
        tarArchiveOutputStream.write(buffer, 0, 0);
        assertEquals(0, outputStream.getCount());
    }

    /**
     * Test write method with non-empty buffer.
     * @throws IOException on error
     */
    @Test
    public void testWriteNonEmptyBuffer() throws IOException {
        byte[] buffer = "Test string".getBytes();
        tarArchiveOutputStream.write(buffer, 0, buffer.length);
        assertEquals(buffer.length, outputStream.getCount());
    }

    /**
     * Test write method with non-multiple of record size.
     * @throws IOException on error
     */
    @Test
    public void testWriteNonMultipleOfRecordSize() throws IOException {
        byte[] buffer = new byte[1025];
        tarArchiveOutputStream.write(buffer, 0, buffer.length);
        assertEquals(1024, outputStream.getCount());
    }

    /**
     * Test write method with multiple of record size.
     * @throws IOException on error
     */
    @Test
    public void testWriteMultipleOfRecordSize() throws IOException {
        byte[] buffer = new byte[2048];
        tarArchiveOutputStream.write(buffer, 0, 2048);
        assertEquals(2048, outputStream.getCount());
    }

    /**
     * Test write method with large buffer.
     * @throws IOException on error
     */
    @Test
    public void testWriteLargeBuffer() throws IOException {
        byte[] buffer = new byte[100_000];
        tarArchiveOutputStream.write(buffer, 0, 100_000);
        assertEquals(100_000, outputStream.getCount());
    }

    /**
     * Test closeArchiveEntry method with empty archive.
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCloseArchiveEntryEmptyArchive() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    /**
     * Test closeArchiveEntry method with archive entry.
     * @throws IOException on error
     */
    @Test
    public void testCloseArchiveEntryWithEntry() throws IOException {
        File testFile = TestFiles.createTestFile("testFile.txt", "Test file content.");
        ArchiveEntry archiveEntry = new TarArchiveEntry(testFile);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] buffer = "Test string".getBytes();
        tarArchiveOutputStream.write(buffer, 0, buffer.length);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(testFile.length() + buffer.length, outputStream.getCount());
    }

    /**
     * Test closeArchiveEntry method with unclosed entry.
     * @throws IOException on error
     */
    @Test(expected = IllegalStateException.class)
    public void testCloseArchiveEntryUnclosedEntry() throws IOException {
        File testFile = TestFiles.createTestFile("testFile.txt", "Test file content.");
        ArchiveEntry archiveEntry = new TarArchiveEntry(testFile);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.closeArchiveEntry();
    }

    /**
     * Test write method with unclosed entry.
     * @throws IOException on error
     */
    @Test(expected = IllegalStateException.class)
    public void testWriteUnclosedEntry() throws IOException {
        File testFile = TestFiles.createTestFile("testFile.txt", "Test file content.");
        ArchiveEntry archiveEntry = new TarArchiveEntry(testFile);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.write("Test string".getBytes());
    }

    /**
     * Test putArchiveEntry method with long file name.
     * @throws IOException on error
     */
    @Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        String longFileName = "longFileName/testFile.txt";
        File testFile = new File(longFileName);
        ArchiveEntry archiveEntry = new TarArchiveEntry(testFile);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test file content.".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedBytes = TestFiles.readAllBytes(testFile);
        byte[] actualBytes = IOUtils.toByteArray(new ByteArrayInputStream(outputStream.getCountData()));

        assertTrue(tarArchiveOutputStream.isFinished());
        assertArrayEquals(expectedBytes, actualBytes);
    }
}