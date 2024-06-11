
import org.junit.Test;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Test loading a file with UTF-8 charset
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            // Perform assertions on the loaded Document
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        // Test loading an InputStream with UTF-8 charset
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            // Perform assertions on the loaded Document
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        // Test loading an InputStream with a custom Parser
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.xml");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
            // Perform assertions on the loaded Document
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
}
