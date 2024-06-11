import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        // add assertions to check the loaded document
    }

    @Test
    public void testLoadInputStreamNullCharset() throws IOException {
        InputStream in = new FileInputStream(new File("test.html"));
        String baseUri = "http://www.example.com";
        try {
            Document doc = DataUtil.load(in, null, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // add assertions to check the exception message
        } finally {
            in.close();
        }
    }

    @Test
    public void testLoadInputStreamEmptyCharset() throws IOException {
        InputStream in = new FileInputStream(new File("test.html"));
        String baseUri = "http://www.example.com";
        try {
            Document doc = DataUtil.load(in, "", baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // add assertions to check the exception message
        } finally {
            in.close();
        }
    }

    @Test
    public void testLoadInputStreamInvalidCharset() throws IOException {
        InputStream in = new FileInputStream(new File("test.html"));
        String baseUri = "http://www.example.com";
        try {
            Document doc = DataUtil.load(in, "invalidCharset", baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // add assertions to check the exception message
        } finally {
            in.close();
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = new FileInputStream(new File("test.html"));
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        // add assertions to check the loaded document
        in.close();
    }

    @Test
    public void testLoadInputStreamNullBaseUri() throws IOException {
        InputStream in = new FileInputStream(new File("test.html"));
        String charsetName = "UTF-8";
        try {
            Document doc = DataUtil.load(in, charsetName, null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // add assertions to check the exception message
        } finally {
            in.close();
        }
    }

    @Test
    public void testParseByteDataValid() {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        // add assertions to check the loaded document
    }

    @Test
    public void testParseByteDataNullCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String baseUri = "http://www.example.com";
        try {
            Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // add assertions to check the exception message
        } finally {
            byteData.clear();
        }
    }

    @Test
    public void testParseByteDataEmptyByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        try {
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // add assertions to check the exception message
        } finally {
            byteData.clear();
        }
    }

    @Test
    public void testParseByteDataInvalidParser() {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        try {
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, new Parser() {
                // unsupported parser implementation
            });
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // add assertions to check the exception message
        } finally {
            byteData.clear();
        }
    }

    @Test
    public void testReadToByteBufferValid() throws IOException {
        InputStream in = new FileInputStream(new File("test.html"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        // add assertions to check the byte buffer
        in.close();
    }

    @Test
    public void testReadToByteBufferNullInputStream() {
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // add assertions to check the exception message
        }
    }

    @Test
    public void testReadToByteBufferUnlimited() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(file));
        long fileSize = file.length();
        byte[] expectedByteData = new byte[(int) fileSize];
        byteData.get(expectedByteData);
        // add assertions to check the byte buffer equals the file content
        byteData.clear();
        file.close();
    }

    @Test
    public void testGetCharsetFromContentTypeValid() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalid() {
        String contentType = "text/html; charset=invalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

}