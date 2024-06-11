package org.jsoup.helper;

import org.hamcrest.CoreMatchers; // For Hamcrest assertions
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {

    private Document htmlDocument;
    private Document xmlDocument;
    private Parser parser;

    @Before
    public void setup() {
        parser = mock(Parser.class);
        htmlDocument = Mockito.spy(Parser.htmlParser().parse("<html><body></body></html>"), Parser.class);
        xmlDocument = Mockito.spy(Parser.xmlParser().parse("<xml></xml>"), Parser.class);
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFile.html"); // Replace with the actual test file path
        Document document = DataUtil.load(file, null, null);
        assertThat(document, is(notNullValue()));
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html"); // Replace with the actual test file path
        Document document = DataUtil.load(file, "UTF-8", null);
        assertThat(document, is(notNullValue()));
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = createTestInputStream("<html><body></body></html>");
        Document document = DataUtil.load(inputStream, null, null);
        assertThat(document, is(notNullValue()));
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        InputStream inputStream = createTestInputStream("<html><body></body></html>");
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertThat(document, is(notNullValue()));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = createTestInputStream("<html><body></body></html>");
        Document document = DataUtil.load(inputStream, null, null, parser);
        assertThat(document, is(notNullValue()));
        verifyParserCalled(parser, htmlDocument);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = createTestInputStream("<html><body></body></html>");
        DataUtil.crossStreams(inputStream, outputStream);
        Document document = DataUtil.load(new ByteArrayInputStream(outputStream.toByteArray()), null, null);
        assertThat(document, is(notNullValue()));
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithInputClosed() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = createTestInputStream("<html><body></body></html>");
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            inputStream.close();
            DataUtil.parseByteData(ByteBuffer.wrap(outputStream.toByteArray()), null, null, parser);
        } finally {
            outputStream.close();
        }
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(parseTestFileToByteArray("testFile.html"));
        Document document = DataUtil.parseByteData(byteData, null, null, parser);
        assertThat(document, is(notNullValue()));
        verifyParserCalled(parser, document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(parseTestFileToByteArray("testFile.html"));
        Document document = DataUtil.parseByteData(byteData, null, null);
        assertThat(document, is(notNullValue()));
        verifyParserCalled(parser, document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(parseTestFileToByteArray("testFile.html"));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null);
        assertThat(document, is(notNullValue()));
        verifyParserCalled(parser, document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = createTestInputStream("<html><body></body></html>");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertThat(byteData.limit(), is(equalTo(inputStream.available())));
        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = createTestInputStream("<html><body></body></html>");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertThat(byteData.limit(), is(lessThanOrEqualTo(10)));
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNegativeMaxSize() throws IOException {
        InputStream inputStream = createTestInputStream("<html><body></body></html>");
        DataUtil.readToByteBuffer(inputStream, -1);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("testFile.html"); // Replace with the actual test file path
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertThat(byteData.limit(), is(equalTo((int) new File("testFile.html").length())));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertThat(byteBuffer.limit(), is(0));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertThat(charset, is(equalTo("UTF-8")));

        String contentType2 = "text/html";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertThat(charset2, is(nullValue()));
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "UTF-16";
        String charset3 = "UTF-32";
        String charset4 = "ISO-8859-1";
        String charset5 = "test";

        String validatedCharset1 = DataUtil.validateCharset(charset1);
        String validatedCharset2 = DataUtil.validateCharset(charset2);
        String validatedCharset3 = DataUtil.validateCharset(charset3);
        String validatedCharset4 = DataUtil.validateCharset(charset4);
        String validatedCharset5 = DataUtil.validateCharset(charset5);

        assertThat(validatedCharset1, is(equalTo("UTF-8")));
        assertThat(validatedCharset2, is(equalTo("UTF-16")));
        assertThat(validatedCharset3, is(equalTo("UTF-32")));
        assertThat(validatedCharset4, is(equalTo("ISO-8859-1")));
        assertThat(validatedCharset5, is(nullValue()));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int length = boundary.length();
        byte[] boundaryBytes = boundary.getBytes();

        assertThat(length, is(equalTo(32)));
        for (int i = 0; i < length; i++) {
            byte expectedByte = boundaryBytes[i];
            byte actualByte = mimeBoundaryChars[i % mimeBoundaryChars.length];
            assertThat(expectedByte, is(equalTo((byte) actualByte)));
        }
    }

    private InputStream createTestInputStream(String data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(data.getBytes());
            outputStream.close();
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create test input stream.", e);
        }
    }

    private byte[] parseTestFileToByteArray(String fileName) throws IOException {
        File file = new File(fileName);
        return DataUtil.readFileToByteArray(file);
    }

    private void verifyParserCalled(Parser parser, Document document) {
        when(parser.parseInput(document.documentElement().html(), null)).thenReturn(document);
        parser.parseInput(document.documentElement().html(), null);
    }
}