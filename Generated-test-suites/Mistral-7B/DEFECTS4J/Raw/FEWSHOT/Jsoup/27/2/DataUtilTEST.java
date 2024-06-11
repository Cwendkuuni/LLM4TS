package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Element;
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
        String html = "<html><head><meta charset=\"UTF-8\"/></head><body><div>Test</div></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("<html><head><meta charset=\"UTF-8\"/></head><body><div>Test</div></body></html>", document.html());
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType("text/html; charset=ISO-8859-1"));
        assertNull(DataUtil.getCharsetFromContentType("text/html"));
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"/></head><body><div>Test</div></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"/></head><body><div>Test</div></body></html>", document.html());

        ByteArrayOutputStream outputStreamWithBOM = new ByteArrayOutputStream();
        outputStreamWithBOM.write(65279); // UTF-8 BOM
        outputStreamWithBOM.write(html.getBytes("UTF-8"));
        InputStream inputStreamWithBOM = new ByteArrayInputStream(outputStreamWithBOM.toByteArray());

        Document documentWithBOM = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStreamWithBOM), null, null, Parser.htmlParser());
        assertEquals("<html><body><div>Test</div></body></html>", documentWithBOM.html());

        String htmlWithInvalidCharset = "<html><head><meta charset=\"InvalidCharset\"/></head><body><div>Test</div></body></html>";
        ByteArrayOutputStream outputStreamWithInvalidCharset = new ByteArrayOutputStream();
        outputStreamWithInvalidCharset.write(htmlWithInvalidCharset.getBytes("UTF-8"));
        InputStream inputStreamWithInvalidCharset = new ByteArrayInputStream(outputStreamWithInvalidCharset.toByteArray());

        Document documentWithInvalidCharset = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStreamWithInvalidCharset), null, null, Parser.htmlParser());
        assertEquals("<html><head></head><body><div>Test</div></body></html>", documentWithInvalidCharset.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"/></head><body><div>Test</div></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] byteArray = new byte[byteBuffer.limit()];
        byteBuffer.get(byteArray);
        assertEquals(html.getBytes("UTF-8"), byteArray);
    }
}