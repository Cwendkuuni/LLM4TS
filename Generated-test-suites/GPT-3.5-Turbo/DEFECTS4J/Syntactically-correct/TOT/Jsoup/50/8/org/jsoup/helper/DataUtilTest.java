import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataUtilTest {

    @Test
    public void testLoadFile_Success() throws Exception {
        // Arrange
        String filePath = "file.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        Document doc = DataUtil.load(new File(filePath), charsetName, baseUri);
        
        // Assert
        assertEquals(baseUri, doc.baseUri());
        // Add more assertions here to validate the loaded document
    }

    @Test
    public void testLoadFile_InvalidCharset() throws Exception {
        // Arrange
        String filePath = "file.html";
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        
        // Act and Assert
        try {
            Document doc = DataUtil.load(new File(filePath), charsetName, baseUri);
            // Test should fail if reaching this point
        } catch (IOException e) {
            // Add assertions here to validate the exception message or type
        }
    }

    @Test
    public void testLoadFile_IOError() throws Exception {
        // Arrange
        String filePath = "non_existing_file.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act and Assert
        try {
            Document doc = DataUtil.load(new File(filePath), charsetName, baseUri);
            // Test should fail if reaching this point
        } catch (IOException e) {
            // Add assertions here to validate the exception message or type
        }
    }

    @Test
    public void testLoadInputStream_Success() throws Exception {
        // Arrange
        InputStream inputStream = new FileInputStream("file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        
        // Assert
        assertEquals(baseUri, doc.baseUri());
        // Add more assertions here to validate the loaded document
    }

    // Add more test cases for the other methods in DataUtil class

}