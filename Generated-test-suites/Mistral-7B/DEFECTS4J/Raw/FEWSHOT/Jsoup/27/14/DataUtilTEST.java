package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        File testFile = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document expectedDocument = Jsoup.parse(fileContent);
        Document actualDocument = DataUtil.load(testFile, "UTF-8", "/testBase");

        assertEquals(expectedDocument, actualDocument);

        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputStreamContent.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document expectedDocument = Jsoup.parse(inputStreamContent);
        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "/testBase");

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testDetectCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\n<html>\n<head>\n<meta charset=\"gb2312\"/>\n<title>Test</title>\n</head>\n<body>\n<p>你好，世界！</p>\n</body>\n</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputStreamContent.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document document = DataUtil.load(inputStream, null, "/testBase");

        assertEquals("gb2312", document.outputSettings().charset());
    }

    @Test
    public void testDetectCharsetNull() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputStreamContent.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document document = DataUtil.load(inputStream, null, "/testBase");

        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testDetectCharsetNotFound() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputStreamContent.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document document = DataUtil.load(inputStream, null, "/testBase");

        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes("UTF-8"));

        Document expectedDocument = Jsoup.parse(fileContent);
        Document actualDocument = DataUtil.parseByteData(byteData, "UTF-8", "/testBase", Parser.htmlParser());

        assertEquals(expectedDocument, actualDocument);

        String metaTagContent = "<html>\n<head>\n<meta charset=\"gb2312\"/>\n<title>Test</title>\n</head>\n<body>\n<p>你好，世界！</p>\n</body>\n</html>";
        byteData = ByteBuffer.wrap(metaTagContent.getBytes("UTF-8"));
        String charset = "gb2312";

        expectedDocument = Jsoup.parse(metaTagContent);
        actualDocument = DataUtil.parseByteData(byteData, charset, "/testBase", Parser.htmlParser());

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";

        ByteArrayInputStream expectedInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(expectedInputStream);

        byte[] expectedBytes = inputStreamContent.getBytes();
        byte[] actualBytes = byteData.array();

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=gb2312";
        String expectedCharset = "gb2312";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = "text/html";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }
}