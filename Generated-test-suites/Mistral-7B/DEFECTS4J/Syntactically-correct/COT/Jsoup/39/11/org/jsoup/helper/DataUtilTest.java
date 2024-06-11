package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String fileContent = "Your HTML content here";
        File file = new File("test.html");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document document = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(document);

        file.delete();
    }

    @Test
    public void testLoadDocumentFromInputStreamWithNullCharset() throws IOException {
        String htmlContent = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        try {
            Document document = DataUtil.load(inputStream, null, null);
            Assert.assertNotNull(document);
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testLoadDocumentFromInputStreamWithValidCharset() throws IOException {
        String htmlContent = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(document);

        inputStream.close();
    }

    @Test
    public void testLoadDocumentFromInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        try {
            Document document = DataUtil.load(inputStream, "INVALID_CHARSET", null);
            Assert.assertNull(document);
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testLoadDocumentFromInputStreamWithIllegalCharsetNameException() throws IOException {
        String htmlContent = "Your HTML content here";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        try {
            Document document = DataUtil.load(inputStream, "x\u0061x\u0061x\u0061", null);
            Assert.assertNull(document);
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testParseByteDataWithValidCharset() {
        String validHtmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Valid content</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(validHtmlContent.getBytes());

        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String htmlContent = "<html><head></head><body>Default content</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());

        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String htmlContent = "<html><head><meta charset=\"InvalidCharset\"></head><body>Invalid content</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());

        try {
            Document document = DataUtil.parseByteData(byteData, "InvalidCharset", null, Parser.htmlParser());
            Assert.assertNull(document);
        } finally {
            byteData.rewind();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(DataUtil.defaultCharset, charset);
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=x\u0061x\u0061x\u0061";
        DataUtil.getCharsetFromContentType(contentType);
    }
}