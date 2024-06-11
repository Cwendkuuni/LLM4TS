
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc); // check if the document is not null
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = new ByteArrayInputStream("<html></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc); // check if the document is not null
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = new ByteArrayInputStream("<html></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        assertNotNull(doc); // check if the document is not null
    }

    @Test
    public void testParseByteDataWithCharset() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        assertNotNull(DataUtil.parseByteData(ByteBuffer.wrap("<html></html>".getBytes()), charsetName, baseUri, parser));
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        assertNotNull(DataUtil.parseByteData(ByteBuffer.wrap("<html></html>".getBytes()), charsetName, baseUri, parser));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        assertNotNull(DataUtil.readToByteBuffer(inStream));
        inStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }
}
