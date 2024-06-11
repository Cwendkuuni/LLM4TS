import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File file = new File("testFile.html");
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();

        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(fileContent, doc.body().text());

        DataUtil.load(file, null, null); // Test default charset

        File.delete(file.getAbsolutePath());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(inputStreamContent.getBytes());
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        Document doc = DataUtil.load(is, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String inputStreamContent = "<html><body><h1>Test Title</h1></body></html>";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(inputStreamContent.getBytes());
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        Document doc = DataUtil.load(is, "UTF-8", null, Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("<h1>Test Title</h1>", doc.select("h1").text());
    }

    @Test(expected = IOException.class)
    public void testLoadNullFile() throws IOException {
        Document doc = DataUtil.load(null, null, null);
        Assert.fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void testLoadNegativeFileSize() throws IOException {
        File file = new File("testFile.html");
        if (file.exists()) {
            file.delete();
        }

        Document doc = DataUtil.load(file, -1, null);
        Assert.fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void testLoadInvalidCharset() throws IOException {
        File file = new File("testFile.html");
        if (file.exists()) {
            file.delete();
        }

        Document doc = DataUtil.load(file, "InvalidCharset", null);
        Assert.fail("Expected IOException");
    }

    @Test
    public void testParseByteData() {
        String charsetName = "UTF-8";
        String baseUri = null;
        String docData = "<html><body><h1>Test Title</h1></body></html>";

        ByteBuffer byteData = ByteBuffer.allocate(docData.length());
        byteData.put(docData.getBytes());
        byteData.flip();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("<h1>Test Title</h1>", doc.select("h1").text());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String charsetName = null;
        String baseUri = null;
        String docData = "<html lang='ja' charset='Shift_JIS'><body><h1>Test Title</h1></body></html>";

        ByteBuffer byteData = ByteBuffer.allocate(docData.length());
        byteData.put(docData.getBytes());
        byteData.flip();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("<h1>Test Title</h1>", doc.select("h1").text());
        Assert.assertEquals("Shift_JIS", doc.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithEmptyByteData() {
        String charsetName = "UTF-8";
        String baseUri = null;
        ByteBuffer byteData = ByteBuffer.allocate(0);

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.fail("Expected IOException");
    }

    // Add more test cases for other methods in the DataUtil class as needed

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(inputStreamContent.getBytes());
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        ByteBuffer byteData = DataUtil.readToByteBuffer(is);
        Assert.assertNotNull(byteData);
        Assert.assertEquals(inputStreamContent.length(), byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNullContentType() {
        String contentType = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharsetFound() {
        String contentType = "text/html; charset=";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }
}