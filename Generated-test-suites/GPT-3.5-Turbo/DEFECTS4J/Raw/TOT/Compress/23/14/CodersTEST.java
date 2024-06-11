import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class CodersTest {

   private static final byte[] PASSWORD = "password".getBytes();

   private Coders.CoderId[] coderTable;
   private Coders.CoderId[] wrongCoderTable;

   @Before
   public void setUp() {
      coderTable = Coders.coderTable;
      wrongCoderTable = new Coders.CoderId[] {
         new Coders.CoderId(SevenZMethod.COPY, new Coders.CopyDecoder())
      };
   }

   @Test
   public void testAddDecoderWithSupportedMethod() {
      InputStream is = new ByteArrayInputStream("Some data".getBytes());
      Coder coder = new Coder(SevenZMethod.COPY.getId(), new byte[] {});
      try {
         InputStream result = Coders.addDecoder(is, coder, PASSWORD);
         assertNotNull(result);
      } catch (IOException e) {
         fail("Unexpected IOException");
      }
   }

   @Test(expected = IOException.class)
   public void testAddDecoderWithUnsupportedMethod() throws IOException {
      InputStream is = new ByteArrayInputStream("Some data".getBytes());
      Coder coder = new Coder(SevenZMethod.AES256SHA256.getId(), new byte[] {});
      Coders.addDecoder(is, coder, PASSWORD);
   }

   @Test
   public void testAddEncoderWithSupportedMethod() {
      OutputStream os = new ByteArrayOutputStream();
      SevenZMethod method = SevenZMethod.COPY;
      try {
         OutputStream result = Coders.addEncoder(os, method, PASSWORD);
         assertNotNull(result);
      } catch (IOException e) {
         fail("Unexpected IOException");
      }
   }

   @Test(expected = IOException.class)
   public void testAddEncoderWithUnsupportedMethod() throws IOException {
      OutputStream os = new ByteArrayOutputStream();
      SevenZMethod method = SevenZMethod.AES256SHA256;
      Coders.addEncoder(os, method, PASSWORD);
   }

   @Test
   public void testCopyDecoderDecode() {
      InputStream is = new ByteArrayInputStream("Some data".getBytes());
      Coder coder = new Coder(SevenZMethod.COPY.getId(), new byte[] {});
      Coders.CopyDecoder decoder = new Coders.CopyDecoder();
      try {
         InputStream result = decoder.decode(is, coder, PASSWORD);
         assertNotNull(result);
      } catch (IOException e) {
         fail("Unexpected IOException");
      }
   }

   @Test
   public void testCopyDecoderEncode() {
      OutputStream os = new ByteArrayOutputStream();
      Coders.CopyDecoder decoder = new Coders.CopyDecoder();
      try {
         OutputStream result = decoder.encode(os, PASSWORD);
         assertNotNull(result);
      } catch (IOException e) {
         fail("Unexpected IOException");
      }
   }

   @Test
   public void testLZMADecoderDecode() {
      InputStream is = new ByteArrayInputStream("Some data".getBytes());
      Coder coder = new Coder(SevenZMethod.LZMA.getId(), new byte[] {0, 0, 0, 0});
      Coders.LZMADecoder decoder = new Coders.LZMADecoder();
      try {
         InputStream result = decoder.decode(is, coder, PASSWORD);
         assertNotNull(result);
      } catch (IOException e) {
         fail("Unexpected IOException");
      }
   }

   @Test
   public void testDeflateDecoderDecode() {
      InputStream is = new ByteArrayInputStream("Some data".getBytes());
      Coder coder = new Coder(SevenZMethod.DEFLATE.getId(), new byte[] {});
      Coders.DeflateDecoder decoder = new Coders.DeflateDecoder();
      try {
         InputStream result = decoder.decode(is, coder, PASSWORD);
         assertNotNull(result);
      } catch (IOException e) {
         fail("Unexpected IOException");
      }
   }

   @Test
   public void testDeflateDecoderEncode() {
      OutputStream os = new ByteArrayOutputStream();
      Coders.DeflateDecoder decoder = new Coders.DeflateDecoder();
      try {
         OutputStream result = decoder.encode(os, PASSWORD);
         assertNotNull(result);
      } catch (IOException e) {
         fail("Unexpected IOException");
      }
   }

   @Test
   public void testBZIP2DecoderDecode() {
      InputStream is = new ByteArrayInputStream("Some data".getBytes());
      Coder coder = new Coder(SevenZMethod.BZIP2.getId(), new byte[] {});
      Coders.BZIP2Decoder decoder = new Coders.BZIP2Decoder();
      try {
         InputStream result = decoder.decode(is, coder, PASSWORD);
         assertNotNull(result);
      } catch (IOException e) {
         fail("Unexpected IOException");
      }
   }

   @Test
   public void testBZIP2DecoderEncode() {
      OutputStream os = new ByteArrayOutputStream();
      Coders.BZIP2Decoder decoder = new Coders.BZIP2Decoder();
      try {
         OutputStream result = decoder.encode(os, PASSWORD);
         assertNotNull(result);
      } catch (IOException e) {
         fail("Unexpected IOException");
      }
   }

   @Test
   public void testAES256SHA256DecoderDecode() {
      InputStream is = new ByteArrayInputStream("Some data".getBytes());
      Coder coder = new Coder(SevenZMethod.AES256SHA256.getId(), new byte[] {0, 0, 0});
      Coders.AES256SHA256Decoder decoder = new Coders.AES256SHA256Decoder();
      try {
         InputStream result = decoder.decode(is, coder, PASSWORD);
         assertNotNull(result);
      } catch (IOException e) {
         fail("Unexpected IOException");
      }
   }

}