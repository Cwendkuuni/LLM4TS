package org.jsoup.helper.test;

import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.jsoup.helper.DataUtil.load;
import static org.jsoup.helper.DataUtil.load; // for the second method overload
import static org.jsoup.helper.DataUtil.parseByteData;
import static org.jsoup.helper.DataUtil.getCharsetFromContentType;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File inputFile = new File("testFile.html");
        Document expectedDocument = Jsoup.parse(inputFile, StandardCharsets.UTF_8);

        Document actualDocument = load(inputFile, null, null);

        Assertions.assertThat(actualDocument).isNotNull();
        Assertions.assertThat(actualDocument).isEqualToIgnoringWhitespace(expectedDocument);

        String discoveredCharset = getCharsetFromContentType(expectedDocument.head().toString());
        Document documentWithNewCharset = load(inputFile, discoveredCharset, null);
        Assertions.assertThat(documentWithNewCharset).isNotNull();
        Assertions.assertThat(documentWithNewCharset).isEqualToIgnoringWhitespace(expectedDocument);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        File inputFile = new File("testFile.html");
        File tempFile = File.createTempFile("test", null);
        FileUtils.copyFile(inputFile, tempFile);
        InputStream inputStream = new FileInputStream(tempFile);

        Document expectedDocument = Jsoup.parse(inputStream, StandardCharsets.UTF_8.name());

        Document actualDocument = load(inputStream, null, null);

        Assertions.assertThat(actualDocument).isNotNull();
        Assertions.assertThat(actualDocument).isEqualToIgnoringWhitespace(expectedDocument);

        String discoveredCharset = getCharsetFromContentType(expectedDocument.head().toString());
        Document documentWithNewCharset = load(inputStream, discoveredCharset, null);
        Assertions.assertThat(documentWithNewCharset).isNotNull();
        Assertions.assertThat(documentWithNewCharset).isEqualToIgnoringWhitespace(expectedDocument);

        tempFile.delete();
        inputStream.close();
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        File inputFile = new File("testFile.xml");
        File tempFile = File.createTempFile("test", null);
        FileUtils.copyFile(inputFile, tempFile);
        InputStream inputStream = new FileInputStream(tempFile);

        Document expectedDocument = Jsoup.parse(inputStream, StandardCharsets.UTF_8.name(), Parser.xmlParser());

        Document actualDocument = load(inputStream, null, null, Parser.xmlParser());

        Assertions.assertThat(actualDocument).isNotNull();
        Assertions.assertThat(actualDocument).isEqualTo(expectedDocument);

        tempFile.delete();
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("largeTestFile.html");
        File outputFile = File.createTempFile("test", null);
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        Document document = load(outputFile, null, null);

        inputStream.close();
        outputStream.close();
        outputFile.delete();

        Assertions.assertThat(document).isNotNull();
        // Add assertions here to check the content of the loaded document.
    }
}