import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testWriteSingleEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile.txt";
        File testFile = new File("src/test/resources/" + testFileName);
        String testFileContent = "Hello World!";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file
        cpioArchiveOutputStream.putArchiveEntry(entry);

        cpioArchiveOutputStream.write(testFileContent.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        byte[] cpioStreamContent = outputStream.toByteArray();

        // Checking the content of the CPIO stream
        String expectedCpioStream = getExpectedCpioStream("testSingleEntry.cpio");
        Assert.assertEquals(expectedCpioStream, new String(cpioStreamContent));

        // Checking the file extracted from the CPIO stream
        byte[] extractedFileContent = CpioUtils.readHeaderAndContent(cpioStreamContent, testFileName);
        Assert.assertEquals(testFileContent, new String(extractedFileContent));
    }

    @Test(expected = IOException.class)
    public void testWriteDuplicateEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile.txt";
        File testFile = new File("src/test/resources/" + testFileName);
        String testFileContent = "Hello World!";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file
        cpioArchiveOutputStream.putArchiveEntry(entry);

        cpioArchiveOutputStream.write(testFileContent.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        // This line should throw an exception
        cpioArchiveOutputStream.write(testFileContent.getBytes());

        cpioArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testWriteInvalidFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        String testFileName = "testFile.txt";
        File testFile = new File("src/test/resources/" + testFileName);
        String testFileContent = "Hello World!";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file
        cpioArchiveOutputStream.putArchiveEntry(entry);

        cpioArchiveOutputStream.write(testFileContent.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        // This line should throw an exception
        cpioArchiveOutputStream.finish((short) 10);
    }

    @Test(expected = IOException.class)
    public void testWriteWithoutPuttingNextEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile.txt";
        File testFile = new File("src/test/resources/" + testFileName);
        String testFileContent = "Hello World!";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        OutputStream output = cpioArchiveOutputStream.getOutputStream();
        output.write(testFileContent.getBytes());
        output.close();

        try {
            cpioArchiveOutputStream.finish();
        } finally {
            cpioArchiveOutputStream.close();
        }
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName1 = "testFile1.txt";
        File testFile1 = new File("src/test/resources/" + testFileName1);
        String testFileContent1 = "Hello World 1!";

        String testFileName2 = "testFile2.txt";
        File testFile2 = new File("src/test/resources/" + testFileName2);
        String testFileContent2 = "Hello World 2!";

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(testFileName1);
        entry1.setFileSize(testFileContent1.length());
        entry1.setMode(CpioConstants.C_ISREG); // regular file
        cpioArchiveOutputStream.putArchiveEntry(entry1);

        cpioArchiveOutputStream.write(testFileContent1.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(testFileName2);
        entry2.setFileSize(testFileContent2.length());
        entry2.setMode(CpioConstants.C_ISREG); // regular file
        cpioArchiveOutputStream.putArchiveEntry(entry2);

        cpioArchiveOutputStream.write(testFileContent2.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();

        byte[] cpioStreamContent = outputStream.toByteArray();

        String expectedCpioStream = getExpectedCpioStream("testMultipleEntries.cpio");
        Assert.assertEquals(expectedCpioStream, new String(cpioStreamContent));

        Map<String, byte[]> extractedFiles = extractFiles(cpioStreamContent);
        Assert.assertEquals(testFileContent1, new String(extractedFiles.get(testFileName1)));
        Assert.assertEquals(testFileContent2, new String(extractedFiles.get(testFileName2)));
    }

    private String getExpectedCpioStream(String testFileName) {
        return new String(getClass().getResourceAsStream("/expected/" + testFileName));
    }

    private Map<String, byte[]> extractFiles(byte[] cpioStreamContent) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cpioStreamContent);
        CpioArchiveInputStream cpioArchiveInputStream = new CpioArchiveInputStream(byteArrayInputStream);

        Map<String, byte[]> extractedFiles = new HashMap<>();
        while (cpioArchiveInputStream.hasNextEntry()) {
            CpioArchiveEntry entry = cpioArchiveInputStream.getNextEntry();
            byte[] fileContent = cpioArchiveInputStream.readAllBytes();
            extractedFiles.put(entry.getName(), fileContent);
        }

        cpioArchiveInputStream.close();
        return extractedFiles;
    }
}