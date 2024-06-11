import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class DataUtilTest {

private static File testFile;
private static String charsetName = "UTF-8";
private static String baseUri = "http://example.com";

@BeforeClass
public static void setup() throws Exception {
testFile = new File("test.html"); // assume tests are run from project root
}

@Test
public void testLoadFile() throws Exception {
Document doc = DataUtil.load(testFile, charsetName, baseUri);
assertNotNull(doc);
}

@Test
public void testLoadInputStreamWithoutCharset() throws Exception {
InputStream in = new FileInputStream(testFile);
Document doc = DataUtil.load(in, null, baseUri);
assertNotNull(doc);
}

@Test
public void testLoadInputStreamWithCharset() throws Exception {
InputStream in = new FileInputStream(testFile);
Document doc = DataUtil.load(in, "ISO-8859-1", baseUri);
assertNotNull(doc);
}

@Test
public void testLoadInputStreamWithInvalidCharset() throws Exception {
InputStream in = new FileInputStream(testFile);
try {
Document doc = DataUtil.load(in, "invalidCharset", baseUri);
fail("Expected IOException");
} catch (IOException e) {
// Expected
}
}

@Test
public void testParseByteDataWithValidCharset() throws Exception {
ByteBuffer byteData = readToByteBuffer(new FileInputStream(testFile));
Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
assertNotNull(doc);
}

@Test
public void testParseByteDataWithMissingCharset() throws Exception {
ByteBuffer byteData = readToByteBuffer(new FileInputStream(testFile));
Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
assertNotNull(doc);
}

@Test
public void testParseByteDataWithInvalidCharset() throws Exception {
ByteBuffer byteData = readToByteBuffer(new FileInputStream(testFile));
Document doc = DataUtil.parseByteData(byteData, "invalidCharset", baseUri, Parser.htmlParser());
assertNotNull(doc);
}

private ByteBuffer readToByteBuffer(InputStream inStream) throws IOException {
byte[] buffer = new byte[bufferSize];
ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
int read;
while(true) {
read = inStream.read(buffer);
if (read == -1) break;
outStream.write(buffer, 0, read);
}
ByteBuffer byteData = ByteBuffer.wrap(outStream.toByteArray());
return byteData;
}
}