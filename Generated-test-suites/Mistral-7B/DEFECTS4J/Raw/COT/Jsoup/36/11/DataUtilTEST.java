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
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static Document validDocument;
    private static Document invalidCharsetDocument;
    private static Document emptyDocument;
    private static Document noDocument;
    private static Document noBaseUriDocument;
    private static Document nullParserDocument;

    @BeforeClass
    public static void setup() throws IOException {
        String htmlContent = "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>Test</title>" +
                "</head>" +
                "<body>" +
                "<p>This is a test.</p>" +
                "</body>" +
                "</html>";

        validDocument = new Parser().parseInput(htmlContent, "UTF-8");

        String invalidCharsetHtmlContent = "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"gb2312\">" +
                "<title>Test</title>" +
                "</head>" +
                "<body>" +
                "<p>This is a test.</p>" +
                "</body>" +
                "</html>";

        invalidCharsetDocument = new Parser().parseInput(invalidCharsetHtmlContent, "UTF-8");

        String emptyHtmlContent = "<html lang=\"en\"></html>";

        emptyDocument = new Parser().parseInput(emptyHtmlContent, "UTF-8");

        String noDocumentHtmlContent = "";

        noDocument = new Parser().parseInput(noDocumentHtmlContent, "UTF-8");

        String noBaseUriHtmlContent = "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>Test</title>" +
                "</head>" +
                "<body>" +
                "<p>This is a test.</p>" +
                "<a href=\"about.html\">About</a>" +
                "</body>" +
                "</html>";

        noBaseUriDocument = new Parser().parseInput(noBaseUriHtmlContent, "UTF-8");

        String nullParserHtmlContent = "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>Test</title>" +
                "</head>" +
                "<body>" +
                "<p>This is a test.</p>" +
                "</body>" +
                "</html>";

        nullParserDocument = new Parser();
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        String expectedCharset = "UTF-8";
        String baseUri = "/test";

        Document document = DataUtil.load(testFile, expectedCharset, baseUri);

        assertEquals(validDocument, document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = validDocument.html();
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String expectedCharset = "UTF-8";
        String baseUri = "/test";

        Document document = DataUtil.load(inputStream, expectedCharset, baseUri);

        assertEquals(validDocument, document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = validDocument.html();
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertEquals(validDocument, document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File testFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "/test";

        assertThrows(IOException.class, () -> DataUtil.load(testFile, charsetName, baseUri));

        File invalidCharsetTestFile = new File("test_invalid_charset.html");
        String expectedCharset = "gb2312";

        assertThrows(IOException.class, () -> DataUtil.load(invalidCharsetTestFile, expectedCharset, baseUri));
    }

    @Test
    public void testParseByteDataWithValidCharset() {
        String htmlContent = validDocument.html();
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        assertEquals(validDocument, document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String htmlContent = invalidCharsetDocument.html();
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        assertNull(document);
    }

    @Test
    public void testParseByteDataWithEmptyInput() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(0);
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        assertNull(document);
    }

    @Test
    public void testParseByteDataWithNoDocument() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[0]);
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        assertNull(document);
    }

    @Test(expected = NullPointerException.class)
    public void testParseByteDataWithNullParser() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[0]);
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Parser parser = null;

        assertThrows(NullPointerException.class, () -> DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = validDocument.html();
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertEquals(expectedByteBuffer, byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String htmlContent = validDocument.html();
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        int maxSize = 10;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // depending on the input content length and max size, the byteBuffer might not be filled completely
        // in this test case, we only check the byteBuffer size to be greater than zero
        assertEquals(maxSize > 0, byteBuffer.capacity() > 0);
    }
}