package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.io.ByteBufferInputSource;
import org.jsoup.io.Charsets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(PowerMockRunner.class)
public class DataUtilTest {

    private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*\"?([^\\s;\"]*)");
    static final String defaultCharset = "UTF-8"; // used if not found in header or meta charset
    private static final int bufferSize = 0x20000; // ~130K.

    private DataUtil dataUtil;
    private Parser parser;
    private Document document;
    private static final String TEST_FILE = "testFile.html";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Validate.class);
        this.dataUtil = new DataUtil();
        this.parser = Mockito.mock(Parser.class);
        this.document = Mockito.mock(Document.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = null;
        File file = new File(TEST_FILE);
        Document expectedDocument = new Document();
        ByteBuffer byteData = ByteBuffer.allocate(bufferSize);

        Mockito.when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDocument);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bufferSize);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        outputStream.write("Test Data".getBytes("UTF-8"));
        outputStream.close();
        fileOutputStream.close();

        FileInputStream inputStream = new FileInputStream(file);

        Document actualDocument = this.dataUtil.load(file, charsetName, baseUri);

        Mockito.verify(parser, Mockito.times(1)).parseInput(Mockito.eq("Test Data"), Mockito.eq(baseUri));
        assertEquals(expectedDocument, actualDocument);
        inputStream.close();
        file.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithIOException() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = null;
        File file = new File(TEST_FILE);

        FileInputStream inputStream = Mockito.mock(FileInputStream.class);
        Mockito.when(inputStream.close()).thenThrow(new IOException());
        Mockito.when(inputStream.readAllBytes()).thenThrow(new IOException());

        try {
            this.dataUtil.load(file, charsetName, baseUri);
        } finally {
            file.delete();
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = null;
        Document expectedDocument = new Document();

        Mockito.when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDocument);

        ByteBuffer byteData = this.dataUtil.readToByteBuffer(inputStream);
        ByteBufferInputSource inputSource = new ByteBufferInputSource(byteData);

        Document actualDocument = this.dataUtil.load(inputStream, charsetName, baseUri);

        Mockito.verify(parser, Mockito.times(1)).parseInput(Mockito.eq("Test Data"), Mockito.eq(baseUri));
        assertEquals(expectedDocument, actualDocument);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes("UTF-8"));
        String charsetName = null;
        String baseUri = null;

        ByteBuffer byteData = this.dataUtil.readToByteBuffer(inputStream);
        ByteBufferInputSource inputSource = new ByteBufferInputSource(byteData);

        Document actualDocument = this.dataUtil.load(inputStream, charsetName, baseUri);

        Mockito.verify(parser, Mockito.times(1)).parseInput(Mockito.eq("Test Data"), Mockito.eq(baseUri));
        assertEquals(actualDocument, this.document); // since no document is returned, it is assumed that the actualDocument is the mocked document
        inputStream.close();
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(bufferSize);
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = Mockito.mock(Parser.class);
        Document expectedDocument = new Document();

        Mockito.when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDocument);

        String docData = "Test Data";
        byteData.put(docData.getBytes("UTF-8"));
        byteData.flip();

        Document actualDocument = this.dataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Mockito.verify(parser, Mockito.times(1)).parseInput(Mockito.eq(docData), Mockito.eq(baseUri));
        assertEquals(expectedDocument, actualDocument);
        byteData.clear();
    }

    @Test
    public void testParseByteDataWithNullCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(bufferSize);
        String charsetName = null;
        String baseUri = null;
        Parser parser = Mockito.mock(Parser.class);
        Document expectedDocument = new Document();

        Mockito.when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDocument);

        String docData = "Test Data";
        byteData.put(docData.getBytes("UTF-8"));
        byteData.flip();

        Document actualDocument = this.dataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Mockito.verify(parser, Mockito.times(1)).parseInput(Mockito.eq(docData), Mockito.eq(baseUri));
        assertEquals(expectedDocument, actualDocument); // since no document is returned, it is assumed that the actualDocument is the mocked document
        byteData.clear();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes("UTF-8"));
        int maxSize = 20;
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap("Test Data".getBytes("UTF-8"));

        ByteBuffer actualByteBuffer = this.dataUtil.readToByteBuffer(inputStream, maxSize);

        Mockito.verify(inputStream, Mockito.times(1)).readAllBytes();
        assertEquals(expectedByteBuffer, actualByteBuffer);
        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithUnlimitedSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes("UTF-8"));
        int maxSize = 0;
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap("Test Data".getBytes("UTF-8"));

        ByteBuffer actualByteBuffer = this.dataUtil.readToByteBuffer(inputStream, maxSize);

        Mockito.verify(inputStream, Mockito.times(1)).readAllBytes();
        assertEquals(expectedByteBuffer, actualByteBuffer);
        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String expectedCharset = "EUC-JP";

        String actualCharset = this.dataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String expectedCharset = null;

        String actualCharset = this.dataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String expectedCharset = null;

        String actualCharset = this.dataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, actualCharset);
    }
}