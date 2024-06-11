import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {
    
    @Test
    public void testLoad_File_CharsetName_BaseUri() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            
            Document document = DataUtil.load(file, charsetName, baseUri);
            
            // Assert the document is not null
            Assert.assertNotNull(document);
            
            // Assert the document's base URI is set correctly
            Assert.assertEquals(baseUri, document.baseUri());
            
            // Assert the document's charset is set correctly
            Assert.assertEquals(charsetName, document.outputSettings().charset().name());
            
            // Assert the document's title is set correctly
            Assert.assertEquals("Test Document", document.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoad_InputStream_CharsetName_BaseUri() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            
            // Assert the document is not null
            Assert.assertNotNull(document);
            
            // Assert the document's base URI is set correctly
            Assert.assertEquals(baseUri, document.baseUri());
            
            // Assert the document's charset is set correctly
            Assert.assertEquals(charsetName, document.outputSettings().charset().name());
            
            // Assert the document's title is set correctly
            Assert.assertEquals("Test Document", document.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoad_InputStream_CharsetName_BaseUri_Parser() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();
            
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            
            // Assert the document is not null
            Assert.assertNotNull(document);
            
            // Assert the document's base URI is set correctly
            Assert.assertEquals(baseUri, document.baseUri());
            
            // Assert the document's charset is set correctly
            Assert.assertEquals(charsetName, document.outputSettings().charset().name());
            
            // Assert the document's title is set correctly
            Assert.assertEquals("Test Document", document.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrossStreams() {
        // Create dummy input and output streams
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            
            // Assert the output stream contains the same data as the input stream
            Assert.assertEquals("Hello, World!", outputStream.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testParseInputStream_Input_CharsetName_BaseUri_Parser() {
        try {
            String input = "<html><head><title>Test Document</title></head><body><h1>Hello, World!</h1></body></html>";
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();
            
            Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
            
            // Assert the document is not null
            Assert.assertNotNull(document);
            
            // Assert the document's base URI is set correctly
            Assert.assertEquals(baseUri, document.baseUri());
            
            // Assert the document's charset is set correctly
            Assert.assertEquals(charsetName, document.outputSettings().charset().name());
            
            // Assert the document's title is set correctly
            Assert.assertEquals("Test Document", document.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadToByteBuffer() {
        try {
            String input = "Hello, World!";
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            int maxSize = 0;
            
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            
            // Assert the byte buffer contains the same data as the input stream
            Assert.assertEquals("Hello, World!", new String(byteBuffer.array()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        
        // Assert the extracted charset is not null
        Assert.assertNotNull(charset1);
        
        // Assert the extracted charset is correct
        Assert.assertEquals("UTF-8", charset1);
        
        String contentType2 = "text/html";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        
        // Assert the extracted charset is null
        Assert.assertNull(charset2);
    }
    
}