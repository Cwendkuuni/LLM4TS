package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    public void testLoad() throws IOException {
        String html = "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello, World!</p>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("ISO-8859-1", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello, World!</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = Charset.forName("UTF-8").decode(ByteBuffer.wrap(html.getBytes()));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset());

        byteData.position(0);
        String metaCharset = "ISO-8859-1";
        String metaCharsetTag = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + metaCharset + "\">";
        byteData = ByteBuffer.wrap(ByteArray.toBytes(metaCharsetTag.getBytes()));
        byteData.position(2 + metaCharsetTag.length()); // skip the tag
        String charsetName = DataUtil.getCharsetFromContentType(new String(byteData.get(ByteBuffer.allocate(10).put(byteData).array())));
        Assert.assertEquals(metaCharset, charsetName);

        byteData.rewind();

        document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testLoadFromFile() throws IOException {
        String fileContent = "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello, World!</p>\n" +
                "</body>\n" +
                "</html>";

        File file = File.createTempFile("jsoup_test", null);
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();

        Document document = DataUtil.load(file, null, null);
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset());

        File.deleteOnExit(file);
    }
}