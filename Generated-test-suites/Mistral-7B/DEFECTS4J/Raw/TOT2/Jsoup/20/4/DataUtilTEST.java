
package org.jsoup.helper;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tests for the DataUtil class.
 */
public class DataUtilTest {
    @TestFactory
    public Stream<DynamicTest> loadTests() {
        return Stream.of(
                dynamicTest("load file with valid charset", () -> {
                    File file = new File("test.html");
                    String charsetName = "UTF-8";
                    String baseUri = "http://example.com";
                    Document document = DataUtil.load(file, charsetName, baseUri);
                    assertNotNull(document);
                }),
                dynamicTest("load file with invalid charset", () -> {
                    File file = new File("test.html");
                    String charsetName = "invalidCharset";
                    String baseUri = "http://example.com";
                    assertThrows(IOException.class, () -> DataUtil.load(file, charsetName, baseUri));
                }),
                dynamicTest("load file with null baseUri", () -> {
                    File file = new File("test.html");
                    String charsetName = "UTF-8";
                    String baseUri = null;
                    assertThrows(NullPointerException.class, () -> DataUtil.load(file, charsetName, baseUri));
                }),
                dynamicTest("load input stream with valid charset", () -> {
                    InputStream inputStream = new FileInputStream("test.html");
                    String charsetName = "UTF-8";
                    String baseUri = "http://example.com";
                    Document document = DataUtil.load(inputStream, charsetName, baseUri);
                    assertNotNull(document);
                }),
                dynamicTest("load input stream with null inputStream", () -> {
                    InputStream inputStream = null;
                    String charsetName = "UTF-8";
                    String baseUri = "http://example.com";
                    assertThrows(NullPointerException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
                }),
                dynamicTest("load input stream with invalid charset", () -> {
                    InputStream inputStream = new ByteArrayInputStream("test.html".getBytes());
                    String charsetName = "invalidCharset";
                    String baseUri = "http://example.com";
                    assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
                }),
                dynamicTest("parse byte data with valid charset", () -> {
                    String htmlContent = "<html><body>Test</body></html>";
                    ByteBuffer byteData = ByteBuffer.allocate(htmlContent.length() + 10);
                    byteData.put(htmlContent.getBytes(StandardCharsets.UTF_8));
                    byteData.flip();
                    String charsetName = "UTF-8";
                    String baseUri = "http://example.com";
                    Parser parser = Parser.htmlParser();
                    Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
                    assertNotNull(document);
                }),
                dynamicTest("parse byte data with invalid charset", () -> {
                    String htmlContent = "<html><body>Test</body></html>";
                    ByteBuffer byteData = ByteBuffer.allocate(htmlContent.length() + 10);
                    byteData.put(htmlContent.getBytes(StandardCharsets.UTF_8));
                    byteData.flip();
                    String charsetName = "invalidCharset";
                    String baseUri = "http://example.com";
                    Parser parser = Parser.htmlParser();
                    assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
                })
        );
    }
}