import junit.framework.TestCase;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest extends TestCase {

    //Helper method to compare two byte arrays
    private boolean compareByteArray(byte[] arr1, byte[] arr2) {
        if (arr1.length != arr2.length)
            return false;

        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i])
                return false;
        }

        return true;
    }

    //Helper method to compare two ByteBuffers
    private boolean compareByteBuffer(ByteBuffer buffer1, ByteBuffer buffer2) {
        byte[] arr1 = buffer1.array();
        byte[] arr2 = buffer2.array();

        return compareByteArray(arr1, arr2);
    }

    //Helper method to compare two Documents
    private boolean compareDocuments(Document doc1, Document doc2) {
        return doc1.html().equals(doc2.html());
    }

    //Test load(File in, String charsetName, String baseUri)
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document expected = new Document(baseUri);
        Document actual = DataUtil.load(file, charsetName, baseUri);
      
        assertEquals(compareDocuments(expected, actual), true);
    }

    //Test load(InputStream in, String charsetName, String baseUri)
    public void testLoadInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document expected = new Document(baseUri);
        Document actual = DataUtil.load(in, charsetName, baseUri);
      
        assertEquals(compareDocuments(expected, actual), true);
    }

    //Test load(InputStream in, String charsetName, String baseUri, Parser parser)
    public void testLoadParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document expected = new Document(baseUri);
        Document actual = DataUtil.load(in, charsetName, baseUri, parser);
      
        assertEquals(compareDocuments(expected, actual), true);
    }

    //Test crossStreams(InputStream in, OutputStream out)
    public void testCrossStreams() throws IOException {
        InputStream in = getClass().getResourceAsStream("input.txt");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
      
        byte[] expected = "This is a test.".getBytes();
        byte[] actual = out.toByteArray();
      
        assertEquals(compareByteArray(expected, actual), true);
    }

    //Test parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Hello, World!</h1></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document expected = new Document(baseUri);
        expected.body().append("<h1>Hello, World!</h1>");
        Document actual = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
      
        assertEquals(compareDocuments(expected, actual), true);
    }

    //Test readToByteBuffer(InputStream inStream, int maxSize)
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = getClass().getResourceAsStream("input.txt");
        int maxSize = 0;
        byte[] expected = "This is a test.".getBytes();
        ByteBuffer actual = DataUtil.readToByteBuffer(inStream, maxSize);
      
        assertEquals(compareByteBuffer(ByteBuffer.wrap(expected), actual), true);
    }

    //Test readToByteBuffer(InputStream inStream)
    public void testReadToByteBufferUnlimited() throws IOException {
        InputStream inStream = getClass().getResourceAsStream("input.txt");
        byte[] expected = "This is a test.".getBytes();
        ByteBuffer actual = DataUtil.readToByteBuffer(inStream);
      
        assertEquals(compareByteBuffer(ByteBuffer.wrap(expected), actual), true);
    }

    //Test readFileToByteBuffer(File file)
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");
        byte[] expected = "This is a test.".getBytes();
        ByteBuffer actual = DataUtil.readFileToByteBuffer(file);
      
        assertEquals(compareByteBuffer(ByteBuffer.wrap(expected), actual), true);
    }

    //Test emptyByteBuffer()
    public void testEmptyByteBuffer() {
        ByteBuffer expected = ByteBuffer.allocate(0);
        ByteBuffer actual = DataUtil.emptyByteBuffer();
      
        assertTrue(expected.equals(actual));
    }

    //Test getCharsetFromContentType(String contentType)
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expected = "UTF-8";
        String actual = DataUtil.getCharsetFromContentType(contentType);
      
        assertEquals(expected, actual);
    }

    //Test mimeBoundary()
    public void testMimeBoundary() {
        String actual = DataUtil.mimeBoundary();
      
        assertEquals(actual.length(), 32);
    }
}