package org.jsoup.helper.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil methods
 */
public class DataUtilTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = Parser.htmlParser();
    }

    @After
    public void teardown() {
        parser = null;
    }

    /**
     * Test loading a file using the default charset
     */
    @Test
    public void testLoadFileDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
    }

    /**
     * Test loading a file with a specified charset
     */
    @Test
    public void testLoadFileSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-16", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-16", doc.outputSettings().charset());
    }

    /**
     * Test loading a file with an invalid charset
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadFileInvalidCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "invalid-charset", null);
    }

    /**
     * Test parsing a Document from an InputStream using the default charset
     */
    @Test
    public void testLoadInputStreamDefaultCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-16\">\n</head>\n<body>\n<h1>Test</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null, parser);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-8", doc.outputSettings().charset());
    }

    /**
     * Test parsing a Document from an InputStream with a specified charset
     */
    @Test
    public void testLoadInputStreamSpecifiedCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-16\">\n</head>\n<body>\n<h1>Test</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-16", null, parser);
        Assert.assertNotNull(doc);
        Assert.assertEquals("UTF-16", doc.outputSettings().charset());
    }

    /**
     * Test parsing a Document from an InputStream with an invalid charset
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadInputStreamInvalidCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"invalid-charset\">\n</head>\n<body>\n<h1>Test</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null, parser);
    }
}