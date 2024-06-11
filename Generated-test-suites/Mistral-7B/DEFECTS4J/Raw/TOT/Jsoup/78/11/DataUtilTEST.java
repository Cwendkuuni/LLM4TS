package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        Assert.assertNotNull(doc);
        // Add assertions for elements, attributes, etc. based on your HTML file and expected results
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html"); // Assuming the file contains an invalid charset in the header
        Document doc = null;
        try {
            doc = DataUtil.load(file, "GBK", "");
        } catch (IOException e) {
            // Expected exception, verify the message contains "Charset not supported"
            String expectedMessage = "Charset not supported: GBK";
            assertTrue(e.getMessage().contains(expectedMessage));
        }
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        Document doc = DataUtil.load(input, "UTF-8", "");
        Assert.assertNotNull(doc);
        // Add assertions for elements, attributes, etc. based on your HTML file and expected results

        input.close();
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        Document doc = null;
        try {
            doc = DataUtil.load(input, "GBK", "");
        } catch (IOException e) {
            // Expected exception, verify the message contains "Charset not supported"
            String expectedMessage = "Charset not supported: GBK";
            assertTrue(e.getMessage().contains(expectedMessage));
        }
        Assert.assertNull(doc);

        input.close();
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        Document doc = null;
        try {
            doc = DataUtil.load(input, "", "");
        } catch (IOException e) {
            // Expected exception, verify the message contains "Could not determine charset"
            String expectedMessage = "Could not determine charset";
            assertTrue(e.getMessage().contains(expectedMessage));
        }
        Assert.assertNull(doc);

        input.close();
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        Document doc = DataUtil.parseInputStream(input, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        // Add assertions for elements, attributes, etc. based on your HTML file and expected results

        input.close();
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream input = new FileInputStream(new File("test.html"));
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(input, "GBK", "", Parser.htmlParser());
        } catch (IOException e) {
            // Expected exception, verify the message contains "Charset not supported"
            String expectedMessage = "Charset not supported: GBK";
            assertTrue(e.getMessage().contains(expectedMessage));
        }
        Assert.assertNull(doc);

        input.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.html");
        File outputFile = new File("test_output.html");
        InputStream input = new FileInputStream(inputFile);
        OutputStream output = new FileOutputStream(outputFile);

        DataUtil.crossStreams(input, output);
        input.close();
        output.close();

        // Add assertions on the output file based on your expected results
    }
}