package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataUtilTest {

    private DataUtil dataUtil;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadWithFile() throws Exception {
        File file = mock(File.class);
        FileInputStream inStream = mock(FileInputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document expected = mock(Document.class);

        whenNew(FileInputStream.class).withArguments(file).thenReturn(inStream);
        when(dataUtil.readToByteBuffer(inStream)).thenReturn(byteData);
        when(dataUtil.parseByteData(byteData, "UTF-8", "baseUri", Parser.htmlParser())).thenReturn(expected);

        Document result = dataUtil.load(file, "UTF-8", "baseUri");

        assertEquals(expected, result);
        verify(inStream).close();
    }

    @Test
    public void testLoadWithInputStream() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document expected = mock(Document.class);

        when(dataUtil.readToByteBuffer(inputStream)).thenReturn(byteData);
        when(dataUtil.parseByteData(byteData, "UTF-8", "baseUri", Parser.htmlParser())).thenReturn(expected);

        Document result = dataUtil.load(inputStream, "UTF-8", "baseUri");

        assertEquals(expected, result);
    }

    @Test
    public void testLoadWithInputStreamAndParser() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document expected = mock(Document.class);
        Parser parser = mock(Parser.class);

        when(dataUtil.readToByteBuffer(inputStream)).thenReturn(byteData);
        when(dataUtil.parseByteData(byteData, "UTF-8", "baseUri", parser)).thenReturn(expected);

        Document result = dataUtil.load(inputStream, "UTF-8", "baseUri", parser);

        assertEquals(expected, result);
    }

    @Test
    public void testParseByteDataWithNullCharset() throws Exception {
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);
        Element meta = mock(Element.class);
        Document expected = mock(Document.class);
        Parser parser = mock(Parser.class);

        when(Charset.forName("UTF-8").decode(byteData)).thenReturn(byteData);
        when(parser.parseInput(byteData.toString(), "baseUri")).thenReturn(doc);
        when(doc.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(meta);
        when(meta.attr("content")).thenReturn("text/html;charset=gb2312");
        when(dataUtil.getCharsetFromContentType("text/html;charset=gb2312")).thenReturn("gb2312");
        when(Charset.forName("gb2312").decode(byteData)).thenReturn(byteData);
        when(parser.parseInput(byteData.toString(), "baseUri")).thenReturn(expected);
        when(expected.outputSettings()).thenReturn(mock(OutputSettings.class));

        Document result = dataUtil.parseByteData(byteData, null, "baseUri", parser);

        assertEquals(expected, result);
    }

    @Test
    public void testParseByteDataWithCharset() throws Exception {
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);
        Document expected = mock(Document.class);
        Parser parser = mock(Parser.class);

        when(Charset.forName("UTF-8").decode(byteData)).thenReturn(byteData);
        when(parser.parseInput(byteData.toString(), "baseUri")).thenReturn(doc);
        when(doc.outputSettings()).thenReturn(mock(OutputSettings.class));

        Document result = dataUtil.parseByteData(byteData, "UTF-8", "baseUri", parser);

        assertEquals(expected, result);
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        InputStream inStream = mock(InputStream.class);
        byte[] buffer = new byte[DataUtil.bufferSize];
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        int read = -1;
        ByteBuffer byteData = mock(ByteBuffer.class);

        when(inStream.read(buffer)).thenReturn(read);
        whenNew(ByteArrayOutputStream.class).withArguments(DataUtil.bufferSize).thenReturn(outStream);
        when(outStream.toByteArray()).thenReturn(buffer);
        when(ByteBuffer.wrap(buffer)).thenReturn(byteData);

        ByteBuffer result = dataUtil.readToByteBuffer(inStream);

        assertEquals(byteData, result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithCharset() {
        String contentType = "text/html;charset=EUC-JP";
        String expected = "EUC-JP";

        String result = dataUtil.getCharsetFromContentType(contentType);

        assertEquals(expected, result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String contentType = "text/html";
        String expected = null;

        String result = dataUtil.getCharsetFromContentType(contentType);

        assertEquals(expected, result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithWhitespace() {
        String contentType = "text/html; charset = EUC-JP";
        String expected = "EUC-JP";

        String result = dataUtil.getCharsetFromContentType(contentType);

        assertEquals(expected, result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithQuotes() {
        String contentType = "text/html; charset=\"EUC-JP\"";
        String expected = "EUC-JP";

        String result = dataUtil.getCharsetFromContentType(contentType);

        assertEquals(expected, result);
    }

}