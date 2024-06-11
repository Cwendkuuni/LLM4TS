import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, expectedCharset, baseUri);

        // Test with correct charset in the file
        InputStream expectedInputStream = new FileInputStream(file);
        ByteBuffer byteData = DataUtil.readToByteBuffer(expectedInputStream);
        Document expectedDoc = DataUtil.parseByteData(byteData, expectedCharset, baseUri, Parser.htmlParser());
        Assert.assertEquals(expectedDoc, doc);

        // Test with incorrect charset in the file
        expectedCharset = "UTF-16";
        doc = DataUtil.load(file, expectedCharset, baseUri);
        byteData = DataUtil.readToByteBuffer(new FileInputStream(file));
        expectedDoc = DataUtil.parseByteData(byteData, "UTF-8", baseUri, Parser.htmlParser()); // assuming the default charset is UTF-8
        Assert.assertNotEquals(expectedDoc, doc);

        // Test with empty file
        File emptyFile = new File("empty.html");
        doc = DataUtil.load(emptyFile, expectedCharset, baseUri);
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName(expectedCharset)));

        Document doc = DataUtil.load(inputStream, expectedCharset, baseUri);
        Document expectedDoc = Parser.htmlParser().parseInput(htmlContent, baseUri);
        Assert.assertEquals(expectedDoc, doc);

        // Test with incorrect charset in the input stream
        expectedCharset = "UTF-16";
        doc = DataUtil.load(inputStream, expectedCharset, baseUri);
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithAlternateParser() throws IOException {
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        String xmlContent = "<xml><message>Hello World</message></xml>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(Charset.forName(expectedCharset)));

        Document doc = DataUtil.load(inputStream, expectedCharset, baseUri, Parser.xmlParser());
        Document expectedDoc = Parser.xmlParser().parseInput(xmlContent, baseUri);
        Assert.assertEquals(expectedDoc, doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("test.html");
        DataUtil.load(file, null, "");
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullBaseUri() throws IOException {
        File file = new File("test.html");
        DataUtil.load(file, "UTF-8", null);
    }
}