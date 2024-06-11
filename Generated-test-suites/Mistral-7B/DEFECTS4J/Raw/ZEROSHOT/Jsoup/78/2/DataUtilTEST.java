import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DataUtil.class)
public class DataUtilTest {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://example.com";
    private static final String XML_DECLARATION = "<?xml version='1.0' encoding='ISO-8859-1'?>";
    private static final String META_CHARSET = "charset='gb2312'";
    private static final String CONTENT_TYPE = "text/html; charset=EUC-JP";

    private Document document;
    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        inputStream = mock(InputStream.class);
        outputStream = mock(OutputStream.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        when(inputStream.available()).thenReturn(file.length());
        when(inputStream.readAllBytes()).thenReturn(new byte[0]);

        Document doc = DataUtil.load(file, CHARSET_NAME, BASE_URI);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        byte[] data = "<html><head></head><body></body></html>".getBytes(StandardCharsets.UTF_8);
        inputStream = new ByteArrayInputStream(data);

        Document doc = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
        assertNotNull(doc);
        assertEquals(data.length, doc.inputStream().available());
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        ByteBuffer bom = ByteBuffer.allocate(3).put(new byte[]{0xEF, 0xBB, 0xBF});

        when(inputStream.markSupported()).thenReturn(true);
        when(inputStream.mark(anyInt())).thenReturn(true);

        DataUtil.parseInputStream(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        inputStream.reset();
        PowerMockito.verifyStatic();
        DataUtil.detectCharsetFromBom(bom);
    }

    @Test
    public void testParseInputStreamWithoutBOM() throws IOException {
        byte[] data = "<?xml version='1.0' encoding='ISO-8859-1'?>".getBytes(StandardCharsets.UTF_8);
        inputStream = new ByteArrayInputStream(data);

        Document doc = DataUtil.parseInputStream(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithUTF8() throws IOException {
        byte[] data = "<?xml version='1.0' encoding='UTF-8'?>".getBytes(StandardCharsets.UTF_8);
        inputStream = new ByteArrayInputStream(data);

        Document doc = DataUtil.parseInputStream(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithUTF8AndMetaCharset() throws IOException {
        byte[] data = "<html><head><meta charset='gb2312'></head><body></body></html>".getBytes(StandardCharsets.UTF_8);
        inputStream = new ByteArrayInputStream(data);

        Document doc = DataUtil.parseInputStream(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("gb2312", doc.charset());
    }

    @Test
    public void testParseInputStreamWithUTF8AndContentType() throws IOException {
        byte[] data = "<html><head></head><body></body></html>".getBytes(StandardCharsets.UTF_8);
        inputStream = new ByteArrayInputStream(data);
        when(inputStream.readLine()).thenReturn(CONTENT_TYPE);

        Document doc = DataUtil.parseInputStream(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("EUC-JP", doc.charset());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        byte[] data = "<html><head></head><body></body></html>".getBytes(StandardCharsets.UTF_8);
        inputStream = new ByteArrayInputStream(data);
        when(inputStream.readLine()).thenReturn("text/html; charset=InvalidCharset");

        DataUtil.parseInputStream(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
    }

    @Test
    public void testCrossStreams() throws IOException {
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);
        InputStream input = new ByteArrayInputStream(data);
        OutputStream output = new ByteArrayOutputStream();

        DataUtil.crossStreams(input, output);
        assertEquals(data, output.toByteArray());
    }
}