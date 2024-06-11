import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataUtilTest {

    @Test
    public void testLoad_file_Charset_BaseUri() throws IOException {
        File file = mock(File.class);
        FileInputStream fileInputStream = mock(FileInputStream.class);
        when(fileInputStream.read()).thenReturn(-1);
        when(file.length()).thenReturn((long) DataUtil.bufferSize);

        when(fileInputStream.read(new byte[DataUtil.bufferSize])).thenReturn(-1);
        when(new FileInputStream(file)).thenReturn(fileInputStream);

        Document document = DataUtil.load(file, "UTF-8", "http://example.com");

        verify(file).length();
        verify(new FileInputStream(file)).read(new byte[DataUtil.bufferSize]);
    }

    @Test
    public void testLoad_inputStream_Charset_BaseUri() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read()).thenReturn(-1);

        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");

        verify(inputStream).read(new byte[DataUtil.bufferSize]);
    }

    @Test
    public void testLoad_inputStream_Charset_BaseUri_Parser() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read()).thenReturn(-1);

        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());

        verify(inputStream).read(new byte[DataUtil.bufferSize]);
    }

    @Test
    public void testParseByteData_CharsetName_BaseUri_Parser() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer_InputStream() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read()).thenReturn(-1);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        verify(inputStream).read(new byte[DataUtil.bufferSize]);
    }

    @Test
    public void testGetCharsetFromContentType_ContentTytpe() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }
}