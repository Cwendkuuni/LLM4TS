import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private CSVRecord record;

 @Test
 public void testCSVRecordConstructor() {
 String[] values = new String[]{"Col1", "Col2", "Col3"};
 Map<String, Integer> mapping = Map.of("Col1", 0, "Col2", 1, "Col3", 2);
 String comment = "Test Comment";
 long recordNumber = 1;
 record = new CSVRecord(values, mapping, comment, recordNumber);

 Assert.assertArrayEquals(values, record.values());
 Assert.assertEquals(mapping, record.mapping);
 Assert.assertEquals(comment, record.getComment());
 Assert.assertEquals(recordNumber, record.getRecordNumber());
 }

 @Test
 public void testGetMethod() {
 record = new CSVRecord(new String[]{"Col1", "Col2", "Col3"}, null, null, 0);

 Assert.assertEquals("Col1", record.get(0));
 Assert.assertEquals("Col2", record.get(1));
 Assert.assertEquals("Col3", record.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodWithInvalidIndex() {
 record = new CSVRecord(new String[]{"Col1", "Col2", "Col3"}, null, null, 0);

 record.get(3);
 }

 @Test
 public void testGetMethodWithInvalidName() {
 record = new CSVRecord(new String[]{"Col1", "Col2", "Col3"}, null, null, 0);
 record.mapping = Map.of("Col1", 0, "Col2", 1, "Col3", 2);

 Assert.assertNull(record.get("InvalidName"));
 }

 @Test
 public void testIsConsistentMethod() {
 record = new CSVRecord(new String[]{"Col1", "Col2", "Col3"}, Map.of("Col1", 0, "Col2", 1, "Col3", 2), null, 0);
 record.mapping = Map.of("Col1", 0, "Col2", 1, "Col3", 2, "Col4", 3);

 Assert.assertTrue(record.isConsistent());

 record.mapping = Map.of("Col1", 0, "Col2", 1, "Col4", 3);

 Assert.assertFalse(record.isConsistent());
 }

 @Test
 public void testIsMappedMethod() {
 record = new CSVRecord(new String[]{"Col1", "Col2", "Col3"}, Map.of("Col1", 0, "Col2", 1, "Col3", 2), null, 0);

 Assert.assertTrue(record.isMapped("Col1"));
 Assert.assertTrue(record.isMapped("Col2"));
 Assert.assertTrue(record.isMapped("Col3"));

 Assert.assertFalse(record.isMapped("InvalidName"));
 }

 @Test
 public void testIsSetMethod() {
 record = new CSVRecord(new String[]{"Col1", "Col2", "Col3"}, Map.of("Col1", 0, "Col2", 1, "Col3", 2), null, 0);

 Assert.assertFalse(record.isSet("InvalidName"));

 record.mapping = Map.of("Col1", 0, "Col1", 0, "Col2", 1, "Col3", 2);

 Assert.assertTrue(record.isSet("Col1"));
 Assert.assertTrue(record.isSet("Col2"));
 Assert.assertFalse(record.isSet("Col4"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMethodWithNoMapping() {
 record = new CSVRecord(new String[]{"Col1", "Col2", "Col3"}, null, null, 0);
 record.get("Col1");
 }

 @Test
 public void testIteratorMethod() {
 record = new CSVRecord(new String[]{"Col1", "Col2", "Col3"}, null, null, 0);

 Iterator<String> iterator = record.iterator();

 Assert.assertEquals("Col1", iterator.next());
 Assert.assertEquals("Col2", iterator.next());
 Assert.assertEquals("Col3", iterator.next());

 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testSizeMethod() {
 record = new CSVRecord(new String[]{"Col1", "Col2", "Col3"}, null, null, 0);

 Assert.assertEquals(3, record.size());
 }

 @Test
 public void testToStringMethod() {
 record = new CSVRecord(new String[]{"Col1", "Col2", "Col3"}, null, null, 0);

 Assert.assertEquals("[Col1, Col2, Col3]", record.toString());
 }
}