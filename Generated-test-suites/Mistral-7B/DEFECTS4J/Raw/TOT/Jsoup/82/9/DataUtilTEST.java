package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "path/to/valid/file.html";
        Document doc = DataUtil.load(new File(filePath), charsetName, "");

        Assert.assertNotNull(doc);
        Assert.assertEquals(charsetName, doc.outputSettings().charset().name());
    }

    @Test
    public void testLoadFileWithInvalidCharsetInHeader() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "path/to/file.html";
        String baseUri = "http://example.com";

        // Create a sample HTML file with an invalid charset
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head profile=\"http://gmpg.org/xfn/11\">\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
                "<title>Test page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div>Test content</div>\n" +
                "</body>\n" +
                "</html>";

        File tempFile = File.createTempFile("datautiltest", null);
        Files.write(tempFile.toPath(), htmlContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(tempFile, charsetName, baseUri);

        tempFile.delete();

        Assert.assertNotNull(doc);
        Assert.assertEquals("ISO-8859-1", doc.outputSettings().charset().name());
    }

    @Test
    public void testLoadFileWithInvalidCharsetInMetaTag() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "path/to/file.html";
        String baseUri = "http://example.com";

        // Create a sample HTML file with an invalid charset in meta tag
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head profile=\"http://gmpg.org/xfn/11\">\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html\">\n" +
                "<meta charset=\"gb2312\">\n" + // invalid charset
                "<title>Test page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div>Test content</div>\n" +
                "</body>\n" +
                "</html>";

        File tempFile = File.createTempFile("datautiltest", null);
        Files.write(tempFile.toPath(), htmlContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(tempFile, charsetName, baseUri);

        tempFile.delete();

        Assert.assertNotNull(doc);
        Assert.assertEquals(charsetName, doc.outputSettings().charset().name());
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head profile=\"http://gmpg.org/xfn/11\">\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + charsetName + "\">\n" +
                "<title>Test page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div>Test content</div>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charsetName, "");

        Assert.assertNotNull(doc);
        Assert.assertEquals(charsetName, doc.outputSettings().charset().name());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidContentType() throws IOException {
        String charsetName = "UTF-8";
        String invalidContentType = "application/x-invalid";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = "Test content".getBytes(StandardCharsets.UTF_8);
        outputStream.write(data);
        outputStream.close();

        InputStream inputStream = new ByteArrayInputStream(("Content-Type: " + invalidContentType + "\r\n" +
                new String(outputStream.toByteArray())).getBytes(StandardCharsets.UTF_8));

        try {
            Document doc = DataUtil.load(inputStream, charsetName, "");
            Assert.fail("Expected IOException to be thrown.");
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().contains("content type is not supported: application/x-invalid"));
        }

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidCharsetInContentType() throws IOException {
        String charsetName = "UTF-8";
        String invalidContentType = "text/html; charset=gb2312";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = "Test content".getBytes(StandardCharsets.UTF_8);
        outputStream.write(data);
        outputStream.close();

        InputStream inputStream = new ByteArrayInputStream(("Content-Type: " + invalidContentType + "\r\n" +
                new String(outputStream.toByteArray())).getBytes(StandardCharsets.UTF_8));

        try {
            Document doc = DataUtil.load(inputStream, null, "");
            Assert.fail("Expected IOException to be thrown.");
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().contains("Must set charset arg to character set of file to parse."));
        }

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithValidCharsetInContentType() throws IOException {
        String charsetName = "UTF-8";
        String validContentType = "text/html; charset=" + charsetName;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = "Test content".getBytes(StandardCharsets.UTF_8);
        outputStream.write(data);
        outputStream.close();

        InputStream inputStream = new ByteArrayInputStream(("Content-Type: " + validContentType + "\r\n" +
                new String(outputStream.toByteArray())).getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, charsetName, "");

        Assert.assertNotNull(doc);
        Assert.assertEquals(charsetName, doc.outputSettings().charset().name());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithUTF8Bom() throws IOException {
        String charsetName = "UTF-8";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = "\uFEFFTest content".getBytes(StandardCharsets.UTF_8); // UTF-8 BOM
        outputStream.write(data);
        outputStream.close();

        InputStream inputStream = new ByteArrayInputStream(new String(outputStream.toByteArray()).getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, charsetName, "");

        Assert.assertNotNull(doc);
        Assert.assertEquals(charsetName, doc.outputSettings().charset().name());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithUTF16Bom() throws IOException {
        String charsetName = "UTF-16";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = {"\u00FE\u00FF", "Test content".getBytes(StandardCharsets.UTF_8)};
        outputStream.write(data);
        outputStream.close();

        InputStream inputStream = new ByteArrayInputStream(new String(outputStream.toByteArray(), StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, charsetName, "");

        Assert.assertNotNull(doc);
        Assert.assertEquals(charsetName, doc.outputSettings().charset().name());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithUTF32Bom() throws IOException {
        String charsetName = "UTF-32";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = {"\u0000\u0000\u00FE\u00FF", "Test content".getBytes(StandardCharsets.UTF_8)};
        outputStream.write(data);
        outputStream.close();

        InputStream inputStream = new ByteArrayInputStream(new String(outputStream.toByteArray(), StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8));

        try {
            Document doc = DataUtil.load(inputStream, charsetName, "");
            Assert.fail("Expected IOException to be thrown.");
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().contains("UTF-32 is not supported"));
        }

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFilePath = "path/to/input/file.txt";
        String outputFilePath = "path/to/output/file.txt";

        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        // Add a test to ensure that the content of the input file is written to the output file
        byte[] expectedContent = "expected content".getBytes(StandardCharsets.UTF_8);
        byte[] actualContent = Files.readAllBytes(new File("path/to/input/file.txt").toPath());

        Assert.assertArrayEquals(expectedContent, actualContent);

        Files.delete(outputFile.toPath());
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseInputStreamWithEmptyByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        try {
            Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
            Assert.fail("Expected IOException to be thrown.");
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().contains("Stream is empty or truncated"));
        }

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithBOMCharset() throws IOException {
        String charsetName = "UTF-8";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bom = {"\u00EF\u00BB\u00BF", "Test content".getBytes(StandardCharsets.UTF_8)};
        outputStream.write(bom);
        outputStream.write("Test content".getBytes(StandardCharsets.UTF_8));
        outputStream.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());

        Assert.assertNotNull(doc);
        Assert.assertEquals(charsetName, doc.outputSettings().charset().name());

        inputStream.close();
        outputStream.close();
    }
}