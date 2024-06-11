package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charset = "UTF-8";
        File file = new File("testData/validCharset.html");
        Document document = DataUtil.load(file, charset, null);
        assertNotNull(document);
        assertEquals("Valid HTML Content", document.body().text(), document.body().outerHtml());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charset = "nonExistentCharset";
        File file = new File("testData/validCharset.html");
        Document document = null;
        try {
            document = DataUtil.load(file, charset, null);
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(document);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String htmlContent = "<html><body>Valid InputStream Content</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, charset, null);
        assertNotNull(document);
        assertEquals("Valid InputStream Content", document.body().text(), document.body().outerHtml());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charset = "nonExistentCharset";
        String htmlContent = "<html><body>Valid InputStream Content</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = null;
        try {
            document = DataUtil.load(inputStream, charset, null);
        } finally {
            inputStream.close();
        }
        assertNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charset = "UTF-8";
        String htmlContent = "<html><body>Valid InputStream Content</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = null;
        try {
            document = DataUtil.load(inputStream, charset, null, Parser.xmlParser());
        } finally {
            inputStream.close();
        }
        assertNotNull(document);
        assertEquals("Valid InputStream Content", document.body().text(), document.body().outerHtml());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputText = "Input text to cross streams.";
        String outputText = "Output text to check.";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputText.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputBytes = outputStream.toByteArray();

        assertEquals(outputText, new String(outputBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testDetectCharsetFromBom() {
        String testCases = "Test Cases for DetectCharsetFromBom:\n" +
                "UTF-32: FE FF 00 00\n" +
                "UTF-16: FE FF\n" +
                "UTF-8: EF BBBF";

        for (String testCase : testCases.split("\n")) {
            String charset = null;
            ByteBuffer byteData = ByteBuffer.wrap(testCase.split(":")[1].getBytes());
            BomCharset detectedCharset = DataUtil.detectCharsetFromBom(byteData);
            if (detectedCharset != null) {
                charset = detectedCharset.charset;
            }
            assertEquals(testCase.split(":")[0], charset);
        }
    }
}