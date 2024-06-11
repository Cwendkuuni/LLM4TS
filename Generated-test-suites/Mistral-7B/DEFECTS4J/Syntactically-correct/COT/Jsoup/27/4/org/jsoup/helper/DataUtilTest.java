package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.parser.ParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     */
    @Test
    public void testLoadFile() throws IOException, HelperException, ParserException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("path/to/your/file.html");
        Document document = DataUtil.load(file, charset, baseUri);
        // add assertions here
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     */
    @Test
    public void testLoadInputStream() throws IOException, HelperException, ParserException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        ByteArrayInputStream inputStream = new ByteArrayInputStream("your input stream data here".getBytes());
        Document document = DataUtil.load(inputStream, charset, baseUri);
        // add assertions here
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException, HelperException, ParserException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        ByteArrayInputStream inputStream = new ByteArrayInputStream("your input stream data here".getBytes());
        Parser parser = Parser.xmlParser(); // use xml parser instead of html parser
        Document document = DataUtil.load(inputStream, charset, baseUri, parser);
        // add assertions here
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
     */
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());
        // add assertions here

        byteData.put("your byte data here".getBytes());
        byteData.flip();

        charset = null; // test case for no charset provided
        document = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());
        // add assertions here

        charset = "GB2312";
        byteData.rewind();
        document = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());
        // add assertions here
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream)}.
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("your input stream data here".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        // add assertions here

        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byteData = DataUtil.readToByteBuffer(inputStream);
        // add assertions here
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("EUC-JP", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(DataUtil.defaultCharset, charset);

        contentType = null;
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }
}