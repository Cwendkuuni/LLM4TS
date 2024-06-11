package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File inputFile = new File("testFileWithValidCharset.html"); // Replace with the actual path to a valid file with a valid charset in its header
        Document doc = DataUtil.load(inputFile, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File inputFile = new File("testFileWithInvalidCharset.html"); // Replace with the actual path to a file with an invalid charset in its header
        Document doc = null;
        try {
            doc = DataUtil.load(inputFile, null, "");
        } catch (IOException e) {
            // Expected since the file has an invalid charset
        }
        assertNull(doc);
    }

    @Test
    public void testLoadStreamWithValidCharset() throws IOException {
        String htmlContent = "HTML content with a valid charset in its header"; // Replace with the actual HTML content with a valid charset
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8")); // Replace with the actual input stream
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadStreamWithInvalidCharset() throws IOException {
        String htmlContent = "HTML content with an invalid charset in its header"; // Replace with the actual HTML content with an invalid charset
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8")); // Replace with the actual input stream
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, null, "");
        } catch (IOException e) {
            // Expected since the input stream has an invalid charset
        }
        assertNull(doc);
    }

    @Test
    public void testLoadStreamWithNullCharset() throws IOException {
        String htmlContent = "HTML content"; // Replace with the actual HTML content
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8")); // Replace with the actual input stream
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, null, "");
        } catch (IOException e) {
            // Expected since no charset was provided, and the default charset is not found in the input
        }
        assertNull(doc);
    }

    @Test
    public void testLoadStreamWithDefaultCharset() throws IOException {
        String htmlContent = "HTML content"; // Replace with the actual HTML content
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-16")); // Replace with the actual input stream
        Document doc = DataUtil.load(inputStream, null, "");
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String htmlContent = "HTML content with a valid charset in its header"; // Replace with the actual HTML content with a valid charset
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8")); // Replace with the actual input stream
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "HTML content with an invalid charset in its header"; // Replace with the actual HTML content with an invalid charset
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8")); // Replace with the actual input stream
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
        } catch (IOException e) {
            // Expected since the input stream has an invalid charset
        }
        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithNullCharset() throws IOException {
        String htmlContent = "HTML content with a null charset in its header"; // Replace with the actual HTML content with a null charset
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8")); // Replace with the actual input stream
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
        } catch (IOException e) {
            // Expected since no charset was provided, and the default charset is not found in the input
        }
        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "HTML content with no charset in its header"; // Replace with the actual HTML content with no charset
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-16")); // Replace with the actual input stream
        Document doc = DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
        assertNotNull(doc);
    }
}