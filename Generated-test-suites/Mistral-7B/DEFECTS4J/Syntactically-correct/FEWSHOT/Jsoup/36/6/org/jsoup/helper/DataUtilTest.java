package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.jsoup.helper.DataUtil.getCharsetFromContentType;
import static org.jsoup.helper.DataUtil.load;
import static org.jsoup.helper.DataUtil.parseByteData;
import static org.jsoup.parser.Parser.htmlParser;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String content = "This is a test <meta charset=\"UTF-8\">";
        File testFile = new File("testFile.html");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(content);
        }

        Document doc = load(testFile, "UTF-8", "/");
        assertEquals(content, doc.body().html());
        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String content = "This is a test <meta charset=\"UTF-8\">";
        InputStream testStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        Document doc = load(testStream, "UTF-8", "/");
        assertEquals(content, doc.body().html());
    }

    @Test
    public void testDetectCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", getCharsetFromContentType(contentType));

        contentType = "text/html; charset=gb2312";
        assertEquals("gb2312", getCharsetFromContentType(contentType));

        contentType = "text/html; charset=EUC-JP";
        assertEquals("EUC-JP", getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", getCharsetFromContentType(contentType));

        contentType = "text/html; charset=windows-1252";
        assertEquals("windows-1252", getCharsetFromContentType(contentType));

        contentType = "text/html; charset=utf-16";
        assertEquals("utf-16", getCharsetFromContentType(contentType));

        contentType = "text/html; charset=us-ascii";
        assertEquals("us-ascii", getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(getCharsetFromContentType(contentType));
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("Hello World\n<html><head><meta charset=\"UTF-8\"></head><body>This is a test</body></html>".getBytes());
        Document doc = parseByteData(byteData, "UTF-8", "/", htmlParser());
        assertEquals("Hello World\n<html><head><meta charset=\"UTF-8\"></head><body>This is a test</body></html>", doc.body().html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String input = "This is a test <meta charset=\"UTF-8\">";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(input.getBytes("UTF-8")));
        assertEquals(input, Charset.forName("UTF-8").decode(byteData).toString());

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        int remaining = 1024;

        InputStream testStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C', 'D'});
        while (true) {
            int read = testStream.read(buffer, 0, buffer.length);
            if (read == -1) break;

            readBuffer.put(buffer, 0, read);
            remaining -= read;

            if (remaining <= 0) break;
        }

        byteData = DataUtil.readToByteBuffer(testStream, 0);
        assertEquals(new String(buffer, 0, 4), Charset.forName("UTF-8").decode(byteData).toString());
    }
}