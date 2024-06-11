
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class DataUtilTest {

    @Test
    public void testLoad_File_charsetName_baseUri() throws IOException {
        File file = new File("testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(doc);
        // Add more assertions for the specific functionality
    }

    @Test
    public void testLoad_InputStream_charsetName_baseUri() throws IOException {
        InputStream inputStream = new FileInputStream("testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(doc);
        // Add more assertions for the specific functionality
    }

    @Test
    public void testLoad_InputStream_charsetName_baseUri_parser() throws IOException {
        InputStream inputStream = new FileInputStream("testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        // Add more assertions for the specific functionality
    }

    // Additional test cases for edge cases and exceptions

    @Test(expected = IOException.class)
    public void testLoad_File_charsetName_baseUri_IOException() throws IOException {
        File file = new File("nonexistentFile.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        DataUtil.load(file, charsetName, baseUri);
        // Expecting an IOException to be thrown
    }

    @Test(expected = IOException.class)
    public void testLoad_InputStream_charsetName_baseUri_IOException() throws IOException {
        InputStream inputStream = new FileInputStream("nonexistentFile.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        DataUtil.load(inputStream, charsetName, baseUri);
        // Expecting an IOException to be thrown
    }

    // Additional test cases for edge cases and exceptions

}

