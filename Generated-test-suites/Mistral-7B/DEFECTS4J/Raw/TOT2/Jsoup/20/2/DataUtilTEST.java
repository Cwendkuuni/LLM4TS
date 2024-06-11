import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String expectedHtml = "...expected HTML content..."; // replace with actual expected HTML content

        File inputFile = new File("path/to/input/file.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", null);
        String actualHtml = doc.html();

        Assert.assertEquals(expectedHtml, actualHtml);
    }
}