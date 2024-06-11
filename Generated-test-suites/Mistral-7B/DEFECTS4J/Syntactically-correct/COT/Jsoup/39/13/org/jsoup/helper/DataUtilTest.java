package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoad() throws Exception {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Jsoup.htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testLoadWithInvalidCharsetName() throws Exception {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=xxyyzz\"></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "xxyyzz";
        String baseUri = "https://example.com";
        Parser parser = Jsoup.htmlParser();

        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, charsetName, baseUri, parser));
    }

    @Test
    public void testLoadWithNullCharsetName() throws Exception {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = null;
        String baseUri = "https://example.com";
        Parser parser = Jsoup.htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testLoadWithEmptyInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Jsoup.htmlParser();

        assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri, parser));
    }

    @Test
    public void testParseByteData() throws Exception {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Jsoup.htmlParser();

        Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithNullCharsetName() throws Exception {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        String baseUri = "https://example.com";
        Parser parser = Jsoup.htmlParser();

        assertThrows(NullPointerException.class, () -> DataUtil.parseByteData(byteData, null, baseUri, parser));
    }

    @Test
    public void testParseByteDataWithInvalidCharsetName() throws Exception {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        String charsetName = "xxyyzz";
        String baseUri = "https://example.com";
        Parser parser = Jsoup.htmlParser();

        assertThrows(IllegalCharsetNameException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithNullInputStream() throws Exception {
        InputStream inputStream = null;

        assertThrows(NullPointerException.class, () -> DataUtil.readToByteBuffer(inputStream));
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws Exception {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        int maxSize = 10;

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithUnlimitedMaxSize() throws Exception {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }
}