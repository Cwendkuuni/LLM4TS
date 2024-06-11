import static org.junit.Assert.*;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoad_file_NullCharset() {
        try {
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(new File("test.html"), null, baseUri);
            assertNotNull(doc);
            // Add more assertions here...
        } catch (IOException e) {
            fail("IO Exception thrown");
        }
    }

    @Test
    public void testLoad_file_EmptyCharset() {
        try {
            String charset = "";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(new File("test.html"), charset, baseUri);
            assertNotNull(doc);
            // Add more assertions here...
        } catch (IOException e) {
            fail("IO Exception thrown");
        }
    }

    @Test
    public void testLoad_file_ValidCharset() {
        try {
            String charset = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(new File("test.html"), charset, baseUri);
            assertNotNull(doc);
            // Add more assertions here...
        } catch (IOException e) {
            fail("IO Exception thrown");
        }
    }

    @Test
    public void testLoad_inputStream_NullCharset() {
        try {
            String baseUri = "http://example.com";
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.load(in, null, baseUri);
            assertNotNull(doc);
            // Add more assertions here...
        } catch (IOException e) {
            fail("IO Exception thrown");
        }
    }

    @Test
    public void testLoad_inputStream_EmptyCharset() {
        try {
            String charset = "";
            String baseUri = "http://example.com";
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.load(in, charset, baseUri);
            assertNotNull(doc);
            // Add more assertions here...
        } catch (IOException e) {
            fail("IO Exception thrown");
        }
    }

    @Test
    public void testLoad_inputStream_ValidCharset() {
        try {
            String charset = "UTF-8";
            String baseUri = "http://example.com";
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.load(in, charset, baseUri);
            assertNotNull(doc);
            // Add more assertions here...
        } catch (IOException e) {
            fail("IO Exception thrown");
        }
    }

    @Test
    public void testLoad_inputStream_CustomParser() {
        try {
            String charset = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.xmlParser();
            InputStream in = new FileInputStream("test.xml");
            Document doc = DataUtil.load(in, charset, baseUri, parser);
            assertNotNull(doc);
            // Add more assertions here...
        } catch (IOException e) {
            fail("IO Exception thrown");
        }
    }
    
    @Test
    public void testCrossStreams() {
        try {
            InputStream in = new FileInputStream("input.txt");
            OutputStream out = new FileOutputStream("output.txt");
            DataUtil.crossStreams(in, out);
            // Add assertions for verifying the output file content
        } catch (IOException e) {
            fail("IO Exception thrown");
        }
    }

    // Add more test cases for other methods...

    // Edge cases and error scenarios...

    @Test(expected = IOException.class)
    public void testLoad_file_InvalidFile() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(new File("invalid.html"), charset, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoad_inputStream_InvalidStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        InputStream in = new FileInputStream("invalid.html");
        Document doc = DataUtil.load(in, charset, baseUri);
    }

    // Add more error scenarios and edge cases...

}