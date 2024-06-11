package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);
            outputStream.write(fileContentBytes);
            testFile.writeBytes(outputStream.toByteArray());

            Document document = DataUtil.load(testFile, StandardCharsets.UTF_8.name(), "/");
            assertEquals("This is a test file.", document.body().text());
        } finally {
            testFile.delete();
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "Another test file.";
        byte[] inputStreamContentBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContentBytes);

        Document document = DataUtil.load(testInputStream, StandardCharsets.UTF_8.name(), "/");
        assertEquals("Another test file.", document.body().text());

        testInputStream.close();
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String inputStreamContent = "<xml><root><data>Test XML</data></root></xml>";
        byte[] inputStreamContentBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContentBytes);

        Document document = DataUtil.load(testInputStream, StandardCharsets.UTF_8.name(), "/", DataUtil.Parser.xmlParser());
        assertEquals("<xml><root><data>Test XML</data></root></xml>", document.html());

        testInputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testFileContent = "Test file content.";
        File testFile = new File("testFile.html");
        testFile.createNewFile();

        try (RandomAccessFile file = new RandomAccessFile(testFile, "rw")) {
            byte[] testFileContentBytes = testFileContent.getBytes(StandardCharsets.UTF_8);
            file.write(testFileContentBytes);

            InputStream fileInputStream = new FileInputStream(testFile);
            OutputStream outputStream = new FileOutputStream("testOutputFile.html");

            DataUtil.crossStreams(fileInputStream, outputStream);
            byte[] outputContentBytes = new byte[file.length()];
            outputStream.read(outputContentBytes);

            String outputContent = new String(outputContentBytes, StandardCharsets.UTF_8);
            assertEquals(testFileContent, outputContent);

            fileInputStream.close();
            outputStream.close();
            testFile.delete();

            // Test error handling
            OutputStream errorOutput = new FileOutputStream("errorFile.html");
            InputStream errorInputStream = new ByteArrayInputStream(new byte[0]);

            try {
                DataUtil.crossStreams(errorInputStream, errorOutput);
                fail("Expected IOException");
            } catch (IOException e) {
                // Expected
            }

            errorOutputStream.close();
            errorInputStream.close();
        }
    }

    @Test
    public void testParseByteData() throws IOException {
        String testContent = "This is a test.";

        ByteBuffer testByteBuffer = ByteBuffer.wrap(testContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(testByteBuffer, null, "/", DataUtil.Parser.htmlParser());
        assertEquals("This is a test.", document.body().text());
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] testByteBufferUtf8 = new byte[] {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, (byte) 'T', (byte) 'e', (byte) 's', (byte) 't'};
        ByteBuffer testByteBufferUtf16 = ByteBuffer.wrap(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) 0x00, (byte) 0x00});
        ByteBuffer testByteBufferUtf32 = ByteBuffer.wrap(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00});

        assertEquals("UTF-8", DataUtil.detectCharsetFromBom(testByteBufferUtf8, null));
        assertEquals(null, DataUtil.detectCharsetFromBom(testByteBufferUtf16, null));
        assertEquals(null, DataUtil.detectCharsetFromBom(testByteBufferUtf32, null));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testContent = "Test content.";
        String testFileContent = testContent;
        File testFile = new File("testFile.html");
        testFile.createNewFile();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] testContentBytes = testContent.getBytes(StandardCharsets.UTF_8);
            outputStream.write(testContentBytes);
            testFile.writeBytes(outputStream.toByteArray());

            ByteBuffer byteData = DataUtil.readFileToByteBuffer(testFile);
            ByteBuffer expectedByteData = ByteBuffer.wrap(testContentBytes);
            assertEquals(expectedByteData, byteData);

            testFile.delete();
        }

        byte[] testInputStreamContent = testContent.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(testInputStreamContent);

        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
        ByteBuffer expectedByteData = ByteBuffer.wrap(testInputStreamContent);
        assertEquals(expectedByteData, byteData);

        testInputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=GBK";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("GBK", charset);

        contentType = "application/xml; charset=UTF-16";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-16", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }

    @Test
    public void testValidateCharset() {
        String charsetUtf8 = "UTF-8";
        String charsetUtf16 = "UTF-16";
        String charsetInvalid = "InvalidCharset";

        assertEquals(charsetUtf8, DataUtil.validateCharset(charsetUtf8));
        assertEquals(charsetUtf16, DataUtil.validateCharset(charsetUtf16));
        assertEquals(null, DataUtil.validateCharset(charsetInvalid));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() > 0);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Character.isAlphabetic(boundary.charAt(i)) || Character.isDigit(boundary.charAt(i)) || boundary.charAt(i) == '-' || boundary.charAt(i) == '_');
        }
    }
}