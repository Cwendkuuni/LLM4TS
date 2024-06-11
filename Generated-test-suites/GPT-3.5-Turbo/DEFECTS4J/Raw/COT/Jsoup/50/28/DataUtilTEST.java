
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void load_file_checkDocument() throws IOException {
        // Given
        File file = new File("path/to/file");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Then
        Assert.assertNotNull(document);
        // Additional assertions for the loaded document
    }

    @Test
    public void load_inputStream_checkDocument() throws IOException {
        // Given
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Then
        Assert.assertNotNull(document);
        // Additional assertions for the loaded document
    }

    @Test
    public void load_inputStream_parser_checkDocument() throws IOException {
        // Given
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // When
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Then
        Assert.assertNotNull(document);
        // Additional assertions for the loaded document
    }

    @Test
    public void crossStreams_sameInputStreamAndOutputStream_noExceptionThrown() throws IOException {
        // Given
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        OutputStream outputStream = new ByteArrayOutputStream();

        // When
        DataUtil.crossStreams(inputStream, outputStream);

        // No assertions needed, just checking for no exceptions
    }

    // ... additional test cases for other methods

}
