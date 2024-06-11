import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

   private CpioArchiveOutputStream cpioOutputStream;
   private ByteArrayOutputStream outputStream;

   @Before
   public void setup() {
      outputStream = new ByteArrayOutputStream();
      cpioOutputStream = new CpioArchiveOutputStream(outputStream);
   }

   @Test
   public void testPutNextEntry() {
      CpioArchiveEntry entry = new CpioArchiveEntry();
      try {
         cpioOutputStream.putNextEntry(entry);
         assertNotNull(cpioOutputStream.getCpioEntry());
      } catch (IOException e) {
         fail("Exception thrown");
      }
   }

   @Test
   public void testInvalidNextEntry() {
      try {
         cpioOutputStream.putNextEntry(null);
         fail("NullPointerException not thrown");
      } catch (IOException e) {
         fail("IOException thrown");
      } catch (NullPointerException e) {
         // expected
      }
   }

   @Test
   public void testCloseArchiveEntry() {
      CpioArchiveEntry entry = new CpioArchiveEntry();
      try {
         cpioOutputStream.putNextEntry(entry);
         cpioOutputStream.write("Test".getBytes());
         cpioOutputStream.closeArchiveEntry();
         assertNull(cpioOutputStream.getCpioEntry());
      } catch (IOException e) {
         fail("Exception thrown");
      }
   }


   @Test
   public void testWrite() {
      CpioArchiveEntry entry = new CpioArchiveEntry();
      byte[] data = "Test".getBytes();
      try {
         cpioOutputStream.putNextEntry(entry);
         cpioOutputStream.write(data);
         assertArrayEquals(data, outputStream.toByteArray());
      } catch (IOException e) {
         fail("Exception thrown");
      }
   }

   @Test
   public void testFinish() {
      try {
         cpioOutputStream.finish();
         assertTrue(outputStream.size() > 0);
      } catch (IOException e) {
         fail("Exception thrown");
      }
   }

   @Test
   public void testClose() {
      try {
         cpioOutputStream.close();
         assertTrue(cpioOutputStream.isClosed());
      } catch (IOException e) {
         fail("Exception thrown");
      }
   }

}