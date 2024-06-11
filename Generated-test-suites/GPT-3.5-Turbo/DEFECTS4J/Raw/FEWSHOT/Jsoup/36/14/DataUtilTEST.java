package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class DataUtilTest {
    private final String mockFileContent = "<html>\n" +
            "<head>\n" +
            "<title>Mock File</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<h1>Hello World</h1>\n" +
            "</body>\n" +
            "</html>";

    @Test
    public void testLoadFromFile() {
        try {
            File tempFile = createTempFile(mockFileContent);
            Document document = DataUtil.load(tempFile, "UTF-8", "");

            Assert.assertNotNull(document);
            Assert.assertEquals("Mock File", document.title());

            tempFile.delete();
        } catch (IOException e) {
            Assert.fail("IO error occurred: " + e.getMessage());

        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            InputStream inputStream = new ByteArrayInputStream(mockFileContent.getBytes());
            Document document = DataUtil.load(inputStream, "UTF-8", "");

            Assert.assertNotNull(document);
            Assert.assertEquals("Mock File", document.title());
        } catch (IOException e) {
            Assert.fail("IO error occurred: " + e.getMessage());

        }
    }

    @Test
    public void testLoadFromInputStreamWithCustomParser() {
        try {
            InputStream inputStream = new ByteArrayInputStream(mockFileContent.getBytes());
            Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());

            Assert.assertNotNull(document);
            Assert.assertEquals("Mock File", document.title());
        } catch (IOException e) {
            Assert.fail("IO error occurred: " + e.getMessage());

        }
    }

    @Test
    public void testParseByteDataWithCharset() {
        try {
            InputStream inputStream = new ByteArrayInputStream(mockFileContent.getBytes());
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

            Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());

            Assert.assertNotNull(document);
            Assert.assertEquals("Mock File", document.title());
        } catch (IOException e) {
            Assert.fail("IO error occurred: " + e.getMessage());

        }
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        try {
            InputStream inputStream = new ByteArrayInputStream(mockFileContent.getBytes());
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

            Document document = DataUtil.parseByteData(byteBuffer, null, "", Parser.htmlParser());

            Assert.assertNotNull(document);
            Assert.assertEquals("Mock File", document.title());
        } catch (IOException e) {
            Assert.fail("IO error occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        try {
            InputStream inputStream = new ByteArrayInputStream(mockFileContent.getBytes());
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 100);

            Assert.assertNotNull(byteBuffer);
            Assert.assertEquals(100, byteBuffer.capacity());
        } catch (IOException e) {
            Assert.fail("IO error occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        try {
            InputStream inputStream = new ByteArrayInputStream(mockFileContent.getBytes());
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

            Assert.assertNotNull(byteBuffer);
            Assert.assertEquals(mockFileContent.length(), byteBuffer.capacity());
        } catch (IOException e) {
            Assert.fail("IO error occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentTypeWithCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNull(charset);
    }

    private File createTempFile(String content) throws IOException {
        File tempFile = File.createTempFile("temp", ".html");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        writer.write(content);
        writer.close();

        return tempFile;
    }
}