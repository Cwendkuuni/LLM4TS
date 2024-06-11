import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadWithFile() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadWithParser() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Arrange
        InputStream input = new FileInputStream("input.txt");
        OutputStream output = new FileOutputStream("output.txt");

        // Act
        DataUtil.crossStreams(input, output);

        // Assert
        // Check if the output file is created and has content
        File outputFile = new File("output.txt");
        Assert.assertTrue(outputFile.exists());
        Assert.assertTrue(outputFile.length() > 0);
    }

    @Test
    public void testParseInputStream() throws IOException {
        // Arrange
        InputStream input = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseInputStream(input, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Arrange
        InputStream input = new FileInputStream("test.html");

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input);

        // Assert
        Assert.assertNotNull(byteBuffer);
        Assert.assertTrue(byteBuffer.limit() > 0);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        // Arrange
        File file = new File("test.html");

        // Act
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        // Assert
        Assert.assertNotNull(byteBuffer);
        Assert.assertTrue(byteBuffer.limit() > 0);
    }

    @Test
    public void testEmptyByteBuffer() {
        // Act
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        // Assert
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(byteBuffer.limit(), 0);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertEquals(charset, "UTF-8");
    }

    @Test
    public void testMimeBoundary() {
        // Act
        String boundary = DataUtil.mimeBoundary();

        // Assert
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), 32);
    }

    @Test
    public void testDetectCharsetFromBom() {
        // Arrange
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

        // Act
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        // Assert
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals(bomCharset.charset, "UTF-8");
        Assert.assertTrue(bomCharset.offset);
    }
}