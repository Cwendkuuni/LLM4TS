package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {
    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);
        Document doc = mock(Document.class);

        String docData = "This is a test.";
        when(parser.parseInput(docData, baseUri)).thenReturn(doc);

        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(charsetName)));
        assertEquals(doc, DataUtil.parseByteData(byteData, charsetName, baseUri, parser));

        charsetName = null;
        String metaContent = "charset=gb2312";
        Element meta = mock(Element.class);
        when(meta.attr("http-equiv")).thenReturn("content-type");
        when(meta.attr("content")).thenReturn(metaContent);
        when(doc.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(meta);

        docData = "This is a test.";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(charsetName)));
        assertEquals(doc, DataUtil.parseByteData(byteData, charsetName, baseUri, parser));

        charsetName = "gb2312";
        when(meta.attr("charset")).thenReturn(charsetName);
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName(charsetName)));
        assertEquals(doc, DataUtil.parseByteData(byteData, charsetName, baseUri, parser));

        charsetName = "UTF-8";
        metaContent = "Content-Type: text/html; charset=gb2312";
        when(meta.attr("http-equiv")).thenReturn(null);
        when(meta.attr("charset")).thenReturn(null);
        when(doc.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(meta);

        byteData = ByteBuffer.wrap(("UTF-8\n" + docData).getBytes(Charset.forName(charsetName)));
        assertEquals(doc, DataUtil.parseByteData(byteData, charsetName, baseUri, parser));

        charsetName = null;
        when(meta.attr("charset")).thenReturn(charsetName);
        when(charsetPattern.matcher(metaContent).find()).thenReturn(true);
        when(meta.attr("content")).thenReturn(metaContent);
        when(doc.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(meta);

        byteData = ByteBuffer.wrap(("UTF-8\n" + docData).getBytes(Charset.forName(charsetName)));
        assertEquals(doc, DataUtil.parseByteData(byteData, charsetName, baseUri, parser));

        charsetName = "gb2312";
        when(meta.attr("charset")).thenReturn(charsetName);
        when(Charset.isSupported(charsetName)).thenReturn(true);
        byteData = ByteBuffer.wrap(("UTF-8\n" + docData).getBytes(Charset.forName(charsetName)));
        assertEquals(doc, DataUtil.parseByteData(byteData, charsetName, baseUri, parser));

        charsetName = "gb2312";
        when(meta.attr("charset")).thenReturn(charsetName);
        when(Charset.isSupported(charsetName)).thenReturn(false);
        assertEquals(doc, DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = new ByteArrayInputStream("Hello World".getBytes());
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);

        ByteBuffer byteData1 = DataUtil.readToByteBuffer(inStream);
        assertEquals(byteData1.capacity(), byteData1.limit());
        assertEquals(byteData1.limit(), byteData1.position());
        ByteBuffer byteData2 = DataUtil.readToByteBuffer(inStream, bufferSize - 1);
        assertEquals(byteData2.capacity(), byteData2.limit());
        assertEquals(byteData2.limit(), byteData2.position());
        assertEquals(byteData1, byteData2);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=GB2312";
        assertEquals("GB2312", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=euc-jp";
        assertEquals("euc-jp", DataUtil.getCharsetFromContentType(contentType));
    }
}