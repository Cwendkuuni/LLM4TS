import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.compress.utils.CpioUtil.long2byteArray;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testConstructor_formatNew() throws IOException {
        // Test case for CpioArchiveOutputStream constructor with FORMAT_NEW format
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        Assert.assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getEntryFormat());
    }

    @Test
    public void testConstructor_formatNewCRC() throws IOException {
        // Test case for CpioArchiveOutputStream constructor with FORMAT_NEW_CRC format
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW_CRC);
        Assert.assertEquals(CpioConstants.FORMAT_NEW_CRC, cpioArchiveOutputStream.getEntryFormat());
    }

    @Test
    public void testConstructor_formatOldASCII() throws IOException {
        // Test case for CpioArchiveOutputStream constructor with FORMAT_OLD_ASCII format
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_ASCII);
        Assert.assertEquals(CpioConstants.FORMAT_OLD_ASCII, cpioArchiveOutputStream.getEntryFormat());
    }

    @Test
    public void testConstructor_formatOldBinary() throws IOException {
        // Test case for CpioArchiveOutputStream constructor with FORMAT_OLD_BINARY format
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_BINARY);
        Assert.assertEquals(CpioConstants.FORMAT_OLD_BINARY, cpioArchiveOutputStream.getEntryFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_unknownFormat() throws IOException {
        // Test case for IllegalArgumentException when passing an unknown format
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, 123);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Test case for putArchiveEntry method

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File inputFile = new File("test.txt");
        String entryName = "testFile.txt";
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.createArchiveEntry(inputFile, entryName);

        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] expectedHeader = ArchiveUtils.getTestCpioHeader(CpioConstants.FORMAT_NEW, inputFile, entryName, 1L);

        byte[] actualOutput = outputStream.toByteArray();
        byte[] expectedOutput = new byte[expectedHeader.length + inputFile.length()];
        System.arraycopy(expectedHeader, 0, expectedOutput, 0, expectedHeader.length);
        System.arraycopy(inputFile.getBytes(StandardCharsets.UTF_8), 0, expectedOutput, expectedHeader.length, inputFile.length());

        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_entryAlreadyExists() throws IOException {
        // Test case for IOException when trying to put a duplicate entry

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File inputFile = new File("test.txt");
        String entryName = "testFile.txt";
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.createArchiveEntry(inputFile, entryName);

        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testWrite() throws IOException {
        // Test case for write method

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File inputFile = new File("test.txt");
        String entryName = "testFile.txt";
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.createArchiveEntry(inputFile, entryName);

        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] inputData = "Test data".getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.write(inputData, 0, inputData.length);

        byte[] expectedOutput = new byte[inputData.length + expectedHeader.length];
        System.arraycopy(expectedHeader, 0, expectedOutput, 0, expectedHeader.length);
        System.arraycopy(inputData, 0, expectedOutput, expectedHeader.length, inputData.length);

        byte[] actualOutput = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        // Test case for closeArchiveEntry method

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File inputFile = new File("test.txt");
        String entryName = "testFile.txt";
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.createArchiveEntry(inputFile, entryName);

        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] inputData = "Test data".getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.write(inputData, 0, inputData.length);

        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedOutput = new byte[expectedHeader.length + inputData.length];
        System.arraycopy(expectedHeader, 0, expectedOutput, 0, expectedHeader.length);
        System.arraycopy(inputData, 0, expectedOutput, expectedHeader.length, inputData.length);

        byte[] actualOutput = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFinish() throws IOException {
        // Test case for finish method

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File inputFile = new File("test.txt");
        String entryName = "testFile.txt";
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.createArchiveEntry(inputFile, entryName);

        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] inputData = "Test data".getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.write(inputData, 0, inputData.length);

        cpioArchiveOutputStream.finish();

        byte[] expectedOutput = new byte[expectedHeaderTrailer.length + inputData.length];
        System.arraycopy(expectedHeaderTrailer, 0, expectedOutput, 0, expectedHeaderTrailer.length);
        System.arraycopy(inputData, 0, expectedOutput, expectedHeaderTrailer.length, inputData.length);

        byte[] actualOutput = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testClose() throws IOException {
        // Test case for close method

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File inputFile = new File("test.txt");
        String entryName = "testFile.txt";
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.createArchiveEntry(inputFile, entryName);

        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] inputData = "Test data".getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.write(inputData, 0, inputData.length);

        cpioArchiveOutputStream.finish();

        byte[] expectedOutput = new byte[expectedHeaderTrailer.length + inputData.length];
        System.arraycopy(expectedHeaderTrailer, 0, expectedOutput, 0, expectedHeaderTrailer.length);
        System.arraycopy(inputData, 0, expectedOutput, expectedHeaderTrailer.length, inputData.length);

        byte[] actualOutput = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedOutput, actualOutput);

        cpioArchiveOutputStream.close();

        byte[] expectedClosedOutput = new byte[expectedClosedHeaderTrailer.length];
        System.arraycopy(expectedClosedHeaderTrailer, 0, expectedClosedOutput, 0, expectedClosedHeaderTrailer.length);

        byte[] actualClosedOutput = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedClosedOutput, actualClosedOutput);
    }

    private static final byte[] expectedHeader = ArchiveUtils.getTestCpioHeader(CpioConstants.FORMAT_NEW, new File("test.txt"), "testFile.txt", 1L);
    private static final byte[] expectedHeaderTrailer = ArchiveUtils.getTestCpioTrailer(CpioConstants.FORMAT_NEW);
    private static final byte[] expectedClosedHeaderTrailer = ArchiveUtils.getTestCpioTrailer(CpioConstants.FORMAT_NEW, true);

}