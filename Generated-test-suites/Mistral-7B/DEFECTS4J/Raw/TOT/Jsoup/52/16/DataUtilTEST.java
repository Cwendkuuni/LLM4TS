import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputFile, expectedCharset, baseUri);

        // Typical use case: Verify the document is loaded correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals(expectedCharset, doc.outputSettings().charset());

        // Edge case: Verify the document is loaded even if charset is not found in the header or meta tag
        File inputFileWithNoCharset = new File("test.html_no_charset.html");
        Document docWithNoCharset = DataUtil.load(inputFileWithNoCharset, null, baseUri);
        Assert.assertNotNull(docWithNoCharset);
        Assert.assertEquals(DataUtil.defaultCharset, docWithNoCharset.outputSettings().charset());

        // Error case: Verify an IOException is thrown if the file does not exist
        File nonExistentFile = new File("non_existent_file.html");
        try {
            DataUtil.load(nonExistentFile, expectedCharset, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputData = "<html><head><meta charset='UTF-8'></head><body><div>Test</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputStream, expectedCharset, baseUri);

        // Typical use case: Verify the document is loaded correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals(expectedCharset, doc.outputSettings().charset());

        // Edge case: Verify the document is loaded even if charset is not specified
        String inputDataWithNoCharset = "<html><head></head><body><div>Test</div></body></html>";
        InputStream inputStreamWithNoCharset = new ByteArrayInputStream(inputDataWithNoCharset.getBytes());
        Document docWithNoCharset = DataUtil.load(inputStreamWithNoCharset, null, baseUri);
        Assert.assertNotNull(docWithNoCharset);
        Assert.assertEquals(DataUtil.defaultCharset, docWithNoCharset.outputSettings().charset());

        // Error case: Verify an IOException is thrown if the input stream is empty
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            DataUtil.load(emptyInputStream, expectedCharset, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithAlternateParser() throws IOException {
        String inputData = "<xml version='1.0' encoding='UTF-8'><root><data>Test</data></root>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputStream, expectedCharset, baseUri, DataUtil.Parser.xmlParser());

        // Typical use case: Verify the document is loaded correctly
        Assert.assertNotNull(doc);
        Assert.assertEquals(expectedCharset, doc.outputSettings().charset());

        // Edge case: Verify the document is loaded even if charset is not found in the header or meta tag
        String inputDataWithNoCharset = "<xml version='1.0'><root><data>Test</data></root>";
        InputStream inputStreamWithNoCharset = new ByteArrayInputStream(inputDataWithNoCharset.getBytes());
        Document docWithNoCharset = DataUtil.load(inputStreamWithNoCharset, null, baseUri, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(docWithNoCharset);
        Assert.assertEquals(DataUtil.defaultCharset, docWithNoCharset.outputSettings().charset());

        // Error case: Verify an IOException is thrown if the input stream is empty
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            DataUtil.load(emptyInputStream, expectedCharset, baseUri, DataUtil.Parser.xmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Test";
        byte[] inputDataBytes = inputData.getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputDataBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        // Typical use case: Verify the input stream is fully read and written to the output stream
        byte[] outputDataBytes = outputStream.toByteArray();
        Assert.assertArrayEquals(inputDataBytes, outputDataBytes);

        // Edge case: Verify the method works with unlimited input and output streams
        ByteBuffer unlimitedInputBuffer = DataUtil.emptyByteBuffer();
        ByteBuffer unlimitedOutputBuffer = DataUtil.emptyByteBuffer();
        byte[] unlimitedInputData = new byte[1024 * 1024]; // 1MB
        System.arraycopy(inputDataBytes, 0, unlimitedInputData, 0, inputDataBytes.length);

        InputStream unlimitedInputStream = new ByteArrayInputStream(unlimitedInputData);
        OutputStream unlimitedOutputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(unlimitedInputStream, unlimitedOutputStream);

        byte[] unlimitedOutputDataBytes = unlimitedOutputStream.toByteArray();
        Assert.assertArrayEquals(unlimitedInputData, unlimitedOutputDataBytes);
    }
}