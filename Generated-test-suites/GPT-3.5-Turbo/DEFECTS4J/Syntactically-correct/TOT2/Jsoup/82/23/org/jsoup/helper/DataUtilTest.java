import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private File testFile;

    @Before
    public void setUp() {
        testFile = new File("testFile.html");
    }

    @Test
    public void testLoadFromFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Document should not be null", doc.title(), "Test File");
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Document should not be null", doc.title(), "Test File");
    }

    @Test
    public void testLoadFromInputStreamWithCustomParser() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("Document should not be null", doc.title(), "Test File");
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new FileInputStream(testFile), byteArrayOutputStream);
        byte[] outputBytes = byteArrayOutputStream.toByteArray();
        assertNotNull(outputBytes);
        assertEquals("Output bytes length should match input file length", testFile.length(), outputBytes.length);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        assertEquals("Byte buffer should not be null", byteBuffer.limit(), 1024);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals("Boundary length should match expected length", boundary.length(), DataUtil.boundaryLength);
    }
}