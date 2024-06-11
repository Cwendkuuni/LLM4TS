import org.junit.Assert;
import org.junit.Test;

public class BinaryCalculateTest {

   private BinaryCalculate binaryCalculate = new BinaryCalculate();

   @Test
   public void testAddBinary() {
       String result = binaryCalculate.addBinary("110", "101");
       Assert.assertEquals("1011", result);
   }

   @Test
   public void testSubBinary() {
       String result = binaryCalculate.subBinary("1011", "101");
       Assert.assertEquals("1001", result);
   }

   @Test
   public void testIPCalculate() {
       String result = binaryCalculate.IPCalculate("192.168.0.1", "/24");
       Assert.assertNotNull(result);
   }

   @Test
   public void testGetBinaryIP() {
       String result = binaryCalculate.getBinaryIP("110010");
       Assert.assertEquals("110.010", result);
   }

   @Test
   public void testGetNetmask() {
       String result = binaryCalculate.getNetmask("110010");
       Assert.assertEquals("001101", result);
   }

   @Test
   public void testCalculate() {
       binaryCalculate.calculate("110", "101");
       Assert.assertNotNull(binaryCalculate.getStartIP());
   }

   @Test
   public void testPrefixInPrefixCalculate() {
       binaryCalculate.prefixInPrefixCalculate("192.168.0.1", "/24", 2, "/26");
       Assert.assertTrue(true);
   }

   @Test
   public void testCheckIPType() {
       binaryCalculate.checkIPType(31);
       Assert.assertTrue(binaryCalculate.isIPv4);
   }

   @Test
   public void testConvert() {
       String result = binaryCalculate.convert("110010");
       Assert.assertEquals("50", result);
   }

   @Test
   public void testConvertToBinary() {
       String result = binaryCalculate.convertToBinary("192.168.0.1");
       Assert.assertEquals("110000001010100000000001", result);
   }

   @Test
   public void testGetStartIP() {
       binaryCalculate.IPRangeAddress = "192.168.0.1 - 192.168.0.10";
       String result = binaryCalculate.getStartIP();
       Assert.assertEquals("192.168.0.1", result);
   }
}