package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Mock
    private Parser parser;

    private DataUtil dataUtil;
    private Document doc;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
        doc = mock(Document.class);
    }

    @Test
    public void testLoadFile() throws Exception {
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.length()).thenReturn(100L);

        InputStream inStream = mock(InputStream.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(file.getInputStream()).thenReturn(inStream);

        ByteBuffer byteData = dataUtil.readToByteBuffer(inStream);
        Document doc = dataUtil.load(file, null, "");
        verify(parser, times(1)).parseInput(eq(outputStream.toByteArray()), eq(""));
        assertEquals(doc, this.doc);
    }

    @Test
    public void testLoadInputStream() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.available()).thenReturn(100);

        ByteBuffer byteData = dataUtil.readToByteBuffer(inputStream);
        Document doc = dataUtil.load(inputStream, null, "");
        verify(parser, times(1)).parseInput(eq(byteData.array()), eq(""));
        assertEquals(doc, this.doc);
    }

    @Test
    public void testParseByteData() throws Exception {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = mock(Parser.class);

        when(parser.parseInput(eq(byteData.array()), eq(charsetName))).thenReturn(doc);

        Document doc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals(doc, this.doc);

        verify(parser, times(1)).parseInput(eq(byteData.array()), eq(charsetName));
    }

    @Test
    public void testReadToByteBufferFromFile() throws Exception {
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.length()).thenReturn(100L);

        InputStream inStream = mock(InputStream.class);
        when(file.getInputStream()).thenReturn(inStream);

        ByteBuffer byteData = dataUtil.readToByteBuffer(file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byteData.get(outputStream.toByteArray(), 0, byteData.limit());
        String expectedContent = new String(outputStream.toByteArray(), Charset.forName("UTF-8"));

        verify(file, times(1)).getInputStream();
        verifyNoMoreInteractions(file);
        verify(inStream, times(1)).readAllBytes();
        verifyNoMoreInteractions(inStream);
        assertEquals(expectedContent, new String(byteData.array(), Charset.forName("UTF-8")));
    }

    @Test
    public void testReadToByteBufferFromInputStream() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] inputBytes = "input".getBytes();
        when(inputStream.readAllBytes()).thenReturn(inputBytes);

        ByteBuffer byteData = dataUtil.readToByteBuffer(inputStream);
        String expectedContent = new String(inputBytes, Charset.forName("UTF-8"));

        verify(inputStream, times(1)).readAllBytes();
        verifyNoMoreInteractions(inputStream);
        assertEquals(expectedContent, new String(byteData.array(), Charset.forName("UTF-8")));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = dataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset= InvalidCharset";
        String expectedCharset = null;

        String charset = dataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }
}