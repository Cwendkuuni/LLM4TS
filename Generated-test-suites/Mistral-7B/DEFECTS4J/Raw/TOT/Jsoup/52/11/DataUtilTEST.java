package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String expectedCharset = "UTF-8";
        String baseUri = "/path/to/base";
        File file = new File("path/to/file.html");
        Document document = DataUtil.load(file, expectedCharset, baseUri);
        // Test document content
        Element title = document.title();
        assertEquals("Expected title", title.text());

        // Test charset detection
        String actualCharset = document.outputSettings().charset();
        assertEquals("Expected charset", expectedCharset, actualCharset);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String expectedCharset = "GBK";
        String baseUri = "/path/to/base";
        String inputStreamContent = "...</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName(expectedCharset)));
        Document document = DataUtil.load(inputStream, expectedCharset, baseUri);
        // Test document content
        Element title = document.title();
        assertEquals("Expected title", title.text(), "Untitled");

        // Test charset detection
        String actualCharset = document.outputSettings().charset();
        assertEquals("Expected charset", expectedCharset, actualCharset);

        // Test invalid charset
        try {
            Document document2 = DataUtil.load(inputStream, "invalidCharset", baseUri);
            fail("Should have thrown an exception");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseByteData() throws IOException {
        String expectedCharset = "UTF-8";
        String baseUri = "/path/to/base";
        String inputStreamContent = "...</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName(expectedCharset)));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document document = DataUtil.parseByteData(byteData, expectedCharset, baseUri, Parser.htmlParser());
        // Test document content
        Element title = document.title();
        assertEquals("Expected title", title.text(), "Untitled");

        // Test charset detection
        String actualCharset = document.outputSettings().charset();
        assertEquals("Expected charset", expectedCharset, actualCharset);

        // Test invalid charset
        ByteBuffer byteData2 = DataUtil.emptyByteBuffer();
        byteData2.put("invalidData".getBytes());
        try {
            Document document2 = DataUtil.parseByteData(byteData2, "invalidCharset", baseUri, Parser.htmlParser());
            fail("Should have thrown an exception");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFileContent = "Expected input content";
        String outputFileContent = "Expected output content";
        File inputFile = new File("path/to/input.html");
        File outputFile = new File("path/to/output.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        outputStream.close();

        // Test input file content
        Document document = DataUtil.load(inputFile, "UTF-8", null);
        Element title = document.title();
        assertEquals("Expected title", title.text(), inputFileContent);

        // Test output file content
        Document document2 = DataUtil.load(outputFile, "UTF-8", null);
        Element title2 = document2.title();
        assertEquals("Expected title", title2.text(), outputFileContent);
    }
}