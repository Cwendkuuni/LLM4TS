import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "Some content of a file";
        File file = new File("sample.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes());
        Document expectedDocument = new Parser().parseInput(fileContent, "");

        Document actualDocument = DataUtil.load(file, "UTF-8", "");

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.title(), actualDocument.title());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("sample.html");
        DataUtil.load(file, null, "");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "Some content of an input stream";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        Document expectedDocument = new Parser().parseInput(inputStreamContent, "");

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "");

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.title(), actualDocument.title());
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String inputStreamContent = "Some content of an input stream";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        DataUtil.load(inputStream, null, "");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "Some content of an input stream";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        Document expectedDocument = new Parser().xmlParser().parseInput(inputStreamContent, "");

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.title(), actualDocument.title());
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String fileContent = "Some content of a file with charset=" + charsetName;
        File file = new File("sample.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes(Charset.forName(charsetName)));
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());

        Document expectedDocument = new Parser().parseInput(fileContent, "");

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, "", Parser.htmlParser());

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.title(), actualDocument.title());
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(0);

        Document actualDocument = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());

        Assert.assertNull(actualDocument);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "Some content of an input stream";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(inputStreamContent.getBytes());

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);

        Assert.assertArrayEquals(expectedByteBuffer.array(), actualByteBuffer.array());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNull(actualCharset);
    }

}