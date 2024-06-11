package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        file.createNewFile();
        Path filePath = Paths.get("test.html");
        byte[] testData = "<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>".getBytes(Charset.forName("UTF-8"));
        Files.write(filePath, testData);

        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
        assertEquals("Test Document", doc.title());
        assertEquals("Hello World!", doc.select("h1").text());

        file.delete();
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        byte[] testData = "<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>".getBytes(Charset.forName("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(testData);

        Document doc1 = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc1);
        assertEquals("Test Document", doc1.title());
        assertEquals("Hello World!", doc1.select("h1").text());

        inputStream.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        byte[] testData = "<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>".getBytes(Charset.forName("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(testData);

        Document doc1 = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc1);
        assertEquals("Test Document", doc1.title());
        assertEquals("Hello World!", doc1.select("h1").text());

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        byte[] testData = "<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>".getBytes(Charset.forName("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(testData);
        byte[] outputData = new byte[1024];
        InputStream inStream = new ByteArrayInputStream(outputData);

        DataUtil.crossStreams(inputStream, inStream);

        int len = 0;
        while ((len = inStream.read(outputData)) != -1) {
            // Do something with the outputData
        }

        inputStream.close();
        inStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] testData = "<html><head><title>Test Document</title></head><body><h1>Hello World!</h1></body></html>".getBytes(Charset.forName("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(testData);

        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(buffer);
        assertEquals(66, buffer.limit());

        inputStream.close();
    }
}