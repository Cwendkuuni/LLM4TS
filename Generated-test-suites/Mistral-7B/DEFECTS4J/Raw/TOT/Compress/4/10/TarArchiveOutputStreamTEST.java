import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TarArchiveOutputStreamTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private TarOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws Exception {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @After
    public void tearDown() throws IOException {
        outputStream.finish();
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        File testFile = new File("testFile.txt");
        String entryName = "testFile.txt";
        TarArchiveEntry testEntry = new TarArchiveEntry(testFile, entryName);
        outputStream.putArchiveEntry(testEntry);
        byte[] expectedHeader = Arrays.copyOfRange(testEntry.getHeader(), 0, TarConstants.STD_REC_SIZE);
        assertEquals("Unexpected tar header", expectedHeader, outputStream.getBuffer().getData(), 0, expectedHeader.length);
        testFile.delete();
    }

    @Test
    public void testWrite() throws IOException {
        String testString = "Hello World!";
        byte[] testData = testString.getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry testEntry = new TarArchiveEntry(new File("testFile.txt"), "testFile.txt");
        outputStream.putArchiveEntry(testEntry);
        outputStream.write(testData, 0, testData.length);
        outputStream.closeArchiveEntry();
        byte[] expectedData = Arrays.copyOfRange(testData, 0, testData.length);
        byte[] actualData = Arrays.copyOfRange(outputStream.getBuffer().getData(), 0, testData.length);
        assertEquals("Unexpected test data", expectedData, actualData);
    }

    @Test
    public void testAssembleRecords() throws IOException {
        String testString = "Hello World!";
        byte[] testData = testString.getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry testEntry = new TarArchiveEntry(new File("testFile.txt"), "testFile.txt");
        outputStream.putArchiveEntry(testEntry);
        outputStream.write(testData, 0, testData.length);
        outputStream.finish();
        outputStream.close();
        byte[] expectedData = Arrays.copyOfRange(testData, 0, testData.length);
        byte[] actualData = new ByteArrayInputStream(byteArrayOutputStream.toByteArray()).readAllBytes();
        byte[] expectedHeader = Arrays.copyOfRange(testEntry.getHeader(), 0, TarConstants.STD_REC_SIZE);
        byte[] actualHeader = Arrays.copyOfRange(actualData, 0, TarConstants.STD_REC_SIZE);
        assertEquals("Unexpected tar header", expectedHeader, actualHeader, 0, expectedHeader.length);
        assertEquals("Unexpected test data", expectedData, Arrays.copyOfRange(actualData, TarConstants.STD_REC_SIZE, actualData.length), 0, expectedData.length);
    }

    @Test(expected = RuntimeException.class)
    public void testLongFilePath() throws IOException {
        String longFilePath = "/path/to/long/file/name/that/exceeds/NAMELEN.txt";
        TarArchiveEntry testEntry = new TarArchiveEntry(new File(longFilePath), "longFilePath.txt");
        outputStream.putArchiveEntry(testEntry);
        exception.expect(RuntimeException.class);
        exception.expectMessage("file name 'longFilePath.txt' is too long ( > " + TarConstants.NAMELEN + " bytes)");
        outputStream.finish();
        outputStream.close();
    }

    @Test
    public void testCloseArchiveEntryBeforeWrite() throws IOException {
        String testString = "Hello World!";
        byte[] testData = testString.getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry testEntry = new TarArchiveEntry(new File("testFile.txt"), "testFile.txt");
        outputStream.putArchiveEntry(testEntry);
        outputStream.closeArchiveEntry();
        exception.expect(IOException.class);
        exception.expectMessage("This archives contains unclosed entries.");
        outputStream.write(testData, 0, testData.length);
    }
}