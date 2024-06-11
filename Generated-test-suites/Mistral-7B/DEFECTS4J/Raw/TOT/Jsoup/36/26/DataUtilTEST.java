import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Parser parser;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataUtil = new DataUtil();
 parser = Parser.htmlParser();
 outputStream = new ByteArrayOutputStream();
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 Document document = dataUtil.load(file, "UTF-8", null);
 assertNotNull(document);
 }

 @Test
 public void testLoadFileWithInvalidCharset() throws IOException {
 File file = new File("test.html");
 Document document = dataUtil.load(file, null, null);
 assertEquals(DataUtil.defaultCharset, document.outputSettings().charset());
 }

 @Test
 public void testLoadInputStream() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes("UTF-8"));
 Document document = dataUtil.load(inputStream, "UTF-8", null);
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithNullCharset() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes("UTF-8"));
 Document document = dataUtil.load(inputStream, null, null);
 assertEquals(DataUtil.defaultCharset, document.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithDefaultParser() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes("UTF-8"));
 Document document = dataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithCustomParser() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test.html".getBytes("UTF-8"));
 Document document = dataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
 assertNull(document); // since thehtmlParser() is used by default in the class
 }

 @Test
 public void testParseByteDataWithEmptyInputStream() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[0]));
 Document document = dataUtil.parseByteData(byteData, null, null, parser);
 assertNull(document);
 }

 @Test
 public void testParseByteDataWithUTF8Bytes() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream("test.html".getBytes("UTF-8")));
 Document document = dataUtil.parseByteData(byteData, null, null, parser);
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithGB2312Bytes() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream("test.html".getBytes("GB2312")));
 Document document = dataUtil.parseByteData(byteData, null, null, parser);
 assertNull(document); // since the default charset is UTF-8
 }

 @Test
 public void testParseByteDataWithGB2312BytesAndGB2312Charset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream("test.html".getBytes("GB2312")));
 Document document = dataUtil.parseByteData(byteData, "GB2312", null, parser);
 assertNotNull(document);
 }
}