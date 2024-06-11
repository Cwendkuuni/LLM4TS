import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {
    private static final String BASE_URI = "http://example.com";

    @Test
    public void testLoadFromFile() {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, "UTF-8", BASE_URI);
            Assertions.assertNotNull(doc);
            // Perform assertions on the loaded document
        } catch (IOException e) {
            Assertions.fail("Was not able to load file");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", BASE_URI);
            Assertions.assertNotNull(doc);
            // Perform assertions on the loaded document
        } catch (IOException e) {
            Assertions.fail("Was not able to load input stream");
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        InputStream inputStream = getClass().getResourceAsStream("/test.xml");
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", BASE_URI, Parser.xmlParser());
            Assertions.assertNotNull(doc);
            // Perform assertions on the loaded document
        } catch (IOException e) {
            Assertions.fail("Was not able to load input stream with parser");
        }
    }

    @Test
    public void testCrossStreams() {
        // Create dummy input and output streams
        InputStream inputStream = getClass().getResourceAsStream("/test.xml");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            DataUtil.crossStreams(inputStream, outputStream);
            // Perform assertions on the output stream
        } catch (IOException e) {
            Assertions.fail("Crossing streams failed");
        }
    }

    @Test
    public void testParseInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        try {
            Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", BASE_URI, Parser.htmlParser());
            Assertions.assertNotNull(doc);
            // Perform assertions on the parsed document
        } catch (IOException e) {
            Assertions.fail("Parsing input stream failed");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        try {
            ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 1024);
            Assertions.assertNotNull(buffer);
            // Perform assertions on the read buffer
        } catch (IOException e) {
            Assertions.fail("Reading to byte buffer failed");
        }
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assertions.assertNotNull(mimeBoundary);
        Assertions.assertTrue(mimeBoundary.length() == 32);
        // Perform assertions on the mime boundary
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assertions.assertNotNull(charset);
        Assertions.assertEquals("UTF-8", charset);
        // Perform assertions on the retrieved charset
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assertions.assertNotNull(validatedCharset);
        Assertions.assertEquals("UTF-8", validatedCharset);
        // Perform assertions on the validated charset
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        byte[] byteArray = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0x41, 0x42, 0x43};
        byteData.put(byteArray);
        byteData.rewind();

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        Assertions.assertNotNull(bomCharset);
        Assertions.assertEquals("UTF-8", bomCharset.charset);
        Assertions.assertTrue(bomCharset.offset);
    }
}