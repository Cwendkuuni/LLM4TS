import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class DataUtilTest {

    private static Document document;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private static String charsetName;
    private static String baseUri;

    @Parameterized.Parameters(name = "{0} - charsetName: {1}, baseUri: {2}")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null, null, ""},
                {"UTF-8", null, ""},
                {null, "baseUri", ""},
                {"UTF-8", "baseUri", null},
                {"UTF-8", "baseUri", "baseUri"}
        });
    }

    @BeforeClass
    public static void setUp() throws Exception {
        File tempFile = File.createTempFile("test", ".html");
        byte[] fileContent = "Test content".getBytes();
        Files.write(tempFile.toPath(), fileContent);
        inputStream = new FileInputStream(tempFile);
        outputStream = new ByteArrayOutputStream();
        charsetName = "UTF-8";
        baseUri = "testBaseUri";
    }

    @Test
    public void testLoadFile() throws IOException {
        document = DataUtil.load(new File("testFile.html"), charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        byte[] inputStreamContent = "Test inputStream content".getBytes();
        inputStream = new ByteArrayInputStream(inputStreamContent);
        document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        byte[] inputStreamContent = "Test inputStream content".getBytes();
        inputStream = new ByteArrayInputStream(inputStreamContent);
        document = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream testInput = new ByteArrayInputStream("Test crossStreams input".getBytes());
        ByteArrayOutputStream testOutput = new ByteArrayOutputStream();

        DataUtil.crossStreams(testInput, testOutput);
        String output = testOutput.toString();
        Assert.assertEquals("Test crossStreams input", output);
    }

    @Test
    public void testParseInputStream() throws IOException {
        byte[] inputStreamContent = "Test parseInputStream content".getBytes();
        byte[] expectedDocumentContent = "Test parseInputStream expected document content".getBytes();

        inputStream = new ByteArrayInputStream(inputStreamContent);
        Document expectedDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.xmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals(expectedDocument.childNodeSize(), document.childNodeSize());
        for (int i = 0; i < expectedDocument.childNodeSize(); i++) {
            Assert.assertEquals(expectedDocument.childNode(i).nodeName(), document.childNode(i).nodeName());
            Assert.assertEquals(expectedDocument.childNode(i).nodeValue(), document.childNode(i).nodeValue());
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] inputStreamContent = "Test readToByteBuffer input".getBytes();
        byte[] expectedByteBufferContent = Arrays.copyOfRange(inputStreamContent, 0, 100);

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] actualByteBufferContent = Arrays.stream(byteBuffer.array())
                .mapToObj(b -> Integer.toHexString(0xFF & b))
                .collect(Collectors.joining(" "))
                .split(" ")
                .map(h -> (char) Integer.parseInt(h, 16))
                .toArray(byte[].class);

        Assert.assertArrayEquals(expectedByteBufferContent, actualByteBufferContent);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File testFile = new File("testFile.html");
        byte[] expectedByteBufferContent = Files.readAllBytes(testFile.toPath());

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);

        Assert.assertArrayEquals(expectedByteBufferContent, Arrays.stream(byteBuffer.array()).toArray());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        String expectedCharset = "UTF-8";

        String actualCharset = DataUtil.validateCharset(cs);
        Assert.assertEquals(expectedCharset, actualCharset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateCharsetWithInvalidCharset() {
        String cs = "invalidCharset";
        DataUtil.validateCharset(cs);
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        String actualMimeBoundary = DataUtil.mimeBoundary();
        Assert.assertEquals(expectedMimeBoundary, actualMimeBoundary);
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        BomCharset expectedBomCharset = new BomCharset("UTF-16", 0);
        BomCharset actualBomCharset = DataUtil.detectCharsetFromBom(byteData);

        Assert.assertNotNull(actualBomCharset);
        Assert.assertEquals(expectedBomCharset.charset, actualBomCharset.charset);
        Assert.assertEquals(expectedBomCharset.offset, actualBomCharset.offset);
    }

    @Test
    public void testParseWithInvalidCharset() throws IOException {
        String charsetName = "INVALID_CHARSET";
        byte[] inputStreamContent = "Test parseInputStream content".getBytes("UTF-8");

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent);
        try {
            Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithNullInputStream() throws IOException {
        try {
            Document document = DataUtil.load(null, charsetName, baseUri);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithInvalidCharsetName() {
        File testFile = new File("testFile.html");
        try {
            Document document = DataUtil.load(testFile, null, baseUri);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithNullBaseUri() {
        File testFile = new File("testFile.html");
        try {
            Document document = DataUtil.load(testFile, charsetName, null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithInvalidBaseUri() {
        File testFile = new File("testFile.html");
        try {
            Document document = DataUtil.load(testFile, charsetName, "invalidBaseUri");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithEmptyBaseUri() {
        File testFile = new File("testFile.html");
        try {
            Document document = DataUtil.load(testFile, charsetName, "");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithNullCharsetName() {
        File testFile = new File("testFile.html");
        try {
            Document document = DataUtil.load(testFile, null, baseUri);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithEmptyCharsetName() {
        File testFile = new File("testFile.html");
        try {
            Document document = DataUtil.load(testFile, "", baseUri);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithNullDocument() {
        try {
            DataUtil.parseInputStream(null, charsetName, baseUri, Parser.htmlParser());
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithNullParser() {
        File testFile = new File("testFile.html");
        try {
            DataUtil.parseInputStream(testFile.toInputStream(), charsetName, baseUri, null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithNullInputStreamReader() throws IOException {
        byte[] inputStreamContent = "Test inputStream content".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent);

        try {
            DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser(), null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithNullParserHtmlParser() {
        File testFile = new File("testFile.html");
        try {
            DataUtil.parseInputStream(testFile.toInputStream(), charsetName, baseUri, null, Parser.htmlParser());
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithNullParserXmlParser() {
        File testFile = new File("testFile.xml");
        try {
            DataUtil.parseInputStream(testFile.toInputStream(), charsetName, baseUri, null, Parser.xmlParser());
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithEmptyInputStream() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.available()).thenReturn(0);

        try {
            DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithNullCharsetNameEmptyString() {
        File testFile = new File("testFile.html");
        try {
            DataUtil.parseInputStream(testFile.toInputStream(), null, baseUri, Parser.htmlParser());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithNullCharsetNameEmptyStringParser() {
        File testFile = new File("testFile.html");
        try {
            DataUtil.parseInputStream(testFile.toInputStream(), null, baseUri, Parser.htmlParser());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithNullCharsetNameEmptyStringParserHtmlParser() {
        File testFile = new File("testFile.html");
        try {
            DataUtil.parseInputStream(testFile.toInputStream(), null, baseUri, Parser.htmlParser(), Parser.htmlParser());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithNullCharsetNameEmptyStringParserXmlParser() {
        File testFile = new File("testFile.xml");
        try {
            DataUtil.parseInputStream(testFile.toInputStream(), null, baseUri, Parser.xmlParser(), Parser.xmlParser());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}