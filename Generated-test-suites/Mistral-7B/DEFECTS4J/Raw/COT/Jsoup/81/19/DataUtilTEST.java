import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static Document mockedDocument;
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final int FIRST_READ_BUFFER_SIZE = 1024 * 5;
    private static final int BUFFER_SIZE = 1024 * 32;

    @BeforeClass
    public static void setup() {
        mockedDocument = new Document("baseUri") {
            @Override
            public String getBaseURI() {
                return "baseUri";
            }

            @Override
            public String toString() {
                return "mockedDocument";
            }
        };
    }

    @Test
    public void testLoadFromFileWithValidCharsetAndBaseUri() throws IOException {
        File file = new File("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "baseUri";

        Document actualDocument = DataUtil.load(file, charsetName, baseUri);

        Assert.assertEquals(mockedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadFromFileWithInvalidCharsetName() throws IOException {
        File file = new File("path/to/file.html");
        String charsetName = "INVALID_CHARSET";
        String baseUri = "baseUri";

        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadFromInputStreamWithValidCharsetAndBaseUri() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("mockedContent".getBytes(Charset.forName(DEFAULT_CHARSET)));
        String charsetName = "UTF-8";
        String baseUri = "baseUri";

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

        Assert.assertEquals(mockedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamWithNullInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "baseUri";

        DataUtil.load(null, charsetName, baseUri);
    }

    @Test
    public void testLoadFromInputStreamWithNullCharsetName() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("mockedContent".getBytes(Charset.forName(DEFAULT_CHARSET)));
        String charsetName = null;
        String baseUri = "baseUri";

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

        Assert.assertEquals(mockedDocument, actualDocument);
    }

    @Test
    public void testLoadFromInputStreamWithEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String charsetName = "UTF-8";
        String baseUri = "baseUri";

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

        Assert.assertEquals(new Document(baseUri), actualDocument);
    }

    @Test
    public void testParseInputStreamWithValidCharsetName() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream("mockedContent".getBytes(Charset.forName(DEFAULT_CHARSET)));
        String baseUri = "baseUri";
        Parser parser = Parser.htmlParser();

        Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        Assert.assertEquals(mockedDocument, actualDocument);
    }

    @Test(expected = UncheckedIOException.class)
    public void testParseInputStreamWithIOException() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]); // empty input stream
        String baseUri = "baseUri";
        Parser parser = Parser.htmlParser();

        DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("mockedContent".getBytes(Charset.forName(DEFAULT_CHARSET)));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertArrayEquals("mockedContent".getBytes(), outputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullInputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(null, outputStream);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF});
        BomCharset actualBomCharset = DataUtil.detectCharsetFromBom(byteData);

        Assert.assertEquals("UTF-8", actualBomCharset.charset);
        Assert.assertTrue(actualBomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0x00, 0x01, 0x02, 0x03});
        BomCharset actualBomCharset = DataUtil.detectCharsetFromBom(byteData);

        Assert.assertNull(actualBomCharset);
    }
}