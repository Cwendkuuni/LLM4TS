package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(PowerMockRunner.class)
@PowerMockRunner.Silent
public class DataUtilTest {

    private static final String TEST_FILE_PATH = "src/test/resources/test.html";
    private static final String DEFAULT_CHARSET = "UTF-8";

    private DataUtil dataUtil;
    private InputStream inputStream;
    private File file;
    private Document doc;

    @Before
    public void setup() {
        Class<?> dataUtilClass = DataUtil.class;
        AnnotatedClassLoader annotatedClassLoader = new AnnotatedClassLoader(DataUtil.class.getClassLoader());
        dataUtil = (DataUtil) annotatedClassLoader.loadClass(dataUtilClass.getName());
        file = new File(TEST_FILE_PATH);
    }

    @Test
    public void testLoadFromFile() throws IOException {
        doc = dataUtil.load(file, DEFAULT_CHARSET, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        inputStream = new FileInputStream(file);
        doc = dataUtil.load(inputStream, DEFAULT_CHARSET, null);
        Assert.assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        inputStream = new FileInputStream(file);
        Parser parser = Parser.xmlParser();
        doc = dataUtil.load(inputStream, DEFAULT_CHARSET, null, parser);
        Assert.assertNotNull(doc);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        inputStream = new FileInputStream(file);
        doc = dataUtil.load(inputStream, null, null);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithEmptyCharset() throws IOException {
        inputStream = new FileInputStream(file);
        doc = dataUtil.load(inputStream, "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInputStreamWithNullBaseUri() throws IOException {
        inputStream = new FileInputStream(file);
        doc = dataUtil.load(inputStream, DEFAULT_CHARSET, null, Parser.htmlParser());
        doc.outputSettings().baseUri(null);
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullInputStream() throws IOException {
        InputStream in = null;
        OutputStream out = new ByteArrayOutputStream();
        dataUtil.crossStreams(in, out);
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullOutputStream() throws IOException {
        InputStream in = new FileInputStream(file);
        OutputStream out = null;
        dataUtil.crossStreams(in, out);
    }

    @Test
    public void testParseInputStream() throws IOException {
        inputStream = new FileInputStream(file);
        doc = dataUtil.parseInputStream(inputStream, DEFAULT_CHARSET, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithCustomParser() throws IOException {
        inputStream = new FileInputStream(file);
        Parser parser = Parser.xmlParser();
        doc = dataUtil.parseInputStream(inputStream, DEFAULT_CHARSET, null, parser);
        Assert.assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        inputStream = new FileInputStream(file);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(file.readAllBytes());
        ByteBuffer actualByteBuffer = dataUtil.readToByteBuffer(inputStream);
        Assert.assertArrayEquals(expectedByteBuffer.array(), actualByteBuffer.array());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNullInputStream() throws IOException {
        ByteBuffer byteBuffer = dataUtil.readToByteBuffer(null);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNegativeMaxSize() throws IOException {
        inputStream = new FileInputStream(file);
        dataUtil.readToByteBuffer(inputStream, -1);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithMaxSizeLargerThanFileSize() throws IOException {
        inputStream = new FileInputStream(file);
        long fileSize = file.length();
        dataUtil.readToByteBuffer(inputStream, (int) (fileSize + 1));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("EUC-JP", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=xyz";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = dataUtil.validateCharset(charset);
        Assert.assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharsetWithNullCharset() {
        String charset = null;
        String validatedCharset = dataUtil.validateCharset(charset);
        Assert.assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = dataUtil.mimeBoundary();
        Assert.assertNotEmpty(mimeBoundary);
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        InputStream bomInputStream = new FileInputStream(new File("src/test/resources/bom_utf8.txt"));
        ByteBuffer byteBuffer = dataUtil.readToByteBuffer(bomInputStream);
        BomCharset bomCharset = dataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertEquals(3, bomCharset.offset);
        bomInputStream.close();
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() throws IOException {
        InputStream bomInputStream = new FileInputStream(new File("src/test/resources/bom_invalid.txt"));
        ByteBuffer byteBuffer = dataUtil.readToByteBuffer(bomInputStream);
        BomCharset bomCharset = dataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertNull(bomCharset);
        bomInputStream.close();
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