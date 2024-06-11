package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;


public class DataUtilTest {
    @Test
    public void testLoad() {
        // Test case 1
        try {
            Document doc = DataUtil.load(new File("test.html"), "UTF-8", "http://www.example.com");
        } catch(IOException e) {
            fail("Should not throw IOException");
        }

        // Test case 2
        try {
            Document doc = DataUtil.load(new File("test.html"), null, "http://www.example.com");
        } catch(IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testParseInputStream() {
        // Test case 1
        try {
            Document doc = DataUtil.parseInputStream(new FileInputStream("test.html"), "UTF-8", "http://www.example.com", Parser.htmlParser());
        } catch(IOException e) {
            fail("Should not throw IOException");
        }

        // Test case 2
        try {
            Document doc = DataUtil.parseInputStream(new FileInputStream("test.html"), null, "http://www.example.com", Parser.htmlParser());
        } catch(IOException e) {
            fail("Should not throw IOException");
        }

        // Test case 3
        try {
            Document doc = DataUtil.parseInputStream(new FileInputStream("test.html"), "UTF-8", "http://www.example.com", null);
        } catch(IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testCrossStreams() {
        // Test case 1
        try {
            DataUtil.crossStreams(new FileInputStream("input.txt"), new FileOutputStream("output.txt"));
        } catch(IOException e) {
            fail("Should not throw IOException");
        }

        // Test case 2
        try {
            DataUtil.crossStreams(new FileInputStream("input.txt"), null);
        } catch(IOException e) {
            fail("Should not throw IOException");
        }

        // Test case 3
        try {
            DataUtil.crossStreams(null, new FileOutputStream("output.txt"));
        } catch(IOException e) {
            fail("Should not throw IOException");
        }

        // Test case 4
        try {
            DataUtil.crossStreams(null, null);
        } catch(IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        // Test case 1
        try {
            ByteBuffer buffer = DataUtil.readToByteBuffer(new FileInputStream("input.txt"), 1024);
            assertEquals(1024, buffer.capacity());
        } catch(IOException e) {
            fail("Should not throw IOException");
        }

        // Test case 2
        try {
            ByteBuffer buffer = DataUtil.readToByteBuffer(null, 1024);
            assertNull(buffer);
        } catch(IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertEquals(0, buffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test case 1
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);

        // Test case 2
        charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);

        // Test case 3
        charset = DataUtil.getCharsetFromContentType("text/html");
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(32, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        // Test case 1
        ByteBuffer buffer = ByteBuffer.wrap(new byte[] {0x00, 0x00, (byte) 0xFE, (byte) 0xFF});
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(buffer);
        assertEquals("UTF-32", bomCharset.charset);
        assertFalse(bomCharset.offset);

        // Test case 2
        buffer = ByteBuffer.wrap(new byte[] {(byte) 0xFF, (byte) 0xFE, 0x00, 0x00});
        bomCharset = DataUtil.detectCharsetFromBom(buffer);
        assertEquals("UTF-16", bomCharset.charset);
        assertFalse(bomCharset.offset);

        // Test case 3
        buffer = ByteBuffer.wrap(new byte[] {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        bomCharset = DataUtil.detectCharsetFromBom(buffer);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }
}