package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataUtilTest {
    
    private static File testFile;
    private static InputStream testInputStream;
    private static String baseUri = "https://www.example.com";
    
    @BeforeAll
    public static void setup() {
        testFile = new File("testFile.html");
        
        try {
            FileOutputStream fos = new FileOutputStream(testFile);
            byte[] fileContent = "<html><body><h1>Test File</h1></body></html>".getBytes(StandardCharsets.UTF_8);
            fos.write(fileContent);
            fos.close();
            
            testInputStream = new ByteArrayInputStream(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadFromFile_ValidFile_ReturnsDocument() {
        try {
            Document doc = DataUtil.load(testFile, "UTF-8", baseUri);
            assertEquals("Test File", doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadFromFile_InvalidFile_ThrowsIOException() {
        assertThrows(IOException.class, () -> {
            DataUtil.load(new File("invalidFile"), "UTF-8", baseUri);
        });
    }
    
    @Test
    public void testLoadFromInputStream_ValidInputStream_ReturnsDocument() {
        try {
            Document doc = DataUtil.load(testInputStream, "UTF-8", baseUri);
            assertEquals("Test File", doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadFromInputStream_InvalidInputStream_ThrowsIOException() {
        assertThrows(IOException.class, () -> {
            DataUtil.load(new FileInputStream("invalidFile"), "UTF-8", baseUri);
        });
    }
    
    @Test
    public void testLoadFromInputStream_CustomParser_ReturnsDocument() {
        try {
            Parser parser = Parser.xmlParser();
            Document doc = DataUtil.load(testInputStream, "UTF-8", baseUri, parser);
            assertEquals("Test File", doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadFromInputStream_CustomParser_InvalidInputStream_ThrowsIOException() {
        assertThrows(IOException.class, () -> {
            Parser parser = Parser.htmlParser();
            DataUtil.load(new FileInputStream("invalidFile"), "UTF-8", baseUri, parser);
        });
    }
    
    @Test
    public void testParseByteData_ValidByteData_ReturnsDocument() {
        try {
            byte[] byteData = "<html><body><h1>Test File</h1></body></html>".getBytes(StandardCharsets.UTF_8);
            Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), "UTF-8", baseUri, Parser.htmlParser());
            assertEquals("Test File", doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testParseByteData_NullCharsetName_ReturnsDocument() {
        try {
            byte[] byteData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test File</h1></body></html>".getBytes(StandardCharsets.UTF_8);
            Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), null, baseUri, Parser.htmlParser());
            assertEquals("Test File", doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testParseByteData_NotFoundCharsetName_ReturnsDocument() {
        try {
            byte[] byteData = "<html><head><meta charset=\"ISO-8859-1\"></head><body><h1>Test File</h1></body></html>".getBytes(StandardCharsets.UTF_8);
            Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), null, baseUri, Parser.htmlParser());
            assertEquals("Test File", doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testParseByteData_InvalidCharsetName_ReturnsDocument() {
        try {
            byte[] byteData = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test File</h1></body></html>".getBytes(StandardCharsets.UTF_8);
            assertThrows(IllegalArgumentException.class, () -> {
                DataUtil.parseByteData(ByteBuffer.wrap(byteData), "InvalidCharset", baseUri, Parser.htmlParser());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadToByteBuffer_ValidInputStream_ReturnsByteBuffer() {
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);
            byte[] byteData = new byte[byteBuffer.remaining()];
            byteBuffer.get(byteData);
            assertEquals("<html><body><h1>Test File</h1></body></html>", new String(byteData, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetCharsetFromContentType_ValidContentType_ReturnsCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_InvalidContentType_ReturnsNull() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }
    
}