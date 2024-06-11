import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private String[] testValues = new String[]{"John", "Doe", "j.doe@example.com"};
 private Map<String, Integer> testMapping = Map.of("FirstName", 0, "LastName", 1, "Email", 2);

 @Test
 public void testConstructor() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertArrayEquals(testValues, record.values);
 Assert.assertEquals(testMapping, record.mapping);
 Assert.assertNull(record.comment);
 Assert.assertEquals(1, record.recordNumber);
 }

 @Test
 public void testGet() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertEquals("John", record.get(0));
 Assert.assertEquals("Doe", record.get(1));
 Assert.assertEquals("j.doe@example.com", record.get(2));
 }

 @Test
 public void testGetNullMapping() {
 CSVRecord record = new CSVRecord(testValues, null, null, 1);
 try {
 record.get("FirstName");
 Assert.fail();
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testGetInvalidIndex() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 try {
 record.get(3);
 Assert.fail();
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testIsConsistent() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertTrue(record.isConsistent());
 }

 @Test
 public void testIsConsistentInconsistent() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe"}, testMapping, null, 1);
 Assert.assertFalse(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertTrue(record.isMapped("FirstName"));
 Assert.assertTrue(record.isMapped("LastName"));
 Assert.assertTrue(record.isMapped("Email"));
 Assert.assertFalse(record.isMapped("NonExistentColumn"));
 }

 @Test
 public void testIsSet() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertTrue(record.isSet("FirstName"));
 Assert.assertTrue(record.isSet("LastName"));
 Assert.assertTrue(record.isSet("Email"));
 Assert.assertFalse(record.isSet("NonExistentColumn"));
 }

 @Test
 public void testIterator() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Iterator<String> iterator = record.iterator();
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals("John", iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals("Doe", iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals("j.doe@example.com", iterator.next());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIteratorRemove() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Iterator<String> iterator = record.iterator();
 iterator.remove();
 }

 @Test
 public void testValues() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertArrayEquals(testValues, record.values());
 }

 @Test
 public void testGetComment() {
 CSVRecord record = new CSVRecord(testValues, testMapping, "comment", 1);
 Assert.assertEquals("comment", record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testSize() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertEquals(3, record.size());
 }

 @Test
 public void testToString() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertEquals("[John, Doe, j.doe@example.com]", record.toString());
 }
}