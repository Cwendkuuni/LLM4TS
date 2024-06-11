package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseByteData() {
        // Test with default charset and no BOM
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'H', 'e', 'l', 'l', 'o'});
        String charsetName = null;
        String baseUri = null;
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(5, document.body().children().size());

        // Test with specified charset and no BOM
        charsetName = "UTF-8";
        byteData = ByteBuffer.wrap(new byte[]{'H', 'e', 'l', 'l', 'o'});
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(5, document.body().children().size());

        // Test with BOM and default charset
        byteData = ByteBuffer.wrap(new byte[]{UNICODE_BOM, 'H', 'e', 'l', 'l', 'o'});
        charsetName = null;
        baseUri = null;
        parser = Parser.htmlParser();
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(5, document.body().children().size());

        // Test with BOM and specified charset
        charsetName = "UTF-8";
        byteData = ByteBuffer.wrap(new byte[]{UNICODE_BOM, 'H', 'e', 'l', 'l', 'o'});
        baseUri = null;
        parser = Parser.htmlParser();
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(5, document.body().children().size());

        // Test with invalid charset
        charsetName = "ABCDEFCG";
        byteData = ByteBuffer.wrap(new byte[]{'H', 'e', 'l', 'l', 'o'});
        baseUri = null;
        parser = Parser.htmlParser();
        try {
            document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid charset: ABCDEFCG", e.getMessage());
        }

        // Test with empty byte buffer
        byteData = DataUtil.emptyByteBuffer();
        charsetName = null;
        baseUri = null;
        parser = Parser.htmlParser();
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(document);
    }

    @Test
    public void testLoad() throws IOException {
        String testFilePath = "src/test/resources/testFile.html";

        // Test loading file with default charset
        File file = new File(testFilePath);
        Document document = DataUtil.load(file, null, null);
        assertNotNull(document);

        // Test loading file with specified charset
        Charset charset = Charset.forName("UTF-8");
        document = DataUtil.load(file, charset.name(), null);
        assertNotNull(document);

        // Test loading non-existent file
        File nonExistentFile = new File("nonExistentFile.html");
        try {
            DataUtil.load(nonExistentFile, null, null);
            fail();
        } catch (IOException e) {
            // Expected
        }

        // Test loading input stream
        InputStream inputStream = getClass().getResourceAsStream("/testFile.html");
        Document documentFromInputStream = DataUtil.load(inputStream, null, null);
        assertNotNull(documentFromInputStream);
    }

    @Test
    public void testLoadWithParser() throws IOException {
        String testFilePath = "src/test/resources/testFile.html";

        // Test loading file with default parser
        File file = new File(testFilePath);
        Document document = DataUtil.load(file, null, null, Parser.htmlParser());
        assertNotNull(document);

        // Test loading file with xml parser
        Document documentFromXmlParser = DataUtil.load(file, null, null, Parser.xmlParser());
        assertNotNull(documentFromXmlParser);

        // Test loading input stream with default parser
        InputStream inputStream = getClass().getResourceAsStream("/testFile.html");
        Document documentFromInputStreamWithDefaultParser = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        assertNotNull(documentFromInputStreamWithDefaultParser);

        // Test loading input stream with xml parser
        Document documentFromInputStreamWithXmlParser = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(documentFromInputStreamWithXmlParser);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);

        contentType = "application/xml; charset=UTF-16";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-16", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int length = boundary.length();
        for (int i = 0; i < length; i++) {
            char c = boundary.charAt(i);
            assertTrue(Character.isDefinedChar(c));
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Test reading from input stream and writing to output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] output = outputStream.toByteArray();
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, output);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedByteArray = new byte[]{0x01, 0x02, 0x03};
        byteBuffer.flip();
        byte[] actualByteArray = new byte[byteBuffer.remaining()];
        byteBuffer.get(actualByteArray);
        assertArrayEquals(expectedByteArray, actualByteArray);

        byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        byteArrayToByteBuffer(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A}, byteBuffer);
        byteBuffer.flip();
        actualByteArray = new byte[byteBuffer.remaining()];
        byteBuffer.get(actualByteArray);
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A}, actualByteArray);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("src/test/resources/testFile.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        byte[] expectedByteArray = readFileToByteArray("src/test/resources/testFile.html");
        byteBuffer.flip();
        byte[] actualByteArray = new byte[byteBuffer.remaining()];
        byteBuffer.get(actualByteArray);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.remaining());
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testCharsetPattern() {
        Pattern pattern = DataUtil.charsetPattern;
        Matcher matcher = pattern.matcher("text/html; charset=UTF-8");
        assertTrue(matcher.find());
        assertEquals("UTF-8", matcher.group(1));

        matcher = pattern.matcher("text/html; charset=ISO-8859-1");
        assertTrue(matcher.find());
        assertEquals("ISO-8859-1", matcher.group(1));

        matcher = pattern.matcher("text/html");
        assertFalse(matcher.find());
    }

    private void byteArrayToByteBuffer(byte[] bytes, ByteBuffer byteBuffer) {
        byteBuffer.put(bytes);
        byteBuffer.flip();
    }

    private byte[] readFileToByteArray(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] byteArray = new byte[inputStream.available()];
        inputStream.read(byteArray);
        inputStream.close();
        return byteArray;
    }
}