
import org.jsoup.helper.DataUtil;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        // Prepare test data
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Call the method
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert the result
        Assert.assertNotNull(document);
        // Add more assertions based on the expected result
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Prepare test data
        String html = "<html><body>Test HTML</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Call the method
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert the result
        Assert.assertNotNull(document);
        // Add more assertions based on the expected result
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Prepare test data
        String html = "<html><body>Test HTML</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Call the method
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert the result
        Assert.assertNotNull(document);
        // Add more assertions based on the expected result
    }

    // Add more test cases for each public method in the DataUtil class to cover all scenarios

}

