package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {
    
    @Test
    public void testLoad_withFileExists() throws IOException {
        File file = mock(File.class);
        FileInputStream inStream = mock(FileInputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("mocked/file/path");
        whenNew(FileInputStream.class).withArguments(file).thenReturn(inStream);
        when(DataUtil.readToByteBuffer(inStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser())).thenReturn(doc);

        Document result = DataUtil.load(file, charsetName, baseUri);

        assertEquals(doc, result);
    }
    
    @Test(expected = IOException.class)
    public void testLoad_withFileNotExists() throws IOException {
        File file = mock(File.class);
        FileInputStream inStream = mock(FileInputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        when(file.exists()).thenReturn(false);

        Document result = DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoad_withInputStream() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        when(DataUtil.readToByteBuffer(inputStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser())).thenReturn(doc);

        Document result = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals(doc, result);
        // Verify that the inputStream is closed
        verify(inputStream).close();
    }

    @Test(expected = IOException.class)
    public void testLoad_withInputStream_IOException() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        when(DataUtil.readToByteBuffer(inputStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser())).thenThrow(new IOException());

        Document result = DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testLoad_withInputStreamAndParser() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        when(DataUtil.readToByteBuffer(inputStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(doc);

        Document result = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertEquals(doc, result);
        // Verify that the inputStream is closed
        verify(inputStream).close();
    }

    @Test(expected = IOException.class)
    public void testLoad_withInputStreamAndParser_IOException() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        when(DataUtil.readToByteBuffer(inputStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenThrow(new IOException());

        Document result = DataUtil.load(inputStream, charsetName, baseUri, parser);
    }

    @Test
    public void testParseByteData_withCharsetIsNull() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = mock(Document.class);
        Element meta = mock(Element.class);
        String contentType = "text/html; charset=UTF-8";

        when(Charset.forName(DataUtil.defaultCharset).decode(byteData)).thenReturn(CharBuffer.wrap("mockedDocData"));
        when(parser.parseInput("mockedDocData", baseUri)).thenReturn(doc);
        when(doc.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(Collections.singletonList(meta));
        when(meta.attr("http-equiv")).thenReturn("Content-Type");
        when(meta.attr("content")).thenReturn(contentType);
        when(DataUtil.getCharsetFromContentType(contentType)).thenReturn("UTF-8");
        when(Charset.forName("UTF-8").decode(byteData)).thenReturn(CharBuffer.wrap("newMockedDocData"));
        when(parser.parseInput("newMockedDocData", baseUri)).thenReturn(doc);
        when(doc.outputSettings()).thenReturn(outputSettings);
        when(outputSettings.charset("UTF-8")).thenReturn(doc);

        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals(doc, result);
    }

    @Test
    public void testParseByteData_withCharsetIsNotNull() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = mock(Document.class);
        Element meta = mock(Element.class);

        when(Charset.forName(charsetName).decode(byteData)).thenReturn(CharBuffer.wrap("mockedDocData"));
        when(parser.parseInput("mockedDocData", baseUri)).thenReturn(doc);
        when(doc.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(Collections.singletonList(meta));
        when(meta.attr("http-equiv")).thenReturn("Content-Type");
        when(DataUtil.getCharsetFromContentType(any(String.class))).thenReturn("UTF-8");
        when(Charset.forName("UTF-8").decode(byteData)).thenReturn(CharBuffer.wrap("newMockedDocData"));
        when(parser.parseInput("newMockedDocData", baseUri)).thenReturn(doc);
        when(doc.outputSettings()).thenReturn(outputSettings);
        when(outputSettings.charset("UTF-8")).thenReturn(doc);

        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals(doc, result);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = mock(InputStream.class);
        byte[] buffer = new byte[DataUtil.bufferSize];
        byte[] mockedByteArray = "mockedByteArray".getBytes();
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        int read = 0;

        when(inStream.read(buffer)).thenReturn(mockedByteArray.length).thenReturn(-1);
        whenNew(ByteArrayOutputStream.class).withArguments(DataUtil.bufferSize).thenReturn(outStream);
        when(outStream.toByteArray()).thenReturn(mockedByteArray);
        when(ByteBuffer.wrap(mockedByteArray)).thenReturn(ByteBuffer.wrap(mockedByteArray));

        ByteBuffer result = DataUtil.readToByteBuffer(inStream);

        assertEquals(ByteBuffer.wrap(mockedByteArray), result);
    }

    @Test
    public void testGetCharsetFromContentType_withContentTypeIsNull() {
        String contentType = null;

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertNull(result);
    }

    @Test
    public void testGetCharsetFromContentType_withContentTypeDoesNotContainCharset() {
        String contentType = "text/html";

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertNull(result);
    }

    @Test
    public void testGetCharsetFromContentType_withContentTypeContainsCharset() {
        String contentType = "text/html; charset=UTF-8";

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", result);
    }
}