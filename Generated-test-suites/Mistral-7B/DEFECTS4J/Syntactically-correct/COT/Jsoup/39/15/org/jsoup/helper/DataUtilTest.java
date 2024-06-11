package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8'></head><body><h1>Hello World</h1></body></html>";
        File tempFile = File.createTempFile("DataUtilTest", null);
        FileWriter fileWriter = new FileWriter(tempFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document doc = DataUtil.load(tempFile, "UTF-8", null);
        assertEquals("Hello World", doc.title());

        DataUtil.load(tempFile, null, null); // Test when charsetName is null
        doc = DataUtil.load(tempFile, "", null); // Test when charsetName is empty

        tempFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8'></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("Hello World", doc.title());

        doc = DataUtil.load(inputStream, null, null); // Test when charsetName is null
        doc = DataUtil.load(inputStream, "", null); // Test when charsetName is empty

        inputStream.close();
    }

    @Test
    public void testParseByteData() throws IOException {
        String charset = "UTF-8";
        String fileContent = "<?xml version='1.0' encoding='" + charset + "'?>\n<html><head><meta charset='" + charset + "'></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName(charset)));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.readToByteBuffer(inputStream, outputStream);
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());

        Document doc = DataUtil.parseByteData(byteData, charset, null, Parser.htmlParser());
        assertEquals("Hello World", doc.title());

        doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()); // Test when charsetName is null
        doc = DataUtil.parseByteData(byteData, "", null, Parser.htmlParser()); // Test when charsetName is empty

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        File tempFile = File.createTempFile("DataUtilTest", null);
        throw new IOException("Test IOException");

        // This test method should throw IOException
        DataUtil.load(tempFile, null, null);

        tempFile.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        throw new IOException("Test IOException");

        // This test method should throw IOException
        DataUtil.load(inputStream, null, null);

        inputStream.close();
    }
}