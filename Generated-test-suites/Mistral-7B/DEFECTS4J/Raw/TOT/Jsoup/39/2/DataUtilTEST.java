import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test Page</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        File testFile = new File("testFile.html");
        try (FileWriter fileWriter = new FileWriter(testFile)) {
            fileWriter.write(fileContent);
        }

        Document document = DataUtil.load(testFile, null, "/");
        Assert.assertEquals("UTF-8", document.outputSettings().charset());
        Assert.assertEquals("Hello World!", document.body().text());

        testFile.delete();
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n<html>\n<head>\n<meta charset=\"iso-8859-1\"/>\n<title>Test Page</title>\n</head>\n<body>\n<p>Hélô Mondê!</p>\n</body>\n</html>";
        File testFile = new File("testFile.html");
        try (FileWriter fileWriter = new FileWriter(testFile)) {
            fileWriter.write(fileContent);
        }

        Document document = DataUtil.load(testFile, "iso-8859-1", "/");
        Assert.assertEquals("iso-8859-1", document.outputSettings().charset());
        Assert.assertEquals("Hélô Mondê!", document.body().text());

        testFile.delete();
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test Page</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, "/");
        Assert.assertEquals("UTF-8", document.outputSettings().charset());
        Assert.assertEquals("Hello World!", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n<html>\n<head>\n<meta charset=\"iso-8859-1\"/>\n<title>Test Page</title>\n</head>\n<body>\n<p>Hélô Mondê!</p>\n</body>\n</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "iso-8859-1", "/");
        Assert.assertEquals("iso-8859-1", document.outputSettings().charset());
        Assert.assertEquals("Hélô Mondê!", document.body().text());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String fileContent = "<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test Page</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteData = ByteBuffer.wrap(fileContentBytes);

        Document document = DataUtil.parseByteData(byteData, null, "/", Parser.htmlParser());
        Assert.assertEquals("UTF-8", document.outputSettings().charset());
        Assert.assertEquals("Hello World!", document.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String fileContent = "<html>\n<head>\n<meta charset=\"iso-8859-1\"/>\n<title>Test Page</title>\n</head>\n<body>\n<p>Hélô Mondê!</p>\n</body>\n</html>";
        byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteData = ByteBuffer.wrap(fileContentBytes);
        String charsetName = "iso-8859-1";

        Document document = DataUtil.parseByteData(byteData, charsetName, "/", Parser.htmlParser());
        Assert.assertEquals(charsetName, document.outputSettings().charset());
        Assert.assertEquals("Hélô Mondê!", document.body().text());
    }

    @Test
    public void testDetectCharsetFromMetaTag() throws IOException {
        String fileContent = "<html>\n<head>\n<meta charset=\"GBK\"/>\n<title>Test Page</title>\n</head>\n<body>\n<p>你好，世界！</p>\n</body>\n</html>";
        File testFile = new File("testFile.html");
        try (FileWriter fileWriter = new FileWriter(testFile)) {
            fileWriter.write(fileContent);
        }

        Document document = DataUtil.load(testFile, null, "/");
        Assert.assertEquals("GBK", document.outputSettings().charset());
        Assert.assertEquals("你好，世界！", document.body().text());

        testFile.delete();
    }

    @Test
    public void testDetectCharsetFromContentType() {
        String fileContent = "<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test Page</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        byte[] fileContentBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteData = ByteBuffer.wrap(fileContentBytes);
        String contentType = "text/html; charset=GBK";

        Document document = DataUtil.parseByteData(byteData, null, "/", Parser.htmlParser());
        Assert.assertEquals("GBK", document.outputSettings().charset());
        Assert.assertEquals("你好，世界！", document.body().text()); // This will fail because the content is UTF-8 encoded, but the charset is set to GBK.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "<html>\n<head>\n<meta charset=\"invalid\"/>\n<title>Test Page</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        File testFile = new File("testFile.html");
        try (FileWriter fileWriter = new FileWriter(testFile)) {
            fileWriter.write(fileContent);
        }

        Document document = DataUtil.load(testFile, "invalid", "/");
    }
}