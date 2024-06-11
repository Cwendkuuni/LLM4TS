import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class DataUtilTest {
    private DataUtil dataUtil;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadWithFile() throws IOException {
        File file = mock(File.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document expected = mock(Document.class);

        FileInputStream inputStream = mock(FileInputStream.class);
        when(inputStream.read()).thenReturn(-1);
        when(file.getName()).thenReturn("test.html");
        when(file.length()).thenReturn(100L);
        when(file.exists()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.lastModified()).thenReturn(System.currentTimeMillis());
        when(file.toURI().toURL().toString()).thenReturn(baseUri);
        whenNew(FileInputStream.class).withArguments(file).thenReturn(inputStream);
        when(dataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser())).thenReturn(expected);

        Document result = dataUtil.load(file, charsetName, baseUri);
  
        assertEquals(expected, result);
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        InputStream input = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document expected = mock(Document.class);

        when(dataUtil.parseInputStream(input, charsetName, baseUri, Parser.htmlParser())).thenReturn(expected);

        Document result = dataUtil.load(input, charsetName, baseUri);
  
        assertEquals(expected, result);
    }

    @Test
    public void testLoadWithInputStreamAndParser() throws IOException {
        InputStream input = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document expected = mock(Document.class);

        when(dataUtil.parseInputStream(input, charsetName, baseUri, parser)).thenReturn(expected);

        Document result = dataUtil.load(input, charsetName, baseUri, parser);
  
        assertEquals(expected, result);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = mock(InputStream.class);
        OutputStream out = mock(OutputStream.class);

        byte[] buffer = new byte[DataUtil.bufferSize];
        when(in.read(buffer)).thenReturn(-1);

        dataUtil.crossStreams(in, out);

        // verify that data was read from input and written to output
        Mockito.verify(in, Mockito.atLeastOnce()).read(buffer);
        Mockito.verify(out, Mockito.times(1)).write(buffer, 0 , buffer.length);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream input = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document expected = mock(Document.class);

        BufferedReader reader = mock(BufferedReader.class);
        InputStreamReader inputStreamReader = mock(InputStreamReader.class);
        whenNew(BufferedReader.class).withArguments(inputStreamReader, DataUtil.bufferSize).thenReturn(reader);
        whenNew(InputStreamReader.class).withArguments(input, charsetName).thenReturn(inputStreamReader);
        when(DatasetUtil.validateCharset(charsetName)).thenReturn(charsetName);
        when(DatasetUtil.parseInput(reader, baseUri)).thenReturn(expected);

        Document result = dataUtil.parseInputStream(input, charsetName, baseUri, parser);
  
        assertEquals(expected, result);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = mock(InputStream.class);
        int maxSize = 512;
        ByteBuffer expected = ByteBuffer.allocate(maxSize);

        when(inStream.read((byte[]) Mockito.any())).thenReturn(-1);

        ByteBuffer result = dataUtil.readToByteBuffer(inStream, maxSize);
  
        assertEquals(expected, result);
    }

    @Test
    public void testMimeBoundary() {
        String expected = "gcAekmXr-2i7JZ380laESdBnfVWhNBi1";

        String result = dataUtil.mimeBoundary();
  
        assertEquals(expected, result);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expected = "UTF-8";

        String result = dataUtil.getCharsetFromContentType(contentType);
  
        assertEquals(expected, result);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        String expected = "UTF-8";

        String result = dataUtil.validateCharset(cs);
  
        assertEquals(expected, result);
    }
}