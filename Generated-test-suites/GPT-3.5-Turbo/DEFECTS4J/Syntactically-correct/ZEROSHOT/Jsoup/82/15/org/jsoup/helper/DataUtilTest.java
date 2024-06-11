
package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    private Document doc;

    @Before
    public void setUp() {
        File file = new File("example.html");
        try {
            doc = DataUtil.load(file, "UTF-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromFile() {
        assertNotNull("Document is null", doc);
    }

    @Test
    public void testLoadFromInputStream() {
        File file = new File("example.html");
        try {
            InputStream inputStream = new FileInputStream(file);
            doc = DataUtil.load(inputStream, "UTF-8", "");
            assertNotNull("Document is null", doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        File file = new File("example.html");
        try {
            InputStream inputStream = new FileInputStream(file);
            doc = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
            assertNotNull("Document is null", doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream inputStream = new FileInputStream("example.html");
            OutputStream outputStream = new FileOutputStream("output.html");
            DataUtil.crossStreams(inputStream, outputStream);
            File outputFile = new File("output.html");
            assertTrue("Output file does not exist", outputFile.exists());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInputStream() {
        File file = new File("example.html");
        try {
            InputStream inputStream = new FileInputStream(file);
            doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
            assertNotNull("Document is null", doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBuffer() {
        File file = new File("example.html");
        try {
            InputStream inputStream = new FileInputStream(file);
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            assertNotNull("ByteBuffer is null", byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull("ByteBuffer is null", byteBuffer);
        assertEquals("ByteBuffer is not empty", 0, byteBuffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("Charset is incorrect", "UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull("Charset should be null", charset);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        String charset = DataUtil.validateCharset(cs);
        assertEquals("Charset is incorrect", "UTF-8", charset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String cs = "InvalidCharset";
        String charset = DataUtil.validateCharset(cs);
        assertNull("Charset should be null", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull("Mime Boundary is null", mimeBoundary);
        assertEquals("Mime Boundary is incorrect", 32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull("BomCharset is null", bomCharset);
        assertEquals("Charset is incorrect", "UTF-8", bomCharset.charset);
        assertTrue("Offset flag is incorrect", bomCharset.offset);
    }
}
