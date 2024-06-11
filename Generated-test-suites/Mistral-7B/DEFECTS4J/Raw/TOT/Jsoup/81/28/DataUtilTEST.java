package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html"); // Replace with the actual file path
        String expectedCharset = "UTF-8"; // Replace with the actual charset if known
        String baseUri = "http://example.com/";

        Document doc = DataUtil.load(testFile, expectedCharset, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());

        // Add more assertions based on the structure and content of the document
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com/";

        Document doc = DataUtil.load(inputStream, expectedCharset, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());

        // Add more assertions based on the structure and content of the document
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"GBK\"></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String baseUri = "http://example.com/";

        try {
            Document doc = DataUtil.load(inputStream, null, baseUri);
            Assert.fail("Expected IOException to be thrown due to invalid charset.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = DataUtil.Parser.htmlParser();

        Document doc = DataUtil.parseInputStream(inputStream, expectedCharset, baseUri, parser);

        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());

        // Add more assertions based on the structure and content of the document
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        // Add assertions to verify that the files have been correctly copied
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();

        Assert.assertTrue(buffer.position() == 0);
        Assert.assertTrue(buffer.limit() == 0);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testContent = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes());
        ByteBuffer expectedResult = ByteBuffer.wrap(testContent.getBytes());

        ByteBuffer result = DataUtil.readToByteBuffer(inputStream);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File testFile = new File("test.txt");
        ByteBuffer expectedResult = DataUtil.readFileToByteBuffer(testFile);

        ByteBuffer result = DataUtil.readFileToByteBuffer(testFile);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String result = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", result);
    }

    @Test
    public void testValidateCharset() {
        String testCharset = "UTF-8";

        String result = DataUtil.validateCharset(testCharset);

        Assert.assertEquals(testCharset, result);
    }

    @Test
    public void testMimeBoundary() {
        String result = DataUtil.mimeBoundary();

        // Add assertions to verify that the generated boundary string is valid
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        String testContentUTF8 = "\uFEFF<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        String testContentUTF16 = "\uFEFF\uFFFE<html><head><meta charset=\"UTF-16\"></head><body><p>Test</p></body></html>";
        String testContentUTF32 = "\uFFFE\uFFFE\uFFFE\uFFFE<html><head><meta charset=\"UTF-32\"></head><body><p>Test</p></body></html>";

        ByteBuffer byteDataUTF8 = ByteBuffer.wrap(testContentUTF8.getBytes());
        ByteBuffer byteDataUTF16 = ByteBuffer.wrap(testContentUTF16.getBytes());
        ByteBuffer byteDataUTF32 = ByteBuffer.wrap(testContentUTF32.getBytes());

        BomCharset resultUTF8 = DataUtil.detectCharsetFromBom(byteDataUTF8);
        BomCharset resultUTF16 = DataUtil.detectCharsetFromBom(byteDataUTF16);
        BomCharset resultUTF32 = DataUtil.detectCharsetFromBom(byteDataUTF32);

        Assert.assertEquals("UTF-8", resultUTF8.charset);
        Assert.assertEquals(false, resultUTF8.offset);

        Assert.assertEquals("UTF-16", resultUTF16.charset);
        Assert.assertEquals(false, resultUTF16.offset);

        Assert.assertEquals("UTF-32", resultUTF32.charset);
        Assert.assertEquals(true, resultUTF32.offset); // and I hope it's on your system
    }
}