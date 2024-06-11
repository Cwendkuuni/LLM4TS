package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=GBK";
        assertEquals("GBK", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=invalid";
        assertNull(DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testParseByteData() throws IOException {
        String docData = "Some HTML document";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals(docData, doc.body().html());

        docData += "\uFEFF";
        byteData.rewind();
        doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(docData.substring(1), doc.body().html());
    }

    @Test
    public void testLoad() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertEquals(file.length(), doc.body().bytes().length);
        assertEquals(charsetName, doc.outputSettings().charset());

        charsetName = null;
        doc = DataUtil.load(file, null, baseUri);
        assertEquals(defaultCharset, doc.outputSettings().charset());

        InputStream inputStream = new FileInputStream(file);
        doc = DataUtil.load(inputStream, charsetName, baseUri);
        inputStream.close();
        assertEquals(file.length(), doc.body().bytes().length);
        assertEquals(charsetName, doc.outputSettings().charset());

        inputStream = new ByteArrayInputStream(file.getBytes());
        doc = DataUtil.load(inputStream, charsetName, baseUri);
        inputStream.close();
        assertEquals(file.length(), doc.body().bytes().length);
        assertEquals(charsetName, doc.outputSettings().charset());

        inputStream = new ByteArrayInputStream(file.getBytes());
        byte[] buffer = new byte[file.length()];
        inputStream.read(buffer);
        inputStream.close();
        doc = DataUtil.load(buffer, charsetName, baseUri);
        assertEquals(file.length(), doc.body().bytes().length);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testLoadWithParser() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(file, charsetName, baseUri, Parser.xmlParser());
        assertEquals(file.length(), doc.body().bytes().length);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "test string";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedByteArray = testString.getBytes();

        assertEquals(expectedByteArray.length, byteBuffer.remaining());
        byte[] buffer = new byte[byteBuffer.remaining()];
        byteBuffer.get(buffer);
        assertArrayEquals(expectedByteArray, buffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        byte[] expectedByteArray = new byte[file.length()];
        FileInputStream inputStream = new FileInputStream(file);
        inputStream.read(expectedByteArray);
        inputStream.close();

        assertEquals(expectedByteArray.length, byteBuffer.remaining());
        byte[] buffer = new byte[byteBuffer.remaining()];
        byteBuffer.get(buffer);
        assertArrayEquals(expectedByteArray, buffer);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(boundary.length(), boundaryLength);

        for (int i = 0; i < boundaryLength; i++) {
            char c = boundary.charAt(i);
            assertTrue("Character '" + c + "' is a valid mime boundary character", DataUtil.isValidMimeBoundaryCharacter(c));
        }
    }
}