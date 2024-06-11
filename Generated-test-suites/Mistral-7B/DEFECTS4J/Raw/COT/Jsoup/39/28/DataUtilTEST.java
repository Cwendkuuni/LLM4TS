import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws IOException {
        File file = Mockito.mock(File.class);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document expectedDocument = Mockito.mock(Document.class);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("<!-- html content -->".getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Mockito.when(file.exists()).thenReturn(true);
        Mockito.when(file.length()).thenReturn(outputStream.size());
        Mockito.when(file.getAbsolutePath()).thenReturn("test.html");

        DataUtil dataUtil = new DataUtil();
        Document actualDocument = dataUtil.load(file, charsetName, baseUri);

        Mockito.verify(file).exists();
        Mockito.verify(file).length();
        Mockito.verify(file).getAbsolutePath();
        Mockito.verify(expectedDocument).equals(actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadFileDocumentWithIOException() throws IOException {
        File file = Mockito.mock(File.class);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Mockito.when(file.exists()).thenReturn(false);

        DataUtil dataUtil = new DataUtil();
        dataUtil.load(file, charsetName, baseUri);

        Mockito.verify(file).exists();
    }

    @Test
    public void testLoadInputStreamDocument() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document expectedDocument = Mockito.mock(Document.class);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("<!-- html content -->".getBytes());
        InputStream inputStreamWithContent = new ByteArrayInputStream(outputStream.toByteArray());

        Mockito.when(inputStream.available()).thenReturn(outputStream.size());

        DataUtil dataUtil = new DataUtil();
        Document actualDocument = dataUtil.load(inputStream, charsetName, baseUri);

        Mockito.verify(inputStream).available();
        Mockito.verify(expectedDocument).equals(actualDocument);
    }

    @Test
    public void testLoadInputStreamDocumentWithNullCharset() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        String charsetName = null;
        String baseUri = "http://test.com";
        Document expectedDocument = Mockito.mock(Document.class);

        DataUtil dataUtil = new DataUtil();
        Document actualDocument = dataUtil.load(inputStream, charsetName, baseUri);

        Mockito.verifyNoMoreInteractions(inputStream);
        Mockito.verify(expectedDocument).equals(actualDocument);
    }

    @Test
    public void testLoadInputStreamDocumentWithUnsupportedCharset() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        String charsetName = "UNSUPPORTED_CHARSET";
        String baseUri = "http://test.com";
        Document expectedDocument = Mockito.mock(Document.class);

        DataUtil dataUtil = new DataUtil();
        Document actualDocument = dataUtil.load(inputStream, charsetName, baseUri);

        Mockito.verifyNoMoreInteractions(inputStream);
        Mockito.verify(expectedDocument).equals(actualDocument);
    }

    @Test
    public void testParseByteDataDocument() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Mockito.mock(Parser.class);
        Document expectedDocument = Mockito.mock(Document.class);

        byte[] byteArray = "<!-- html content -->".getBytes();
        byteData.put(byteArray);
        byteData.flip();

        Mockito.when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDocument);

        DataUtil dataUtil = new DataUtil();
        Document actualDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Mockito.verify(parser).parseInput(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(expectedDocument).equals(actualDocument);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataDocumentWithIOException() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Mockito.mock(Parser.class);

        DataUtil dataUtil = new DataUtil();
        dataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Mockito.verifyNoMoreInteractions(parser);
        // IOException is expected to be thrown by readToByteBuffer method.
    }
}