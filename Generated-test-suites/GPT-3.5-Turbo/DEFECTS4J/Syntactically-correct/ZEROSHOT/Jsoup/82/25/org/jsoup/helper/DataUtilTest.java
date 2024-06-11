import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    private File testFile;

    @Before
    public void setup() {
        testFile = new File("test.html");
    }

    @Test
    public void loadFile() throws IOException {
        Document doc = DataUtil.load(testFile, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void loadInputStream() throws IOException {
        InputStream in = new FileInputStream(testFile);
        Document doc = DataUtil.load(in, "UTF-8", "");
        assertNotNull(doc);
        in.close();
    }

    @Test
    public void loadInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream(testFile);
        Document doc = DataUtil.load(in, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        in.close();
    }

    @Test
    public void crossStreams() throws IOException {
        InputStream in = new FileInputStream(testFile);
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {}
        };
        DataUtil.crossStreams(in, out);
        in.close();
    }

    @Test
    public void parseInputStream() throws IOException {
        InputStream in = new FileInputStream(testFile);
        Document doc = DataUtil.parseInputStream(in, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        in.close();
    }

    @Test
    public void readToByteBuffer() throws IOException {
        InputStream in = new FileInputStream(testFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, 1024);
        assertNotNull(byteBuffer);
        in.close();
    }

    @Test
    public void emptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void getCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void validateCharset() {
        String charset = DataUtil.validateCharset("UTF-8");
        assertEquals("UTF-8", charset);
    }

    @Test
    public void mimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

    @Test
    public void detectCharsetFromBom() {
        byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bom);
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }
}