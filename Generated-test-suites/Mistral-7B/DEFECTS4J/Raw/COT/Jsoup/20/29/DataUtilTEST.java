package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html"); // Assuming that test.html exists and is accessible.
        Document doc = DataUtil.load(file, charsetName, baseUri);
        // Add assertions here to test the returned document.
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write("Test content".getBytes());
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Document doc = DataUtil.load(is, charsetName, baseUri);
        // Add assertions here to test the returned document.
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write("Test content".getBytes());
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Document doc = DataUtil.load(is, charsetName, baseUri, Parser.xmlParser());
        // Add assertions here to test the returned document.
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteData() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteArrayInputStream is = new ByteArrayInputStream("Test content".getBytes());
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(is);
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
            // Add assertions here to test the returned document.
        } finally {
            is.close();
        }
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream)}.
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write("Test content".getBytes());
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(is);
        // Add assertions here to test the byte buffer.
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String expectedCharset = "EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }

    /**
     * Test loading a file with a non-existent file path.
     */
    @Test(expected = FileNotFoundException.class)
    public void testLoadFileNonExistentFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("non-existent-file.html");
        DataUtil.load(file, charsetName, baseUri);
    }

    /**
     * Test loading an input stream with null character set name.
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testLoadInputStreamNullCharsetName() throws IOException {
        String baseUri = "http://example.com";
        InputStream is = new ByteArrayInputStream("Test content".getBytes());
        DataUtil.load(is, null, baseUri);
    }

    /**
     * Test loading an input stream with an empty base URI.
     * @throws IOException
     */
    @Test
    public void testLoadInputStreamEmptyBaseUri() throws IOException {
        String charsetName = "UTF-8";
        String emptyBaseUri = "";
        InputStream is = new ByteArrayInputStream("Test content".getBytes());
        Document doc = DataUtil.load(is, charsetName, emptyBaseUri);
        // Add assertions here to test the returned document.
    }
}