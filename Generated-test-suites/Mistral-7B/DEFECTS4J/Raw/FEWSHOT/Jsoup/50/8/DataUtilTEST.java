package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        String html = "<html><head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World</p>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ByteArrayInputStream is = new ByteArrayInputStream(html.getBytes());

        Document doc = DataUtil.load(is, "gb2312", null);
        assertEquals("gb2312", doc.outputSettings().charset());
        assertEquals("Hello World", doc.body().text());

        doc = DataUtil.load(is, null, null);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseByteData() {
        String html = "<html><head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document doc = DataUtil.parseByteData(byteData, "gb2312", null, Parser.htmlParser());
        assertEquals("gb2312", doc.outputSettings().charset());
        assertEquals("Hello World", doc.body().text());

        byteData.rewind();
        doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset=gb2312"));
        assertEquals("EUC-JP", DataUtil.getCharsetFromContentType("text/html; charset=EUC-JP"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html"));
        assertEquals(null, DataUtil.getCharsetFromContentType(null));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertFalse(boundary.isEmpty());
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Character.isDefinedIsAlphabetic(boundary.charAt(i)) || Character.isDigit(boundary.charAt(i)) || boundary.charAt(i) == '-');
        }
    }
}