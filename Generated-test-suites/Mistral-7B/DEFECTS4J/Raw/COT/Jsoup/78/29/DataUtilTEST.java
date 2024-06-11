import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("sample.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        String outputContent = outputStream.toString(StandardCharsets.UTF_8.toString());
        Assert.assertEquals(inputContent, outputContent);
    }

    @Test
    public void testParseInputStreamWithCharsetDetect() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-16\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"InvalidCharset\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        DataUtil.parseInputStream(inputStream, "InvalidCharset", null, Parser.htmlParser());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputStreamWithNullParser() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-16\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        DataUtil.parseInputStream(inputStream, null, null, null);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputContent = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedBytes = inputContent.getBytes(StandardCharsets.UTF_8);

        Assert.assertArrayEquals(expectedBytes, byteBuffer.array());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String inputContent = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        int maxSize = 10;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        byte[] expectedBytes = inputContent.getBytes(StandardCharsets.UTF_8);

        Assert.assertArrayEquals(expectedBytes, byteBuffer.array());
        Assert.assertEquals(inputContent.length(), byteBuffer.position());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "This is a test file.";
        File file = new File("sample.txt");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        String expectedContent = fileContent;

        Assert.assertArrayEquals(expectedContent.getBytes(StandardCharsets.UTF_8), byteBuffer.array());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, byteBuffer.limit());
    }
}

class FileWriter {
    FileWriter(File file) throws IOException {
        this.file = file;
    }

    void write(String content) throws IOException {
        FileWriter writer = this;
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(writer.file));
        fileWriter.write(content);
        fileWriter.close();
    }

    File file;
}