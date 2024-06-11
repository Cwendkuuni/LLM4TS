package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     * @throws IOException
     */
    @Test
    public void testLoadFromFile() throws IOException {
        String charsetName = StandardCharsets.UTF_8.name();
        File file = new File("test.html");
        Document doc = DataUtil.load(file, charsetName, null);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)} with UTF-8 charset.
     * @throws IOException
     */
    @Test
    public void testLoadFromInputStreamUTF8() throws IOException {
        String charsetName = StandardCharsets.UTF_8.name();
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charsetName, null);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)} with different charset.
     * @throws IOException
     */
    @Test
    public void testLoadFromInputStreamDifferentCharset() throws IOException {
        String charsetName = "ISO-8859-1";
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charsetName, null);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with empty byte buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with unsupported charset.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataUnsupportedCharset() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{0x00, 0x01, 0x02, 0x03});
        String charsetName = "INVALID_CHARSET";
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
    }

    /**
     * Test method for {@link DataUtil#detectCharsetFromBom(ByteBuffer, String)} with no BOM.
     */
    @Test
    public void testDetectCharsetFromBomNoBOM() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{0x00, 0x01, 0x02, 0x03});
        String charsetName = DataUtil.detectCharsetFromBom(byteBuffer, null);
        assertEquals("", charsetName);
    }

    /**
     * Test method for {@link DataUtil#detectCharsetFromBom(ByteBuffer, String)} with UTF-8 BOM.
     */
    @Test
    public void testDetectCharsetFromBomUTF8() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF, 0x00, 0x01, 0x02, 0x03});
        String charsetName = DataUtil.detectCharsetFromBom(byteBuffer, null);
        assertEquals("UTF-8", charsetName);
    }
}