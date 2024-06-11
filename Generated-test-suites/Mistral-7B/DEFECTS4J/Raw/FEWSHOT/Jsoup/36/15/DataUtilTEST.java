package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test cases for DataUtil class.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @InjectMocks
    private DataUtil util;

    @Mock
    private Parser parser;

    @Mock
    private Validate validate;

    /**
     * Test load method with valid arguments.
     * @throws IOException on IO error
     */
    @Test
    public void testLoad() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(1024);

        when(validate.notEmpty((String) anyObject(), "Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML")).thenReturn(true);

        ByteBuffer byteData = util.readToByteBuffer(inputStream);
        Document doc = util.load(byteData, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    /**
     * Test load method with null charsetName argument.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadWithNullCharsetName() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);

        Document doc = util.load(inputStream, null, "http://example.com", parser);

        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    /**
     * Test load method with empty charsetName argument.
     * @throws IOException on IO error
     */
    @Test
    public void testLoadWithEmptyCharsetName() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);

        Document doc = util.load(inputStream, "", "http://example.com", parser);

        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    /**
     * Test parseByteData method with valid arguments and no charset detected.
     * @throws IOException on IO error
     */
    @Test
    public void testParseByteDataWithNoDetectedCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'H', 'e', 'l', 'l', 'o', 0});
        String baseUri = "http://example.com";

        Document doc = util.parseByteData(byteData, null, baseUri, parser);

        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    /**
     * Test parseByteData method with valid arguments and detected charset.
     * @throws IOException on IO error
     */
    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Content-Type: text/html; charset=ISO-8859-1".getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'H', 'e', 'l', 'l', 'o'});
        String baseUri = "http://example.com";

        when(parser.parseInput((String) anyObject(), (String) anyObject())).thenReturn(mockDocument());

        Document doc = util.parseByteData(byteData, "ISO-8859-1", baseUri, parser);

        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    /**
     * Test readToByteBuffer method with valid arguments and maximum size.
     * @throws IOException on IO error
     */
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        int maxSize = 4;

        ByteBuffer byteData = util.readToByteBuffer(inputStream, maxSize);

        assertEquals(4, byteData.limit());
        assertEquals("test", Charset.forName("UTF-8").decode(byteData).toString());
    }

    /**
     * Test readToByteBuffer method with valid arguments and no maximum size.
     * @throws IOException on IO error
     */
    @Test
    public void testReadToByteBufferWithNoMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());

        ByteBuffer byteData = util.readToByteBuffer(inputStream);

        assertEquals(7, byteData.limit());
        assertEquals("test", Charset.forName("UTF-8").decode(byteData).toString());
    }

    /**
     * Test getCharsetFromContentType method with valid content type.
     * @throws IOException on IO error
     */
    @Test
    public void testGetCharsetFromContentTypeWithValidContentType() throws IOException {
        String contentType = "text/html; charset=UTF-8";

        assertEquals("UTF-8", util.getCharsetFromContentType(contentType));
    }

    /**
     * Test getCharsetFromContentType method with invalid content type.
     * @throws IOException on IO error
     */
    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() throws IOException {
        String contentType = "text/html; charset=gb2312";

        assertNull(util.getCharsetFromContentType(contentType));
    }

    /**
     * Mock document returned by parser.parseInput method.
     * @return mock document
     */
    private Document mockDocument() {
        Document doc = mock(Document.class);
        when(doc.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(mockElement());
        return doc;
    }

    /**
     * Mock element returned by document.select method.
     * @return mock element
     */
    private Element mockElement() {
        Element element = mock(Element.class);
        when(element.attr("http-equiv")).thenReturn("content-type");
        when(element.attr("content")).thenReturn("text/html; charset=ISO-8859-1");
        return element;
    }
}