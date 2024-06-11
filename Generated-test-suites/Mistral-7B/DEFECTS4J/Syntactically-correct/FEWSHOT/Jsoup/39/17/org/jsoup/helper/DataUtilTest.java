package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;

public class DataUtilTest {
    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("EUC-JP", DataUtil.getCharsetFromContentType("text/html; charset=EUC-JP"));
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType("text/html; charset=ISO-8859-1"));
        assertEquals("US-ASCII", DataUtil.getCharsetFromContentType("text/plain; charset=US-ASCII"));
        assertEquals("UTF-16BE", DataUtil.getCharsetFromContentType("application/pdf; charset=UTF-16BE"));
        assertEquals("UTF-16LE", DataUtil.getCharsetFromContentType("application/pdf; charset=UTF-16LE"));
        assertEquals("UTF-32BE", DataUtil.getCharsetFromContentType("application/pdf; charset=UTF-32BE"));
        assertEquals("UTF-32LE", DataUtil.getCharsetFromContentType("application/pdf; charset=UTF-32LE"));
        assertNull(DataUtil.getCharsetFromContentType("text/html"));
        assertNull(DataUtil.getCharsetFromContentType("application/octet-stream"));
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Hello, World!</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(Charset.forName("UTF-8")));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals("Hello, World!", doc.body().text());

        byteData = ByteBuffer.wrap(html.getBytes(Charset.forName("ISO-8859-1")));
        doc = DataUtil.parseByteData(byteData, "ISO-8859-1", null, Parser.htmlParser());
        assertEquals("Hello, World!", doc.body().text());

        byteData = ByteBuffer.wrap(html.getBytes(Charset.forName("UTF-16BE")));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()));
        byteData.rewind();
        doc = DataUtil.parseByteData(byteData, "UTF-16BE", null, Parser.htmlParser());
        assertEquals("Hello, World!", doc.body().text());

        byteData = ByteBuffer.wrap(html.getBytes(Charset.forName("UTF-32BE")));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()));
        byteData.rewind();
        doc = DataUtil.parseByteData(byteData, "UTF-32BE", null, Parser.htmlParser());
        assertEquals("Hello, World!", doc.body().text());

        byteData = ByteBuffer.wrap(new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()));
    }

    @Test
    public void testLoad() throws IOException {
        File file = new File("test.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("<html><head><meta charset=\"UTF-8\"></head><body>Hello, World!</body></html>".getBytes(Charset.forName("UTF-8")));
        FileWriter writer = new FileWriter(file);
        writer.write(outputStream.toString());
        writer.close();

        Document doc = DataUtil.load(file, null, null);
        assertEquals("Hello, World!", doc.body().text());

        doc = DataUtil.load(new FileInputStream(file), null, null);
        assertEquals("Hello, World!", doc.body().text());

        doc = DataUtil.load(file, "UTF-16BE", null);
        assertEquals("Hello, World!", doc.body().text());

        doc = DataUtil.load(file, "UTF-16LE", null);
        assertEquals("Hello, World!", doc.body().text());

        doc = DataUtil.load(file, "UTF-32BE", null);
        assertEquals("Hello, World!", doc.body().text());

        doc = DataUtil.load(file, "UTF-32LE", null);
        assertEquals("Hello, World!", doc.body().text());

        doc = DataUtil.load(file, null, null);
        assertEquals("Hello, World!", doc.body().text());

        doc = DataUtil.load(file, null, null, Parser.xmlParser());
        assertEquals("Hello, World!", doc.body().text());

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, null, null));
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, null, null, Parser.htmlParser()));

        file.delete();
    }
}