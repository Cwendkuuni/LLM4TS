import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiters.junit4.launcher.junit4.TestLauncher;
import java.io.*;

public class DataUtilTest {
    @Test
    public void testLoadFile() {
        File inputFile = new File("test.html"); // replace with actual test file
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        File file = new File("test.html"); // replace with actual test file
        InputStream inputStream = new FileInputStream(file);

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        File file = new File("test.html"); // replace with actual test file
        InputStream inputStream = new FileInputStream(file);

        Document document = DataUtil.load(inputStream, "UTF-8", null, Jsoup.htmlParser());
        assertNotNull(document);

        inputStream.close();
    }

    // Add edge cases and exception handling tests here...

    @Test(expected = FileNotFoundException.class)
    public void testLoadFileNotFound() {
        File inputFile = new File("non_existent_file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        DataUtil.load(inputFile, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadIOException() throws IOException {
        File inputFile = new File("test.html"); // replace with actual test file
        InputStream inputStream = new FileInputStream(inputFile);
        String charsetName = "UTF-8";
        String baseUri = null;

        DataUtil.load(inputStream, charsetName, baseUri);
        inputStream.close();
    }

    @Test
    public void testLoadInvalidCharset() {
        File inputFile = new File("test.html"); // replace with actual test file
        String charsetName = "InvalidCharset";
        String baseUri = "http://test.com";

        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadNullBaseUri() {
        File inputFile = new File("test.html"); // replace with actual test file
        String charsetName = "UTF-8";
        String baseUri = null;

        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadEmptyBaseUri() {
        File inputFile = new File("test.html"); // replace with actual test file
        String charsetName = "UTF-8";
        String baseUri = "";

        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadNullInput() {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = DataUtil.load(null, charsetName, baseUri);
        assertNull(document);
    }

    @Test
    public void testLoadEmptyFile() {
        File inputFile = new File("");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        assertNull(document);
    }

    // Add more test cases as needed...

}