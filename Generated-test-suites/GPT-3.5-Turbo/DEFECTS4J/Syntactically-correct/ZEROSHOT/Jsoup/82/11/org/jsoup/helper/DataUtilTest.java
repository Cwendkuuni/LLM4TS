import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        FileInputStream fis = new FileInputStream("test.html");
        Document doc = DataUtil.load(fis, "UTF-8", "");
        assertNotNull(doc);
        fis.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        FileInputStream fis = new FileInputStream("test.html");
        Document doc = DataUtil.load(fis, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        fis.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        FileInputStream fis = new FileInputStream("input.txt");
        Document doc = DataUtil.load(fis, "UTF-8", "");
        assertNotNull(doc);

        File outputFile = new File("output.txt");
        DataUtil.crossStreams(fis, outputFile);
        assertTrue(outputFile.exists());
        outputFile.delete();
    }

    @Test
    public void testParseInputStream() throws IOException {
        FileInputStream fis = new FileInputStream("test.html");
        Document doc = DataUtil.parseInputStream(fis, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        fis.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream fis = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(fis, 1024);
        assertNotNull(byteBuffer);
        fis.close();
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.capacity());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
    
    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(cs);
        assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void testValidateCharset_InvalidCharset() {
        String cs = "InvalidCharset";
        String validatedCharset = DataUtil.validateCharset(cs);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bom);
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }
}