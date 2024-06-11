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
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.*;

public class DataUtilTest {

    private InputStream inputStream;
    private OutputStream outputStream;
    private File file;

    @Before
    public void setup() {
        inputStream = Mockito.mock(InputStream.class);
        outputStream = Mockito.mock(OutputStream.class);
        file = Mockito.mock(File.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        String text = "Test Document";
        File testFile = new File("test.html");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(text.getBytes());
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        Document document = Mockito.mock(Document.class);

        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.length()).thenReturn(byteBuffer.limit());

        when(inputStream.readAllBytes()).thenReturn(byteBuffer.array());
        when(parser.parseInput(anyString(), anyString())).thenReturn(document);

        Document result = DataUtil.load(file, null, null);
        assertEquals(document, result);
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        File testFile = Mockito.mock(File.class);

        when(testFile.exists()).thenReturn(false);

        DataUtil.load(testFile, null, null);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String text = "Test Document";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        Document document = Mockito.mock(Document.class);

        when(parser.parseInput(anyString(), anyString())).thenReturn(document);

        Document result = DataUtil.load(inputStream, null, null);
        assertEquals(document, result);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamIOException() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);

        when(inputStream.readAllBytes()).thenThrow(new IOException("IOException"));

        DataUtil.load(inputStream, null, null);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String text = "Test Document";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        Document document = Mockito.mock(Document.class);

        when(parser.parseInput(anyString(), anyString())).thenReturn(document);

        Document result = DataUtil.load(inputStream, null, null, parser);
        assertEquals(document, result);
    }

    @Test
    public void testParseByteData() {
        String text = "Test Document";
        ByteBuffer byteBuffer = ByteBuffer.wrap(text.getBytes());
        Document document = Mockito.mock(Document.class);

        when(parser.parseInput(anyString(), anyString())).thenReturn(document);

        Document result = DataUtil.parseByteData(byteBuffer, null, null, parser);
        assertEquals(document, result);
    }

    @Test
    public void testParseByteDataWithCharset() {
        String text = "Test Document";
        String charset = "UTF-8";
        ByteBuffer byteBuffer = ByteBuffer.wrap(text.getBytes(Charset.forName(charset)));
        Document document = Mockito.mock(Document.class);

        when(parser.parseInput(anyString(), anyString())).thenReturn(document);

        Document result = DataUtil.parseByteData(byteBuffer, charset, null, parser);
        assertEquals(document, result);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String text = "Test Document";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        int read = 0;
        byte[] buffer = new byte[1024];

        while (read != -1) {
            read = inputStream.read(buffer);
            byteBuffer.put(buffer, 0, read);
        }

        ByteBuffer result = DataUtil.readToByteBuffer(inputStream);
        assertEquals(byteBuffer, result);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferIOException() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);

        when(inputStream.read(anyByteArray())).thenThrow(new IOException("IOException"));

        DataUtil.readToByteBuffer(inputStream);
    }
}