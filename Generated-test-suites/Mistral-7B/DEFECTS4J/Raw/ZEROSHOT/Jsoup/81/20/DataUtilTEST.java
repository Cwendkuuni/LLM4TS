package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {

    private InputStream inputStream;
    private OutputStream outputStream;
    private Document document;

    @Before
    public void setUp() throws IOException {
        inputStream = mock(InputStream.class);
        outputStream = mock(OutputStream.class);
        document = mock(Document.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        when(inputStream.available()).thenReturn(1024 * 5);
        when(inputStream.read()).thenCallRealMethod();

        ByteBuffer firstBytes = ByteBuffer.allocate(1024 * 5 - 1);
        firstBytes.put((byte) 'A');
        firstBytes.flip();
        when(inputStream).thenReturn(firstBytes.asInputStream());

        ByteBuffer secondBytes = ByteBuffer.allocate(1024 * 32);
        secondBytes.put((byte) 'B').put((byte) 'B').put((byte) 'B');
        secondBytes.flip();
        when(inputStream).thenReturn(secondBytes.asInputStream());

        when(inputStream.close()).thenCallRealMethod();

        Charset charset = Charset.forName("UTF-8");
        when(inputStream.markSupported()).thenReturn(true);

        Document actual = DataUtil.load(file, "UTF-8", "");

        assertEquals(document, actual);

        Mockito.verify(inputStream, Mockito.times(2)).reset();
        Mockito.verify(inputStream, Mockito.times(1)).mark(1024 * 5 - 1);
        Mockito.verify(inputStream, Mockito.times(1)).close();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        when(outputStream.available()).thenReturn(1024 * 32);
        when(outputStream.write(Mockito.anyByte())).thenCallRealMethod();

        ByteBuffer firstBytes = ByteBuffer.allocate(1024 * 5 - 1);
        firstBytes.put((byte) 'A');
        firstBytes.flip();
        when(inputStream).thenReturn(firstBytes.asInputStream());

        ByteBuffer secondBytes = ByteBuffer.allocate(1024 * 32);
        secondBytes.put((byte) 'B').put((byte) 'B').put((byte) 'B');
        secondBytes.flip();
        when(inputStream).thenReturn(secondBytes.asInputStream());

        when(inputStream.close()).thenCallRealMethod();
        when(inputStream.markSupported()).thenReturn(true);

        DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());

        Mockito.verify(inputStream, Mockito.times(3)).reset();
        Mockito.verify(inputStream, Mockito.times(1)).mark(1024 * 5 - 1);
        Mockito.verify(inputStream, Mockito.times(1)).close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        when(outputStream.available()).thenReturn(1024 * 32);
        when(outputStream.write(Mockito.anyByte())).thenCallRealMethod();

        ByteBuffer firstBytes = ByteBuffer.allocate(1024 * 5 - 1);
        firstBytes.put((byte) 'A');
        firstBytes.flip();
        when(inputStream).thenReturn(firstBytes.asInputStream());

        ByteBuffer secondBytes = ByteBuffer.allocate(1024 * 32);
        secondBytes.put((byte) 'B').put((byte) 'B').put((byte) 'B');
        secondBytes.flip();
        when(inputStream).thenReturn(secondBytes.asInputStream());

        when(inputStream.close()).thenCallRealMethod();
        when(inputStream.markSupported()).thenReturn(true);

        Parser parser = Mockito.mock(Parser.class);
        when(parser.parseInput(Mockito.anyString(), Mockito.eq(""))).thenReturn(document);

        DataUtil.load(inputStream, "UTF-8", "", parser);

        Mockito.verify(inputStream, Mockito.times(3)).reset();
        Mockito.verify(inputStream, Mockito.times(1)).mark(1024 * 5 - 1);
        Mockito.verify(inputStream, Mockito.times(1)).close();
        Mockito.verify(parser, Mockito.times(1)).parseInput(Mockito.eq("test.html"), Mockito.eq(""));
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024 * 32];

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals("test.html", new String(outputStream.toByteArray()));
    }

    @Test
    public void testParseInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        when(inputStream.available()).thenReturn(1024 * 5 - 1);
        when(inputStream.read()).thenCallRealMethod();

        ByteBuffer firstBytes = ByteBuffer.allocate(1024 * 5 - 1);
        firstBytes.put((byte) 'A');
        firstBytes.flip();
        when(inputStream).thenReturn(firstBytes.asInputStream());

        ByteBuffer secondBytes = ByteBuffer.allocate(1024 * 32);
        secondBytes.put((byte) 'B').put((byte) 'B').put((byte) 'B');
        secondBytes.flip();
        when(inputStream).thenReturn(secondBytes.asInputStream());

        when(inputStream.close()).thenCallRealMethod();
        when(inputStream.markSupported()).thenReturn(true);

        Charset charset = Charset.forName("UTF-8");

        Document actual = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());

        assertEquals(document, actual);

        Mockito.verify(inputStream, Mockito.times(3)).reset();
        Mockito.verify(inputStream, Mockito.times(1)).mark(1024 * 5 - 1);
        Mockito.verify(inputStream, Mockito.times(1)).close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes());

        ByteBuffer actual = DataUtil.readToByteBuffer(inputStream);

        assertEquals(actual.capacity(), actual.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");

        ByteBuffer actual = DataUtil.readFileToByteBuffer(file);

        assertEquals(actual.capacity(), actual.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String actual = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", actual);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = "text/html";

        String actual = DataUtil.getCharsetFromContentType(contentType);

        assertNull(actual);
    }

    @Test
    public void testMimeBoundary() {
        String actual = DataUtil.mimeBoundary();

        assertNotNull(actual);
    }
}