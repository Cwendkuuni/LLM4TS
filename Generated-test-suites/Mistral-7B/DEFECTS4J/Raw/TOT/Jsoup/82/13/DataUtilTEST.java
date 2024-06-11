package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtil.BomCharset;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void init() {
        inputStream = mock(InputStream.class);
        outputStream = mock(OutputStream.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "InvalidCharset");
        assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        Document doc = DataUtil.load(inputStream, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test.html".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        Document doc = DataUtil.load(inputStream, "InvalidCharset");
        assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test.xml".getBytes());
        Document doc = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("CrossStreamsTest".getBytes());
        OutputStream outputStream = new FileOutputStream("output.txt");

        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        outputStream.close();

        File file = new File("output.txt");
        assertEquals("CrossStreamsTest", file.length() > 0 ? new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8) : "");
    }

    @Test
    public void testParseInputStream() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream("test.html".getBytes()), 0);
        Document doc = DataUtil.parseInputStream(new ByteArrayInputStream("test.html".getBytes()), null, null, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("test.html", doc.body().text());
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream("test.html".getBytes(StandardCharsets.UTF_8)), 0);
        Document doc = DataUtil.parseInputStream(new ByteArrayInputStream("test.html".getBytes(StandardCharsets.UTF_8)), "UTF-8", null, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("test.html", doc.body().text());
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xFE, 0, 0}), 0);
        Document doc = DataUtil.parseInputStream(new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xFE, 0, 0}), null, null, DataUtil.Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithBOMAndDefaultCharset() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xFE, 0, 0}), 0);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        String charset = bomCharset == null ? null : bomCharset.charset;
        Document doc = DataUtil.parseInputStream(new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xFE, 0, 0}), charset, null, DataUtil.Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithBOMAndSpecifiedCharset() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xFE, 0, 0}), 0);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        String charset = bomCharset != null ? bomCharset.charset : "UTF-8";
        Document doc = DataUtil.parseInputStream(new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xFE, 0, 0}), charset, null, DataUtil.Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWith1ByteUTF8BOM() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}), 0);
        Document doc = DataUtil.parseInputStream(new ByteArrayInputStream(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}), "UTF-8", null, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithInvalidBOM() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{0x00, 0x01}), 0);
        Document doc = DataUtil.parseInputStream(new ByteArrayInputStream(new byte[]{0x00, 0x01}), null, null, DataUtil.Parser.htmlParser());
        assertNull(doc);
    }
}