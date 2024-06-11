import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.Before;
import java.io.*;
import java.nio.ByteBuffer;
import static org.junit.Assert.*;

public class DataUtilTest {

@Before
public void setUp() {
// setup code
}

@Test
public void testLoadFromFile() {
// Test typical use case
File file = new File("test.html");
String charsetName = "UTF-8";
String baseUri = "http://www.example.com";
try {
Document doc = DataUtil.load(file, charsetName, baseUri);
assertEquals("Document title", doc.title(), "Example Title");
} catch (IOException e) {
fail("Exception thrown: " + e.getMessage());
}
}

@Test
public void testLoadFromInputStream() {
// Test typical use case
InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
String charsetName = "UTF-8";
String baseUri = "http://www.example.com";
try {
Document doc = DataUtil.load(inputStream, charsetName, baseUri);
assertEquals("Document body", doc.body().text(), "Test Data");
} catch (IOException e) {
fail("Exception thrown: " + e.getMessage());
}
}

@Test
public void testLoadFromInputStreamWithParser() {
// Test typical use case
InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
String charsetName = "UTF-8";
String baseUri = "http://www.example.com";
Parser parser = Parser.htmlParser();
try {
Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
assertEquals("Document body", doc.body().text(), "Test Data");
} catch (IOException e) {
fail("Exception thrown: " + e.getMessage());
}

}

@Test
public void testParseByteData() {
// Test typical use case
ByteBuffer byteBuffer = ByteBuffer.wrap("test data".getBytes());
String charsetName = "UTF-8";
String baseUri = "http://www.example.com";
Parser parser = Parser.htmlParser();
Document doc = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);
assertEquals("Document base URI", doc.baseUri(), "http://www.example.com");
}

@Test
public void testReadToByteBuffer() {
// Test typical use case
InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
int maxSize = 100;
try {
ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
assertEquals("Byte buffer size", byteBuffer.capacity(), 9);
} catch (IOException e) {
fail("Exception thrown: " + e.getMessage());
}

// Test edge case
inputStream = new ByteArrayInputStream("test data".getBytes());
maxSize = 5;
try {
ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
assertEquals("Byte buffer size", byteBuffer.capacity(), 5);
} catch (IOException e) {
fail("Exception thrown: " + e.getMessage());
}
}

@Test
public void testGetCharsetFromContentType() {
// Test typical use case
String contentType = "text/html; charset=UTF-8";
String charset = DataUtil.getCharsetFromContentType(contentType);
assertEquals("Content charset", charset, "UTF-8");

// Test edge case
contentType = "text/html; charset=ISO-8859-1";
charset = DataUtil.getCharsetFromContentType(contentType);
assertEquals("Content charset", charset, "ISO-8859-1");

// Test error scenario
contentType = null;
charset = DataUtil.getCharsetFromContentType(contentType);
assertEquals("Content charset", charset, null);
}

}