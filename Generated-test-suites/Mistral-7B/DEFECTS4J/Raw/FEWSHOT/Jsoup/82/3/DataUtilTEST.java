package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testParseInputStream() throws Exception {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        InputStream input = Mockito.mock(InputStream.class);
        ByteBuffer buffer = DataUtil.readToByteBuffer(input, 1024 * 32);
        Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.htmlParser());
        assertEquals("Expected document to be returned", doc, DataUtil.parseString("<html></html>", charsetName, baseUri, Parser.htmlParser()));

        // test with null input
        assertNull("Expected null document to be returned", DataUtil.parseInputStream(null, charsetName, baseUri, Parser.htmlParser()));

        // test with empty input
        Mockito.when(input.available()).thenReturn(0);
        doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.htmlParser());
        assertEquals("Expected empty document to be returned", doc, DataUtil.parseString("", charsetName, baseUri, Parser.htmlParser()));

        // test with empty charsetName
        charsetName = null;
        doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.htmlParser());
        assertEquals("Expected empty document to be returned", doc, DataUtil.parseString("", charsetName, baseUri, Parser.htmlParser()));

        // test with invalid charsetName
        charsetName = "INVALID_CHARSET";
        doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.htmlParser());
        assertEquals("Expected empty document to be returned", doc, DataUtil.parseString("", charsetName, baseUri, Parser.htmlParser()));

        // test with valid charsetName
        charsetName = "UTF-8";
        Mockito.when(input.available()).thenReturn(buffer.limit());
        buffer.position(0);
        Mockito.when(input.read()).thenAnswer(i -> buffer.get((int) i));
        doc = DataUtil.parseInputStream(input, charsetName, baseUri, Parser.htmlParser());
        assertEquals("Expected document to be returned", doc, parseTestDocument(charsetName, baseUri, "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>"));

        // test with malformed BOM
        buffer.clear();
        buffer.put((byte) 0x00);
        buffer.put((byte) 0x01);
        buffer.put((byte) 0xFF);
        buffer.put((byte) 0xFF);
        InputStream invalidInput = new ByteArrayInputStream(buffer.array());
        doc = DataUtil.parseInputStream(invalidInput, charsetName, baseUri, Parser.htmlParser());
        assertEquals("Expected empty document to be returned", doc, DataUtil.parseString("", charsetName, baseUri, Parser.htmlParser()));

        // test with valid BOM
        buffer.clear();
        buffer.put((byte) 0xFF);
        buffer.put((byte) 0xFE);
        buffer.put((byte) 0x00);
        buffer.put((byte) 0x00);
        InputStream validBomInput = new ByteArrayInputStream(buffer.array());
        doc = DataUtil.parseInputStream(validBomInput, charsetName, baseUri, Parser.htmlParser());
        assertEquals("Expected document to be returned", doc, parseTestDocument(charsetName, baseUri, "<html><head><meta charset=\"UTF-16\"></meta></head><body></body></html>"));

        // test with CRLF line breaks
        buffer.clear();
        buffer.put((byte) 0x0D);
        buffer.put((byte) 0x0A);
        buffer.put((byte) 0x0D);
        buffer.put((byte) 0x0A);
        buffer.put("<html></html>".getBytes());
        InputStream crlfInput = new ByteArrayInputStream(buffer.array());
        doc = DataUtil.parseInputStream(crlfInput, charsetName, baseUri, Parser.htmlParser());
        assertEquals("Expected document to be returned", doc, parseTestDocument(charsetName, baseUri, "<html>\r\n</html>"));
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ByteBuffer buffer = DataUtil.readToByteBuffer(input, 1024 * 32);
        byte[] expectedBytes = "test".getBytes();
        assertArrayEquals("Expected read buffer to match input", expectedBytes, buffer.array());
        assertEquals("Expected buffer size to match input size", expectedBytes.length, buffer.limit());
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertFalse("Expected boundary to be longer than zero", boundary.length() <= 0);
        assertTrue("Expected boundary to only contain mimeBoundaryChars", boundary.matches("[-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ]+"));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("Expected charset to match content-type", "UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull("Expected charset to be null when not present", charset);

        contentType = "text/html; charset=x-invalid";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull("Expected charset to be null when unsupported", charset);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        assertEquals("Expected charset to be valid", cs, DataUtil.validateCharset(cs));

        cs = "UTF-16";
        assertEquals("Expected charset to be valid", cs, DataUtil.validateCharset(cs));

        cs = "UTF-32";
        assertEquals("Expected charset to be valid", cs, DataUtil.validateCharset(cs));

        cs = "UTF-8\r\n";
        assertEquals("Expected charset to be valid", cs, DataUtil.validateCharset(cs));

        cs = "UTF-8\t";
        assertEquals("Expected charset to be valid", cs, DataUtil.validateCharset(cs));

        cs = "UTF-8'";
        assertEquals("Expected charset to be valid", cs, DataUtil.validateCharset(cs));

        cs = "UTF-8\"";
        assertEquals("Expected charset to be valid", cs, DataUtil.validateCharset(cs));

        cs = "invalid";
        assertNull("Expected charset to be null when invalid", DataUtil.validateCharset(cs));

        cs = "invalid'";
        assertNull("Expected charset to be null when invalid", DataUtil.validateCharset(cs));

        cs = "invalid\"";
        assertNull("Expected charset to be null when invalid", DataUtil.validateCharset(cs));

        cs = "invalid\r";
        assertNull("Expected charset to be null when invalid", DataUtil.validateCharset(cs));

        cs = "invalid\n";
        assertNull("Expected charset to be null when invalid", DataUtil.validateCharset(cs));

        cs = "invalid\t";
        assertNull("Expected charset to be null when invalid", DataUtil.validateCharset(cs));

        cs = "invalid\b";
        assertNull("Expected charset to be null when invalid", DataUtil.validateCharset(cs));

        cs = "invalid\f";
        assertNull("Expected charset to be null when invalid", DataUtil.validateCharset(cs));
    }

    private Document parseTestDocument(String charsetName, String baseUri, String testString) throws IOException {
        InputStream testInput = new ByteArrayInputStream(testString.getBytes(charsetName));
        Document doc = DataUtil.parseInputStream(testInput, charsetName, baseUri, Parser.htmlParser());
        testInput.close();
        return doc;
    }
}