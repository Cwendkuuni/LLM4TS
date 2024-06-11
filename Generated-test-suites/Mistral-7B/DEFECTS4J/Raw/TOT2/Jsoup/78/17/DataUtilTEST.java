import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.IOException;

public class DataUtilTest {
    // Test for load method with valid file and default charset
    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null);
        Assert.assertNotNull(doc);
    }

    // Test for load method with valid file and specified charset
    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8");
        Assert.assertNotNull(doc);
    }

    // Test for load method with null file
    @Test (expected = IOException.class)
    public void testLoadNullFile() {
        Document doc = DataUtil.load(null, "UTF-8");
    }

    // Test for load method with invalid file
    @Test (expected = IOException.class)
    public void testLoadInvalidFile() throws IOException {
        File file = new File("nonexistentfile.html");
        Document doc = DataUtil.load(file, "UTF-8");
    }

    // Test for load method with InputStream
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8");
        Assert.assertNotNull(doc);
    }

    // Test for load method with InputStream and null charset
    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, null);
        Assert.assertNotNull(doc);
    }

    // Test for parseInputStream method with valid input and default charset
    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    // Test for parseInputStream method with valid input and specified charset
    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    // Test for parseInputStream method with null input
    @Test (expected = IOException.class)
    public void testParseInputStreamWithNullInput() {
        Document doc = DataUtil.parseInputStream(null, "UTF-8", null, Parser.htmlParser());
    }
}