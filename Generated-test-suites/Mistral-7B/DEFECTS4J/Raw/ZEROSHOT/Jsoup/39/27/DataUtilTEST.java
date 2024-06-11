package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html"); // Replace with the actual file path
        Document doc = DataUtil.load(inputFile, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testCharsetDetection() throws IOException {
        String htmlContent = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=gb2312'></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8)), 0);

        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        Element metaElement = doc.select("meta[http-equiv=content-type]").first();
        String detectedCharset = DataUtil.getCharsetFromContentType(metaElement.attr("content"));
        assertTrue(detectedCharset != null && detectedCharset.equalsIgnoreCase("gb2312"));
    }
}