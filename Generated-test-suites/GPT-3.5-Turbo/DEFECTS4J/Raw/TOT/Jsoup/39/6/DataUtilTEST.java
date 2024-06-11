
import org.jsoup.helper.DataUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {
    private File testFile;

    @Before
    public void setUp() {
        testFile = new File("testfile.html");
        try {
            PrintWriter writer = new PrintWriter(testFile);
            writer.println("<html><body><h1>Hello World!</h1></body></html>");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadWithFile() throws IOException {
        String expected = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        Document doc = DataUtil.load(testFile, "UTF-8", "http://example.com");
        assertEquals(expected, doc.outerHtml());
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        String expected = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        FileInputStream inStream = new FileInputStream(testFile);
        Document doc = DataUtil.load(inStream, "UTF-8", "http://example.com");
        assertEquals(expected, doc.outerHtml());
        inStream.close();
    }

    @Test
    public void testLoadWithInputStreamAndParser() throws IOException {
        String expected = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        FileInputStream inStream = new FileInputStream(testFile);
        Document doc = DataUtil.load(inStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertEquals(expected, doc.outerHtml());
        inStream.close();
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        String expected = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(expected.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());
        assertEquals(expected, doc.outerHtml());
    }

    @Test
    public void testParseByteDataWithCharset() {
        String dataWithCharset = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        String expected = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(dataWithCharset.getBytes());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());
        assertEquals(expected, doc.outerHtml());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String data = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        File testFile = new File("testfile.html");
        PrintWriter writer = new PrintWriter(testFile);
        writer.println(data);
        writer.close();

        FileInputStream inStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, data.length());
        String byteString = new String(byteData.array(), "UTF-8");
        assertEquals(data, byteString);
        inStream.close();
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        String data = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        File testFile = new File("testfile.html");
        PrintWriter writer = new PrintWriter(testFile);
        writer.println(data);
        writer.close();

        FileInputStream inStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        String byteString = new String(byteData.array(), "UTF-8");
        assertEquals(data, byteString);
        inStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expected = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expected, charset);
    }
}
