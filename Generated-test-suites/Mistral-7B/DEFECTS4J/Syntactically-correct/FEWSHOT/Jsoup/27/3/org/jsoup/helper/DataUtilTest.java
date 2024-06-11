package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Hello World</h1></body></html>";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, fileContent, "UTF-8");

        Document doc = DataUtil.load(file, null);
        assertEquals("Hello World", doc.title());

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String content = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        Document doc = DataUtil.load(inputStream, null);
        assertEquals("Hello World", doc.title());
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String content = "text/html; charset=UTF-8\n<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        Document doc = DataUtil.load(inputStream, "UTF-8");
        assertEquals("Hello World", doc.title());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String content = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertEquals("Hello World", doc.title());
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String content = "text/html; charset=" + charsetName + "\n<?xml version='1.0' encoding='" + charsetName + "'?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("Hello World", doc.title());

        // Test charset detection
        ByteBuffer byteDataWithoutCharset = ByteBuffer.wrap(content.substring(0, content.indexOf("\n") + 1).getBytes());
        Document docWithoutCharset = DataUtil.parseByteData(byteDataWithoutCharset, null, null, Parser.htmlParser());
        String detectedCharset = docWithoutCharset.outputSettings().charset();
        assertEquals(charsetName, detectedCharset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        String contentTypeWithoutCharset = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentTypeWithoutCharset);
        assertNull(charset);
    }
}