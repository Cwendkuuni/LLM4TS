import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private CSVRecord record;

 @Test
 public void testCSVRecordWithValidValues() {
 String[] values = new String[]{"name", "age", "city"};
 Map<String, Integer> mapping = Map.of("name", 0, "age", 1, "city", 2);
 record = new CSVRecord(values, mapping, null, 1);

 Assert.assertEquals("name", record.get(0));
 Assert.assertEquals("age", record.get(1));
 Assert.assertEquals("city", record.get(2));
 Assert.assertEquals(3, record.size());
 }

 @Test
 public void testCSVRecordWithEmptyValues() {
 record = new CSVRecord(new String[0], null, null, 1);
 Assert.assertNull(record.get(0));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetWithNoMapping() {
 record = new CSVRecord(new String[]{"name", "age", "city"}, null, null, 1);
 record.get("age");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInvalidIndex() {
 record = new CSVRecord(new String[]{"name", "age", "city"}, null, null, 1);
 record.get(3);
 }

 @Test
 public void testIsConsistentWithValidMapping() {
 record = new CSVRecord(new String[]{"name", "age", "city"}, Map.of("name", 0, "age", 1, "city", 2), null, 1);
 Assert.assertTrue(record.isConsistent());
 }

 @Test
 public void testIsConsistentWithInvalidMapping() {
 record = new CSVRecord(new String[]{"name", "age", "city"}, null, null, 1);
 record.values = new String[2];
 Assert.assertFalse(record.isConsistent());
 }

 @Test
 public void testIsMappedWithValidMapping() {
 record = new CSVRecord(new String[]{"name", "age", "city"}, Map.of("name", 0, "age", 1, "city", 2), null, 1);
 Assert.assertTrue(record.isMapped("name"));
 Assert.assertTrue(record.isMapped("age"));
 Assert.assertTrue(record.isMapped("city"));
 }

 @Test
 public void testIsMappedWithInvalidMapping() {
 record = new CSVRecord(new String[]{"name", "age", "city"}, null, null, 1);
 Assert.assertFalse(record.isMapped("country"));
 }

 @Test
 public void testIsSetWithValidMapping() {
 record = new CSVRecord(new String[]{"name", "age", "city"}, Map.of("name", 0, "age", 1, "city", 2), null, 1);
 record.values = new String[]{"John Doe", "30", "New York"};
 Assert.assertTrue(record.isSet("name"));
 Assert.assertTrue(record.isSet("age"));
 Assert.assertTrue(record.isSet("city"));
 }

 @Test
 public void testIsSetWithInvalidMapping() {
 record = new CSVRecord(new String[]{"name", "age", "city"}, null, null, 1);
 record.values = new String[]{"John Doe", "30", "New York"};
 Assert.assertFalse(record.isSet("country"));
 }

 @Test
 public void testIterator() {
 record = new CSVRecord(new String[]{"name", "age", "city"}, null, null, 1);
 record.values = new String[]{"John Doe", "30", "New York"};
 Iterator<String> iterator = record.iterator();
 Assert.assertEquals("John Doe", iterator.next());
 Assert.assertEquals("30", iterator.next());
 Assert.assertEquals("New York", iterator.next());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testToString() {
 record = new CSVRecord(new String[]{"name", "age", "city"}, null, null, 1);
 record.values = new String[]{"John Doe", "30", "New York"};
 String expected = "[name = John Doe, age = 30, city = New York]";
 Assert.assertEquals(expected, record.toString());
 }

}