import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private File file;
    private String charsetName;
    private String baseUri;

    @Before
    public void setUp() {
        file = new File("test.html");
        charsetName = "UTF-8";
        baseUri = "http://example.com";
    }

    @After
    public void tearDown() {
        file.delete();
    }

    @Test
    public void testLoadFile() throws IOException {
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
        }
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
            assertNotNull(document);
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test".getBytes());
        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
            assertNotNull(byteBuffer);
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        File nonExistentFile = new File("nonexistent.html");
        DataUtil.load(nonExistentFile, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamIOException() throws IOException {
        InputStream invalidInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        DataUtil.load(invalidInputStream, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithParserIOException() throws IOException {
        InputStream invalidInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        DataUtil.load(invalidInputStream, charsetName, baseUri, Parser.htmlParser());
    }
}