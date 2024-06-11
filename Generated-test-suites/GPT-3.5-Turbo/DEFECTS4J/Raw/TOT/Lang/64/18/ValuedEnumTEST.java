import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {

   @Test
   public void testConstructor() {
       JavaVersionEnum java1_0 = new JavaVersionEnum("Java 1.0", 100);
       
       Assert.assertEquals("Java 1.0", java1_0.getName());
       Assert.assertEquals(100, java1_0.getValue());
   }

   @Test
   public void testGetEnumByName() {
       JavaVersionEnum java1_1 = JavaVersionEnum.getEnum("Java 1.1");
       JavaVersionEnum java1_2 = JavaVersionEnum.getEnum("Java 1.2");
       
       Assert.assertNotNull(java1_1);
       Assert.assertNotNull(java1_2);
       Assert.assertEquals("Java 1.1", java1_1.getName());
       Assert.assertEquals("Java 1.2", java1_2.getName());
       Assert.assertEquals(110, java1_1.getValue());
       Assert.assertEquals(120, java1_2.getValue());
   }

   @Test
   public void testGetEnumByValue() {
       JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(100);
       JavaVersionEnum java1_3 = JavaVersionEnum.getEnum(130);
       
       Assert.assertNotNull(java1_0);
       Assert.assertNotNull(java1_3);
       Assert.assertEquals("Java 1.0", java1_0.getName());
       Assert.assertEquals("Java 1.3", java1_3.getName());
       Assert.assertEquals(100, java1_0.getValue());
       Assert.assertEquals(130, java1_3.getValue());
   }
   
   @Test
   public void testGetEnumMap() {
       Map enumMap = JavaVersionEnum.getEnumMap();
       
       Assert.assertFalse(enumMap.isEmpty());
       Assert.assertTrue(enumMap.containsKey("Java 1.0"));
       Assert.assertTrue(enumMap.containsKey("Java 1.1"));
       Assert.assertTrue(enumMap.containsKey("Java 1.2"));
       Assert.assertTrue(enumMap.containsKey("Java 1.3"));
       Assert.assertEquals(100, ((JavaVersionEnum) enumMap.get("Java 1.0")).getValue());
       Assert.assertEquals(110, ((JavaVersionEnum) enumMap.get("Java 1.1")).getValue());
       Assert.assertEquals(120, ((JavaVersionEnum) enumMap.get("Java 1.2")).getValue());
       Assert.assertEquals(130, ((JavaVersionEnum) enumMap.get("Java 1.3")).getValue());
   }
   
   @Test
   public void testGetEnumList() {
       List enumList = JavaVersionEnum.getEnumList();
       
       Assert.assertFalse(enumList.isEmpty());
       Assert.assertTrue(enumList.get(0) instanceof JavaVersionEnum);
       Assert.assertTrue(enumList.get(1) instanceof JavaVersionEnum);
       Assert.assertTrue(enumList.get(2) instanceof JavaVersionEnum);
       Assert.assertTrue(enumList.get(3) instanceof JavaVersionEnum);
       Assert.assertEquals("Java 1.0", ((JavaVersionEnum) enumList.get(0)).getName());
       Assert.assertEquals("Java 1.1", ((JavaVersionEnum) enumList.get(1)).getName());
       Assert.assertEquals("Java 1.2", ((JavaVersionEnum) enumList.get(2)).getName());
       Assert.assertEquals("Java 1.3", ((JavaVersionEnum) enumList.get(3)).getName());
       Assert.assertEquals(100, ((JavaVersionEnum) enumList.get(0)).getValue());
       Assert.assertEquals(110, ((JavaVersionEnum) enumList.get(1)).getValue());
       Assert.assertEquals(120, ((JavaVersionEnum) enumList.get(2)).getValue());
       Assert.assertEquals(130, ((JavaVersionEnum) enumList.get(3)).getValue());
   }
   
   @Test
   public void testIterator() {
       Iterator iterator = JavaVersionEnum.iterator();
       
       Assert.assertNotNull(iterator);
       Assert.assertTrue(iterator.hasNext());
       Assert.assertTrue(iterator.next() instanceof JavaVersionEnum);
       Assert.assertTrue(iterator.hasNext());
       Assert.assertTrue(iterator.next() instanceof JavaVersionEnum);
       Assert.assertTrue(iterator.hasNext());
       Assert.assertTrue(iterator.next() instanceof JavaVersionEnum);
       Assert.assertTrue(iterator.hasNext());
       Assert.assertTrue(iterator.next() instanceof JavaVersionEnum);
       Assert.assertFalse(iterator.hasNext());
   }
}