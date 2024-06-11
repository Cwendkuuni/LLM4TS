import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    private String baseUri;
    private File file;
    private InputStream inputStream;
    private String charsetName;

    @Before
    public void setUp() {
        baseUri = "https://example.com";
        file = new File("test.html");
        charsetName = "UTF-8";
        try {
            inputStream = new FileInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFile() {
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            DataUtil.crossStreams(inputStream, System.out);
            // Manually verify the output in console
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            assertNotNull(DataUtil.readToByteBuffer(inputStream, 1024));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSizeUnlimited() {
        try {
            assertNotNull(DataUtil.readToByteBuffer(inputStream, 0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=utf-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        String bomUTF8 = "\uFEFF"; // UTF-8 BOM
        String bomUTF16LE = "\uFFFE"; // UTF-16 LE BOM
        String bomUTF16BE = "\uFEFF"; // UTF-16 BE BOM
        String bomUTF32LE = "\u0000\u0000\uFEFF"; // UTF-32 LE BOM
        String bomUTF32BE = "\uFEFF\u0000\u0000"; // UTF-32 BE BOM

        byte[] bomBytesUTF8 = bomUTF8.getBytes();
        byte[] bomBytesUTF16LE = bomUTF16LE.getBytes();
        byte[] bomBytesUTF16BE = bomUTF16BE.getBytes();
        byte[] bomBytesUTF32LE = bomUTF32LE.getBytes();
        byte[] bomBytesUTF32BE = bomUTF32BE.getBytes();

        ByteBuffer byteBufferUTF8 = ByteBuffer.wrap(bomBytesUTF8);
        ByteBuffer byteBufferUTF16LE = ByteBuffer.wrap(bomBytesUTF16LE);
        ByteBuffer byteBufferUTF16BE = ByteBuffer.wrap(bomBytesUTF16BE);
        ByteBuffer byteBufferUTF32LE = ByteBuffer.wrap(bomBytesUTF32LE);
        ByteBuffer byteBufferUTF32BE = ByteBuffer.wrap(bomBytesUTF32BE);

        DataUtil.BomCharset bomCharsetUTF8 = DataUtil.detectCharsetFromBom(byteBufferUTF8);
        DataUtil.BomCharset bomCharsetUTF16LE = DataUtil.detectCharsetFromBom(byteBufferUTF16LE);
        DataUtil.BomCharset bomCharsetUTF16BE = DataUtil.detectCharsetFromBom(byteBufferUTF16BE);
        DataUtil.BomCharset bomCharsetUTF32LE = DataUtil.detectCharsetFromBom(byteBufferUTF32LE);
        DataUtil.BomCharset bomCharsetUTF32BE = DataUtil.detectCharsetFromBom(byteBufferUTF32BE);

        assertNotNull(bomCharsetUTF8);
        assertNotNull(bomCharsetUTF16LE);
        assertNotNull(bomCharsetUTF16BE);
        assertNotNull(bomCharsetUTF32LE);
        assertNotNull(bomCharsetUTF32BE);

        assertEquals("UTF-8", bomCharsetUTF8.charset);
        assertEquals("UTF-16", bomCharsetUTF16LE.charset);
        assertEquals("UTF-16", bomCharsetUTF16BE.charset);
        assertEquals("UTF-32", bomCharsetUTF32LE.charset);
        assertEquals("UTF-32", bomCharsetUTF32BE.charset);

        assertEquals(0, bomCharsetUTF8.offset);
        assertEquals(0, bomCharsetUTF16LE.offset);
        assertEquals(0, bomCharsetUTF16BE.offset);
        assertEquals(0, bomCharsetUTF32LE.offset);
        assertEquals(0, bomCharsetUTF32BE.offset);
    }
}