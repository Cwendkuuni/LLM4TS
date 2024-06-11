package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileWithCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Test</h1></body></html>";
        File inputFile = new File("input.xml");
        Files.write(inputFile.toPath(), fileContent.getBytes());

        Document doc = DataUtil.load(inputFile, "UTF-8", null);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc.body());
        assertEquals("Test", doc.body().text());

        Files.delete(inputFile.toPath());
    }

    @Test
    public void testLoadFileWithoutCharset() throws IOException {
        String fileContent = "<html><body><h1>Test</h1></body></html>";
        File inputFile = new File("input.xml");
        Files.write(inputFile.toPath(), fileContent.getBytes());

        Document doc = DataUtil.load(inputFile, null, null);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc.body());
        assertEquals("Test", doc.body().text());

        Files.delete(inputFile.toPath());
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc.body());
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithoutCharset() throws IOException {
        String fileContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc.body());
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String fileContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        try {
            Document doc = DataUtil.load(inputStream, "ISO-8859-1", null);
            assertFalse(true);
        } catch (IOException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.xmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc.body());
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testParseInputStreamWithoutCharset() throws IOException {
        String fileContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc.body());
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String fileContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        try {
            Document doc = DataUtil.parseInputStream(inputStream, "ISO-8859-1", null, Parser.xmlParser());
            assertFalse(true);
        } catch (IOException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String data = "Test data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(data, outputStream.toString());
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(boundary.length(), 32);
    }
}