import org.junit.Test;
import org.jsoup.nodes.Document;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
        //Test code here
    }

    @Test
    public void testLoadInputStream() throws Exception {
        DataUtil.load(new FileInputStream("test.html"), "UTF-8", "http://example.com");
        //Test code here
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        DataUtil.load(new FileInputStream("test.html"), "UTF-8", "http://example.com", Parser.htmlParser());
        //Test code here
    }

    @Test
    public void testCrossStreams() throws Exception {
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new FileOutputStream("output.txt");
        DataUtil.crossStreams(in, out);
        //Test code here
    }

    @Test
    public void testParseInputStream() throws Exception {
        InputStream in = new FileInputStream("test.html");
        DataUtil.parseInputStream(in, "UTF-8", "http://example.com", Parser.htmlParser());
        //Test code here
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        InputStream in = new FileInputStream("input.txt");
        DataUtil.readToByteBuffer(in, 1024);
        //Test code here
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        String contentType = "text/html; charset=UTF-8";
        DataUtil.getCharsetFromContentType(contentType);
        //Test code here
    }

    @Test
    public void testMimeBoundary() throws Exception {
        DataUtil.mimeBoundary();
        //Test code here
    }
}