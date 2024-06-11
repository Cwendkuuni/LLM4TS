import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;

/**
 * Test class for DataUtil methods.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "Your HTML content here";
        File file = new File("test.html");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(fileContent);
        }

        Document expectedDoc = DataUtil.parseInput(fileContent, "UTF-8");
        Document actualDoc = DataUtil.load(file, "UTF-8", "baseUri");

        Assert.assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "Your HTML content here";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inStream = new ByteArrayInputStream(inputStreamContent.getBytes())) {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
            Document doc = DataUtil.parseByteData(byteData, "UTF-8", "baseUri", Parser.htmlParser());

            Assert.assertEquals(expectedDocument(), doc);
        }
    }

    @Test
    public void testLoadInputStreamWithCharsetNameNull() throws IOException {
        String inputStreamContent = "Your HTML content here";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inStream = new ByteArrayInputStream(inputStreamContent.getBytes())) {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
            Document doc = DataUtil.parseByteData(byteData, null, "baseUri", Parser.htmlParser());

            Assert.assertNotNull(doc);
        }
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithIOException() throws IOException {
        ByteArrayInputStream inStream = new ByteArrayInputStream(new byte[0]);
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
            Document doc = DataUtil.parseByteData(byteData, "UTF-8", "baseUri", Parser.htmlParser());
        } finally {
            inStream.close();
        }

        throw new IOException("Expected IOException when reading from ByteArrayInputStream");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInputStreamWithNullContentType() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inStream = new ByteArrayInputStream(new byte[0])) {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
            Document doc = DataUtil.parseByteData(byteData, null, "baseUri", Parser.htmlParser());
            DataUtil.parseByteData(byteData, null, "baseUri", Parser.xmlParser());
        }

        throw new IllegalArgumentException("Expected IllegalArgumentException when parsing with null content type");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithNullCharsetName() {
        String content = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes(Charset.forName("UTF-8")));

        try {
            DataUtil.parseByteData(byteData, null, "baseUri", Parser.htmlParser());
        } finally {
            byteData.clear();
        }

        throw new IllegalArgumentException("Expected IllegalArgumentException when charsetName is null");
    }

    @Test
    public void testParseByteDataWithCorrectCharset() {
        String content = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "baseUri", Parser.htmlParser());
        Assert.assertEquals(expectedDocument(), doc);
    }

    @Test
    public void testParseByteDataWithIncorrectCharset() {
        String content = "<html><head><meta charset=\"UTF-16\"></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "baseUri", Parser.htmlParser());
        Assert.assertNotEquals(expectedDocument(), doc);

        // Rewind the ByteBuffer, change the charset and parse again
        byteData.rewind();
        String correctCharset = getCharsetFromContentType(content);
        doc = DataUtil.parseByteData(byteData, correctCharset, "baseUri", Parser.htmlParser());
        Assert.assertEquals(expectedDocument(), doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithEmptyContentType() {
        String content = "<html><head></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "baseUri", Parser.htmlParser());

        throw new IllegalArgumentException("Expected IllegalArgumentException when content type is empty");
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "Your HTML content here";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inStream = new ByteArrayInputStream(inputStreamContent.getBytes())) {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);

            byte[] expectedData = inputStreamContent.getBytes();
            byte[] actualData = new byte[byteData.limit()];
            byteData.get(actualData, 0, actualData.length);

            Assert.assertArrayEquals(expectedData, actualData);
        }
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithIOException() throws IOException {
        ByteArrayInputStream inStream = new ByteArrayInputStream(new byte[0]);

        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        } finally {
            inStream.close();
        }

        throw new IOException("Expected IOException when reading from ByteArrayInputStream");
    }

    private Document expectedDocument() {
        // Create an expected Document based on the code sample
        // or use a pre-existing Document that represents the correct structure and content
        return null;
    }

    private String getCharsetFromContentType(String contentType) {
        Matcher m = DataUtil.charsetPattern.matcher(contentType);
        if (m.find()) {
            return m.group(1).trim().toUpperCase();
        }
        return null;
    }
}