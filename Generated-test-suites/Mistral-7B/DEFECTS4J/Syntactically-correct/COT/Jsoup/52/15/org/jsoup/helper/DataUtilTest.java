package org.jsoup.helper; // make sure to import all necessary classes

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = Mockito.mock(File.class);
        Document expectedDocument = Mockito.mock(Document.class);
        Mockito.when(file.exists()).thenReturn(true);
        Mockito.when(file.length()).thenReturn(100L);
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        Document actualDocument = DataUtil.load(file, "UTF-8", "baseUri");
        assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadFileThrowIOException() throws IOException {
        File file = Mockito.mock(File.class);
        Mockito.when(file.exists()).thenReturn(false);
        DataUtil.load(file, "UTF-8", "baseUri");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        Document expectedDocument = Mockito.mock(Document.class);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Mockito.when(inputStream.available()).thenReturn(100);
        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "baseUri");
        assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamThrowIOException() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.available()).thenThrow(new IOException());
        DataUtil.load(inputStream, "UTF-8", "baseUri");
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertNull(charsetName);

        byteData = DataUtil.readFileToByteBuffer(new File("testFile.html"));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "baseUri", Parser.htmlParser());
        assertNotNull(document);

        byteData.rewind();
        charsetName = DataUtil.detectCharsetFromBom(byteData, "UTF-8");
        assertEquals("UTF-8", charsetName);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        OutputStream outputStream = Mockito.mock(OutputStream.class);

        Mockito.when(inputStream.read()).thenReturn('A');
        Mockito.when(outputStream.write(ByteBuffer.allocate(1).put((byte) 'A').array())).thenReturn(1);

        DataUtil.crossStreams(inputStream, outputStream);

        verify(inputStream, times(1)).read();
        verify(outputStream, times(1)).write(ByteBuffer.allocate(1).put((byte) 'A').array());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String actualCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, actualCharset);

        charset = "invalidCharset";
        actualCharset = DataUtil.validateCharset(charset);
        assertNull(actualCharset);
    }

    @Test
    public void testMimeBoundary() {
        String actualBoundary = DataUtil.mimeBoundary();
        String expectedBoundary = new String(new Random().getInts(boundaryLength, new RandomAccessFile("SecureRandomSource.java", "r.d")).toArray(new byte[boundaryLength]));
        assertEquals(expectedBoundary, actualBoundary);
    }
}