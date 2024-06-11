package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body><p>Hello World</p></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(input, "UTF-8", null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"UTF-8\"></meta></head><body><p>Hello World</p></body></html>", doc.html());

        input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_16));
        doc = DataUtil.parseInputStream(input, "UTF-16", null, Parser.htmlParser());
        assertEquals("UTF-16", doc.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"UTF-16\"></meta></head><body><p>Hello World</p></body></html>", doc.html());

        input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8), 0, html.length() - 1);
        doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());

        input = new ByteArrayInputStream(new byte[0]);
        doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertTrue(doc.outputSettings().charset() == null || doc.outputSettings().charset().isEmpty());
        assertEquals("<html><head></head><body></body></html>", doc.html());

        input = new ByteArrayInputStream(new byte[0]);
        try {
            DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta></head><body></body></html>";
        Document doc = Jsoup.parse(html);
        Elements metaElements = doc.select("meta[http-equiv=content-type]");
        String charset = DataUtil.getCharsetFromContentType(metaElements.get(0).attr("content"));
        assertEquals("UTF-8", charset);

        html = "<html><head><meta charset=\"UTF-16\"></meta></head><body></body></html>";
        doc = Jsoup.parse(html);
        charset = DataUtil.getCharsetFromContentType(null);
        assertEquals("UTF-8", charset); // default charset is used if not found in header or meta charset

        html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html\"></head><body></body></html>";
        doc = Jsoup.parse(html);
        charset = DataUtil.getCharsetFromContentType(null);
        assertEquals(null, charset);

        html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"></meta></head><body></body></html>";
        doc = Jsoup.parse(html);
        charset = DataUtil.getCharsetFromContentType(null);
        assertEquals("ISO-8859-1", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        String expected = new String(new byte[]{'-', '_', '-', 123, 45, 67, 89, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24});
        assertEquals(expected, boundary);
    }
}

class Jsoup {
    public static Document parse(String html) throws IOException {
        return Jsoup.connect("").parse(new InputStreamReader(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8))));
    }
}