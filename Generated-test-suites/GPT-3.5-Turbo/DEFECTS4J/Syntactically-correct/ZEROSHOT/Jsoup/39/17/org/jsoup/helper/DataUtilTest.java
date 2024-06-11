package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    private File file;
    private InputStream inputStream;

    @Before
    public void setUp() throws IOException {
        file = new File("testFile.txt");
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("<html><head></head><body></body></html>");
        writer.close();
        inputStream = new FileInputStream(file);
    }

    @Test
    public void testLoad_FileCharsetBaseUri_Success() throws IOException {
        Document document = DataUtil.load(file, "UTF-8", "http://www.example.com");
        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertEquals("UTF-8", document.outputSettings().charset().displayName());
    }

    @Test
    public void testLoad_InputStreamCharsetBaseUri_Success() throws IOException {
        Document document = DataUtil.load(inputStream, "UTF-8", "http://www.example.com");
        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertEquals("UTF-8", document.outputSettings().charset().displayName());
    }

    @Test
    public void testLoad_InputStreamCharsetBaseUriParser_Success() throws IOException {
        Document document = DataUtil.load(inputStream, "UTF-8", "http://www.example.com", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertEquals("UTF-8", document.outputSettings().charset().displayName());
    }

    @Test
    public void testParseByteData_CharsetNameNull_Success() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        String charsetName = null;
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertEquals("UTF-8", document.outputSettings().charset().displayName());
    }

    @Test
    public void testParseByteData_CharsetNameSpecified_Success() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertEquals("UTF-8", document.outputSettings().charset().displayName());
    }

    @Test
    public void testReadToByteBuffer_InputSteamMaxSize_Success() throws IOException {
        int maxSize = 100;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteBuffer);
        assertEquals(maxSize, byteBuffer.capacity());
    }

    @Test
    public void testReadToByteBuffer_InputStream_Success() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType_ContentTypeSupportedCharset_Success() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_ContentTypeNotSupportedCharset_Success() {
        String contentType = "text/html; charset=ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("ISO-8859-1", charset);
    }

    @Test
    public void testGetCharsetFromContentType_ContentTypeEmpty_ReturnsNull() {
        String contentType = "";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_ContentTypeNull_ReturnsNull() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testLoad_FileCharsetBaseUri_FileNotFoundException() {
        try {
            File nonExistentFile = new File("non_existent_file.txt");
            Document document = DataUtil.load(nonExistentFile, "UTF-8", "http://www.example.com");
        } catch (FileNotFoundException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void testLoad_InputStreamCharsetBaseUri_IOException() throws IOException {
        InputStream invalidInputStream = new FileInputStream(new File("invalid_file.txt"));
        Document document = DataUtil.load(invalidInputStream, "UTF-8", "http://www.example.com");
    }
}