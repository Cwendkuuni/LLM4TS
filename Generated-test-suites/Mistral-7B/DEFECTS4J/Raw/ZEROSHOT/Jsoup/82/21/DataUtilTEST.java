package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String expectedContent = "Hello World";
        File file = new File("test.html");
        FileUtils.writeStringToFile(file, expectedContent, StandardCharsets.UTF_8);

        Document actualDocument = DataUtil.load(file, "UTF-8", null);
        assertEquals(expectedContent, actualDocument.body().text());
        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String expectedContent = "Hello World";
        InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes(StandardCharsets.UTF_8));

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals(expectedContent, actualDocument.body().text());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String expectedContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes(StandardCharsets.UTF_8));

        Parser parser = Parser.xmlParser();
        Document actualDocument = DataUtil.load(inputStream, "UTF-8", null, parser);
        assertEquals(expectedContent, actualDocument.html().toString());
        inputStream.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String expectedContent = "Hello World";
        String charsetName = "UTF-8";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new ByteArrayInputStream(expectedContent.getBytes(StandardCharsets.UTF_8)), outputStream);
        byte[] fileContent = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(fileContent);

        Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
        assertEquals(expectedContent, actualDocument.body().text());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithParserAndCharsetFromContentType() throws IOException {
        String expectedContent = "text/html; charset=GB2312\n<html><body><h1>你好 世界</h1></body></html>";
        String charsetName = null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new ByteArrayInputStream(expectedContent.getBytes(StandardCharsets.UTF_8)), outputStream);
        byte[] fileContent = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(fileContent);

        Elements metaElements = new Elements();
        metaElements.add(new Element("meta", new Attrs().put("http-equiv", "content-type").put("content", "text/html; charset=GB2312")));
        Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
        Element charsetElement = actualDocument.selectFirst("meta[http-equiv=content-type]");
        assertEquals("GB2312", charsetElement.attr("content").split(";")[0].trim());
        assertEquals("你好 世界", actualDocument.body().text());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithParserAndCharsetFromXmlDeclaration() throws IOException {
        String expectedContent = "<?xml encoding='GB2312'?>\n<html><body><h1>你好 世界</h1></body></html>";
        String charsetName = null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new ByteArrayInputStream(expectedContent.getBytes(StandardCharsets.UTF_8)), outputStream);
        byte[] fileContent = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(fileContent);

        Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.xmlParser());
        assertEquals("GB2312", actualDocument.outputSettings().charset().name());
        assertEquals("你好 世界", actualDocument.body().text());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithParserAndInvalidCharset() throws IOException {
        String expectedContent = "text/html; charset=invalid-charset\n<html><body><h1>Invalid Charset</h1></body></html>";
        String charsetName = null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new ByteArrayInputStream(expectedContent.getBytes(StandardCharsets.UTF_8)), outputStream);
        byte[] fileContent = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(fileContent);

        Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
        assertEquals("UTF-8", actualDocument.outputSettings().charset().name()); // fallback to default charset
        assertEquals("Invalid Charset", actualDocument.body().text());
        inputStream.close();
    }

    @Test
    public void testMimeBoundary() {
        String expectedContent = "--MyBoundary\r\nContent-Disposition: form-data; name=\"fieldName\"\r\nContent-Type: text/plain\r\nContent-Transfer-Encoding: 7bit\r\n\r\nHello World\r\n--MyBoundary--";
        String boundary = DataUtil.mimeBoundary();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new ByteArrayInputStream(expectedContent.getBytes(StandardCharsets.UTF_8)), outputStream);
        byte[] fileContent = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(fileContent);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, -1);
        String actualContent = new String(byteBuffer.array(), 0, byteBuffer.limit(), StandardCharsets.UTF_8);

        assertEquals(expectedContent, actualContent);
        inputStream.close();
    }
}