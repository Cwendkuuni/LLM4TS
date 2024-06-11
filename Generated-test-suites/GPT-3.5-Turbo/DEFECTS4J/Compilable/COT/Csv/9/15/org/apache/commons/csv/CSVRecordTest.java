package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

   @Test
   public void testGetEnum() {
      CSVRecord record = createCSVRecord();
      Assert.assertEquals("value1", record.get(TestEnum.TEST1));
      Assert.assertEquals("value2", record.get(TestEnum.TEST2));
   }

   @Test
   public void testGetInt() {
      CSVRecord record = createCSVRecord();
      Assert.assertEquals("value1", record.get(0));
      Assert.assertEquals("value2", record.get(1));
   }

   @Test(expected = IllegalStateException.class)
   public void testGetWithoutMapping() {
      CSVRecord record = createCSVRecordWithoutMapping();
      record.get("name");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testGetInvalidName() {
      CSVRecord record = createCSVRecord();
      record.get("invalidName");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testGetInvalidIndex() {
      CSVRecord record = createCSVRecord();
      record.get(2);
   }

   @Test
   public void testGetComment() {
      CSVRecord record = createCSVRecord();
      Assert.assertEquals("This is a comment", record.getComment());
   }

   @Test
   public void testGetRecordNumber() {
      CSVRecord record = createCSVRecord();
      Assert.assertEquals(1, record.getRecordNumber());
   }

   @Test
   public void testIsConsistent() {
      CSVRecord record1 = createCSVRecord();
      Assert.assertTrue(record1.isConsistent());

      CSVRecord record2 = createInconsistentCSVRecord();
      Assert.assertFalse(record2.isConsistent());
   }

   @Test
   public void testIsMapped() {
      CSVRecord record = createCSVRecord();
      Assert.assertTrue(record.isMapped("name"));
      Assert.assertTrue(record.isMapped("age"));
      Assert.assertFalse(record.isMapped("invalidName"));
   }

   @Test
   public void testIsSet() {
      CSVRecord record = createCSVRecord();
      Assert.assertTrue(record.isSet("name"));
      Assert.assertTrue(record.isSet("age"));
      Assert.assertFalse(record.isSet("invalidName"));
   }

   @Test
   public void testSize() {
      CSVRecord record = createCSVRecord();
      Assert.assertEquals(2, record.size());
   }

   @Test
   public void testToMap() {
      CSVRecord record = createCSVRecord();
      Map<String, String> expectedMap = new HashMap<>();
      expectedMap.put("name", "value1");
      expectedMap.put("age", "value2");
      Assert.assertEquals(expectedMap, record.toMap());
   }

   private CSVRecord createCSVRecord() {
      String[] values = {"value1", "value2"};
      Map<String, Integer> mapping = new HashMap<>();
      mapping.put("name", 0);
      mapping.put("age", 1);
      String comment = "This is a comment";
      long recordNumber = 1;
      return new CSVRecord(values, mapping, comment, recordNumber);
   }
   
   private CSVRecord createCSVRecordWithoutMapping() {
      String[] values = {"value1", "value2"};
      String comment = "This is a comment";
      long recordNumber = 1;
      return new CSVRecord(values, null, comment, recordNumber);
   }

   private CSVRecord createInconsistentCSVRecord() {
      String[] values = {"value1"};
      Map<String, Integer> mapping = new HashMap<>();
      mapping.put("name", 0);
      mapping.put("age", 1);
      String comment = "This is a comment";
      long recordNumber = 1;
      return new CSVRecord(values, mapping, comment, recordNumber);
   }

   private enum TestEnum {
      TEST1,
      TEST2
   }
}