import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {
    private static final File testFile = new File("test.html");
    private static final String baseUri = "http://example.com";

    @BeforeClass
    public static void setup() throws IOException {
        // Creating a test file for input stream testing
        BufferedWriter writer = new BufferedWriter(new FileWriter(testFile));
        writer.write("<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>");
        writer.close();
    }

    @AfterClass
    public static void cleanup() {
        // Deleting the test file
        testFile.delete();
    }

    @Test
    public void testLoadFile() throws IOException {
        // Typical use case
        Document document = DataUtil.load(testFile, "UTF-8", baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Test Page", document.title());

        // Edge case: Empty file
        File emptyFile = new File("empty.html");
        emptyFile.createNewFile();
        Document emptyDocument = DataUtil.load(emptyFile, "UTF-8", baseUri);
        Assert.assertNotNull(emptyDocument);
        Assert.assertEquals("", emptyDocument.text());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Typical use case
        FileInputStream inputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(inputStream, "UTF-8", baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Test Page", document.title());

        // Edge case: Empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        Document emptyDocument = DataUtil.load(emptyInputStream, "UTF-8", baseUri);
        Assert.assertNotNull(emptyDocument);
        Assert.assertEquals("", emptyDocument.text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Typical use case
        FileInputStream inputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(inputStream, "UTF-8", baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("Test Page", document.title());

        // Edge case: Empty input stream with XML parser
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        Document emptyDocument = DataUtil.load(emptyInputStream, "UTF-8", baseUri, Parser.xmlParser());
        Assert.assertNotNull(emptyDocument);
        Assert.assertEquals("", emptyDocument.text());

    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = Charset.forName("UTF-8").encode("<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>");
        Document document = DataUtil.parseByteData(byteData, "UTF-8", baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("Test Page", document.title());

        // Edge case: Null charsetName
        ByteBuffer nullCharsetByteData = Charset.forName("UTF-8").encode("<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>");
        Document nullCharsetDocument = DataUtil.parseByteData(nullCharsetByteData, null, baseUri, Parser.htmlParser());
        Assert.assertNotNull(nullCharsetDocument);
        Assert.assertEquals("Test Page", nullCharsetDocument.title());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] testData = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
        Assert.assertEquals(testData.length, byteData.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Typical use case
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        // Edge case: No charset specified
        String noCharsetContentType = "text/html";
        String noCharset = DataUtil.getCharsetFromContentType(noCharsetContentType);
        Assert.assertNull(noCharset);

        // Edge case: Multiple content types with charset
        String multipleContentType = "text/html; charset=UTF-8, text/plain; charset=ISO-8859-1";
        String multipleCharset = DataUtil.getCharsetFromContentType(multipleContentType);
        Assert.assertEquals("UTF-8", multipleCharset);
    }
}