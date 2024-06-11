import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Parser parser;
 private Random random;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataUtil = new DataUtil();
 parser = DataUtil.Parser.htmlParser();
 random = new Random();
 }

 @Test
 public void testLoadFile() throws IOException {
 // Test loading a file with default charset
 File file = new File("test.html");
 Document document = dataUtil.load(file, null, null);
 assertNotNull(document);
 // Test loading a file with specified charset
 String charset = "UTF-16";
 Document documentWithCharset = dataUtil.load(file, charset, null);
 assertNotNull(documentWithCharset);
 assertEquals(charset, documentWithCharset.outputSettings().charset());
 }

 @Test
 public void testLoadInputStream() throws IOException {
 // Test loading an InputStream with default charset
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test content".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document document = dataUtil.load(inputStream, null, null);
 assertNotNull(document);
 // Test loading an InputStream with specified charset
 ByteArrayOutputStream outputStreamWithBom = new ByteArrayOutputStream();
 outputStreamWithBom.write(new byte[]{(byte) 0xFEFF, 'T', 'e', 's', 't', ' ', 'c', 'o', 'n', 't', 'e', 'n', 't'});
 InputStream inputStreamWithBom = new ByteArrayInputStream(outputStreamWithBom.toByteArray());
 Document documentWithBom = dataUtil.load(inputStreamWithBom, "UTF-16", null);
 assertNotNull(documentWithBom);
 assertEquals("UTF-16", documentWithBom.outputSettings().charset());
 }

 @Test
 public void testParseByteData() throws IOException {
 // Test parsing ByteBuffer with default charset
 ByteBuffer byteBuffer = ByteBuffer.wrap("Test content".getBytes(StandardCharsets.UTF_8));
 Document document = dataUtil.parseByteData(byteBuffer, null, null, parser);
 assertNotNull(document);
 // Test parsing ByteBuffer with specified charset
 ByteBuffer byteBufferWithBom = ByteBuffer.wrap(new byte[]{(byte) 0xFEFF, 'T', 'e', 's', 't', ' ', 'c', 'o', 'n', 't', 'e', 'n', 't'});
 Document documentWithBom = dataUtil.parseByteData(byteBufferWithBom, "UTF-16", null, parser);
 assertNotNull(documentWithBom);
 assertEquals("UTF-16", documentWithBom.outputSettings().charset());
 }

 @Test
 public void testCrossStreams() throws IOException {
 // Test crossStreams with default charset
 ByteArrayInputStream inputStream = new ByteArrayInputStream("Test content".getBytes(StandardCharsets.UTF_8));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 dataUtil.crossStreams(inputStream, outputStream);
 assertEquals("Test content", outputStream.toString(StandardCharsets.UTF_8.name()));
 // Test crossStreams with specified charset
 ByteArrayInputStream inputStreamWithBom = new ByteArrayInputStream(new byte[]{(byte) 0xFEFF, 'T', 'e', 's', 't', ' ', 'c', 'o', 'n', 't', 'e', 'n', 't'});
 ByteArrayOutputStream outputStreamWithBom = new ByteArrayOutputStream();
 dataUtil.crossStreams(inputStreamWithBom, outputStreamWithBom);
 assertEquals("Test content", outputStreamWithBom.toString(StandardCharsets.UTF_16.name()));
 }

 @Test
 public void testParseContentTypeHeader() {
 String contentType = "text/html; charset=GBK";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("GBK", charset);
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertEquals(DataUtil.boundaryLength, boundary.length());
 }
}