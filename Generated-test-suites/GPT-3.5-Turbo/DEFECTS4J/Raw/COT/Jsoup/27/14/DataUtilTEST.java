
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        // Test data
        File testFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        // Create test file content
        String testContent = "<html><head></head><body><h1>Test</h1></body></html>";
        PrintWriter writer = new PrintWriter(testFile, charsetName);
        writer.write(testContent);
        writer.close();

        // Call method under test
        Document doc = DataUtil.load(testFile, charsetName, baseUri);

        // Verify results
        String expectedTitle = "Test";
        String actualTitle = doc.title();
        Assert.assertEquals(expectedTitle, actualTitle);

        // Clean up
        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Test data
        String testContent = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        // Call method under test
        Document doc = DataUtil.load(testInputStream, charsetName, baseUri);

        // Verify results
        String expectedTitle = "Test";
        String actualTitle = doc.title();
        Assert.assertEquals(expectedTitle, actualTitle);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Test data
        String testContent = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        // Call method under test
        Document doc = DataUtil.load(testInputStream, charsetName, baseUri, parser);

        // Verify results
        String expectedTitle = "Test";
        String actualTitle = doc.title();
        Assert.assertEquals(expectedTitle, actualTitle);
    }

    @Test
    public void testParseByteData() {
        // Test data
        String testContent = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        // Call method under test
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Verify results
        String expectedTitle = "Test";
        String actualTitle = doc.title();
        Assert.assertEquals(expectedTitle, actualTitle);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test data
        String testContent = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testContent.getBytes());

        // Call method under test
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);

        // Verify results
        byte[] expectedBytes = testContent.getBytes();
        byte[] actualBytes = byteData.array();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test data
        String contentType1 = "text/html; charset=utf-8";
        String contentType2 = "text/html; charset=EUC-JP";
        String contentType3 = "text/html;charset=gb2312";
        String contentType4 = "text/html; charset=";
        String contentType5 = "text/html";

        // Call method under test
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);
        String charset4 = DataUtil.getCharsetFromContentType(contentType4);
        String charset5 = DataUtil.getCharsetFromContentType(contentType5);

        // Verify results
        String expectedCharset1 = "UTF-8";
        String expectedCharset2 = "EUC-JP";
        String expectedCharset3 = "GB2312";
        String expectedCharset4 = null;
        String expectedCharset5 = null;
        Assert.assertEquals(expectedCharset1, charset1);
        Assert.assertEquals(expectedCharset2, charset2);
        Assert.assertEquals(expectedCharset3, charset3);
        Assert.assertEquals(expectedCharset4, charset4);
        Assert.assertEquals(expectedCharset5, charset5);
    }
}
