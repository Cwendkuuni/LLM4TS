package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes());
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document document = DataUtil.load(testFile, "UTF-8", null);
        assertNotNull(document);
        assertEquals(fileContent, document.body().html());

        DataUtil.load(testFile, null, null); // test default charset
        assertEquals(defaultCharset, DataUtil.getDefaultCharset());

        // Test loading a non-existent file
        Document documentNull = DataUtil.load(new File("nonExistentFile.html"), null, null);
        assertNull(documentNull);

        // Test closing input stream
        File testFile2 = new File("testFile2.html");
        try (InputStream inputStream = new FileInputStream(testFile2)) {
            DataUtil.load(inputStream, null, null);
        }
        assertNull(testFile2.exists());

        testFile2.createNewFile();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        assertEquals(inputStreamContent, document.body().html());

        Document documentNull = DataUtil.load(inputStream, null, null);
        assertNull(documentNull);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><body>This is a test input stream.</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(inputStreamContent, document.body().html());

        Document documentXml = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(documentXml);
        assertEquals(inputStreamContent, documentXml.body().html());

        Document documentNull = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        assertNull(documentNull);
    }

    @Test
    public void testParseByteData() throws IOException {
        String fileContent = "<html><body>This is a test file.</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes());

        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(fileContent, document.body().html());

        String charset = "GB2312";
        Document documentWithMetaTag = new Document() {
            {
                body().appendElement("html").appendElement("head").appendElement("meta").attr("http-equiv", "Content-Type")
                        .attr("content", "text/html; charset=" + charset);
                body().appendElement("body").text(fileContent);
            }
        };
        ByteArrayOutputStream metaTagOutputStream = new ByteArrayOutputStream();
        metaTagOutputStream.write(documentWithMetaTag.html().getBytes());
        ByteBuffer byteDataWithMetaTag = ByteBuffer.wrap(metaTagOutputStream.toByteArray());

        Document documentWithMetaTagParsed = DataUtil.parseByteData(byteDataWithMetaTag, null, null, Parser.htmlParser());
        assertNotNull(documentWithMetaTagParsed);
        assertEquals(fileContent, documentWithMetaTagParsed.body().html());
        assertEquals(charset, documentWithMetaTagParsed.outputSettings().charset());

        Document documentNull = DataUtil.parseByteData(ByteBuffer.wrap(new byte[0]), null, null, Parser.htmlParser());
        assertNull(documentNull);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(inputStreamContent.getBytes().length, byteData.limit());

        ByteBuffer byteDataWithCharset = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(inputStreamContent.getBytes().length, byteDataWithCharset.limit());

        ByteBuffer byteDataWithFile = new ByteArrayInputStream(inputStreamContent.getBytes()).markReadLimit(100);
        byteDataWithFile = DataUtil.readToByteBuffer(byteDataWithFile);
        assertEquals(inputStreamContent.getBytes().length, byteDataWithFile.limit());

        ByteBuffer byteDataNull = DataUtil.readToByteBuffer(null);
        assertEquals(0, byteDataNull.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        String contentType2 = "text/html; charset=euc-jp";
        charset = DataUtil.getCharsetFromContentType(contentType2);
        assertEquals("euc-jp", charset);

        String contentType3 = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType3);
        assertEquals(defaultCharset, charset);

        String contentType4 = "text/xml";
        charset = DataUtil.getCharsetFromContentType(contentType4);
        assertEquals(defaultCharset, charset);

        String contentType5 = "text/plain; charset=gb2312";
        charset = DataUtil.getCharsetFromContentType(contentType5);
        assertEquals("gb2312", charset);
    }
}