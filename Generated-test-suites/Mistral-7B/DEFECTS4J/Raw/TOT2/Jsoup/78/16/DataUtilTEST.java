import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // assuming test.html exists in the classpath
        Document doc = DataUtil.load(file, null); // default charset will be used
        String html = doc.body().text();
        // add assertions based on the content of the test.html file
        assertEquals("Expected content in the file", "Actual content in the file", html);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8");
        String html = doc.body().text();
        // add assertions based on the content of the test.html file
        assertEquals("Expected content in the file", "Actual content in the file", html);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "Test input stream content";
        InputStream input = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(input, null);
        String documentHtml = doc.body().text();
        // add assertions based on the expected content
        assertEquals("Expected content in the document", "Test input stream content", documentHtml);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String html = "Test input stream content";
        InputStream input = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(input, "UTF-8");
        String documentHtml = doc.body().text();
        // add assertions based on the expected content
        assertEquals("Expected content in the document", "Test input stream content", documentHtml);
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String html = "Test input stream content";
        String charsetName = DataUtil.defaultCharset;
        InputStream input = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.parseInputStream(input, charsetName, null, Parser.htmlParser());
        String documentHtml = doc.body().text();
        // add assertions based on the expected content
        assertEquals("Expected content in the document", "Test input stream content", documentHtml);
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String html = "Test input stream content";
        String charsetName = "UTF-8";
        InputStream input = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.parseInputStream(input, charsetName, null, Parser.htmlParser());
        String documentHtml = doc.body().text();
        // add assertions based on the expected content
        assertEquals("Expected content in the document", "Test input stream content", documentHtml);
    }
}