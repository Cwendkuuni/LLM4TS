import org.jsoup.helper.DataUtil;
import org.jsoup.helper.ConstrainableInputStream;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        // Given
        String filePath = "testData/testFile.html";
        File file = new File(filePath);

        // When
        Document document = DataUtil.load(file, null);

        // Then
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        // Given
        String filePath = "testData/testFileWithCharset.html";
        File file = new File(filePath);
        String charset = "ISO-8859-1";

        // When
        Document document = DataUtil.load(file, charset);

        // Then
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        // Given
        String filePath = "testData/testFile.html";
        File file = new File(filePath);
        String charset = "invalidCharset";

        // When & Then
        DataUtil.load(file, charset);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        // Given
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        // When
        Document document = DataUtil.load(inputStream, null);

        // Then
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        // Given
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String charset = "ISO-8859-1";

        // When
        Document document = DataUtil.load(inputStream, charset);

        // Then
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        // Given
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String charset = "invalidCharset";

        // When & Then
        DataUtil.load(inputStream, charset);
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        // Given
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        // When
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());

        // Then
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        // Given
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String charset = "ISO-8859-1";

        // When
        Document document = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());

        // Then
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        // Given
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String charset = "invalidCharset";

        // When & Then
        DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Given
        File inputFile = new File("testData/inputFile.txt");
        File outputFile = new File("testData/outputFile.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        // When
        DataUtil.crossStreams(inputStream, outputStream);

        // Then
        byte[] expectedOutput = new byte[]{'T', 'e', 's', 't', ' ', 'D', 'a', 't', 'a', ' ', 'i', 'n', 'p', 'u', 't', ' ', 'o', 'u', 't', 'p', 'u', 't', '\n'};
        byte[] outputData = new byte[outputFile.length()];
        FileInputStream outputStreamReader = new FileInputStream(outputFile);
        outputStreamReader.read(outputData);
        outputStreamReader.close();

        Assert.assertArrayEquals(expectedOutput, outputData);

        inputFile.delete();
        outputFile.delete();
    }

    @Test
    public void testParseInputStreamWithEmptyContent() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // When
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());

        // Then
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Given
        InputStream inputStream = Mockito.mock(InputStream.class);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1000);

        // When
        Mockito.when(inputStream.read()).thenReturn(3);
        Mockito.when(inputStream.read(Mockito.anyByteArray())).thenAnswer(invocation -> {
            byte[] bytes = invocation.getArgument(0);
            System.arraycopy(new byte[]{1, 2, 3}, 0, bytes, 0, 3);
            return 3;
        });

        // Then
        byte[] expectedBytes = new byte[]{1, 2, 3};
        Assert.assertArrayEquals(expectedBytes, byteBuffer.array());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Given
        String contentType = "text/html; charset=UTF-8";

        // When
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Then
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWhenInvalid() {
        // Given
        String contentType = "text/html";

        // When
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Then
        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        // Given
        String charset = "UTF-8";

        // When
        String validatedCharset = DataUtil.validateCharset(charset);

        // Then
        Assert.assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void testValidateCharsetWhenInvalid() {
        // Given
        String charset = "invalidCharset";

        // When
        String validatedCharset = DataUtil.validateCharset(charset);

        // Then
        Assert.assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        // When
        String mimeBoundary = DataUtil.mimeBoundary();

        // Then
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        // Given
        String bomData = "EF BBBF";
        ByteBuffer byteBuffer = ByteBuffer.wrap(bomData.getBytes());

        // When
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        // Then
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertFalse(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWhenUtf16() throws IOException {
        // Given
        String bomData = "FE FF";
        ByteBuffer byteBuffer = ByteBuffer.wrap(bomData.getBytes());

        // When
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        // Then
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-16", bomCharset.charset);
        Assert.assertFalse(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWhenUtf32() throws IOException {
        // Given
        String bomData = "00 00 FE FF";
        ByteBuffer byteBuffer = ByteBuffer.wrap(bomData.getBytes());

        // When
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        // Then
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-32", bomCharset.charset);
        Assert.assertFalse(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWhenNoBom() throws IOException {
        // Given
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1);
        inputStream.close();

        // When
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        // Then
        Assert.assertNull(bomCharset);
    }
}