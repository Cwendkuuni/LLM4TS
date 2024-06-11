import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new FileOutputStream("output.txt");
        DataUtil.crossStreams(in, out);
        // Assert output file is created successfully
        File outputFile = new File("output.txt");
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
        // Clean up the output file
        outputFile.delete();
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream input = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new FileInputStream("test.html");
        int maxSize = 1024;
        ByteBuffer buffer = DataUtil.readToByteBuffer(in, maxSize);
        assertNotNull(buffer);
        assertTrue(buffer.capacity() <= maxSize);
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream in = new FileInputStream("test.html");
        ByteBuffer buffer = DataUtil.readToByteBuffer(in);
        assertNotNull(buffer);
        assertTrue(buffer.capacity() > 0);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(buffer);
        assertEquals(file.length(), buffer.capacity());
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer emptyBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(emptyBuffer);
        assertEquals(0, emptyBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("EUC-JP", charset);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(cs);
        assertNotNull(validatedCharset);
        assertEquals(cs.toUpperCase(), validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'H', 'e', 'l', 'l', 'o'});
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }
}