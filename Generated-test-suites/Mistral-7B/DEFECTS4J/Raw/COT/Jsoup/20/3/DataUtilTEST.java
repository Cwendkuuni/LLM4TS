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
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class DataUtilTest {

    private InputStream inputStream;
    private File file;
    private Parser parser;

    @Before
    public void setup() {
        parser = mock(Parser.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        file = new File("testFile.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes());
        FileInputStream fileInputStream = new FileInputStream(file);

        ByteBuffer expectedByteData = ByteBuffer.wrap(fileContent.getBytes());
        Document expectedDocument = DataUtil.parseByteData(expectedByteData, "UTF-8", "", parser);

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        Document actualDocument = DataUtil.load(file, "UTF-8", "", parser);

        assertEquals(expectedDocument, actualDocument);

        fileInputStream.close();
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        file = new File("testFile.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes());
        FileInputStream fileInputStream = new FileInputStream(file);

        ByteBuffer expectedByteData = ByteBuffer.wrap(fileContent.getBytes());
        Document expectedDocument = DataUtil.parseByteData(expectedByteData, "UTF-8", "", parser);

        InputStream inputStream = new FileInputStream(file);
        Document actualDocument = DataUtil.load(file, null, "", parser);

        assertEquals(expectedDocument, actualDocument);

        fileInputStream.close();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        byte[] inputStreamData = inputStreamContent.getBytes();

        ByteBuffer expectedByteData = ByteBuffer.wrap(inputStreamData);
        Document expectedDocument = DataUtil.parseByteData(expectedByteData, "UTF-8", "", parser);

        inputStream = new ByteArrayInputStream(inputStreamData);
        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "", parser);

        assertEquals(expectedDocument, actualDocument);

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        byte[] inputStreamData = inputStreamContent.getBytes();

        ByteBuffer expectedByteData = ByteBuffer.wrap(inputStreamData);
        Document expectedDocument = DataUtil.parseByteData(expectedByteData, "UTF-8", "", parser);

        inputStream = new ByteArrayInputStream(inputStreamData);
        Document actualDocument = DataUtil.load(inputStream, null, "", parser);

        assertEquals(expectedDocument, actualDocument);

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        file = new File("testFile.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes());
        FileInputStream fileInputStream = new FileInputStream(file);

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        try {
            DataUtil.load(file, "ISO-8859-1", "", parser);
        } finally {
            fileInputStream.close();
            inputStream.close();
        }
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        byte[] inputStreamData = inputStreamContent.getBytes();

        InputStream inputStream = new ByteArrayInputStream(inputStreamData);
        try {
            DataUtil.load(inputStream, "ISO-8859-1", "", parser);
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String docData = "<html><head></head><body></body></html>";

        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        Document expectedDocument = parser.parseInput(docData, "");
        Document actualDocument = DataUtil.parseByteData(byteData, null, "", parser);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithCustomCharset() {
        String docData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        String charsetName = "UTF-8";

        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        Document expectedDocument = parser.parseInput(docData, "");
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, "", parser);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        byte[] inputStreamData = inputStreamContent.getBytes();

        ByteBuffer expectedByteData = ByteBuffer.wrap(inputStreamData);
        ByteBuffer actualByteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(inputStreamData));

        assertEquals(expectedByteData, actualByteData);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNullInputStream() throws IOException {
        DataUtil.readToByteBuffer(null);
    }
}