package org.jsoup.helper.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.jsoup.helper.DataUtil.defaultCharset;
import static org.jsoup.helper.DataUtil.load;
import static org.jsoup.helper.DataUtil.mimeBoundary;
import static org.jsoup.helper.DataUtil.parseByteData;

class DataUtilTest {

    @Test
    void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        String charsetName = "UTF-8";
        File file = new File("testFile.html");
        File tempFile = new File("tempFile.html");

        // Normal case, file exists and can be read
        writeFileContent(file, fileContent);
        Document doc = DataUtil.load(file, charsetName, null);
        Assertions.assertEquals(fileContent, doc.body().text());

        // Edge case, file does not exist
        doc = DataUtil.load(new File("nonexistentFile.html"), charsetName, null);
        Assertions.assertNull(doc);

        // Error case, invalid file path
        doc = DataUtil.load(null, charsetName, null);
        Assertions.assertThrows(IOException.class, () -> doc = DataUtil.load(file, null, null));

        // Clean up
        tempFile.deleteOnExit();
        file.deleteOnExit();
    }

    @Test
    void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        String charsetName = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName(charsetName)));

        // Normal case, input stream contains valid data
        Document doc = DataUtil.load(inputStream, charsetName, null);
        Assertions.assertEquals(inputStreamContent, doc.body().text());

        // Edge case, input stream is empty
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        inputStream.close();
        doc = DataUtil.load(new ByteArrayInputStream(outputStream.toByteArray()), charsetName, null);
        Assertions.assertNull(doc);

        // Error case, input stream is null
        Assertions.assertThrows(IOException.class, () -> DataUtil.load(null, charsetName, null));

        // Clean up
        inputStream.close();
    }

    @Test
    void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        String charsetName = "UTF-8";
        Parser parser = Parser.xmlParser();
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName(charsetName)));

        // Normal case, input stream contains valid data and parser is provided
        Document doc = DataUtil.load(inputStream, charsetName, null, parser);
        Assertions.assertEquals(inputStreamContent, doc.body().text());

        // Edge case, input stream is empty
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        inputStream.close();
        doc = DataUtil.load(new ByteArrayInputStream(outputStream.toByteArray()), charsetName, null, parser);
        Assertions.assertNull(doc);

        // Error case, input stream is null
        Assertions.assertThrows(IOException.class, () -> DataUtil.load(null, charsetName, null, parser));

        // Clean up
        inputStream.close();
    }

    @Test
    void testParseByteData() throws IOException {
        String fileContent = "This is a test file.";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("testFile.html");

        // Normal case, file exists and can be read
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assertions.assertEquals(fileContent, doc.body().text());

        // Edge case, file does not exist
        byteData = DataUtil.emptyByteBuffer();
        doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assertions.assertNull(doc);

        // Error case, input stream is null
        Assertions.assertThrows(IOException.class, () -> DataUtil.parseByteData(null, charsetName, baseUri, Parser.htmlParser()));
    }

    @Test
    void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assertions.assertEquals(charset, "UTF-8");

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assertions.assertNull(charset);
    }

    @Test
    void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assertions.assertNotNull(boundary);
        Assertions.assertEquals(boundary.length(), 32);
    }

    private void writeFileContent(File file, String content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        }
    }
}