import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    private Document doc;

    @Before
    public void setUp() throws IOException {
        doc = DataUtil.parseInputStream(new File("test.html").inputStream(), "UTF-8", null);
    }

    @After
    public void tearDown() throws IOException {
        if (doc != null) {
            doc.close();
        }
    }

    @Test
    public void testLoadFile() throws IOException {
        Document doc = DataUtil.load(new File("test.html"), "UTF-8", null);
        assertEquals(doc, this.doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream is = new File("test.html").inputStream();
        Document doc = DataUtil.load(is, "UTF-8", null);
        assertEquals(doc, this.doc);
        is.close();
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        InputStream is = new File("test.html").inputStream();
        Document doc = DataUtil.load(is, "ISO-8859-1", null);
        assertNull(doc);
        is.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream is = new File("test.html").inputStream();
        Document doc = DataUtil.load(is, "UTF-8", null, DataUtil.Parser.xmlParser());
        assertNull(doc);
        is.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.html");
        File outputFile = new File("test_out.html");
        InputStream input = inputFile.inputStream();
        OutputStream output = outputFile.outputStream();

        DataUtil.crossStreams(input, output);
        input.close();
        output.flush();
        output.close();

        Document docInput = DataUtil.parseInputStream(inputFile.inputStream(), "UTF-8", null);
        Document docOutput = DataUtil.parseInputStream(outputFile.inputStream(), "UTF-8", null);
        assertEquals(docInput, docOutput);
        inputFile.delete();
        outputFile.delete();
    }

    @Test
    public void testParseInputStream() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes(StandardCharsets.UTF_8));
        os.write("<html>\n<head><meta charset=\"UTF-8\"/></head>\n<body>\n</body>\n</html>\n".getBytes(StandardCharsets.UTF_8));
        os.close();
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        Document doc = DataUtil.parseInputStream(is, "UTF-8", null, DataUtil.Parser.xmlParser());
        assertEquals(doc.rootElement().tagName(), "html");
        is.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File inputFile = new File("test.html");
        InputStream is = inputFile.inputStream();

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(is);
        assertEquals(byteBuffer.capacity(), (int) inputFile.length());

        is.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        File inputFile = new File("test.html");
        InputStream is = inputFile.inputStream();

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(is, 10);
        assertEquals(byteBuffer.capacity(), 10);

        is.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File inputFile = new File("test.html");

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(inputFile);
        assertEquals(byteBuffer.capacity(), (int) inputFile.length());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(byteBuffer.capacity(), 0);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals(DataUtil.getCharsetFromContentType(contentType), "UTF-8");

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals(DataUtil.getCharsetFromContentType(contentType), "ISO-8859-1");

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertEquals(mimeBoundary.length(), 32);
    }
}