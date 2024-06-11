import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class DataUtilTest {

    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CHARSET_GB2312 = "GB2312";
    private static final String CHARSET_INVALID = "INVALID_CHARSET";
    private static final String BASE_URI = "http://example.com/";

    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private Document doc;
    private Parser parser;

    @Before
    public void setUp() {
        parser = mock(Parser.class);
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File file = new File("test.html");
        InputStream expectedInputStream = new FileInputStream(file);
        byte[] expectedData = "expected data".getBytes();
        expectedInputStream.write(expectedData);
        expectedInputStream.close();

        ByteBuffer expectedByteData = ByteBuffer.wrap(expectedData);
        ByteBuffer actualByteData = DataUtil.readToByteBuffer(new FileInputStream(file));
        assertEquals(expectedByteData, actualByteData);

        Document actualDocument = DataUtil.load(file, CHARSET_UTF8, BASE_URI);
        Document expectedDocument = parser.parseInput(new String(expectedData), BASE_URI);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFileWithDetectedCharset() throws IOException {
        File file = new File("test.html");
        String expectedCharset = CHARSET_GB2312;
        ByteBuffer expectedByteData = ByteBuffer.wrap(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'c', 'h', 'a', 'r', 's', 'e', 't', '=', ' ', CHARSET_GB2312.getBytes(), '>'});
        ByteBuffer actualByteData = DataUtil.readToByteBuffer(new FileInputStream(file));
        assertEquals(expectedByteData, actualByteData);

        Document actualDocument = DataUtil.load(file, null, BASE_URI);
        assertEquals(expectedCharset, actualDocument.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        byte[] data = "expected data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(ByteBuffer.wrap(data), byteData);

        Document document = DataUtil.load(inputStream, CHARSET_UTF8, BASE_URI, parser);
        assertEquals(parser.parseInput(new String(data), BASE_URI), document);
    }

    @Test
    public void testLoadInputStreamWithDetectedCharset() throws IOException {
        byte[] data = ("<html><head><meta charset=" + CHARSET_GB2312 + "></head><body>expected data</body></html>").getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byteData.rewind();
        String charset = new String(Charset.forName(CHARSET_UTF8).decode(byteData)).split("charset=")[1].trim();
        assertEquals(CHARSET_GB2312, charset);

        Document document = DataUtil.load(inputStream, null, BASE_URI, parser);
        assertEquals(CHARSET_GB2312, document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithIOException() throws IOException {
        File file = new File("test.html");
        DataUtil.load(file, CHARSET_UTF8, BASE_URI);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithIOException() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.read()).thenThrow(new IOException());
        DataUtil.load(inputStream, CHARSET_UTF8, BASE_URI, parser);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=" + CHARSET_UTF8;
        assertEquals(CHARSET_UTF8, DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=" + CHARSET_INVALID;
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}