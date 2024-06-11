package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File testFile = new File("test.html");
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://example.com";
        Document expectedDocument = createTestDocument();

        Document actualDocument = DataUtil.load(testFile, expectedCharset, expectedBaseUri);

        assertNotNull(actualDocument);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String testData = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'><body>Test</body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testData.getBytes());
        String expectedBaseUri = "http://example.com";
        Document expectedDocument = createTestDocument();

        Document actualDocument = DataUtil.load(testInputStream, null, expectedBaseUri);

        assertNotNull(actualDocument);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'><body>Test</body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testData.getBytes());
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://example.com";
        Document expectedDocument = createTestDocument();

        Document actualDocument = DataUtil.load(testInputStream, expectedCharset, expectedBaseUri);

        assertNotNull(actualDocument);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFromInputStreamWithCustomParser() throws IOException {
        String testData = "<html xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xhtml='http://www.w3.org/1999/xhtml'><head><meta http-equiv='Content-Type' content='application/xml' /><meta charset='UTF-8' /><title>Test</title></head><body><p>Test</p></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testData.getBytes());
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://example.com";
        Parser parser = Mockito.mock(Parser.class);
        Document expectedDocument = createTestDocument();

        Mockito.when(parser.parseInput(Mockito.anyString(), expectedBaseUri)).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(testInputStream, expectedCharset, expectedBaseUri, parser);

        assertNotNull(actualDocument);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadFromInvalidFile() throws IOException {
        File testFile = new File("non-existent-file.html");
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://example.com";

        DataUtil.load(testFile, expectedCharset, expectedBaseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamWithBOM() throws IOException {
        String testData = "\uFEFF<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'><body>Test</body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testData.getBytes());
        String expectedCharset = "UTF-8";
        String expectedBaseUri = "http://example.com";

        Document expectedDocument = createTestDocument();

        Document actualDocument = DataUtil.load(testInputStream, expectedCharset, expectedBaseUri);

        assertNotNull(actualDocument);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String testData = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'><body>Test</body></html>";
        ByteBuffer byteData = DataUtil.parseByteData(createByteBufferFromString(testData), null, "", Parser.htmlParser());
        String expectedCharset = "ISO-8859-1";
        Document expectedDocument = createTestDocument();

        Document actualDocument = parseByteDataToDocument(byteData);

        assertNotNull(actualDocument);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String testData = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'><body>Test</body></html>";
        String expectedCharset = "UTF-8";
        ByteBuffer byteData = DataUtil.parseByteData(createByteBufferFromString(testData), expectedCharset, "", Parser.htmlParser());
        Document expectedDocument = createTestDocument();

        Document actualDocument = parseByteDataToDocument(byteData);

        assertNotNull(actualDocument);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithCustomParser() {
        String testData = "<html xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xhtml='http://www.w3.org/1999/xhtml'><head><meta http-equiv='Content-Type' content='application/xml' /><meta charset='UTF-8' /><title>Test</title></head><body><p>Test</p></body></html>";
        String expectedCharset = "UTF-8";
        ByteBuffer byteData = DataUtil.parseByteData(createByteBufferFromString(testData), expectedCharset, "", Parser.xmlParser());
        Document expectedDocument = createTestDocument();

        Document actualDocument = parseByteDataToDocument(byteData);

        assertNotNull(actualDocument);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        byte[] testData = new byte[200];
        InputStream testInputStream = new ByteArrayInputStream(testData);
        int maxSize = 100;

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(testInputStream, maxSize);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(testData, 0, maxSize);

        assertNotNull(actualByteBuffer);
        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        byte[] testData = new byte[200];
        InputStream testInputStream = new ByteArrayInputStream(testData);

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(testInputStream);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(testData);

        assertNotNull(actualByteBuffer);
        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testGetCharsetFromContentTypeWithSupportedCharset() {
        String testContentType = "text/html; charset=UTF-8";

        String actualCharset = DataUtil.getCharsetFromContentType(testContentType);

        assertEquals("UTF-8", actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String testContentType = "text/html; charset=EUC-KR";

        String actualCharset = DataUtil.getCharsetFromContentType(testContentType);

        assertEquals(null, actualCharset);
    }

    private Document createTestDocument() {
        return Parser.htmlParser().parse("<html><body><p>Test</p></body></html>");
    }

    private ByteBuffer createByteBufferFromString(String data) {
        return ByteBuffer.wrap(data.getBytes());
    }

    private Document parseByteDataToDocument(ByteBuffer byteData) {
        return Parser.htmlParser().parseInput(byteData.asString(), "");
    }
}