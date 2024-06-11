package org.jsoup.helper;

import org.jsoup.Document;
import org.jsoup.helper.DataUtil;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = mock(Parser.class);
    }

    // Test case for load(File, String, String) method
    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"></meta>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n";
        File file = new File("testFile.html");
        byte[] fileBytes = fileContent.getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();

        Document expectedDocument = DataUtil.parseInput(fileContent, "UTF-8", "/baseUri");

        InputStream inputStream = new ByteArrayInputStream(fileBytes);
        Document actualDocument = DataUtil.load(file, "UTF-8", "/baseUri");

        MockitoAnnotations.initMocks(this);
        assertNotNull(actualDocument);
        assertNotNull(actualDocument.title());
        assertNotNull(actualDocument.titleText());
        assertNotNull(actualDocument.head());
        assertNotNull(actualDocument.body());
        assertNotNull(actualDocument.body().text());
        assertNotNull(actualDocument.select("title").text());
        assertNotNull(actualDocument.select("p").text());
        assertNotNull(expectedDocument.title());
        assertNotNull(expectedDocument.titleText());
        assertNotNull(expectedDocument.head());
        assertNotNull(expectedDocument.body());
        assertNotNull(expectedDocument.body().text());
        assertNotNull(expectedDocument.select("title").text());
        assertNotNull(expectedDocument.select("p").text());
        assertNotNull(parser);
        MockitoAnnotations.initMocks(this);
        Mockito.verify(parser).parseInput(fileContent, "/baseUri");

        DataUtil.load(file, null, "/baseUri"); // Test case for default charset

        MockitoAnnotations.initMocks(this);
        inputStream.close();
        file.delete();
    }

    // Test case for load(InputStream, String, String) method
    @Test
    public void testLoadInputStream() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"></meta>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n";
        byte[] fileBytes = fileContent.getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(fileBytes);

        Document expectedDocument = DataUtil.parseInput(fileContent, "UTF-8", "/baseUri");

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "/baseUri");

        assertNotNull(actualDocument);
        assertNotNull(actualDocument.title());
        assertNotNull(actualDocument.titleText());
        assertNotNull(actualDocument.head());
        assertNotNull(actualDocument.body());
        assertNotNull(actualDocument.body().text());
        assertNotNull(actualDocument.select("title").text());
        assertNotNull(actualDocument.select("p").text());
        assertNotNull(expectedDocument.title());
        assertNotNull(expectedDocument.titleText());
        assertNotNull(expectedDocument.head());
        assertNotNull(expectedDocument.body());
        assertNotNull(expectedDocument.body().text());
        assertNotNull(expectedDocument.select("title").text());
        assertNotNull(expectedDocument.select("p").text());
    }

    // Test case for load(InputStream, String, String, Parser) method
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"></meta>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n";
        byte[] fileBytes = fileContent.getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(fileBytes);

        Document expectedDocument = DataUtil.parseInput(fileContent, "UTF-8", "/baseUri");

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "/baseUri", parser);

        assertNotNull(actualDocument);
        assertNotNull(actualDocument.title());
        assertNotNull(actualDocument.titleText());
        assertNotNull(actualDocument.head());
        assertNotNull(actualDocument.body());
        assertNotNull(actualDocument.body().text());
        assertNotNull(actualDocument.select("title").text());
        assertNotNull(actualDocument.select("p").text());
        assertNotNull(expectedDocument.title());
        assertNotNull(expectedDocument.titleText());
        assertNotNull(expectedDocument.head());
        assertNotNull(expectedDocument.body());
        assertNotNull(expectedDocument.body().text());
        assertNotNull(expectedDocument.select("title").text());
        assertNotNull(expectedDocument.select("p").text());
        Mockito.verify(parser).parseInput(fileContent, "/baseUri");
    }

    // Test case for parseByteData(ByteBuffer, String, String, Parser) method with default charset
    @Test
    public void testParseByteDataDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"></meta>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n";
        byte[] fileBytes = fileContent.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(fileBytes);

        Document expectedDocument = DataUtil.parseInput(fileContent, "UTF-8", "/baseUri");

        Document actualDocument = DataUtil.parseByteData(byteBuffer, null, "/baseUri", parser);

        assertNotNull(actualDocument);
        assertNotNull(actualDocument.title());
        assertNotNull(actualDocument.titleText());
        assertNotNull(actualDocument.head());
        assertNotNull(actualDocument.body());
        assertNotNull(actualDocument.body().text());
        assertNotNull(actualDocument.select("title").text());
        assertNotNull(actualDocument.select("p").text());
        assertNotNull(expectedDocument.title());
        assertNotNull(expectedDocument.titleText());
        assertNotNull(expectedDocument.head());
        assertNotNull(expectedDocument.body());
        assertNotNull(expectedDocument.body().text());
        assertNotNull(expectedDocument.select("title").text());
        assertNotNull(expectedDocument.select("p").text());

        DataUtil.parseByteData(byteBuffer, null, "/baseUri", parser); // Test case for null charset
    }

    // Test case for parseByteData(ByteBuffer, String, String, Parser) method with specified charset
    @Test
    public void testParseByteDataSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"></meta>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n";
        byte[] fileBytes = fileContent.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(fileBytes);

        Document expectedDocument = DataUtil.parseInput(fileContent, "UTF-8", "/baseUri");

        Document actualDocument = DataUtil.parseByteData(byteBuffer, "UTF-8", "/baseUri", parser);

        assertNotNull(actualDocument);
        assertNotNull(actualDocument.title());
        assertNotNull(actualDocument.titleText());
        assertNotNull(actualDocument.head());
        assertNotNull(actualDocument.body());
        assertNotNull(actualDocument.body().text());
        assertNotNull(actualDocument.select("title").text());
        assertNotNull(actualDocument.select("p").text());
        assertNotNull(expectedDocument.title());
        assertNotNull(expectedDocument.titleText());
        assertNotNull(expectedDocument.head());
        assertNotNull(expectedDocument.body());
        assertNotNull(expectedDocument.body().text());
        assertNotNull(expectedDocument.select("title").text());
        assertNotNull(expectedDocument.select("p").text());

        DataUtil.parseByteData(byteBuffer, "GBK", "/baseUri", parser); // Test case for unsupported charset
    }
}