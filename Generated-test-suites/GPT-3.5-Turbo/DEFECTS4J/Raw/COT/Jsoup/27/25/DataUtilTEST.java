
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Internal static utilities for handling data.
 *
 */
public class DataUtilTest {
    @Test
    public void testLoad_File_CharsetName_BaseUri() {
        File in = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        try {
            Document doc = DataUtil.load(in, charsetName, baseUri);
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri() {
        InputStream in = new ByteArrayInputStream("Test HTML".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        try {
            Document doc = DataUtil.load(in, charsetName, baseUri);
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri_Parser() {
        InputStream in = new ByteArrayInputStream("Test HTML".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document doc = DataUtil.load(in, charsetName, baseUri, parser);
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("Test HTML".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() {
        byte[] buffer = "Test HTML".getBytes();
        InputStream inStream = new ByteArrayInputStream(buffer);
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
            Assert.assertNotNull(byteData);
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }
   
}
