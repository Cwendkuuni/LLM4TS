package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class DataUtilTest {
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "https://www.example.com/";
    private static final String TEST_FILE_PATH = "src/test/resources/test.html";

    private DataUtil dataUtil;

    @BeforeEach
    void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    void load_file_charsetName_baseUri() {
        File file = new File(TEST_FILE_PATH);

        try {
            Document document = dataUtil.load(file, CHARSET_NAME, BASE_URI);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void load_inputStream_charsetName_baseUri() {
        try {
            FileInputStream inputStream = new FileInputStream(TEST_FILE_PATH);
            Document document = dataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
            assertNotNull(document);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void load_inputStream_charsetName_baseUri_parser() {
        try {
            FileInputStream inputStream = new FileInputStream(TEST_FILE_PATH);
            Document document = dataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
            assertNotNull(document);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void parseByteData_byteData_charsetName_baseUri_parser() {
        String htmlData = "<html><head><title>Test HTML</title></head><body><h1>Hello, world!</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlData.getBytes());

        Document document = dataUtil.parseByteData(byteBuffer, CHARSET_NAME, BASE_URI, Parser.htmlParser());

        assertNotNull(document);
        assertEquals("Test HTML", document.title());
        assertEquals("Hello, world!", document.selectFirst("h1").text());
    }

    @Test
    void readToByteBuffer_inStream_maxSize() {
        try {
            FileInputStream inputStream = new FileInputStream(TEST_FILE_PATH);
            ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream, 10000);
            assertNotNull(byteBuffer);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readToByteBuffer_inStream() {
        try {
            FileInputStream inputStream = new FileInputStream(TEST_FILE_PATH);
            ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteBuffer);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getCharsetFromContentType_contentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}