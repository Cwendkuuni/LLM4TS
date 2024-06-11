
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class DataUtilTest {

    @Test
    public void loadFile() throws IOException {
        // Test basic functionality
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "");
        Assert.assertNotNull(document);
    }

    @Test
    public void loadInputStream() throws IOException {
        // Test basic functionality
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(document);
    }

    @Test
    public void loadInputStreamWithParser() throws IOException {
        // Test basic functionality
        InputStream inputStream = new FileInputStream("test.xml");
        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void crossStreams() throws IOException {
        // Test basic functionality
        InputStream inputStream = new FileInputStream("source.txt");
        Path outputPath = Files.createTempFile("output", ".txt");
        Files.write(outputPath, "".getBytes(), StandardOpenOption.TRUNCATE_EXISTING);

        DataUtil.crossStreams(inputStream, Files.newOutputStream(outputPath, StandardOpenOption.APPEND));

        long inputFileSize = new File("source.txt").length();
        long outputFileSize = outputPath.toFile().length();

        Assert.assertEquals(inputFileSize, outputFileSize);
    }

    @Test
    public void parseByteData() throws IOException {
        // Test basic functionality
        byte[] byteData = "<html><body><h1>Hello World</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(byteData);
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        Assert.assertEquals(Jsoup.parse(new String(byteData, Charset.forName("UTF-8"))), document);
    }

    @Test
    public void readToByteBufferWithMaxSize() throws IOException {
        // Test basic functionality
        byte[] byteData = "<html><body><h1>Hello World</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(byteData);
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, byteData.length);
        Assert.assertEquals(byteData.length, buffer.capacity());
    }

    @Test
    public void readToByteBuffer() throws IOException {
        // Test basic functionality
        byte[] byteData = "<html><body><h1>Hello World</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(byteData);
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertEquals(byteData.length, buffer.capacity());
    }

    @Test
    public void readFileToByteBuffer() throws IOException {
        // Test basic functionality
        File file = new File("source.txt");
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
        Assert.assertNotNull(buffer);
    }

    @Test
    public void emptyByteBuffer() {
        // Test basic functionality
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        Assert.assertNotNull(buffer);
        Assert.assertEquals(0, buffer.capacity());
    }

    @Test
    public void getCharsetFromContentType() {
        // Test basic functionality
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNotNull(charset);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void mimeBoundary() {
        // Test basic functionality
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(DataUtil.boundaryLength, boundary.length());
        Assert.assertTrue(boundary.matches("[a-zA-Z0-9_-]+"));
    }

    @Test
    public void detectCharsetFromBom() {
        // Test basic functionality
        String charset = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF}), null);
        Assert.assertNotNull(charset);
        Assert.assertEquals("UTF-8", charset);
    }
}
