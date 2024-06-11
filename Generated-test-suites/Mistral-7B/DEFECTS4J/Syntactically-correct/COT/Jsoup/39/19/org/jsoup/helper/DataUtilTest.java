package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String fileContent = "Some content of the file";
        File file = new File("path/to/your/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, baseUri);
        Document actualDocument = DataUtil.load(file, charsetName, baseUri);

        assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    @Test
    public void testLoadDocumentFromInputStreamWhenCharsetIsSpecified() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String fileContent = "Some content of the file";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, baseUri);
        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    @Test
    public void testLoadDocumentFromInputStreamWhenCharsetIsDetectedFromMetaTag() throws IOException {
        String charsetName = null;
        String baseUri = "http://example.com";
        String fileContent = "<html lang=\"en\"><head><meta charset=\"gb2312\"/></head><body>Some content of the file</body></html>";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, baseUri);
        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    @Test(expected = IOException.class)
    public void testLoadDocumentFromInputStreamWithInvalidCharsetName() throws IOException {
        String charsetName = "invalidCharsetName";
        String baseUri = "http://example.com";
        String fileContent = "Some content of the file";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String fileContent = "<html lang=\"en\"><head><meta charset=\"gb2312\"/></head><body>Some content of the file</body></html>";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, baseUri);
        assertEquals(expectedDocument.body().text(), actualDocument.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "Some content of the file";
        File file = new File("path/to/your/file.html");
        InputStream inputStream = new FileInputStream(file);

        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(fileContent.getBytes());
        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        assertEquals(expectedByteBuffer.capacity(), actualByteBuffer.capacity());
        assertEquals(expectedByteBuffer.position(), actualByteBuffer.position());
        assertEquals(expectedByteBuffer.limit(), actualByteBuffer.limit());
        assertEquals(expectedByteBuffer.get(0), actualByteBuffer.get(0));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharsetName() {
        String contentType = "text/html; charset=invalidCharsetName";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }

}