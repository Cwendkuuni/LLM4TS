
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    @Test
    public void testLoad_validFile_success() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        assertNotNull(document);
        // Add more assertions...
    }

    @Test
    public void testLoad_invalidFile_throwIOException() {
        // Arrange
        File file = new File("invalid.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act & Assert
        try {
            DataUtil.load(file, charsetName, baseUri);
            assert false; // should throw IOException
        } catch (IOException e) {
            // Expected exception
            assert true;
            // Add more assertions...
        }
    }

    @Test
    public void testLoad_nullInputStream_throwIOException() {
        // Arrange
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act & Assert
        try {
            DataUtil.load(inputStream, charsetName, baseUri);
            assert false; // should throw IOException
        } catch (IOException e) {
            // Expected exception
            assert true;
            // Add more assertions...
        }
    }

    // Add more test methods for other public methods in DataUtil class...

}

