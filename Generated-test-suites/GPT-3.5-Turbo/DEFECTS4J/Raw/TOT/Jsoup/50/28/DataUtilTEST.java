import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.*;

public class DataUtilTest {
    
    private File testFile;
    private InputStream testInputStream;
    private String testCharset;
    private String testBaseUri;

    @Before
    public void setUp() throws Exception {
        testFile = new File("test.html");
        testInputStream = new ByteArrayInputStream("<html><body><h1>Hello World!</h1></body></html>".getBytes());
        testCharset = "UTF-8";
        testBaseUri = "https://www.example.com/";
    }

    @Test
    public void testLoadFile() {
        try {
            Document doc = DataUtil.load(testFile, testCharset, testBaseUri);
            
            assertNotNull(doc);
            assertEquals(testBaseUri, doc.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException was thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            Document doc = DataUtil.load(testInputStream, testCharset, testBaseUri);
            
            assertNotNull(doc);
            assertEquals(testBaseUri, doc.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException was thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithCustomParser() {
        try {
            Parser customParser = Parser.xmlParser();
            Document doc = DataUtil.load(testInputStream, testCharset, testBaseUri, customParser);
            
            assertNotNull(doc);
            assertEquals(testBaseUri, doc.baseUri());
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException was thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            DataUtil.crossStreams(testInputStream, outStream);
            
            String expectedOutput = "<html><body><h1>Hello World!</h1></body></html>";
            String actualOutput = outStream.toString();
            
            assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException was thrown");
        }
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String testByteData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testByteData.getBytes());
        
        Document doc = DataUtil.parseByteData(byteData, null, testBaseUri, Parser.htmlParser());
        
        assertNotNull(doc);
        assertEquals(testBaseUri, doc.baseUri());
    }

    @Test
    public void testParseByteDataWithCustomCharset() {
        String testByteData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=ISO-8859-1\"></head><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testByteData.getBytes());

        Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", testBaseUri, Parser.htmlParser());

        assertNotNull(doc);
        assertEquals(testBaseUri, doc.baseUri());
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);
            byte[] expectedBytes = "<html><body><h1>Hello World!</h1></body></html>".getBytes();
            byte[] actualBytes = byteBuffer.array();
            
            assertArrayEquals(expectedBytes, actualBytes);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException was thrown");
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        try {
            int maxSize = 10;
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, maxSize);
            byte[] expectedBytes = "<html><".getBytes();
            byte[] actualBytes = byteBuffer.array();
            
            assertArrayEquals(expectedBytes, actualBytes);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException was thrown");
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
            byte[] expectedBytes = "<html><body><h1>Hello World!</h1></body></html>".getBytes();
            byte[] actualBytes = byteBuffer.array();

            assertArrayEquals(expectedBytes, actualBytes);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException was thrown");
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String testContentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(testContentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String testContentType = "text/html; charset=invalid";

        String charset = DataUtil.getCharsetFromContentType(testContentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String testContentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(testContentType);

        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }
}