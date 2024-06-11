import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File file = new File("test.html");
        DataUtil.writeStringToFile(file, fileContent);

        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(fileContent, doc.body().text());

        DataUtil.deleteRecursively(file.getParentFile()); // clean up
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.writeStringToOutputStream(outputStream, inputStreamContent);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String inputStreamContent = "<xml><data>This is a test input stream.</data></xml>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.writeStringToOutputStream(outputStream, inputStreamContent);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.xml().toString());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFileContent = "Input file content.";
        String outputFileContent = "Output file content.";
        File inputFile = new File("input.html");
        File outputFile = new File("output.html");

        DataUtil.writeStringToFile(inputFile, inputFileContent);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.load(new File("input.html"), "UTF-8", null, new DataUtil.DataConsumer() {
            @Override
            public void handleData(InputStream in, OutputStream out) throws IOException {
                DataUtil.crossStreams(in, out);
            }
        });
        Assert.assertEquals(outputFileContent, outputStream.toString());

        DataUtil.deleteRecursively(inputFile.getParentFile());
        DataUtil.deleteRecursively(new File("output.html").getParentFile()); // clean up
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String byteDataContent = "This is a test byte data.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.writeStringToOutputStream(outputStream, byteDataContent);
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());

        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(byteDataContent, doc.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String byteDataContent = "This is a test byte data.";
        String charsetName = "UTF-8";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.writeStringToOutputStream(outputStream, byteDataContent);
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());

        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(byteDataContent, doc.body().text());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String byteDataContent = "This is a test byte data.";
        String charsetName = "InvalidCharset";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.writeStringToOutputStream(outputStream, byteDataContent);
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());

        try {
            Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
            Assert.fail("Expected IllegalArgumentException with message: Invalid charset name: InvalidCharset");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid charset name: InvalidCharset", e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertTrue(boundary.length() > 0);
        Assert.assertFalse(boundary.isEmpty());
    }
}