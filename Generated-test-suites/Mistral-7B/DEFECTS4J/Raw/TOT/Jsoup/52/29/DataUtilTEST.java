package org.jsoup.helper.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataUtilTest {

    private static final String HTML_FILE_CONTENTS = "" +
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<meta charset=\"UTF-8\">\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
            "</head>\n" +
            "<body>\n" +
            "</body>\n" +
            "</html>";

    private static final String XML_FILE_CONTENTS = "" +
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<root>\n" +
            "<element/>\n" +
            "</root>";

    private static InputStream inputStreamFromString;
    private static Document docFromString;

    @BeforeAll
    public static void setUp() throws IOException {
        inputStreamFromString = new ByteArrayInputStream(HTML_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8));
        docFromString = DataUtil.load(inputStreamFromString, "UTF-8", null, Parser.htmlParser());
    }

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File testFile = new File("test.html");
        testFile.createNewFile();
        Assertions.assertDoesNotThrow(() -> DataUtil.load(testFile, "UTF-8", null));
        testFile.delete();
    }

    @Test
    public void testLoadFileWithMissingCharset() throws IOException {
        File testFile = new File("test.html");
        testFile.createNewFile();
        InputStream in = new FileInputStream(testFile);
        assertThrows(IOException.class, () -> DataUtil.load(in, null, null));
        testFile.delete();
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        Document doc = DataUtil.load(inputStreamFromString, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithMissingCharset() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStreamFromString, byteStream);
        byteStream.close();
        InputStream inputStreamFromBytes = new ByteArrayInputStream(byteStream.toByteArray());
        assertThrows(IOException.class, () -> DataUtil.load(inputStreamFromBytes, null, null));
    }

    @Test
    public void testLoadInputStreamWithXmlParser() throws IOException {
        Document doc = DataUtil.load(inputStreamFromString, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals(Parser.xmlParser().parseInput(XML_FILE_CONTENTS, null).rootElement(), doc.rootElement());
    }

    @Test
    public void testParseByteDataWithValidCharset() {
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStreamFromString);
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(docFromString.title(), doc.title());
    }

    @Test
    public void testParseByteDataWithMissingCharset() {
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStreamFromString);
        ByteBuffer byteDataWithoutBom = ByteBuffer.wrap(byteData.array(), 0, byteData.limit() - 3);
        Document doc = DataUtil.parseByteData(byteDataWithoutBom, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertNotNull(doc.select("html"));
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStreamFromString);
        Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }
}