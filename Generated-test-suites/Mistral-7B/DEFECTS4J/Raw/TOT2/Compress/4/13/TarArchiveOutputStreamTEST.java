import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/**
 * TarArchiveOutputStreamTest class contains JUnit tests for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private File outputFile;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputFile = File.createTempFile("junit-test-", null);
        tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile));
    }

    @After
    public void tearDown() throws Exception {
        if (tarArchiveOutputStream != null) {
            tarArchiveOutputStream.finish();
            tarArchiveOutputStream.close();
        }
        if (outputFile != null && outputFile.exists()) {
            Files.delete(Paths.get(outputFile.getAbsolutePath()));
        }
    }

    @Test
    public void testConstructorWithoutArguments() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertFalse(tarArchiveOutputStream instanceof TarArchiveOutputStream);
    }

    @Test
    public void testConstructorWithOutputStream() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile));
        assertTrue(tarArchiveOutputStream instanceof TarArchiveOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile), TarBuffer.DEFAULT_BLKSIZE);
        assertTrue(tarArchiveOutputStream instanceof TarArchiveOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeRecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        assertTrue(tarArchiveOutputStream instanceof TarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() {
        tarArchiveOutputStream.setLongFileMode(4);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String fileName = "testFile";
        String entryName = "testEntry";
        File inputFile = new File("testInputFile.txt");
        TarArchiveEntry testEntry = new TarArchiveEntry(inputFile, entryName);
        tarArchiveOutputStream.putArchiveEntry(testEntry);

        // Write some data to the test file
        FileWriter fileWriter = new FileWriter(inputFile);
        fileWriter.write("This is a test file.");
        fileWriter.close();

        // Check that the archive entry has been correctly added
        TarArchiveEntry readEntry = (TarArchiveEntry) tarArchiveOutputStream.getNextEntry();
        assertEquals(entryName, readEntry.getName());

        // Write some data to the output stream
        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);

        // Close the archive entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Check that the data has been written to the archive
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        int bytesRead;
        byte[] buffer = new byte[1024];
        while ((bytesRead = outputStream.read(buffer)) != -1) {
            byte[] readData = new byte[bytesRead];
            System.arraycopy(buffer, 0, readData, 0, bytesRead);
            TarArchiveEntry testReadEntry = (TarArchiveEntry) new TarArchiveInputStream(
                    new FileInputStream(outputFile)).getNextEntry();
            if (testReadEntry != null) {
                List<byte[]> fileContent = TarArchiveEntry.readAllBytes(testReadEntry.getInputStream());
                byte[] expectedData = new byte[data.length + fileContent.get(0).length];
                System.arraycopy(data, 0, expectedData, 0, data.length);
                System.arraycopy(fileContent.get(0), 0, expectedData, data.length, fileContent.get(0).length);
                assertArrayEquals(expectedData, readData);
            }
            break;
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidArchiveEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new Object());
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new FileInputStream(outputFile));

        // Verify that the archive is correctly closed
        TarArchiveEntry archiveEntry = tarArchiveInputStream.getNextEntry();
        assertNull(archiveEntry);
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new FileInputStream(outputFile));

        // Verify that the archive is correctly closed
        TarArchiveEntry archiveEntry = tarArchiveInputStream.getNextEntry();
        assertNull(archiveEntry);
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new FileInputStream(outputFile));

        // Read the data back from the archive
        byte[] readData = new byte[data.length];
        tarArchiveOutputStream.read(readData, 0, data.length);
        assertArrayEquals(data, readData);
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidData() throws IOException {
        byte[] data = new byte[1025];
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    @Test
    public void testWriteWithPartialData() throws IOException {
        byte[] data = new byte[1024];
        tarArchiveOutputStream.write(data, 0, data.length - 1);
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new FileInputStream(outputFile));

        // Read the data back from the archive
        byte[] readData = new byte[data.length - 1];
        tarArchiveOutputStream.read(readData, 0, data.length - 1);
        assertArrayEquals(new byte[data.length - 1], readData);
    }
}