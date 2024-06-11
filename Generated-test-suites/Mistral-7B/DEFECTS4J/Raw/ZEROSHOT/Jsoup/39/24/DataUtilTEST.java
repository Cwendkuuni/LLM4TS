package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document document = DataUtil.load(testFile, "UTF-8", "/");
        assertEquals("This is a test file.", document.body().text());

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testString = "This is a test input stream.";
        InputStream testInput = new ByteArrayInputStream(testString.getBytes("UTF-8"));

        Document document = DataUtil.load(testInput, "UTF-8", "/");
        assertEquals(testString, document.body().text());
    }

    @Test
    public void testLoadInputStreamWithCharsetDetection() throws IOException {
        String testString1 = "This is a test input stream with charset=UTF-8.";
        String testString2 = "This is a test input stream with charset=ISO-8859-1.";

        ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
        byteArrayOutputStream1.write(testString1.getBytes("UTF-8"));
        InputStream testInput1 = new ByteArrayInputStream(byteArrayOutputStream1.toByteArray());

        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        byteArrayOutputStream2.write(testString2.getBytes("ISO-8859-1"));
        InputStream testInput2 = new ByteArrayInputStream(byteArrayOutputStream2.toByteArray());

        Document document1 = DataUtil.load(testInput1, null, "/");
        assertEquals(testString1, document1.body().text());

        Document document2 = DataUtil.load(testInput2, null, "/");
        assertEquals(testString2, document2.body().text());

        String charset1 = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset1);

        String charset2 = DataUtil.getCharsetFromContentType("text/html; charset=ISO-8859-1");
        assertEquals("ISO-8859-1", charset2);
    }

    @Test
    public void testParseByteData() throws IOException {
        String testString = "This is a test DataUtil.parseByteData.";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes("UTF-8"));

        Document document = DataUtil.parseByteData(byteBuffer, null, "/", Parser.htmlParser());
        assertEquals(testString, document.body().text());

        byteBuffer.rewind();

        String charset = "ISO-8859-1";
        String newTestString = "This is a test DataUtil.parseByteData with charset=" + charset + ".";
        byteBuffer.put(newTestString.getBytes(charset));
        byteBuffer.flip();

        document = DataUtil.parseByteData(byteBuffer, charset, "/", Parser.htmlParser());
        assertEquals(newTestString, document.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "This is a test readToByteBuffer.";
        InputStream testInputStream = new ByteArrayInputStream(testString.getBytes("UTF-8"));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);
        assertEquals(testString, Charset.forName("UTF-8").decode(byteBuffer).toString());

        byteBuffer = DataUtil.readToByteBuffer(testInputStream, 0);
        assertEquals(testString, Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html; charset=ISO-8859-1";

        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        assertEquals("UTF-8", charset1);

        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertEquals("ISO-8859-1", charset2);
    }
}