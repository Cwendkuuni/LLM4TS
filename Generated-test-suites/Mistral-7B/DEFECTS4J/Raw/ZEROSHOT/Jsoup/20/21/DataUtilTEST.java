package org.jsoup.helper;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class DataUtilTest {

 @Rule
 public static final ExternalResourceFolder resourceFolder = new ExternalResourceFolder();

 @BeforeClass
 public static void setUp() {
 // Set up anything static that needs to be initialized before tests run.
 }

 /**
 * Test method for {@link DataUtil#load(File, String, String)}.
 * @throws IOException
 */
 @Test
 public void testLoadFile() throws IOException {
 File file = new File(resourceFolder.getResourcePath("testFile.html"));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com/";
 Document expectedDoc = Parser.htmlParser().parseInput(new File("src/test/resources/testFile.html"), baseUri);
 Document actualDoc = DataUtil.load(file, charsetName, baseUri);
 assertEquals(expectedDoc, actualDoc);
 }

 /**
 * Test method for {@link DataUtil#load(InputStream, String, String)}.
 * @throws IOException
 */
 @Test
 public void testLoadInputStream() throws IOException {
 String html = "html content here";
 InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com/";
 Document expectedDoc = Parser.htmlParser().parseInput(html, baseUri);
 Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri);
 assertEquals(expectedDoc, actualDoc);
 }

 /**
 * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
 * @throws IOException
 */
 @Test
 public void testLoadInputStreamParser() throws IOException {
 String html = "html content here";
 InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com/";
 Parser parser = Parser.xmlParser();
 Document expectedDoc = parser.parseInput(html, baseUri);
 Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertEquals(expectedDoc, actualDoc);
 }

 /**
 * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}.
 * @throws IOException
 */
 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!', '\u0000'});
 String charsetName = "UTF-8";
 String baseUri = "http://example.com/";
 Parser parser = Parser.htmlParser();
 String expectedDocData = "<html>\nHello World!\n</html>";
 Document expectedDoc = parser.parseInput(expectedDocData, baseUri);
 Document actualDoc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals(expectedDoc, actualDoc);
 }

 /**
 * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
 * @throws Exception
 */
 @Test
 public void testGetCharsetFromContentType() throws Exception {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 String actualCharset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, actualCharset);
 }

 /**
 * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
 * @throws Exception
 */
 @Test (expected = AssertionError.class)
 public void testGetCharsetFromContentTypeNullContentType() {
 String contentType = null;
 String expectedCharset = "";
 String actualCharset = DataUtil.getCharsetFromContentType(contentType);
 fail("Expected " + expectedCharset + ", but was " + actualCharset);
 }

 /**
 * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
 * @throws Exception
 */
 @Test (expected = AssertionError.class)
 public void testGetCharsetFromContentTypeEmptyContentType() {
 String contentType = "";
 String expectedCharset = "";
 String actualCharset = DataUtil.getCharsetFromContentType(contentType);
 fail("Expected " + expectedCharset + ", but was " + actualCharset);
 }

 /**
 * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
 * @throws Exception
 */
 @Test
 public void testGetCharsetFromContentTypeNoCharset() throws Exception {
 String contentType = "text/html";
 String expectedCharset = "UTF-8"; // default charset
 String actualCharset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, actualCharset);
 }
}