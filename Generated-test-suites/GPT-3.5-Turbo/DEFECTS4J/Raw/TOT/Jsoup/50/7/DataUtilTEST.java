import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

public class DataUtilTest {

    @Test
    public void testLoad_withFile_typicalAndErrorCases() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            
            Assertions.assertNotNull(document);
            Assertions.assertTrue(document instanceof Document);
            Assertions.assertEquals("test.html", document.title());
        } catch (IOException e) {
            Assertions.fail("IOException should not be thrown");
        }
        
        // Test with non-existent file
        File nonExistentFile = new File("nonexistent.html");
        try {
            Document document = DataUtil.load(nonExistentFile, charsetName, baseUri);
            Assertions.fail("IOException should be thrown for non-existent file");
        } catch (IOException e) {
            Assertions.assertTrue(e.getMessage().contains("No such file or directory"));
        }
        
        // Test with null charsetName
        try {
            Document document = DataUtil.load(file, null, baseUri);
            
            Assertions.assertNotNull(document);
            Assertions.assertTrue(document instanceof Document);
            Assertions.assertEquals("test.html", document.title());
        } catch (IOException e) {
            Assertions.fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testLoad_withInputStream_typicalAndErrorCases() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            
            Assertions.assertNotNull(document);
            Assertions.assertTrue(document instanceof Document);
            Assertions.assertEquals("test.html", document.title());
        } catch (IOException e) {
            Assertions.fail("IOException should not be thrown");
        }
        
        // Test with null InputStream
        try {
            Document document = DataUtil.load(null, charsetName, baseUri);
            Assertions.fail("IOException should be thrown for null InputStream");
        } catch (IOException e) {
            Assertions.assertTrue(e.getMessage().contains("InputStream cannot be null"));
        }
        
        // Test with unsupported charsetName
        String unsupportedCharset = "UNSUPPORTED-CHARSET";
        try {
            Document document = DataUtil.load(inputStream, unsupportedCharset, baseUri);
            Assertions.fail("IllegalCharsetNameException should be thrown for unsupported charsetName");
        } catch (IllegalCharsetNameException e) {
            Assertions.assertTrue(e.getMessage().contains("Unsupported charset: " + unsupportedCharset));
        } catch (IOException e) {
            Assertions.fail("IOException should not be thrown for unsupported charsetName");
        }
    }
    
    @Test
    public void testLoad_withInputStreamAndParser_typicalAndErrorCases() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            
            Assertions.assertNotNull(document);
            Assertions.assertTrue(document instanceof Document);
            Assertions.assertEquals("test.html", document.title());
        } catch (IOException e) {
            Assertions.fail("IOException should not be thrown");
        }
        
        // Test with null InputStream
        try {
            Document document = DataUtil.load(null, charsetName, baseUri, parser);
            Assertions.fail("IOException should be thrown for null InputStream");
        } catch (IOException e) {
            Assertions.assertTrue(e.getMessage().contains("InputStream cannot be null"));
        }
        
        // Test with unsupported charsetName
        String unsupportedCharset = "UNSUPPORTED-CHARSET";
        try {
            Document document = DataUtil.load(inputStream, unsupportedCharset, baseUri, parser);
            Assertions.fail("IllegalCharsetNameException should be thrown for unsupported charsetName");
        } catch (IllegalCharsetNameException e) {
            Assertions.assertTrue(e.getMessage().contains("Unsupported charset: " + unsupportedCharset));
        } catch (IOException e) {
            Assertions.fail("IOException should not be thrown for unsupported charsetName");
        }
    }
    
    @Test
    public void testCrossStreams_typicalCase() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("input.txt");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            String output = outputStream.toString(Charset.forName("UTF-8").name());
            Assertions.assertEquals("This is a test file.", output);
        } catch (IOException e) {
            Assertions.fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testParseByteData_typicalCase() {
        ByteBuffer byteData = ByteBuffer.wrap("This is a test document.".getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        Assertions.assertNotNull(document);
        Assertions.assertTrue(document instanceof Document);
        Assertions.assertEquals("This is a test document.", document.text());
    }
    
    // Additional test cases can be added for other methods as well
    
}