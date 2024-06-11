import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, "UTF-8", "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamNullCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, null, "");
        Assert.assertNotNull(document);

        Assert.assertTrue(document.outputSettings().charset().toString().equals("UTF-8"));
    }

    @Test
    public void testLoadInputStreamUtf8Charset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(document);

        Assert.assertTrue(document.outputSettings().charset().toString().equals("UTF-8"));
    }

    @Test
    public void testLoadInputStreamInvalidCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        try {
            Document document = DataUtil.load(inputStream, "ISO-8859-1", "");
            Assert.fail("Expected IOException to be thrown.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamNoCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        try {
            Document document = DataUtil.load(inputStream, null, "");
            Assert.fail("Expected IOException to be thrown.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamEmptyContent() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ByteArrayInputStream inStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertTrue(document.outputSettings().charset().toString().equals("UTF-8"));
    }

    @Test
    public void testParseByteDataDefaultCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ByteArrayInputStream inStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertTrue(document.outputSettings().charset().toString().equals("UTF-8"));
    }

    @Test
    public void testParseByteDataInvalidCharset() throws IOException {
        String gb2312Content = "<html><head><meta charset=\"GB2312\"></head><body></body></html>";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ByteArrayInputStream inStream = new ByteArrayInputStream(gb2312Content.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        Assert.assertNull(document);

        document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertTrue(document.outputSettings().charset().toString().equals("GB2312"));
    }

    @Test
    public void testParseByteDataNoCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ByteArrayInputStream inStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertTrue(document.outputSettings().charset().toString().equals("UTF-8"));
    }

    @Test
    public void testParseByteDataBOM() throws IOException {
        String utf8BOM = "\uFEFF" + "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ByteArrayInputStream inStream = new ByteArrayInputStream(utf8BOM.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertTrue(document.outputSettings().charset().toString().equals("UTF-8"));
    }
}