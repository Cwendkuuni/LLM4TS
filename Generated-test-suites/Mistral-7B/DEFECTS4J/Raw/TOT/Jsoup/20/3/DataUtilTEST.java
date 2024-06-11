package org.jsoup.helper.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil methods.
 */
public class DataUtilTest {

 @Rule
 public ResourceFolder resourceFolder = new ResourceFolder();

 @Before
 public void setup() {
 }

 @After
 public void teardown() {
 }

 /**
 * Test loading a file with a known UTF-8 encoded content.
 * @throws IOException
 */
 @Test
 public void testLoadFileUtf8() throws IOException {
 String filePath = resourceFolder.getResourcePath("testFileUtf8.html");
 String expectedCharset = "UTF-8";
 String baseUri = "http://example.com";
 Document doc = DataUtil.load(new File(filePath), expectedCharset, baseUri);
 assertNotNull(doc);
 assertEquals(expectedCharset, doc.outputSettings().charset());
 }

 /**
 * Test loading a file with no charset specified.
 * @throws IOException
 */
 @Test
 public void testLoadFileNoCharset() throws IOException {
 String filePath = resourceFolder.getResourcePath("testFileUtf8.html");
 String baseUri = "http://example.com";
 Document doc = DataUtil.load(new File(filePath), null, baseUri);
 assertNotNull(doc);
 // Charset should be determined automatically
 assertNotEquals("UTF-8", doc.outputSettings().charset());
 // Check that the determined charset matches the actual charset
 Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
 String charset = meta != null ? meta.attr("charset") : null;
 assertNotNull(charset);
 assertEquals(charset, doc.outputSettings().charset());
 }

 /**
 * Test loading a file with an incorrect charset specified.
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testLoadFileIncorrectCharset() throws IOException {
 String filePath = resourceFolder.getResourcePath("testFileUtf8.html");
 String incorrectCharset = "ISO-8859-1";
 String baseUri = "http://example.com";
 try {
 Document doc = DataUtil.load(new File(filePath), incorrectCharset, baseUri);
 fail("Expected IOException when loading file with incorrect charset");
 } catch (IOException e) {
 // Expected
 }
 }

 /**
 * Test loading a malformed file (empty file).
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testLoadFileMalformed() throws IOException {
 String filePath = resourceFolder.getResourcePath("testFileMalformed.html");
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 try {
 DataUtil.load(new File(filePath), charsetName, baseUri);
 fail("Expected IOException when loading malformed file");
 } catch (IOException e) {
 // Expected
 }
 }

 /**
 * Test parsing a ByteBuffer with a known UTF-8 encoded content.
 * @throws IOException
 */
 @Test
 public void testParseByteDataUtf8() throws IOException {
 String expectedCharset = "UTF-8";
 String baseUri = "http://example.com";
 byte[] utf8Data = "Some UTF-8 data".getBytes(Charset.forName("UTF-8"));
 ByteBuffer byteData = ByteBuffer.wrap(utf8Data);
 Document doc = DataUtil.parseByteData(byteData, expectedCharset, baseUri, Parser.htmlParser());
 assertNotNull(doc);
 assertEquals(expectedCharset, doc.outputSettings().charset());
 }

 /**
 * Test parsing a ByteBuffer with an incorrect charset.
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testParseByteDataIncorrectCharset() throws IOException {
 String incorrectCharset = "ISO-8859-1";
 String baseUri = "http://example.com";
 String data = "Some ISO-8859-1 data";
 byte[] iso88591Data = data.getBytes(Charset.forName("ISO-8859-1"));
 ByteBuffer byteData = ByteBuffer.wrap(iso88591Data);
 try {
 Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
 fail("Expected IOException when parsing ByteBuffer with incorrect charset");
 } catch (IOException e) {
 // Expected
 }
 }

 /**
 * Test parsing a ByteBuffer with no charset specified.
 * @throws IOException
 */
 @Test
 public void testParseByteDataNoCharset() throws IOException {
 String baseUri = "http://example.com";
 byte[] utf8Data = "Some UTF-8 data".getBytes(Charset.forName("UTF-8"));
 ByteBuffer byteData = ByteBuffer.wrap(utf8Data);
 Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
 assertNotNull(doc);
 // Charset should be determined automatically
 assertNotEquals("UTF-8", doc.outputSettings().charset());
 // Check that the determined charset matches the actual charset
 Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
 String charset = meta != null ? meta.attr("charset") : null;
 assertNotNull(charset);
 assertEquals(charset, doc.outputSettings().charset());
 }

 /**
 * Test parsing a ByteBuffer with an empty input.
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testParseByteDataEmpty() throws IOException {
 String baseUri = "http://example.com";
 ByteBuffer byteData = ByteBuffer.allocate(0);
 try {
 Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
 fail("Expected IOException when parsing empty ByteBuffer");
 } catch (IOException e) {
 // Expected
 }
 }
}