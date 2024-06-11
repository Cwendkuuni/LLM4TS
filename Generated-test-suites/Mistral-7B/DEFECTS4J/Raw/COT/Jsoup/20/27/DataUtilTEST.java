import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
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

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "test.html"; // replace with valid file path
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        File file = new File(filePath);
        Document document = DataUtil.load(file, charsetName, baseUri);

        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamNullCharset() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("Testing".getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Assert.assertThrows(IOException.class, () -> DataUtil.load(inputStream, null, ""));
    }

    @Test
    public void testLoadInputStreamEmptyCharset() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("Testing".getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Assert.assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, "", ""));
    }

    @Test
    public void testLoadInputStreamInvalidCharset() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("Testing\u00E9ting".getBytes()); // a character that's not present in UTF-8
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Assert.assertThrows(IOException.class, () -> DataUtil.load(inputStream, "UTF-8", ""));
    }

    @Test
    public void testParseByteDataNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Assert.assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, null, baseUri, parser));
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String testHtml = "<html lang=\"en\"><head><meta charset=\"gb2312\"></head><body></body></html>";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testHtml.getBytes("UTF-8"));

        Document document = DataUtil.parseByteData(DataUtil.readToByteBuffer(byteArrayInputStream), null, "", Parser.htmlParser());
        Assert.assertEquals("gb2312", document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBufferNullInputStream() {
        Assert.assertThrows(NullPointerException.class, () -> DataUtil.readToByteBuffer(null));
    }

    @Test
    public void testReadToByteBufferEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byteBuffer.rewind();
        String testString = Charset.forName("UTF-8").decode(byteBuffer).toString();
        Assert.assertEquals("", testString);
    }

    @Test
    public void testGetCharsetFromContentTypeNullContentType() {
        Assert.assertNull(DataUtil.getCharsetFromContentType(null));
    }

    @Test
    public void testGetCharsetFromContentTypeValidContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidContentType() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }
}