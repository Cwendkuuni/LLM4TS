package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        File inputFile = new File("testFile.html");
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
            DataUtil.writeFile(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
            InputStream in = new FileInputStream(inputFile);
            Document doc = DataUtil.load(inputFile, "UTF-8", null);
            assertEquals("Hello World!", doc.body().text());
            in.close();
            inputFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        String streamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(streamContent.getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, "UTF-8", null);
            assertEquals("Hello World!", doc.body().text());
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
        String streamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\"/>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(streamContent.getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.load(inputStream, null, null);
        });
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithEmptyContent() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        assertNull(DataUtil.load(inputStream, "UTF-8", null));
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteData() {
        String charsetName = "UTF-8";
        ByteBuffer byteData = ByteBuffer.wrap(new byte[] {'H', 'e', 'l', 'l', 'o', ' '});
        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNotNull(doc);
        assertTrue(doc.body().text().isEmpty());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String charsetName = null;
        ByteBuffer byteData = ByteBuffer.wrap(new byte[] {'H', 'e', 'l', 'l', 'o', ' '});
        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNotNull(doc);
        assertTrue(doc.body().text().isEmpty());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String charsetName = "InvalidCharset";
        ByteBuffer byteData = ByteBuffer.wrap(new byte[] {'H', 'e', 'l', 'l', 'o', ' '});
        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        });
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'H', 'e', 'l', 'l', 'o'});
        DataUtil.crossStreams(inputStream, outputStream);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(byteData.getLong(0), 5L);
        assertEquals(byteData.get(0), 'H');
        assertEquals(byteData.get(4), 'o');
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'H', 'e', 'l', 'l', 'o', 'l', 'o', 'l'});
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 4);
        assertEquals(byteData.get(0), 'H');
        assertEquals(byteData.get(1), 'e');
        assertEquals(byteData.get(2), 'l');
        assertEquals(byteData.get(3), 'l');
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(charset, "UTF-8");
    }

    @Test
    public void testGetCharsetFromContentTypeWithEmptyContentType() {
        String contentType = "";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(mimeBoundary.length(), 32);
    }
}