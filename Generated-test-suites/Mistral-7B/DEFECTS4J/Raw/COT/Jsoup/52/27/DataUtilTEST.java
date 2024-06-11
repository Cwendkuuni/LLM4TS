package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser.HtmlParser;

/**
 * Test class for DataUtil
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("src/test/resources/test.html");
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInvalidFile() throws IOException {
        File file = new File("src/test/resources/non_existent_file.html");
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        assertThrows(IOException.class, () -> DataUtil.load(file, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = IOUtils.toString(getClass().getResourceAsStream("/test.html"));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInvalidInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = IOUtils.toString(getClass().getResourceAsStream("/test.html"));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("invalid_charset".toCharArray()));
        String charsetName = "invalid_charset";
        String baseUri = "http://example.com";
        assertThrows(IllegalCharsetNameException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStreamWithValidCharsetButInvalidContent() throws IOException {
        String invalidContent = "not_html";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        assertThrows(ExceptionInInitializerError.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStreamWithValidCharsetAndContent() throws IOException {
        String htmlContent = IOUtils.toString(getClass().getResourceAsStream("/test.html"));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri, HtmlParser.instance());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File file = new File("src/test/resources/test.html");
        File outputFile = new File("src/test/resources/output.html");

        DataUtil.crossStreams(new FileInputStream(file), new FileOutputStream(outputFile));

        String expectedContent = IOUtils.toString(getClass().getResourceAsStream("/test.html"));
        String actualContent = IOUtils.toString(new FileInputStream(outputFile));

        assertNotNull(actualContent);
        assertNotNull(expectedContent);
        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testParseByteData() throws IOException {
        File file = new File("src/test/resources/test.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);

        Document document = DataUtil.parseByteData(byteData, null, null, HtmlParser.instance());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithEmptyContent() throws IOException {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();

        Document document = DataUtil.parseByteData(byteData, null, null, HtmlParser.instance());
        assertNotNull(document);
    }

    // Add more test cases for the remaining methods in DataUtil

}