import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("path/to/file.html"); // replace with your test file path

        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String inputStreamContent = "some HTML content"; // replace with your test input stream content

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown: " + e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // expected
            }
        }
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() {
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        String inputStreamContent = "some HTML content"; // replace with your test input stream content

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("IOException should have been thrown");
        } catch (IOException e) {
            // expected
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // expected
            }
        }
    }
}