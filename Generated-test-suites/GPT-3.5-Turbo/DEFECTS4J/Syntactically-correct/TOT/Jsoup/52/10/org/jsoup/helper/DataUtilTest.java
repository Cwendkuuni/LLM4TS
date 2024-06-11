package org.jsoup.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DataUtilTest {

    private final String testFile;
    private final String charsetName;
    private final String baseUri;
    private final String expectedOutput;

    public DataUtilTest(String testFile, String charsetName, String baseUri, String expectedOutput) {
        this.testFile = testFile;
        this.charsetName = charsetName;
        this.baseUri = baseUri;
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"test.html", "UTF-8", "http://example.com", "<html><head></head><body><h1>Hello World!</h1></body></html>"},
                {"test.xml", "UTF-8", "http://example.com", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>Hello World!</root>"},
                {"test.html", "ISO-8859-1", "http://example.com", "<html><head></head><body><h1>Hello World&#33;</h1></body></html>"}
        });
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File(getClass().getClassLoader().getResource(testFile).getFile());
        Document document = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(expectedOutput, document.toString());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(getClass().getClassLoader().getResource(testFile).getFile());
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(expectedOutput, document.toString());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream(getClass().getClassLoader().getResource(testFile).getFile());
        Parser parser = Parser.xmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(expectedOutput, document.toString());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream(getClass().getClassLoader().getResource(testFile).getFile());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertNotNull(outputStream.toString());
        assertNotEquals("", outputStream.toString().trim());
    }

    @Test
    public void testParseByteData() throws IOException {
        InputStream inputStream = new FileInputStream(getClass().getClassLoader().getResource(testFile).getFile());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        assertNotNull(document);
        assertEquals(expectedOutput, document.toString());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(getClass().getClassLoader().getResource(testFile).getFile());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteData);
        assertNotEquals(0, byteData.capacity());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File(getClass().getClassLoader().getResource(testFile).getFile());
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);

        assertNotNull(byteData);
        assertNotEquals(0, byteData.capacity());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();

        assertNotNull(byteData);
        assertEquals(0, byteData.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("ISO-8859-1", charset);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, '<', 'r', 'o', 'o', 't', '>', '<', '/', 'r', 'o', 'o', 't', '>'});
        String charsetName = DataUtil.detectCharsetFromBom(byteData, null);

        assertNotNull(charsetName);
        assertEquals("UTF-8", charsetName);
    }
}