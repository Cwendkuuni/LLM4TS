package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private static InputStream createInputStreamFromString(String value) {
        return new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testLoad() throws IOException {
        String xmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        String htmlContent = "<html><body></body></html>";

        Document xmlDocument = DataUtil.load(new File("xmlFile.xml"), "UTF-8", null); // should load from file, but we'll test with a string for simplicity
        Document htmlDocument = DataUtil.load(new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8)), "UTF-8", null);
        Document xmlDocumentFromString = DataUtil.load(createInputStreamFromString(xmlContent), "UTF-8", null);
        Document htmlDocumentFromString = DataUtil.load(createInputStreamFromString(htmlContent), "UTF-8", null);

        assertEquals(xmlDocument, xmlDocumentFromString);
        assertEquals(htmlDocument, htmlDocumentFromString);
        assertNotNull(xmlDocument.title());
        assertNull(htmlDocument.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String xmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        String htmlContent = "<html><body></body></html>";

        ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
        xmlOutputStream.write(xmlContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream htmlOutputStream = new ByteArrayOutputStream();
        htmlOutputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));

        InputStream xmlInputStream = new ByteArrayInputStream(xmlOutputStream.toByteArray());
        InputStream htmlInputStream = new ByteArrayInputStream(htmlOutputStream.toByteArray());

        Document xmlDocument = DataUtil.parseInputStream(xmlInputStream, "UTF-8", null, Parser.xmlParser());
        Document htmlDocument = DataUtil.parseInputStream(htmlInputStream, "UTF-8", null, Parser.htmlParser());

        assertEquals(xmlDocument, DataUtil.parseInputStream(xmlInputStream, null, null, Parser.xmlParser()));
        assertEquals(htmlDocument, DataUtil.parseInputStream(htmlInputStream, null, null, Parser.htmlParser()));
        assertEquals(xmlDocument, DataUtil.parseInputStream(xmlInputStream, "UTF-8", null, Parser.htmlParser()));
        assertEquals(htmlDocument, DataUtil.parseInputStream(htmlInputStream, "UTF-8", null, Parser.xmlParser()));

        assertEquals(xmlDocument, xmlDocumentFromString);
        assertEquals(htmlDocument, htmlDocumentFromString);
        assertNotNull(xmlDocument.title());
        assertNull(htmlDocument.title());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Lorem ipsum dolor sit amet, consectetur adipiscing elit";
        String outputData = "";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] result = outputStream.toByteArray();

        assertEquals(inputData, new String(result, StandardCharsets.UTF_8));
    }
}