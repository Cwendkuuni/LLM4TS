import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.utils.ArchiveUtils;
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
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputFile = new File("test.cpio");
        outputStream = new FileOutputStream(outputFile);
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
            outputFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntry_WithValidEntry_WritesEntryToOutputStream() throws IOException {
        // Given
        String entryName = "testfile";
        File inputFile = new File("testInputFile.txt");
        byte[] inputFileContents = "Hello World".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry(inputFile, entryName);

        // When
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write("Hello World".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        // Then
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        File testOutputFile = new File("testOutputFile.cpio");
        OutputStream testOutputStreamWithCpio = new FileOutputStream(testOutputFile);
        CpioArchiveOutputStream testCpioArchiveOutputStream = new CpioArchiveOutputStream(testOutputStreamWithCpio, CpioConstants.FORMAT_NEW);
        ArchiveEntry testEntry = cpioArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        testCpioArchiveOutputStream.putArchiveEntry(testEntry);
        testCpioArchiveOutputStream.write(inputFileContents);
        testCpioArchiveOutputStream.closeArchiveEntry();
        testCpioArchiveOutputStream.finish();
        testCpioArchiveOutputStream.close();

        byte[] expectedOutput = ArchiveUtils.toByteArray(outputFile);
        byte[] testOutput = testOutputStream.toByteArray();

        assertEquals(expectedOutput.length, testOutput.length);
        for (int i = 0; i < expectedOutput.length; i++) {
            assertEquals(expectedOutput[i], testOutput[i]);
        }
    }

    @Test
    public void testPutArchiveEntry_WithDuplicateEntry_ThrowsException() throws IOException {
        // Given
        String entryName = "testfile";
        File inputFile = new File("testInputFile.txt");
        CpioArchiveEntry entry = new CpioArchiveEntry(inputFile, entryName);

        // When
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        // Then
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testPutArchiveEntry_WithInvalidFormat_ThrowsException() throws IOException {
        // Given
        String entryName = "testfile";
        File inputFile = new File("testInputFile.txt");
        CpioArchiveEntry entry = new CpioArchiveEntry(inputFile, entryName);

        // When
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 123);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            cpioArchiveOutputStream.putArchiveEntry(entry);
        });

        // Clean up
        cpioArchiveOutputStream.close();
    }

    @Test
    public void testWrite_WritesDataToCpioEntry() throws IOException {
        // Given
        String entryName = "testfile";
        File inputFile = new File("testInputFile.txt");
        CpioArchiveEntry entry = new CpioArchiveEntry(inputFile, entryName);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        // When
        byte[] data = "Data to be written".getBytes();
        cpioArchiveOutputStream.write(data);

        // Then
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        File testOutputFile = new File("testOutputFile.cpio");
        OutputStream testOutputStreamWithCpio = new FileOutputStream(testOutputFile);
        CpioArchiveOutputStream testCpioArchiveOutputStream = new CpioArchiveOutputStream(testOutputStreamWithCpio, CpioConstants.FORMAT_NEW);
        ArchiveEntry testEntry = cpioArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        testCpioArchiveOutputStream.putArchiveEntry(testEntry);
        testCpioArchiveOutputStream.write(data);
        testCpioArchiveOutputStream.closeArchiveEntry();
        testCpioArchiveOutputStream.finish();
        testCpioArchiveOutputStream.close();

        byte[] expectedOutput = ArchiveUtils.toByteArray(outputFile);
        byte[] testOutput = testOutputStream.toByteArray();

        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], testOutput[i + entry.getHeaderSize()]);
        }
        assertEquals(data.length + entry.getHeaderSize(), testOutput.length);
    }

    @Test
    public void testCloseArchiveEntry_WhenDataWrittenToEntry_ClosesEntry() throws IOException {
        // Given
        String entryName = "testfile";
        File inputFile = new File("testInputFile.txt");
        CpioArchiveEntry entry = new CpioArchiveEntry(inputFile, entryName);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Data to be written".getBytes();
        cpioArchiveOutputStream.write(data);

        // When
        cpioArchiveOutputStream.closeArchiveEntry();

        // Then
        assertNull(cpioArchiveOutputStream.getEntry());
    }

    @Test
    public void testCloseArchiveEntry_WhenNoDataWrittenToEntry_DoesNotCloseEntry() throws IOException {
        // Given
        String entryName = "testfile";
        File inputFile = new File("testInputFile.txt");
        CpioArchiveEntry entry = new CpioArchiveEntry(inputFile, entryName);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        // When
        cpioArchiveOutputStream.closeArchiveEntry();

        // Then
        assertEquals(entry, cpioArchiveOutputStream.getEntry());
    }

    @Test
    public void testFinish_WhenAllEntriesClosed_WritesTrailerToOutputStream() throws IOException {
        // Given
        String entryName1 = "testfile1";
        String entryName2 = "testfile2";
        File inputFile1 = new File("testInputFile1.txt");
        File inputFile2 = new File("testInputFile2.txt");
        CpioArchiveEntry entry1 = new CpioArchiveEntry(inputFile1, entryName1);
        CpioArchiveEntry entry2 = new CpioArchiveEntry(inputFile2, entryName2);

        // When
        cpioArchiveOutputStream.putArchiveEntry(entry1);
        cpioArchiveOutputStream.write(new byte[0]);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.putArchiveEntry(entry2);
        cpioArchiveOutputStream.write(new byte[0]);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        // Then
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        File testOutputFile = new File("testOutputFile.cpio");
        OutputStream testOutputStreamWithCpio = new FileOutputStream(testOutputFile);
        CpioArchiveOutputStream testCpioArchiveOutputStream = new CpioArchiveOutputStream(testOutputStreamWithCpio, CpioConstants.FORMAT_NEW);
        ArchiveEntry testEntry1 = cpioArchiveOutputStream.createArchiveEntry(inputFile1, entryName1);
        ArchiveEntry testEntry2 = cpioArchiveOutputStream.createArchiveEntry(inputFile2, entryName2);
        testCpioArchiveOutputStream.putArchiveEntry(testEntry1);
        testCpioArchiveOutputStream.write(new byte[0]);
        testCpioArchiveOutputStream.closeArchiveEntry();
        testCpioArchiveOutputStream.putArchiveEntry(testEntry2);
        testCpioArchiveOutputStream.write(new byte[0]);
        testCpioArchiveOutputStream.closeArchiveEntry();
        testCpioArchiveOutputStream.finish();
        testCpioArchiveOutputStream.close();

        byte[] expectedOutput = ArchiveUtils.toByteArray(outputFile);
        byte[] testOutput = testOutputStream.toByteArray();

        assertEquals(expectedOutput.length, testOutput.length);
        for (int i = 0; i < expectedOutput.length; i++) {
            assertEquals(expectedOutput[i], testOutput[i]);
        }
    }
}