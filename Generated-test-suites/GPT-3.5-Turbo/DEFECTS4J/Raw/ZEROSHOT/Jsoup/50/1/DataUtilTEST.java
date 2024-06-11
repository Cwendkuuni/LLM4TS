import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

public class DataUtilTest {

    private Document document;

    @Before
    public void setUp() throws IOException {
        File testFile = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(testFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println("<html><head></head><body><h1>Hello World</h1></body></html>");
        printWriter.close();
        document = DataUtil.load(testFile, "UTF-8", "");
    }

    @Test
    public void testLoad() throws IOException {
        assertNotNull(document);
        assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        FileInputStream inputStream = new FileInputStream("testFile.html");
        document = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(document);
        assertEquals("Hello World", document.select("h1").text());
        inputStream.close();
    }

    @Test
    public void testLoadWithParser() throws IOException {
        FileInputStream inputStream = new FileInputStream("testFile.html");
        Parser parser = Parser.htmlParser();
        document = DataUtil.load(inputStream, "UTF-8", "", parser);
        assertNotNull(document);
        assertEquals("Hello World", document.select("h1").text());
        inputStream.close();
    }


    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileInputStream inputStream = new FileInputStream("testFile.html");
        DataUtil.crossStreams(inputStream, outputStream);
        assertNotNull(outputStream);
        inputStream.close();
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("Hello World".getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("Hello World", document.select("body").text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream inputStream = new FileInputStream("testFile.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        inputStream.close();
    }

    @Test
    public void testReadToByteBufferCapped() throws IOException {
        FileInputStream inputStream = new FileInputStream("testFile.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        assertNotNull(byteBuffer);
        assertEquals(10, byteBuffer.capacity());
        inputStream.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File testFile = new File("testFile.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer emptyBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(emptyBuffer);
        assertEquals(0, emptyBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNotFound() {
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }
}