package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

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

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        String testHtml = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
        testOutput.write(testHtml.getBytes());
        File testFile = new File("test.html");
        testFile.createNewFile();
        FileOutputStream testFileOutput = new FileOutputStream(testFile);
        testFileOutput.write(testHtml.getBytes());
        testFileOutput.close();

        Document document = DataUtil.load(testFile, "UTF-8", null);
        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());

        Document documentFromStream = DataUtil.load(new ByteArrayInputStream(testHtml.getBytes()), "UTF-8", null);
        assertNotNull(documentFromStream);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", documentFromStream.html());

        documentFromStream = DataUtil.load(new ByteArrayInputStream(testHtml.getBytes()), null, null);
        assertNotNull(documentFromStream);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", documentFromStream.html());

        documentFromStream = DataUtil.load(new ByteArrayInputStream(testHtml.getBytes()), null, "/test");
        assertNotNull(documentFromStream);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", documentFromStream.html());

        documentFromStream = DataUtil.load(new ByteArrayInputStream(testHtml.getBytes()), "ISO-8859-1", null);
        assertNotNull(documentFromStream);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", documentFromStream.html());

        documentFromStream = DataUtil.load(new ByteArrayInputStream(testHtml.getBytes()), "ISO-8859-1", "/test");
        assertNotNull(documentFromStream);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", documentFromStream.html());

        document = DataUtil.load(testFile, null, null);
        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());

        document = DataUtil.load(testFile, "ISO-8859-1", null);
        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());

        document = DataUtil.load(testFile, "ISO-8859-1", "/test");
        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());

        document = DataUtil.load(testFile, "UTF-8", null);
        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());

        document = DataUtil.load(testFile, "UTF-8", "/test");
        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());

        testFile.delete();
    }

    @Test
    public void testParseByteData() throws IOException {
        String testHtml = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
        testOutput.write(testHtml.getBytes());
        ByteBuffer testByteData = ByteBuffer.wrap(testOutput.toByteArray());

        Document document = DataUtil.parseByteData(testByteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());

        document = DataUtil.parseByteData(testByteData, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());

        document = DataUtil.parseByteData(testByteData, null, "/test", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());

        document = DataUtil.parseByteData(testByteData, "ISO-8859-1", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());

        document = DataUtil.parseByteData(testByteData, "ISO-8859-1", "/test", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());

        document = DataUtil.parseByteData(testByteData, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("<html xmlns:xmlns='http://www.w3.org/1999/xhtml' xml:lang='en' xmlns='http://www.w3.org/1999/xhtml'><head></head><body><h1>Test</h1></body></html>", document.html());

        document = DataUtil.parseByteData(testByteData, null, null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("<html xmlns:xmlns='http://www.w3.org/1999/xhtml' xml:lang='en' xmlns='http://www.w3.org/1999/xhtml'><head></head><body><h1>Test</h1></body></html>", document.html());

        document = DataUtil.parseByteData(testByteData, null, "/test", Parser.xmlParser());
        assertNotNull(document);
        assertEquals("<html xmlns:xmlns='http://www.w3.org/1999/xhtml' xml:lang='en' xmlns='http://www.w3.org/1999/xhtml'><head></head><body><h1>Test</h1></body></html>", document.html());

        document = DataUtil.parseByteData(testByteData, "ISO-8859-1", null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("<html xmlns:xmlns='http://www.w3.org/1999/xhtml' xml:lang='en' xmlns='http://www.w3.org/1999/xhtml'><head></head><body><h1>Test</h1></body></html>", document.html());

        document = DataUtil.parseByteData(testByteData, "ISO-8859-1", "/test", Parser.xmlParser());
        assertNotNull(document);
        assertEquals("<html xmlns:xmlns='http://www.w3.org/1999/xhtml' xml:lang='en' xmlns='http://www.w3.org/1999/xhtml'><head></head><body><h1>Test</h1></body></html>", document.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testHtml = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteArrayInputStream testInput = new ByteArrayInputStream(testHtml.getBytes());

        ByteBuffer buffer = DataUtil.readToByteBuffer(testInput);
        assertEquals(testHtml.getBytes().length, buffer.limit());

        buffer = DataUtil.readToByteBuffer(testInput, 0x10000);
        assertEquals(testHtml.getBytes().length, buffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8; foo=bar"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8; foo=bar"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8; foo=bar; charset=gb2312"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8; foo=bar; charset=gb2312"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8; foo=bar; charset=gb2312; charset=gbk"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8; foo=bar; charset=gb2312; charset=gbk"));

        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset=gb2312"));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset=gb2312; foo=bar"));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset=gb2312; foo=bar; charset=gb2312"));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset=gb2312; foo=bar; charset=gb2312; charset=gbk"));

        assertEquals("gbk", DataUtil.getCharsetFromContentType("text/html; charset=gbk"));
        assertEquals("gbk", DataUtil.getCharsetFromContentType("text/html; charset=gbk; foo=bar"));
        assertEquals("gbk", DataUtil.getCharsetFromContentType("text/html; charset=gbk; foo=bar; charset=gb2312"));
        assertEquals("gbk", DataUtil.getCharsetFromContentType("text/html; charset=gbk; foo=bar; charset=gb2312; charset=gbk"));

        assertEquals(null, DataUtil.getCharsetFromContentType("text/html"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; foo=bar"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset="));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset="));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset= "));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset= "));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset= "));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset= "));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset= "));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset= "));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset= "));
    }
}