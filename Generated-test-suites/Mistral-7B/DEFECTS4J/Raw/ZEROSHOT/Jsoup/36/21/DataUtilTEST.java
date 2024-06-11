package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.net.ssl.*"})
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String xmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body>Test File</body></html>";
        File file = new File("testFile.html");
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(xmlContent.getBytes());
        file.createNewFile();
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getProperty("user.dir")).thenReturn("/path/to/test");

        Document document = DataUtil.load(file, "UTF-8", null);
        assertEquals("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body>Test File</body></html>", document.html());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String xmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body>Test InputStream</body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes());

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body>Test InputStream</body></html>", document.html());
    }

    @Test
    public void testDetectCharset() throws IOException {
        String xmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body>Test File</body></html>";
        File file = new File("testFile.html");
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(xmlContent.getBytes());
        file.createNewFile();
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getProperty("user.dir")).thenReturn("/path/to/test");

        Document document = DataUtil.load(file, null, null);
        assertEquals("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body>Test File</body></html>", document.html());

        ByteBuffer byteData = ByteBuffer.wrap(xmlContent.getBytes());
        document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body>Test File</body></html>", document.html());

        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);
    }

    @Test(expected = NullPointerException.class)
    public void testLoadFileWithNullBaseUri() throws IOException {
        String xmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body>Test File</body></html>";
        File file = new File("testFile.html");
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(xmlContent.getBytes());
        file.createNewFile();

        DataUtil.load(file, null, null);
    }
}