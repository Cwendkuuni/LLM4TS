package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        String fileContent = "This is a sample file";
        File inputFile = new File("sample.html");
        inputFile.createNewFile();
        FileWriter fileWriter = new FileWriter(inputFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document doc = DataUtil.load(inputFile, "UTF-8", "");
        Assert.assertNotNull(doc);
        Assert.assertEquals(fileContent, doc.body().text());

        inputFile.delete();
    }

    @Test
    public void testLoadInputStream() throws Exception {
        String inputStreamContent = "This is a sample input stream";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        String inputStreamContent = "<html><body><p>This is a sample input stream</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("<p>This is a sample input stream</p>", doc.body().html());
    }

    @Test
    public void testCrossStreams() throws Exception {
        String inputContent = "This is input content";
        String outputContent = "This is output content";

        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputBytes = outputStream.toByteArray();
        String outputString = new String(outputBytes, "UTF-8");

        Assert.assertEquals(outputContent, outputString);
    }

    @Test
    public void testParseInputStreamWithEmptyCharset() throws Exception {
        String charsetName = "";
        ByteArrayInputStream inputStream = new ByteArrayInputStream("This is a test".getBytes("UTF-8"));

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws Exception {
        String charsetName = "InvalidCharset";
        ByteArrayInputStream inputStream = new ByteArrayInputStream("This is a test".getBytes("UTF-8"));

        DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws Exception {
        String charsetName = "UTF-8";
        ByteArrayInputStream inputStream = new ByteArrayInputStream("This is a test".getBytes("UTF-8"));

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithBOM() throws Exception {
        String bomContent = "\uFEFFThis is a test";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bomContent.getBytes("UTF-16"));

        Document doc = DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithMetaTag() throws Exception {
        String metaContent = "<html><head><meta charset=\"UTF-16\"></head><body><p>This is a test</p></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(metaContent.getBytes("UTF-8"));

        Document doc = DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("<p>This is a test</p>", doc.body().html());
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws Exception {
        String xmlContent = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xml:base=\"http://example.com/malta/\" xml version=\"1.0\" encoding=\"UTF-16\"> <head><title>Malta</title></head> <body><h1>Welcome to Malta</h1></body> </html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));

        Document doc = DataUtil.parseInputStream(inputStream, null, "", Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("<h1>Welcome to Malta</h1>", doc.body().html());
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        String inputContent = "This is input content";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(inputContent.length(), byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws Exception {
        String inputContent = "This is input content";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 5);
        Assert.assertEquals(5, byteBuffer.limit());
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNegativeMaxSize() throws Exception {
        String inputContent = "This is input content";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));

        DataUtil.readToByteBuffer(inputStream, -5);
    }
}