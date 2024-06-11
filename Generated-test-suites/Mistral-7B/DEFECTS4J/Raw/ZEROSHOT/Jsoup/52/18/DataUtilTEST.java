package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    private static ByteBuffer testData;
    private static Document testDocument;

    @BeforeClass
    public static void setup() throws IOException {
        String testDataText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello world!</p>\n" +
                "</body>\n" +
                "</html>";

        testData = ByteBuffer.wrap(testDataText.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        File outputFile = new File("output.html");

        // Write testData to testFile
        try (RandomAccessFile output = new RandomAccessFile(testFile, "rw");
             OutputStream os = new OutputStream() {
                 @Override
                 public void write(int b) throws IOException {
                     output.writeByte((byte) b);
                 }

                 @Override
                 public void close() throws IOException {
                 }
             }) {
            testData.flip();
            os.write(testData);
            testData.clear();
        }

        Document doc = DataUtil.load(testFile, null, null);
        assertEquals("Hello world!", doc.text());

        // Verify that testFile was properly read
        ByteBuffer fileData = DataUtil.readFileToByteBuffer(testFile);
        assertEquals(testData.limit(), fileData.limit());
        testFile.delete();

        // Verify that outputFile was not created
        assertFalse(outputFile.exists());

        // Test with invalid charset
        Document invalidDoc = null;
        try {
            invalidDoc = DataUtil.load(testFile, "invalidCharset", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        assertNull(invalidDoc);

        // Test with null baseUri
        Document nullBaseUriDoc = DataUtil.load(testFile, null, null);
        assertNotNull(nullBaseUriDoc);

        // Test with empty baseUri
        Document emptyBaseUriDoc = DataUtil.load(testFile, "", null);
        assertNotNull(emptyBaseUriDoc);

        // Test with valid baseUri
        Document validBaseUriDoc = DataUtil.load(testFile, "http://example.com/", null);
        assertNotNull(validBaseUriDoc);

        // Test parseByteData
        ByteBuffer byteData = testData.asReadOnlyBuffer();
        Document parsedDoc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("Hello world!", parsedDoc.text());

        // Test with invalid charset
        String invalidCharset = "invalidCharset";
        Document invalidParsedDoc = null;
        try {
            invalidParsedDoc = DataUtil.parseByteData(byteData, invalidCharset, null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        assertNull(invalidParsedDoc);

        // Test with null parser
        Document nullParserDoc = null;
        try {
            nullParserDoc = DataUtil.parseByteData(byteData, null, null, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        assertNull(nullParserDoc);

        // Test loadInputStream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.array());
        Document inputStreamDoc = DataUtil.load(inputStream, null, null);
        assertEquals("Hello world!", inputStreamDoc.text());

        // Test loadInputStream with null charsetName
        Document nullCharsetNameDoc = null;
        try {
            nullCharsetNameDoc = DataUtil.load(inputStream, null, null, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        assertNull(nullCharsetNameDoc);

        // Test loadInputStream with null baseUri
        Document nullBaseUriInputStreamDoc = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        assertNotNull(nullBaseUriInputStreamDoc);

        // Test loadInputStream with empty baseUri
        Document emptyBaseUriInputStreamDoc = DataUtil.load(inputStream, "", null, Parser.htmlParser());
        assertNotNull(emptyBaseUriInputStreamDoc);

        // Test loadInputStream with valid baseUri
        Document validBaseUriInputStreamDoc = DataUtil.load(inputStream, "http://example.com/", null, Parser.htmlParser());
        assertNotNull(validBaseUriInputStreamDoc);

        // Test loadInputStream with parser
        Document parserInputStreamDoc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(parserInputStreamDoc);

        // Test parseByteData with null byteData
        Document nullByteDataDoc = null;
        try {
            nullByteDataDoc = DataUtil.parseByteData(null, null, null, Parser.htmlParser());
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
        assertNull(nullByteDataDoc);

        // Test parseByteData with null charsetName
        Document nullCharsetNameByteDataDoc = null;
        try {
            nullCharsetNameByteDataDoc = DataUtil.parseByteData(testData, null, null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        assertNull(nullCharsetNameByteDataDoc);

        // Test parseByteData with null baseUri
        Document nullBaseUriByteDataDoc = null;
        try {
            nullBaseUriByteDataDoc = DataUtil.parseByteData(testData, null, null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        assertNull(nullBaseUriByteDataDoc);

        // Test parseByteData with null parser
        Document nullParserByteDataDoc = null;
        try {
            nullParserByteDataDoc = DataUtil.parseByteData(testData, null, null, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        assertNull(nullParserByteDataDoc);

        // Test parseByteData with empty ByteBuffer
        Document emptyByteBufferDoc = null;
        try {
            emptyByteBufferDoc = DataUtil.parseByteData(DataUtil.emptyByteBuffer(), null, null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        assertNull(emptyByteBufferDoc);

        // Test parseByteData with valid byteData
        Document validByteDataDoc = DataUtil.parseByteData(testData, null, null, Parser.htmlParser());
        assertEquals("Hello world!", validByteDataDoc.text());

        // Test parseByteData with valid charsetName
        Document validCharsetNameByteDataDoc = DataUtil.parseByteData(testData, "UTF-8", null, Parser.htmlParser());
        assertEquals("Hello world!", validCharsetNameByteDataDoc.text());

        // Test parseByteData with valid baseUri
        Document validBaseUriByteDataDoc = DataUtil.parseByteData(testData, null, "http://example.com/", Parser.htmlParser());
        assertEquals("Hello world!", validBaseUriByteDataDoc.text());

        // Test parseByteData with valid parser
        Document validParserByteDataDoc = DataUtil.parseByteData(testData, null, null, Parser.xmlParser());
        assertNotNull(validParserByteDataDoc);

        // Test crossStreams
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream2 = new ByteArrayInputStream(testData.array());
        DataUtil.crossStreams(inputStream2, outputStream);
        ByteBuffer outputByteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document crossStreamsDoc = DataUtil.parseByteData(outputByteData, null, null, Parser.htmlParser());
        assertEquals("Hello world!", crossStreamsDoc.text());

        // Test getCharsetFromContentType
        String contentType = "text/html; charset=UTF-16";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-16", charset);

        // Test getCharsetFromContentType with null contentType
        String nullContentType = null;
        String nullCharset = DataUtil.getCharsetFromContentType(nullContentType);
        assertNull(nullCharset);

        // Test getCharsetFromContentType with invalid contentType
        String invalidContentType = "application/octet-stream";
        String invalidCharset = DataUtil.getCharsetFromContentType(invalidContentType);
        assertNull(invalidCharset);

        // Test mimeBoundary
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() >= 32);

        // Test detectCharsetFromBom
        ByteBuffer bomByteData = ByteBuffer.wrap(new byte[] { 0xFF, 0xFE, 0x00, 0x00 });
        String bomCharset = DataUtil.detectCharsetFromBom(bomByteData, null);
        assertEquals("UTF-16", bomCharset);

        // Test detectCharsetFromBom with null byteData
        ByteBuffer nullByteData = DataUtil.emptyByteBuffer();
        String nullBomCharset = DataUtil.detectCharsetFromBom(nullByteData, null);
        assertNull(nullBomCharset);

        // Test detectCharsetFromBom with empty byteData
        ByteBuffer emptyByteData = DataUtil.emptyByteBuffer();
        String emptyBomCharset = DataUtil.detectCharsetFromBom(emptyByteData, null);
        assertNull(emptyBomCharset);

        // Test detectCharsetFromBom with non-BOM byteData
        ByteBuffer nonBomByteData = ByteBuffer.wrap(new byte[] { 0x00, 0x01 });
        String nonBomBomCharset = DataUtil.detectCharsetFromBom(nonBomByteData, null);
        assertNull(nonBomBomCharset);
    }

    @Test
    public void testLoadWithInvalidInput() {
        try {
            DataUtil.load(null, null, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        try {
            DataUtil.load("", null, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}