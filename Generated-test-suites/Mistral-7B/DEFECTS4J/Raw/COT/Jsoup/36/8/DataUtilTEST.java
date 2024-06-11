package org.jsoup.helper;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil class.
 *
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("sample.html"); // replace with actual file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        // perform assertions on the loaded document
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String sampleHtml = "sample HTML content"; // replace with actual HTML content
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(sampleHtml.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        // perform assertions on the loaded document
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String sampleHtml = "sample HTML content"; // replace with actual HTML content
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(sampleHtml.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser(); // use any valid parser
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        // perform assertions on the loaded document
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024); // replace with actual byte buffer
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.htmlParser(); // use any valid parser
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        // perform assertions on the loaded document
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}.
     * @throws IOException
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        String sampleHtml = "sample HTML content"; // replace with actual HTML content
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(sampleHtml.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        int maxSize = 1024;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        // perform assertions on the byte buffer
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream)}.
     * @throws IOException
     */
    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        String sampleHtml = "sample HTML content"; // replace with actual HTML content
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(sampleHtml.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        // perform assertions on the byte buffer
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        // perform assertions on the charset
    }
}