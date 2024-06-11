import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParsedText;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String filePath = "test.html"; // Replace with the actual path to the test HTML file
        String charsetName = "UTF-8"; // Replace with the actual charset of the test HTML file if known
        String baseUri = "http://example.com"; // Replace with a base URI if necessary

        Document document = DataUtil.load(new File(filePath), charsetName, baseUri);

        // Check if the document is not null
        Assert.assertNotNull(document);

        // Check if the document has expected elements
        Element titleElement = document.title();
        Assert.assertNotNull(titleElement);
        Assert.assertEquals("Test Title", titleElement.text());

        // Check if the document can be parsed from an InputStream
        InputStream inputStream = new FileInputStream(new File(filePath));
        Document documentFromInputStream = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        inputStream.close();
        Assert.assertEquals(document, documentFromInputStream);
    }

    @Test
    public void testLoadDocumentFromInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        InputStream inputStream = new FileInputStream(new File("test.html"));
        try {
            Document document = DataUtil.load(inputStream, charsetName, null);
            Assert.fail(); // Expected IOException
        } catch (IOException e) {
            Assert.assertEquals("Error parsing input stream: Unknown charset: InvalidCharset", e.getMessage());
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        InputStream inputStream = new FileInputStream(new File("test.html"));
        try {
            DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
            Assert.fail(); // Expected IOException
        } catch (IOException e) {
            Assert.assertEquals("Error parsing input stream: Unknown charset: InvalidCharset", e.getMessage());
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFile = "input.txt";
        String outputFile = "output.txt";

        File input = new File(inputFile);
        File output = new File(outputFile);

        InputStream inputStream = new FileInputStream(input);
        OutputStream outputStream = new FileOutputStream(output);

        DataUtil.crossStreams(inputStream, outputStream);

        // Check if the contents of the input and output files are the same
        byte[] inputBytes = new byte[input.length()];
        byte[] outputBytes = new byte[output.length()];

        inputStream.read(inputBytes, 0, input.length());
        inputStream.close();

        byte[] expectedBytes = inputBytes.clone();

        output.getBytes(0, outputBytes, 0, output.length());
        output.deleteOnExit();

        Assert.assertArrayEquals(expectedBytes, outputBytes);
    }
}