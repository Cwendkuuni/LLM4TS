import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.jsoup.UncheckedIOException;
import org.jsoup.internal.ConstrainableInputStream;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    private DataUtil dataUtil;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File file = mock(File.class);
        FileInputStream inputStream = mock(FileInputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        Document document = dataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(document);
        verify(file).getPath();
        verify(file).getName();
        verify(inputStream, atLeastOnce()).close();
    }
    
    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        Document document = dataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
        verify(inputStream, atLeastOnce()).close();
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = mock(Parser.class);
        
        Document document = dataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
        verify(inputStream, atLeastOnce()).close();
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = mock(InputStream.class);
        OutputStream out = mock(OutputStream.class);
        
        dataUtil.crossStreams(in, out);
        
        verify(in, atLeastOnce()).read(any(byte[].class));
        verify(out, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
    }
    
    @Test
    public void testParseInputStream() throws IOException {
        InputStream input = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = mock(Parser.class);
        
        Document document = dataUtil.parseInputStream(input, charsetName, baseUri, parser);
        
        assertNotNull(document);
        verify(input).close();
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = mock(InputStream.class);
        int maxSize = 1024;
        
        ByteBuffer buffer = dataUtil.readToByteBuffer(inStream, maxSize);
        
        assertNotNull(buffer);
        verify(inStream, atLeastOnce()).read(any(byte[].class));
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }
}