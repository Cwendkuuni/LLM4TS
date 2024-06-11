package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>foo</element>\n</root>";
        File testFile = new File("test.xml");
        FileOutputStream testFileOutputStream = new FileOutputStream(testFile);
        testFileOutputStream.write(fileContent.getBytes());
        testFileOutputStream.close();

        Document document = DataUtil.load(testFile, "UTF-8", null);
        assertEquals("<root><element>foo</element></root>", document.html());

        testFile.delete();
    }

    @Test
    public void testLoadFileWithNullCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>foo</element>\n</root>";
        File testFile = new File("test.xml");
        FileOutputStream testFileOutputStream = new FileOutputStream(testFile);
        testFileOutputStream.write(fileContent.getBytes());
        testFileOutputStream.close();

        Document document = DataUtil.load(testFile, null, null);
        assertEquals("<root><element>foo</element></root>", document.html());

        testFile.delete();
    }

    @Test
    public void testLoadFileWithWrongCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>foo</element>\n</root>";
        File testFile = new File("test.xml");
        FileOutputStream testFileOutputStream = new FileOutputStream(testFile);
        testFileOutputStream.write(fileContent.getBytes());
        testFileOutputStream.close();

        Document document = DataUtil.load(testFile, "ISO-8859-1", null);
        assertNull(document);

        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>foo</element>\n</root>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document document = DataUtil.load(testInputStream, "UTF-8", null);
        assertEquals("<root><element>foo</element></root>", document.html());
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>foo</element>\n</root>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document document = DataUtil.load(testInputStream, null, null);
        assertEquals("<root><element>foo</element></root>", document.html());
    }

    @Test
    public void testLoadInputStreamWithWrongCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>foo</element>\n</root>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document document = DataUtil.load(testInputStream, "ISO-8859-1", null);
        assertNull(document);
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String inputStreamContent = "<html>\n<head>\n<meta charset=\"UTF-8\"></head>\n<body>\n<element>foo</element>\n</body>\n</html>";
        InputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        Parser parser = mock(Parser.class);
        when(parser.parseInput(anyString(), anyString())).thenReturn(createMockDocument());

        Document document = DataUtil.load(testInputStream, "UTF-8", null, parser);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><element>foo</element></body></html>", document.html());
    }

    @Test
    public void testParseByteData() throws IOException {
        String testString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>foo</element>\n</root>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes());

        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", null, Parser.htmlParser());
        assertEquals("<root><element>foo</element></root>", document.html());
    }

    @Test
    public void testParseByteDataWithNullCharset() throws IOException {
        String testString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>foo</element>\n</root>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes());

        Document document = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertEquals("<root><element>foo</element></root>", document.html());
    }

    @Test
    public void testParseByteDataWithWrongCharset() throws IOException {
        String testString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>foo</element>\n</root>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes());

        Document document = DataUtil.parseByteData(byteBuffer, "ISO-8859-1", null, Parser.htmlParser());
        assertNull(document);
    }

    @Test
    public void testParseByteDataWithCustomParser() throws IOException {
        String testString = "<html>\n<head>\n<meta charset=\"UTF-8\"></head>\n<body>\n<element>foo</element>\n</body>\n</html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes());
        Parser parser = mock(Parser.class);
        when(parser.parseInput(anyString(), anyString())).thenReturn(createMockDocument());

        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", null, parser);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><element>foo</element></body></html>", document.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>foo</element>\n</root>";
        InputStream testInputStream = new ByteArrayInputStream(testString.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);

        byte[] expectedByteArray = testString.getBytes();
        byte[] actualByteArray = byteBuffer.array();
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType("text/html; charset=ISO-8859-1"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/plain"));
    }

    private Document createMockDocument() {
        Document document = mock(Document.class);
        when(document.html()).thenReturn("<root></root>");
        return document;
    }
}