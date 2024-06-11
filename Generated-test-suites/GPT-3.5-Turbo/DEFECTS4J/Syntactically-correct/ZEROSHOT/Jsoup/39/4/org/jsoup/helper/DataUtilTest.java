package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class DataUtilTest {

    private Document document;

    @Before
    public void setUp() throws Exception {
        document = Jsoup.parse("<html><head><title>Test Document</title></head><body><p>Hello, World!</p></body></html>");
    }

    @Test
    public void testLoadFile() {
        File file = new File("testFile.html");
        try {
            Document loadedDocument = DataUtil.load(file, "UTF-8", "");
            assertNotNull(loadedDocument);
            assertEquals(document.html(), loadedDocument.html());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            Document loadedDocument = DataUtil.load(Jsoup.parse("<html></html>").body().byteInputStream(),"UTF-8", "");
            assertNotNull(loadedDocument);
            assertEquals(document.html(), loadedDocument.html());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            Document loadedDocument = DataUtil.load(Jsoup.parse("<html></html>").body().byteInputStream(),"UTF-8", "", Parser.htmlParser());
            assertNotNull(loadedDocument);
            assertEquals(document.html(), loadedDocument.html());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteData() {
        Document parsedDocument = DataUtil.parseByteData(document.outerHtml().getBytes().toString(), "UTF-8", "", Parser.htmlParser());
        assertNotNull(parsedDocument);
        assertEquals(document.html(), parsedDocument.html());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        try {
            File file = new File("testFile.html");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(file), 1024);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            File file = new File("testFile.html");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(file));
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html;charset=UTF-8");
        assertEquals("UTF-8", charset);
    }
}