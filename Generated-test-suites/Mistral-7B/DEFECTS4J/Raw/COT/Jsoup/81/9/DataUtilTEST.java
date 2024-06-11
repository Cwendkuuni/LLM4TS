import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        String filePath = "path/to/your/file.html"; // Replace with the actual file path
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        try {
            Document doc = DataUtil.load(new File(filePath), charsetName, baseUri);
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to load file");
        }
    }

    @Test
    public void testLoadInputStream() {
        String inputData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to load input stream");
        }
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() {
        String inputData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "invalidCharset";
        String baseUri = "http://www.example.com";

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.assertNull(doc); // The test should fail if the method doesn't return null in this case
        } catch (IOException e) {
            Assert.fail("Unexpected IOException was thrown");
        }
    }

    @Test
    public void testParseInputStreamWithEmptyInput() {
        Document doc = DataUtil.parseInputStream(new ByteArrayInputStream(new byte[0]), null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(0, doc.childNodeSize());
    }

    @Test
    public void testParseInputStreamWithInvalidCharsetDetected() {
        String inputData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://www.example.com";

        try {
            Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            Assert.fail("Unexpected IOException was thrown");
        }

        // Verify the charset was automatically detected and used
        String detectedCharset = doc.outputSettings().charset();
        Assert.assertNotNull(detectedCharset);
        Assert.assertEquals("UTF-8", detectedCharset);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputData = outputStream.toByteArray();
        String outputString = new String(outputData, StandardCharsets.UTF_8);

        Assert.assertEquals(inputData, outputString);
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithIOException() throws IOException {
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Test IOException");
            }
        };

        DataUtil.crossStreams(inputStream, outputStream);
    }
}