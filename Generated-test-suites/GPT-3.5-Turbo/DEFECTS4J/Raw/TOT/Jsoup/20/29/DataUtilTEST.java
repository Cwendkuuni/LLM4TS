import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoad_File_ValidCharset_BaseUri() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";

        Document document = DataUtil.load(file, charset, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testLoad_File_InvalidCharset_BaseUri() throws IOException {
        File file = new File("test.html");
        String charset = "invalidCharset";
        String baseUri = "http://www.example.com";

        try {
            Document document = DataUtil.load(file, charset, baseUri);
            fail("IOException expected");
        } catch (IOException e) {
            assertEquals("Unsupported charset: invalidCharset", e.getMessage());
        }
    }

    @Test
    public void testLoad_File_ValidCharset_NullBaseUri() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = null;

        Document document = DataUtil.load(file, charset, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testLoad_File_NullCharset_BaseUri() throws IOException {
        File file = new File("test.html");
        String charset = null;
        String baseUri = "http://www.example.com";

        Document document = DataUtil.load(file, charset, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testLoad_File_NullCharset_NullBaseUri() throws IOException {
        File file = new File("test.html");
        String charset = null;
        String baseUri = null;

        Document document = DataUtil.load(file, charset, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_ValidCharset_BaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";

        Document document = DataUtil.load(inputStream, charset, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_InvalidCharset_BaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "invalidCharset";
        String baseUri = "http://www.example.com";

        try {
            Document document = DataUtil.load(inputStream, charset, baseUri);
            fail("IOException expected");
        } catch (IOException e) {
            assertEquals("Unsupported charset: invalidCharset", e.getMessage());
        }
    }

    @Test
    public void testLoad_InputStream_ValidCharset_NullBaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = null;

        Document document = DataUtil.load(inputStream, charset, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_NullCharset_BaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = null;
        String baseUri = "http://www.example.com";

        Document document = DataUtil.load(inputStream, charset, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_NullCharset_NullBaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = null;
        String baseUri = null;

        Document document = DataUtil.load(inputStream, charset, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_ValidCharset_BaseUri_Parser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";

        Document document = DataUtil.load(inputStream, charset, baseUri, Parser.htmlParser());

        assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_ValidCharset_BaseUri_NullParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = null;

        Document document = DataUtil.load(inputStream, charset, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_InvalidCharset_BaseUri_Parser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "invalidCharset";
        String baseUri = "http://www.example.com";

        try {
            Document document = DataUtil.load(inputStream, charset, baseUri, Parser.htmlParser());
            fail("IOException expected");
        } catch (IOException e) {
            assertEquals("Unsupported charset: invalidCharset", e.getMessage());
        }
    }

    @Test
    public void testLoad_InputStream_ValidCharset_NullBaseUri_Parser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = null;

        Document document = DataUtil.load(inputStream, charset, baseUri, Parser.htmlParser());

        assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_NullCharset_BaseUri_Parser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = null;
        String baseUri = "http://www.example.com";

        Document document = DataUtil.load(inputStream, charset, baseUri, Parser.htmlParser());

        assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_NullCharset_NullBaseUri_Parser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = null;
        String baseUri = null;

        Document document = DataUtil.load(inputStream, charset, baseUri, Parser.htmlParser());

        assertNotNull(document);
    }

    @Test
    public void testParseByteData_NullCharset_ValidBaseUri() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><p>Hello World</p></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testParseByteData_ValidCharset_ValidBaseUri() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><p>Hello World</p></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testParseByteData_NullCharset_InvalidBaseUri() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><p>Hello World</p></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testParseByteData_ValidCharset_InvalidBaseUri() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><p>Hello World</p></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testParseByteData_NullCharset_NullBaseUri() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><p>Hello World</p></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = null;
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testParseByteData_ValidCharset_NullBaseUri() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><p>Hello World</p></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testParseByteData_NullCharset_ValidBaseUri_NullParser() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><p>Hello World</p></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://www.example.com";
        Parser parser = null;

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testParseByteData_ValidCharset_ValidBaseUri_NullParser() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><p>Hello World</p></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = null;

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer_ValidInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType_ContentTypeWithCharset() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_ContentTypeWithoutCharset() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_NullContentType() {
        String contentType = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}