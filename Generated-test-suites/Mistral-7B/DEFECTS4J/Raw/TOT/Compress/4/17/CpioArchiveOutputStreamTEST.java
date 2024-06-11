import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private File testFile;
    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws IOException {
        testFile = new File("output.cpio");
        outputStream = new FileOutputStream(testFile);
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioOutputStream != null) {
            cpioOutputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Put a new entry
        String fileContent = "Test data";
        byte[] fileBytes = fileContent.getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        entry.setSize(fileBytes.length);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(fileBytes, 0, fileBytes.length);
        cpioOutputStream.closeArchiveEntry();

        // Verify the written data is correct
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] fileData = new byte[testFile.length()];
        FileInputStream inputStream = new FileInputStream(testFile);
        int read = inputStream.read(fileData, 0, fileData.length);
        inputStream.close();
        buffer.write(fileData, 0, read);
        byte[] expectedData = buffer.toByteArray();
        byte[] readData = cpioOutputStream.getNextEntry().getInputStream().readAllBytes();
        Assert.assertArrayEquals(expectedData, readData);

        // Verify the written file header is correct
        String header = new String(CpioArchiveOutputStreamTest.readFileAsBytes(testFile), "UTF-8");
        Assert.assertTrue(header.contains("7163616d 65786167 73207072 6f637365 72766572")); // MAGIC_NEW
        Assert.assertTrue(header.contains("00000000 01006464 6f6d696e 696f6e61 6d657373 00000000 00000000 00000000")); // Entry format, mode, uid, gid, number of links, time, size, device maj and min
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithWrongFormat() throws IOException {
        cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        String fileContent = "Test data";
        byte[] fileBytes = fileContent.getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        entry.setSize(fileBytes.length);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(fileBytes, 0, fileBytes.length);
        cpioOutputStream.closeArchiveEntry();

        // Try to put another entry with the same name
        entry = new CpioArchiveEntry("test.txt");
        cpioOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidSize() throws IOException {
        String fileContent = "Test data";
        byte[] fileBytes = fileContent.getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        entry.setSize(-1);
        cpioOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNegativeSize() throws IOException {
        String fileContent = "Test data";
        byte[] fileBytes = fileContent.getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        entry.setSize(-fileBytes.length);
        cpioOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidLength() throws IOException {
        String fileContent = "Test data";
        byte[] fileBytes = fileContent.getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(fileBytes, 0, fileBytes.length + 1);
    }

    @Test
    public void testFinish() throws IOException {
        String fileContent = "Test data";
        byte[] fileBytes = fileContent.getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        entry.setSize(fileBytes.length);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(fileBytes, 0, fileBytes.length);
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();

        // Verify there's no more entry in the stream
        Assert.assertNull(cpioOutputStream.getNextEntry());

        // Verify the trailer is written
        ByteArrayOutputStream trailerBuffer = new ByteArrayOutputStream();
        byte[] trailerData = cpioOutputStream.getOutputStream().readAllBytes();
        trailerBuffer.write(trailerData);
        String trailerHeader = new String(trailerBuffer.toByteArray(), "UTF-8");
        Assert.assertTrue(trailerHeader.contains("TRAILER"));
    }

    private static byte[] readFileAsBytes(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[file.length()];
        inputStream.read(buffer, 0, buffer.length);
        inputStream.close();
        return buffer;
    }
}