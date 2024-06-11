import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class DataUtilTest {

 private InputStream inputStream;
 private OutputStream outputStream;
 private Document doc;

 @Before
 public void setUp() throws Exception {
 inputStream = new FileInputStream(new File("test.html"));;
 outputStream = new RandomAccessFile("output.html", "rw");
 }

 @Test
 public void testLoadFile() throws IOException {
 doc = DataUtil.load(new File("test.html"), null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 doc = DataUtil.load(inputStream, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithCharset() throws IOException {
 doc = DataUtil.load(inputStream, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithBaseUri() throws IOException {
 String baseUri = "https://example.com";
 doc = DataUtil.load(inputStream, null, baseUri);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 Parser parser = DataUtil.parser().xmlParser();
 doc = DataUtil.load(inputStream, null, null, parser);
 assertNotNull(doc);
 }

 @Test
 public void testCrossStreams() throws IOException {
 InputStream testInputStream = new FileInputStream(new File("test.html"));
 OutputStream testOutputStream = new RandomAccessFile("output.html", "rw");
 DataUtil.crossStreams(testInputStream, testOutputStream);
 testInputStream.close();
 testOutputStream.close();

 File outputFile = new File("output.html");
 Document expectedDoc = DataUtil.load(new File("test.html"), null, null);
 Document actualDoc = DataUtil.load(outputFile.getAbsolutePath(), null, null);
 assertEquals(expectedDoc.html(), actualDoc.html());
 }

 @Test(expected = IOException.class)
 public void testParseInputStreamWithInvalidFile() throws IOException {
 InputStream invalidFile = new FileInputStream(new File("nonexistentfile.html"));
 DataUtil.parseInputStream(invalidFile, null, null, Parser.htmlParser());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputStreamWithNullParser() throws IOException {
 InputStream inputStream = new FileInputStream(new File("test.html"));
 DataUtil.parseInputStream(inputStream, null, null, null);
 }
}