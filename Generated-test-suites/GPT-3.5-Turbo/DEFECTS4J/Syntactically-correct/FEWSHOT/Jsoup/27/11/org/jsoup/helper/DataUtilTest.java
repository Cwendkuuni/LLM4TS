package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataUtilTest {

    private static Document document;

    @BeforeClass
    public static void setUp() throws Exception {
        document = new Document("");
    }

    @Test
    public void testLoadFromFile() throws Exception {
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document result = DataUtil.load(new File("test.html"), charsetName, baseUri);

        assertEquals(document, result);
    }

    @Test
    public void testLoadFromInputStream() throws Exception {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document result = DataUtil.load(in, charsetName, baseUri);

        assertEquals(document, result);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws Exception {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document result = DataUtil.load(in, charsetName, baseUri, parser);

        assertEquals(document, result);
    }

    @Test
    public void testParseByteDataWithCharset() throws Exception {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals(document, result);
    }

    @Test
    public void testParseByteDataWithoutCharset() throws Exception {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = null;
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals(document, result);
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        InputStream inStream = new FileInputStream("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);

        ByteBuffer expectedByteData = ByteBuffer.wrap("test".getBytes());
        assertEquals(expectedByteData, byteData);
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() throws Exception {
        String contentType = "text/html";
        String expectedCharset = null;

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, result);
    }
}