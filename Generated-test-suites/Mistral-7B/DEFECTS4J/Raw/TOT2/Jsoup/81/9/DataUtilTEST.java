import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        Document doc = DataUtil.load(file, null);
        Assert.assertNotNull(doc);
        // add assertions to check the loaded document
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        Document doc = DataUtil.load(file, "UTF-16");
        Assert.assertNotNull(doc);
        // add assertions to check the loaded document
    }

    @Test(expected = IOException.class)
    public void testInvalidFile() throws IOException {
        File file = new File("non-existent-file.html"); // replace with actual file path that does not exist
        Document doc = DataUtil.load(file, null);
        // the test should fail with an IOException
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream input = getClass().getResourceAsStream("/test.html"); // replace with actual resource path
        Document doc = DataUtil.load(input, null);
        Assert.assertNotNull(doc);
        // add assertions to check the loaded document
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        InputStream input = getClass().getResourceAsStream("/test.html"); // replace with actual resource path
        Document doc = DataUtil.load(input, "UTF-16");
        Assert.assertNotNull(doc);
        // add assertions to check the loaded document
    }

    @Test(expected = IOException.class)
    public void testInvalidInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.load(input, null);
        // the test should fail with an IOException
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        InputStream input = getClass().getResourceAsStream("/test.html"); // replace with actual resource path
        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        // add assertions to check the loaded document
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        InputStream input = getClass().getResourceAsStream("/test.html"); // replace with actual resource path
        Document doc = DataUtil.parseInputStream(input, "UTF-16", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        // add assertions to check the loaded document
    }

    @Test(expected = IOException.class)
    public void testInvalidInputStreamForParse() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        // the test should fail with an IOException
    }
}