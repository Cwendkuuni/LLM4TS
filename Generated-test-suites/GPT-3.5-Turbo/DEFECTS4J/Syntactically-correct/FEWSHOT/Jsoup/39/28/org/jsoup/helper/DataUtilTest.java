import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
            // perform assertions on the loaded document
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
            // perform assertions on the loaded document
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
            // perform assertions on the loaded document
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        // perform assertions on the parsed document
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        // perform assertions on the extracted charset
    }
}