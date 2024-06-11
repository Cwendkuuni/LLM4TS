import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * CpioArchiveOutputStreamTest
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testFileContent = "Test Content";

        File testFile = new File("testFile.txt");
        testFile.createNewFile();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFile, testFileName));
        cpioArchiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        byte[] expectedCpioStream = generateExpectedCpioStream(CpioConstants.FORMAT_NEW, testFileName, testFileContent.getBytes(StandardCharsets.UTF_8).length);
        Assert.assertArrayEquals("Unexpected Cpio stream output", expectedCpioStream, outputStream.toByteArray());

        testFile.delete();
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        String testFileName = "testFile";
        String testFileContent = "Test Content";

        File testFile = new File("testFile.txt");
        testFile.createNewFile();

        try {
            cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFile, testFileName));
            cpioArchiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
            cpioArchiveOutputStream.closeArchiveEntry();
            cpioArchiveOutputStream.finish();
            Assert.fail("Expected IllegalArgumentException with invalid format");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        testFile.delete();
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testFileContent = "Test Content";

        File testFile = new File("testFile.txt");
        testFile.createNewFile();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFile, testFileName));
        cpioArchiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
        cpioArchiveOutputStream.closeArchiveEntry();

        try {
            cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFile, testFileName));
            cpioArchiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
            cpioArchiveOutputStream.closeArchiveEntry();
            Assert.fail("Expected IOException with duplicate entry");
        } catch (IOException e) {
            // Expected exception
        }

        testFile.delete();
        outputStream.close();
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testFileContent = "Test Content";

        File testFile = new File("testFile.txt");
        testFile.createNewFile();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFile, testFileName));

        byte[] dataToWrite = testFileContent.getBytes(StandardCharsets.UTF_8);

        int partOfDataToWrite = dataToWrite.length / 2;

        cpioArchiveOutputStream.write(dataToWrite, 0, partOfDataToWrite);
        cpioArchiveOutputStream.write(dataToWrite, partOfDataToWrite, dataToWrite.length - partOfDataToWrite);

        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        byte[] expectedCpioStream = generateExpectedCpioStream(CpioConstants.FORMAT_NEW, testFileName, dataToWrite.length);
        Assert.assertArrayEquals("Unexpected Cpio stream output", expectedCpioStream, outputStream.toByteArray());

        testFile.delete();
        outputStream.close();
    }

    @Test
    public void testWriteWithInvalidFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        String testFileName = "testFile";
        String testFileContent = "Test Content";

        File testFile = new File("testFile.txt");
        testFile.createNewFile();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFile, testFileName));

        byte[] dataToWrite = testFileContent.getBytes(StandardCharsets.UTF_8);

        try {
            cpioArchiveOutputStream.write(dataToWrite);
            Assert.fail("Expected IllegalArgumentException with invalid format");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        testFile.delete();
        outputStream.close();
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testFileContent = "Test Content";

        File testFile = new File("testFile.txt");
        testFile.createNewFile();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFile, testFileName));

        byte[] dataToWrite = testFileContent.getBytes(StandardCharsets.UTF_8);

        try {
            cpioArchiveOutputStream.write(dataToWrite);
            Assert.fail("Expected IOException with unclosed entry");
        } catch (IOException e) {
            // Expected exception
        }

        testFile.delete();
        outputStream.close();
    }

    @Test
    public void testWriteWithUnclosedStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testFileContent = "Test Content";

        File testFile = new File("testFile.txt");
        testFile.createNewFile();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFile, testFileName));

        byte[] dataToWrite = testFileContent.getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.write(dataToWrite);
        cpioArchiveOutputStream.close();

        try {
            cpioArchiveOutputStream.write(dataToWrite);
            Assert.fail("Expected IOException with closed stream");
        } catch (IOException e) {
            // Expected exception
        }

        testFile.delete();
        outputStream.close();
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testFileContent = "Test Content";

        File testFile = new File("testFile.txt");
        testFile.createNewFile();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFile, testFileName));

        byte[] dataToWrite = testFileContent.getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.write(dataToWrite);

        try {
            cpioArchiveOutputStream.finish();
            Assert.fail("Expected IOException with unclosed entry");
        } catch (IOException e) {
            // Expected exception
        }

        testFile.delete();
        outputStream.close();
    }

    @Test
    public void testFinishWithClosedEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testFileContent = "Test Content";

        File testFile = new File("testFile.txt");
        testFile.createNewFile();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFile, testFileName));

        byte[] dataToWrite = testFileContent.getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.write(dataToWrite);
        cpioArchiveOutputStream.closeArchiveEntry();

        try {
            cpioArchiveOutputStream.finish();
            Assert.fail("Expected IOException with closed stream");
        } catch (IOException e) {
            // Expected exception
        }

        testFile.delete();
        outputStream.close();
    }

    @Test
    public void testFinishWithClosedStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testFileContent = "Test Content";

        File testFile = new File("testFile.txt");
        testFile.createNewFile();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(testFile, testFileName));

        byte[] dataToWrite = testFileContent.getBytes(StandardCharsets.UTF_8);

        cpioArchiveOutputStream.write(dataToWrite);
        cpioArchiveOutputStream.finish();

        try {
            cpioArchiveOutputStream.finish();
            Assert.fail("Expected IOException with closed stream");
        } catch (IOException e) {
            // Expected exception
        }

        testFile.delete();
        outputStream.close();
    }

    private byte[] generateExpectedCpioStream(short format, String fileName, long fileSize) {
        // Generate the expected Cpio stream based on the given format, fileName, and fileSize.
        // You can use the CpioConstants, ArchiveUtils and CpioArchiveEntry classes to help generate the expected bytes.
        // For simplicity, this method is left empty in this example.
        return new byte[0];
    }
}