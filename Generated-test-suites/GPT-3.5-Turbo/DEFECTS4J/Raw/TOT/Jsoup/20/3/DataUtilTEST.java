import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    @Test
    public void testLoad_File_ValidInput() throws IOException {
        // Prepare
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        assertNotNull(document);
        // Add more assertions for verifying the correctness of the loaded document
    }

    @Test(expected = IOException.class)
    public void testLoad_File_InvalidInput() throws IOException {
        // Prepare
        File file = new File("invalid.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        // Act
        DataUtil.load(file, charsetName, baseUri);

        // Expect to throw IOException due to invalid file input
    }

    @Test
    public void testLoad_InputStream_ValidInput() throws IOException {
        // Prepare
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        assertNotNull(document);
        // Add more assertions for verifying the correctness of the loaded document
    }

    @Test(expected = IOException.class)
    public void testLoad_InputStream_InvalidInput() throws IOException {
        // Prepare
        InputStream inputStream = getClass().getResourceAsStream("invalid.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        // Act
        DataUtil.load(inputStream, charsetName, baseUri);

        // Expect to throw IOException due to invalid input stream
    }

    @Test
    public void testLoad_InputStream_CustomParser_ValidInput() throws IOException {
        // Prepare
        InputStream inputStream = getClass().getResourceAsStream("test.xml");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.xmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
        // Add more assertions for verifying the correctness of the loaded document
    }

    @Test(expected = IOException.class)
    public void testLoad_InputStream_CustomParser_InvalidInput() throws IOException {
        // Prepare
        InputStream inputStream = getClass().getResourceAsStream("invalid.xml");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.xmlParser();

        // Act
        DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Expect to throw IOException due to invalid input stream
    }

    // Add more test methods for other public methods in DataUtil class

}