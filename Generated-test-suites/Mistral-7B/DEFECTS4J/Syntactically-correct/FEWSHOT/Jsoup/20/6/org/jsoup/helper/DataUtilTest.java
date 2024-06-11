package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseByteData() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" lang=\"en\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test data.</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.xmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" lang=\"en\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test data.</p>\n" +
                "</body>\n" +
                "</html>", doc.html());

        byteData.rewind();
        Element meta = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser()).select("meta[http-equiv=content-type]").first();
        String foundCharset = meta != null ? meta.attr("content").split(";")[1].trim().toUpperCase() : null;
        assertEquals("UTF-8", foundCharset);

        byteData = ByteBuffer.wrap("<?xml version=\"1.0\" encoding=\"gb2312\"?>\n".getBytes());
        doc = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
        assertEquals("gb2312", doc.outputSettings().charset());
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" lang=\"\">\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "</body>\n" +
                "</html>", doc.html());
    }

    @Test
    public void testLoadFromFileWithNullCharset() throws IOException {
        File file = new File("src/test/resources/testData.html");
        Document doc = DataUtil.load(file, null);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFromFileWithSpecifiedCharset() throws IOException {
        File file = new File("src/test/resources/testData.html");
        Document doc = DataUtil.load(file, "gb2312");
        assertEquals("gb2312", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFromInputStreamWithNullCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" lang=\"en\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test data.</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream in = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(in, null);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" lang=\"en\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Test data.</p>\n" +
                "</body>\n" +
                "</html>", doc.html());
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\n" +
                "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" lang=\"\">\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "</body>\n" +
                "</html>";

        InputStream in = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(in, "gb2312");
        assertEquals("gb2312", doc.outputSettings().charset());
        assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" lang=\"\">\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "</body>\n" +
                "</html>", doc.html());
    }

    @Test
    public void testLoadFromInputStreamWithNullBaseUri() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" lang=\"en\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                "<base href=\"/test/\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<a href=\"/test/testData.html\">Test data</a>\n" +
                "</body>\n" +
                "</html>";

        InputStream in = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(in, null, null);
        String expectedLink = "/test/testData.html";
        assertEquals(expectedLink, doc.select("a").attr("href"));
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedBaseUri() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" lang=\"en\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                "<base href=\"/test/\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<a href=\"testData.html\">Test data</a>\n" +
                "</body>\n" +
                "</html>";

        InputStream in = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(in, null, "/test/");
        String expectedLink = "testData.html";
        assertEquals(expectedLink, doc.select("a").attr("href"));

        doc = DataUtil.load(in, null, "/test2/");
        assertEquals("", doc.select("a").attr("href"));
    }

    @Test
    public void testLoadFromFileWithNullBaseUri() throws IOException {
        File file = new File("src/test/resources/testDataWithBaseUri.html");
        Document doc = DataUtil.load(file, null);
        String expectedLink = "/testData.html";
        assertEquals(expectedLink, doc.select("a").attr("href"));
    }

    @Test
    public void testLoadFromFileWithSpecifiedBaseUri() throws IOException {
        File file = new File("src/test/resources/testDataWithBaseUri.html");
        Document doc = DataUtil.load(file, "/test/");
        String expectedLink = "testData.html";
        assertEquals(expectedLink, doc.select("a").attr("href"));

        doc = DataUtil.load(file, "/test2/");
        assertEquals("", doc.select("a").attr("href"));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html";
        String contentType3 = "application/xml; charset=gb2312";

        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType1));
        assertNull(DataUtil.getCharsetFromContentType(contentType2));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType(contentType3));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test data.";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ByteBuffer byteData;

        InputStream inStream = new ByteArrayInputStream(testData.getBytes());
        byteData = DataUtil.readToByteBuffer(inStream);

        byte[] buffer = new byte[byteData.limit()];
        byteData.get(buffer);
        assertEquals(testData, new String(buffer));

        outStream.write(testData.getBytes());
        byteData = DataUtil.readToByteBuffer(new InputStreamReader(new BufferedInputStream(outStream.toInputStream()), Charset.forName("UTF-8")));
        assertEquals(testData, Charset.forName("UTF-8").decode(byteData).toString());

        byteData.rewind();
        assertEquals(testData, Charset.forName("UTF-8").decode(byteData).toString());
    }
}