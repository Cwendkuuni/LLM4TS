package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.UncheckedIOException;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.ElementParser;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Test cases for DataUtil methods.
 */
public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String xmlData = "<html><head><meta charset=\"UTF-8\"/></head><body>Hello World</body></html>";
        File testFile = new File("test.html");
        Files.write(testFile.toPath(), xmlData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        assertEquals("<html><head><meta charset=\"UTF-8\"/></head><body>Hello World</body></html>", doc.html());

        Files.delete(testFile.toPath());
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String xmlData = "<html><head><meta charset=\"UTF-8\"/></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("<html><head><meta charset=\"UTF-8\"/></head><body>Hello World</body></html>", doc.html());

        inputStream.close();
    }

    @Test
    public void testLoadDocumentFromInputStreamWithCustomParser() throws IOException {
        String xmlData = "<html><head><meta charset=\"UTF-8\"/></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", null, new ElementParser("html") {
            @Override
            public Document parse() {
                return new Document();
            }
        });

        assertEquals("<html></html>", doc.html());
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceData = "Source Data";
        String targetData = "Target Data";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(sourceData, outputStream.toString(StandardCharsets.UTF_8.name()));

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        byte[] utf8Data = {
                0xEF, 0xBB, 0xBF,
                0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x61, 0x73, 0x73, 0x5F, 0x63, 0x74, 0x61, 0x72, 0x74, 0x65, 0x64, 0x2F, 0x78, 0x6D, 0x6C, 0x20, 0x68, 0x74, 0x6D, 0x6C, 0x3E
        };
        byte[] utf16LEData = {
                0xFF, 0xFF,
                0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x61, 0x73, 0x73, 0x5F, 0x63, 0x74, 0x61, 0x72, 0x74, 0x65, 0x64, 0x2F, 0x78, 0x6D, 0x6C, 0x20, 0x68, 0x74, 0x6D, 0x6C, 0x3E
        };
        byte[] utf32BEData = {
                0x00, 0x00, 0xFE, 0xFF,
                0x00, 0x00, 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x61, 0x73, 0x73, 0x5F, 0x63, 0x74, 0x61, 0x72, 0x74, 0x65, 0x64, 0x2F, 0x78, 0x6D, 0x6C, 0x20, 0x68, 0x74, 0x6D, 0x6C, 0x3E,
                0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x05, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x09
        };

        ByteArrayInputStream inputStream = new ByteArrayInputStream(utf8Data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"/></head><body></body></html>", doc.html());

        inputStream = new ByteArrayInputStream(utf16LEData);
        doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("<html><head></head><body></body></html>", doc.html());

        inputStream = new ByteArrayInputStream(utf32BEData);
        doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("<html><head></head><body></body></html>", doc.html());

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String utf8Data = "UTF-8 Data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(utf8Data.getBytes(StandardCharsets.UTF_8));

        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(utf8Data, StandardCharsets.UTF_8.decode(buffer).toString());

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String utf8Data = "UTF-8 Data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(utf8Data.getBytes(StandardCharsets.UTF_8));

        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(10, buffer.remaining());

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSizeZero() throws IOException {
        String utf8Data = "UTF-8 Data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(utf8Data.getBytes(StandardCharsets.UTF_8));

        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 0);
        assertEquals(utf8Data.length(), buffer.remaining());

        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer utf32BEBytes = ByteBuffer.wrap(new byte[]{0x00, 0x00, 0xFE, 0xFF});
        ByteBuffer utf16LEBytes = ByteBuffer.wrap(new byte[]{0xFF, 0xFF});
        ByteBuffer utf8Bytes = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF});

        BomCharset bomCharsetUTF32BE = DataUtil.detectCharsetFromBom(utf32BEBytes);
        assertSame(BomCharset.class, bomCharsetUTF32BE.getClass());
        assertEquals("UTF-32", bomCharsetUTF32BE.charset);
        assertTrue(bomCharsetUTF32BE.offset);

        BomCharset bomCharsetUTF16LE = DataUtil.detectCharsetFromBom(utf16LEBytes);
        assertSame(BomCharset.class, bomCharsetUTF16LE.getClass());
        assertEquals("UTF-16", bomCharsetUTF16LE.charset);
        assertFalse(bomCharsetUTF16LE.offset);

        BomCharset bomCharsetUTF8 = DataUtil.detectCharsetFromBom(utf8Bytes);
        assertSame(BomCharset.class, bomCharsetUTF8.getClass());
        assertEquals("UTF-8", bomCharsetUTF8.charset);
        assertFalse(bomCharsetUTF8.offset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String xmlContentType = "text/xml; charset=UTF-8";
        String htmlContentType = "text/html; charset=UTF-8";
        String textPlainContentType = "text/plain";

        String utf8Charset = DataUtil.getCharsetFromContentType(xmlContentType);
        assertEquals("UTF-8", utf8Charset);

        utf8Charset = DataUtil.getCharsetFromContentType(htmlContentType);
        assertEquals("UTF-8", utf8Charset);

        utf8Charset = DataUtil.getCharsetFromContentType(textPlainContentType);
        assertNull(utf8Charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertTrue(mimeBoundary.length() >= 32);
    }
}