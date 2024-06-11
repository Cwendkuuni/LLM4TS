import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.IllegalCharsetNameException;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://www.example.com");
        
        Assert.assertNotNull(document);
        // Add more assertions to test the correctness of the loaded document
    }

    @Test
    public void testLoadStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://www.example.com");

        Assert.assertNotNull(document);
        // Add more assertions to test the correctness of the loaded document
    }

    @Test
    public void testLoadStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, "UTF-8", "http://www.example.com", parser);

        Assert.assertNotNull(document);
        // Add more assertions to test the correctness of the loaded document
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        File file = new File("nonexistent.html");
        Document document = DataUtil.load(file, "UTF-8", "http://www.example.com");
        
        // Should throw IOException because the file does not exist
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void testLoadFileIllegalCharsetName() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "illegalCharset", "http://www.example.com");
        
        // Should throw IllegalCharsetNameException because the specified charset is invalid
    }

    // Add more test cases to cover edge cases and error scenarios
    
}