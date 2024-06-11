package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("path/to/file.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInvalidFile() throws IOException {
        File file = new File("path/to/invalid/file");
        try {
            Document doc = DataUtil.load(file, "UTF-8", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadNonExistentFile() throws IOException {
        File file = new File("path/to/non-existent/file");
        try {
            Document doc = DataUtil.load(file, "UTF-8", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadNullCharsetName() throws IOException {
        File file = new File("path/to/file.html");
        try {
            Document doc = DataUtil.load(file, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInvalidCharsetName() throws IOException {
        File file = new File("path/to/file.html");
        try {
            Document doc = DataUtil.load(file, "invalidCharset", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLoadNullBaseUri() throws IOException {
        File file = new File("path/to/file.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadEmptyBaseUri() throws IOException {
        File file = new File("path/to/file.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadNullBaseUriWithNullCharsetName() throws IOException {
        File file = new File("path/to/file.html");
        try {
            Document doc = DataUtil.load(file, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testLoadNullInputStream() throws IOException {
        try {
            Document doc = DataUtil.load(null, "UTF-8", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInvalidCharsetNameInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        try {
            Document doc = DataUtil.load(inputStream, "invalidCharset", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        inputStream.close();
    }

    @Test
    public void testLoadNullBaseUriInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testLoadEmptyBaseUriInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testLoadNullBaseUriWithNullCharsetNameInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        try {
            Document doc = DataUtil.load(inputStream, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testLoadNullParser() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        inputStream.close();
    }

    @Test
    public void testLoadParserNotSupported() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", null, new UnsupportedParser());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        inputStream.close();
    }

    @Test
    public void testLoadFileWithInvalidFile() throws IOException {
        File file = new File("path/to/invalid/file");
        try {
            Document doc = DataUtil.load(file, "UTF-8", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetName() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        try {
            Document doc = DataUtil.load(inputStream, "invalidCharset", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        inputStream.close();
    }

    @Test
    public void testLoadNullCharsetNameWithNullInputStream() throws IOException {
        try {
            Document doc = DataUtil.load(null, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLoadNullCharsetNameWithFile() throws IOException {
        File file = new File("path/to/file.html");
        try {
            Document doc = DataUtil.load(file, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLoadFileWithEmptyBaseUri() throws IOException {
        File file = new File("path/to/file.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithEmptyBaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testLoadFileWithNullBaseUri() throws IOException {
        File file = new File("path/to/file.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithNullBaseUri() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        inputStream.close();
    }
}