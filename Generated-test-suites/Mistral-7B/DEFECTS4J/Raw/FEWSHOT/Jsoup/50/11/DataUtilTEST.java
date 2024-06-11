package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testParseByteData() throws IOException {
        // Test with defaultCharset
        String defaultCharsetTest = "This is a test.";
        ByteBuffer byteData = ByteBuffer.wrap(defaultCharsetTest.getBytes(Charset.forName("UTF-8")));
        String charsetName = null;
        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("This is a test.", doc.body().text());

        // Test with specifiedCharset
        String specifiedCharsetTest = "Das ist ein Test.";
        byteData = ByteBuffer.wrap(specifiedCharsetTest.getBytes(Charset.forName("UTF-8")));
        charsetName = "ISO-8859-1";
        doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("Das ist ein Test.", doc.body().text());

        // Test with BOM
        String bomTest = "\uFEFFThis is a test.";
        byteData = ByteBuffer.wrap(bomTest.getBytes(Charset.forName("UTF-8")));
        charsetName = null;
        doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("This is a test.", doc.body().text());

        // Test with invalid charset
        String invalidCharsetTest = "This is a test.";
        byteData = ByteBuffer.wrap(invalidCharsetTest.getBytes(Charset.forName("ABC-123")));
        charsetName = "ABC-123";
        doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testLoad() throws IOException {
        String testFile = "src/test/resources/testFile.html";
        File file = new File(testFile);

        // Test with null charsetName and baseUri
        Document doc = DataUtil.load(file, null, null);
        assertEquals(200, doc.statusCode());
        assertEquals("HTML document", doc.title());

        // Test with specifiedCharset and baseUri
        Charset charset = Charset.forName("ISO-8859-1");
        String baseUri = "http://example.com/";
        doc = DataUtil.load(file, charset.name(), baseUri);
        assertEquals(200, doc.statusCode());
        assertEquals("HTML document", doc.title());

        // Test with invalid charsetName
        Charset invalidCharset = Charset.forName("ABC-123");
        doc = DataUtil.load(file, invalidCharset.name(), baseUri);
        assertNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testString = "This is a test.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testString.getBytes(Charset.forName("UTF-8")));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // Test with null charsetName and baseUri
        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("This is a test.", doc.body().text());

        // Test with specifiedCharset and baseUri
        Charset charset = Charset.forName("ISO-8859-1");
        String baseUri = "http://example.com/";
        doc = DataUtil.load(inputStream, charset.name(), baseUri);
        assertEquals("This is a test.", doc.body().text());

        // Test with invalid charsetName
        Charset invalidCharset = Charset.forName("ABC-123");
        doc = DataUtil.load(inputStream, invalidCharset.name(), baseUri);
        assertNull(doc);
    }

    @Test
    public void testLoadInputStreamParser() throws IOException {
        String testString = "This is a test.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testString.getBytes(Charset.forName("UTF-8")));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // Test with null charsetName and baseUri
        Document doc = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        assertEquals("This is a test.", doc.body().text());

        // Test with specifiedCharset and baseUri
        Charset charset = Charset.forName("ISO-8859-1");
        String baseUri = "http://example.com/";
        doc = DataUtil.load(inputStream, charset.name(), baseUri, Parser.htmlParser());
        assertEquals("This is a test.", doc.body().text());

        // Test with invalid charsetName
        Charset invalidCharset = Charset.forName("ABC-123");
        doc = DataUtil.load(inputStream, invalidCharset.name(), baseUri, Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test string".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] readByteArray = outputStream.toByteArray();
        String testString = new String(readByteArray, 0, readByteArray.length, "UTF-8");
        assertEquals("Test string", testString);
    }
}