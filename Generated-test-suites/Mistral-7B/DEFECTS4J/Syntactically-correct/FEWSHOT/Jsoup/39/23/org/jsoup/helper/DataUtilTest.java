package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseByteData() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        // Test with no charset specified
        String docData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(charsetName)));
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>", doc.html());

        // Test with charset determined from meta tag
        String metaCharset = "GBK";
        docData = "<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + metaCharset + "\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(charsetName)));
        doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertEquals("<html>\n<head>\n<meta charset=\"" + metaCharset + "\">\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>", doc.html());

        // Test with charset determined from HTML5 meta tag
        docData = "<html>\n<head>\n<meta charset=\"gb2312\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(charsetName)));
        doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertEquals("<html>\n<head>\n<meta charset=\"gb2312\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>", doc.html());

        // Test with charset determined from Content-Type header
        String contentType = "text/html; charset=EUC-JP";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(charsetName)));
        doc = DataUtil.parseByteData(byteData, contentType, baseUri, parser);
        assertEquals("<html>\n<head>\n<meta charset=\"EUC-JP\">\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>", doc.html());

        // Test BOM indicator
        docData = "\uFEFF<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(charsetName)));
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>", doc.html());

        // Test with unsupported charset
        String unsupportedCharset = " unknown-charset";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(charsetName)));
        doc = DataUtil.parseByteData(byteData, unsupportedCharset, baseUri, parser);
        assertNull(doc);
    }

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        File inputFile = new File("testFile.html");
        FileOutputStream outputStream = new FileOutputStream(inputFile);
        outputStream.write("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>".getBytes(Charset.forName(charsetName)));
        outputStream.close();

        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>", doc.html());

        inputFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        String docData = "<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(docData.getBytes(Charset.forName(charsetName)));

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>", doc.html());

        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "text/html; charset=gb2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("gb2312", charset);

        contentType = "text/html; charset=EUC-JP";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("EUC-JP", charset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String charsetName = "UTF-8";
        String docData = "<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(docData.getBytes(Charset.forName(charsetName)));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] expected = docData.getBytes(Charset.forName(charsetName));
        byte[] actual = new byte[byteData.limit()];
        byteData.get(actual);

        assertArrayEquals(expected, actual);

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String charsetName = "UTF-8";
        String docData = "<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(docData.getBytes(Charset.forName(charsetName)));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 5);
        assertTrue(byteData.hasRemaining());

        byteData = DataUtil.readToByteBuffer(inputStream, 15);
        assertFalse(byteData.hasRemaining());

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithNullInput() {
        ByteBuffer byteData = DataUtil.readToByteBuffer(null);
        assertNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithEmptyInputStream() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[0]));
        assertFalse(byteData.hasRemaining());
    }

    @Test
    public void testReadToByteBufferWithMaxSizeEqualToByteBufferSize() throws IOException {
        String charsetName = "UTF-8";
        String docData = "<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(docData.getBytes(Charset.forName(charsetName)));

        ByteBuffer byteDataExpected = ByteBuffer.wrap(docData.getBytes(Charset.forName(charsetName)));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, byteDataExpected.limit());

        byte[] expected = docData.getBytes(Charset.forName(charsetName));
        byte[] actual = new byte[byteData.limit()];
        byteData.get(actual);

        assertArrayEquals(expected, actual);

        inputStream.close();
    }
}