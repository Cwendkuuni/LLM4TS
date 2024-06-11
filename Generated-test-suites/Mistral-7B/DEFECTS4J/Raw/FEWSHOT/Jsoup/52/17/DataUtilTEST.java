package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        File testFile = new File("testFile.xml");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document doc = DataUtil.load(testFile, "UTF-16", null);
        assertEquals("UTF-16", doc.outputSettings().charset());
        assertEquals("<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", doc.html());

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputStreamContent.getBytes("UTF-8"));
        InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document doc = DataUtil.load(testInputStream, "UTF-16", null);
        assertEquals("UTF-16", doc.outputSettings().charset());
        assertEquals("<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", doc.html());
    }

    @Test
    public void testParseByteData() {
        String charsetName = null;
        String documentContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        ByteBuffer byteData = ByteBuffer.wrap(documentContent.getBytes("UTF-8"));

        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", doc.html());

        charsetName = "UTF-16";
        byteData = ByteBuffer.wrap(documentContent.getBytes("UTF-8"));
        doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("UTF-16", doc.outputSettings().charset());
        assertEquals("<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", doc.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);

        byte[] expectedData = inputStreamContent.getBytes("UTF-8");
        assertEquals(expectedData.length, byteData.limit());
        byte[] readData = new byte[expectedData.length];
        byteData.get(readData);
        assertArrayEquals(expectedData, readData);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        File testFile = new File("testFile.xml");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(testFile);
        String readContent = Charset.forName("UTF-8").decode(byteData).toString();
        assertEquals(fileContent, readContent);

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-16";
        String expectedCharset = "UTF-16";
        assertEquals(expectedCharset, DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-16";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-16BE";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8";
        assertEquals("UTF-8", DataUtil.validateCharset(charset));

        charset = "invalidCharset";
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertTrue(boundary.length() >= 32);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0x00, 0x00});
        assertEquals("UTF-16LE", DataUtil.detectCharsetFromBom(byteData, null));

        byteData = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0x00, 0x00});
        assertEquals("UTF-16BE", DataUtil.detectCharsetFromBom(byteData, null));

        byteData = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF});
        assertEquals("UTF-8", DataUtil.detectCharsetFromBom(byteData, null));

        byteData = ByteBuffer.wrap(new byte[]{0x00, 0x00, 0xFE, 0xFF});
        assertNull(DataUtil.detectCharsetFromBom(byteData, null));
    }
}