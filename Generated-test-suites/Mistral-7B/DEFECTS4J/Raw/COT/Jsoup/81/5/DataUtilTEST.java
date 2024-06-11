package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.junit.Test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     */
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with a valid file path
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     */
    @Test
    public void testLoadInputStreamParser() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", "", DataUtil.Parser.xmlParser());
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String, Parser)}.
     */
    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", DataUtil.Parser.htmlParser());
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#crossStreams(InputStream, OutputStream)}.
     */
    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source data";
        String target = "Target data";

        InputStream inputStream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] sourceBytes = inputStream.readAllBytes();
        byte[] targetBytes = outputStream.toByteArray();

        assertNotNull("Copying from InputStream to OutputStream failed.", new String(targetBytes, StandardCharsets.UTF_8));
        assertArrayEquals("Data not copied properly.", sourceBytes, targetBytes);
    }

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String)}.
     */
    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String invalidCharset = "ISO-12345";

        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        assertThrows("IOException should be thrown when parsing with an unsupported charset.", IOException.class,
                () -> DataUtil.parseInputStream(inputStream, invalidCharset, ""));
    }

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String)}.
     */
    @Test
    public void testParseInputStreamWithEmptyInputStream() throws IOException {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);

        Document doc = DataUtil.parseInputStream(emptyInputStream, "UTF-8", "");
        assertNotNull(doc); // An empty Document is a valid result
    }
}