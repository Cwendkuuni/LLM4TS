package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

public class DataUtilTest {
    @Test
    public void testLoad() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head>\n<meta charset=\"UTF-16\"/>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayWriter writer = new ByteArrayWriter(out);
        writer.write(testData);
        InputStream in = new ByteArrayInputStream(out.toByteArray());

        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testLoadWithCharsetFromMetaTag() throws IOException {
        String testData = "<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16\"/>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayWriter writer = new ByteArrayWriter(out);
        writer.write(testData);
        InputStream in = new ByteArrayInputStream(out.toByteArray());

        Document doc = DataUtil.load(in, null, "", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testLoadWithCharsetFromXmlDeclaration() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<html>\n<body>\n<p>Test</p>\n</body>\n</html>";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayWriter writer = new ByteArrayWriter(out);
        writer.write(testData);
        InputStream in = new ByteArrayInputStream(out.toByteArray());

        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-32\"?>\n<html><head>\n<meta charset=\"UTF-16\"/>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayWriter writer = new ByteArrayWriter(out);
        writer.write(testData);
        InputStream in = new ByteArrayInputStream(out.toByteArray());

        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDetectedCharsetFromContentTypeHeader() throws IOException {
        String testData = "<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16\"/>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayWriter writer = new ByteArrayWriter(out);
        writer.write(testData);
        InputStream in = new ByteArrayInputStream(out.toByteArray());

        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        String charsetName = null;
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDetectedCharsetFromXmlDeclaration() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-32\"?>\n<html>\n<body>\n<p>Test</p>\n</body>\n</html>";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayWriter writer = new ByteArrayWriter(out);
        writer.write(testData);
        InputStream in = new ByteArrayInputStream(out.toByteArray());

        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("UTF-32", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<html><head>\n<meta charset=\"UTF-16\"/>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayWriter writer = new ByteArrayWriter(out);
        writer.write(testData);
        InputStream in = new ByteArrayInputStream(out.toByteArray());

        String charsetName = "UTF-16";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(in), charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayWriter writer = new ByteArrayWriter(out);
        writer.write(testData);
        InputStream in = new ByteArrayInputStream(out.toByteArray());

        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        assertEquals(testData.length(), byteData.remaining());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testData = "Test";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayWriter writer = new ByteArrayWriter(out);
        writer.write(testData.repeat(1000));
        InputStream in = new ByteArrayInputStream(out.toByteArray());

        ByteBuffer byteData = DataUtil.readToByteBuffer(in, 1024);
        assertEquals(1024, byteData.limit());
        assertEquals(1024, byteData.remaining());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String testFilePath = "test.txt";
        String testData = "Test";

        File file = new File(testFilePath);
        FileWriter writer = new FileWriter(file);
        writer.write(testData);
        writer.close();

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertEquals(testData.length(), byteData.remaining());

        file.delete();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-16";
        assertEquals("UTF-16", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalid";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-16", DataUtil.validateCharset("UTF-16"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-32", DataUtil.validateCharset("UTF-32"));
        assertEquals("UTF-16BE", DataUtil.validateCharset("UTF-16BE"));
        assertNull(DataUtil.validateCharset("invalid"));
    }

    @Test
    public void testMimeBoundary() {
        assertEquals(32, DataUtil.mimeBoundary().length());
    }
}