package org.jsoup.helper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    public void testParseByteData() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        // test with BOM
        ByteBuffer byteDataBom = ByteBuffer.wrap(new byte[]{UNICODE_BOM, 'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 'J', 's', 'o', 'u', 'p', '\r', '\n'});
        String docDataBom = "This is Jsoup";
        Document docBom = DataUtil.parseByteData(byteDataBom, charsetName, baseUri, parser);
        assertEquals(docDataBom, docBom.body().text());

        // test with valid charset in Content-Type header
        String contentTypeValid = "text/html; charset=UTF-8";
        ByteArrayOutputStream byteStreamValid = new ByteArrayOutputStream();
        byteStreamValid.write(docDataBom.getBytes());
        InputStream inputStreamValid = new ByteArrayInputStream(byteStreamValid.toByteArray());
        Document docValid = DataUtil.parseByteData(ByteBuffer.wrap(inputStreamValid), charsetName, baseUri, parser);
        assertEquals(docDataBom, docValid.body().text());

        // test with invalid charset in Content-Type header
        String contentTypeInvalid = "text/html; charset=invalid";
        ByteArrayOutputStream byteStreamInvalid = new ByteArrayOutputStream();
        byteStreamInvalid.write(docDataBom.getBytes());
        InputStream inputStreamInvalid = new ByteArrayInputStream(byteStreamInvalid.toByteArray());
        Document docInvalid = DataUtil.parseByteData(ByteBuffer.wrap(inputStreamInvalid), charsetName, baseUri, parser);
        assertNull(docInvalid);

        // test with detected charset from meta tag
        String metaCharset = "text/html; charset=ISO-8859-1";
        ByteArrayOutputStream byteStreamMeta = new ByteArrayOutputStream();
        byteStreamMeta.write("<html lang=\"en\"><head><meta charset=\"ISO-8859-1\"></head><body><p>This is Jsoup</p></body></html>".getBytes());
        InputStream inputStreamMeta = new ByteArrayInputStream(byteStreamMeta.toByteArray());
        Document docMeta = DataUtil.parseByteData(ByteBuffer.wrap(inputStreamMeta), null, baseUri, parser);
        assertEquals("This is Jsoup", docMeta.body().text());
        assertEquals("ISO-8859-1", docMeta.outputSettings().charset());

        // test with empty input
        ByteBuffer emptyByteData = DataUtil.emptyByteBuffer();
        Document docEmpty = DataUtil.parseByteData(emptyByteData, charsetName, baseUri, parser);
        assertTrue(docEmpty.body().text().isEmpty());
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertEquals("This is a test file", doc.body().text());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String docData = "This is a test file";
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(docData.getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(docData, doc.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);
        DataUtil.crossStreams(inputStream, outputStream);
        String expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        InputStream inputStreamTest = new ByteArrayInputStream(expected.getBytes());
        int len;
        while ((len = inputStreamTest.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        InputStream inputStreamRead = new FileInputStream(outputFile);
        byte[] outputBuffer = new byte[bufferSize];
        int outputLen;
        String actual = "";
        while ((outputLen = inputStreamRead.read(outputBuffer)) != -1) {
            actual += new String(outputBuffer, 0, outputLen);
        }
        inputStreamRead.close();
        assertEquals(expected, actual);
    }
}