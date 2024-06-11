package org.jsoup.helper;

import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.helper.DataUtil.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String fileContent = "This is a test.";
        File file = new File("test.html");
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();

        Document doc = load(file, "UTF-8", "/");
        assertEquals("This is a test.", doc.body().text());

        file.delete();
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "This is a test.";
        File file = new File("test.html");
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();

        try {
            Document doc = load(file, "invalidCharset", "/");
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        file.delete();
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String content = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        Document doc = load(inputStream, "UTF-8", "/");
        assertEquals("This is a test.", doc.body().text());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() {
        String content = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        try {
            Document doc = load(inputStream, "invalidCharset", "/");
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        String content = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        Document doc = load(inputStream, null, "/");
        assertEquals("This is a test.", doc.body().text());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String content = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";

        Document doc = parseInputStream(inputStream, charset, "/", Parser.htmlParser());
        assertEquals("This is a test.", doc.body().text());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String content = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        String charset = "invalidCharset";

        try {
            Document doc = parseInputStream(inputStream, charset, "/", Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source data.";
        String target = "Target data.";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        crossStreams(inputStream, outputStream);
        inputStream.close();

        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(source, new String(outputBytes, StandardCharsets.UTF_8));

        outputStream.close();
    }
}