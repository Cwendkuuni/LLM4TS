import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "This is a test.";
        File testFile = new File("testFile.html");
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getProperty("user.dir")).thenReturn("src/test/resources");
        PowerMockito.when(System.getProperty("file.encoding")).thenReturn("UTF-8");

        Document document = DataUtil.load(testFile, null);
        assertEquals("src/test/resources/testFile.html", document.baseUri());
        assertEquals(fileContent, document.body().text());
        PowerMockito.verifyStatic();
        Document expectedDocument = Mockito.mock(Document.class);
        DataUtil.parseInputStream(PowerMockito.mockInputOutput(new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")))), "UTF-8", "src/test/resources/testFile.html", Parser.htmlParser());
        PowerMockito.verifyStatic().when(Parser.htmlParser()).parseInput(expectedDocument, "src/test/resources/testFile.html");
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String fileContent = "This is a test.";
        File testFile = new File("testFile.html");
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getProperty("user.dir")).thenReturn("src/test/resources");
        PowerMockito.when(System.getProperty("file.encoding")).thenReturn("UTF-8");

        Document document = DataUtil.load(testFile, "ISO-8859-1");
        assertEquals("src/test/resources/testFile.html", document.baseUri());
        assertEquals(fileContent, document.body().text());
        PowerMockito.verifyStatic();
        Document expectedDocument = Mockito.mock(Document.class);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(outputStream.toByteArray());
        PowerMockito.when(System.getProperty("file.encoding")).thenReturn("ISO-8859-1");
        DataUtil.parseInputStream(PowerMockito.mockInputOutput(byteBuffer), "ISO-8859-1", "src/test/resources/testFile.html", Parser.htmlParser());
        PowerMockito.verifyStatic().when(Parser.htmlParser()).parseInput(expectedDocument, "src/test/resources/testFile.html");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "This is a test.";
        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.load(testInputStream, null);
        assertEquals(fileContent, document.body().text());
        PowerMockito.verifyStatic();
        Document expectedDocument = Mockito.mock(Document.class);
        DataUtil.parseInputStream(testInputStream, "UTF-8", null, Parser.htmlParser());
        PowerMockito.verifyStatic().when(Parser.htmlParser()).parseInput(expectedDocument, null);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String fileContent = "This is a test.";
        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.load(testInputStream, "ISO-8859-1");
        assertEquals(fileContent, document.body().text());
        PowerMockito.verifyStatic();
        Document expectedDocument = Mockito.mock(Document.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(testInputStream, outputStream);
        testInputStream.close();
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(outputStream.toByteArray());
        PowerMockito.when(System.getProperty("file.encoding")).thenReturn("ISO-8859-1");
        DataUtil.parseInputStream(PowerMockito.mockInputOutput(byteBuffer), "ISO-8859-1", null, Parser.htmlParser());
        PowerMockito.verifyStatic().when(Parser.htmlParser()).parseInput(expectedDocument, null);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<root>\n<element>Test</element>\n</root>";
        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));

        Document document = DataUtil.load(testInputStream, "UTF-8", null, Parser.xmlParser());
        assertEquals("<root><element>Test</element></root>", document.body().html());
        PowerMockito.verifyStatic();
        Document expectedDocument = Mockito.mock(Document.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(testInputStream, outputStream);
        testInputStream.close();
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(outputStream.toByteArray());
        DataUtil.parseInputStream(PowerMockito.mockInputOutput(byteBuffer), "UTF-8", null, Parser.xmlParser());
        PowerMockito.verifyStatic().when(Parser.xmlParser()).parseInput(expectedDocument, null);
    }

    @Test
    public void testParseInputStreamWithEmptyFile() throws IOException {
        InputStream testInputStream = Mockito.mock(InputStream.class);
        Mockito.when(testInputStream.read()).thenReturn(-1);

        Document document = DataUtil.parseInputStream(testInputStream, "UTF-8", null, Parser.htmlParser());
        PowerMockito.verifyStatic().when(Parser.htmlParser()).parseInput(Mockito.eq(document), Mockito.anyString());
        assertEquals(null, document);
    }

    @Test
    public void testReadToByteBufferWithFile() throws IOException {
        File testFile = new File("testFile.html");
        ByteBuffer expectedByteBuffer = DataUtil.readToByteBuffer(testFile);

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(testFile);
        assertSame(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testReadToByteBufferWithStream() throws IOException {
        String fileContent = "This is a test.";
        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));
        ByteBuffer expectedByteBuffer = DataUtil.readToByteBuffer(testInputStream);

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(testInputStream);
        assertSame(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testReadFileToByteBufferWithFile() throws IOException {
        File testFile = new File("testFile.html");
        ByteBuffer expectedByteBuffer = DataUtil.readFileToByteBuffer(testFile);

        ByteBuffer actualByteBuffer = DataUtil.readFileToByteBuffer(testFile);
        assertSame(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer expectedByteBuffer = DataUtil.emptyByteBuffer();
        ByteBuffer actualByteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "mime_boundary_1234567890";
        String actualMimeBoundary = DataUtil.mimeBoundary();

        assertEquals(expectedMimeBoundary, actualMimeBoundary);
    }

    @Test
    public void testGetCharsetFromContentTypeWithValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/html";
        String expectedCharset = null;

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testValidateCharsetWithValidCharset() {
        String charset = "UTF-8";
        String expectedCharset = "UTF-8";

        String actualCharset = DataUtil.validateCharset(charset);
        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "Test";
        String expectedCharset = null;

        String actualCharset = DataUtil.validateCharset(charset);
        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String fileContent = "This is a test.";
        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));
        String invalidCharset = "InvalidCharset";

        Document document = DataUtil.parseInputStream(testInputStream, invalidCharset, null, Parser.htmlParser());
        assertEquals(null, document);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF8Bom() throws IOException {
        String bom = "\uFEFFThis is a test.";
        ByteBuffer byteData = DataUtil.readToByteBuffer(bom.getBytes(Charset.forName("UTF-8")));

        BomCharset actualBomCharset = DataUtil.detectCharsetFromBom(byteData);
        BomCharset expectedBomCharset = new BomCharset("UTF-8", 3);

        assertEquals(expectedBomCharset.charset, actualBomCharset.charset);
        assertEquals(expectedBomCharset.offset, actualBomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF16Bom() throws IOException {
        String bom = "\uFEFF\uFFFEThis is a test.";
        ByteBuffer byteData = DataUtil.readToByteBuffer(bom.getBytes(Charset.forName("UTF-8")));

        BomCharset actualBomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(actualBomCharset);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF32Bom() throws IOException {
        String bom = "\u0000\u0000\uFFFE\uFFFEThis is a test.";
        ByteBuffer byteData = DataUtil.readToByteBuffer(bom.getBytes(Charset.forName("UTF-8")));

        BomCharset actualBomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(actualBomCharset);
    }
}

class BomCharset {
    private final String charset;
    private final int offset;

    public BomCharset(String charset, int offset) {
        this.charset = charset;
        this.offset = offset;
    }
}