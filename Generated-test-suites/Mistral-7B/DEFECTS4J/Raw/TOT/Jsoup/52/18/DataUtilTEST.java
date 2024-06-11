package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileUTF8() throws IOException {
        String fileContent = "This is a test file in UTF-8 encoding.";
        File testFile = new File("testFile.html");
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testOutputStream.write(fileContent.getBytes(Charset.forName("UTF-8")));

        testFile.createNewFile();
        try (OutputStream testFileOutputStream = new FileOutputStream(testFile)) {
            testFileOutputStream.write(testOutputStream.toByteArray());
        }

        Document doc = DataUtil.load(testFile, "UTF-8", "");
        Assert.assertNotNull(doc);
        Assert.assertEquals(fileContent, doc.body().text());
    }

    @Test
    public void testLoadFileUnknownEncoding() throws IOException {
        String fileContent = "This is a test file in ISO-8859-1 encoding.";
        File testFile = new File("testFile.html");
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testOutputStream.write(fileContent.getBytes(Charset.forName("ISO-8859-1")));

        testFile.createNewFile();
        try (OutputStream testFileOutputStream = new FileOutputStream(testFile)) {
            testFileOutputStream.write(testOutputStream.toByteArray());
        }

        try {
            Document doc = DataUtil.load(testFile, null, "");
            Assert.fail("Expected IOException for unknown encoding");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInvalidFile() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = null;

        try {
            doc = DataUtil.load(testFile, "UTF-8", "");
            Assert.fail("Expected IOException for invalid file");
        } catch (IOException e) {
            // Expected
        }

        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStreamUTF8() throws IOException {
        String inputStreamContent = "This is a test input stream in UTF-8 encoding.";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.load(testInputStream, "UTF-8", "");
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testLoadInputStreamUnknownEncoding() throws IOException {
        String inputStreamContent = "This is a test input stream in ISO-8859-1 encoding.";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName("ISO-8859-1")));

        try {
            Document doc = DataUtil.load(testInputStream, null, "");
            Assert.fail("Expected IOException for unknown encoding");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamInvalid() throws IOException {
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testOutputStream.write("This is a test input stream.".getBytes());
        InputStream testInputStream = new ByteArrayInputStream(testOutputStream.toByteArray());

        try {
            Document doc = DataUtil.load(testInputStream, "UTF-8", "");
            Assert.fail("Expected IOException for invalid input stream");
        } catch (IOException e) {
            // Expected
        }

        Assert.assertNull(DataUtil.parseByteData(DataUtil.readToByteBuffer(testInputStream), null, "", Parser.htmlParser()));
    }
}