package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File file = new File("test.html");
        File tempFile = File.createTempFile("test", null);
        try (Writer writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(fileContent);
        }

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, baseUri);

        Document actualDocument = DataUtil.load(tempFile, charsetName, baseUri);
        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.body().text(), actualDocument.body().text());

        tempFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String fileContent = "This is a test file.";
        byte[] fileContentBytes = fileContent.getBytes(Charset.forName("UTF-8"));

        InputStream inputStream = new ByteArrayInputStream(fileContentBytes);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, baseUri);

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    @Test
    public void testLoadInputStreamWithDifferentParser() throws IOException {
        String fileContent = "This is a test file.";
        byte[] fileContentBytes = fileContent.getBytes(Charset.forName("UTF-8"));

        InputStream inputStream = new ByteArrayInputStream(fileContentBytes);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        Document expectedDocument = parser.parseInput(fileContent, baseUri);
        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    @Test
    public void testParseByteDataWithUTF8() {
        String fileContent = "This is a test file.";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        byte[] fileContentBytes = fileContent.getBytes(Charset.forName("UTF-8"));

        ByteBuffer byteData = ByteBuffer.wrap(fileContentBytes);
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, baseUri);

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    @Test
    public void testParseByteDataWithUnknownCharset() {
        String fileContent = "This is a test file.";
        String charsetName = "EUC-JP";
        String baseUri = "http://example.com";
        byte[] fileContentBytes = fileContent.getBytes(Charset.forName("UTF-8"));

        ByteBuffer byteData = ByteBuffer.wrap(fileContentBytes);
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, baseUri);

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFile = "test.html";
        String outputFile = "output.html";

        File input = new File(inputFile);
        File output = new File(outputFile);
        InputStream in = new FileInputStream(input);
        OutputStream out = new FileOutputStream(output);

        DataUtil.crossStreams(in, out);
        byte[] expectedBytes = input.length() > 0 ? input.readAllBytes() : new byte[0];
        byte[] actualBytes = output.readAllBytes();

        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }
}