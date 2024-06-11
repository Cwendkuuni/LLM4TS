package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, fileContent, StandardCharsets.UTF_8);

        Document doc = DataUtil.load(file, null, null);
        assertEquals("UTF-16", doc.outputSettings().charset());
        assertEquals("<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>", doc.html());

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("UTF-16", doc.outputSettings().charset());
        assertEquals("<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>", doc.html());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertEquals("<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>", doc.xml().toString());
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String fileContent = "Test Data";
        File inputFile = new File("inputFile.txt");
        File outputFile = new File("outputFile.txt");
        FileUtils.writeStringToFile(inputFile, fileContent, StandardCharsets.UTF_8);

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(fileContent, FileUtils.readFileToString(outputFile, StandardCharsets.UTF_8));

        inputStream.close();
        outputStream.close();
        FileUtils.deleteQuietly(inputFile);
        FileUtils.deleteQuietly(outputFile);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        String charsetName = "UTF-16";
        String baseUri = "https://example.com";
        Parser parser = Parser.xmlParser();

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertEquals("<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>", doc.xml().toString());
        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, fileContent, StandardCharsets.UTF_8);

        InputStream inputStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(fileContent.getBytes(StandardCharsets.UTF_8).length, byteBuffer.limit());

        inputStream.close();
        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testReadToByteBufferEmpty() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(0, byteBuffer.limit());

        inputStream.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-16'>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, fileContent, StandardCharsets.UTF_8);

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertEquals(fileContent.getBytes(StandardCharsets.UTF_8).length, byteBuffer.limit());

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-16", DataUtil.getCharsetFromContentType("text/html; charset=UTF-16"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset="));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset=invalid-charset"));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-16", DataUtil.validateCharset("UTF-16"));
        assertEquals("UTF-16", DataUtil.validateCharset("utf-16"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("utf-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("ISO-8859-1")); // should be treated as a synonym for UTF-8
        assertEquals(null, DataUtil.validateCharset("invalid-charset"));
        assertEquals(null, DataUtil.validateCharset(""));
        assertEquals(null, DataUtil.validateCharset(null));
    }

    @Test
    public void testMimeBoundary() {
        assertEquals("--abcdef1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", DataUtil.mimeBoundary());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer bomUtf32 = ByteBuffer.wrap(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF});
        ByteBuffer bomUtf16BE = ByteBuffer.wrap(new byte[]{(byte) 0xFE, (byte) 0xFF, (byte) 0x00, (byte) 0x00});
        ByteBuffer bomUtf8 = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

        BomCharset utf32 = DataUtil.detectCharsetFromBom(bomUtf32);
        BomCharset utf16BE = DataUtil.detectCharsetFromBom(bomUtf16BE);
        BomCharset utf8 = DataUtil.detectCharsetFromBom(bomUtf8);

        assertEquals("UTF-32", utf32.charset);
        assertEquals(false, utf32.offset);
        assertEquals("UTF-16", utf16BE.charset);
        assertEquals(false, utf16BE.offset);
        assertEquals("UTF-8", utf8.charset);
        assertEquals(true, utf8.offset);

        assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[0])));
    }
}

class FileUtils {
    public static String readFileToString(File file, Charset charset) throws IOException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), charset);
        String content = "";
        try (BufferedReader buf = new BufferedReader(reader)) {
            String line = buf.readLine();
            while (line != null) {
                content += line + System.lineSeparator();
                line = buf.readLine();
            }
        }
        return content;
    }

    public static void writeStringToFile(File file, String content, Charset charset) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, charset);
            writer.write(content);
            writer.flush();
        }
    }

    public static void deleteQuietly(File file) {
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }
}