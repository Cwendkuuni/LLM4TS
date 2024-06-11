package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.Parser.Tag;
import org.jsoup.selenium.internal.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class DataUtilTest {

 private File testFile;
 private InputStream testInputStream;
 private String baseUri = "http://example.com";
 private Parser parser;

 @Before
 public void setup() {
 testFile = new File("test.html");
 testInputStream = new FileInputStream(testFile);
 parser = Parser.htmlParser();
 }

 @Test
 public void testLoadFileWithCharset() throws IOException {
 Document doc = DataUtil.load(testFile, "UTF-8", baseUri);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithoutCharset() throws IOException {
 Document doc = DataUtil.load(testFile, null, baseUri);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", baseUri, parser);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithoutCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
 Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithWrongCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
 String charsetName = "GBK";
 assertThrows(IOException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
 String charsetName = "invalidCharset";
 assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
 }

 @Test
 public void testLoadInputStreamWithBOM() throws IOException {
 String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
 testFile.writeBytes(testData.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
 Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
 assertEquals("UTF-8", doc.outputSettings().charset());
 testFile.delete();
 }

 @Test
 public void testParseByteDataWithCharsetInContentType() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
 String header = "Content-Type: text/html; charset=GBK";
 Document doc = DataUtil.parseByteData(byteData, header, baseUri, parser);
 assertEquals("GBK", doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithCharsetInMetaTag() throws IOException {
 String metaTag = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">";
 testFile.writeBytes(metaTag.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(testInputStream), null, baseUri, parser);
 assertEquals("GBK", doc.outputSettings().charset());
 testFile.delete();
 }
}