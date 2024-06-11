import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static Document doc;
    private static Parser parser;

    @BeforeClass
    public static void setup() {
        parser = DataUtil.Parser.htmlParser();
    }

    // Test method for load(File, String, String)
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "";
        doc = DataUtil.load(file, charsetName, baseUri);
        assertEquals("Test HTML content", doc.body().text(), "Test HTML content");
    }

    // Test method for load(InputStream, String, String)
    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "Test HTML content";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlContent.getBytes(Charset.forName("UTF-8")));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "";
        doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("Test HTML content", doc.body().text(), "Test HTML content");
    }

    // Test method for load(InputStream, String, String, Parser)
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "Test HTML content";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlContent.getBytes(Charset.forName("UTF-8")));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertEquals("Test HTML content", doc.body().text(), "Test HTML content");
    }

    // Test method for parseByteData(ByteBuffer, String, String, Parser) with charsetName null
    @Test
    public void testParseByteDataNullCharsetName() {
        ByteBuffer byteData = ByteBuffer.wrap("Test HTML content".getBytes(Charset.forName("UTF-8")));
        assertThrows(IOException.class, () -> DataUtil.parseByteData(byteData, null, "", parser));
    }

    // Test method for parseByteData(ByteBuffer, String, String, Parser) with valid charsetName
    @Test
    public void testParseByteDataValidCharsetName() {
        ByteBuffer byteData = ByteBuffer.wrap("Test HTML content".getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "";
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("Test HTML content", doc.body().text(), "Test HTML content");
    }

    // Test method for parseByteData(ByteBuffer, String, String, Parser) with invalid charsetName
    @Test
    public void testParseByteDataInvalidCharsetName() {
        ByteBuffer byteData = ByteBuffer.wrap("Test HTML content".getBytes(Charset.forName("UTF-8")));
        String charsetName = "InvalidCharset";
        String baseUri = "";
        assertNull(DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
    }

    // Test method for readToByteBuffer(InputStream)
    @Test
    public void testReadToByteBufferInputStream() throws IOException {
        String htmlContent = "Test HTML content";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlContent.getBytes(Charset.forName("UTF-8")));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(outputStream.size(), byteData.limit(), "Byte buffer size should match that of the input stream");
    }

    // Test method for getCharsetFromContentType(String)
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}