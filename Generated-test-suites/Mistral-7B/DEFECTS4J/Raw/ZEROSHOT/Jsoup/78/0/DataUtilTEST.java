package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DataUtil.class)
public class DataUtilTest {

    private static final String TEST_URL = "test.html";
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String TEST_BASE_URI = "http://example.com";

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("src/test/resources/test.html");
        Document document = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
        assertNotNull(document);
        assertEquals(TEST_BASE_URI, document.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testHtml = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";
        InputStream testInputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI);
        assertNotNull(document);
        assertEquals(TEST_BASE_URI, document.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParseError() throws IOException {
        String testHtml = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";
        InputStream testInputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        try {
            DataUtil.load(testInputStream, null, TEST_BASE_URI);
            fail();
        } catch (AssertionError e) {
            // expected
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testHtml = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";
        InputStream testInputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        Parser parser = Mockito.mock(Parser.class);
        PowerMockito.when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(createTestDocument());
        Document document = DataUtil.parseInputStream(testInputStream, TEST_CHARSET, TEST_BASE_URI, parser);
        assertNotNull(document);
        assertEquals(TEST_BASE_URI, document.baseUri());
        PowerMockito.verifyStatic();
        parser.parseInput(testHtml, TEST_BASE_URI);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testHtml = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test</p>\n" +
                "</body>\n" +
                "</html>";
        InputStream testInputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(testInputStream, outputStream);
        assertEquals(testHtml, outputStream.toString(StandardCharsets.UTF_8.name()));
    }

    private Document createTestDocument() {
        Document document = new Document();
        Element body = document.createElement("body");
        Element p = document.createElement("p");
        p.setText("Test");
        body.appendChild(p);
        document.appendChild(body);
        return document;
    }
}