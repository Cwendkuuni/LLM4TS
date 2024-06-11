
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoad_withFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "";
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert statements
    }

    @Test
    public void testLoad_withInputStream() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        // Assert statements
    }

    @Test
    public void testLoad_withInputStreamAndParser() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert statements
    }

    // Add test cases for other public methods in DataUtil class

    // Edge cases and exception handling scenarios

    @Test
    public void testLoad_withInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "InvalidCharset";
        String baseUri = "";
        
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            // Fail the test if no exception is thrown
            fail("Expected IOException");
        } catch (IOException e) {
            // Assert exception message or perform further checks
        }
    }
}

