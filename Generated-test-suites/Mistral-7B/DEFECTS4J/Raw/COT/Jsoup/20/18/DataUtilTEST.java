import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@PowerMockIgnore({"javax.activation.*", "javax.mail.*"})
@RunWith(PowerMockRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        File file = new File("testFile.html");
        Document expectedDocument = Mockito.mock(Document.class);
        PowerMockito.when(expectedDocument.outputSettings().charset()).thenReturn(Charset.forName(charsetName));

        Document actualDocument = DataUtil.load(file, charsetName, baseUri);
        Assert.assertEquals(expectedDocument, actualDocument);

        PowerMockito.verifyStatic(Parser.class).htmlParser();
    }

    @Test
    public void testLoadInputStreamCharsetDetected() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        String charsetName = "gb2312";
        String baseUri = "http://test.com";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(DataUtil.bufferSize);
        byte[] expectedBytes = "expectedData".getBytes(Charset.forName(charsetName));
        outputStream.write(expectedBytes);
        PowerMockito.when(inputStream.readAllBytes()).thenReturn(expectedBytes);

        Document actualDocument = DataUtil.load(inputStream, null, baseUri);
        Assert.assertNotNull(actualDocument);

        PowerMockito.verifyStatic(Parser.class).parseInput(Mockito.anyString(), Mockito.eq(baseUri));
    }

    @Test
    public void testLoadInputStreamCharsetProvided() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(DataUtil.bufferSize);
        byte[] expectedBytes = "expectedData".getBytes(Charset.forName(charsetName));
        outputStream.write(expectedBytes);
        PowerMockito.when(inputStream.readAllBytes()).thenReturn(expectedBytes);

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(actualDocument);

        PowerMockito.verifyStatic(Parser.class).parseInput(Mockito.anyString(), Mockito.eq(baseUri), Mockito.eq(Parser.htmlParser()));
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        File file = new File("fileThatDoesNotExist.html");

        DataUtil.load(file, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamIOException() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        PowerMockito.when(inputStream.read()).thenReturn(-1);

        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(15);
        byteData.put("expectedData".getBytes(Charset.forName("UTF-8")));
        byteData.flip();
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Mockito.mock(Parser.class);
        Document expectedDocument = Mockito.mock(Document.class);

        PowerMockito.when(parser.parseInput(Mockito.anyString(), Mockito.eq(baseUri))).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataCharsetDetected() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(30);
        byte[] expectedBytes = "expectedData<meta charset=gb2312>".getBytes();
        byteData.put(expectedBytes);
        byteData.flip();
        String charsetName = null;
        String baseUri = "http://test.com";
        Parser parser = Mockito.mock(Parser.class);

        PowerMockito.when(parser.parseInput(Mockito.anyString(), Mockito.eq(baseUri))).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertEquals(expectedDocument, actualDocument);

        PowerMockito.verifyStatic(DataUtil.class).getCharsetFromContentType(DataUtil.parseByteDataContentType(byteData));
    }

    @Test
    public void testParseByteDataCharsetProvided() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(30);
        byte[] expectedBytes = "expectedData".getBytes(Charset.forName("UTF-8"));
        byteData.put(expectedBytes);
        byteData.flip();
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Mockito.mock(Parser.class);

        PowerMockito.when(parser.parseInput(Mockito.anyString(), Mockito.eq(baseUri), Mockito.eq(Parser.htmlParser()))).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(DataUtil.bufferSize);

        PowerMockito.when(inputStream.readAllBytes()).thenReturn(new byte[]{"test1", "test2"});

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(new byte[]{"test1", "test2"});

        Assert.assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;

        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNull(actualCharset);
    }
}