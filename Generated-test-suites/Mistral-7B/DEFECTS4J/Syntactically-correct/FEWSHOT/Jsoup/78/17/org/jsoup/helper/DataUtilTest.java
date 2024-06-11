package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<meta charset=\"UTF-8\">" +
                "</head>" +
                "<body>" +
                "<xml encoding=\"ISO-8859-1\"></xml>" +
                "</body>" +
                "</html>";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<meta charset=\"UTF-8\">" +
                "</head>" +
                "<body>" +
                "<xml encoding=\"ISO-8859-1\"></xml>" +
                "</body>" +
                "</html>", doc.html());

        inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<meta charset=\"UTF-8\">" +
                "</head>" +
                "<body>" +
                "<xml encoding=\"ISO-8859-1\"></xml>" +
                "</body>" +
                "</html>", doc.html());

        inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        assertEquals("<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<meta charset=\"UTF-8\">" +
                "</head>" +
                "<body>" +
                "<xml encoding=\"ISO-8859-1\"></xml>" +
                "</body>" +
                "</html>", doc.xml().asXml());

        inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseInputStream(inputStream, "ISO-8859-1", null, Parser.htmlParser());
        assertEquals("<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<meta charset=\"UTF-8\">" +
                "</head>" +
                "<body>" +
                "<xml encoding=\"ISO-8859-1\"></xml>" +
                "</body>" +
                "</html>", doc.html());

        inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.parseInputStream(inputStream, "ISO-8859-1", null, Parser.xmlParser());
        assertEquals("<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<meta charset=\"UTF-8\">" +
                "</head>" +
                "<body>" +
                "<xml encoding=\"ISO-8859-1\"></xml>" +
                "</body>" +
                "</html>", doc.xml().asXml());

        inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(inputStream, null, null, null));
    }

    @Test
    public void testLoad() throws IOException {
        String filePath = "src/test/resources/DataUtilTest.class";
        File file = new File(filePath);
        Document doc = DataUtil.load(file, null, null);
        assertNotNull(doc);

        String html = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<meta charset=\"UTF-8\">" +
                "</head>" +
                "<body>" +
                "<xml encoding=\"ISO-8859-1\"></xml>" +
                "</body>" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.load(inputStream, null, null);
        assertEquals("<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<meta charset=\"UTF-8\">" +
                "</head>" +
                "<body>" +
                "<xml encoding=\"ISO-8859-1\"></xml>" +
                "</body>" +
                "</html>", doc.html());

        inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.load(inputStream, "ISO-8859-1", null);
        assertEquals("<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<meta charset=\"UTF-8\">" +
                "</head>" +
                "<body>" +
                "<xml encoding=\"ISO-8859-1\"></xml>" +
                "</body>" +
                "</html>", doc.html());

        inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        doc = DataUtil.load(inputStream, "ISO-8859-1", null, Parser.xmlParser());
        assertEquals("<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<meta charset=\"UTF-8\">" +
                "</head>" +
                "<body>" +
                "<xml encoding=\"ISO-8859-1\"></xml>" +
                "</body>" +
                "</html>", doc.xml().asXml());

        inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, null, null, null));
    }

    @Test
    public void testCrossStreams() throws IOException {
        String src = "Hello World!";
        byte[] buffer = new byte[bufferSize];
        RandomAccessFile file = new RandomAccessFile("src/test/resources/DataUtilTestCrossStreams.txt", "rw");
        file.writeBytes(src.getBytes(StandardCharsets.UTF_8));

        InputStream inputStream = new FileInputStream("src/test/resources/DataUtilTestCrossStreams.txt");
        OutputStream outputStream = new FileOutputStream("src/test/resources/DataUtilTestCrossStreamsOutput.txt");

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals(src, new String(new FileInputStream("src/test/resources/DataUtilTestCrossStreamsOutput.txt").readAllBytes(), StandardCharsets.UTF_8));

        inputStream.close();
        outputStream.close();
        file.close();
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertTrue(boundary.length() >= boundaryLength);
    }
}

class TestUtils {
    static Document createTestDocument() {
        String html = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<meta charset=\"UTF-8\">" +
                "</head>" +
                "<body>" +
                "<xml encoding=\"ISO-8859-1\"></xml>" +
                "</body>" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        return doc;
    }
}