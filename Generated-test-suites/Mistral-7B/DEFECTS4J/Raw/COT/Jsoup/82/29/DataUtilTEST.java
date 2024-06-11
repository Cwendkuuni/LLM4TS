package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "Some content to be loaded";
        File file = new File("testFile.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        FileInputStream inputStream = new FileInputStream(file);

        Document document = DataUtil.load(file, null, null);

        assertEquals(fileContent, document.body().html());
        inputStream.close();
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String fileContent = "Some content to be loaded";
        String charset = "UTF-16";
        File file = new File("testFile.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        FileInputStream inputStream = new FileInputStream(file);

        Document document = DataUtil.load(file, charset, null);

        assertEquals(fileContent, document.body().html());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "Some content to be loaded";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null);

        assertEquals(inputStreamContent, document.body().html());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputStreamContent = "Some content to be loaded";
        String charset = "UTF-16";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, charset, null);

        assertEquals(inputStreamContent, document.body().html());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><body>Some content to be loaded</body></html>";
        String charset = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, charset, null, DataUtil.Parser.xmlParser());

        assertEquals("<html><body>Some content to be loaded</body></html>", document.body().html());
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals("Test Data", outputStream.toString());

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "Some content to be loaded";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());

        assertEquals(inputStreamContent, document.body().html());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String inputStreamContent = "Some content to be loaded";
        String charset = "UTF-16";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, charset, null, DataUtil.Parser.htmlParser());

        assertEquals(inputStreamContent, document.body().html());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithSpecifiedParser() throws IOException {
        String inputStreamContent = "<html><body>Some content to be loaded</body></html>";
        String charset = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, charset, null, DataUtil.Parser.xmlParser());

        assertEquals("<html><body>Some content to be loaded</body></html>", document.body().html());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "Some content to be loaded";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String invalidCharset = "InvalidCharset";

        DataUtil.parseInputStream(inputStream, invalidCharset, null, DataUtil.Parser.htmlParser());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();

        assertEquals(0, emptyByteBuffer.limit());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "Some content to be read";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        int maxSize = 32;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertEquals(inputStreamContent.length(), byteBuffer.limit());
        inputStream.close();
    }

    @Test
    public void testValidateCharset() {
        String validCharset = "UTF-8";
        String invalidCharset = "InvalidCharset";

        assertEquals(validCharset, DataUtil.validateCharset(validCharset));
        assertNull(DataUtil.validateCharset(null));
        assertNull(DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
        assertTrue(mimeBoundary.length() >= 32);
    }
}