import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class DataUtilTest {

    private InputStream inputStream;
    private Document document;
    private Parser parser;

    @Before
    public void setup() {
        parser = mock(Parser.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("<html><head><meta charset='UTF-8'/></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        InputStream expectedInput = new ByteArrayInputStream(outputStream.toByteArray());

        Mockito.when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(createDocument());

        document = DataUtil.load(file, charsetName, baseUri);

        assertEquals("Expected file to be loaded correctly", expectedInput, DataUtil.getInputStreamFromDocument(document));
        assertNotNull("Document should not be null", document);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = null;
        String baseUri = "http://test.com";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("<html><head><meta charset='UTF-8'/></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        InputStream expectedInput = new ByteArrayInputStream(outputStream.toByteArray());

        Mockito.when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(createDocument());

        try {
            document = DataUtil.load(file, charsetName, baseUri);
            assert false; // Test should fail because of IOException
        } catch (IOException e) {
            // Expected
        }

        assertEquals("Expected file to be loaded correctly if charset is provided or detected", expectedInput, DataUtil.getInputStreamFromDocument(document));
        assertNotNull("Document should not be null", document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><head><meta charset='UTF-8'/></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        ByteBuffer byteData = ByteBuffer.allocate(4096);
        DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertEquals("Expected charset to be used when provided", charsetName, parser.parserInputCharset());
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><head><meta charset='UTF-8'/></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://test.com";

        ByteBuffer byteData = ByteBuffer.allocate(4096);
        DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertEquals("Expected UTF-8 to be used as default charset when not provided", "UTF-8", parser.parserInputCharset());
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><head><meta charset='UTF-8'/></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser customParser = mock(Parser.class);

        ByteBuffer byteData = ByteBuffer.allocate(4096);
        DataUtil.load(inputStream, charsetName, baseUri, customParser);

        Mockito.verify(customParser).parseInput(Mockito.anyString(), Mockito.anyString());
    }

    private Document createDocument() {
        return new Document();
    }

    private InputStream getInputStreamFromDocument(Document document) {
        return new ByteArrayInputStream(document.html().getBytes(StandardCharsets.UTF_8));
    }

}