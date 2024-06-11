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
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseByteData() throws IOException {
        String charset = "UTF-8";
        String html = "<html lang=\"en\"><head><meta charset=\"UTF-8\"/><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = Charset.forName(charset).encode(CharSequence.class, html);

        Document doc = DataUtil.parseByteData(byteData, charset, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());

        charset = "ISO-8859-1";
        byteData = Charset.forName(charset).encode(CharSequence.class, html);
        doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());

        byteData = Charset.forName(charset).encode(CharSequence.class, html);
        doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());

        byteData = Charset.forName(charset).encode(CharSequence.class, html);
        doc = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());

        byteData = Charset.forName(charset).encode(CharSequence.class, html);
        doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testGetCharsetFromContentType() throws IOException {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testLoad() throws IOException {
        String charset = "UTF-8";
        String html = "<html lang=\"en\"><head><meta charset=\"UTF-8\"/><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body><h1>Test</h1></body></html>";

        File tempFile = File.createTempFile("DataUtilTest", ".html");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(html);
        }

        Document doc = DataUtil.load(tempFile, charset, null);
        assertNotNull(doc);
        assertEquals(tempFile.getAbsolutePath(), doc.baseUri());
        assertEquals("UTF-8", doc.outputSettings().charset());

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(charset));
        doc = DataUtil.load(inputStream, charset, null);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());

        inputStream = new ByteArrayInputStream(html.getBytes("ISO-8859-1".toCharArray()));
        doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());

        inputStream = new ByteArrayInputStream(html.getBytes("ISO-8859-1".toCharArray()));
        doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());

        inputStream = new ByteArrayInputStream(html.getBytes("ISO-8859-1".toCharArray()));
        doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }
}