package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadDocument() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        // Test loading from file
        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(document);

        // Test loading from InputStream
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'h', 't', 'm', 'l'});
        document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);

        // Test loading with different charset
        charsetName = "ISO-8859-1";
        document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);

        // Test loading with null charset
        document = DataUtil.load(inputStream, null, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = null;
        String baseUri = "http://example.com/";
        Parser parser = mock(Parser.class);

        ByteBuffer byteData = ByteBuffer.allocate(10);
        byteData.put("Content-Type: text/html; charset=UTF-8\r\n\r\n<html><body><h1>Test</h1></body></html>".getBytes());
        byteData.flip();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.html());

        charsetName = "ISO-8859-1";
        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(mock(Document.class));
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.html());

        byteData = ByteBuffer.wrap("Content-Type: text/html; charset=UTF-8\r\n\r\n<html><body><h1>Test</h1></body></html>"
                .getBytes());
        byteData.flip();
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.html());

        byteData = ByteBuffer.wrap("Content-Type: text/html; charset=UTF-8\r\n\r\n<html><body><h1>Test</h1></body></html>"
                .getBytes());
        byteData.rewind();
        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(document);

        byteData = ByteBuffer.wrap("Content-Type: text/html; charset=UTF-8\r\n\r\n<html><body><h1>Test</h1></body></html>"
                .getBytes());
        byteData.flip();
        charsetName = "ISO-8859-1";
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.html());

        byteData = ByteBuffer.wrap("<html><body><h1>Test</h1></body></html>".getBytes());
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.html());
    }

    @Test
    public void testGetCharsetFromContentType() throws IOException {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}