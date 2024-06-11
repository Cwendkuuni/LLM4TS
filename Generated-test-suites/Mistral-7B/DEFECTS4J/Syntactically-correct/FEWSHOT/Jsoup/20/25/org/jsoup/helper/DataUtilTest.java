package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.helper.DataUtil;

public class DataUtilTest {

    @Test
    public void testParseByteData() throws IOException {
        String utf8Data = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8'/></head><body></body></html>";
        ByteBuffer byteDataUtf8 = ByteBuffer.wrap(utf8Data.getBytes(StandardCharsets.UTF_8));
        String gb2312Data = "<?xml version='1.0' encoding='gb2312'?>\n<html><head><meta charset='gb2312'/></head><body></body></html>";
        ByteBuffer byteDataGb2312 = ByteBuffer.wrap(gb2312Data.getBytes(StandardCharsets.UTF_8)); // UTF-8 encoded for testing
        String charsetHeaderData = "text/html; charset=gb2312";
        String charsetMetaData = "<html><head><meta charset='GB2312'/></head><body></body></html>";

        Document documentUtf8 = DataUtil.parseByteData(byteDataUtf8, null, null, Parser.htmlParser());
        Document documentGb2312 = DataUtil.parseByteData(byteDataGb2312, null, null, Parser.htmlParser());
        Document documentCharsetHeader = DataUtil.parseByteData(byteDataUtf8, charsetHeaderData, null, Parser.htmlParser());
        Document documentCharsetMeta = DataUtil.parseByteData(byteDataUtf8, null, null, Parser.htmlParser());

        assertNotNull(documentUtf8);
        assertNotNull(documentGb2312);
        assertNotNull(documentCharsetHeader);
        assertNotNull(documentCharsetMeta);

        Element charsetHeader = documentCharsetHeader.selectFirst("meta[http-equiv=content-type]");
        assertEquals("text/html; charset=gb2312", charsetHeader.attr("content"));

        Element charsetMeta = documentCharsetMeta.selectFirst("meta[charset]");
        assertEquals("GB2312", charsetMeta.attr("charset"));

        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(ByteBuffer.allocate(0), null, null, Parser.htmlParser()));
    }

    @Test
    public void testLoad() throws IOException {
        String utf8FilePath = "src/test/resources/DataUtilTest_UTF8.html";
        String gb2312FilePath = "src/test/resources/DataUtilTest_GB2312.html";

        File fileUtf8 = new File(utf8FilePath);
        File fileGb2312 = new File(gb2312FilePath);

        Document documentUtf8 = DataUtil.load(fileUtf8, null, null);
        Document documentGb2312 = DataUtil.load(fileGb2312, null, null);

        assertNotNull(documentUtf8);
        assertNotNull(documentGb2312);

        Element charsetHeaderUtf8 = documentUtf8.selectFirst("meta[http-equiv=content-type]");
        assertNull(charsetHeaderUtf8);

        Element charsetMetaUtf8 = documentUtf8.selectFirst("meta[charset]");
        assertNull(charsetMetaUtf8);

        Element charsetHeaderGb2312 = documentGb2312.selectFirst("meta[http-equiv=content-type]");
        assertEquals("text/html; charset=gb2312", charsetHeaderGb2312.attr("content"));

        Element charsetMetaGb2312 = documentGb2312.selectFirst("meta[charset]");
        assertEquals("gb2312", charsetMetaGb2312.attr("charset"));

        documentUtf8 = DataUtil.load(fileUtf8, "UTF-8", null);
        documentGb2312 = DataUtil.load(fileGb2312, "gb2312", null);

        assertNotNull(documentUtf8);
        assertNotNull(documentGb2312);

        charsetHeaderUtf8 = documentUtf8.selectFirst("meta[http-equiv=content-type]");
        assertNull(charsetHeaderUtf8);

        charsetMetaUtf8 = documentUtf8.selectFirst("meta[charset]");
        assertNull(charsetMetaUtf8);

        charsetHeaderGb2312 = documentGb2312.selectFirst("meta[http-equiv=content-type]");
        assertEquals("text/html; charset=gb2312", charsetHeaderGb2312.attr("content"));

        charsetMetaGb2312 = documentGb2312.selectFirst("meta[charset]");
        assertEquals("gb2312", charsetMetaGb2312.attr("charset"));

        InputStream inputStreamUtf8 = DataUtil.class.getResourceAsStream("/DataUtilTest_UTF8.html");
        InputStream inputStreamGb2312 = DataUtil.class.getResourceAsStream("/DataUtilTest_GB2312.html");

        Document documentUtf8InputStream = DataUtil.load(inputStreamUtf8, null, null);
        Document documentGb2312InputStream = DataUtil.load(inputStreamGb2312, null, null);

        assertNotNull(documentUtf8InputStream);
        assertNotNull(documentGb2312InputStream);

        charsetHeaderUtf8 = documentUtf8InputStream.selectFirst("meta[http-equiv=content-type]");
        assertNull(charsetHeaderUtf8);

        charsetMetaUtf8 = documentUtf8InputStream.selectFirst("meta[charset]");
        assertNull(charsetMetaUtf8);

        charsetHeaderGb2312 = documentGb2312InputStream.selectFirst("meta[http-equiv=content-type]");
        assertEquals("text/html; charset=gb2312", charsetHeaderGb2312.attr("content"));

        charsetMetaGb2312 = documentGb2312InputStream.selectFirst("meta[charset]");
        assertEquals("gb2312", charsetMetaGb2312.attr("charset"));

        ByteArrayInputStream byteArrayInputStreamUtf8 = new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8'/></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream byteArrayInputStreamGb2312 = new ByteArrayInputStream("<?xml version='1.0' encoding='gb2312'?>\n<html><head><meta charset='gb2312'/></head><body></body></html>".getBytes(StandardCharsets.UTF_8)); // UTF-8 encoded for testing

        Document documentUtf8ByteArrayInputStream = DataUtil.load(byteArrayInputStreamUtf8, null, null);
        Document documentGb2312ByteArrayInputStream = DataUtil.load(byteArrayInputStreamGb2312, null, null);

        assertNotNull(documentUtf8ByteArrayInputStream);
        assertNotNull(documentGb2312ByteArrayInputStream);

        charsetHeaderUtf8 = documentUtf8ByteArrayInputStream.selectFirst("meta[http-equiv=content-type]");
        assertNull(charsetHeaderUtf8);

        charsetMetaUtf8 = documentUtf8ByteArrayInputStream.selectFirst("meta[charset]");
        assertNull(charsetMetaUtf8);

        charsetHeaderGb2312 = documentGb2312ByteArrayInputStream.selectFirst("meta[http-equiv=content-type]");
        assertEquals("text/html; charset=gb2312", charsetHeaderGb2312.attr("content"));

        charsetMetaGb2312 = documentGb2312ByteArrayInputStream.selectFirst("meta[charset]");
        assertEquals("gb2312", charsetMetaGb2312.attr("charset"));
    }

    @Test
    public void testLoadWithCustomParser() throws IOException {
        String utf8Data = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8'/></head><body></body></html>";
        ByteBuffer byteDataUtf8 = ByteBuffer.wrap(utf8Data.getBytes(StandardCharsets.UTF_8));
        String xmlData = "<root><element>Test</element></root>";
        ByteBuffer byteDataXml = ByteBuffer.wrap(xmlData.getBytes(StandardCharsets.UTF_8));

        Document documentUtf8 = DataUtil.parseByteData(byteDataUtf8, null, null, Parser.xmlParser());
        Document documentXml = DataUtil.parseByteData(byteDataXml, null, null, Parser.xmlParser());

        assertNotNull(documentUtf8);
        assertNotNull(documentXml);

        assertEquals(1, documentUtf8.select("html").children().size());
        assertEquals(1, documentXml.select("root").children().size());

        Document documentUtf8Load = DataUtil.load(new ByteArrayInputStream(utf8Data.getBytes(StandardCharsets.UTF_8)), "UTF-8", null, Parser.xmlParser());
        Document documentXmlLoad = DataUtil.load(new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8)), null, null, Parser.xmlParser());

        assertNotNull(documentUtf8Load);
        assertNotNull(documentXmlLoad);

        assertEquals(1, documentUtf8Load.select("html").children().size());
        assertEquals(1, documentXmlLoad.select("root").children().size());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String utf8Data = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8'/></head><body></body></html>";
        String gb2312Data = "<?xml version='1.0' encoding='gb2312'?>\n<html><head><meta charset='gb2312'/></head><body></body></html>";

        ByteArrayOutputStream outputStreamUtf8 = new ByteArrayOutputStream();
        FileOutputStream fileOutputStreamUtf8 = new FileOutputStream("test_utf8.txt");
        FileOutputStream fileOutputStreamGb2312 = new FileOutputStream("test_gb2312.txt");

        byte[] utf8Bytes = utf8Data.getBytes(StandardCharsets.UTF_8);
        byte[] gb2312Bytes = gb2312Data.getBytes(StandardCharsets.UTF_8); // UTF-8 encoded for testing

        outputStreamUtf8.write(utf8Bytes);
        fileOutputStreamUtf8.write(utf8Bytes);
        fileOutputStreamGb2312.write(gb2312Bytes);

        InputStream inputStreamUtf8 = new FileInputStream("test_utf8.txt");
        InputStream inputStreamGb2312 = new FileInputStream("test_gb2312.txt");

        ByteBuffer byteDataUtf8 = DataUtil.readToByteBuffer(inputStreamUtf8);
        ByteBuffer byteDataGb2312 = DataUtil.readToByteBuffer(inputStreamGb2312);

        assertArrayEquals(utf8Bytes, byteDataUtf8.array());
        assertArrayEquals(gb2312Bytes, byteDataGb2312.array());

        ByteBuffer byteDataUtf8File = DataUtil.readToByteBuffer(new FileInputStream("test_utf8.txt"));
        ByteBuffer byteDataGb2312File = DataUtil.readToByteBuffer(new FileInputStream("test_gb2312.txt"));

        assertArrayEquals(utf8Bytes, byteDataUtf8File.array());
        assertArrayEquals(gb2312Bytes, byteDataGb2312File.array());

        ByteArrayInputStream byteArrayInputStreamUtf8 = new ByteArrayInputStream(utf8Data.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream byteArrayInputStreamGb2312 = new ByteArrayInputStream(gb2312Data.getBytes(StandardCharsets.UTF_8)); // UTF-8 encoded for testing

        ByteBuffer byteDataUtf8ByteArrayInputStream = DataUtil.readToByteBuffer(byteArrayInputStreamUtf8);
        ByteBuffer byteDataGb2312ByteArrayInputStream = DataUtil.readToByteBuffer(byteArrayInputStreamGb2312);

        assertArrayEquals(utf8Bytes, byteDataUtf8ByteArrayInputStream.array());
        assertArrayEquals(gb2312Bytes, byteDataGb2312ByteArrayInputStream.array());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String utf8ContentType = "text/html; charset=UTF-8";
        String gb2312ContentType = "text/html; charset=gb2312";
        String unknownContentType = "text/html";

        String utf8Charset = DataUtil.getCharsetFromContentType(utf8ContentType);
        String gb2312Charset = DataUtil.getCharsetFromContentType(gb2312ContentType);
        String unknownCharset = DataUtil.getCharsetFromContentType(unknownContentType);

        assertEquals("UTF-8", utf8Charset);
        assertEquals("gb2312", gb2312Charset);
        assertNull(unknownCharset);
    }
}