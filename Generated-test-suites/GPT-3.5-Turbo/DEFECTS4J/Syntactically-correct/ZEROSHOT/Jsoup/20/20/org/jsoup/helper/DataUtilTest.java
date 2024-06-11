import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataUtilTest {

    private File file;
    private InputStream inputStream;
    private String charsetName;
    private String baseUri;
    private Parser parser;

    @Before
    public void setUp() {
        file = mock(File.class);
        inputStream = mock(InputStream.class);
        charsetName = "UTF-8";
        baseUri = "http://example.com";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        FileInputStream fileInputStream = mock(FileInputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document document = mock(Document.class);
        String docData = "test document data";

        whenNew(FileInputStream.class).withArguments(file).thenReturn(fileInputStream);
        when(DataUtil.readToByteBuffer(fileInputStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(document);

        Document result = DataUtil.load(file, charsetName, baseUri);

        verify(fileInputStream).close();
        assertEquals(document, result);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document document = mock(Document.class);

        when(DataUtil.readToByteBuffer(inputStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(document);

        Document result = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals(document, result);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document document = mock(Document.class);

        when(DataUtil.readToByteBuffer(inputStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(document);

        Document result = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertEquals(document, result);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document document = mock(Document.class);
        Element meta = mock(Element.class);

        when(byteData.rewind()).thenReturn(byteData);
        when(Charset.forName(DataUtil.defaultCharset)).thenReturn(Charset.defaultCharset());
        when(Charset.defaultCharset().decode(byteData)).thenReturn(Charset.defaultCharset().decode(byteData));
        when(parser.parseInput(anyString(), eq(baseUri))).thenReturn(document);
        when(document.select(anyString())).thenReturn(meta);
        when(meta.attr(anyString())).thenReturn(DataUtil.defaultCharset);
        when(Charset.forName(DataUtil.defaultCharset).decode(byteData)).thenReturn(Charset.forName(DataUtil.defaultCharset).decode(byteData));

        Document result = DataUtil.parseByteData(byteData, null, baseUri, parser);

        verify(meta).attr("charset");
        verify(byteData).rewind();
        verify(parser).parseInput(DataUtil.defaultCharset, baseUri);
        verify(document).outputSettings();
        verify(document.outputSettings()).charset(DataUtil.defaultCharset);
        assertEquals(document, result);
    }

    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document document = mock(Document.class);

        when(Charset.forName(charsetName)).thenReturn(Charset.forName(charsetName));
        when(Charset.forName(charsetName).decode(byteData)).thenReturn(Charset.forName(charsetName).decode(byteData));
        when(parser.parseInput(anyString(), eq(baseUri))).thenReturn(document);

        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        verify(parser).parseInput(Charset.forName(charsetName).decode(byteData).toString(), baseUri);
        verify(document).outputSettings();
        verify(document.outputSettings()).charset(charsetName);
        assertEquals(document, result);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] buffer = new byte[DataUtil.bufferSize];
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        int bytesRead = 10;

        when(inputStream.read(buffer)).thenReturn(bytesRead);
        whenNew(ByteArrayOutputStream.class).withArguments(DataUtil.bufferSize).thenReturn(outStream);
        when(outStream.toByteArray()).thenReturn(buffer);
        when(ByteBuffer.wrap(buffer)).thenReturn(byteData);

        ByteBuffer result = DataUtil.readToByteBuffer(inputStream);

        verify(outStream).write(buffer, 0, bytesRead);
        assertEquals(byteData, result);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String expected = "EUC-JP";

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expected, result);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(null, result);
    }
}