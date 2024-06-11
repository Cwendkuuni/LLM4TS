package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private DataUtil dataUtil;
    private Parser parser;
    private Document document;

    @Before
    public void setup() {
        dataUtil = new DataUtil();
        parser = mock(Parser.class);
        document = mock(Document.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        byte[] fileContents = "Test file contents".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(fileContents);

        when(parser.parseInput(any(String.class), any(String.class))).thenReturn(document);

        Document result = dataUtil.load(file, charset, baseUri);

        assertNotNull(result);
        assertEquals("Test file contents", result.body().html());
        Mockito.verify(parser).parseInput(any(String.class), any(String.class));
        Mockito.verifyNoMoreInteractions(parser);
        inputStream.close();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        byte[] inputStreamContents = "Test input stream contents".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputStreamContents);

        when(parser.parseInput(any(InputStream.class), any(String.class), any(String.class))).thenReturn(document);

        Document result = dataUtil.load(inputStream, charset, baseUri);

        assertNotNull(result);
        assertEquals("Test input stream contents", result.body().html());
        Mockito.verify(parser).parseInput(any(InputStream.class), any(String.class), any(String.class));
        Mockito.verifyNoMoreInteractions(parser);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.xmlParser.class);

        byte[] inputStreamContents = "Test input stream contents".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputStreamContents);

        when(parser.parseInput(any(InputStream.class), any(String.class), any(String.class))).thenReturn(document);

        Document result = dataUtil.load(inputStream, charset, baseUri, parser);

        assertNotNull(result);
        assertEquals("Test input stream contents", result.body().html());
        Mockito.verify(parser).parseInput(any(InputStream.class), any(String.class), any(String.class));
        Mockito.verifyNoMoreInteractions(parser);
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test input stream contents".getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        dataUtil.crossStreams(inputStream, outputStream);

        byte[] result = outputStream.toByteArray();
        assertEquals("Test input stream contents", new String(result, StandardCharsets.UTF_8));

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        byte[] inputStreamContents = "Test input stream contents".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputStreamContents);

        when(parser.parseInput(any(InputStream.class), any(String.class), any(String.class))).thenReturn(document);

        Document result = dataUtil.parseInputStream(inputStream, charset, baseUri, parser);

        assertNotNull(result);
        assertEquals("Test input stream contents", result.body().html());
        Mockito.verify(parser).parseInput(any(InputStream.class), any(String.class), any(String.class));
        Mockito.verifyNoMoreInteractions(parser);
        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test input stream contents".getBytes(StandardCharsets.UTF_8));
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap("Test input stream contents".getBytes(StandardCharsets.UTF_8));

        ByteBuffer result = dataUtil.readToByteBuffer(inputStream, -1);

        assertEquals(expectedByteBuffer.limit(), result.limit());
        assertEquals(expectedByteBuffer.capacity(), result.capacity());
        assertEquals(expectedByteBuffer.position(), result.position());
        assertEquals(expectedByteBuffer.remaining(), result.remaining());

        byte[] expectedBytes = new byte[expectedByteBuffer.limit()];
        result.get(expectedBytes);
        assertEquals(new String(expectedBytes, StandardCharsets.UTF_8), "Test input stream contents");

        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String result = dataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", result);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = "text/html";

        String result = dataUtil.getCharsetFromContentType(contentType);

        assertNull(result);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";

        String result = dataUtil.validateCharset(cs);

        assertEquals(cs, result);
    }

    @Test
    public void testValidateCharsetNull() {
        String cs = null;

        String result = dataUtil.validateCharset(cs);

        assertNull(result);
    }

    @Test
    public void testMimeBoundary() {
        String result = dataUtil.mimeBoundary();

        Random random = new Random();
        for (int i = 0; i < 32; i++) {
            assertEquals(mimeBoundaryChars[random.nextInt(mimeBoundaryChars.length)], result.charAt(i));
        }
    }

    @Test
    public void testDetectCharsetFromBomUtf32() throws IOException {
        byte[] bom = new byte[]{0, 0, (byte) 0xFE, (byte) 0xFF};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bom);

        BomCharset result = dataUtil.detectCharsetFromBom(byteBuffer);

        assertEquals("UTF-32", result.charset);
        assertEquals(false, result.offset);
    }

    @Test
    public void testDetectCharsetFromBomUtf16Be() throws IOException {
        byte[] bom = new byte[]{0xFF, 0xFE};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bom);

        BomCharset result = dataUtil.detectCharsetFromBom(byteBuffer);

        assertEquals("UTF-16", result.charset);
        assertEquals(false, result.offset);
    }

    @Test
    public void testDetectCharsetFromBomUtf16Le() throws IOException {
        byte[] bom = new byte[]{0xFE, 0xFF};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bom);

        BomCharset result = dataUtil.detectCharsetFromBom(byteBuffer);

        assertEquals("UTF-16", result.charset);
        assertEquals(true, result.offset);
    }

    @Test
    public void testDetectCharsetFromBomUtf8() throws IOException {
        byte[] bom = new byte[]{0xEF, 0xBB, 0xBF};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bom);

        BomCharset result = dataUtil.detectCharsetFromBom(byteBuffer);

        assertEquals("UTF-8", result.charset);
        assertEquals(true, result.offset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDetectCharsetFromBomInvalidCharset() throws IOException {
        byte[] bom = new byte[]{0xFF, 0xFF};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bom);

        dataUtil.detectCharsetFromBom(byteBuffer);
    }
}