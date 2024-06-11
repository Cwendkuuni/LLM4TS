import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, null);
        assertEquals("Expected HTML document", doc.html(), "Test file content");
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFileWithInvalidCharset.html");
        Document doc = DataUtil.load(file, "ISO-8859-1");
        assertEquals("Expected HTML document with specified charset", doc.html(), "Test file content with ISO-8859-1 charset");
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharsetThrowsIOException() throws IOException {
        File file = new File("testFileWithInvalidCharset.html");
        Document doc = DataUtil.load(file, "InvalidCharset");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "Expected test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null);
        assertEquals("Expected HTML document", doc.html(), testData);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "Expected test data with specified charset";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.ISO_8859_1));
        Document doc = DataUtil.load(inputStream, "ISO-8859-1");
        assertEquals("Expected HTML document with specified charset", doc.html(), testData);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharsetThrowsIOException() throws IOException {
        String testData = "Expected test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "InvalidCharset");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "Expected test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, Parser.xmlParser());
        assertEquals("Expected XML document", doc.xml(), testData);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("inputFile.html");
        File outputFile = new File("outputFile.html");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        Document inputDoc = DataUtil.load(inputFile, null);
        Document outputDoc = DataUtil.load(outputFile, null);

        assertEquals("Expected identical documents", inputDoc.html(), outputDoc.html());

        inputStream.close();
        outputStream.close();
    }
}