import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private DataUtil dataUtil;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = dataUtil.load(testFile, charsetName, baseUri);

        assertEquals("Test document", document.body().text(), readFile("test.html"));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test document".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = dataUtil.load(inputStream, charsetName, baseUri);

        assertEquals("Test document", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test document".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = org.jsoup.parser.Jsoup.htmlParser();

        Document document = dataUtil.load(inputStream, charsetName, baseUri, parser);

        assertEquals("Test document", document.body().text());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test document".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = dataUtil.parseInputStream(inputStream, charsetName, baseUri);

        assertEquals("Test document", document.body().text());
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test document".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = org.jsoup.parser.Jsoup.htmlParser();

        Document document = dataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        assertEquals("Test document", document.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testString = "Test document";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        dataUtil.crossStreams(inputStream, outputStream);

        assertEquals(testString, outputStream.toString(StandardCharsets.UTF_8.name()));
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File testFile = new File("test.html");
        String charsetName = "invalidCharset";
        String baseUri = "http://test.com";

        assertNull(dataUtil.load(testFile, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test document".getBytes(StandardCharsets.UTF_8));
        String charsetName = "invalidCharset";
        String baseUri = "http://test.com";

        assertNull(dataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.allocate(4);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0xFF);

        BomCharset bomCharset = dataUtil.detectCharsetFromBom(byteData);

        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        byteData.rewind();
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0xFF);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);

        bomCharset = dataUtil.detectCharsetFromBom(byteData);

        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        byteData.rewind();
        byteData.put((byte) 0xEF);
        byteData.put((byte) 0xBB);
        byteData.put((byte) 0xBF);

        bomCharset = dataUtil.detectCharsetFromBom(byteData);

        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(true, bomCharset.offset);
    }

    private String readFile(String fileName) {
        try {
            InputStream inputStream = new FileInputStream(new File(fileName));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 1024);
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }

            reader.close();
            inputStream.close();

            return stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}