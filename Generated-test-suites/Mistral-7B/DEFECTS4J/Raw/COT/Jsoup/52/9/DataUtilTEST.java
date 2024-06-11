package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String inputData = "<html><head><title>Test</title></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputData.getBytes(Charset.forName(charsetName)));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String inputData = "<html><head><title>Test</title></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputData.getBytes(Charset.forName(charsetName)));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFilePath = "input.txt";
        String outputFilePath = "output.txt";
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);
        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
        Assert.assertTrue(outputFile.length() > 0);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String charsetName = null;
        String baseUri = "http://example.com";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String docData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String docData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() {
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String docData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName("UTF-8")));
        Document document = null;
        try {
            document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(document);
    }
}