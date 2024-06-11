import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File sampleFile = new File("sample.html");
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";

        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello, World!</h1></body></html>";
        Files.write(sampleFile.toPath(), htmlContent.getBytes(StandardCharsets.UTF_8));

        Document actualDocument = DataUtil.load(sampleFile, expectedCharset, baseUri);
        assertNotNull(actualDocument);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello, World!</h1></body></html>", actualDocument.html());

        Files.delete(sampleFile.toPath());
    }

    @Test
    public void testLoadInputStreamWithUtf8() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(actualDocument);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello, World!</h1></body></html>", actualDocument.html());
    }

    @Test
    public void testLoadInputStreamWithUnknownCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"ISO-8859-1\"></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(actualDocument);
        assertEquals("<html><head><meta charset=\"ISO-8859-1\"></head><body><h1>Hello, World!</h1></body></html>", actualDocument.html());

        String decodedContentType = "text/html; charset=ISO-8859-1";
        String foundCharset = DataUtil.getCharsetFromContentType(decodedContentType);
        assertEquals("ISO-8859-1", foundCharset);
    }

    @Test
    public void testLoadInputStreamWithNoContentType() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(actualDocument);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello, World!</h1></body></html>", actualDocument.html());
    }

    @Test
    public void testLoadInputStreamWithNoContentTypeOrMetaTag() throws IOException {
        String htmlContent = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";

        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(actualDocument);
        assertEquals("<html><body><h1>Hello, World!</h1></body></html>", actualDocument.html());
    }
}