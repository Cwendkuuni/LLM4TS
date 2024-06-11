package org.jsoup.helper;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        File file = new File("tempFile.html");
        FileUtils.writeStringToFile(file, fileContent, StandardCharsets.UTF_8);

        Document document = DataUtil.load(file, "ISO-8859-1", null);
        assertEquals("Hello World!", document.body().text());
        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null);
        assertEquals("Hello World!", document.body().text());
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-16", null);
        assertEquals("", document.body().text());

        document = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("Hello World!", document.body().text());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("src/test/resources/DataUtilTest.html"));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("Hello World!", document.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("src/test/resources/DataUtilTest.html"));
        Document document = DataUtil.parseByteData(byteData, "UTF-16", null, Parser.htmlParser());
        assertEquals("", document.body().text());

        document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals("Hello World!", document.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Hello World!";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals(inputContent, outputStream.toString(StandardCharsets.UTF_8));
    }
}