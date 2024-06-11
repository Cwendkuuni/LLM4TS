
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void load_file_charsetName_baseUri() throws IOException {
        // Arrange
        File testFile = new File("path/to/test/file.html"); // Replace with actual test file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document result = DataUtil.load(testFile, charsetName, baseUri);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void load_inputStream_charsetName_baseUri() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html"); // Replace with actual test input stream
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document result = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void load_inputStream_charsetName_baseUri_parser() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html"); // Replace with actual test input stream
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document result = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void parseByteData_byteData_charsetName_baseUri() {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void readToByteBuffer_inStream() throws IOException {
        // Arrange
        InputStream inStream = getClass().getResourceAsStream("test.html"); // Replace with actual test input stream

        // Act
        ByteBuffer result = DataUtil.readToByteBuffer(inStream);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void getCharsetFromContentType_contentType() {
        // Arrange
        String contentType = "text/html;charset=UTF-8";

        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("UTF-8", result);
    }
}


