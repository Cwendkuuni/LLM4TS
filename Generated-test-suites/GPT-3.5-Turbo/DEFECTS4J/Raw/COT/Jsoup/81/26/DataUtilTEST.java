import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
        // Add additional assertions here
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
        // Add additional assertions here
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        // Add additional assertions here
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(buffer);
        // Add additional assertions here
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "");
        assertNotNull(doc);
        // Add additional assertions here
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertEquals(0, buffer.remaining());
        // Add additional assertions here
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
        // Add additional assertions here
    }

    // Add additional test cases for other methods in the DataUtil class

}



// Insert all the test cases here

