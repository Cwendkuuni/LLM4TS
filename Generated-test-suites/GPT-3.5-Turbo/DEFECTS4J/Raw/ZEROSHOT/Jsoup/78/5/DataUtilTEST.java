import org.jsoup.helper.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {
    private File testFile;

    @Before
    public void setUp() throws Exception {
        testFile = new File("test.html");
        BufferedWriter writer = new BufferedWriter(new FileWriter(testFile));
        writer.write("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");
        writer.close();
    }

    @After
    public void tearDown() throws Exception {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testLoadFile() throws Exception {
        Document doc = DataUtil.load(testFile, "UTF-8", "");
        assertNotNull("Document should not be null", doc);
        assertEquals("Document title should be 'Test'", "Test", doc.title());
    }

    @Test
    public void testLoadInputStream() throws Exception {
        FileInputStream fis = new FileInputStream(testFile);
        Document doc = DataUtil.load(fis, "UTF-8", "");
        fis.close();
        assertNotNull("Document should not be null", doc);
        assertEquals("Document title should be 'Test'", "Test", doc.title());
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        FileInputStream fis = new FileInputStream(testFile);
        Document doc = DataUtil.load(fis, "UTF-8", "", Parser.htmlParser());
        fis.close();
        assertNotNull("Document should not be null", doc);
        assertEquals("Document title should be 'Test'", "Test", doc.title());
    }

    @Test
    public void testCrossStreams() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataUtil.crossStreams(bais, baos);
        bais.close();
        baos.close();
        String output = baos.toString("UTF-8");
        assertEquals("Output should be 'Hello, World!'", "Hello, World!", output);
    }

    @Test
    public void testParseInputStream() throws Exception {
        FileInputStream fis = new FileInputStream(testFile);
        Document doc = DataUtil.parseInputStream(fis, "UTF-8", "", Parser.htmlParser());
        fis.close();
        assertNotNull("Document should not be null", doc);
        assertEquals("Document title should be 'Test'", "Test", doc.title());
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        FileInputStream fis = new FileInputStream(testFile);
        ByteBuffer buffer = DataUtil.readToByteBuffer(fis, 1024);
        fis.close();
        assertNotNull("Buffer should not be null", buffer);
        assertEquals("Buffer size should be greater than 0", true, buffer.remaining() > 0);
    }
}