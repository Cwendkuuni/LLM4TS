import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.Connection.Method;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoad_FileCharsetBaseUri() throws IOException {
        File file = mock(File.class);
        
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        
        verify(file).length();
        verify(file).readFully(any(byte[].class));
        verifyNoMoreInteractions(file);
        
        assertEquals(expectedDocument, doc);
    }
    
    @Test
    public void testLoad_InputCharsetBaseUri() throws IOException {
        InputStream in = mock(InputStream.class);
        
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
        
        verify(in).read(any(byte[].class));
        verifyNoMoreInteractions(in);
        
        assertEquals(expectedDocument, doc);
    }
    
    @Test
    public void testLoad_InputCharsetBaseUriParser() throws IOException {
        InputStream in = mock(InputStream.class);
        Parser parser = mock(Parser.class);
        
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com", parser);
        
        verify(in).read(any(byte[].class));
        verifyNoMoreInteractions(in);
        verify(parser).parseInput(anyString(), eq("http://example.com"));
        verifyNoMoreInteractions(parser);
        
        assertEquals(expectedDocument, doc);
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = mock(InputStream.class);
        OutputStream out = mock(OutputStream.class);
        
        DataUtil.crossStreams(in, out);
        
        verify(in).read(any(byte[].class));
        verify(out).write(any(byte[].class), eq(0), anyInt());
        
        verifyNoMoreInteractions(in, out);
    }
    
    @Test
    public void testParseByteData_CharsetNameBaseUriParser() {
        ByteBuffer byteData = ByteBuffer.wrap(htmlBytes);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        verify(parser).parseInput(anyString(), eq("http://example.com"));
        verifyNoMoreInteractions(parser);
        
        assertEquals(expectedDocument, doc);
    }
    
    @Test
    public void testReadToByteBuffer_MaxSize() throws IOException {
        InputStream inStream = mock(InputStream.class);
        int maxSize = 1024;
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, maxSize);
        
        verify(inStream, times(2)).read(any(byte[].class));
        verifyNoMoreInteractions(inStream);
        
        assertEquals(expectedByteBuffer, byteBuffer);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = mock(InputStream.class);
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);
        
        verify(inStream).read(any(byte[].class));
        verifyNoMoreInteractions(inStream);
        
        assertEquals(expectedByteBuffer, byteBuffer);
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = mock(File.class);
        RandomAccessFile randomAccessFile = mock(RandomAccessFile.class);
        
        when(file.length()).thenReturn((long) htmlBytes.length);
        whenNew(RandomAccessFile.class).withArguments(eq(file), eq("r")).thenReturn(randomAccessFile);
        doAnswer(invocation -> {
            ((byte[]) invocation.getArgument(0))[0] = htmlBytes[0];
            ((byte[]) invocation.getArgument(0))[1] = htmlBytes[1];
            ((byte[]) invocation.getArgument(0))[2] = htmlBytes[2];
            ((byte[]) invocation.getArgument(0))[3] = htmlBytes[3];
            return null;
        }).when(randomAccessFile).readFully(any(byte[].class));
        
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        
        verify(file).length();
        verifyNew(RandomAccessFile.class).withArguments(eq(file), eq("r"));
        verify(randomAccessFile).length();
        verify(randomAccessFile).readFully(any(byte[].class));
        verify(randomAccessFile).close();
        verifyNoMoreInteractions(file, randomAccessFile);
        
        assertEquals(expectedByteBuffer, byteBuffer);
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        
        assertEquals(expectedEmptyByteBuffer, byteBuffer);
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_InvalidCharset() {
        String contentType = "text/html; charset=invalid";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }
    
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(bomBytes);
        String charsetName = "UTF-8";
        
        String detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        
        assertEquals(expectedDetectedCharset, detectedCharset);
        assertEquals(expectedByteBufferAfterBomDetection, byteData);
    }
}