import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private static Document expectedHtmlDocument;
 private static File inputFile;
 private static InputStream inputStream;
 private static Parser parser;

 @BeforeClass
 public static void setup() {
 // Create a test HTML document for parsing
 String testHtml = "<html lang='en'>\n" +
 "  <head>\n" +
 "    <meta charset='UTF-8'>\n" +
 "    <meta http-equiv='Content-Type' content='text/html; charset=GBK'>\n" +
 "    <title>Test Document</title>\n" +
 "  </head>\n" +
 "  <body>\n" +
 "    <p>Hello, World!</p>\n" +
 "  </body>\n" +
 "</html>";
 expectedHtmlDocument = DataUtil.parseString(testHtml, Parser.htmlParser());

 // Create a test input file for loading
 inputFile = new File("test.html");
 try {
 FileWriter writer = new FileWriter(inputFile);
 writer.write(testHtml);
 writer.close();
 } catch (IOException e) {
 e.printStackTrace();
 }

 inputStream = new ByteArrayInputStream(testHtml.getBytes(StandardCharsets.UTF_8));
 parser = Mockito.mock(Parser.class);
}

 @Test
 public void testLoadFileUtf8() throws IOException {
 Document actualDocument = DataUtil.load(inputFile, "UTF-8", null);
 assertNotNull(actualDocument);
 assertEquals(expectedHtmlDocument, actualDocument);
}

 @Test
 public void testLoadFileUnknownCharset() throws IOException {
 Document actualDocument = DataUtil.load(inputFile, null, null);
 assertNotNull(actualDocument);
 assertEquals(expectedHtmlDocument, actualDocument);
}

 @Test
 public void testLoadFileNoMetaCharset() throws IOException {
 File inputFileNoMeta = new File("test_no_meta.html");
 String testHtmlNoMeta = "<html lang='en'>\n" +
 "  <head>\n" +
 "    <title>Test Document</title>\n" +
 "  </head>\n" +
 "  <body>\n" +
 "    <p>Hello, World!</p>\n" +
 "  </body>\n" +
 "</html>";
 try {
 FileWriter writer = new FileWriter(inputFileNoMeta);
 writer.write(testHtmlNoMeta);
 writer.close();
 } catch (IOException e) {
 e.printStackTrace();
 }

 Document actualDocument = DataUtil.load(inputFileNoMeta, null, null);
 assertNotNull(actualDocument);
 assertEquals(expectedHtmlDocument, actualDocument);
}

 @Test
 public void testLoadFileInvalidCharset() throws IOException {
 try {
 Document actualDocument = DataUtil.load(inputFile, "invalid_charset", null);
 fail("Expected IOException but no exception was thrown.");
 } catch (IOException e) {
 // Expected exception
 }
}

 @Test
 public void testLoadStreamUtf8() throws IOException {
 ByteArrayInputStream inputStreamUtf8 = new ByteArrayInputStream(expectedHtmlDocument.html().getBytes(StandardCharsets.UTF_8));
 Document actualDocument = DataUtil.load(inputStreamUtf8, "UTF-8", null);
 assertNotNull(actualDocument);
 assertEquals(expectedHtmlDocument, actualDocument);
}

 @Test
 public void testLoadStreamUnknownCharset() throws IOException {
 ByteArrayInputStream inputStreamUnknown = new ByteArrayInputStream(expectedHtmlDocument.html().getBytes(StandardCharsets.UTF_8));
 Document actualDocument = DataUtil.load(inputStreamUnknown, null, null);
 assertNotNull(actualDocument);
 assertEquals(expectedHtmlDocument, actualDocument);
}

 @Test
 public void testLoadStreamNoCharset() throws IOException {
 ByteArrayInputStream inputStreamNoCharset = new ByteArrayInputStream(expectedHtmlDocument.html().getBytes(StandardCharsets.UTF_8));
 Document actualDocument = DataUtil.load(inputStreamNoCharset, null, null);
 assertNotNull(actualDocument);
 assertEquals(expectedHtmlDocument, actualDocument);
}

 @Test
 public void testParseByteDataUtf8() throws IOException {
 ByteArrayInputStream inputStreamUtf8 = new ByteArrayInputStream(expectedHtmlDocument.html().getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStreamUtf8);
 Document actualDocument = DataUtil.parseByteData(byteData, "UTF-8", null, parser);
 assertNotNull(actualDocument);
 assertEquals(expectedHtmlDocument, actualDocument);
}

 @Test
 public void testParseByteDataUnknownCharset() throws IOException {
 ByteArrayInputStream inputStreamUnknown = new ByteArrayInputStream(expectedHtmlDocument.html().getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStreamUnknown);
 Document actualDocument = DataUtil.parseByteData(byteData, null, null, parser);
 assertNotNull(actualDocument);
 assertEquals(expectedHtmlDocument, actualDocument);
}

 @Test
 public void testParseByteDataHtml5() throws IOException {
 // Create a test HTML document for parsing with the "meta charset" tag
 String testHtmlHtml5 = "<html lang='en'>\n" +
 "  <head>\n" +
 "    <meta charset='UTF-8'>\n" +
 "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n" +
 "    <title>Test Document</title>\n" +
 "  </head>\n" +
 "  <body>\n" +
 "    <p>Hello, World!</p>\n" +
 "  </body>\n" +
 "</html>";
 ByteArrayInputStream inputStreamHtml5 = new ByteArrayInputStream(testHtmlHtml5.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteDataHtml5 = DataUtil.readToByteBuffer(inputStreamHtml5);
 Document actualDocumentHtml5 = DataUtil.parseByteData(byteDataHtml5, null, null, parser);
 assertNotNull(actualDocumentHtml5);
 assertEquals(DataUtil.parseString(testHtmlHtml5, Parser.htmlParser()), actualDocumentHtml5);
}

 @Test
 public void testParseByteDataNoMeta() throws IOException {
 // Create a test HTML document for parsing without the "meta charset" tag
 String testHtmlNoMeta = "<html lang='en'>\n" +
 "  <head>\n" +
 "    <title>Test Document</title>\n" +
 "  </head>\n" +
 "  <body>\n" +
 "    <p>Hello, World!</p>\n" +
 "  </body>\n" +
 "</html>";
 ByteArrayInputStream inputStreamNoMeta = new ByteArrayInputStream(testHtmlNoMeta.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteDataNoMeta = DataUtil.readToByteBuffer(inputStreamNoMeta);
 Document actualDocumentNoMeta = DataUtil.parseByteData(byteDataNoMeta, null, null, parser);
 assertNotNull(actualDocumentNoMeta);
 assertEquals(DataUtil.parseString(testHtmlNoMeta, Parser.htmlParser()), actualDocumentNoMeta);
}
}