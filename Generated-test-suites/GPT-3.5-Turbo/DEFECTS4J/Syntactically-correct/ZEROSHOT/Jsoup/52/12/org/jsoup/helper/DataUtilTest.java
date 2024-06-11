import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private File file;
    private InputStream inputStream;
    private String charsetName;
    private String baseUri;
    private Parser parser;

    @Before
    public void setup() {
        file = new File("testFile.html");
        inputStream = new ByteArrayInputStream("<html><body><h1>Hello World!</h1></body></html>".getBytes());
        charsetName = "UTF-8";
        baseUri = "http://www.example.com";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        OutputStream outputStream = new OutputStream() {
            private StringBuilder stringBuilder = new StringBuilder();

            @Override
            public void write(int b) throws IOException {
                stringBuilder.append((char) b);
            }

            @Override
            public String toString() {
                return stringBuilder.toString();
            }
        };
        DataUtil.crossStreams(inputStream, outputStream);
        String output = outputStream.toString();
        assertEquals("<html><body><h1>Hello World!</h1></body></html>", output);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Hello World!</h1></body></html>".getBytes());
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("Hello World!", doc.select("h1").text());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        int maxSize = 5000;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteBuffer);
        assertEquals(40, byteBuffer.capacity());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        assertEquals(40, byteBuffer.capacity());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
        assertEquals(40, byteBuffer.capacity());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Hello World!</h1></body></html>".getBytes());
        String charset = DataUtil.detectCharsetFromBom(byteData, "UTF-8");
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

}