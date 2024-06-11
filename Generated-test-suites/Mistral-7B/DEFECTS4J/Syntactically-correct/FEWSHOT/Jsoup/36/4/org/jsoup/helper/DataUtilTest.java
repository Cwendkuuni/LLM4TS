package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        // Given
        String fileContent = "html content here";
        File file = new File("test.html");
        FileOutputStream out = new FileOutputStream(file);
        out.write(fileContent.getBytes());
        out.close();

        // When
        Document result = DataUtil.load(file, null, null);

        // Then
        assertNotNull(result);
        assertEquals(fileContent, result.html());

        // Cleanup
        file.delete();
    }

    @Test
    public void testLoadFileWithCharset() throws IOException {
        // Given
        String fileContent = "html content here";
        String charset = "UTF-16";
        File file = new File("test.html");
        FileOutputStream out = new FileOutputStream(file);
        out.write(fileContent.getBytes(Charset.forName(charset)));
        out.close();

        // When
        Document result = DataUtil.load(file, charset, null);

        // Then
        assertNotNull(result);
        assertEquals(fileContent, result.html());

        // Cleanup
        file.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Given
        String inputStreamContent = "html content here";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        // When
        Document result = DataUtil.load(inputStream, null, null);

        // Then
        assertNotNull(result);
        assertEquals(inputStreamContent, result.html());
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        // Given
        String inputStreamContent = "html content here";
        String charset = "UTF-16";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName(charset)));

        // When
        Document result = DataUtil.load(inputStream, charset, null);

        // Then
        assertNotNull(result);
        assertEquals(inputStreamContent, result.html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Given
        String inputStreamContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        Parser parser = mock(Parser.class);

        // When
        Document result = DataUtil.load(inputStream, null, null, parser);

        // Then
        when(parser.parseInput(anyString(), anyString())).thenReturn(new Document("Test"));
        assertEquals(new Document("Test"), result);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Given
        String inputStreamContent = "html content here";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        // When
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Then
        byte[] expectedByteArray = inputStreamContent.getBytes();
        byte[] actualByteArray = new byte[byteBuffer.limit()];
        byteBuffer.get(actualByteArray);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        // Given
        String inputStreamContent = "html content here";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        // When
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);

        // Then
        assertEquals(10, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Given
        String contentType = "text/html; charset=UTF-8";

        // When
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Then
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        // Given
        String contentType = "text/html; charset=invalidCharset";

        // When
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Then
        assertNull(charset);
    }

    @Test
    public void testParseByteData() throws IOException {
        // Given
        String inputStreamContent = "html content here";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        Parser parser = mock(Parser.class);

        // When
        Document result = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "UTF-8", null, parser);

        // Then
        when(parser.parseInput(anyString(), anyString())).thenReturn(new Document("Test"));
        assertEquals(new Document("Test"), result);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        // Given
        String inputStreamContent = "html content here";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        Parser parser = mock(Parser.class);

        // When
        Document result = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, null, parser);

        // Then
        assertEquals(null, result);
    }

    @Test
    public void testParseByteDataWithMetaCharset() throws IOException {
        // Given
        String inputStreamContent = "<html><head><meta charset=\"UTF-16\"></head><body>html content here</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        Parser parser = mock(Parser.class);

        // When
        Document result = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, null, parser);

        // Then
        when(parser.parseInput(anyString(), anyString())).thenReturn(new Document("Test"));
        assertEquals(new Document("Test"), result);
    }
}