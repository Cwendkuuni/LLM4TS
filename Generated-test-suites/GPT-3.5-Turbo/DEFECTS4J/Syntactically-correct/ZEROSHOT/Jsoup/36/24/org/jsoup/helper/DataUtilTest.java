import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.mockito.Mockito.*;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() throws IOException {
        // Arrange
        File file = mock(File.class);
        
        FileInputStream inStream = mock(FileInputStream.class);
        when(file.getName()).thenReturn("test.html");
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        when(file.length()).thenReturn(1000L);
        when(file.lastModified()).thenReturn(123456789L);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(inStream);
        
        // Act
        Document document = DataUtil.load(file, "UTF-8", "https://www.example.com");
        
        // Assert
        verify(inStream, times(1)).close();
        Assert.assertNotNull(document);
        // Additional assertions
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        // Arrange
        InputStream inputStream = mock(InputStream.class);
        
        // Act
        Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
        
        // Assert
        verify(inputStream, times(1)).close();
        Assert.assertNotNull(document);
        // Additional assertions
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Arrange
        InputStream inputStream = mock(InputStream.class);
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com", parser);
        
        // Assert
        verify(inputStream, times(1)).close();
        Assert.assertNotNull(document);
        // Additional assertions
    }
    
    @Test
    public void testParseByteData() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        Assert.assertNotNull(document);
        // Additional assertions
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        // Arrange
        InputStream inStream = mock(InputStream.class);
        int maxSize = 1000;
        when(inStream.read(any(byte[].class))).thenReturn(100).thenReturn(100).thenReturn(-1);
        
        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, maxSize);
        
        // Assert
        verify(inStream, times(1)).close();
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(200, byteBuffer.limit());
        // Additional assertions
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        // Arrange
        InputStream inStream = mock(InputStream.class);
        when(inStream.read(any(byte[].class))).thenReturn(100).thenReturn(100).thenReturn(-1);
        
        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);
        
        // Assert
        verify(inStream, times(1)).close();
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(200, byteBuffer.limit());
        // Additional assertions
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        Assert.assertEquals("UTF-8", charset);
        // Additional assertions
    }

}