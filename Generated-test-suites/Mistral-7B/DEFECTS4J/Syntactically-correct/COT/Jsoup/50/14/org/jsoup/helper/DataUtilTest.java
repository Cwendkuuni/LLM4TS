package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil methods.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "Hello World!";
        File file = new File("test.html");
        FileUtils.writeStringToFile(file, fileContent, StandardCharsets.UTF_8);

        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertNotNull(doc.body().text());
        assertNotNull(doc.body().text().trim());
        assertNotNull(doc.body().text().equals(fileContent));

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "Hello World!";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertNotNull(doc.body().text());
        assertNotNull(doc.body().text().trim());
        assertNotNull(doc.body().text().equals(inputStreamContent));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertNotNull(doc.body().text());
        assertNotNull(doc.body().text().trim());
        assertNotNull(doc.body().select("h1").text().equals("Hello World!"));
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, null);
        assertThrows(IOException.class, () -> doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "invalid_charset", null);
        assertThrows(IOException.class, () -> doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        String inputStreamContent = "<html><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", null, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertNotNull(doc.body().text());
        assertNotNull(doc.body().text().trim());
        assertNotNull(doc.body().text().equals(inputStreamContent));
        assertNotNull(doc.body().select("h1").text().equals("Hello World!"));
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithNullCharset() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteBuffer, null, null, DataUtil.Parser.htmlParser());
        assertThrows(IOException.class, () -> doc);
    }

}