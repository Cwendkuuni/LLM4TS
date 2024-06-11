import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private String baseUri;

    @Before
    public void setUp() {
        baseUri = "http://example.com";
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", baseUri);
        assertNotNull(document);
        // Add more assertions if needed
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        try (InputStream inputStream = new FileInputStream("test.html")) {
            Document document = DataUtil.load(inputStream, "UTF-8", baseUri);
            assertNotNull(document);
            // Add more assertions if needed
        }
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        try (InputStream inputStream = new FileInputStream("test.html")) {
            Document document = DataUtil.load(inputStream, "UTF-8", baseUri, Parser.xmlParser());
            assertNotNull(document);
            // Add more assertions if needed
        }
    }

    // Add more test cases as needed

}