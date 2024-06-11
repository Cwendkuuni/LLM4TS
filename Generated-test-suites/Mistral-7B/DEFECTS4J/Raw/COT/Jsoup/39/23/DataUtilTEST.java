import org.jsoup.helper.DataUtil;
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
import java.nio.charset.IllegalCharsetNameException;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "Some content here.";
        File file = new File("test.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document expectedDocument = new HtmlDocument();
        expectedDocument.setDocumentElement("html", null);
        expectedDocument.getDocumentElement().setTagName("html");
        expectedDocument.getDocumentElement().appendText(fileContent);

        Document actualDocument = DataUtil.load(file, null, null);
        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.toString(), actualDocument.toString());

        file.delete();
    }

    @Test
    public void testLoadInputStreamNullCharset() throws IOException {
        String content = "Some content here.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        Document actualDocument = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(actualDocument);

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamValidCharset() throws IOException {
        String content = "Some content here.";
        String charset = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName(charset)));

        Document actualDocument = DataUtil.load(inputStream, charset, null);
        Assert.assertNotNull(actualDocument);

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamInvalidCharset() throws IOException {
        String content = "Some content here.";
        String charset = "invalidCharset";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        DataUtil.load(inputStream, charset, null);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String content = "Some content here.";
        String charset = "UTF-8";
        Parser parser = Parser.htmlParser();
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName(charset)));

        Document actualDocument = DataUtil.load(inputStream, charset, null, parser);
        Assert.assertNotNull(actualDocument);

        inputStream.close();
    }

    @Test
    public void testParseByteDataNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document actualDocument = DataUtil.parseByteData(byteData, null, baseUri, parser);
        Assert.assertNull(actualDocument);
    }

    @Test
    public void testParseByteDataValidCharset() throws IOException {
        String content = "Some content here.";
        String charset = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName(charset)));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        inputStream.close();
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document actualDocument = DataUtil.parseByteData(byteData, charset, baseUri, parser);
        Assert.assertNotNull(actualDocument);

        byteData.rewind();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "Some content here.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(content.getBytes());
        Assert.assertEquals(expectedByteBuffer, actualByteBuffer);

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferMaxSize() throws IOException {
        String content = "Some content here.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        int maxSize = 10;

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(content.substring(0, maxSize).getBytes());
        Assert.assertEquals(expectedByteBuffer, actualByteBuffer);

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferUnlimited() throws IOException {
        String content = "Some content here.";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(content.getBytes());
        Assert.assertEquals(expectedByteBuffer, actualByteBuffer);

        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidCharset() {
        String contentType = "text/html; charset=invalid";
        String expectedCharset = null;

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeNullContentType() {
        String expectedCharset = null;
        String contentType = null;

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, actualCharset);
    }
}