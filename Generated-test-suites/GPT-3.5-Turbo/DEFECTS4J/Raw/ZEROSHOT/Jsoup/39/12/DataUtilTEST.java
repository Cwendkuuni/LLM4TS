package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        FileInputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        FileInputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "", Jsoup.parser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Jsoup.parser();
        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        FileInputStream inputStream = new FileInputStream("test.html");
        int maxSize = 100;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
    }
}