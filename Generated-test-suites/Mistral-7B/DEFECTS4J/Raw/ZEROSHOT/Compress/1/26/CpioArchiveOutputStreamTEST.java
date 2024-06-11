import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Test class for {@link CpioArchiveOutputStream} class.
 */
public class CpioArchiveOutputStreamTest {

    /**
     * Tests constructor with valid format.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testConstructorValidFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        Assert.assertNull(cpioArchiveOutputStream.getCpioEntry());
        Assert.assertFalse(cpioArchiveOutputStream.isClosed());
        Assert.assertFalse(cpioArchiveOutputStream.isFinished());
        Assert.assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());
        Assert.assertNull(cpioArchiveOutputStream.getNames());
        Assert.assertEquals(0L, cpioArchiveOutputStream.getCrc());
        Assert.assertEquals(0L, cpioArchiveOutputStream.getWritten());
    }

    /**
     * Tests constructor with invalid format.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 0xFF);
    }

    /**
     * Tests writing a new entry.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testWriteNewEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile.txt";
        String testContent = "Hello World!";
        byte[] testContentBytes = testContent.getBytes(StandardCharsets.UTF_8);

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(testFileName);
        cpioArchiveEntry.setSize(testContentBytes.length);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.write(testContentBytes, 0, testContentBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedOutput = createExpectedOutput("testNewEntry.bin");
        Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    /**
     * Tests writing an old ASCII entry.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testWriteOldAsciiEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_ASCII);

        String testFileName = "testFile.txt";
        String testContent = "Hello World!";
        byte[] testContentBytes = testContent.getBytes(StandardCharsets.UTF_8);

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(testFileName);
        cpioArchiveEntry.setSize(testContentBytes.length);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.write(testContentBytes, 0, testContentBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedOutput = createExpectedOutput("testOldAsciiEntry.bin");
        Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    /**
     * Tests writing an old binary entry.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testWriteOldBinaryEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_BINARY);

        String testFileName = "testFile.txt";
        String testContent = "Hello World!";
        byte[] testContentBytes = testContent.getBytes(StandardCharsets.UTF_8);

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(testFileName);
        cpioArchiveEntry.setSize(testContentBytes.length);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.write(testContentBytes, 0, testContentBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedOutput = createExpectedOutput("testOldBinaryEntry.bin");
        Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    /**
     * Tests writing a new entry with CRC.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testWriteNewEntryWithCrc() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW_CRC);

        String testFileName = "testFile.txt";
        String testContent = "Hello World!";
        byte[] testContentBytes = testContent.getBytes(StandardCharsets.UTF_8);

        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(testFileName);
        cpioArchiveEntry.setSize(testContentBytes.length);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.write(testContentBytes, 0, testContentBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedOutput = createExpectedOutput("testNewEntryWithCrc.bin");
        Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    /**
     * Tests writing multiple entries.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testWriteMultipleEntries() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName1 = "testFile1.txt";
        String testContent1 = "Hello World 1!";
        byte[] testContentBytes1 = testContent1.getBytes(StandardCharsets.UTF_8);

        String testFileName2 = "testFile2.txt";
        String testContent2 = "Hello World 2!";
        byte[] testContentBytes2 = testContent2.getBytes(StandardCharsets.UTF_8);

        CpioArchiveEntry cpioArchiveEntry1 = new CpioArchiveEntry();
        cpioArchiveEntry1.setName(testFileName1);
        cpioArchiveEntry1.setSize(testContentBytes1.length);

        CpioArchiveEntry cpioArchiveEntry2 = new CpioArchiveEntry();
        cpioArchiveEntry2.setName(testFileName2);
        cpioArchiveEntry2.setSize(testContentBytes2.length);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry1);
        cpioArchiveOutputStream.write(testContentBytes1, 0, testContentBytes1.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry2);
        cpioArchiveOutputStream.write(testContentBytes2, 0, testContentBytes2.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();

        byte[] expectedOutput = createExpectedOutput("testMultipleEntries.bin");
        Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    /**
     * Tests writing a CPIO trailer.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testWriteTrailer() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.finish();

        byte[] expectedOutput = createExpectedOutput("testTrailer.bin");
        Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    /**
     * Creates an expected output byte array for a given test case.
     *
     * @param fileName the name of the test file.
     * @return the expected output byte array.
     */
    private byte[] createExpectedOutput(String fileName) {
        return this.getClass().getResourceAsStream(fileName).readAllBytes();
    }
}