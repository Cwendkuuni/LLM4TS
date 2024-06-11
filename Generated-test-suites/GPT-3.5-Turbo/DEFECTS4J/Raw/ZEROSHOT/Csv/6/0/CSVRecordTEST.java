import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

   @Test
   public void testGetByEnum() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
      assertEquals("value1", record.get(TestEnum.VALUE1));
      assertEquals("value2", record.get(TestEnum.VALUE2));
   }

   @Test
   public void testGetByIndex() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
      assertEquals("value1", record.get(0));
      assertEquals("value2", record.get(1));
   }

   @Test
   public void testGetByName() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
      assertEquals("value1", record.get("name1"));
      assertEquals("value2", record.get("name2"));
   }

   @Test(expected = IllegalStateException.class)
   public void testGetByNameNoMapping() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
      record.get("name1");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testGetByInvalidName() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
      record.get("invalid");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testGetByInvalidIndex() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
      record.get(2);
   }

   @Test
   public void testGetComment() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
      assertEquals("comment", record.getComment());
   }

   @Test
   public void testGetRecordNumber() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
      assertEquals(1, record.getRecordNumber());
   }

   @Test
   public void testIsConsistent() {
      CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
      assertTrue(record1.isConsistent());

      CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
      assertFalse(record2.isConsistent());
   }

   @Test
   public void testIsMapped() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
      assertTrue(record.isMapped("name1"));
      assertTrue(record.isMapped("name2"));
      assertFalse(record.isMapped("name3"));
   }

   @Test
   public void testIsSet() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
      assertTrue(record.isSet("name1"));
      assertTrue(record.isSet("name2"));
      assertFalse(record.isSet("name3"));
   }

   @Test
   public void testIterator() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
      Iterator<String> iterator = record.iterator();
      assertTrue(iterator.hasNext());
      assertEquals("value1", iterator.next());
      assertTrue(iterator.hasNext());
      assertEquals("value2", iterator.next());
      assertFalse(iterator.hasNext());
   }

   @Test
   public void testPutIn() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
      HashMap<String, String> map = new HashMap<>();
      map.put("name1", "value1");
      map.put("name2", "value2");
      assertEquals(map, record.putIn(new HashMap<String, String>()));
   }

   @Test
   public void testSize() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
      assertEquals(2, record.size());
   }

   @Test
   public void testToList() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
      assertEquals(Arrays.asList("value1", "value2"), record.toList());
   }

   @Test
   public void testToMap() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
      HashMap<String, String> map = new HashMap<>();
      map.put("name1", "value1");
      map.put("name2", "value2");
      assertEquals(map, record.toMap());
   }

   @Test
   public void testToString() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
      assertEquals("[value1, value2]", record.toString());
   }

   @Test
   public void testValues() {
      CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
      assertArrayEquals(new String[]{"value1", "value2"}, record.values());
   }
}

public enum TestEnum {
   VALUE1, VALUE2
}