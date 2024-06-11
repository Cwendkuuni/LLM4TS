package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "This is a test.";
        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document document = DataUtil.load(testFile, null, null);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals(fileContent, document.html());

        testFile.delete();
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String fileContent = "This is a test.";
        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document document = DataUtil.load(testFile, "ISO-8859-1", null);
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertEquals(fileContent, document.html());

        testFile.delete();
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testInput = "This is a test.";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));

        Document document = DataUtil.load(testInputStream, null, null);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals(testInput, document.html());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testInput = "This is a test.";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));

        Document document = DataUtil.load(testInputStream, "ISO-8859-1", null);
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertEquals(testInput, document.html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testInput = "<html><body><h1>This is a test</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));

        Document document = DataUtil.load(testInputStream, null, null, Parser.xmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("<html><body><h1>This is a test</h1></body></html>", document.xml().asXml());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "This is a test.";
        String output = "Output: ";
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");

        FileWriter inputWriter = new FileWriter(inputFile);
        inputWriter.write(input);
        inputWriter.close();

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        inputStream.close();
        outputStream.close();

        String expectedOutput = "Output: " + input;
        assertEquals(expectedOutput, FileUtils.readFileToString(outputFile, "UTF-8"));

        inputFile.delete();
        outputFile.delete();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testInput = "This is a test.";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));

        Document document = DataUtil.parseInputStream(testInputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals(testInput, document.html());

        testInputStream.close();
    }

    @Test
    public void testParseInputStreamWithCharsetFromContentType() throws IOException {
        String testInput = "text/html; charset=ISO-8859-1\n<html><body><h1>This is a test</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));

        Document document = DataUtil.parseInputStream(testInputStream, null, null, Parser.htmlParser());
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertEquals("<html><body><h1>This is a test</h1></body></html>", document.html());

        testInputStream.close();
    }

    @Test
    public void testParseInputStreamWithCharsetFromMetaTag() throws IOException {
        String testInput = "<html>\n<head>\n<meta charset=\"ISO-8859-1\">\n</head>\n<body>\n<h1>This is a test</h1>\n</body>\n</html>";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));

        Document document = DataUtil.parseInputStream(testInputStream, null, null, Parser.htmlParser());
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertEquals("<html>\n<head>\n<meta charset=\"ISO-8859-1\">\n</head>\n<body>\n<h1>This is a test</h1>\n</body>\n</html>", document.html());

        testInputStream.close();
    }

    @Test
    public void testParseInputStreamWithCharsetFromXmlDeclaration() throws IOException {
        String testInput = "<html xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns=\"http://www.w3.org/TR/REC-html40/strict.dtd\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<h1>This is a test</h1>\n</body>\n</html>";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));

        Document document = DataUtil.parseInputStream(testInputStream, null, null, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("<html xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns=\"http://www.w3.org/TR/REC-html40/strict.dtd\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<h1>This is a test</h1>\n</body>\n</html>", document.html());

        testInputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testInput = "This is a test.";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, 1024);
        assertEquals(testInput.length(), byteBuffer.limit());
        assertEquals(testInput, Charset.forName("UTF-8").decode(byteBuffer).toString());

        testInputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testInput = "This is a test.";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes("UTF-8"));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, 1);
        assertEquals(0, byteBuffer.limit());
        assertEquals("", Charset.forName("UTF-8").decode(byteBuffer).toString());

        testInputStream.close();
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] utf8Bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] utf16Lebom = {(byte) 0xFF, (byte) 0xFE};
        byte[] utf16BeBom = {(byte) 0xFE, (byte) 0xFF};
        byte[] utf32Bom = {(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF};

        assertEquals(new DataUtil.BomCharset("UTF-8", true), DataUtil.detectCharsetFromBom(ByteBuffer.wrap(utf8Bom)));
        assertEquals(new DataUtil.BomCharset("UTF-16", false), DataUtil.detectCharsetFromBom(ByteBuffer.wrap(utf16Lebom)));
        assertEquals(new DataUtil.BomCharset("UTF-16", false), DataUtil.detectCharsetFromBom(ByteBuffer.wrap(utf16BeBom)));
        assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[0])));
        assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[1])));
        assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[4])));
    }
}

class FileUtils {
    public static String readFileToString(File file, String charset) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, charset));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }

        reader.close();
        fileInputStream.close();

        return stringBuilder.toString();
    }

    public static void writeStringToFile(File file, String text, String charset) throws IOException {
        FileWriter fileWriter = new FileWriter(file, false);
        fileWriter.write(text);
        fileWriter.close();

        FileInputStream fileInputStream = new FileInputStream(file);
        assertEquals(text, Charset.forName(charset).decode(fileInputStream).toString());
        fileInputStream.close();
    }
}