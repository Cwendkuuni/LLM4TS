import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, null, "");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithNullCharset() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        try {
            Document doc = DataUtil.load(inputStream, null, "");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadFromInputStreamWithEmptyCharset() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        try {
            Document doc = DataUtil.load(inputStream, "", "");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadFromInputStreamWithCustomParser() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Parser parser = org.jsoup.parser.Jsoup.xmlParser();
        Document doc = DataUtil.load(inputStream, "UTF-8", "", parser);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        try {
            Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseByteDataWithEmptyCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        try {
            Document doc = DataUtil.parseByteData(byteData, "", "", Parser.htmlParser());
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithClosedInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        try {
            DataUtil.readToByteBuffer(inputStream);
            inputStream.close();
            DataUtil.readToByteBuffer(inputStream);
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        Assert.assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        Assert.assertNull(DataUtil.getCharsetFromContentType("text/html"));
    }

}