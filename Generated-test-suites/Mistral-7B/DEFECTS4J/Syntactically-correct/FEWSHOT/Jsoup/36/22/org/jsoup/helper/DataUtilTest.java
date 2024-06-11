package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test cases for DataUtil class.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFileWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>";
        File testFile = new File("testFile.xml");
        FileOutputStream testFileOut = new FileOutputStream(testFile);
        testFileOut.write(fileContent.getBytes());
        testFileOut.close();

        Document document = DataUtil.load(testFile, null);
        assertNotNull(document);
        assertEquals("<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>", document.html());
    }

    @Test
    public void testLoadDocumentFromFileWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>\n<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>";
        File testFile = new File("testFile.xml");
        FileOutputStream testFileOut = new FileOutputStream(testFile);
        testFileOut.write(fileContent.getBytes("UTF-16LE"));
        testFileOut.close();

        Document document = DataUtil.load(testFile, "UTF-16LE");
        assertNotNull(document);
        assertEquals("<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>", document.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        Document document = DataUtil.load(inputStream, null);
        assertNotNull(document);
        assertEquals("<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>", document.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithSpecifiedCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>\n<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-16LE"));
        Document document = DataUtil.load(inputStream, "UTF-16LE");
        assertNotNull(document);
        assertEquals("<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>", document.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        Parser parser = mock(Parser.class);
        when(parser.parseInput(anyString(), anyString())).thenReturn(mock(Document.class));

        Document document = DataUtil.load(inputStream, null, null, parser);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadDocumentFromNullInputStream() throws IOException {
        DataUtil.load((InputStream) null, null, null, null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadDocumentFromNonexistentFile() throws IOException {
        File nonexistentFile = new File("nonexistentFile.xml");
        Document document = DataUtil.load(nonexistentFile, null);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String documentData = "<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(documentData.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>", document.html());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String documentData = "<?xml version=\"1.0\" encoding=\"UTF-16LE\"?>\n<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(documentData.getBytes("UTF-16LE"));
        Document document = DataUtil.parseByteData(byteData, "UTF-16LE", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>", document.html());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(inputStreamContent.length(), byteData.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(10, byteData.limit());
    }
}