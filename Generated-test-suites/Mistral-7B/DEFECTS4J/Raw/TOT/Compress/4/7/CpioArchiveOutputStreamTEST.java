import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "test.cpio";
    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private File testFile;

    @Before
    public void setUp() throws IOException {
        testFile = new File(TEST_FILE_NAME);
        outputStream = new FileOutputStream(testFile);
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testWritingSingleEntryWithValidValues() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Test content.";

        ArchiveEntry testEntry = new CpioArchiveEntry(testFileName);
        testEntry.setSize(testFileContent.length());
        testEntry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();

        File testFileOutput = new File(testFile.getAbsolutePath() + "/" + testFileName);
        String outputContent = new String(Files.readAllBytes(Paths.get(testFileOutput.getAbsolutePath())), StandardCharsets.UTF_8);
        assertEquals(testFileContent, outputContent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWritingEntryWithInvalidFormat() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Test content.";

        ArchiveEntry testEntry = new CpioArchiveEntry(testFileName);
        testEntry.setSize(testFileContent.length());
        testEntry.setFormat((short) -1);

        cpioArchiveOutputStream.putArchiveEntry(testEntry);
    }

    @Test(expected = IOException.class)
    public void testWritingTwoEntriesWithOverlappingSizes() throws IOException {
        String testFileName1 = "testFile1.txt";
        String testFileContent1 = "Test content 1.";
        String testFileName2 = "testFile2.txt";
        String testFileContent2 = "Test content 2.";

        ArchiveEntry testEntry1 = new CpioArchiveEntry(testFileName1);
        testEntry1.setSize(testFileContent1.length());
        testEntry1.setFormat(CpioConstants.FORMAT_NEW);

        ArchiveEntry testEntry2 = new CpioArchiveEntry(testFileName2);
        testEntry2.setSize(testFileContent1.length());
        testEntry2.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(testEntry1);
        cpioArchiveOutputStream.write(testFileContent1.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putArchiveEntry(testEntry2);
        // This line should throw an IOException
        cpioArchiveOutputStream.write(testFileContent2.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testWritingMultipleEntriesWithValidValues() throws IOException {
        String testFileName1 = "testFile1.txt";
        String testFileContent1 = "Test content 1.";
        String testFileName2 = "testFile2.txt";
        String testFileContent2 = "Test content 2.";

        ArchiveEntry testEntry1 = new CpioArchiveEntry(testFileName1);
        testEntry1.setSize(testFileContent1.length());
        testEntry1.setFormat(CpioConstants.FORMAT_NEW);

        ArchiveEntry testEntry2 = new CpioArchiveEntry(testFileName2);
        testEntry2.setSize(testFileContent2.length());
        testEntry2.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(testEntry1);
        cpioArchiveOutputStream.write(testFileContent1.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putArchiveEntry(testEntry2);
        cpioArchiveOutputStream.write(testFileContent2.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();

        File testFileOutput = new File(testFile.getAbsolutePath());
        String outputContent1 = new String(Files.readAllBytes(Paths.get(testFileOutput.getAbsolutePath() + "/" + testFileName1)), StandardCharsets.UTF_8);
        String outputContent2 = new String(Files.readAllBytes(Paths.get(testFileOutput.getAbsolutePath() + "/" + testFileName2)), StandardCharsets.UTF_8);

        assertEquals(testFileContent1, outputContent1);
        assertEquals(testFileContent2, outputContent2);
    }

    @Test
    public void testWritingEntryWithEmptyName() throws IOException {
        ArchiveEntry testEntry = new CpioArchiveEntry("");
        testEntry.setSize(10);
        testEntry.setFormat(CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(new byte[10]);
        cpioArchiveOutputStream.closeArchiveEntry();

        File testFileOutput = new File(testFile.getAbsolutePath());
        byte[] outputContent = Files.readAllBytes(Paths.get(testFileOutput.getAbsolutePath()));

        byte[] expectedContent = new byte[11]; // Including null byte at the end
        expectedContent[0] = 0; // null byte for empty name

        assertArrayEquals(expectedContent, outputContent);
    }

    @Test
    public void testWritingNewEntryWithFormatOldBinary() throws IOException {
        String testFileName = "testFile.dat";
        byte[] testFileContent = new byte[] {1, 2, 3};

        ArchiveEntry testEntry = new CpioArchiveEntry(testFileName);
        testEntry.setSize(testFileContent.length);
        testEntry.setFormat((short) CpioConstants.FORMAT_OLD_BINARY);

        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testFileContent);
        cpioArchiveOutputStream.closeArchiveEntry();

        File testFileOutput = new File(testFile.getAbsolutePath() + "/" + testFileName);
        byte[] outputContent = Files.readAllBytes(Paths.get(testFileOutput.getAbsolutePath()));

        assertArrayEquals(testFileContent, outputContent);
    }

    @Test(expected = IOException.class)
    public void testWritingNewEntryWithFormatOldBinaryAndInvalidSwapHalfWord() throws IOException {
        String testFileName = "testFile.dat";
        byte[] testFileContent = new byte[] {1, 2, 3};

        ArchiveEntry testEntry = new CpioArchiveEntry(testFileName);
        testEntry.setSize(testFileContent.length);
        testEntry.setFormat((short) CpioConstants.FORMAT_OLD_BINARY);

        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testFileContent);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();
    }

    @Test
    public void testWritingNewEntryWithFormatOldBinaryAndValidSwapHalfWord() throws IOException {
        String testFileName = "testFile.dat";
        byte[] testFileContent = new byte[] {1, 2, 3};

        ArchiveEntry testEntry = new CpioArchiveEntry(testFileName);
        testEntry.setSize(testFileContent.length);
        testEntry.setFormat((short) CpioConstants.FORMAT_OLD_BINARY);

        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testFileContent);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.ensureOpen();
        cpioArchiveOutputStream.finish();

        File testFileOutput = new File(testFile.getAbsolutePath() + "/" + testFileName);
        byte[] outputContent = Files.readAllBytes(Paths.get(testFileOutput.getAbsolutePath()));

        byte[] expectedContent = CpioUtil.reverseBytes(testFileContent);

        assertArrayEquals(expectedContent, outputContent);
    }

    @Test
    public void testWritingNewEntryWithFormatNewCrc() throws IOException {
        String testFileName = "testFile.crc";
        String testFileContent = "Test content.";

        ArchiveEntry testEntry = new CpioArchiveEntry(testFileName);
        testEntry.setSize(testFileContent.length());
        testEntry.setFormat((short) CpioConstants.FORMAT_NEW_CRC);

        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();

        File testFileOutput = new File(testFile.getAbsolutePath() + "/" + testFileName);
        String outputContent = new String(Files.readAllBytes(Paths.get(testFileOutput.getAbsolutePath())), StandardCharsets.UTF_8);

        assertEquals(testFileContent, outputContent);

        cpioArchiveOutputStream.close();
    }

    @Test
    public void testWritingNewEntryWithFormatNewCrcAndInvalidChksum() throws IOException {
        String testFileName = "testFile.crc";
        String testFileContent = "Test content.";

        ArchiveEntry testEntry = new CpioArchiveEntry(testFileName);
        testEntry.setSize(testFileContent.length());
        testEntry.setFormat((short) CpioConstants.FORMAT_NEW_CRC);

        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedChecksum = CpioUtil.calculateChecksum(testFileContent);

        cpioArchiveOutputStream.ensureOpen();
        cpioArchiveOutputStream.write(new byte[] {1, 2, 3}); // Invalid checksum
        cpioArchiveOutputStream.finish();

        File testFileOutput = new File(testFile.getAbsolutePath() + "/" + testFileName);
        byte[] outputContent = Files.readAllBytes(Paths.get(testFileOutput.getAbsolutePath()));

        assertArrayNotEquals(expectedChecksum, outputContent);
    }

    @Test
    public void testWritingNewEntryWithFormatNewCrcAndValidChksum() throws IOException {
        String testFileName = "testFile.crc";
        String testFileContent = "Test content.";

        ArchiveEntry testEntry = new CpioArchiveEntry(testFileName);
        testEntry.setSize(testFileContent.length());
        testEntry.setFormat((short) CpioConstants.FORMAT_NEW_CRC);

        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedChecksum = CpioUtil.calculateChecksum(testFileContent);

        cpioArchiveOutputStream.ensureOpen();
        cpioArchiveOutputStream.write(expectedChecksum);
        cpioArchiveOutputStream.finish();

        File testFileOutput = new File(testFile.getAbsolutePath() + "/" + testFileName);
        byte[] outputContent = Files.readAllBytes(Paths.get(testFileOutput.getAbsolutePath()));

        assertArrayEquals(expectedChecksum, outputContent);

        cpioArchiveOutputStream.close();
    }

    @After
    public void tearDown() throws IOException {
        if (testFile.exists()) {
            testFile.delete();
        }
    }
}