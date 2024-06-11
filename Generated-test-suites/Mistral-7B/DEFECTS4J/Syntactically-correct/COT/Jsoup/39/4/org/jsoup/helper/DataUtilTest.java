package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "test.html"; // Replace with actual path to the test file
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";

        File testFile = new File(filePath);
        Document doc = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultParser() throws IOException {
        String testData = "Test Data";
        InputStream testInputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";

        Document doc = DataUtil.load(testInputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertTrue(doc.body().text().contains(testData));
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String testData = "Test Data";
        InputStream testInputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        Parser parser = DataUtil.parser(); // Use the default parser

        Document doc = DataUtil.load(testInputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertTrue(doc.body().text().contains(testData));
    }

    @Test
    public void testParseByteDataWithDefaultParser() throws IOException {
        String charsetName = null;
        String baseUri = "http://localhost";
        Parser parser = DataUtil.parser();

        String testData = "Test Data";
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(testData.getBytes("UTF-8"));
        byteStream.writeTo(byteData -> DataUtil.parseByteData(ByteBuffer.wrap(byteData), charsetName, baseUri, parser));

        Document doc = parser.parseInput(byteStream.toByteArray(), baseUri);
        assertNotNull(doc);
        assertTrue(doc.body().text().contains(testData));
    }

    @Test
    public void testParseByteDataWithInvalidCharsetName() throws IOException {
        String charsetName = "InvalidCharsetName";
        String baseUri = "http://localhost";
        Parser parser = DataUtil.parser();

        String testData = "Test Data";
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(testData.getBytes("UTF-8"));

        try {
            DataUtil.parseByteData(ByteBuffer.wrap(byteStream.toByteArray()), charsetName, baseUri, parser);
            // This should not come here as we expect an exception
            fail("Expected IllegalArgumentException for invalid charset name.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadToByteBufferWithDefaultMaxSize() throws IOException {
        String testData = "Test Data";
        InputStream testInputStream = new ByteArrayInputStream(testData.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);

        byte[] expectedBytes = testData.getBytes();
        byte[] actualBytes = new byte[byteData.limit()];
        byteData.get(actualBytes);

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadToByteBufferWithCustomMaxSize() throws IOException {
        String testData = "Test Data";
        InputStream testInputStream = new ByteArrayInputStream(testData.getBytes());
        int maxSize = 5;

        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream, maxSize);

        byte[] expectedBytes = new byte[maxSize];
        testData.getBytes(0, expectedBytes, 0, maxSize);

        byte[] actualBytes = new byte[byteData.limit()];
        byteData.get(actualBytes);

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testGetCharsetFromContentTypeWithSupportedCharset() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertTrue(charset != null);
        assertTrue(charset.equals("UTF-8"));
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=UTF-16BE";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertTrue(charset == null);
    }

}