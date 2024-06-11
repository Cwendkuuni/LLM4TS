
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;

public class DataUtilTest {
    
    @Test
    public void testLoadFromFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            
            assertNotNull(document);
            // add more assertions if necessary
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadFromInputStream() {
        InputStream inStream = new FileInputStream(new File("test.html"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        try {
            Document document = DataUtil.load(inStream, charsetName, baseUri);
            
            assertNotNull(document);
            // add more assertions if necessary
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() {
        InputStream inStream = new FileInputStream(new File("test.html"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        try {
            Document document = DataUtil.load(inStream, charsetName, baseUri, parser);
            
            assertNotNull(document);
            // add more assertions if necessary
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(document);
        // add more assertions if necessary
    }
    
    @Test
    public void testReadToByteBuffer() {
        InputStream inStream = new FileInputStream(new File("test.html"));
        
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
            
            assertNotNull(byteData);
            // add more assertions if necessary
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentTypeWithCharset = "text/html; charset=EUC-JP";
        String contentTypeWithoutCharset = "text/html; charset=UTF-8";
        String invalidContentType = "text/html";
        
        String charsetFromContentType1 = DataUtil.getCharsetFromContentType(contentTypeWithCharset);
        String charsetFromContentType2 = DataUtil.getCharsetFromContentType(contentTypeWithoutCharset);
        String charsetFromContentType3 = DataUtil.getCharsetFromContentType(invalidContentType);
        
        assertEquals("EUC-JP", charsetFromContentType1);
        assertEquals("UTF-8", charsetFromContentType2);
        assertNull(charsetFromContentType3);
    }
}

