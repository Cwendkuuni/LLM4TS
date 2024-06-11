package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private DataUtil dataUtil;
    private Parser parser;

    @Before
    public void setUp() throws Exception {
        parser = mock(Parser.class);
        dataUtil = new DataUtil();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        File file = new File("testFile.html");
        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        when(parser.parseInput(anyString(), anyString())).thenReturn(createDocument("testFile.html"));

        Document document = dataUtil.load(file, "UTF-8", null);

        assertEquals("testFile.html", document.title());
        assertEquals(fileContent, document.html());

        fileInputStream.close();
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        File file = new File("testFile.html");

        when(parser.parseInput(anyString(), anyString())).thenReturn(createDocument("testFile.html"));

        Document document = dataUtil.load(file, null, null);

        assertEquals("testFile.html", document.title());
        assertEquals(fileContent, document.html());

        file.delete();
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        File file = new File("testFile.html");

        when(parser.parseInput(anyString(), anyString())).thenReturn(createDocument("testFile.html"));

        try {
            dataUtil.load(file, "GBK", null);
            fail();
        } catch (IOException e) {
            // expected
        }

        file.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        when(parser.parseInput(anyString(), anyString())).thenReturn(createDocument("testInputStream"));

        Document document = dataUtil.load(inputStream, "UTF-8", null);

        assertEquals("testInputStream", document.title());
        assertEquals(inputStreamContent, document.html());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        when(parser.parseInput(anyString(), anyString())).thenReturn(createDocument("testInputStream"));

        Document document = dataUtil.load(inputStream, null, null);

        assertEquals("testInputStream", document.title());
        assertEquals(inputStreamContent, document.html());

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        try {
            dataUtil.load(inputStream, "GBK", null);
            fail();
        } catch (IOException e) {
            // expected
        }

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Parser parserHtml = mock(Parser.Parser.class);
        when(parser.parser(Parser.htmlParser())).thenReturn(parserHtml);

        Document document = dataUtil.load(inputStream, "UTF-8", null, parserHtml);

        assertEquals("", document.title());
        assertEquals(inputStreamContent, document.html());

        inputStream.close();
    }

    @Test
    public void testParseByteData() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(fileContent.getBytes("UTF-8"));

        Document document = dataUtil.parseByteData(byteBuffer, "UTF-8", null, parser);

        assertEquals("", document.title());
        assertEquals(fileContent, document.html());

        byteBuffer.rewind();
        byteBuffer.position(byteBuffer.limit());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(fileContent.getBytes("UTF-8"));

        Document document = dataUtil.parseByteData(byteBuffer, null, null, parser);

        assertEquals("", document.title());
        assertEquals(fileContent, document.html());

        byteBuffer.rewind();
        byteBuffer.position(byteBuffer.limit());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(fileContent.getBytes("UTF-8"));

        try {
            dataUtil.parseByteData(byteBuffer, "GBK", null, parser);
            fail();
        } catch (IOException e) {
            // expected
        }

        byteBuffer.rewind();
        byteBuffer.position(byteBuffer.limit());
    }

    private Document createDocument(String documentName) {
        return parser.parseInput("<html><head><title>" + documentName + "</title></head><body></body></html>", null);
    }
}