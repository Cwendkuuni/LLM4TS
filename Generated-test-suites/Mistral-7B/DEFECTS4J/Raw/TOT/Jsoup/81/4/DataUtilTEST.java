package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileWithUtf8Charset() throws IOException {
        // Given a file with UTF-8 encoding
        File inputFile = new File("test_file_utf8.html");
        Document doc = null;

        // When DataUtil.load method is called with the file and UTF-8 charset
        try {
            doc = DataUtil.load(inputFile, "UTF-8", "");
        } catch (IOException e) {
            // Then no exception should be thrown
            Assert.assertNull(e);
        }

        // Then the document should be correctly loaded
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        // Given a file with an invalid charset
        File inputFile = new File("test_file_invalid_charset.html");
        Document doc = null;

        // When DataUtil.load method is called with the file and an invalid charset
        try {
            doc = DataUtil.load(inputFile, "InvalidCharset", "");
            Assert.fail("Should have thrown an IOException");
        } catch (IOException e) {
            // Then an IOException should be thrown
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithUtf8Charset() throws IOException {
        // Given a InputStream with UTF-8 encoded data
        String testInput = "This is a test. <p>Unicode: abcdefghijklmnopqrstuvwxyz ABCEFGHIJKLMNOPQRSTUVWXYZ</p>";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));
        Document doc = null;

        // When DataUtil.load method is called with the InputStream and UTF-8 charset
        try {
            doc = DataUtil.load(inputStream, "UTF-8", "");
        } catch (IOException e) {
            // Then no exception should be thrown
            Assert.assertNull(e);
        }

        // Then the document should be correctly loaded
        Assert.assertNotNull(doc);
        Assert.assertEquals(testInput, doc.outerHtml());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        // Given a InputStream with data and an invalid charset
        String testInput = "This is a test. <p>Unicode: abcdefghijklmnopqrstuvwxyz ABCEFGHIJKLMNOPQRSTUVWXYZ</p>";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));

        // When DataUtil.load method is called with the InputStream and an invalid charset
        try {
            doc = DataUtil.load(inputStream, "InvalidCharset", "");
            Assert.fail("Should have thrown an IOException");
        } catch (IOException e) {
            // Then an IOException should be thrown
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithBomAndUtf8Charset() throws IOException {
        // Given a InputStream with UTF-8 encoded data and a BOM
        String testInput = "\ufeffThis is a test. <p>Unicode: abcdefghijklmnopqrstuvwxyz ABCEFGHIJKLMNOPQRSTUVWXYZ</p>";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));
        Document doc = null;

        // When DataUtil.load method is called with the InputStream and UTF-8 charset
        try {
            doc = DataUtil.load(inputStream, "UTF-8", "");
        } catch (IOException e) {
            // Then no exception should be thrown
            Assert.assertNull(e);
        }

        // Then the document should be correctly loaded
        Assert.assertNotNull(doc);
        Assert.assertEquals(testInput, doc.outerHtml());
    }

    @Test
    public void testLoadInputStreamWithBomAndInvalidCharset() throws IOException {
        // Given a InputStream with UTF-8 encoded data and a BOM, and an invalid charset
        String testInput = "\ufeffThis is a test. <p>Unicode: abcdefghijklmnopqrstuvwxyz ABCEFGHIJKLMNOPQRSTUVWXYZ</p>";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));

        // When DataUtil.load method is called with the InputStream and an invalid charset
        try {
            doc = DataUtil.load(inputStream, "InvalidCharset", "");
            Assert.fail("Should have thrown an IOException");
        } catch (IOException e) {
            // Then an IOException should be thrown
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Given two InputStreams
        String testInput = "This is a test.";
        InputStream input1 = new ByteArrayInputStream(testInput.getBytes("UTF-8"));
        OutputStream output = new ByteArrayOutputStream();

        // When DataUtil.crossStreams method is called with the InputStreams
        try {
            DataUtil.crossStreams(input1, output);
        } catch (IOException e) {
            // Then no exception should be thrown
            Assert.assertNull(e);
        }

        // Then the data from the first InputStream should be written to the second OutputStream
        String expectedOutput = testInput;
        String outputString = output.toString();
        Assert.assertEquals(expectedOutput, outputString);
    }

    @Test
    public void testParseInputStreamWithUtf8CharsetAndBaseUri() throws IOException {
        // Given a InputStream with UTF-8 encoded data and a base URI
        String testInput = "<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));
        String baseUri = "http://localhost:8080";
        Parser parser = Parser.htmlParser();
        Document doc = null;

        // When DataUtil.parseInputStream method is called with the InputStream, UTF-8 charset, and base URI
        try {
            doc = DataUtil.parseInputStream(inputStream, "UTF-8", baseUri, parser);
        } catch (IOException e) {
            // Then no exception should be thrown
            Assert.assertNull(e);
        }

        // Then the document should be correctly parsed
        Assert.assertNotNull(doc);
        Assert.assertEquals(testInput, doc.outerHtml());
        Assert.assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseInputStreamWithInvalidCharsetAndBaseUri() throws IOException {
        // Given a InputStream with data and an invalid charset, and a base URI
        String testInput = "<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));
        String baseUri = "http://localhost:8080";
        Parser parser = Parser.htmlParser();

        // When DataUtil.parseInputStream method is called with the InputStream, an invalid charset, and base URI
        try {
            doc = DataUtil.parseInputStream(inputStream, "InvalidCharset", baseUri, parser);
            Assert.fail("Should have thrown an IOException");
        } catch (IOException e) {
            // Then an IOException should be thrown
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testParseInputStreamWithBomAndUtf8CharsetAndBaseUri() throws IOException {
        // Given a InputStream with UTF-8 encoded data and a BOM, and a base URI
        String testInput = "\ufeff<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));
        String baseUri = "http://localhost:8080";
        Parser parser = Parser.htmlParser();
        Document doc = null;

        // When DataUtil.parseInputStream method is called with the InputStream, UTF-8 charset, and base URI
        try {
            doc = DataUtil.parseInputStream(inputStream, "UTF-8", baseUri, parser);
        } catch (IOException e) {
            // Then no exception should be thrown
            Assert.assertNull(e);
        }

        // Then the document should be correctly parsed
        Assert.assertNotNull(doc);
        Assert.assertEquals(testInput, doc.outerHtml());
        Assert.assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseInputStreamWithBomAndInvalidCharsetAndBaseUri() throws IOException {
        // Given a InputStream with UTF-8 encoded data and a BOM, and an invalid charset, and a base URI
        String testInput = "\ufeff<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));
        String baseUri = "http://localhost:8080";
        Parser parser = Parser.htmlParser();

        // When DataUtil.parseInputStream method is called with the InputStream, an invalid charset, and base URI
        try {
            doc = DataUtil.parseInputStream(inputStream, "InvalidCharset", baseUri, parser);
            Assert.fail("Should have thrown an IOException");
        } catch (IOException e) {
            // Then an IOException should be thrown
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, byteBuffer.limit());
        Assert.assertEquals(0, byteBuffer.position());
    }

    @Test(expected = HelperException.class)
    public void testEmptyByteBufferWrite() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        byteBuffer.put("Unexpected data".getBytes());
    }

    @Test
    public void testReadToByteBufferWithValidCharset() throws IOException {
        String inputData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, inputData.length());

        // Then the byte buffer should contain the expected data
        Assert.assertEquals(inputData.length(), byteBuffer.limit());
        Assert.assertEquals(inputData, Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test(expected = HelperException.class)
    public void testReadToByteBufferWithInvalidCharset() throws IOException {
        String inputData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, inputData.length());

        // When an attempt is made to decode the byte buffer with an invalid charset
        String invalidCharset = "InvalidCharset";
        Assert.assertThrows(HelperException.class, () -> {
            Charset.forName(invalidCharset).decode(byteBuffer);
        });
    }

    @Test
    public void testReadToByteBufferWithZeroLength() throws IOException {
        String inputData = "";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        // Then the byte buffer should be empty
        Assert.assertEquals(0, byteBuffer.limit());
        Assert.assertEquals(0, byteBuffer.position());
    }

    @Test
    public void testReadToByteBufferWithInputStreamClosed() throws IOException {
        String inputData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, inputData.length());
        inputStream.close();

        // Then the byte buffer should contain the expected data
        Assert.assertEquals(inputData.length(), byteBuffer.limit());
        Assert.assertEquals(inputData, Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String inputData = "Test data";
        File inputFile = new File("test_file.txt");
        byte[] fileData = inputData.getBytes("UTF-8");
        Files.write(Paths.get(inputFile.getPath()), fileData);

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(inputFile);

        // Then the byte buffer should contain the expected data
        Assert.assertEquals(inputData.length(), byteBuffer.limit());
        Assert.assertEquals(inputData, Charset.forName("UTF-8").decode(byteBuffer).toString());

        Files.delete(Paths.get(inputFile.getPath()));
    }
}