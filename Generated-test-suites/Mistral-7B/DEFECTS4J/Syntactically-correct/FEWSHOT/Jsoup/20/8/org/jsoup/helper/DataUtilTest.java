package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

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

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        // Test loading a file using the default parser
        File file = new File("test.html");
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
        assertEquals("html", document.tagName());

        // Test loading an input stream using the default parser
        InputStream inputStream = new FileInputStream(file);
        document = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(document, DataUtil.load(file, charsetName, baseUri).cloneNode(true));
        inputStream.close();

        // Test loading an input stream with a specified parser
        Parser parser = mock(Parser.class);
        when(parser.parseInput(anyString(), eq(baseUri))).thenReturn(document.cloneNode(true));
        document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertEquals(document, DataUtil.load(file, charsetName, baseUri, parser).cloneNode(true));
        inputStream.close();
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = parser();

        // Test decoding with default charset
        ByteBuffer byteData = DataUtil.readToByteBuffer(getFileInputStream("test_utf8.html"));
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("html", document.tagName());

        // Test decoding with specified charset
        charsetName = "ISO-8859-1";
        byteData = DataUtil.readToByteBuffer(getFileInputStream("test_latin1.html"));
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("html", document.tagName());

        // Test re-decoding when charset is found in the document
        byteData = DataUtil.readToByteBuffer(getFileInputStream("test_charset.html"));
        document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        document = DataUtil.parseByteData(byteData, "ISO-8859-1", baseUri, parser);
        assertEquals("html", document.tagName());

        // Test handling of null charsetName
        byteData = DataUtil.readToByteBuffer(getFileInputStream("test_utf8.html"));
        document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertEquals("html", document.tagName());
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    private InputStream getFileInputStream(String fileName) throws IOException {
        return new FileInputStream(new File(getClass().getResource(fileName).getFile()));
    }
}