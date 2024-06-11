package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testMimeBoundary() {
        assertEquals(32, DataUtil.mimeBoundary().length());
        String boundary = DataUtil.mimeBoundary();
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Character.isDefinedIsAlphabetic(boundary.charAt(i)) || Character.isDigit(boundary.charAt(i)) || boundary.charAt(i) == '-');
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("US-ASCII", DataUtil.getCharsetFromContentType("text/plain; charset=US-ASCII"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("GB18030", DataUtil.getCharsetFromContentType("text/html; charset=gb18030"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html"));
        assertEquals(null, DataUtil.getCharsetFromContentType("application/octet-stream"));
    }

    @Test
    public void testParseByteData() throws IOException {
        String docData = "Hello, World!";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName("UTF-8")));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals(docData, doc.body().text());

        docData = "\uFEFFHello, World!";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName("UTF-8")));
        doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(docData.substring(1), doc.body().text());

        docData = "<html><head><meta charset=\"GB18030\"/></head><body>Hello, World!</body></html>";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName("UTF-8")));
        doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(docData, doc.html());

        docData = "<html><head><meta charset=\"GB18030\"/></head><body>Hello, World!</body></html>";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName("UTF-8")));
        doc = DataUtil.parseByteData(byteData, "GB18030", null, Parser.htmlParser());
        assertEquals(docData, doc.html());

        docData = "<html><head><meta charset=\"GB18030\"/></head><body>Hello, World!</body></html>";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName("UTF-8")));
        doc = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
        assertEquals(docData, doc.xml().asXml());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");
        byte[] fileData = "Hello, World!".getBytes(Charset.forName("UTF-8"));
        try (RandomAccessFile output = new RandomAccessFile(file, "rw")) {
            output.write(fileData);
        }

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertEquals(fileData.length, byteData.limit());

        byte[] resultData = new byte[byteData.limit()];
        byteData.get(resultData);
        assertEquals(fileData, resultData);

        assertEquals(fileData, new String(resultData, Charset.forName("UTF-8")));

        try {
            DataUtil.readFileToByteBuffer(null);
            fail("Expected FileNotFoundException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            DataUtil.readFileToByteBuffer(new File("nonexistentfile.txt"));
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // Expected
        }

        try {
            DataUtil.readFileToByteBuffer(file);
            file.delete();
            fail("Expected FileNotFoundException after deletion");
        } catch (FileNotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{"Hello, World!"}));
        assertEquals(13, byteData.limit());

        byte[] resultData = new byte[byteData.limit()];
        byteData.get(resultData);
        assertEquals("Hello, World!".getBytes(Charset.forName("UTF-8")), resultData);

        assertEquals("Hello, World!", new String(resultData, Charset.forName("UTF-8")));

        byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{}));
        assertEquals(0, byteData.limit());

        byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(null));
        assertNull(byteData);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Hello, World!";
        ByteArrayOutputStream outputData = new ByteArrayOutputStream();

        try (InputStream input = new ByteArrayInputStream(inputData.getBytes(Charset.forName("UTF-8")))) {
            DataUtil.crossStreams(input, outputData);
            byte[] resultData = outputData.toByteArray();
            assertEquals(inputData, new String(resultData, Charset.forName("UTF-8")));
        }
    }

    @Test
    public void testLoad() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"/></head><body>Hello, World!</body></html>";
        File testFile = new File("test.html");
        try (RandomAccessFile output = new RandomAccessFile(testFile, "rw")) {
            output.write(fileContent.getBytes(Charset.forName("UTF-8")));
        }

        Document doc = DataUtil.load(testFile, null, null);
        assertEquals(fileContent, doc.html());
        testFile.delete();

        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));
        doc = DataUtil.load(testInputStream, "UTF-8", null);
        assertEquals(fileContent, doc.html());

        testInputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));
        try {
            Document doc2 = DataUtil.load(testInputStream, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        testInputStream = null;
        try {
            Document doc3 = DataUtil.load(testInputStream, "UTF-8", null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}