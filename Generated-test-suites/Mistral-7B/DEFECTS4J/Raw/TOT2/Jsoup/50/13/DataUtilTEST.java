import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document expectedDoc = Mockito.mock(Document.class);

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        Document actualDoc = DataUtil.load(file, charsetName, baseUri);

        Mockito.verify(expectedDoc).outputSettings().charset(charsetName);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document expectedDoc = Mockito.mock(Document.class);

        InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri);

        Mockito.verify(expectedDoc).outputSettings().charset(charsetName);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Mockito.mock(Parser.class);
        Document expectedDoc = Mockito.mock(Document.class);

        InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
        Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri, parser);

        Mockito.verify(parser).parseInput(Mockito.anyString(), baseUri);
        Mockito.verify(expectedDoc).outputSettings().charset(charsetName);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://test.com";
        DataUtil.load(file, null, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://test.com";
        String charsetName = "INVALID_CHARSET";

        try {
            DataUtil.load(file, charsetName, baseUri);
        } catch (IllegalCharsetNameException e) {
            throw new IOException(e);
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = Mockito.mock(InputStream.class);
        OutputStream out = Mockito.mock(OutputStream.class);

        DataUtil.crossStreams(in, out);

        Mockito.verify(in, Mockito.times(1)).read(Mockito.anyByteArray());
        Mockito.verify(out, Mockito.times(1)).write(Mockito.anyByteArray());
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Mockito.mock(Parser.class);
        Document expectedDoc = Mockito.mock(Document.class);
        ByteBuffer byteData = DataUtil.emptyByteBuffer();

        String docData = "<html><head></head><body>Test</body></html>";
        byteData.put(docData.getBytes());
        byteData.flip();

        Document actualDoc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Mockito.verify(parser).parseInput(Mockito.eq(docData), baseUri);
        Mockito.verify(expectedDoc).outputSettings().charset(charsetName);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testParseByteDataWithBOM() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Mockito.mock(Parser.class);
        Document expectedDoc = Mockito.mock(Document.class);
        ByteBuffer byteData = DataUtil.emptyByteBuffer();

        String docData = "\uFEFF<html><head></head><body>Test</body></html>";
        byteData.put(docData.getBytes());
        byteData.flip();

        Document actualDoc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Mockito.verify(parser).parseInput(Mockito.eq(docData.substring(1)), baseUri);
        Mockito.verify(expectedDoc).outputSettings().charset(charsetName);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://test.com";
        Parser parser = Mockito.mock(Parser.class);
        Document expectedDoc = Mockito.mock(Document.class);
        ByteBuffer byteData = DataUtil.emptyByteBuffer();

        String docData = "<html><head></head><body>Test</body></html>";

        try {
            DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        } catch (IllegalCharsetNameException e) {
            throw new IOException(e);
        }

        Mockito.verify(parser, Mockito.never()).parseInput(Mockito.anyString(), baseUri);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = Mockito.mock(InputStream.class);
        ByteBuffer expectedByteBuffer = DataUtil.emptyByteBuffer();

        Mockito.when(inStream.read(Mockito.anyByteArray())).thenReturn(123);

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inStream);

        Mockito.verify(inStream, Mockito.times(1)).read(Mockito.anyByteArray());
        Assert.assertEquals(expectedByteBuffer.capacity(), actualByteBuffer.capacity());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inStream = Mockito.mock(InputStream.class);
        ByteBuffer expectedByteBuffer = ByteBuffer.allocate(1024);

        Mockito.when(inStream.read(Mockito.anyByteArray())).thenReturn(1024);

        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inStream, 1024);

        Mockito.verify(inStream, Mockito.times(1)).read(Mockito.anyByteArray());
        Assert.assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNegativeMaxSize() throws IOException {
        InputStream inStream = Mockito.mock(InputStream.class);
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        DataUtil.readToByteBuffer(inStream, -1);
    }
}