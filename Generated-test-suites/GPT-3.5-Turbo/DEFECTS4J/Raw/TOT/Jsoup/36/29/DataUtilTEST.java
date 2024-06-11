import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            Document doc = DataUtil.load(file, "UTF-8", "");

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // typical use case

            doc = DataUtil.load(file, "ISO-8859-1", "");

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // edge case - different charset

            doc = DataUtil.load(file, "", "");

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // edge case - empty charset

            doc = DataUtil.load(file, null, "");

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // edge case - null charset

            doc = DataUtil.load(file, "UTF-8", null);

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // edge case - null baseUri

        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document doc = DataUtil.load(inputStream, "UTF-8", "");

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // typical use case

            inputStream = new FileInputStream("test.html");
            doc = DataUtil.load(inputStream, "ISO-8859-1", "");

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // edge case - different charset

            inputStream = new FileInputStream("test.html");
            doc = DataUtil.load(inputStream, "", "");

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // edge case - empty charset

            inputStream = new FileInputStream("test.html");
            doc = DataUtil.load(inputStream, null, "");

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // edge case - null charset

            inputStream = new FileInputStream("test.html");
            doc = DataUtil.load(inputStream, "UTF-8", null);

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // edge case - null baseUri

        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // typical use case

            inputStream = new FileInputStream("test.html");
            doc = DataUtil.load(inputStream, "ISO-8859-1", "", Parser.htmlParser());

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // edge case - different charset

            inputStream = new FileInputStream("test.html");
            doc = DataUtil.load(inputStream, "", "", Parser.htmlParser());

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // edge case - empty charset

            inputStream = new FileInputStream("test.html");
            doc = DataUtil.load(inputStream, null, "", Parser.htmlParser());

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // edge case - null charset

            inputStream = new FileInputStream("test.html");
            doc = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());

            assertNotNull(doc);
            assertEquals("Test File", doc.title());
            // edge case - null baseUri

        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<!DOCTYPE html><html><head><title>Test File</title></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", baseUri, parser);

        assertNotNull(doc);
        assertEquals("Test File", doc.title());
        // typical use case

        doc = DataUtil.parseByteData(byteBuffer, "", baseUri, parser);

        assertNotNull(doc);
        assertEquals("Test File", doc.title());
        // edge case - empty charset

        doc = DataUtil.parseByteData(byteBuffer, null, baseUri, parser);

        assertNotNull(doc);
        assertEquals("Test File", doc.title());
        // edge case - null charset

        doc = DataUtil.parseByteData(byteBuffer, "ISO-8859-1", baseUri, parser);

        assertNotNull(doc);
        assertEquals("Test File", doc.title());
        // edge case - different charset

        byteBuffer = ByteBuffer.wrap("<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=EUC-JP\"></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseByteData(byteBuffer, null, baseUri, parser);

        assertNotNull(doc);
        assertEquals("Test File", doc.title());
        // edge case - charset detected from meta tag

        byteBuffer = ByteBuffer.wrap("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseByteData(byteBuffer, null, baseUri, parser);

        assertNotNull(doc);
        assertEquals("Test File", doc.title());
        // edge case - charset detected from HTML5 meta tag

        byteBuffer = ByteBuffer.wrap("<!DOCTYPE html><html><head><meta charset=\"UTF-32\"></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseByteData(byteBuffer, null, baseUri, parser);

        assertNotNull(doc);
        assertEquals("Test File", doc.title());
        // edge case - charset not supported

        byteBuffer = ByteBuffer.wrap("<!DOCTYPE html><html><head><title>Test File</title></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseByteData(byteBuffer, null, baseUri, parser);

        assertNotNull(doc);
        assertEquals("Test File", doc.title());
        // edge case - no charset specified, default to UTF-8

        byteBuffer = ByteBuffer.wrap("<!DOCTYPE html><html><head><title>Test File</title></head><body></body></html>\uFEFF".getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseByteData(byteBuffer, null, baseUri, parser);

        assertNotNull(doc);
        assertEquals("Test File", doc.title());
        // edge case - remove byte order mark

        byteBuffer = ByteBuffer.wrap("<!DOCTYPE html><html><head><title>Test File</title></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseByteData(byteBuffer, "UTF-8", baseUri, parser);

        assertNotNull(doc);
        assertEquals("Test File", doc.title());
        // edge case - doc is not null

    }


    @Test
    public void testReadToByteBuffer() {
        try {
            FileInputStream fileInputStream = new FileInputStream("test.html");
            int maxSize = 0;
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(fileInputStream, maxSize);

            assertNotNull(byteBuffer);
            // typical use case

            maxSize = 100;
            byteBuffer = DataUtil.readToByteBuffer(fileInputStream, maxSize);

            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.remaining() <= maxSize);
            // edge case - limited size

            maxSize = -1;
            byteBuffer = DataUtil.readToByteBuffer(fileInputStream, maxSize);

            assertNotNull(byteBuffer);
            // edge case - unlimited size

        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        try {
            FileInputStream fileInputStream = new FileInputStream("test.html");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(fileInputStream);

            assertNotNull(byteBuffer);
            // typical use case

        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html;charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("EUC-JP", charset);
        // typical use case

        contentType = null;
        charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
        // edge case - null content type

        contentType = "text/html;charset=UTF-8";
        charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
        // edge case - charset is supported

        contentType = "text/html;charset=INVALID";
        charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
        // edge case - charset is not supported

        contentType = "text/html;";
        charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
        // edge case - charset not defined

        contentType = "text/html;charset=";
        charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
        // edge case - empty charset

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
        // edge case - charset not defined

    }

}