package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testParseByteData() throws IOException {
        String docDataUtf8 = "<?xml version='1.0' encoding='UTF-8'?>\r\n<html>\r\n<head>\r\n<title>UTF-8 Test</title>\r\n</head>\r\n<body>\r\n<p>This is a UTF-8 document.</p>\r\n</body>\r\n</html>";
        String docDataGb2312 = "<?xml version='1.0' encoding='gb2312'?>\r\n<html>\r\n<head>\r\n<title>gb2312 Test</title>\r\n</head>\r\n<body>\r\n<p>这是一个gb2312文档。</p>\r\n</body>\r\n</html>";

        ByteBuffer byteDataUtf8 = Charset.forName("UTF-8").encode(Charset.forName("UTF-8").decode(docDataUtf8));
        ByteBuffer byteDataGb2312 = Charset.forName("gb2312").encode(Charset.forName("gb2312").decode(docDataGb2312));

        Document docUtf8 = DataUtil.parseByteData(byteDataUtf8, null, null, Parser.htmlParser());
        Document docGb2312 = DataUtil.parseByteData(byteDataGb2312, null, null, Parser.htmlParser());

        assertEquals(docDataUtf8, docUtf8.html());
        assertEquals(docDataGb2312, docGb2312.html());

        assertEquals(docUtf8.title(), docGb2312.title());

        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(docUtf8.head().selectFirst("meta[http-equiv=content-type]").attr("content")));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType(docGb2312.head().selectFirst("meta[http-equiv=content-type]").attr("content")));

        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("gb2312", DataUtil.validateCharset("gb2312"));
        assertNull(DataUtil.validateCharset("invalidCharset"));

        assertEquals("UTF-8", DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[]{0x00, 0x00, (byte) 0xFE, (byte) 0xFF})));
        assertEquals("UTF-16", DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[]{0xFE, 0xFF})));
        assertEquals("UTF-8", DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF})));
        assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[]{0x00, 0x01})));
    }

    @Test
    public void testLoadFile() throws IOException {
        String docDataUtf8 = "<?xml version='1.0' encoding='UTF-8'?>\r\n<html>\r\n<head>\r\n<title>UTF-8 Test</title>\r\n</head>\r\n<body>\r\n<p>This is a UTF-8 document.</p>\r\n</body>\r\n</html>";
        File fileUtf8 = new File("test.html");
        File fileGb2312 = new File("test_gb2312.html");

        try (RandomAccessFile rafUtf8 = new RandomAccessFile(fileUtf8, "rw");
             RandomAccessFile rafGb2312 = new RandomAccessFile(fileGb2312, "rw")) {

            rafUtf8.writeBytes(docDataUtf8.getBytes(StandardCharsets.UTF_8));
            rafGb2312.writeBytes(docDataGb2312.getBytes(StandardCharsets.UTF_8));
        }

        Document docUtf8 = DataUtil.load(fileUtf8, null, null);
        Document docGb2312 = DataUtil.load(fileGb2312, null, null);
        File tempFile = File.createTempFile("test_", null);

        DataUtil.saveFile(tempFile, docUtf8.html().getBytes(StandardCharsets.UTF_8), "UTF-8");
        assertEquals(docDataUtf8, new String(Files.readAllBytes(tempFile.toPath()), StandardCharsets.UTF_8));

        DataUtil.saveFile(tempFile, docGb2312.html().getBytes(StandardCharsets.UTF_8), "gb2312");
        assertEquals(docDataGb2312, new String(Files.readAllBytes(tempFile.toPath()), StandardCharsets.UTF_8));

        assertTrue(fileUtf8.exists());
        assertTrue(fileGb2312.exists());

        assertFalse(fileUtf8.delete());
        assertFalse(fileGb2312.delete());
        assertTrue(tempFile.delete());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String docDataUtf8 = "<?xml version='1.0' encoding='UTF-8'?>\r\n<html>\r\n<head>\r\n<title>UTF-8 Test</title>\r\n</head>\r\n<body>\r\n<p>This is a UTF-8 document.</p>\r\n</body>\r\n</html>";
        String docDataGb2312 = "<?xml version='1.0' encoding='gb2312'?>\r\n<html>\r\n<head>\r\n<title>gb2312 Test</title>\r\n</head>\r\n<body>\r\n<p>这是一个gb2312文档。</p>\r\n</body>\r\n</html>";

        InputStream inputStreamUtf8 = new ByteArrayInputStream(docDataUtf8.getBytes(StandardCharsets.UTF_8));
        InputStream inputStreamGb2312 = new ByteArrayInputStream(docDataGb2312.getBytes(StandardCharsets.UTF_8));

        Document docUtf8 = DataUtil.load(inputStreamUtf8, null, null);
        Document docGb2312 = DataUtil.load(inputStreamGb2312, null, null);

        assertEquals(docDataUtf8, docUtf8.html());
        assertEquals(docDataGb2312, docGb2312.html());

        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(docUtf8.head().selectFirst("meta[http-equiv=content-type]").attr("content")));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType(docGb2312.head().selectFirst("meta[http-equiv=content-type]").attr("content")));

        assertSame(inputStreamUtf8, DataUtil.loadInputStreamWithCharset(docUtf8.head().selectFirst("meta[http-equiv=content-type]").attr("content"), docUtf8.inputStream()));
        assertNull(DataUtil.loadInputStreamWithCharset(null, docUtf8.inputStream()));
        assertNull(DataUtil.loadInputStreamWithCharset("invalidCharset", docUtf8.inputStream()));

        assertSame(inputStreamGb2312, DataUtil.loadInputStreamWithCharset(docGb2312.head().selectFirst("meta[http-equiv=content-type]").attr("content"), docGb2312.inputStream()));
        assertNull(DataUtil.loadInputStreamWithCharset(null, docGb2312.inputStream()));
        assertNull(DataUtil.loadInputStreamWithCharset("invalidCharset", docGb2312.inputStream()));

        inputStreamUtf8.close();
        inputStreamGb2312.close();
    }

    @Test
    public void testSaveFile() throws IOException {
        String docData = "This is a test.";
        File file = File.createTempFile("test_", null);

        DataUtil.saveFile(file, docData.getBytes(StandardCharsets.UTF_8), "UTF-8");
        assertEquals(docData, new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8));

        DataUtil.saveFile(file, docData.getBytes(StandardCharsets.UTF_16LE), "UTF-16LE");
        assertEquals(docData, new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_16LE));

        DataUtil.saveFile(file, docData.getBytes(StandardCharsets.UTF_16BE), "UTF-16BE");
        assertEquals(docData, new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_16BE));

        DataUtil.saveFile(file, docData.getBytes(StandardCharsets.UTF_32LE), "UTF-32LE");
        assertEquals(docData, new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_32LE));

        DataUtil.saveFile(file, docData.getBytes(StandardCharsets.UTF_32BE), "UTF-32BE");
        assertEquals(docData, new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_32BE));

        DataUtil.saveFile(file, docData.getBytes(StandardCharsets.UTF_8), "invalidCharset");
        assertEquals(docData, new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8));

        assertTrue(file.delete());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String docData = "This is a test.";
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(docData.getBytes(StandardCharsets.UTF_8));

        DataUtil.crossStreams(inputStream, byteStream);

        byte[] result = byteStream.toByteArray();
        assertEquals(docData, new String(result, StandardCharsets.UTF_8));

        inputStream.close();
        byteStream.close();
    }

    @Test
    public void testMimeBoundary() {
        assertEquals("--1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", DataUtil.mimeBoundary());
    }
}