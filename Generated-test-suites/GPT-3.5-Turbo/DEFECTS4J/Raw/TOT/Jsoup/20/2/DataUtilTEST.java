import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Mock
    private File file;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadWithFile() throws Exception {
        // Create a temporary file with test data
        String testData = "<html><body><h1>This is a test</h1></body></html>";
        Path tempFilePath = Files.createTempFile("temp", ".html");
        Files.write(tempFilePath, testData.getBytes());

        // Mock File object to return the temporary file path
        when(file.getAbsolutePath()).thenReturn(tempFilePath.toString());

        // Load the file using DataUtil.load method
        Document document = DataUtil.load(file, "UTF-8", "");

        assertNotNull(document);
        Element body = document.body();
        assertNotNull(body);
        Element h1 = body.select("h1").first();
        assertNotNull(h1);
        assertEquals("This is a test", h1.text());
    }

    @Test
    public void testLoadWithInputStream() throws Exception {
        // Create a test input stream with test data
        String testData = "<html><body><h1>This is a test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        // Load the input stream using DataUtil.load method
        Document document = DataUtil.load(inputStream, "UTF-8", "");

        assertNotNull(document);
        Element body = document.body();
        assertNotNull(body);
        Element h1 = body.select("h1").first();
        assertNotNull(h1);
        assertEquals("This is a test", h1.text());
    }

    @Test
    public void testLoadWithInputStreamAndParser() throws Exception {
        // Create a test input stream with test data
        String testData = "<html><body><h1>This is a test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        // Load the input stream using DataUtil.load method with custom parser
        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());

        assertNotNull(document);
        Element body = document.body();
        assertNotNull(body);
        Element h1 = body.select("h1").first();
        assertNotNull(h1);
        assertEquals("This is a test", h1.text());
    }

    @Test
    public void testParseByteDataWithCharset() {
        // Create a test byte data with test data
        String testData = "<html><body><h1>This is a test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        // Parse the byte data using DataUtil.parseByteData method
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());

        assertNotNull(document);
        Element body = document.body();
        assertNotNull(body);
        Element h1 = body.select("h1").first();
        assertNotNull(h1);
        assertEquals("This is a test", h1.text());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        // Create a test byte data with test data
        String testData = "<html><body><h1>This is a test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        // Parse the byte data using DataUtil.parseByteData method without specifying charset
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());

        assertNotNull(document);
        Element body = document.body();
        assertNotNull(body);
        Element h1 = body.select("h1").first();
        assertNotNull(h1);
        assertEquals("This is a test", h1.text());
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        // Create a temporary file with test data
        String testData = "<html><body><h1>This is a test</h1></body></html>";
        Path tempFilePath = Files.createTempFile("temp", ".html");
        Files.write(tempFilePath, testData.getBytes());

        // Read the file into ByteBuffer using DataUtil.readToByteBuffer method
        File tempFile = tempFilePath.toFile();
        FileInputStream fileInputStream = new FileInputStream(tempFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(fileInputStream);

        assertNotNull(byteData);
        byte[] bytes = new byte[byteData.remaining()];
        byteData.get(bytes);
        assertEquals(testData, new String(bytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }

}