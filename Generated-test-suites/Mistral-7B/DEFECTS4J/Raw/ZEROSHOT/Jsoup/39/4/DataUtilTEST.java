package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

/**
 * Test class for DataUtil.
 *
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, fileContent);

        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><h1>Test</h1></body></html>";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, fileContent);

        Document doc = DataUtil.load(file, null, null);
        assertNotNull(doc);

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(inputStreamContent.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(inputStreamContent.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithOtherParser() throws IOException {
        String inputStreamContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/xml\"></head><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(inputStreamContent.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(fileContent.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);

        doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        // Determine charset from Content-Type and parse again
        String charset = DataUtil.getCharsetFromContentType(doc.outputSettings().contentType());
        if (charset != null) {
            byteData.rewind();
            doc = DataUtil.parseByteData(byteData, charset, null, Parser.htmlParser());
        }
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(inputStreamContent.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] bytes = byteData.array();
        assertTrue(bytes.length > 0);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(inputStreamContent.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
        byte[] bytes = byteData.array();
        assertTrue(bytes.length <= 10);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertTrue(charset != null && charset.equals("UTF-8"));

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertTrue(charset == null);
    }
}