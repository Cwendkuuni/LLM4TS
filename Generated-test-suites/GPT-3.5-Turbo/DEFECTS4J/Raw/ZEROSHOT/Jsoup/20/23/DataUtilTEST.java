import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private DataUtil dataUtil;
    private InputStream in;
    private File file;
    private String charsetName;
    private String baseUri;
    private Parser parser;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
        in = null;
        file = new File("testFile.txt");
        charsetName = "UTF-8";
        baseUri = "http://example.com";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadWithFile() {
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadWithInputStream() {
        try {
            Document document = DataUtil.load(in, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadWithParser() {
        try {
            Document document = DataUtil.load(in, charsetName, baseUri, parser);
            assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteData() {
        Document document = dataUtil.parseByteData(null, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            in = getClass().getClassLoader().getResourceAsStream("testFile.txt");
            assertNotNull(in);
            ByteBuffer byteData = dataUtil.readToByteBuffer(in);
            assertNotNull(byteData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        assertEquals(charset, "UTF-8");
    }
}