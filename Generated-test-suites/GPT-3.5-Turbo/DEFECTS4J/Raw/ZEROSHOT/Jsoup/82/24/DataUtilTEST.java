import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private DataUtil dataUtil;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFile() throws Exception {
        File file = new File("test.html");
        Document document = dataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add assertions to test the loaded document
    }

    @Test
    public void testLoadInputStream() throws Exception {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = dataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add assertions to test the loaded document
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = dataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        // Add assertions to test the loaded document
    }

    // Add more test methods to comprehensively test other methods in the DataUtil class

}