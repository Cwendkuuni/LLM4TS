package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

public class DataUtilTest {

    @Test
    public void test_load_withFileAndCharsetNameAndBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        Document doc = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(doc);
        // add more assertions here
    }

    @Test
    public void test_load_withInputStreamAndCharsetNameAndBaseUri() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        Document doc = DataUtil.load(in, charsetName, baseUri);

        assertNotNull(doc);
        // add more assertions here
    }

    @Test
    public void test_load_withInputStreamAndCharsetNameAndBaseUriAndParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.load(in, charsetName, baseUri, parser);

        assertNotNull(doc);
        // add more assertions here
    }

    @Test
    public void test_parseByteData_withByteBufferAndCharsetNameAndBaseUriAndParser() {
        // implement test
    }

    @Test
    public void test_readToByteBuffer_withInputStreamAndMaxSize() throws IOException {
        InputStream in = new FileInputStream("test.html");
        int maxSize = 0;

        ByteBuffer byteData = DataUtil.readToByteBuffer(in, maxSize);

        assertNotNull(byteData);
        // add more assertions here
    }

    @Test
    public void test_readToByteBuffer_withInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");

        ByteBuffer byteData = DataUtil.readToByteBuffer(in);

        assertNotNull(byteData);
        // add more assertions here
    }

    @Test
    public void test_getCharsetFromContentType_withContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
        // add more assertions here
    }
}