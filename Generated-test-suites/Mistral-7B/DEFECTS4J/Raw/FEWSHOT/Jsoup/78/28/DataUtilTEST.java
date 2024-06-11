package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>";
        File file = new File("testFile.xml");
        file.createNewFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(fileContent);
        }

        Document doc = DataUtil.load(file, null, null);
        assertEquals("<root></root>", doc.html());

        file.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root></root>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("<root></root>", doc.html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Hello World";
        InputStream input = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        DataUtil.crossStreams(input, output);
        assertEquals(inputContent, output.toString(StandardCharsets.UTF_8.toString()));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(boundary.length(), 32);
    }

    @Test
    public void testInvalidCharset() {
        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.validateCharset("gb2312x");
        });
    }

    @Test
    public void testValidCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("GB2312", DataUtil.validateCharset("GB2312"));
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer bomUTF8 = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF});
        ByteBuffer bomUTF16 = ByteBuffer.wrap(new byte[]{0xFF, 0xFF}); // LE
        ByteBuffer bomUTF16BE = ByteBuffer.wrap(new byte[]{0xFF, 0xFE}); // BE
        ByteBuffer bomUTF32 = ByteBuffer.wrap(new byte[]{0x00, 0x00, 0xFE, 0xFF}); // BE

        assertEquals("UTF-8", DataUtil.detectCharsetFromBom(bomUTF8).charset);
        assertEquals("UTF-16", DataUtil.detectCharsetFromBom(bomUTF16).charset);
        assertEquals("UTF-16", DataUtil.detectCharsetFromBom(bomUTF16BE).charset);
        assertEquals("UTF-32", DataUtil.detectCharsetFromBom(bomUTF32).charset);

        assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[0])));
    }
}