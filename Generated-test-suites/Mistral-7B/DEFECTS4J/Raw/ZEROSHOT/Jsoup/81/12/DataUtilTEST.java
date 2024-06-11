import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws Exception {
        File inputFile = new File("path/to/your/test.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", "");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStream() throws Exception {
        String testData = "Test data for InputStream test.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(doc);
        Assert.assertEquals(testData, doc.body().wholeText());
    }

    @Test
    public void testParseInputStreamWithParser() throws Exception {
        String testData = "Test data for InputStream test.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(testData, doc.body().wholeText());
    }

    @Test
    public void testCrossStreams() throws Exception {
        File inputFile = new File("path/to/your/input.html");
        File outputFile = new File("path/to/your/output.html");
        Document inputDoc = DataUtil.load(inputFile, "UTF-8", "");
        Document outputDoc = new Document();

        // Write the input document to the output document
        DataUtil.crossStreams(inputDoc.body().outputStream(), outputDoc.inputStream());

        // Check if the documents are equal
        Assert.assertEquals(inputDoc.body().wholeText(), outputDoc.body().wholeText());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer bomBytes = ByteBuffer.wrap(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00});
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(bomBytes);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-16", bomCharset.charset);
        Assert.assertFalse(bomCharset.offset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=GB2312";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNotNull(charset);
        Assert.assertEquals("GB2312", charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        Assert.assertEquals(charset, DataUtil.validateCharset(charset));

        String invalidCharset = "UTF-8\"";
        Assert.assertNull(DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), 32);
    }
}