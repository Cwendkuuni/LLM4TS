package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert. assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseByteData() {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(11, doc.body().children().size());

        charsetName = null;
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());

        charsetName = "ISO-8859-1";
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("ISO-8859-1", doc.outputSettings().charset());

        byteData = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        charsetName = null;
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());

        charsetName = "gb2312";
        byteData = DataUtil.readFileToByteBuffer(new File("testfile.html"));
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("gb2312", doc.outputSettings().charset());

        charsetName = "unknown";
        assertThrows(IllegalCharsetNameException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser()));

        byteData = ByteBuffer.wrap(new byte[]{0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0});
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser()));

        byteData = ByteBuffer.wrap(new byte[]{0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0});
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals(12, doc.body().children().size());

        charsetName = "UTF-16BE";
        byteData = ByteBuffer.wrap(new byte[]{0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0});
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("UTF-16BE", doc.outputSettings().charset());
    }

    @Test
    public void testLoad() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        File file = new File("testfile.html");
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(11, doc.body().children().size());

        InputStream inputStream = new ByteArrayInputStream(new byte[]{0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(11, doc.body().children().size());

        charsetName = null;
        doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("UTF-8", doc.outputSettings().charset());

        inputStream = new ByteArrayInputStream(new byte[]{0xEF, 0xBB, 0xBF, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("UTF-8", doc.outputSettings().charset());

        charsetName = "gb2312";
        doc = DataUtil.load(new File("testfile.html"), charsetName, baseUri);
        assertEquals("gb2312", doc.outputSettings().charset());

        charsetName = "unknown";
        assertThrows(IllegalCharsetNameException.class, () -> DataUtil.load(new File("testfile.html"), charsetName, baseUri));

        doc = DataUtil.load(new File("testfile.html"), null, baseUri);
        assertEquals("UTF-8", doc.outputSettings().charset());

        assertThrows(IOException.class, () -> DataUtil.load(null, charsetName, baseUri));

        File nonExistentFile = new File("nonExistentFile.html");
        assertThrows(IOException.class, () -> DataUtil.load(nonExistentFile, charsetName, baseUri));
    }

    @Test
    public void testLoadWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        File file = new File("testfile.xml");
        Document doc = DataUtil.load(file, charsetName, baseUri, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("xml", doc.tagName());

        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x6A, 0x61, 0x76, 0x61, 0x20, 0x43, 0x6F, 0x6D, 0x70, 0x75, 0x74, 0x65, 0x64, 0x20, 0x61, 0x73, 0x73, 0x5C, 0x69, 0x6E, 0x66, 0x6F, 0x63, 0x61, 0x6C, 0x73, 0x74, 0x3E});
        doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("xml", doc.tagName());

        charsetName = null;
        doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());

        charsetName = "gb2312";
        doc = DataUtil.load(new File("testfile.xml"), charsetName, baseUri, Parser.xmlParser());
        assertEquals("gb2312", doc.outputSettings().charset());

        charsetName = "unknown";
        assertThrows(IllegalCharsetNameException.class, () -> DataUtil.load(new File("testfile.xml"), charsetName, baseUri, Parser.xmlParser()));

        doc = DataUtil.load(new File("testfile.xml"), null, baseUri, Parser.xmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());

        assertThrows(IOException.class, () -> DataUtil.load(null, charsetName, baseUri, Parser.xmlParser()));

        File nonExistentFile = new File("nonExistentFile.xml");
        assertThrows(IOException.class, () -> DataUtil.load(nonExistentFile, charsetName, baseUri, Parser.xmlParser()));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertFalse(mimeBoundary.isEmpty());
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=gb2312";
        assertEquals("gb2312", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=Shift_JIS";
        assertEquals("Shift_JIS", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=unknown-charset";
        assertNull(DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));

        contentType = "application/octet-stream; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "application/octet-stream; charset=gb2312";
        assertEquals("gb2312", DataUtil.getCharsetFromContentType(contentType));

        contentType = "application/octet-stream; charset=Shift_JIS";
        assertEquals("Shift_JIS", DataUtil.getCharsetFromContentType(contentType));

        contentType = "application/octet-stream; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));

        contentType = "application/octet-stream; charset=unknown-charset";
        assertNull(DataUtil.getCharsetFromContentType(contentType));

        contentType = "application/octet-stream";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43, 0x44});
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expected = new byte[]{0x41, 0x42, 0x43, 0x44};
        byte[] actual = outputStream.toByteArray();
        assertArrayEquals(expected, actual);
    }
}