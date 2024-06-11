package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        File testFile = new File("test.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadFileWithNullCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        File testFile = new File("test.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document doc = DataUtil.load(testFile, null, null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
        FileUtils.deleteQuietly(testFile);

        doc = DataUtil.load(testFile, null, null); // reusing the same file
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());

        // This time, we should attempt to detect the charset from the file
        doc = DataUtil.load(testFile, null, null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadFileWithIncorrectCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        File testFile = new File("test.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document doc = DataUtil.load(testFile, "windows-1252", null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadFileWithEmptyContent() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, null, null);
        assertNull(doc);
        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadFileWithNullFile() {
        assertThrows(FileNotFoundException.class, () -> DataUtil.load(null, null, null));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadInputStreamWithNullCharset() {
        String inputContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadInputStreamWithIncorrectCharset() {
        String inputContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "windows-1252", null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadInputStreamWithEmptyContent() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataUtil.load(new ByteArrayInputStream(new byte[0]), null, null);
        assertNull(byteArrayOutputStream.toByteArray());
    }

    @Test
    public void testLoadInputStreamWithNullInput() {
        assertThrows(IOException.class, () -> DataUtil.load(null, null, null));
    }

    @Test
    public void testParseByteData() {
        String documentContent = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(documentContent.getBytes("UTF-8"));

        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", null, Parser.htmlParser());
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        String documentContent = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(documentContent.getBytes("UTF-8"));

        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());

        String expectedContentType = "text/html; charset=UTF-8";
        byteBuffer.rewind();
        doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());

        String charsetFromContentType = DataUtil.getCharsetFromContentType(expectedContentType);
        doc = DataUtil.parseByteData(byteBuffer, charsetFromContentType, null, Parser.htmlParser());
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseByteDataWithIncorrectCharset() {
        String documentContent = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(documentContent.getBytes("UTF-8"));

        Document doc = DataUtil.parseByteData(byteBuffer, "windows-1252", null, Parser.htmlParser());
        assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseByteDataWithEmptyContent() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[0]);

        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testParseByteDataWithNullByteBuffer() {
        assertNull(DataUtil.parseByteData(null, null, null, Parser.htmlParser()));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(inputContent.length(), byteData.limit());

        byte[] expectedData = inputContent.getBytes("UTF-8");
        for (int i = 0; i < expectedData.length; i++) {
            assertEquals(expectedData[i], byteData.get(i));
        }
    }

    @Test
    public void testReadToByteBufferWithNullInputStream() {
        assertThrows(IOException.class, () -> DataUtil.readToByteBuffer(null));
    }

    @Test
    public void testReadToByteBufferWithMaxSizeGreaterThanBufferSize() {
        String inputContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, Integer.MAX_VALUE);
        assertEquals(inputContent.length(), byteData.limit());

        byte[] expectedData = inputContent.getBytes("UTF-8");
        for (int i = 0; i < expectedData.length; i++) {
            assertEquals(expectedData[i], byteData.get(i));
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSizeLessThanBufferSize() {
        String inputContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 17);
        assertEquals(17, byteData.limit());

        byte[] expectedData = new byte[17];
        inputStream.read(expectedData);

        for (int i = 0; i < 17; i++) {
            assertEquals(expectedData[i], byteData.get(i));
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        assertNull(DataUtil.getCharsetFromContentType(null));
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnknownCharset() {
        String contentType = "text/html; charset=x-pig-latin";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}