import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

   private final CSVRecord record = new CSVRecord(new String[]{"a", "b", "c"},
        new HashMap<String, Integer>(), "comment", 1);

   @Test
   public void testGetByEnum() {
       Assert.assertEquals("a", record.get(TestEnum.A));
   }

   @Test
   public void testGetByIndex() {
       Assert.assertEquals("b", record.get(1));
   }

   @Test
   public void testGetByName() {
       Assert.assertEquals("c", record.get("c"));
   }

   @Test
   public void testGetComment() {
       Assert.assertEquals("comment", record.getComment());
   }

   @Test
   public void testGetRecordNumber() {
       Assert.assertEquals(1, record.getRecordNumber());
   }

   @Test
   public void testIsConsistent() {
       Assert.assertTrue(record.isConsistent());
   }

   @Test
   public void testIsMapped() {
       Assert.assertFalse(record.isMapped("d"));
       Assert.assertTrue(record.isMapped("a"));
   }

   @Test
   public void testIsSet() {
       Assert.assertFalse(record.isSet("d"));
       Assert.assertTrue(record.isSet("a"));
   }

   @Test
   public void testIterator() {
       Assert.assertTrue(record.iterator().hasNext());
       Assert.assertEquals("a", record.iterator().next());
   }

   @Test
   public void testPutIn() {
       Map<String, String> map = new HashMap<>();
       map.put("a", "value");
       map.put("b", "value");
       map.put("c", "value");
       Assert.assertEquals(map, record.putIn(new HashMap<>()));
   }

   @Test
   public void testSize() {
       Assert.assertEquals(3, record.size());
   }

   @Test
   public void testToList() {
       Assert.assertEquals(Arrays.asList("a", "b", "c"), record.toList());
   }

   @Test
   public void testToMap() {
       Map<String, String> map = new HashMap<>();
       map.put("a", "a");
       map.put("b", "b");
       map.put("c", "c");
       Assert.assertEquals(map, record.toMap());
   }

   @Test
   public void testToString() {
       Assert.assertEquals("[a, b, c]", record.toString());
   }

   @Test
   public void testValues() {
       Assert.assertArrayEquals(new String[]{"a", "b", "c"}, record.values());
   }

   private enum TestEnum {
       A
   }
}