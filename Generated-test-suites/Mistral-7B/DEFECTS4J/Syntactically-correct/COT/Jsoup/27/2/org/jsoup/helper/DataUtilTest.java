import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {

    private static Document doc;
    private static Parser parser;
    private static File inputFile;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void setup() {
        parser = mock(Parser.class);
        inputFile = new File("test.html");
    }

    // Test case for load(File, String, String) method
    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document expectedDoc = Mockito.mock(Document.class);
        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDoc);

        Document actualDoc = DataUtil.load(inputFile, charsetName, baseUri);

        assertEquals(expectedDoc, actualDoc);
    }

    // Test case for load(InputStream, String, String) method
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test.html content".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document expectedDoc = Mockito.mock(Document.class);
        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDoc);

        Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals(expectedDoc, actualDoc);
    }

    // Test case for load(InputStream, String, String, Parser) method
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test.html content".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = mock(Parser.class);
        Document expectedDoc = Mockito.mock(Document.class);
        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDoc);

        Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertEquals(expectedDoc, actualDoc);
    }

    // Test case for parseByteData method with null charsetName
    @Test
    public void testParseByteDataWithNullCharsetName() {
        ByteBuffer byteData = ByteBuffer.wrap("test.html content".getBytes());
        String baseUri = "http://test.com";
        Parser parser = mock(Parser.class);
        Document expectedDoc = Mockito.mock(Document.class);

        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDoc);

        Document actualDoc = DataUtil.parseByteData(byteData, null, baseUri, parser);

        assertNull(actualDoc);
    }

    // Test case for parseByteData method with valid charsetName
    @Test
    public void testParseByteDataWithValidCharsetName() {
        ByteBuffer byteData = ByteBuffer.wrap("test.html content".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = mock(Parser.class);
        Document expectedDoc = Mockito.mock(Document.class);

        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDoc);

        Document actualDoc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals(expectedDoc, actualDoc);
    }

    // Test case for readToByteBuffer method
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test.html content".getBytes());
        ByteBuffer expectedByteData = ByteBuffer.wrap("test.html content".getBytes());
        ByteBuffer actualByteData = DataUtil.readToByteBuffer(inputStream);

        assertEquals(expectedByteData, actualByteData);
    }

    // Test case for getCharsetFromContentType method
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, actualCharset);
    }

    // Test case for getCharsetFromContentType method with no charset specified
    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String expectedCharset = null;
        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, actualCharset);
    }

    // Test case for load(File, null, null) method with IOException
    @Test
    public void testLoadFileWithIOException() {
        expectedException.expect(IOException.class);

        DataUtil.load(inputFile, null, null);
    }

    // Test case for load(InputStream, null, null) method with IOException
    @Test
    public void testLoadInputStreamWithIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test.html content".getBytes());

        expectedException.expect(IOException.class);

        DataUtil.load(inputStream, null, null);
    }

    // Test case for parseByteData method with null byteData
    @Test
    public void testParseByteDataWithNullByteData() {
        ByteBuffer byteData = null;
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = mock(Parser.class);
        Document expectedDoc = Mockito.mock(Document.class);

        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDoc);

        Document actualDoc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNull(actualDoc);
    }
}