import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class DataUtilTest {

    @Test
    public void testLoadMethodWithFileInputSuccess() throws IOException {
        // Given a valid file
        File inputFile = new File("path/to/valid/file.html");
        String expectedCharset = "UTF-8"; // or any valid charset
        String baseUri = "http://example.com";

        // When loading the file with DataUtil.load()
        Document document = DataUtil.load(inputFile, expectedCharset, baseUri);

        // Then the document should not be null and its charset should match the expected one
        Assert.assertNotNull(document);
        Assert.assertEquals(expectedCharset, document.outputSettings().charset());
    }

    // Add test cases for other methods and edge cases later
}