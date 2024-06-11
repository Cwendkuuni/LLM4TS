package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Document;
import org.jsoup.HelperException;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil methods.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     */
    @Test
    public void testLoadFile() throws IOException {
        String xmlContent = "<xml version='1.0' encoding='UTF-8'>" +
                "<root><child>Hello World</child></root>";

        File testFile = new File("testFile.xml");
        Files.write(testFile.getAbsoluteFile(), xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(testFile, null, null);
        Assert.assertNotNull(document);
        Assert.assertEquals(DocumentType.XML_DECL, document.documentType());
        Assert.assertEquals("<xml version='1.0' encoding='UTF-8'", document.body().html());

        Files.delete(testFile.getAbsoluteFile());
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     */
    @Test
    public void testLoadInputStream() throws IOException, HelperException {
        String xmlContent = "<xml version='1.0' encoding='UTF-8'>" +
                "<root><child>Hello World</child></root>";

        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(DocumentType.XML_DECL, document.documentType());
        Assert.assertEquals("<xml version='1.0' encoding='UTF-8'", document.body().html());

        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException, HelperException {
        String htmlContent = "<html><head><meta charset='UTF-8'></head><body>" +
                "<h1>Test Page</h1>" +
                "</body></html>";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("html", document.documentType().nodeName());
        Assert.assertEquals("<h1>Test Page</h1>", document.body().html());

        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String, Parser)}.
     */
    @Test
    public void testParseInputStream() throws IOException, HelperException {
        String xmlContent = "<xml version='1.0' encoding='UTF-8'>" +
                "<root><child>Hello World</child></root>";

        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(DocumentType.XML_DECL, document.documentType());
        Assert.assertEquals("<xml version='1.0' encoding='UTF-8'", document.body().html());

        inputStream.close();
    }

    /**
     * Test method for {@link DataUtil#crossStreams(InputStream, OutputStream)}.
     */
    @Test
    public void testCrossStreams() throws IOException {
        String inputText = "Input: Hello World";
        String outputText = "Output: Hello World";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(inputText.getBytes(StandardCharsets.UTF_8));

        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertEquals(outputText, outputStream.toString(StandardCharsets.UTF_8.name()));

        inputStream.close();
        outputStream.close();
    }

    /**
     * Test method for edge cases and exceptions.
     */
    @Test
    public void testEdgeCasesAndExceptions() throws IOException, HelperException {
        // Test null file
        Assert.assertThrows(IllegalArgumentException.class, () -> DataUtil.load(null, null, null));

        // Test empty file
        File emptyFile = new File("emptyFile.txt");
        Assert.assertThrows(IOException.class, () -> DataUtil.load(emptyFile, null, null));

        // Test invalid charset
        File invalidCharsetFile = new File("invalidCharsetFile.txt");
        Files.write(invalidCharsetFile.getAbsoluteFile(), "text/html; charset=x-abcd".getBytes(StandardCharsets.UTF_8));
        Assert.assertThrows(IllegalArgumentException.class, () -> DataUtil.load(invalidCharsetFile, null, null));

        // Test empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        Document document = DataUtil.parseInputStream(emptyInputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("", document.html());
        emptyInputStream.close();

        // Test invalid UTF-8 byte
        String invalidUtf8Content = "Invalid UTF-8 byte: Ã";
        InputStream invalidUtf8InputStream = new ByteArrayInputStream(invalidUtf8Content.getBytes(StandardCharsets.UTF_8));
        Assert.assertThrows(IOException.class, () -> DataUtil.parseInputStream(invalidUtf8InputStream, null, null, Parser.htmlParser()));

        // Test invalid input stream
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[1025]);
        Assert.assertThrows(IOException.class, () -> DataUtil.parseInputStream(invalidInputStream, null, null, Parser.htmlParser()));
    }
}