import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            Document document = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            String html = "<html><head></head><body></body></html>";
            InputStream inputStream = new StringBufferInputStream(html);
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            String input = "test input";
            InputStream inputStream = new StringBufferInputStream(input);
            StringBuilder output = new StringBuilder();
            DataUtil.crossStreams(inputStream, output);
            assertEquals(input, output.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Add more test cases for other methods

}