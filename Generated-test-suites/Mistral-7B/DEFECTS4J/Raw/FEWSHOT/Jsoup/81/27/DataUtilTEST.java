package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "UTF-8";
        Document doc = DataUtil.parseInputStream(inputStream, expectedCharset, null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><div>Hello</div></body></html>", doc.html());
        assertEquals(expectedCharset, doc.outputSettings().charset());

        String charsetHeader = "text/html; charset=ISO-8859-1";
        String htmlWithCharsetHeader = "<html><head><meta http-equiv=\"Content-Type\" content=\"" + charsetHeader + "\"></head><body><div>Hello</div></body></html>";
        inputStream = new ByteArrayInputStream(htmlWithCharsetHeader.getBytes(StandardCharsets.UTF_8));
        expectedCharset = "ISO-8859-1";
        doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("<html><head><meta http-equiv=\"Content-Type\" content=\"" + charsetHeader + "\"><meta charset=\"ISO-8859-1\"></head><body><div>Hello</div></body></html>", doc.html());
        assertEquals(expectedCharset, doc.outputSettings().charset());

        String invalidCharset = "invalid-charset";
        String htmlWithInvalidCharsetHeader = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + invalidCharset + "\"></head><body><div>Hello</div></body></html>";
        inputStream = new ByteArrayInputStream(htmlWithInvalidCharsetHeader.getBytes(StandardCharsets.UTF_8));
        expectedCharset = "UTF-8"; // defaults to UTF-8 if no charset is found
        doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html\"><meta charset=\"UTF-8\"></head><body><div>Hello</div></body></html>", doc.html());
        assertEquals(expectedCharset, doc.outputSettings().charset());

        // Test when input stream is empty
        inputStream = new ByteArrayInputStream(new byte[0]);
        doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("<html></html>", doc.html());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "testData/test.html";
        File file = new File(filePath);
        String expectedCharset = "UTF-8";
        Document doc = DataUtil.load(file, expectedCharset, null);
        assertEquals(new File(filePath).length(), doc.body().text().length() * 2); // html multiplies the text length by 2
        assertEquals(expectedCharset, doc.outputSettings().charset());

        // Test when file doesn't exist
        file = new File("does_not_exist.html");
        assertThrows(IOException.class, () -> DataUtil.load(file, null, null));

        // Test when charset is not found in the file
        String fileWithNoCharset = "testData/test_no_charset.html";
        doc = DataUtil.load(new File(fileWithNoCharset), null, null);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String originalData = "Original Data";
        String targetData = "Target Data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(originalData.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String readDataFromOutputStream = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(targetData, readDataFromOutputStream);
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "abcdefghijkl-mno-pqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String mimeBoundary = DataUtil.mimeBoundary();
        assertEquals(expectedMimeBoundary, mimeBoundary);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test Data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedData = testData.getBytes(StandardCharsets.UTF_8);

        assertEquals(expectedData.length, byteBuffer.limit());
        byte[] resultData = new byte[byteBuffer.limit()];
        byteBuffer.get(resultData);
        assertEquals(expectedData, resultData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);

        charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String validCharset = "UTF-8";
        String invalidCharset = "invalid-charset";
        String invalidCharsetEmpty = "";
        String invalidCharsetWithQuotes = "\"invalid-charset\"";

        String validatedCharset = DataUtil.validateCharset(validCharset);
        assertEquals(validCharset, validatedCharset);

        assertNull(DataUtil.validateCharset(null));
        assertNull(DataUtil.validateCharset(""));
        assertNull(DataUtil.validateCharset(invalidCharsetWithQuotes));
        assertNull(DataUtil.validateCharset(invalidCharset));
    }
}