import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static ByteBuffer byteData;

    @BeforeClass
    public static void init() {
        String docData = "Your HTML document data here";
        byteData = ByteBuffer.wrap(docData.getBytes(Charset.forName("UTF-8")));
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("path/to/your/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(byteData.array());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(byteData.array());

        Document doc = DataUtil.load(inputStream, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(byteData.array());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = DataUtil.Parser.xmlParser();

        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("path/to/your/file.html");
        String charsetName = null;
        String baseUri = "http://www.example.com";

        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testParseByteDataWithNullCharsetName() {
        String charsetName = null;
        String baseUri = "http://www.example.com";
        Parser parser = DataUtil.Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(doc);
    }

    @Test
    public void testParseByteDataWithEmptyCharsetName() {
        String charsetName = "";
        String baseUri = "http://www.example.com";
        Parser parser = DataUtil.Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(doc);
    }

    @Test
    public void testParseByteDataWithUnsupportedCharsetName() {
        String charsetName = "X-UNSUPPORTED-ENCODING";
        String baseUri = "http://www.example.com";
        Parser parser = DataUtil.Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(doc);
    }

    @Test
    public void testParseByteDataWithSupportedCharsetName() {
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = DataUtil.Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }
}