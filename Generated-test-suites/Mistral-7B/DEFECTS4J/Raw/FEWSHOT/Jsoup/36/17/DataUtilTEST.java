package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.elements.Meta;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadDocument() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);
        when(parser.parseInput(anyString(), eq(baseUri))).thenReturn(createDocument("Hello World"));

        Document document = DataUtil.load(inputStream, "UTF-8", baseUri, parser);

        assertEquals("Hello World", document.title());
        verify(parser, times(1)).parseInput(anyString(), eq(baseUri));
    }

    @Test
    public void testDetectCharset() throws IOException {
        String htmlContent = "<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"/>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);

        Document document = DataUtil.load(inputStream, null, baseUri, parser);

        assertEquals("ISO-8859-1", document.outputSettings().charset());
        verify(parser, times(1)).parseInput(anyString(), eq(baseUri));
    }

    @Test
    public void testDetectCharsetWithUnknownMetaTag() throws IOException {
        String htmlContent = "<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<meta http-equiv=\"Content-Type\" content=\"text/html\"/>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);

        Document document = DataUtil.load(inputStream, null, baseUri, parser);

        assertEquals("UTF-8", document.outputSettings().charset());
        verify(parser, times(1)).parseInput(anyString(), eq(baseUri));
    }

    @Test
    public void testDetectCharsetWithInvalidMetaTag() throws IOException {
        String htmlContent = "<html>\n<head>\n<meta charset=\"UTF-8\"/>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=invalid\"/>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);

        Document document = DataUtil.load(inputStream, null, baseUri, parser);

        assertEquals("UTF-8", document.outputSettings().charset());
        verify(parser, times(1)).parseInput(anyString(), eq(baseUri));
    }

    @Test
    public void testDetectCharsetWithNoMetaTagOrContentTypeHeader() throws IOException {
        String htmlContent = "<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);

        Document document = DataUtil.load(inputStream, null, baseUri, parser);

        assertEquals("UTF-8", document.outputSettings().charset());
        verify(parser, times(1)).parseInput(anyString(), eq(baseUri));
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        int maxSize = 3;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertEquals("Test", Charset.forName("UTF-8").decode(byteBuffer).toString());
        assertEquals(3, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferWithNoMaxSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertEquals("Test", Charset.forName("UTF-8").decode(byteBuffer).toString());
        assertEquals(byteBuffer.limit(), inputStream.available());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBufferWithNegativeMaxSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());

        DataUtil.readToByteBuffer(inputStream, -1);
    }

    private Document createDocument(String title) {
        return new Document() {
            @Override
            public String title() {
                return title;
            }
        };
    }
}