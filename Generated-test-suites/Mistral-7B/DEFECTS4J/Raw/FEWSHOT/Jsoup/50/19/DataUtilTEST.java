package org.jsoup.helper;

import static org.junit.Assert. assertArrayEquals;
import static org.junit.Assert. assertFalse;
import static org.junit.Assert. assertNull;
import static org.junit.Assert. assertSame;
import static org.junit.Assert. assertEquals;
import static org.junit.Assert. assertNotEquals;
import static org.junit.Assert. assertTrue;
import static org.junit.Assert. fail;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseByteData() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Test empty byte buffer
        ByteBuffer byteDataEmpty = DataUtil.emptyByteBuffer();
        Document docEmpty = DataUtil.parseByteData(byteDataEmpty, charsetName, baseUri, parser);
        assertNull(docEmpty);

        // Test null input
        InputStream inNull = null;
        Document docNull = null;
        try {
            docNull = DataUtil.load(inNull, charsetName, baseUri, parser);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        assertNull(docNull);

        // Test empty input stream
        ByteArrayOutputStream outEmpty = new ByteArrayOutputStream();
        OutputStream osEmpty = outEmpty.outputStream();
        DataUtil.crossStreams(new FileInputStream(new File("")), osEmpty); // empty file
        ByteBuffer byteDataEmptyStream = DataUtil.readToByteBuffer(new ByteArrayInputStream(outEmpty.toByteArray()));
        Document docEmptyStream = DataUtil.parseByteData(byteDataEmptyStream, charsetName, baseUri, parser);
        assertNull(docEmptyStream);

        // Test valid input
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World!</div></body></html>";
        OutputStream os = out.outputStream();
        os.write(html.getBytes(Charset.forName(charsetName)));
        Document docValid = DataUtil.parseByteData(DataUtil.readToByteBuffer(new ByteArrayInputStream(out.toByteArray())), charsetName, baseUri, parser);
        assertEquals(html, docValid.html());

        // Test invalid charset name
        String invalidCharsetName = "invalid-charset";
        String errorMessage = "Error parsing input as charset: " + invalidCharsetName;
        try {
            Document docInvalidCharset = DataUtil.parseByteData(DataUtil.readToByteBuffer(new ByteArrayInputStream(out.toByteArray())), invalidCharsetName, baseUri, parser);
            fail(errorMessage);
        } catch (IOException e) {
            // Expected
        }
        assertNull(docInvalidCharset);

        // Test BOM detection
        ByteArrayOutputStream outBom = new ByteArrayOutputStream();
        OutputStream osBom = outBom.outputStream();
        osBom.write(new byte[]{UNICODE_BOM}); // UTF-BOM
        osBom.write(html.getBytes(Charset.forName(charsetName)));
        ByteBuffer byteDataBom = DataUtil.readToByteBuffer(new ByteArrayInputStream(outBom.toByteArray()));
        Document docBom = DataUtil.parseByteData(byteDataBom, charsetName, baseUri, parser);
        assertEquals(html, docBom.html());
        assertEquals(charsetName, docBom.outputSettings().charset());
    }

    @Test
    public void testLoad() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Test empty file
        File fileEmpty = new File("");
        Document docEmpty = DataUtil.load(fileEmpty, charsetName, baseUri);
        assertNull(docEmpty);

        // Test null file
        File fileNull = null;
        Document docNull = null;
        try {
            docNull = DataUtil.load(fileNull, charsetName, baseUri);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        assertNull(docNull);

        // Test valid file
        String fileName = "test.html";
        File fileValid = new File(fileName);
        String expectedHtml = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World!</div></body></html>";
        FileOutputStream fos = new FileOutputStream(fileValid);
        fos.write(expectedHtml.getBytes(Charset.forName(charsetName)));
        fos.close();
        Document docValid = DataUtil.load(fileValid, charsetName, baseUri);
        assertEquals(expectedHtml, docValid.html());
        assertEquals(charsetName, docValid.outputSettings().charset());
        fileValid.delete();

        // Test file with invalid charset name
        File fileInvalidCharset = new File(fileName);
        String invalidCharsetName = "invalid-charset";
        String errorMessage = "Error parsing file as charset: " + invalidCharsetName;
        try {
            Document docInvalidCharset = DataUtil.load(fileInvalidCharset, invalidCharsetName, baseUri);
            fail(errorMessage);
        } catch (IOException e) {
            // Expected
        }
        assertNull(docInvalidCharset);

        // Test file with BOM
        String fileNameBom = "testBom.html";
        File fileBom = new File(fileNameBom);
        String expectedHtmlBom = "<html>\uFEFF<head><meta charset=\"UTF-8\"></head><body><div>Hello World!</div></body></html>";
        FileOutputStream fosBom = new FileOutputStream(fileBom);
        fosBom.write(expectedHtmlBom.getBytes(Charset.forName(charsetName)));
        fosBom.close();
        Document docBom = DataUtil.load(fileBom, charsetName, baseUri);
        assertEquals(expectedHtmlBom, docBom.html());
        assertEquals(charsetName, docBom.outputSettings().charset());
        fileBom.delete();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=EUC-JP";
        assertEquals("EUC-JP", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=gb2312";
        assertEquals("gb2312", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=windows-1252";
        assertEquals("windows-1252", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=Shift_JIS";
        assertEquals("Shift_JIS", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testMimeBoundary() {
        assertEquals(boundaryLength, DataUtil.mimeBoundary().length());

        String boundary = DataUtil.mimeBoundary();
        StringBuilder boundaryBuilder = new StringBuilder(boundary);
        for (int i = 0; i < boundaryLength; i++) {
            if (boundary.charAt(i) != mimeBoundaryChars[i % mimeBoundaryChars.length]) {
                fail("Boundary does not match expected characters");
            }
            boundaryBuilder.setCharAt(i, (char) (i % mimeBoundaryChars.length + 'A' + new Random().nextInt(mimeBoundaryChars.length)));
        }
        assertNotEquals(boundary, boundaryBuilder.toString());

        boundary = DataUtil.mimeBoundary();
        for (int i = 0; i < boundaryLength; i++) {
            assertTrue(DataUtil.mimeBoundaryChars[i % mimeBoundaryChars.length] == boundary.charAt(i) ||
                      DataUtil.mimeBoundaryChars[(i % mimeBoundaryChars.length + mimeBoundaryChars.length) % mimeBoundaryChars.length] == boundary.charAt(i));
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream(new File("")); // empty file
        OutputStream out = new FileOutputStream("testCrossStreams.txt");
        DataUtil.crossStreams(in, out);
        assertEquals(0, out.size());
        in.close();
        out.close();

        in = new FileInputStream(new File("testCrossStreams.txt"));
        ByteArrayOutputStream outTest = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, outTest);
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outTest.toByteArray()));
        assertEquals(0, byteData.limit());
        in.close();
        outTest.close();

        in = new FileInputStream(new File("testCrossStreams.txt"));
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        DataUtil.crossStreams(in, out);
        byte[] buffer = out.toByteArray();
        ByteBuffer byteDataBuffer = ByteBuffer.wrap(buffer);
        assertEquals(0, byteDataBuffer.limit());
        in.close();
        out.close();

        in = new FileInputStream(new File("testCrossStreams.txt"));
        ByteArrayOutputStream outTest2 = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, outTest2);
        byte[] bufferTest = outTest2.toByteArray();
        ByteBuffer byteDataBufferTest = ByteBuffer.wrap(bufferTest);
        assertEquals(0, byteDataBufferTest.limit());
        in.close();
        outTest2.close();

        in = new FileInputStream(new File("testCrossStreams.txt"));
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out2);
        byte[] buffer2 = out2.toByteArray();
        ByteBuffer byteDataBuffer2 = ByteBuffer.wrap(buffer2);
        assertEquals(1024, byteDataBuffer2.limit());
        in.close();
        out2.close();

        in = new FileInputStream(new File("testCrossStreams.txt"));
        ByteArrayOutputStream out3 = new ByteArrayOutputStream(2048);
        DataUtil.crossStreams(in, out3);
        byte[] buffer3 = out3.toByteArray();
        ByteBuffer byteDataBuffer3 = ByteBuffer.wrap(buffer3);
        assertEquals(2048, byteDataBuffer3.limit());
        in.close();
        out3.close();

        in = new FileInputStream(new File("testCrossStreams.txt"));
        ByteArrayOutputStream out4 = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out4);
        byte[] buffer4 = out4.toByteArray();
        ByteBuffer byteDataBuffer4 = ByteBuffer.wrap(buffer4);
        assertEquals(1024, byteDataBuffer4.limit());
        in.close();
        out4.close();

        in = new FileInputStream(new File("testCrossStreams.txt"));
        ByteArrayOutputStream out5 = new ByteArrayOutputStream(4096);
        DataUtil.crossStreams(in, out5);
        byte[] buffer5 = out5.toByteArray();
        ByteBuffer byteDataBuffer5 = ByteBuffer.wrap(buffer5);
        assertEquals(4096, byteDataBuffer5.limit());
        in.close();
        out5.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Test empty input stream
        InputStream inEmpty = new ByteArrayInputStream(new byte[0][0]);
        ByteBuffer byteDataEmpty = DataUtil.readToByteBuffer(inEmpty);
        assertEquals(0, byteDataEmpty.limit());

        // Test valid input stream
        InputStream inValid = new ByteArrayInputStream("Hello World!".getBytes(Charset.forName(charsetName)));
        ByteBuffer byteDataValid = DataUtil.readToByteBuffer(inValid);
        assertEquals(13, byteDataValid.limit());
        inValid.close();

        // Test large input stream
        InputStream inLarge = new ByteArrayInputStream("Hello World!".getBytes(Charset.forName(charsetName)), 0, 1024 * 1024);
        ByteBuffer byteDataLarge = DataUtil.readToByteBuffer(inLarge);
        assertEquals(1024 * 1024, byteDataLarge.limit());
        inLarge.close();

        // Test invalid input stream
        InputStream inNull = null;
        try {
            ByteBuffer byteDataNull = DataUtil.readToByteBuffer(inNull);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        assertNull(byteDataNull);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        // Test empty file
        File fileEmpty = new File("");
        ByteBuffer byteDataEmpty = DataUtil.readFileToByteBuffer(fileEmpty);
        assertEquals(0, byteDataEmpty.limit());

        // Test valid file
        String fileName = "testFile.txt";
        File fileValid = new File(fileName);
        String expectedContent = "Hello World!";
        FileWriter fileWriter = new FileWriter(fileValid);
        fileWriter.write(expectedContent);
        fileWriter.close();
        ByteBuffer byteDataValid = DataUtil.readFileToByteBuffer(fileValid);
        assertEquals(expectedContent.length(), byteDataValid.limit());
        fileValid.delete();

        // Test invalid file
        File fileInvalid = new File("invalidFile.txt");
        ByteBuffer byteDataInvalid = DataUtil.readFileToByteBuffer(fileInvalid);
        assertEquals(0, byteDataInvalid.limit());
    }
}