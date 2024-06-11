package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>";

        File testFile = new File("test.html");
        Files.write(testFile.toPath(), fileContent.getBytes());

        Document expectedDoc = Parser.htmlParser().parseInput(fileContent, null);

        Document actualDoc = DataUtil.load(testFile, "UTF-8", null);

        assertEquals(expectedDoc, actualDoc);

        // Clean up
        testFile.delete();
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document expectedDoc = Parser.htmlParser().parseInput(inputStreamContent, null);

        Document actualDoc = DataUtil.load(inputStream, null, null);

        assertEquals(expectedDoc, actualDoc);

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document expectedDoc = Parser.htmlParser().parseInput(inputStreamContent, null);

        Document actualDoc = DataUtil.load(inputStream, "ISO-8859-1", null);

        assertEquals(expectedDoc, actualDoc);

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"X-PASCAL\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>";

        File testFile = new File("test.html");
        Files.write(testFile.toPath(), fileContent.getBytes());

        Document actualDoc = null;

        try {
            actualDoc = DataUtil.load(testFile, "X-PASCAL", null);
        } finally {
            testFile.delete();
        }

        fail("Expected IOException when loading file with unsupported charset");
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.wrap(inputStreamContent.getBytes());

        Document expectedDoc = Parser.htmlParser().parseInput(inputStreamContent, null);

        Document actualDoc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());

        assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.wrap(inputStreamContent.getBytes());

        Document expectedDoc = Parser.htmlParser().parseInput(inputStreamContent, null);

        Document actualDoc = DataUtil.parseByteData(byteData, "ISO-8859-1", null, Parser.htmlParser());

        assertEquals(expectedDoc, actualDoc);
    }
}