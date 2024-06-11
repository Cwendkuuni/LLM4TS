package org.jsoup.helper;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        String contentType = "text/html; charset=gb2312";
        String expected = "gb2312";
        assertEquals(expected, DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=UTF-8";
        expected = "UTF-8";
        assertEquals(expected, DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=invalidCharset";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testLoad() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);

        InputStream inputStream = new FileInputStream(file);
        Document documentFromStream = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(document, documentFromStream);

        charsetName = null;
        document = DataUtil.load(file, charsetName, baseUri);
        assertEquals(charsetName, DataUtil.defaultCharset);
        assertEquals(documentFromStream, document);

        inputStream.close();
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        String docData = "<html><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(charsetName)));

        Document documentFromByteData = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(documentFromByteData);
        assertEquals(docData, documentFromByteData.body().html());

        charsetName = null;
        documentFromByteData = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals(charsetName, DataUtil.defaultCharset);
        assertEquals(docData, documentFromByteData.body().html());

        ByteBuffer byteDataWithBom = ByteBuffer.wrap(new byte[]{UNICODE_BOM} );
        byteData = ByteBuffer.wrap(new byte[]{UNICODE_BOM} );
        byteData.put(docData.getBytes(Charset.forName(charsetName)));

        documentFromByteData = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals(docData.substring(1), documentFromByteData.body().html());
        assertEquals(charsetName, DataUtil.defaultCharset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test data";
        byte[] data = testData.getBytes(Charset.forName(DataUtil.defaultCharset));

        InputStream inputStream = new ByteArrayInputStream(data);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(testData, Charset.forName(DataUtil.defaultCharset).decode(byteBuffer).toString());

        byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        assertEquals(testData, Charset.forName(DataUtil.defaultCharset).decode(byteBuffer).toString());

        inputStream.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        Document document = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertEquals("<html><body>Test</body></html>", document.body().html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testData = "Test data";
        byte[] data = testData.getBytes(Charset.forName(DataUtil.defaultCharset));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(data);

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputData = outputStream.toByteArray();
        assertEquals(testData, new String(outputData, Charset.forName(DataUtil.defaultCharset)));

        inputStream.close();
        outputStream.close();
    }
}