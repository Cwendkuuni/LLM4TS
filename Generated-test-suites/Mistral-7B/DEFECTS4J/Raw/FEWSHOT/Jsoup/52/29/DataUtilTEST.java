package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Hello World</h1></body></html>";
        File file = new File("tempFile.xml");
        try (RandomAccessFile fileWriter = new RandomAccessFile(file, "rw");
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            fileWriter.writeBytes(fileContent.getBytes(StandardCharsets.UTF_8));
            Document doc = DataUtil.load(file, null, null);
            assertEquals("Hello World", doc.select("h1").text());
            assertNotNull(doc.outputSettings().charset());
            assertEquals("UTF-8", doc.outputSettings().charset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataUtil.deleteFile(file);
    }

    @Test
    public void testLoadInputStream() {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, "UTF-8", null);
            assertEquals("Hello World", doc.select("h1").text());
            assertNotNull(doc.outputSettings().charset());
            assertEquals("UTF-8", doc.outputSettings().charset());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, "ISO-8859-1", null);
            assertNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testLoadInputStreamWithEmptyContent() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, "UTF-8", null);
            assertNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testParseByteData() {
        String documentContent = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(documentContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertEquals("Hello World", doc.select("h1").text());
        assertNotNull(doc.outputSettings().charset());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithBOM() {
        String documentContent = "\uFEFF<?xml version='1.0' encoding='UTF-16'?>\n<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(documentContent.getBytes(StandardCharsets.UTF_16));
        Document doc = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        assertEquals("Hello World", doc.select("h1").text());
        assertNotNull(doc.outputSettings().charset());
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String documentContent = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(documentContent.getBytes(StandardCharsets.UTF_8));
        assertEquals(null, DataUtil.parseByteData(byteBuffer, "ISO-8859-1", null, Parser.htmlParser()));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><h1>Hello World</h1></body></html>";
        File file = new File("tempFile.xml");
        try (RandomAccessFile fileWriter = new RandomAccessFile(file, "rw");
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            fileWriter.writeBytes(fileContent.getBytes(StandardCharsets.UTF_8));
            ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
            assertEquals(fileContent, Charset.forName("UTF-8").decode(byteData).toString());
        } finally {
            DataUtil.deleteFile(file);
        }
    }

    @Test
    public void testReadToByteBufferWithEmptyFile() throws IOException {
        File file = new File("tempFile.xml");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertEquals(DataUtil.emptyByteBuffer(), byteData);
        DataUtil.deleteFile(file);
    }

    @Test
    public void testReadToByteBufferWithInvalidFile() throws IOException {
        File file = new File("tempFile.xml");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteData);
        DataUtil.deleteFile(file);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String largeFileContent = new String(new byte[1024 * 1024], 0, 1024 * 1024);
        File largeFile = new File("tempFile.xml");
        try (RandomAccessFile fileWriter = new RandomAccessFile(largeFile, "rw")) {
            fileWriter.write(largeFileContent.getBytes(StandardCharsets.UTF_8));
            ByteBuffer byteData = DataUtil.readFileToByteBuffer(largeFile, 1023 * 1024);
            assertNotNull(byteData);
        } finally {
            DataUtil.deleteFile(largeFile);
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Hello World";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
             OutputStream outputStream2 = new ByteArrayOutputStream()) {
            DataUtil.crossStreams(inputStream, outputStream);
            assertEquals(inputContent, outputStream.toString());

            ByteArrayInputStream inputStream2 = new ByteArrayInputStream(outputStream.toByteArray());
            ByteArrayOutputStream outputStream3 = new ByteArrayOutputStream();
            DataUtil.crossStreams(inputStream2, outputStream3);
            assertEquals(inputContent, outputStream3.toString());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("GB2312", DataUtil.getCharsetFromContentType("text/html; charset=gb2312"));
        assertEquals("GB2312", DataUtil.getCharsetFromContentType("text/html; charset=gb_2312"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html; charset=unknown"));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(boundary.length(), 32);
        for (int i = 0; i < boundary.length(); i++) {
            char c = boundary.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '-' || c == '_');
        }
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer bomUTF8 = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF });
        ByteBuffer bomUTF16 = ByteBuffer.wrap(new byte[] { 0xFF, 0xFE });
        ByteBuffer bomUTF32 = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF, 0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00 });

        assertEquals("UTF-8", DataUtil.detectCharsetFromBom(bomUTF8, null));
        assertEquals("UTF-16", DataUtil.detectCharsetFromBom(bomUTF16, null));
        assertEquals("UTF-32", DataUtil.detectCharsetFromBom(bomUTF32, null));
        assertEquals(null, DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[] { 0x00, 0x01, 0x02, 0x03 }), null));
    }
}