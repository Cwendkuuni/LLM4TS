package org.jsoup.helper; // import statements go here

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // given
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // when
        Document doc = DataUtil.load(inputFile, charsetName, baseUri);

        // then
        assertNotNull(doc);
        assertEquals("HTML document", doc.tagName(), "html");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        // given
        String inputStreamData = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";

        // when
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        // then
        assertNotNull(doc);
        assertEquals("HTML document", doc.tagName(), "html");
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        // given
        String inputStreamData = "<?xml version='1.0' encoding='ISO-8859-1'?>\n<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes());
        String charsetName = "ISO-8859-1";
        String baseUri = "http://example.com";

        // when
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        // then
        assertNotNull(doc);
        assertEquals("HTML document", doc.tagName(), "html");
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        // given
        String inputStreamData = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";

        // when
        DataUtil.load(inputStream, charsetName, baseUri);

        // then
        // exception should be thrown
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        // given
        String byteData = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData.getBytes());

        // when
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());

        // then
        assertNotNull(doc);
        assertEquals("HTML document", doc.tagName(), "html");
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        // given
        String byteData = "<?xml version='1.0' encoding='ISO-8859-1'?>\n<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData.getBytes());

        // when
        Document doc = DataUtil.parseByteData(byteBuffer, "ISO-8859-1", "", Parser.htmlParser());

        // then
        assertNotNull(doc);
        assertEquals("HTML document", doc.tagName(), "html");
    }

    @Test
    public void testParseByteDataWithDefaultCharsetAndBOM() {
        // given
        String byteData = "\ufeff<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData.getBytes());

        // when
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());

        // then
        assertNotNull(doc);
        assertEquals("HTML document", doc.tagName(), "html");
    }
}