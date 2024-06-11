import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, StandardCharsets.UTF_8);

        Document doc = DataUtil.load(testFile, "UTF-8", "");
        Assert.assertEquals(fileContent, doc.body().html());

        testFile.delete();
    }

    @Test
    public void testLoadInputStreamNullDocument() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "");
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamEmptyDocument() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(doc);
        Assert.assertEquals(0, doc.childNodeSize());
    }

    @Test
    public void testLoadInputStreamInvalidCharset() throws IOException {
        String invalidCharset = "abcdef";
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test content.".getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.load(inputStream, invalidCharset, "");
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamInvalidInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{-1, -2, -3, -4});
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "");
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamNullDocument() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(0, doc.childNodeSize());
    }

    @Test
    public void testParseInputStreamEmptyDocument() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(0, doc.childNodeSize());
    }

    @Test
    public void testParseInputStreamInvalidCharset() throws IOException {
        String invalidCharset = "abcdef";
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test content.".getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(inputStream, invalidCharset, "", Parser.htmlParser());
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(doc);
    }

    @Test
    public void testParseInputStreamInvalidInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{-1, -2, -3, -4});
        try {
            Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test content.".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertEquals("Test content.", outputStream.toString());
    }
}