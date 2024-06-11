import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private DataUtil dataUtil;
    private Parser parser;
    private Document document;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataUtil = new DataUtil();
        parser = DataUtil.Parser.htmlParser();
    }

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String fileContent = "This is a test.";
        File file = new File("test.html");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(fileContent.getBytes("UTF-8"));
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document expectedDocument = parser.parseInput(fileContent, "");

        Document actualDocument = dataUtil.load(file, "UTF-8", "");

        assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "This is a test.";
        File file = new File("test.html");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(fileContent.getBytes("UTF-8"));
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes("UTF-8"));

        Document expectedDocument = parser.parseInput(fileContent, "");

        Document actualDocument = dataUtil.parseByteData(byteData, null, "", parser);

        assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String inputStreamContent = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        Document expectedDocument = parser.parseInput(inputStreamContent, "");

        Document actualDocument = dataUtil.load(inputStream, "UTF-8", "");

        assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));

        ByteBuffer byteData = ByteBuffer.wrap(inputStreamContent.getBytes("UTF-8"));

        Document expectedDocument = parser.parseInput(inputStreamContent, "");

        Document actualDocument = dataUtil.parseByteData(byteData, null, "", parser);

        assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    @Test
    public void testParseByteDataWithValidCharset() throws IOException {
        String fileContent = "This is a test.";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes("UTF-8"));

        Document expectedDocument = parser.parseInput(fileContent, "");

        Document actualDocument = dataUtil.parseByteData(byteData, "UTF-8", "", parser);

        assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String fileContent = "This is a test.";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes("UTF-8"));

        Document expectedDocument = parser.parseInput(fileContent, "");

        String charset = "GBK";
        Document actualDocument = dataUtil.parseByteData(byteData, charset, "", parser);

        assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(inputStreamContent.getBytes("UTF-8"));

        ByteBuffer actualByteBuffer = dataUtil.readToByteBuffer(inputStream);

        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String expectedCharset = "UTF-8";
        String actualCharset = dataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalidCharset";

        String expectedCharset = null;
        String actualCharset = dataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, actualCharset);
    }
}