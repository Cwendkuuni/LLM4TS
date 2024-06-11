import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

   @Test
   public void testLoadFile() {
       // Typical use case
       // Create a sample file
       File file = new File("test.html");
       Document doc = null;
       try {
           doc = DataUtil.load(file, "UTF-8", "https://www.example.com");
       } catch (IOException e) {
           fail("IOException should not be thrown");
       }
       
       assertNotNull(doc);
   }

   @Test
   public void testLoadInputStream() {
       // Edge case: input stream is null
       InputStream in = null;
       try {
           DataUtil.load(in, "UTF-8", "https://www.example.com");
           fail("Should throw IOException for null input stream");
       } catch (IOException e) {
           assertTrue(e.getMessage().contains("null"));
       }
   }

   @Test
   public void testLoadInputStreamWithParser() {
       // Error scenario: invalid parser
       InputStream in = new ByteArrayInputStream("test".getBytes());
       try {
           DataUtil.load(in, "UTF-8", "https://www.example.com", null);
           fail("Should throw IOException for null parser");
       } catch (IOException e) {
           assertTrue(e.getMessage().contains("parser"));
       }
   }

   @Test
   public void testParseByteData() {
       // Error scenario: no charset specified
       ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
       try {
           DataUtil.parseByteData(byteData, null, "https://www.example.com", Parser.htmlParser());
           fail("Should throw IOException for null charset");
       } catch (IOException e) {
           assertTrue(e.getMessage().contains("charset"));
       }
   }

   @Test
   public void testReadToByteBuffer() {
       // Edge case: empty input stream
       InputStream in = new ByteArrayInputStream(new byte[0]);
       try {
           ByteBuffer byteData = DataUtil.readToByteBuffer(in);
           assertEquals(0, byteData.capacity());
       } catch (IOException e) {
           fail("IOException should not be thrown for empty input stream");
       }
   }

   @Test
   public void testGetCharsetFromContentType() {
       // Typical use case
       String contentType = "text/html; charset=UTF-8";
       String charset = DataUtil.getCharsetFromContentType(contentType);
       assertEquals("UTF-8", charset);
       
       // Edge case: no charset specified
       contentType = "text/html";
       charset = DataUtil.getCharsetFromContentType(contentType);
       assertNull(charset);
   }
}