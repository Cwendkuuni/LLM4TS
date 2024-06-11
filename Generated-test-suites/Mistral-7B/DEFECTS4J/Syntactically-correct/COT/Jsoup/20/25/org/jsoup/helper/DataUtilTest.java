package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestRuleCollection;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {

    private static final String HTML_CONTENT = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
    private static final String CHARSET_CHANGE_HTML_CONTENT = "<html><head><meta charset=\"GBK\"></head><body></body></html>";
    private static final String CHARSET_NOT_FOUND_CONTENT = "<html><head></head><body></body></html>";

    @Rule
    public TestRule collections = new TestRuleCollection() {{
        add(new ExpectedException.ThrowableTypeRule(IOException.class));
        add(new ExpectedException.ThrowableTypeRule(NullPointerException.class));
    }};

    private Document expectedDocument;
    private InputStream inputStream;
    private File file;
    private Parser parser;

    @Before
    public void setUp() throws IOException {
        expectedDocument = org.jsoup.Jsoup.parse(HTML_CONTENT);
        inputStream = new ByteArrayInputStream(HTML_CONTENT.getBytes());
        file = new File("test.html");
        parser = mock(Parser.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        when(parser.parseInput(expectedDocument.html(), expectedDocument.baseUri())).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(file, "UTF-8", expectedDocument.baseUri());

        assertSame(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFile_charsetNull() throws IOException {
        Document actualDocument = DataUtil.load(file, null, expectedDocument.baseUri());

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFile_defaultCharset() throws IOException {
        when(parser.parseInput(expectedDocument.html(), expectedDocument.baseUri())).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(file, null, expectedDocument.baseUri());

        assertSame(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        when(parser.parseInput(HTML_CONTENT, expectedDocument.baseUri())).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", expectedDocument.baseUri());

        assertSame(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStream_charsetNull() throws IOException {
        when(parser.parseInput(HTML_CONTENT, expectedDocument.baseUri())).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(inputStream, null, expectedDocument.baseUri());

        assertSame(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStream_defaultCharset() throws IOException {
        when(parser.parseInput(HTML_CONTENT, expectedDocument.baseUri())).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(inputStream, null, expectedDocument.baseUri());

        assertSame(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStream_parser() throws IOException {
        Parser parser = mock(Parser.class);
        when(parser.parseInput(HTML_CONTENT, expectedDocument.baseUri())).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", expectedDocument.baseUri(), parser);

        assertSame(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStream_parser_defaultCharset() throws IOException {
        Parser parser = mock(Parser.class);

        Document actualDocument = DataUtil.load(inputStream, null, expectedDocument.baseUri(), parser);

        assertSame(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteData_charsetFoundInContentType() {
        ByteBuffer byteData = ByteBuffer.wrap(HTML_CONTENT.getBytes());
        String charsetName = "UTF-8";
        Document expectedDocument = org.jsoup.Jsoup.parse(HTML_CONTENT);

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, expectedDocument.baseUri(), parser);

        assertSame(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteData_charsetFoundInMetaTag() {
        ByteBuffer byteData = ByteBuffer.wrap(CHARSET_CHANGE_HTML_CONTENT.getBytes());
        String charsetName = null;

        Document expectedDocument = org.jsoup.Jsoup.parse(CHARSET_CHANGE_HTML_CONTENT);

        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, expectedDocument.baseUri(), parser);

        assertSame(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testParseByteData_charsetNotFound() {
        ByteBuffer byteData = ByteBuffer.wrap(CHARSET_NOT_FOUND_CONTENT.getBytes());
        String charsetName = null;

        Document document = DataUtil.parseByteData(byteData, charsetName, expectedDocument.baseUri(), parser);

        assertNull(document);
    }
}