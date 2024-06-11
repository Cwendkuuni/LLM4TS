
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile_charsetName_baseUri() throws IOException {
        File file = new File("path/to/file");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream_charsetName_baseUri() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream_charsetName_baseUri_parser() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/file");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        Assert.assertNotNull(document);
    }

}
