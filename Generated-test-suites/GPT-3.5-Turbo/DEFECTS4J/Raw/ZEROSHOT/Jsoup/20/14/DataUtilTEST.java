package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithCharsetNameAndBaseUri() {
        // Given
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Then
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithCharsetNameAndBaseUri() {
        // Given
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Then
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithCharsetNameBaseUriAndParser() {
        // Given
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // When
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Then
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithCharsetNameBaseUriAndParser() {
        // Given
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // When
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Then
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBufferWithInputStream() {
        // Given
        InputStream inputStream = new FileInputStream("test.html");

        // When
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        // Then
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Given
        String contentType = "text/html; charset=UTF-8";

        // When
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Then
        assertEquals("UTF-8", charset);
    }
}