import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void loadFile() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void loadInputStream() {
        InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Hello World</h1></body></html>".getBytes());
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void loadInputStreamWithParser() {
        InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Hello World</h1></body></html>".getBytes());
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void crossStreams() {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            assertEquals("Hello World", outputStream.toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void parseInputStream() {
        InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Hello World</h1></body></html>".getBytes());
        try {
            Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void readToByteBuffer() {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        try {
            assertNotNull(DataUtil.readToByteBuffer(inputStream, 0));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void readFileToByteBuffer() {
        File file = new File("test.txt");
        try {
            assertNotNull(DataUtil.readFileToByteBuffer(file));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void emptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertEquals(0, buffer.capacity());
    }
    
    @Test
    public void getCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void getCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void validateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void validateCharsetWithInvalidCharset() {
        String charset = "InvalidCharset";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void mimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }
}