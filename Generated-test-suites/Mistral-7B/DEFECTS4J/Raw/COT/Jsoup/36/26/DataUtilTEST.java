package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {

    private InputStream inputStreamMock;
    private Parser parserMock;
    private Document documentMock;

    @Before
    public void setup() {
        inputStreamMock = mock(InputStream.class);
        parserMock = mock(Parser.class);
        documentMock = mock(Document.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("testFile.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("html content".getBytes());
        FileInputStream inputStream = new FileInputStream(file);
        when(inputStreamMock.readAllBytes()).thenReturn(outputStream.toByteArray());

        Charset charset = Charset.forName("UTF-8");
        String baseUri = "baseUri";

        Document result = DataUtil.load(file, charset != null ? charset.name() : null, baseUri);

        assertEquals(documentMock, result); // replace with actual assertion
        Mockito.verify(parserMock).parseInput(ArgumentMatchers.anyString(), ArgumentMatchers.eq(baseUri));
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("html content".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "baseUri";

        Document result = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals(documentMock, result); // replace with actual assertion
        Mockito.verify(parserMock).parseInput(ArgumentMatchers.anyString(), ArgumentMatchers.eq(baseUri), ArgumentMatchers.eq(parserMock));
    }

    @Test
    public void testLoadInputStreamWithoutCharset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("html content".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String baseUri = "baseUri";

        Document result = DataUtil.load(inputStream, null, baseUri);

        assertEquals(documentMock, result); // replace with actual assertion
        Mockito.verify(parserMock).parseInput(ArgumentMatchers.anyString(), ArgumentMatchers.eq(baseUri), ArgumentMatchers.eq(parserMock));
    }

    @Test
    public void testParseByteDataWithCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("html content".getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "baseUri";

        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parserMock);

        assertEquals(documentMock, result); // replace with actual assertion
        Mockito.verifyNoMoreInteractions(parserMock);
    }

    @Test
    public void testParseByteDataWithoutCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("html content".getBytes());
        String baseUri = "baseUri";

        Document result = DataUtil.parseByteData(byteData, null, baseUri, parserMock);

        assertEquals(documentMock, result); // replace with actual assertion
        Mockito.verify(parserMock).parseInput(ArgumentMatchers.anyString(), ArgumentMatchers.eq(baseUri), ArgumentMatchers.eq(parserMock));
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("html content".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        int maxSize = 10;

        ByteBuffer result = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertEquals(maxSize > 0 ? maxSize : ByteBuffer.ALL_UNIT_SIZE, result.limit()); // replace with actual assertion
        Mockito.verify(inputStreamMock).read(ArgumentMatchers.any(byte[][].class));
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("html content".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        ByteBuffer result = DataUtil.readToByteBuffer(inputStream);

        assertEquals(ByteBuffer.ALL_UNIT_SIZE, result.limit()); // replace with actual assertion
        Mockito.verify(inputStreamMock).readAllBytes();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", result); // replace with actual assertion
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalidCharset";

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertNull(result); // replace with actual assertion
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFileWithNegativeMaxSize() throws IOException {
        File file = new File("testFile.html");
        Charset charset = Charset.forName("UTF-8");
        String baseUri = "baseUri";

        DataUtil.load(file, charset != null ? charset.name() : null, baseUri, -1);
    }
}