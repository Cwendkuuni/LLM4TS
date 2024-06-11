package org.jsoup.helper;

import org.junit.*;
import static org.junit.Assert.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import static org.jsoup.parser.Parser.*;
import org.junit.rules.*;

public class DataUtilTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testLoadFile() throws Exception {
        File testFile = new File("test.html"); // assuming a valid test html file
        String baseUri = "http://test.com";
        String charsetName = "UTF-8";

        Document doc = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData() throws Exception {
        String docData = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test Data</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        String baseUri = "http://test.com";
        Parser parser = htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadInvalidFilePath() throws Exception {
        File invalidFilePath = new File("/nonexistent/path");
        String baseUri = "http://test.com";
        String charsetName = "UTF-8";

        DataUtil.load(invalidFilePath, charsetName, baseUri);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadFileNotFound() throws Exception {
        File fileNotFound = new File("nonexistentfile.html");
        String baseUri = "http://test.com";
        String charsetName = "UTF-8";

        DataUtil.load(fileNotFound, charsetName, baseUri);
    }

    @Test
    public void testLoadFileNotReadable() throws Exception {
        File fileNotReadable = new File("test.html"); // assuming test.html is not readable
        String baseUri = "http://test.com";
        String charsetName = "UTF-8";

        assertThrows(IOException.class, () -> DataUtil.load(fileNotReadable, charsetName, baseUri));
    }

    @Test
    public void testLoadInvalidCharsetName() throws Exception {
        File file = new File("test.html");
        String baseUri = "http://test.com";
        String charsetName = "";

        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(file, charsetName, baseUri));
    }

    @Test
    public void testLoadIOException() throws Exception {
        File file = new File("test.html"); // assuming test.html cannot be read due to an IO error
        String baseUri = "http://test.com";
        String charsetName = "UTF-8";

        assertThrows(IOException.class, () -> DataUtil.load(file, charsetName, baseUri));
    }
}