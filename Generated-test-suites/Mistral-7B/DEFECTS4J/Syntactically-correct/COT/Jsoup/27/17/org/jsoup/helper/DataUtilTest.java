package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, "UTF-8", null);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("testFile.html");
        Document document = assertThrows(IOException.class, () -> DataUtil.load(file, "", null));
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        byteBuffer.put((byte) 65); // BOM
        byteBuffer.put((byte) 65);
        byteBuffer.put((byte) 65);
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithNullCharsetName() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[0]);
        Document document = DataUtil.parseByteData(byteBuffer, null, "", Parser.htmlParser());
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteDataWithEmptyCharsetName() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[0]);
        Document document = DataUtil.parseByteData(byteBuffer, "", "", Parser.htmlParser());
        assertNotNull(document);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream)}.
     * @throws IOException
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'd', 'o', 'c', 'u', 'm', 'e', 'n', 't', '>', '\n'});
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] bytes = byteBuffer.array();
        String expected = "<document>";
        assertArrayEquals(expected.getBytes(), bytes);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}