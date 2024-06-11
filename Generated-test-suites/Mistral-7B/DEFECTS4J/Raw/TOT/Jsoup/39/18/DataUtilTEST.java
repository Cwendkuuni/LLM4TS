import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 private Document document;
 private InputStream inputStream;

 @Before
 public void setUp() throws IOException {
 String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><div>Test Data</div></body></html>";
 byte[] htmlBytes = htmlContent.getBytes(StandardCharsets.UTF_8);
 inputStream = new ByteArrayInputStream(htmlBytes);
 document = DataUtil.load(inputStream, "UTF-8", "");
 }

 @Test
 public void testLoadFile() throws IOException {
 File testFile = new File("testFile.html");
 Document expectedDocument = DataUtil.load(testFile, null, "");
 Document actualDocument = DataUtil.load(inputStream, null, testFile.getAbsolutePath());
 assertEquals(expectedDocument.toString(), actualDocument.toString());
 }

 @Test
 public void testLoadInputStreamWithCharset() throws IOException {
 Document documentWithCharset = DataUtil.load(inputStream, "UTF-8", "");
 assertEquals(document.toString(), documentWithCharset.toString());
 }

 @Test
 public void testLoadInputStreamWithoutCharset() throws IOException {
 Document documentWithoutCharset = DataUtil.load(inputStream, null, "");
 assertEquals(document.toString(), documentWithoutCharset.toString());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 byte[] expectedBytes = new byte[]{(byte) 0x3C, (byte) 0x68, (byte) 0x6F, (byte) 0x6D, (byte) 0x65, (byte) 0x20, (byte) 0x68, (byte) 0x74, (byte) 0x6D, (byte) 0x6C, (byte) 0x20, (byte) 0x73, (byte) 0x63, (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x20, (byte) 0x75, (byte) 0x73, (byte) 0x68, (byte) 0x20, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x20, (byte) 0x64, (byte) 0x69, (byte) 0x73, (byte) 0x70, (byte) 0x6C, (byte) 0x69, (byte) 0x63, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x20, (byte) 0x74, (byte) 0x68, (byte) 0x69, (byte) 0x73, (byte) 0x73, (byte) 0x20, (byte) 0x69, (byte) 0x6E, (byte) 0x66, (byte) 0x6F, (byte) 0x63, (byte) 0x74, (byte) 0x68, (byte) 0x2F, (byte) 0x75, (byte) 0x73, (byte) 0x68, (byte) 0x2F, (byte) 0x62, (byte) 0x65, (byte) 0x20, (byte) 0x68, (byte) 0x6D, (byte) 0x6F, (byte) 0x6D, (byte) 0x65, (byte) 0x20, (byte) 0x69, (byte) 0x73, (byte) 0x20, (byte) 0x63, (byte) 0x61, (byte) 0x6D, (byte) 0x65, (byte) 0x20, (byte) 0x76, (byte) 0x65, (byte) 0x63, (byte) 0x74, (byte) 0x6F, (byte) 0x72, (byte) 0x20, (byte) 0x74, (byte) 0x68, (byte) 0x69, (byte) 0x73, (byte) 0x74, (byte) 0x3E, (byte) 0x3C, (byte) 0x21};
 ByteBuffer expectedByteBuffer = ByteBuffer.wrap(expectedBytes);
 ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream, expectedBytes.length);
 byte[] actualBytes = new byte[expectedBytes.length];
 actualByteBuffer.get(actualBytes);
 assertArrayEquals(expectedBytes, actualBytes);
 }

 @Test
 public void testReadToByteBufferWithMaxSize() throws IOException {
 ByteBuffer expectedByteBuffer = ByteBuffer.allocate(1024);
 byte[] expectedBytes = new byte[1024];
 int read = 0;
 while ((read = inputStream.read(expectedBytes)) > 0) {
 expectedByteBuffer.put(expectedBytes, 0, read);
 }
 ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
 byte[] actualBytes = new byte[1024];
 actualByteBuffer.get(actualBytes);
 assertArrayEquals(expectedBytes, actualBytes);
 }

 @Test(expected = IOException.class)
 public void testReadToByteBufferNegativeMaxSize() throws IOException {
 DataUtil.readToByteBuffer(inputStream, -1);
 }
}