package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseByteData() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        String docData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<h1>Test</h1>\n</body>\n</html>";
        InputStream in = new ByteArrayInputStream(docData.getBytes(Charset.forName(charsetName)));
        Document doc = DataUtil.load(in, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(docData, doc.body().html());

        charsetName = null;
        docData = "<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n<title>Test</title>\n</head>\n<body>\n<h1>Test</h1>\n</body>\n</html>";
        in = new ByteArrayInputStream(docData.getBytes(Charset.forName(defaultCharset)));
        doc = DataUtil.load(in, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(docData, doc.body().html());

        charsetName = "gb2312";
        docData = "<html>\n<head>\n<meta charset=\"gb2312\">\n<title>Test</title>\n</head>\n<body>\n<h1>Test</h1>\n</body>\n</html>";
        in = new ByteArrayInputStream(docData.getBytes(Charset.forName(charsetName)));
        doc = DataUtil.load(in, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(docData, doc.body().html());
    }

    @Test
    public void testParseByteData_invalidCharset() {
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        InputStream in = new ByteArrayInputStream("Test data".getBytes());

        try {
            Document doc = DataUtil.load(in, charsetName, baseUri, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseByteData_nullCharset() {
        String baseUri = "http://example.com";
        InputStream in = new ByteArrayInputStream("Test data".getBytes());

        try {
            Document doc = DataUtil.load(in, null, baseUri, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String expected = "EUC-JP";
        String result = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expected, result);

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] bytes = "Test data".getBytes();
        InputStream in = new ByteArrayInputStream(bytes);
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        assertEquals(bytes.length, byteData.limit());
    }
}