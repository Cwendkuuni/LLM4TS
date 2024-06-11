package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Parser.OutputSettings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        // test empty byte array
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals(Document.class, doc.getClass());
        assertEquals("", doc.body().text());

        // test null charsetName
        byteData = ByteBuffer.wrap("Hello, World!".getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertEquals(Document.class, doc.getClass());
        assertEquals("Hello, World!", doc.body().text());
        assertEquals("UTF-8", doc.outputSettings().charset());

        // test empty input stream
        InputStream in = new ByteArrayInputStream(new byte[0]);
        doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(in), charsetName, baseUri, parser);
        assertEquals(Document.class, doc.getClass());
        assertEquals("", doc.body().text());

        // test null baseUri
        byteData = ByteBuffer.wrap("Hello, World!".getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseByteData(byteData, charsetName, null, parser);
        assertEquals(Document.class, doc.getClass());
        assertEquals("", doc.body().text());
        assertEquals("UTF-8", doc.outputSettings().charset());

        // test non-UTF-8 byte array
        charsetName = "ISO-8859-1";
        byteData = ByteBuffer.wrap("Hello, World!".getBytes(StandardCharsets.ISO_8859_1));
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals(Document.class, doc.getClass());
        assertEquals("Hello, World!", doc.body().text());
        assertEquals("ISO-8859-1", doc.outputSettings().charset());

        // test BOM in byte array
        charsetName = null;
        byteData = ByteBuffer.wrap(new byte[] { UNICODE_BOM, 'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '!' });
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals(Document.class, doc.getClass());
        assertEquals("Hello, World!", doc.body().text());
        assertEquals(defaultCharset, doc.outputSettings().charset());

        // test invalid charsetName
        charsetName = "x-invalid";
        byteData = ByteBuffer.wrap("Hello, World!".getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalCharsetNameException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, parser));

        // test valid charsetName
        charsetName = "US-ASCII";
        byteData = ByteBuffer.wrap("Hello, World!".getBytes(StandardCharsets.US_ASCII));
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals(Document.class, doc.getClass());
        assertEquals("Hello, World!", doc.body().text());
        assertEquals("US-ASCII", doc.outputSettings().charset());

        // test reading byte buffer midstream
        byteData = ByteBuffer.wrap("Hello, World!".getBytes(StandardCharsets.UTF_8));
        byte[] buffer = new byte[10];
        int read = 0;
        int totalRead = 0;
        OutputStream out = new ByteArrayOutputStream();
        while (read != -1) {
            read = byteData.get(buffer);
            if (read > 0) {
                out.write(buffer, 0, read);
                totalRead += read;
            }
        }
        InputStream in2 = new ByteArrayInputStream(out.toByteArray());
        doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(in2), charsetName, baseUri, parser);
        assertEquals(Document.class, doc.getClass());
        assertEquals("Hello, World!", doc.body().text());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=x-invalid";
        assertNull(DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=UTF-8, charset=ISO-8859-1";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=UTF-8, charset=ISO-8859-1, charset=euc-jp";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testLoad() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        File file = new File("data/testfile.html");
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertEquals(Document.class, doc.getClass());
        assertEquals("Hello, World!", doc.body().text());
        assertEquals(charsetName, doc.outputSettings().charset());

        InputStream inputStream = this.getClass().getResourceAsStream("/data/testfile.html");
        doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(Document.class, doc.getClass());
        assertEquals("Hello, World!", doc.body().text());
        assertEquals(charsetName, doc.outputSettings().charset());

        byte[] fileBytes = new byte[1024];
        int bytesRead = 0;
        InputStream inputFileStream = new FileInputStream(file);
        while ((bytesRead = inputFileStream.read(fileBytes)) != -1) {
            inputStream = new ByteArrayInputStream(fileBytes, 0, bytesRead);
            doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertEquals(Document.class, doc.getClass());
            assertEquals("Hello, World!", doc.body().text());
            assertEquals(charsetName, doc.outputSettings().charset());
            break;
        }
        inputFileStream.close();
        inputStream.close();

        byte[] testData = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testData);
        doc = DataUtil.load(testInputStream, charsetName, baseUri);
        assertEquals(Document.class, doc.getClass());
        assertEquals("Hello, World!", doc.body().text());
        assertEquals(charsetName, doc.outputSettings().charset());
        testInputStream.close();
    }

    @Test
    public void testLoadWithDifferentParsers() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parserHtml = Parser.htmlParser();
        Parser parserXml = Parser.xmlParser();

        File file = new File("data/testfile.html");
        Document docHtml = DataUtil.load(file, charsetName, baseUri, parserHtml);
        assertEquals(Document.class, docHtml.getClass());
        assertEquals("Hello, World!", docHtml.body().text());
        assertEquals("UTF-8", docHtml.outputSettings().charset());

        Document docXml = DataUtil.load(file, charsetName, baseUri, parserXml);
        assertEquals(Document.class, docXml.getClass());
        assertEquals("", docXml.body().text());
        assertEquals("UTF-8", docXml.outputSettings().charset());

        InputStream inputStream = this.getClass().getResourceAsStream("/data/testfile.html");
        docHtml = DataUtil.load(inputStream, charsetName, baseUri, parserHtml);
        assertEquals(Document.class, docHtml.getClass());
        assertEquals("Hello, World!", docHtml.body().text());
        assertEquals("UTF-8", docHtml.outputSettings().charset());

        docXml = DataUtil.load(inputStream, charsetName, baseUri, parserXml);
        assertEquals(Document.class, docXml.getClass());
        assertEquals("", docXml.body().text());
        assertEquals("UTF-8", docXml.outputSettings().charset());
    }

    @Test
    public void testLoadWithNullCharsetName() throws IOException {
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        File file = new File("data/testfile.html");
        Document doc = DataUtil.load(file, null, baseUri);
        assertEquals(Document.class, doc.getClass());
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testLoadWithEmptyCharsetName() throws IOException {
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        File file = new File("data/testfile.html");
        Document doc = DataUtil.load(file, "", baseUri);
        assertEquals(Document.class, doc.getClass());
        assertEquals("Hello, World!", doc.body().text());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadWithNullBaseUri() throws IOException {
        String charsetName = "UTF-8";
        Parser parser = Parser.htmlParser();

        File file = new File("data/testfile.html");
        Document doc = DataUtil.load(file, charsetName, null);
        assertEquals(Document.class, doc.getClass());
        assertEquals("Hello, World!", doc.body().text());
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testLoadWithNullFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.load(null, charsetName, baseUri);
        assertEquals(Document.class, doc.getClass());
        assertEquals("", doc.body().text());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadWithNullCharsetNameAndNullBaseUri() throws IOException {
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.load(new File("data/testfile.html"), null, null);
        assertEquals(Document.class, doc.getClass());
        assertEquals("", doc.body().text());
    }

    @Test
    public void testLoadWithNullParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document doc = DataUtil.load(new File("data/testfile.html"), charsetName, baseUri, null);
        assertEquals(Document.class, doc.getClass());
        assertEquals("", doc.body().text());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFilePath = "data/testinput.txt";
        String outputFilePath = "data/testoutput.txt";

        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expected = new byte[inputFile.length()];
        inputStream.read(expected);
        inputStream.close();

        byte[] actual = new byte[outputFile.length()];
        outputStream.read(actual);
        outputStream.close();

        assertArrayEquals("Input and output streams should contain the same data", expected, actual);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue("Boundary string should be at least " + boundaryLength + " characters long", boundary.length() >= boundaryLength);
        for (int i = 0; i < boundaryLength; i++) {
            char expectedChar = mimeBoundaryChars[i % mimeBoundaryChars.length];
            char actualChar = boundary.charAt(i);
            assertEquals("Unexpected character at index " + i, expectedChar, actualChar);
        }
    }
}