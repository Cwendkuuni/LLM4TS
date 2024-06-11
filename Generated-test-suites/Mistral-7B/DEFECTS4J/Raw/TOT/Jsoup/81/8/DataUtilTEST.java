package org.jsoup.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private Document htmlDocument;
    private Document xmlDocument;

    @BeforeEach
    public void setUp() {
        final String htmlData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        htmlDocument = DataUtil.load(new ByteArrayInputStream(htmlData.getBytes(StandardCharsets.UTF_8)), "UTF-8", null);

        final String xmlData = "<?xml version='1.0' encoding='UTF-8'?><root><element>Hello</element></root>";
        xmlDocument = DataUtil.load(new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8)), "UTF-8", null, Parser.xmlParser());
    }

    @Test
    public void testLoadFileWithUTF8Charset() throws IOException {
        // Given: A file with UTF-8 encoding
        final String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        final File testFile = new File("test.html");
        Files.write(testFile.toPath(), fileContent.getBytes(StandardCharsets.UTF_8));

        // When: DataUtil.load() is called with the file and UTF-8 charset
        Document document = DataUtil.load(testFile, "UTF-8", null);

        // Then: Document should not be null and contain the expected HTML content
        Assertions.assertNotNull(document);
        Assertions.assertEquals(htmlDocument.outerHtml(), document.outerHtml());

        // Cleanup: Delete the test file
        testFile.delete();
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        // Given: A test input stream with an unsupported charset
        InputStream invalidInputStream = new ByteArrayInputStream("Invalid charset data".getBytes("GBK"));

        // When: DataUtil.load() is called with the invalid input stream
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataUtil.load(invalidInputStream, "GBK", null));

        // Cleanup: Close the test input stream
        invalidInputStream.close();
    }

    @Test
    public void testLoadInputStreamWithEmptyContent() throws IOException {
        // Given: An empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);

        // When: DataUtil.load() is called with the empty input stream
        Document document = DataUtil.load(emptyInputStream, null, null);

        // Then: Document should not be null and contain no content
        Assertions.assertNotNull(document);
        Assertions.assertTrue(document.childNodeSize() == 0);

        // Cleanup: Close the test input stream
        emptyInputStream.close();
    }

    @Test
    public void testParseInputStreamWithUTF8BOM() throws IOException {
        // Given: A test input stream containing a UTF-8 BOM
        final String bomData = "\uFEFF<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        InputStream bomInputStream = new ByteArrayInputStream(bomData.getBytes(StandardCharsets.UTF_8));

        // When: DataUtil.parseInputStream() is called with the BOM input stream
        Document document = DataUtil.parseInputStream(bomInputStream, "UTF-8", null, Parser.htmlParser());

        // Then: Document should not be null and contain the expected HTML content
        Assertions.assertNotNull(document);
        Assertions.assertEquals(htmlDocument.outerHtml(), document.outerXml());

        // Cleanup: Close the test input stream
        bomInputStream.close();
    }

    @Test
    public void testParseInputStreamWithXMLBOM() throws IOException {
        // Given: A test input stream containing an XML BOM
        final String xmlBomData = "<?xml version='1.0' encoding='UTF-8'?><root><element>Hello</element></root>";
        InputStream xmlBomInputStream = new ByteArrayInputStream(xmlBomData.getBytes(StandardCharsets.UTF_8));

        // When: DataUtil.parseInputStream() is called with the XML BOM input stream
        Document document = DataUtil.parseInputStream(xmlBomInputStream, "UTF-8", null, Parser.xmlParser());

        // Then: Document should not be null and contain the expected XML content
        Assertions.assertNotNull(document);
        Assertions.assertEquals(xmlDocument.outerXml(), document.outerXml());

        // Cleanup: Close the test input stream
        xmlBomInputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidXMLBOM() throws IOException {
        // Given: A test input stream containing an invalid XML BOM
        final String invalidXmlBomData = "<root><element>Hello</element>"; // Missing XML declaration
        InputStream invalidXmlBomInputStream = new ByteArrayInputStream(invalidXmlBomData.getBytes(StandardCharsets.UTF_8));

        // When: DataUtil.parseInputStream() is called with the invalid XML BOM input stream
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(invalidXmlBomInputStream, "UTF-8", null, Parser.xmlParser()));

        // Cleanup: Close the test input stream
        invalidXmlBomInputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Given: Two test input and output streams
        final String testData = "Test Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        // When: DataUtil.crossStreams() is called with the input and output streams
        DataUtil.crossStreams(inputStream, outputStream);

        // Then: Output stream should contain the expected data
        Assertions.assertEquals(testData, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));

        // Cleanup: Close the test input and output streams
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Given: A test input stream
        final String testData = "Test Data";
        InputStream testInputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        // When: DataUtil.readToByteBuffer() is called with the test input stream
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, 1024);

        // Then: Byte buffer should contain the expected data
        Assertions.assertTrue(byteBuffer.hasRemaining());
        final byte[] expectedData = testData.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < expectedData.length; i++) {
            Assertions.assertEquals(expectedData[i], byteBuffer.get(i));
        }

        // Cleanup: Close the test input stream
        testInputStream.close();
    }
}