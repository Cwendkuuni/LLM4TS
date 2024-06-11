package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charset = "UTF-8";
        File inputFile = new File("testFile.html");
        Document document = DataUtil.load(inputFile, charset, null);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charset = "InvalidCharset";
        File inputFile = new File("testFile.html");
        try {
            Document document = DataUtil.load(inputFile, charset, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        InputStream inputStream = getClass().getResourceAsStream("/testFile.html");
        Document document = DataUtil.load(inputStream, charset, null);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charset = "InvalidCharset";
        InputStream inputStream = getClass().getResourceAsStream("/testFile.html");
        try {
            Document document = DataUtil.load(inputStream, charset, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithNullCharsetName() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/testFile.html");
        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset()); // default charset
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        InputStream inputStream = getClass().getResourceAsStream("/testFile.html");
        Document document = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charset = "InvalidCharset";
        InputStream inputStream = getClass().getResourceAsStream("/testFile.html");
        try {
            Document document = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("testFile.html");
        File outputFile = new File("outputFile.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        outputStream.close();

        File expectedFile = new File("expectedOutputFile.html");
        File actualFile = new File("outputFile.html");
        assertEquals(expectedFile.length(), actualFile.length());
        assertTrue(new FileComparison().compareFiles(expectedFile, actualFile));
    }

    @Test
    public void testParseInputStreamWithBOMAndValidCharset() throws IOException {
        String charset = "UTF-8";
        InputStream inputStream = getClass().getResourceAsStream("/testFileWithBOM.html");
        Document document = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithBOMAndInvalidCharset() throws IOException {
        String charset = "InvalidCharset";
        InputStream inputStream = getClass().getResourceAsStream("/testFileWithBOM.html");
        try {
            Document document = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File inputFile = new File("testFile.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(inputFile));
        long expectedSize = inputFile.length();
        assertEquals(expectedSize, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        File inputFile = new File("testFile.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(inputFile), 1L);
        long expectedSize = inputFile.length();
        assertEquals(expectedSize, byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File inputFile = new File("testFile.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(inputFile);
        long expectedSize = inputFile.length();
        assertEquals(expectedSize, byteBuffer.limit());
    }

    @Test(expected = IOException.class)
    public void testReadFileToByteBufferWithInvalidFile() throws IOException {
        File inputFile = new File("nonExistentFile.html");
        DataUtil.readFileToByteBuffer(inputFile);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("UTF-8", validatedCharset);

        charset = "InvalidCharset";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);

        charset = "UTF-8\t";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertFalse(mimeBoundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File("testFileWithBOM.html"));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File("testFileInvalidBOM.html"));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNull(bomCharset);
    }
}

class FileComparison {

    public boolean compareFiles(File expectedFile, File actualFile) {
        try {
            byte[] expectedBytes = new byte[expectedFile.length()];
            FileInputStream expectedInputStream = new FileInputStream(expectedFile);
            expectedInputStream.read(expectedBytes);
            expectedInputStream.close();

            byte[] actualBytes = new byte[actualFile.length()];
            FileInputStream actualInputStream = new FileInputStream(actualFile);
            actualInputStream.read(actualBytes);
            actualInputStream.close();

            for (int i = 0; i < expectedBytes.length; i++) {
                if (expectedBytes[i] != actualBytes[i]) {
                    return false;
                }
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}