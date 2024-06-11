package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        // Given
        File file = new File("testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        InputStream inputStream = new FileInputStream(file);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document expectedDocument = mock(Document.class);
        Parser parser = mock(Parser.class);
        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDocument);

        // When
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Then
        assertEquals(expectedDocument, document);
    }

    @Test
    public void testLoadDocumentFromInputStreamWithDefaultCharset() throws IOException {
        // Given
        String charsetName = null;
        String baseUri = "http://example.com/";
        InputStream inputStream = new ByteArrayInputStream("testData".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document expectedDocument = mock(Document.class);
        Parser parser = mock(Parser.class);

        // When
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Then
        assertEquals(expectedDocument, document);
    }

    @Test
    public void testLoadDocumentFromInputStreamWithSpecifiedCharset() throws IOException {
        // Given
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        InputStream inputStream = new ByteArrayInputStream("testData".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document expectedDocument = mock(Document.class);
        Parser parser = mock(Parser.class);

        // When
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Then
        assertEquals(expectedDocument, document);
    }

    @Test
    public void testParseByteData() throws IOException {
        // Given
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = mock(Parser.class);
        Document document = mock(Document.class);
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'T', 'e', 's', 't', '\u0003'});
        String docData = "Test Data";

        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);

        // When
        Document parsedDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Then
        assertEquals(document, parsedDocument);
    }

    @Test
    public void testDetermineCharsetFromContentType() {
        // Given
        String contentType = "text/html; charset=UTF-8";

        // When
        String charsetName = DataUtil.getCharsetFromContentType(contentType);

        // Then
        assertEquals("UTF-8", charsetName);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream("testData".getBytes());

        // When
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        // Then
        assertEquals("testData".getBytes(), byteData.array());
    }

    @Test
    public void testReadToByteBufferWithEmptyInputStream() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // When
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        // Then
        assertEquals(0, byteData.limit());
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNullInputStream() throws IOException {
        // Given
        InputStream inputStream = null;

        // When
        DataUtil.readToByteBuffer(inputStream);

        // Then
        // Exception should be thrown
    }
}