package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Parser.HtmlParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head><body>Hello World</body></html>";
        File testFile = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document expectedDoc = HtmlParser.parseInput(fileContent, "");

        Document doc = DataUtil.load(testFile, "ISO-8859-1", "");
        assertEquals(expectedDoc, doc);

        testFile.delete();
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head><body>Hello World</body></html>";
        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document expectedDoc = HtmlParser.parseInput(fileContent, "");

        Document doc = DataUtil.load(testInputStream, "ISO-8859-1", "");
        assertEquals(expectedDoc, doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head><body>Hello World</body></html>";
        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes());

        Document expectedDoc = HtmlParser.parseInput(fileContent, "");

        Document doc = DataUtil.load(testInputStream, null, "");
        assertEquals(expectedDoc, doc);
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head><body>Hello World</body></html>";
        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes());
        Parser customParser = mock(HtmlParser.class);

        Document expectedDoc = HtmlParser.parseInput(fileContent, "");

        when(customParser.parseInput(anyString(), eq(""))).thenReturn(expectedDoc);

        Document doc = DataUtil.load(testInputStream, null, "", customParser);
        assertEquals(expectedDoc, doc);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head><body>Hello World</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes());

        Document expectedDoc = HtmlParser.parseInput(fileContent, "");

        Document doc = DataUtil.parseByteData(byteData, null, "", HtmlParser.htmlParser());
        assertEquals(expectedDoc, doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head><body>Hello World</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes());

        Document expectedDoc = HtmlParser.parseInput(fileContent, "");

        Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", "", HtmlParser.htmlParser());
        assertEquals(expectedDoc, doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharsetAndCustomParser() {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head><body>Hello World</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes());
        Parser customParser = mock(HtmlParser.class);

        Document expectedDoc = HtmlParser.parseInput(fileContent, "");

        when(customParser.parseInput(anyString(), eq(""))).thenReturn(expectedDoc);

        Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", "", customParser);
        assertEquals(expectedDoc, doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        ByteBuffer expectedByteData = ByteBuffer.wrap(fileContent.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(expectedByteData, byteData);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

        ByteBuffer expectedByteData = ByteBuffer.wrap(fileContent.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 1024);
        assertEquals(expectedByteData.slice(0, 1024), byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";
        String expectedCharset = "ISO-8859-1";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNullContentType() {
        String contentType = null;
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testLoadDocumentWithNullInputStream() throws IOException {
        InputStream inputStream = null;
        assertNull(DataUtil.load(inputStream, null, ""));
    }

    @Test
    public void testParseByteDataWithNullByteData() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        assertNull(DataUtil.parseByteData(byteData, null, "", HtmlParser.htmlParser()));
    }
}