package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtil.BomCharset;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private static InputStream createInputMock(String content, String charset) {
        byte[] contentBytes = content.getBytes(Charset.forName(charset));
        return Mockito.mock(InputStream.class, mock -> mock.when(mock.readAllBytes()).thenReturn(contentBytes));
    }

    private static OutputStream createOutputMock() {
        return Mockito.mock(OutputStream.class);
    }

    private static Document createMockDocument() {
        return Mockito.mock(Document.class);
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File testFile = new File("test.html");
        InputStream input = new FileInputStream(testFile);
        Document document = DataUtil.load(input, null, null);
        // typical usage: assert document is not null
        assertNotNull(document);
        input.close();
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File testFile = new File("test.html");
        InputStream input = new FileInputStream(testFile);
        Document document = DataUtil.load(input, "UTF-16", null);
        // typical usage: assert document is not null
        assertNotNull(document);
        input.close();
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testContent = "test content";
        InputStream input = new ByteArrayInputStream(testContent.getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.load(input, null, null);
        // typical usage: assert document is not null
        assertNotNull(document);
        input.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testContent = "test content";
        InputStream input = new ByteArrayInputStream(testContent.getBytes(Charset.forName("UTF-16")));
        Document document = DataUtil.load(input, "UTF-16", null);
        // typical usage: assert document is not null
        assertNotNull(document);
        input.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testContent = "test content";
        InputStream input = new ByteArrayInputStream(testContent.getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.load(input, null, null, DataUtil.Parser.xmlParser());
        // typical usage: assert document is not null
        assertNotNull(document);
        input.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.html");
        File outputFile = new File("test_output.html");
        InputStream input = new FileInputStream(inputFile);
        OutputStream output = new ByteArrayOutputStream();

        DataUtil.crossStreams(input, output);
        // typical usage: assert no exceptions were thrown
        // edge case: check bytes from input Stream are in output Stream
        InputStream outputStream = new ByteArrayInputStream(output.toByteArray());
        byte[] expected = new byte[input.available()];
        int read = input.read(expected);
        byte[] actual = new byte[outputStream.available()];
        outputStream.read(actual);
        // test for equality
        assertEquals(expected, actual);
        input.close();
        output.close();
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String testContent = "test content";
        InputStream input = new ByteArrayInputStream(testContent.getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.parseInputStream(input, null, null);
        // typical usage: assert document is not null
        assertNotNull(document);
        // edge case: check document content
        assertEquals(testContent, document.body().text());
        input.close();
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String testContent = "test content";
        InputStream input = new ByteArrayInputStream(testContent.getBytes(Charset.forName("UTF-16")));
        Document document = DataUtil.parseInputStream(input, "UTF-16", null);
        // typical usage: assert document is not null
        assertNotNull(document);
        // edge case: check document content
        assertEquals(testContent, document.body().text());
        input.close();
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String testContent = "test content";
        InputStream input = new ByteArrayInputStream(testContent.getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.parseInputStream(input, null, null, DataUtil.Parser.xmlParser());
        // typical usage: assert document is not null
        assertNotNull(document);
        // edge case: check document content
        assertEquals(testContent, document.body().text());
        input.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File testFile = new File("test.html");
        InputStream input = new FileInputStream(testFile);
        ByteBuffer buffer = DataUtil.readToByteBuffer(input, 1024 * 5);
        // typical usage: assert buffer is not empty
        assertEquals(true, buffer.hasRemaining());
        // edge case: check byte size
        assertEquals(input.available(), (int) buffer.limit());
        input.close();
    }

    @Test
    public void testReadToByteBufferWithError() throws IOException {
        InputStream input = Mockito.mock(InputStream.class);
        Mockito.when(input.read()).thenThrow(IOException.class);
        ByteBuffer buffer = DataUtil.readToByteBuffer(input, 1024 * 5);
        // error case: check buffer is empty
        assertEquals(false, buffer.hasRemaining());
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        String testContent = "test content";
        ByteBuffer byteData = ByteBuffer.wrap(testContent.getBytes(Charset.forName("UTF-8")));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        // typical usage: assert bomCharset is not null
        assertNotNull(bomCharset);
        // edge case: check detected charset
        assertEquals("UTF-8", bomCharset.charset);
        // error case: check byte offset
        assertEquals(0, bomCharset.offset);
        byteData.clear();
    }

    @Test
    public void testDetectCharsetFromBomWithError() throws IOException {
        String testContent = "test content";
        ByteBuffer byteData = ByteBuffer.wrap(testContent.getBytes(Charset.forName("UTF-16")));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        // error case: check bomCharset is null
        assertNull(bomCharset);
    }
}