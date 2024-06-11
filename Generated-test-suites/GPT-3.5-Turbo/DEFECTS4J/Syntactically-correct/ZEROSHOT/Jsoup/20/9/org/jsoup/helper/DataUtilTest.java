import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {
    
    private DataUtil dataUtil;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        Document doc = dataUtil.load(file, charsetName, baseUri);

        Assert.assertNotNull(doc);
        // additional assertions
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        Document doc = dataUtil.load(in, charsetName, baseUri);

        Assert.assertNotNull(doc);
        // additional assertions
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.xmlParser();

        Document doc = dataUtil.load(in, charsetName, baseUri, parser);

        Assert.assertNotNull(doc);
        // additional assertions
    }

    @Test
    public void testParseByteData() {
        // create mock data
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        Document doc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Assert.assertNotNull(doc);
        // additional assertions
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");

        ByteBuffer byteData = dataUtil.readToByteBuffer(in);

        Assert.assertNotNull(byteData);
        // additional assertions
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = dataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", charset);
    }

}