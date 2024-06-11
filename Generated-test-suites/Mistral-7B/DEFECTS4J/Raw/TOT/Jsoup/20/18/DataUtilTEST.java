import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.helper.Validate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        File file = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document expectedDocument = new Parser().parseInput(fileContent, null);
        Document actualDocument = DataUtil.load(file, null, null);

        Assertions.assertEquals(expectedDocument, actualDocument);
        file.delete(); // clean up the test file
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<html>\n<head>\n<meta charset=\"ISO-8859-1\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        File file = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document expectedDocument = new Parser().parseInput(fileContent, null);
        Document actualDocument = DataUtil.load(file, "ISO-8859-1", null);

        Assertions.assertEquals(expectedDocument, actualDocument);
        file.delete(); // clean up the test file
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] input = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);

        Document expectedDocument = new Parser().parseInput(inputStreamContent, null);
        Document actualDocument = DataUtil.load(inputStream, null, null);

        Assertions.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<html>\n<head>\n<meta charset=\"ISO-8859-1\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] input = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);

        Document expectedDocument = new Parser().parseInput(inputStreamContent, null);
        Document actualDocument = DataUtil.load(inputStream, "ISO-8859-1", null);

        Assertions.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><head></head><body><script>document.write('Hello World!')</script></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] input = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);

        Document expectedDocument = new Parser().xmlParser().parseInput(inputStreamContent, null);
        Document actualDocument = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());

        Assertions.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String byteDataContent = "<html><head><meta charset=\"UTF-8\"/></head><body><p>Hello World!</p></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteDataContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        Document expectedDocument = new Parser().parseInput(byteDataContent, null);
        Document actualDocument = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());

        Assertions.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String byteDataContent = "<html><head><meta charset=\"ISO-8859-1\"/></head><body><p>Hello World!</p></body></html>";
        String charsetName = "ISO-8859-1";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteDataContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        Document expectedDocument = new Parser().parseInput(byteDataContent, null);
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());

        Assertions.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testReadToByteBuffer() {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"/></head><body><p>Hello World!</p></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] input = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        ByteBuffer expectedByteData = ByteBuffer.wrap(input);
        ByteBuffer actualByteData = DataUtil.readToByteBuffer(inputStream);

        Assertions.assertEquals(expectedByteData, actualByteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        Assertions.assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        String contentType = "text/html";
        String expectedCharset = null;
        String actualCharset = DataUtil.getCharsetFromContentType(contentType);

        Assertions.assertEquals(expectedCharset, actualCharset);
    }
}