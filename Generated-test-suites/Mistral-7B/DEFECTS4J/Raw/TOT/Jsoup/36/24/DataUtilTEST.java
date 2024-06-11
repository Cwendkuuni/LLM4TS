import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.InputStreamParser;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(PowerMockRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String expectedHtml = "<html><head></head><body><h1>Test</h1></body></html>";
        File file = new File("test.html");
        file.createNewFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(expectedHtml);
        }

        Document actualDocument = DataUtil.load(file, "UTF-8", "");
        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedHtml, actualDocument.html());

        file.deleteOnExit();
    }

    @Test
    public void testLoadInputStreamWithUtf8Charset() throws IOException {
        String expectedHtml = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));

        Document actualDocument = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedHtml, actualDocument.html());
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String expectedHtml = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));

        Document actualDocument = DataUtil.load(inputStream, null, "");
        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedHtml, actualDocument.html());

        // Determine charset from meta tag and parse again
        String metaCharset = "iso-8859-1";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(expectedHtml.getBytes(StandardCharsets.UTF_8));
        outputStream.write("<meta charset=\"".getBytes(StandardCharsets.UTF_8));
        outputStream.write(metaCharset.getBytes(StandardCharsets.UTF_8));
        outputStream.write("\" />".getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream metaInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document documentWithMetaTag = DataUtil.load(metaInputStream, null, "");
        Assert.assertNotNull(documentWithMetaTag);
        Assert.assertEquals(expectedHtml, documentWithMetaTag.html());
        Assert.assertEquals(metaCharset, documentWithMetaTag.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String expectedHtml = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));

        // Mock Parser
        Parser parserMock = PowerMockito.mock(Parser.class);
        PowerMockito.when(parserMock.parseInput(expectedHtml, ""))
                .thenReturn(Mockito.mock(Document.class));
        DataUtil.load(inputStream, "X-Custom-Charset", "");

        // Test the method without mocking Parser
        Document actualDocument = DataUtil.load(inputStream, "X-Custom-Charset", "");
        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedHtml, actualDocument.html());
    }

    @Test
    public void testParseByteDataWithUtf8Charset() throws IOException {
        String expectedHtml = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(expectedHtml.getBytes(StandardCharsets.UTF_8));

        Document actualDocument = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedHtml, actualDocument.html());
    }

    @Test
    public void testParseByteDataWithNoCharset() throws IOException {
        String expectedHtml = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(expectedHtml.getBytes(StandardCharsets.UTF_8));

        Document actualDocument = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedHtml, actualDocument.html());

        // Determine charset from meta tag and parse again
        String metaCharset = "iso-8859-1";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(expectedHtml.getBytes(StandardCharsets.UTF_8));
        outputStream.write("<meta charset=\"".getBytes(StandardCharsets.UTF_8));
        outputStream.write(metaCharset.getBytes(StandardCharsets.UTF_8));
        outputStream.write("\" />".getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream metaInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ByteBuffer metaByteData = ByteBuffer.wrap(metaInputStream.readAllBytes());
        Document documentWithMetaTag = DataUtil.parseByteData(metaByteData, null, "", Parser.htmlParser());
        Assert.assertNotNull(documentWithMetaTag);
        Assert.assertEquals(expectedHtml, documentWithMetaTag.html());
        Assert.assertEquals(metaCharset, documentWithMetaTag.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String expectedHtml = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(expectedHtml.getBytes(StandardCharsets.UTF_8));

        // Mock Parser
        Parser parserMock = PowerMockito.mock(Parser.class);
        PowerMockito.when(parserMock.parseInput(expectedHtml, ""))
                .thenReturn(Mockito.mock(Document.class));
        Document actualDocument = DataUtil.parseByteData(byteData, "X-Custom-Charset", "", Parser.htmlParser());
        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedHtml, actualDocument.html());

        // Test the method without mocking Parser
        ByteBuffer invalidByteData = ByteBuffer.wrap(expectedHtml.getBytes(StandardCharsets.UTF_8));
        invalidByteData.position(expectedHtml.length() + 1); // Add a byte to make it invalid
        Document actualDocumentInvalid = DataUtil.parseByteData(invalidByteData, "X-Custom-Charset", "", Parser.htmlParser());
        Assert.assertNull(actualDocumentInvalid);
    }
}