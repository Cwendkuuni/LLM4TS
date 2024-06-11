package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.helper.DataUtil;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        String expectedCharset = "UTF-8";

        File tempFile = new File(new TemporaryFolder().newFile("testFile").getAbsolutePath());
        FileWriter fileWriter = new FileWriter(tempFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document doc = DataUtil.load(tempFile, null, "");
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc);

        DataUtil.load(tempFile, null, "");
        File.delete(tempFile.getAbsolutePath());
    }

    @Test
    public void testLoadStreamWithDefaultCharset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("<html><head><meta charset=\"UTF-8\"></head><body></body></html>".getBytes());
        outputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, null, "");
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc);
    }

    @Test
    public void testLoadStreamWithSpecifiedCharset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("<html><head><meta charset=\"GBK\"></head><body></body></html>".getBytes());
        outputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, "GBK", "");
        assertEquals("GBK", doc.outputSettings().charset());
        assertNotNull(doc);
    }

    @Test
    public void testLoadStreamWithSpecifiedParser() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("<html><head><meta charset=\"UTF-8\"></head><body></body></html>".getBytes());
        outputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, "UTF-8", "", DataUtil.Parser.xmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadNullFile() throws IOException {
        Document doc = DataUtil.load(null, null, "");
        assertNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta charset=\"UTF-8\"></head><body></body></html>".getBytes());
        String expectedCharset = "UTF-8";

        Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc);

        byteData.rewind();
        doc = DataUtil.parseByteData(byteData, "GBK", "", Parser.htmlParser());
        assertEquals("GBK", doc.outputSettings().charset());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><head><meta charset=\"UTF-8\"></head><body></body></html>".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertArrayEquals("<html><head><meta charset=\"UTF-8\"></head><body></body></html>".getBytes(), byteData.array());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        assertNotNull(DataUtil.getCharsetFromContentType(contentType));
        assertEquals(expectedCharset, DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}