import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import static org.junit.Assert.*;

public class DataUtilTest {
    
    @Test
    public void testLoad_File_Success() {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = Mockito.mock(File.class);
        FileInputStream inputStream = Mockito.mock(FileInputStream.class);
        ByteBuffer byteBuffer = Mockito.mock(ByteBuffer.class);
        Document expectedDocument = Mockito.mock(Document.class);
        
        try {
            Mockito.when(file.exists()).thenReturn(true);
            Mockito.when(file.isFile()).thenReturn(true);
            Mockito.when(file.canRead()).thenReturn(true);
            Mockito.when(file.getAbsolutePath()).thenReturn("test.html");
            Mockito.when(file.length()).thenReturn(100L);
            Mockito.whenNew(FileInputStream.class).withArguments(file).thenReturn(inputStream);
            Mockito.when(DataUtil.readToByteBuffer(inputStream)).thenReturn(byteBuffer);
            Mockito.when(DataUtil.parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser())).thenReturn(expectedDocument);
            
            // Act
            Document actualDocument = DataUtil.load(file, charsetName, baseUri);
            
            // Assert
            Mockito.verify(file, Mockito.times(1)).exists();
            Mockito.verify(file, Mockito.times(1)).isFile();
            Mockito.verify(file, Mockito.times(1)).canRead();
            Mockito.verify(file, Mockito.times(1)).getAbsolutePath();
            Mockito.verify(file, Mockito.times(1)).length();
            Mockito.verifyNew(FileInputStream.class, Mockito.times(1)).withArguments(file);
            Mockito.verify(DataUtil, Mockito.times(1)).readToByteBuffer(inputStream);
            Mockito.verify(DataUtil, Mockito.times(1)).parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser());
            assertEquals(expectedDocument, actualDocument);
            
        } catch (IOException e) {
            fail("Exception should not be thrown");
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testLoad_File_NotFound() {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = Mockito.mock(File.class);
        
        try {
            Mockito.when(file.exists()).thenReturn(false);
            
            // Act
            Document actualDocument = DataUtil.load(file, charsetName, baseUri);
            
            // Assert
            Mockito.verify(file, Mockito.times(1)).exists();
            assertNull(actualDocument);
            
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testLoad_File_NotFile() {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = Mockito.mock(File.class);
        
        try {
            Mockito.when(file.exists()).thenReturn(true);
            Mockito.when(file.isFile()).thenReturn(false);
            
            // Act
            Document actualDocument = DataUtil.load(file, charsetName, baseUri);
            
            // Assert
            Mockito.verify(file, Mockito.times(1)).exists();
            Mockito.verify(file, Mockito.times(1)).isFile();
            assertNull(actualDocument);
            
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testLoad_File_CannotRead() {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = Mockito.mock(File.class);
        
        try {
            Mockito.when(file.exists()).thenReturn(true);
            Mockito.when(file.isFile()).thenReturn(true);
            Mockito.when(file.canRead()).thenReturn(false);
            
            // Act
            Document actualDocument = DataUtil.load(file, charsetName, baseUri);
            
            // Assert
            Mockito.verify(file, Mockito.times(1)).exists();
            Mockito.verify(file, Mockito.times(1)).isFile();
            Mockito.verify(file, Mockito.times(1)).canRead();
            assertNull(actualDocument);
            
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testLoad_File_ExceptionThrown() {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = Mockito.mock(File.class);
        FileInputStream inputStream = Mockito.mock(FileInputStream.class);
        
        try {
            Mockito.when(file.exists()).thenReturn(true);
            Mockito.when(file.isFile()).thenReturn(true);
            Mockito.when(file.canRead()).thenReturn(true);
            Mockito.when(file.getAbsolutePath()).thenReturn("test.html");
            Mockito.when(file.length()).thenReturn(100L);
            Mockito.whenNew(FileInputStream.class).withArguments(file).thenReturn(inputStream);
            Mockito.doThrow(new IOException("IO Exception")).when(DataUtil.readToByteBuffer(inputStream));
            
            // Act
            Document actualDocument = DataUtil.load(file, charsetName, baseUri);
            
            // Assert
            Mockito.verify(file, Mockito.times(1)).exists();
            Mockito.verify(file, Mockito.times(1)).isFile();
            Mockito.verify(file, Mockito.times(1)).canRead();
            Mockito.verify(file, Mockito.times(1)).getAbsolutePath();
            Mockito.verify(file, Mockito.times(1)).length();
            Mockito.verifyNew(FileInputStream.class, Mockito.times(1)).withArguments(file);
            Mockito.verify(DataUtil, Mockito.times(1)).readToByteBuffer(inputStream);
            assertNull(actualDocument);
            
        } catch (IOException e) {
            fail("Exception should not be thrown");
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    // other test methods for load(InputStream in, String charsetName, String baseUri) and load(InputStream in, String charsetName, String baseUri, Parser parser) can be added similarly
    
    @Test
    public void testParseByteData_DefaultCharsetName_Success() {
        // Arrange
        String charsetName = null; // default charset should be used
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        ByteBuffer byteData = Mockito.mock(ByteBuffer.class);
        String docData = "<html></html>";
        Document expectedDocument = Mockito.mock(Document.class);
        
        try {
            Mockito.when(DataUtil.defaultCharset).thenReturn("UTF-8");
            Mockito.when(DataUtil.parseByteData(byteData, "UTF-8", baseUri, parser)).thenReturn(expectedDocument);
            Mockito.when(charset.forName("UTF-8").decode(byteData).toString()).thenReturn(docData);
            
            // Act
            Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);
            
            // Assert
            Mockito.verify(DataUtil, Mockito.times(1)).defaultCharset;
            Mockito.verify(DataUtil, Mockito.times(1)).parseByteData(byteData, "UTF-8", baseUri, parser);
            Mockito.verify(charset.forName("UTF-8"), Mockito.times(1)).decode(byteData).toString();
            assertEquals(expectedDocument, actualDocument);
            
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    // other test methods for parseByteData can be added similarly
    
    @Test
    public void testReadToByteBuffer_UnlimitedSize_Success() {
        // Arrange
        int maxSize = 0; // unlimited size
        InputStream inputStream = Mockito.mock(InputStream.class);
        byte[] buffer = new byte[DataUtil.bufferSize];
        ByteArrayOutputStream outStream = Mockito.mock(ByteArrayOutputStream.class);
        ByteBuffer expectedByteBuffer = Mockito.mock(ByteBuffer.class);
        
        try {
            Mockito.when(DataUtil.readToByteBuffer(inputStream)).thenCallRealMethod();
            Mockito.when(inputStream.read(buffer)).thenReturn(100).thenReturn(-1); // return 100 bytes first and then -1 to indicate end of stream
            Mockito.whenNew(ByteArrayOutputStream.class).withArguments(DataUtil.bufferSize).thenReturn(outStream);
            Mockito.whenNew(ByteBuffer.class).withArguments(outStream.toByteArray()).thenReturn(expectedByteBuffer);
            
            // Act
            ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            
            // Assert
            Mockito.verify(DataUtil, Mockito.times(1)).readToByteBuffer(inputStream);
            Mockito.verify(inputStream, Mockito.times(2)).read(buffer);
            Mockito.verifyNew(ByteArrayOutputStream.class, Mockito.times(1)).withArguments(DataUtil.bufferSize);
            Mockito.verify(outStream, Mockito.times(1)).write(buffer, 0, 100);
            Mockito.verifyNew(ByteBuffer.class, Mockito.times(1)).withArguments(outStream.toByteArray());
            assertEquals(expectedByteBuffer, actualByteBuffer);
            
        } catch (IOException e) {
            fail("Exception should not be thrown");
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    // other test methods for readToByteBuffer can be added similarly
    
    @Test
    public void testGetCharsetFromContentType_Success() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        
        // Act
        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertEquals(expectedCharset, actualCharset);
    }
    
    // other test methods for getCharsetFromContentType can be added similarly
}