package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        String testContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "<title>Test Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>This is a test document.</p>\n" +
                "</body>\n" +
                "</html>";

        File tempFile = File.createTempFile("datautil-test", null);
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            outputStream.write(testContent.getBytes());

            Document document = DataUtil.load(tempFile, null, null);
            assertEquals("This is a test document.", document.body().text());
        } finally {
            tempFile.delete();
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testContent.getBytes());
        Document document = DataUtil.load(byteArrayInputStream, null, null);
        assertEquals("This is a test document.", document.body().text());
    }

    @Test
    public void testLoadWithCharset() throws IOException {
        String testContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "<title>Test Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>This is a test document.</p>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testContent.getBytes());
        Document document = DataUtil.load(byteArrayInputStream, "UTF-8", null);
        assertEquals("This is a test document.", document.body().text());

        ByteArrayInputStream byteArrayInputStreamWithBOM = new ByteArrayInputStream(new byte[]{(byte)0xef, (byte)0xbb, (byte)0xbf});
        byteArrayInputStream = new ByteArrayInputStream(testContent.getBytes(), 3);
        document = DataUtil.load(byteArrayInputStreamWithBOM, null, null);
        assertEquals("This is a test document.", document.body().text());
    }

    @Test
    public void testLoadWithBaseUri() throws IOException {
        String testContent = "<html>\n" +
                "<head>\n" +
                "<title>Test Document</title>\n" +
                "<link rel=\"stylesheet\" href=\"styles.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"content\">\n" +
                "<p>This is a test document.</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testContent.getBytes());
        Document document = DataUtil.load(byteArrayInputStream, null, "/styles.css");
        Element contentElement = document.select("div#content").first();
        assertEquals("This is a test document.", contentElement.text());
    }

    @Test
    public void testLoadWithParser() throws IOException {
        String testContent = "<xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<data>\n" +
                "<item name=\"apple\">\n" +
                "<color red=\"255\" green=\"0\" blue=\"0\"/>\n" +
                "</item>\n" +
                "<item name=\"banana\">\n" +
                "<color red=\"0\" green=\"255\" blue=\"0\"/>\n" +
                "</item>\n" +
                "</data>";

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testContent.getBytes());
        Document document = DataUtil.load(byteArrayInputStream, null, null, DataUtil.Parser.xmlParser());
        Element dataElement = document.select("data").first();
        assertEquals(2, dataElement.children().size());

        Element appleElement = dataElement.select("item[name=apple]").first();
        Element colorElement = appleElement.select("color").first();
        int[] appleColor = new int[]{255, 0, 0};
        assertArrayEquals(appleColor, new int[]{colorElement.select("red").get(0).text().getBytes()[0], colorElement.select("green").get(0).text().getBytes()[0], colorElement.select("blue").get(0).text().getBytes()[0]});

        Element bananaElement = dataElement.select("item[name=banana]").first();
        Element bananaColorElement = bananaElement.select("color").first();
        int[] bananaColor = new int[]{0, 255, 0};
        assertArrayEquals(bananaColor, new int[]{bananaColorElement.select("red").get(0).text().getBytes()[0], bananaColorElement.select("green").get(0).text().getBytes()[0], bananaColorElement.select("blue").get(0).text().getBytes()[0]});
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String testContent = "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "<title>Test Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>This is a test document.</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.wrap(testContent.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, null, null);
        assertEquals("This is a test document.", document.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String testContent = "<html>\n" +
                "<head>\n" +
                "<title>Test Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>This is a test document.</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.wrap(testContent.getBytes());
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, null);
        assertEquals("This is a test document.", document.body().text());

        ByteBuffer byteDataWithBOM = ByteBuffer.wrap(new byte[]{0xef, 0xbb, 0xbf}.concat(testContent.getBytes().substring(3).getBytes()));
        document = DataUtil.parseByteData(byteDataWithBOM, null, null, null);
        assertEquals("This is a test document.", document.body().text());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String testContent = "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
                "<title>Test Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>This is a test document.</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.wrap(testContent.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, null, null);
        assertEquals("This is a test document.", document.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        byte[] testData = "Test Data".getBytes();

        try (InputStream inputStream = new ByteArrayInputStream(testData)) {
            DataUtil.readToByteBuffer(inputStream);
            byte[] readData = byteArrayOutputStream.toByteArray();
            assertArrayEquals(testData, readData);
        }

        try (InputStream inputStream = new ByteArrayInputStream(testData)) {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            byte[] readData = new byte[byteBuffer.limit()];
            byteBuffer.get(readData);
            assertArrayEquals(testData, readData);
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html; charset=ISO-8859-1";
        String contentType3 = "text/html";

        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType1));
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType2));
        assertNull(DataUtil.getCharsetFromContentType(contentType3));
    }
}