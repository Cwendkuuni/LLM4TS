package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithCharsetAndBaseUri() {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8' /></head><body>\n<h1>Hello World</h1>\n</body></html>";
        File inputFile = new File("input.html");
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document doc = null;
        try {
            doc = DataUtil.load(inputFile, "UTF-8", "/base-uri/");
            assertEquals("/base-uri/", doc.baseUri());
            assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inputFile.delete();
        }
    }

    @Test
    public void testLoadFileWithDefaultCharsetAndBaseUri() {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8' /></head><body>\n<h1>Hello World</h1>\n</body></html>";
        File inputFile = new File("input.html");
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document doc = null;
        try {
            doc = DataUtil.load(inputFile, null, "/base-uri/");
            assertEquals("/base-uri/", doc.baseUri());
            assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inputFile.delete();
        }
    }

    @Test
    public void testLoadInputStreamWithCharsetAndBaseUri() {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8' /></head><body>\n<h1>Hello World</h1>\n</body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"))) {
            Document doc = DataUtil.load(inputStream, "UTF-8", "/base-uri/");
            assertEquals("/base-uri/", doc.baseUri());
            assertNotNull(doc);
        }
    }

    @Test
    public void testLoadInputStreamWithDefaultCharsetAndBaseUri() {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8' /></head><body>\n<h1>Hello World</h1>\n</body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"))) {
            Document doc = DataUtil.load(inputStream, null, "/base-uri/");
            assertEquals("/base-uri/", doc.baseUri());
            assertNotNull(doc);
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        String fileContent = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"))) {
            Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
            assertNotNull(doc);
        }
    }

    @Test
    public void testParseByteDataWithCharsetDetection() {
        String fileContent = "<html><head><title>Test</title><meta charset='ISO-8859-1'></head><body><p>Hello World</p></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"))) {
            Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, null, Parser.htmlParser());
            assertEquals("ISO-8859-1", doc.outputSettings().charset());
            assertNotNull(doc);
        }
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String fileContent = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"))) {
            Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "UTF-8", null, Parser.htmlParser());
            assertEquals("UTF-8", doc.outputSettings().charset());
            assertNotNull(doc);
        }
    }

    @Test
    public void testReadToByteBuffer() {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8' /></head><body>\n<h1>Hello World</h1>\n</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedBytes = fileContent.getBytes("UTF-8");
        assertEquals(expectedBytes.length, byteBuffer.limit());
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], byteBuffer.get(i));
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8' /></head><body>\n<h1>Hello World</h1>\n</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 5);
        assertEquals(5, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        assertEquals(expectedCharset, DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeNullCharset() {
        String contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}