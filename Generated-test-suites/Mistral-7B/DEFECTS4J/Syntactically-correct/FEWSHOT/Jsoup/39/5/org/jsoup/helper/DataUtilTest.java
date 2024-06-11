package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseByteData() {
        // Test with default charset
        String docData = "This is a test.";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName("UTF-8")));
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(docData, doc.body().text());

        // Test with specified charset
        String charset = "ISO-8859-1";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName("UTF-8")));
        doc = DataUtil.parseByteData(byteData, charset, null, Parser.htmlParser());
        assertEquals(docData, doc.body().text());

        // Test with meta charset tag
        String docDataWithMeta = "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "This is a test.\n" +
                "</body>\n" +
                "</html>";
        byteData = ByteBuffer.wrap(docDataWithMeta.getBytes(Charset.forName("UTF-8")));
        doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(docData, doc.body().text());

        // Test with Content-Type header
        String contentType = "text/html; charset=ISO-8859-1";
        byteData = ByteArray.getsByteBufferFromString(docDataWithMeta, Charset.forName("UTF-8"));
        doc = DataUtil.parseByteData(byteData, contentType, null, Parser.htmlParser());
        assertEquals(docData, doc.body().text());

        // Test with invalid charset
        String invalidCharset = "invalidCharset";
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, invalidCharset, null, Parser.htmlParser()));

        // Test with UTF-8 BOM
        String docDataWithBOM = "\uFEFF" + docData;
        byteData = ByteBuffer.wrap(docDataWithBOM.getBytes(Charset.forName("UTF-8")));
        doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(docData, doc.body().text());

        // Test with empty input
        byteData = ByteBuffer.allocate(0);
        assertNull(DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()));
    }

    @Test
    public void testLoad() throws IOException {
        String testFilePath = "src/test/resources/test.html";
        File testFile = new File(testFilePath);

        // Test with default charset
        Document doc = DataUtil.load(testFile, null, null);
        assertEquals(docDataWithMeta.length(), doc.body().text().length());

        // Test with specified charset
        String charset = "ISO-8859-1";
        doc = DataUtil.load(testFile, charset, null);
        assertEquals(docDataWithMeta.length(), doc.body().text().length());

        // Test with invalid charset
        String invalidCharset = "invalidCharset";
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(testFile, invalidCharset, null));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testFilePath = "src/test/resources/test.html";
        File testFile = new File(testFilePath);
        InputStream in = new FileInputStream(testFile);

        // Test with default charset
        Document doc = DataUtil.load(in, null, null);
        assertEquals(docDataWithMeta.length(), doc.body().text().length());

        // Test with specified charset
        String charset = "ISO-8859-1";
        doc = DataUtil.load(in, charset, null);
        assertEquals(docDataWithMeta.length(), doc.body().text().length());

        // Test with invalid charset
        String invalidCharset = "invalidCharset";
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(in, invalidCharset, null));

        // Test with specified parser
        Parser parser = Parser.xmlParser();
        doc = DataUtil.load(in, null, null, parser);
        assertEquals(docDataWithMeta.length(), doc.body().text().length());
    }

    @Test
    public void testLoadInputStreamWithCharsetDetected() throws IOException {
        String testFilePath = "src/test/resources/test_charset.html";
        File testFile = new File(testFilePath);
        InputStream in = new FileInputStream(testFile);

        Document doc = DataUtil.load(in, null, null);
        assertEquals(docDataWithMeta.length(), doc.body().text().length());

        Element meta = doc.selectFirst("meta[charset]");
        assertSame(meta, doc.selectFirst("meta[http-equiv=content-type][content*='text/html']"));

        String charset = null;
        if (meta != null) {
            charset = meta.attr("charset");
        }

        assertFalse(charset.isEmpty());
        assertEquals(charset, "ISO-8859-1");

        doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(in), charset, null, Parser.htmlParser());
        assertEquals(docDataWithMeta.length(), doc.body().text().length());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testFilePath = "src/test/resources/test.html";
        File testFile = new File(testFilePath);
        InputStream in = new FileInputStream(testFile);

        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[bufferSize];
        int read;

        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }

        byte[] expectedByteData = out.toByteArray();
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(expectedByteData);

        assertArrayEquals(expectedByteBuffer.array(), byteData.array());
        assertTrue(byteData.hasArray());
        assertEquals(expectedByteBuffer.limit(), byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=gb2312";
        assertEquals("gb2312", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=euc-jp";
        assertEquals("euc-jp", DataUtil.getCharsetFromContentType(contentType));

        contentType = null;
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    private static ByteBuffer getByteBufferFromString(String data, Charset charset) {
        return ByteBuffer.wrap(charset.decode(new ByteArrayInputStream(data.getBytes(charset))));
    }

    private static String docDataWithMeta = "<html lang=\"en\">\n" +
            "<head>\n" +
            "<meta charset=\"UTF-8\">\n" +
            "</head>\n" +
            "<body>\n" +
            "This is a test.\n" +
            "</body>\n" +
            "</html>";
}