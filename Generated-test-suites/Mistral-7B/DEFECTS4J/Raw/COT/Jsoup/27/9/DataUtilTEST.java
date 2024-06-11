import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Parser;
import org.junit.Assert;
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
        File file = new File("test.html"); // Replace with the actual test file
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        File file = new File("nonexistentfile.html"); // Replace with the actual nonexistent file
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlData = "<html><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlData.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamIOException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DataUtil.load(inputStream, null, null, Parser.htmlParser());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlData = "<html><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(htmlData.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String charsetName = "invalidCharset";
        String baseUri = "http://localhost";
        DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNull(doc);

        String htmlData = "<html><body></body></html>";
        ByteBuffer byteDataWithContentType = ByteBuffer.wrap(("Content-Type: text/html; charset=UTF-8\r\n" + htmlData).getBytes());
        Document docWithContentType = DataUtil.parseByteData(byteDataWithContentType, null, baseUri, Parser.htmlParser());
        Assert.assertNotNull(docWithContentType);

        String htmlDataWithInvalidCharset = "<html><body></body></html>";
        ByteBuffer byteDataWithInvalidCharset = ByteBuffer.wrap(("Content-Type: text/html; charset=invalidCharset\r\n" + htmlDataWithInvalidCharset).getBytes());
        Document docWithInvalidCharset = null;
        try {
            docWithInvalidCharset = DataUtil.parseByteData(byteDataWithInvalidCharset, null, baseUri, Parser.htmlParser());
        } catch (IOException e) {
            // Expected exception
        }
        Assert.assertNull(docWithInvalidCharset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlData = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DataUtil.readToByteBuffer(inputStream);
    }
}