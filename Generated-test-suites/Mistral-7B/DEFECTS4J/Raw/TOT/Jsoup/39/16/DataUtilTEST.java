import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        String htmlContent = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head><body></body></html>";
        File file = new File("test.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(htmlContent);
        fileWriter.close();

        Document expectedDocument = Parser.htmlParser().parseInput(htmlContent, "/baseUri");

        Document actualDocument = DataUtil.load(file, "UTF-8", "/baseUri");

        assertSame(expectedDocument, actualDocument);

        // Close the file
        file.delete();
    }

    @Test
    public void testLoadFromFileWithDefaultCharset() throws IOException {
        String htmlContent = "<html><head><meta charset='gb2312'></head><body></body></html>";
        File file = new File("test.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(htmlContent);
        fileWriter.close();

        Document expectedDocument = Parser.htmlParser().parseInput(htmlContent, "/baseUri");

        Document actualDocument = DataUtil.load(file, null, "/baseUri");

        assertSame(expectedDocument, actualDocument);

        // Close the file
        file.delete();
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String htmlContent = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));

        Document expectedDocument = Parser.htmlParser().parseInput(htmlContent, "/baseUri");

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "/baseUri");

        assertSame(expectedDocument, actualDocument);

        inputStream.close();
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><head><meta charset='gb2312'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("gb2312"));

        Document expectedDocument = Parser.htmlParser().parseInput(htmlContent, "/baseUri");

        Document actualDocument = DataUtil.load(inputStream, null, "/baseUri");

        assertSame(expectedDocument, actualDocument);

        inputStream.close();
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String htmlContent = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Parser parser = Mockito.mock(Parser.class);

        Document expectedDocument = Parser.htmlParser().parseInput(htmlContent, "/baseUri");

        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "/baseUri", parser);

        assertSame(expectedDocument, actualDocument);

        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteBuffer);

        byte[] expectedBytes = htmlContent.getBytes("UTF-8");
        byte[] actualBytes = ByteBuffer.allocate(expectedBytes.length).put(byteBuffer).array();

        assertArrayEquals(expectedBytes, actualBytes);

        inputStream.close();
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String htmlContent = "<html><head><meta charset='gb2312'></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlContent.getBytes("gb2312"));

        Document expectedDocument = Parser.htmlParser().parseInput(htmlContent, "/baseUri");

        Document actualDocument = DataUtil.parseByteData(byteBuffer, null, "/baseUri", Parser.htmlParser());

        assertSame(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String htmlContent = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));

        Document expectedDocument = Parser.htmlParser().parseInput(htmlContent, "/baseUri");

        Document actualDocument = DataUtil.parseByteData(byteBuffer, "UTF-8", "/baseUri", Parser.htmlParser());

        assertSame(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String htmlContentWithUTF8 = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head><body></body></html>";
        String htmlContentWithGB2312 = "<html><head><meta charset='gb2312'></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlContentWithUTF8.getBytes("UTF-8"));

        Document expectedDocument = Parser.htmlParser().parseInput(htmlContentWithUTF8, "/baseUri");

        Document actualDocument = DataUtil.parseByteData(byteBuffer, null, "/baseUri", Parser.htmlParser());

        assertSame(expectedDocument, actualDocument);

        byteBuffer.rewind();
        byteBuffer.put(htmlContentWithGB2312.getBytes("gb2312"));
        byteBuffer.flip();

        Document detectedCharsetDocument = DataUtil.parseByteData(byteBuffer, null, "/baseUri", Parser.htmlParser());

        assertEquals(expectedDocument, detectedCharsetDocument);
    }
}