import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadFile() throws IOException {
        String testFileContent = "Test File Content";
        File testFile = new File("testFile.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream writer = outputStream;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Given
        writeFileContentToFile(testFile, testFileContent);

        // When
        Document doc = DataUtil.load(testFile, charsetName, baseUri);

        // Then
        assertEquals("Loaded file content should match", testFileContent, doc.text());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testInputStreamContent = "Test InputStream Content";
        InputStream inputStream = new ByteArrayInputStream(testInputStreamContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Given

        // When
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        // Then
        assertEquals("Loaded input stream content should match", testInputStreamContent, doc.text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testInputStreamContent = "Test Input Stream Content";
        InputStream inputStream = new ByteArrayInputStream(testInputStreamContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = DataUtil.parser().xml();

        // Given

        // When
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Then
        assertEquals("Loaded input stream content should match", testInputStreamContent, doc.text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputStreamContent = "Test Input Stream Content";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        // Given

        // When
        DataUtil.crossStreams(inputStream, outputStream);

        // Then
        InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String testOutputStreamContent = new String(testInputStream.readAllBytes(), StandardCharsets.UTF_8);
        assertEquals("Cross streams should have copied the input stream content to the output stream", inputStreamContent, testOutputStreamContent);
    }

    @Test
    public void testParseByteData() throws IOException {
        String testFileContent = "Test File Content";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testFileContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Given

        // When
        Document doc = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, DataUtil.parser().htmlParser());

        // Then
        assertEquals("Parsed byte data content should match", testFileContent, doc.text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testInputStreamContent = "Test Input Stream Content";
        InputStream inputStream = new ByteArrayInputStream(testInputStreamContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Then
        byte[] expectedByteArray = testInputStreamContent.getBytes(StandardCharsets.UTF_8);
        byte[] actualByteArray = byteBuffer.array();
        assertArrayEquals("Read to byte buffer should have the same content as the input stream", expectedByteArray, actualByteArray);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String testFileContent = "Test File Content";
        File testFile = new File("testFile.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);

        // Then
        String fileContent = new String(testFile.readAllBytes(), StandardCharsets.UTF_8);
        assertEquals("Read file to byte buffer should have the same content as the file", fileContent, new String(byteBuffer.array(), StandardCharsets.UTF_8));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        // Then
        assertEquals("Empty byte buffer should have a limit of 0", 0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        // When
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Then
        assertEquals("Get charset from content type should return UTF-8", "UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        // When
        String mimeBoundary = DataUtil.mimeBoundary();

        // Then
        assertTrue("Mime boundary should have a length of at least 32", mimeBoundary.length() >= 32);
    }

    private void writeFileContentToFile(File file, String content) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}