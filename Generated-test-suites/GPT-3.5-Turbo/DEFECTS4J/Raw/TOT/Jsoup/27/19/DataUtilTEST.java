import org.junit.*;
import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);

            assertNotNull(doc);
            assertEquals("UTF-8", doc.outputSettings().charset().name());
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);

            assertNotNull(doc);
            assertEquals("UTF-8", doc.outputSettings().charset().name());
            assertEquals("Hello World!", doc.select("h1").text());
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithCustomParser() {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser(); // using XML parser for testing purposes

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);

            assertNotNull(doc);
            assertEquals("UTF-8", doc.outputSettings().charset().name());
            assertEquals("html", doc.select("html").tagName());
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException thrown");
        }
    }

    @Test
    public void testParseByteDataWithCharsetFromMetaTag() {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=iso-8859-1\"></head><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteData = Charset.forName("UTF-8").encode(html);
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.outputSettings().charset().name());
        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testParseByteDataWithCharsetFromContentType() {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteData = Charset.forName("UTF-8").encode(html);
        String charsetName = "ISO-8859-1";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.outputSettings().charset().name());
        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testReadToByteBuffer() {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());

        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

            assertNotNull(byteData);
            assertEquals(html, new String(byteData.array()));
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }
}