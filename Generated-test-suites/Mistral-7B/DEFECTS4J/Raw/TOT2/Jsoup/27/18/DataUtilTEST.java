import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        String filePath = "test.html"; // Replace this with the path to a valid HTML file
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080/"; // Replace this with a valid base URI

        Document document = DataUtil.load(new File(filePath), charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    // Edge case testing for load method

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFilePath() throws IOException {
        String filePath = "InvalidFile.txt"; // Replace this with an invalid file path
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080/";

        DataUtil.load(new File(filePath), charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullFilePath() {
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080/";

        try {
            DataUtil.load(null, charsetName, baseUri);
        } catch (IOException e) {
        }
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharsetName() {
        File file = new File("test.html");
        String baseUri = "http://localhost:8080/";

        try {
            DataUtil.load(file, null, baseUri);
        } catch (IOException e) {
        }
    }
}