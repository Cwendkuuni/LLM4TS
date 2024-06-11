package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.jsoup.helper.DataUtil;
import org.jsoup.parser.Parser;

import org.junit.Test;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadFile() throws IOException {
        InputStream expectedInputStream = this.getClass().getResourceAsStream("/test.html");
        File testFile = new File(expectedInputStream.getFD().getFile());
        Document expectedDocument = DataUtil.load(testFile, "UTF-8", "/test");

        Document actualDocument = DataUtil.load(testFile, "UTF-8", "/test");

        assertEquals(expectedDocument, actualDocument);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"<html><body><h1>Test</h1></body></html>"
                .getBytes(StandardCharsets.UTF_8)});

        Document expectedDocument = new Document() {
            {
                setBaseUri("/test");
                body().text(test -> test.html("Test").select("h1").text("Test"));
            }
        };

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "/test");

        assertEquals(expectedDocument, actualDocument);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)} with htmlParser.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStreamHtmlParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"<html><body><h1>Test</h1></body></html>"
                .getBytes(StandardCharsets.UTF_8)});

        Document expectedDocument = new Document() {
            {
                setBaseUri("/test");
                body().text(test -> test.html("Test").select("h1").text("Test"));
            }
        };

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "/test", Parser.htmlParser());

        assertEquals(expectedDocument, actualDocument);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)} with xmlParser.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadInputStreamXmlParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">"
                + "<head><meta charset=\"UTF-8\"/>"
                + "<title>Test</title></head><body><h1>Test</h1></body></html>"
                .getBytes(StandardCharsets.UTF_8)});

        Document expectedDocument = new Document() {
            {
                setBaseUri("/test");
                body().text(test -> test.html("Test").select("h1").text("Test"));
                metaEncoding("UTF-8");
            }
        };

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "/test", Parser.xmlParser());

        assertEquals(expectedDocument, actualDocument);
    }

    /**
     * Test method for {@link DataUtil#crossStreams(InputStream, OutputStream)}.
     * @throws IOException on IO error
     */
    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{"Test".getBytes(StandardCharsets.UTF_8)});
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expectedBytes = new byte[]{"Test".getBytes(StandardCharsets.UTF_8)};
        byte[] actualBytes = outputStream.toByteArray();

        assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String, Parser)} with invalid charset.
     * @throws IOException on IO error
     */
    @Test
    public void testParseInputStreamInvalidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"<html><body><h1>Test</h1></body></html>"
                .getBytes(StandardCharsets.UTF_8)});

        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.parseInputStream(inputStream, null, "/test", Parser.htmlParser());
        });
    }

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String, Parser)} with null parser.
     * @throws IOException on IO error
     */
    @Test
    public void testParseInputStreamNullParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"<html><body><h1>Test</h1></body></html>"
                .getBytes(StandardCharsets.UTF_8)});

        assertThrows(NullPointerException.class, () -> {
            DataUtil.parseInputStream(inputStream, "UTF-8", "/test", null);
        });
    }
}