package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil class
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "Test data";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(testData.getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "Test data";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(testData.getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.xmlParser();
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        InputStream in = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't'});
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertNotNull(out.toString());
    }

    @Test
    public void testParseByteDataWithNullCharsetName() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'T', 'e', 's', 't'});
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser();
        assertThrows(NullPointerException.class, () -> DataUtil.parseByteData(byteData, null, baseUri, parser));
    }

    @Test
    public void testParseByteDataWithEmptyCharsetName() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'T', 'e', 's', 't'});
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser();
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, "", baseUri, parser));
    }

    @Test
    public void testParseByteDataWithInvalidCharsetName() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'T', 'e', 's', 't'});
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser();
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, "InvalidCharset", baseUri, parser));
    }

    @Test
    public void testParseByteDataWithInvalidCharsetNameUppercase() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'T', 'e', 's', 't'});
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser();
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, "InvalidCharset", baseUri, parser));
    }

    @Test
    public void testParseByteDataWithUnsupportedCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'T', 'e', 's', 't'});
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser();
        assertThrows(IllegalCharsetNameException.class, () -> DataUtil.parseByteData(byteData, "X-CustomCharset", baseUri, parser));
    }

    @Test
    public void testReadToByteBufferWithNegativeMaxSize() throws IOException {
        InputStream inStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> DataUtil.readToByteBuffer(inStream, -1));
    }

    @Test
    public void testReadToByteBufferWithZeroMaxSize() throws IOException {
        InputStream inStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, 0);
        assertNotNull(byteBuffer);
        assertFalse(byteBuffer.hasRemaining());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = new ByteArrayInputStream(new byte[1024]);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.hasRemaining());
        byteBuffer.clear();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.hasRemaining());
        byteBuffer.clear();
    }
}