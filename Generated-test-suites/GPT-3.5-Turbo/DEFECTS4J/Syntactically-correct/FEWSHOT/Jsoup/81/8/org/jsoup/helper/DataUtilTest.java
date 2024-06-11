import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoad() {
        try {
            Document doc = DataUtil.load(new FileInputStream("file.html"), "UTF-8", "http://www.example.com");
            assertEquals("http://www.example.com", doc.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadWithCharset() {
        try {
            Document doc = DataUtil.load(new FileInputStream("file.html"), "ISO-8859-1", "http://www.example.com");
            assertEquals("ISO-8859-1", doc.outputSettings().charset().displayName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadWithParser() {
        try {
            Document doc = DataUtil.load(new FileInputStream("file.html"), "UTF-8", "http://www.example.com", Parser.xmlParser());
            assertEquals(Parser.xmlParser(), doc.parser());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream in = new FileInputStream("input.txt");
            OutputStream out = new FileOutputStream("output.txt");
            DataUtil.crossStreams(in, out);
            // Assert file contents
            String expected = "This is the input file.";
            String result = new String(Files.readAllBytes(Paths.get("output.txt")));
            assertEquals(expected, result);
            // Clean up
            in.close();
            out.close();
            Files.deleteIfExists(Paths.get("output.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            Document doc = DataUtil.parseInputStream(new FileInputStream("file.html"), "UTF-8", "http://www.example.com", Parser.htmlParser());
            assertEquals("http://www.example.com", doc.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream in = new FileInputStream("file.txt");
            ByteBuffer buffer = DataUtil.readToByteBuffer(in);
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            String result = new String(bytes);
            assertEquals("This is the file contents.", result);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            ByteBuffer buffer = DataUtil.readFileToByteBuffer(new File("file.txt"));
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            String result = new String(bytes);
            assertEquals("This is the file contents.", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}