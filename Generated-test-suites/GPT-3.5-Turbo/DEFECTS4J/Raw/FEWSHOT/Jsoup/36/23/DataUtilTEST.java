import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = mock(File.class);
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        FileInputStream inStream = mock(FileInputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document expected = mock(Document.class);

        when(file.getAbsolutePath()).thenReturn("path/to/file");
        when(file.length()).thenReturn(100L);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        when(inStream.read(any(byte[].class))).thenReturn(100).thenReturn(-1);
        when(byteData.array()).thenReturn(new byte[100]);
        when(byteData.limit()).thenReturn(100);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser())).thenReturn(expected);
        whenNew(FileInputStream.class).withArguments(file).thenReturn(inStream);

        Document result = DataUtil.load(file, charsetName, baseUri);

        verify(file).getAbsolutePath();
        verify(file).length();
        verify(file).exists();
        verify(file).isFile();
        verify(file).canRead();
        verifyNew(FileInputStream.class).withArguments(file);
        verify(inStream, times(2)).read(any(byte[].class));
        verify(byteData).array();
        verify(byteData).limit();
        verify(DataUtil).parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        assertEquals(expected, result);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document expected = mock(Document.class);

        when(in.read(any(byte[].class))).thenReturn(100).thenReturn(-1);
        when(byteData.array()).thenReturn(new byte[100]);
        when(byteData.limit()).thenReturn(100);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser())).thenReturn(expected);
        when(DataUtil.readToByteBuffer(in)).thenReturn(byteData);

        Document result = DataUtil.load(in, charsetName, baseUri);

        verify(in, times(2)).read(any(byte[].class));
        verify(byteData).array();
        verify(byteData).limit();
        verify(DataUtil).parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        assertEquals(expected, result);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document expected = mock(Document.class);
        Parser parser = mock(Parser.class);

        when(in.read(any(byte[].class))).thenReturn(100).thenReturn(-1);
        when(byteData.array()).thenReturn(new byte[100]);
        when(byteData.limit()).thenReturn(100);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(expected);
        when(DataUtil.readToByteBuffer(in)).thenReturn(byteData);

        Document result = DataUtil.load(in, charsetName, baseUri, parser);

        verify(in, times(2)).read(any(byte[].class));
        verify(byteData).array();
        verify(byteData).limit();
        verify(DataUtil).parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals(expected, result);
    }

    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = mock(Parser.class);
        String docData = "<html><head></head><body></body></html>";
        Document expected = mock(Document.class);

        when(charsetName == null).thenReturn(false);
        when(byteData.array()).thenReturn(docData.getBytes());
        when(byteData.limit()).thenReturn(docData.length());
        when(Charset.forName(charsetName).decode(byteData)).thenReturn(Charset.forName(charsetName).decode(byteData));
        when(parser.parseInput(docData, baseUri)).thenReturn(expected);

        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        verify(charsetName == null).thenReturn(false);
        verify(byteData).array();
        verify(byteData).limit();
        verify(Charset.forName(charsetName)).decode(byteData);
        verify(parser).parseInput(docData, baseUri);

        assertEquals(expected, result);
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = null;
        String baseUri = "https://example.com";
        Parser parser = mock(Parser.class);
        String docData = "<html><head></head><body></body></html>";
        Document expected = mock(Document.class);
        Element meta = mock(Element.class);

        when(charsetName == null).thenReturn(true);
        when(byteData.array()).thenReturn(docData.getBytes());
        when(byteData.limit()).thenReturn(docData.length());
        when(Charset.forName(DataUtil.defaultCharset).decode(byteData)).thenReturn(Charset.forName(DataUtil.defaultCharset).decode(byteData));
        when(parser.parseInput(docData, baseUri)).thenReturn(expected);
        when(expected.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(meta);
        when(meta.hasAttr("http-equiv")).thenReturn(true);
        when(meta.attr("content")).thenReturn("text/html; charset=UTF-8");
        when(DataUtil.getCharsetFromContentType("text/html; charset=UTF-8")).thenReturn("UTF-8");
        when(Charset.forName("UTF-8").decode(byteData)).thenReturn(Charset.forName("UTF-8").decode(byteData));

        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        verify(charsetName == null).thenReturn(true);
        verify(byteData).array();
        verify(byteData).limit();
        verify(Charset.forName(DataUtil.defaultCharset)).decode(byteData);
        verify(parser, times(2)).parseInput(docData, baseUri);
        verify(expected).select("meta[http-equiv=content-type], meta[charset]");
        verify(meta).hasAttr("http-equiv");
        verify(meta).attr("content");
        verify(DataUtil).getCharsetFromContentType("text/html; charset=UTF-8");
        verify(Charset.forName("UTF-8")).decode(byteData);

        assertEquals(expected, result);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inStream = mock(InputStream.class);
        int maxSize = 100;
        ByteBuffer byteData = mock(ByteBuffer.class);

        when(maxSize >= 0).thenReturn(true);
        when(DataUtil.bufferSize).thenReturn(0x20000);
        when(DataUtil.readToByteBuffer(inStream, maxSize)).thenCallRealMethod();
        when(inStream.read(any(byte[].class))).thenReturn(100).thenReturn(-1);
        when(byteData.array()).thenReturn(new byte[100]);
        when(byteData.limit()).thenReturn(100);

        ByteBuffer result = DataUtil.readToByteBuffer(inStream, maxSize);

        verify(maxSize >= 0).thenReturn(true);
        verify(DataUtil).bufferSize;
        verify(inStream, times(2)).read(any(byte[].class));
        verify(byteData).array();
        verify(byteData).limit();

        assertEquals(byteData, result);
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream inStream = mock(InputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);

        when(DataUtil.readToByteBuffer(inStream, 0)).thenReturn(byteData);

        ByteBuffer result = DataUtil.readToByteBuffer(inStream);

        verify(DataUtil).readToByteBuffer(inStream, 0);

        assertEquals(byteData, result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithCharset() {
        String contentType = "text/html; charset=UTF-8";

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String contentType = "text/html";

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertNull(result);
    }

    @Test
    public void testCase1() throws IOException {
        InputStream in = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes());
        Parser parser = Parser.htmlParser();
        Document expected = mock(Document.class);

        when(in.read(any(byte[].class))).thenReturn(100).thenReturn(-1);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(expected);
        when(DataUtil.readToByteBuffer(in)).thenReturn(byteData);

        Document result = DataUtil.load(in, charsetName, baseUri, parser);

        verify(in, times(2)).read(any(byte[].class));
        verify(DataUtil).parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals(expected, result);
    }
}