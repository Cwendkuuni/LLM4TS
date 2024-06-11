
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoad_File_CharsetName_BaseUri() throws IOException {
        // Arrange
        File in = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        Document doc = DataUtil.load(in, charsetName, baseUri);
        
        // Assert
        Assert.assertNotNull(doc);
        // Additional assertions for the expected behavior of the method
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri() throws IOException {
        // Arrange
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        Document doc = DataUtil.load(in, charsetName, baseUri);
        
        // Assert
        Assert.assertNotNull(doc);
        // Additional assertions for the expected behavior of the method
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri_Parser() throws IOException {
        // Arrange
        InputStream in = new FileInputStream("test.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        
        // Act
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        
        // Assert
        Assert.assertNotNull(doc);
        // Additional assertions for the expected behavior of the method
    }

    // Additional test cases for the remaining methods

}
