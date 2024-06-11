import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {

    private File testFile;
    private InputStream testInputStream;

    @Before
    public void setUp() throws Exception {
        testFile = new File("test.html");
        FileOutputStream fos = new FileOutputStream(testFile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
        bw.write("<html><head></head><body><h1>Hello, World!</h1></body></html>");
        bw.close();
        fos.close();
        testInputStream = new FileInputStream(testFile);
    }

    @Test
    public void testLoadWithFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(document);
        assertEquals("Hello, World!", document.select("h1").text());
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(testInputStream, charsetName, baseUri);
        assertNotNull(document);
        assertEquals("Hello, World!", document.select("h1").text());
    }

    @Test
    public void testLoadWithInputStreamAndParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(testInputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals("Hello, World!", document.select("h1").text());
    }

    @Test(expected = IOException.class)
    public void testLoadWithNonExistentFile() throws IOException {
        File nonExistentFile = new File("non_existent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        DataUtil.load(nonExistentFile, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadWithClosingInputStreamError() throws IOException {
        InputStream closingInputStream = new ClosingInputStream(testInputStream);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(closingInputStream, charsetName, baseUri);
    }

    private static class ClosingInputStream extends FilterInputStream {
        ClosingInputStream(InputStream in) {
            super(in);
        }

        @Override
        public void close() throws IOException {
            throw new IOException("Error closing input stream");
        }
    }
}