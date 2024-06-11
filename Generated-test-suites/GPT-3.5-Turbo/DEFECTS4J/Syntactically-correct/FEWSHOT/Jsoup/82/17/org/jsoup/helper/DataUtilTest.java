import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.*;

public class DataUtilTest {
    @Test
    public void testLoadFromEmptyFile() throws IOException {
        File file = new File("test.html");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.close();

        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
        assertTrue(doc.children().isEmpty());
        assertFalse(file.exists());
    }

    @Test
    public void testLoadFromString() throws IOException {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";

        Document doc = DataUtil.load(new ByteArrayInputStream(html.getBytes()), "UTF-8", "");
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testLoadWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset='ISO-2022-JP'></head><body><h1>こんにちは</h1></body></html>";

        Document doc = DataUtil.load(new ByteArrayInputStream(html.getBytes()), "UTF-8", "");
        assertNotNull(doc);
        assertEquals("こんにちは", doc.body().select("h1").text());
    }

    @Test
    public void testLoadWithValidCharset() throws IOException {
        String html = "<html><head><meta charset='UTF-8'></head><body><h1>こんにちは</h1></body></html>";

        Document doc = DataUtil.load(new ByteArrayInputStream(html.getBytes()), "UTF-8", "");
        assertNotNull(doc);
        assertEquals("こんにちは", doc.body().select("h1").text());
    }

    @Test
    public void testLoadWithBaseUri() throws IOException {
        String html = "<html><head><meta charset='UTF-8'></head><body><a href='http://example.com'>Link</a></body></html>";

        Document doc = DataUtil.load(new ByteArrayInputStream(html.getBytes()), "UTF-8", "http://example.com/base");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("http://example.com", doc.select("a").attr("href"));
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Hello World!";
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        DataUtil.crossStreams(new ByteArrayInputStream(input.getBytes()), out);

        String output = new String(out.toByteArray());
        assertEquals(input, output);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String input = "Hello World!";
        InputStream in = new ByteArrayInputStream(input.getBytes());

        ByteBuffer buffer = DataUtil.readToByteBuffer(in, input.getBytes().length);
        assertNotNull(buffer);
        int remaining = buffer.remaining();
        assertEquals(input.getBytes().length, remaining);
        assertEquals(input, new String(buffer.array()));
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String input = "Hello World!";
        InputStream in = new ByteArrayInputStream(input.getBytes());

        ByteBuffer buffer = DataUtil.readToByteBuffer(in, 5);
        assertNotNull(buffer);
        int remaining = buffer.remaining();
        assertEquals(5, remaining);
        assertEquals("Hello", new String(buffer.array()));
    }

    @Test
    public void testReadToByteBufferWithInvalidMaxSize() throws IOException {
        String input = "Hello World!";
        InputStream in = new ByteArrayInputStream(input.getBytes());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        byte[] bytes = out.toByteArray();

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, -1);
        assertEquals(ByteBuffer.wrap(bytes), byteBuffer);
    }
}