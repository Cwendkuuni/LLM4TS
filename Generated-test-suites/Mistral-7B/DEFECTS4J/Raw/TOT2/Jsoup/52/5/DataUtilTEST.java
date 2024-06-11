import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // assuming test.html is in the classpath
        String expectedCharset = "UTF-8";
        Document document = DataUtil.load(file, null, null);
        Assert.assertNotNull(document);
        Assert.assertEquals(expectedCharset, document.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "ISO-8859-1";
        Document document = DataUtil.load(file, charsetName, null);
        Assert.assertNotNull(document);
        Assert.assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset='UTF-8'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String expectedCharset = "UTF-8";
        Document document = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(document);
        Assert.assertEquals(expectedCharset, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String html = "<html><head><meta charset='ISO-8859-1'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "ISO-8859-1";
        Document document = DataUtil.load(inputStream, charsetName, null);
        Assert.assertNotNull(document);
        Assert.assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head><meta charset='UTF-8'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Parser parser = DataUtil.Parser.xmlParser();
        Document document = DataUtil.load(inputStream, null, null, parser);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharsetName() throws IOException {
        File file = new File("test.html");
        String charsetName = "InvalidCharset";
        DataUtil.load(file, charsetName, null);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String html = "<html><head><meta charset='UTF-8'></head><body></body></html>";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put(html.getBytes(Charset.forName("UTF-8")));
        byteData.flip();
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithNullParser() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        Document document = DataUtil.parseByteData(byteData, null, null, null);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expectedInput = "Input data".getBytes();
        byte[] expectedOutput = "Input data".getBytes();
        byte[] inputData = new byte[expectedInput.length];
        byte[] outputData = new byte[expectedOutput.length];

        int read = inputStream.read(inputData);
        Assert.assertEquals(expectedInput.length, read);

        outputStream.write(expectedOutput);
        outputStream.close();

        InputStream outputStream2 = new FileInputStream(outputFile);
        byte[] outputData2 = new byte[expectedOutput.length];
        int read2 = outputStream2.read(outputData2);
        Assert.assertEquals(expectedOutput.length, read2);

        Assert.assertArrayEquals(expectedOutput, outputData2);
    }
}