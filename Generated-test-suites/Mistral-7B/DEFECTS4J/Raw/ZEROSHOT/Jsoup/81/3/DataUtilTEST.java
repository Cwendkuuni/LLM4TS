package org.jsoup.helper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private Document expectedDocument;
    private Document actualDocument;

    @Before
    public void setUp() {
        // Initialize expectedDocument with some test data
        String testHtml = "<html><head><meta charset=\"UTF-8\"></head><body><div id=\"test\">Test Data</div></body></html>";
        expectedDocument = DataUtil.parseInputStream(new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8)), "UTF-8", null, Parser.htmlParser());
    }

    @After
    public void tearDown() {
        // Perform any necessary cleanup
        if (expectedDocument != null) {
            expectedDocument.outputSettings().clear();
            expectedDocument.outputSettings().indentAmount(0);
            expectedDocument.outputSettings().charset(null);
            expectedDocument = null;
        }
        if (actualDocument != null) {
            actualDocument.outputSettings().clear();
            actualDocument.outputSettings().indentAmount(0);
            actualDocument = null;
        }
    }

    @org.junit.Test
    public void testLoadFile() throws IOException {
        String testFilePath = "test.html";
        File testFile = new File(testFilePath);
        String testHtml = "<html><head><meta charset=\"UTF-8\"></head><body><div id=\"test\">Test Data</div></body></html>";
        Files.write(Paths.get(testFilePath), testHtml.getBytes(StandardCharsets.UTF_8));

        actualDocument = DataUtil.load(testFile, "UTF-8", null);

        Assert.assertEquals("Comparing expected and actual Document", expectedDocument, actualDocument);

        Files.deleteIfExists(Paths.get(testFilePath));
    }

    @org.junit.Test
    public void testLoadInputStream() throws IOException {
        InputStream testInput = new ByteArrayInputStream(expectedDocument.html().getBytes(StandardCharsets.UTF_8));
        actualDocument = DataUtil.load(testInput, "UTF-8", null);

        Assert.assertEquals("Comparing expected and actual Document", expectedDocument, actualDocument);
    }

    @org.junit.Test
    public void testParseInputStream() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"></head><body><div id=\"test\">Test Data</div></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        actualDocument = DataUtil.parseInputStream(testInputStream, "UTF-8", null, Parser.htmlParser());

        Assert.assertEquals("Comparing expected and actual Document", expectedDocument, actualDocument);
    }

    @org.junit.Test
    public void testCrossStreams() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"></head><body><div id=\"test\">Test Data</div></body></html>";
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(testInputStream, testOutputStream);
        String actualHtml = testOutputStream.toString(StandardCharsets.UTF_8.toString());

        Document actualDocument = DataUtil.parseInputStream(new ByteArrayInputStream(actualHtml.getBytes(StandardCharsets.UTF_8)), "UTF-8", null);

        Assert.assertEquals("Comparing expected and actual Document", expectedDocument, actualDocument);
    }
}