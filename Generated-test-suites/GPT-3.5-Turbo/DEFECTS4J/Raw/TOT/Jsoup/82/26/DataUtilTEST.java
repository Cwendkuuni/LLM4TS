package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, "UTF-8", "");
            assertNotNull(doc);
            // Test if the document is loaded correctly
            assertEquals(1, doc.children().size());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "");
            assertNotNull(doc);
            // Test if the document is loaded correctly
            assertEquals(1, doc.children().size());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
            assertNotNull(doc);
            // Test if the document is loaded correctly
            assertEquals(1, doc.children().size());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            assertEquals(45, outputStream.size());
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }

    @Test
    public void testParseInputStream() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        try {
            Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
            assertNotNull(doc);
            // Test if the document is parsed correctly
            assertEquals(1, doc.children().size());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 5);
            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.limit() > 0);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);

        charset = DataUtil.getCharsetFromContentType("text/html");
        assertEquals(null, charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 4);
            DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
            assertNotNull(bomCharset);
            assertEquals("UTF-8", bomCharset.charset);
            assertEquals(true, bomCharset.offset);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testLoadFileIOException() {
        File file = new File("non_existent_file.html");
        try {
            DataUtil.load(file, "UTF-8", "");
            fail();
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testLoadInputStreamIOException() {
        InputStream inputStream = new ByteArrayInputStream("".getBytes());
        try {
            DataUtil.load(inputStream, "UTF-8", "");
            fail();
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testParseInputStreamIOException() {
        InputStream inputStream = new ByteArrayInputStream("".getBytes());
        try {
            DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
            fail();
        } catch (IOException e) {
            // Expected IOException
        }
    }

}