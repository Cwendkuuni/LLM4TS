import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    private File testFile;

    @Before
    public void setUp() {
        testFile = new File("test.html");
        try {
            testFile.createNewFile();
            FileWriter writer = new FileWriter(testFile);
            writer.write("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromFile() {
        try {
            Document doc = DataUtil.load(testFile, "UTF-8", "");
            assertNotNull(doc);
            assertEquals("Test", doc.title());
            assertEquals("Hello World!", doc.body().getElementsByTag("h1").first().text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            FileInputStream inputStream = new FileInputStream(testFile);
            Document doc = DataUtil.load(inputStream, "UTF-8", "");
            assertNotNull(doc);
            assertEquals("Test", doc.title());
            assertEquals("Hello World!", doc.body().getElementsByTag("h1").first().text());
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            FileInputStream inputStream = new FileInputStream(testFile);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataUtil.crossStreams(inputStream, outputStream);
            String result = outputStream.toString("UTF-8");
            assertEquals("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>", result);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            FileInputStream inputStream = new FileInputStream(testFile);
            Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
            assertNotNull(doc);
            assertEquals("Test", doc.title());
            assertEquals("Hello World!", doc.body().getElementsByTag("h1").first().text());
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            FileInputStream inputStream = new FileInputStream(testFile);
            ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(buffer);
            assertEquals(73, buffer.capacity());
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        try {
            ByteBuffer buffer = DataUtil.readFileToByteBuffer(testFile);
            assertNotNull(buffer);
            assertEquals(73, buffer.capacity());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }
}