import org.junit.Test;
import static org.junit.Assert.*;

public class CodersTest {

   Coders coders = new Coders();
  
   @Test
   public void testAddDecoder() throws IOException {
     InputStream is = new InputStream();
     Coder coder = new Coder();
     byte[] password = new byte[10];
     InputStream result = coders.addDecoder(is, coder, password);
     assertNotNull(result);
   }

   @Test
   public void testAddEncoder() throws IOException {
     OutputStream out = new OutputStream();
     SevenZMethod method = new SevenZMethod();
     byte[] password = new byte[10];
     OutputStream result = coders.addEncoder(out, method, password);
     assertNotNull(result);
   }
  
   @Test(expected = IOException.class)
   public void testAddDecoderUnsupportedCompressionMethod() throws IOException {
     InputStream is = new InputStream();
     Coder coder = new Coder();
     byte[] password = new byte[10];
     coders.addDecoder(is, coder, password);
   }

   @Test(expected = IOException.class)
   public void testAddEncoderUnsupportedCompressionMethod() throws IOException {
     OutputStream out = new OutputStream();
     SevenZMethod method = new SevenZMethod();
     byte[] password = new byte[10];
     coders.addEncoder(out, method, password);
   }

   @Test
   public void testCopyDecoderDecode() throws IOException {
     InputStream in = new InputStream();
     Coder coder = new Coder();
     byte[] password = new byte[10];
     InputStream result = coders.new CopyDecoder().decode(in, coder, password);
     assertEquals(in, result);
   }

   @Test
   public void testCopyDecoderEncode() throws IOException {
     OutputStream out = new OutputStream();
     byte[] password = new byte[10];
     OutputStream result = coders.new CopyDecoder().encode(out, password);
     assertEquals(out, result);
   }

   @Test
   public void testLZMADecoderDecode() throws IOException {
     InputStream in = new InputStream();
     Coder coder = new Coder();
     coder.properties = new byte[10];
     byte[] password = new byte[10];
     InputStream result = coders.new LZMADecoder().decode(in, coder, password);
     assertNotNull(result);
   }

   @Test(expected=IOException.class)
   public void testLZMADecoderDecodeInvalidDictSize() throws IOException {
     InputStream in = new InputStream();
     Coder coder = new Coder();
     coder.properties = new byte[]{1, 1, 1, 1};
     byte[] password = new byte[10];
     coders.new LZMADecoder().decode(in, coder, password);
   }

   @Test
   public void testDeflateDecoderDecode() throws IOException {
     InputStream in = new InputStream();
     Coder coder = new Coder();
     byte[] password = new byte[10];
     InputStream result = coders.new DeflateDecoder().decode(in, coder, password);
     assertNotNull(result);
   }

   @Test
   public void testDeflateDecoderEncode() throws IOException {
     OutputStream out = new OutputStream();
     byte[] password = new byte[10];
     OutputStream result = coders.new DeflateDecoder().encode(out, password);
     assertNotNull(result);
   }

   @Test
   public void testBZIP2DecoderDecode() throws IOException {
     InputStream in = new InputStream();
     Coder coder = new Coder();
     byte[] password = new byte[10];
     InputStream result = coders.new BZIP2Decoder().decode(in, coder, password);
     assertNotNull(result);
   }

   @Test
   public void testBZIP2DecoderEncode() throws IOException {
     OutputStream out = new OutputStream();
     byte[] password = new byte[10];
     OutputStream result = coders.new BZIP2Decoder().encode(out, password);
     assertNotNull(result);
   }

   @Test
   public void testAES256SHA256DecoderDecode() throws IOException {
     InputStream in = new InputStream();
     Coder coder = new Coder();
     coder.properties = new byte[10];
     byte[] passwordBytes = new byte[10];
     InputStream result = coders.new AES256SHA256Decoder().decode(in, coder, passwordBytes);
     assertNotNull(result);
   }

   @Test(expected=IOException.class)
   public void testAES256SHA256DecoderDecodeNullPassword() throws IOException {
     InputStream in = new InputStream();
     Coder coder = new Coder();
     coder.properties = new byte[10];
     byte[] passwordBytes = null;
     coders.new AES256SHA256Decoder().decode(in, coder, passwordBytes);
   }
}