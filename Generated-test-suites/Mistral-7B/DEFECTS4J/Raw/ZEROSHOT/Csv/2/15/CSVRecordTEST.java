import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordWithValues() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "female"}, null, null, 1);
 assertEquals("Alice", record.get(0));
 assertEquals("25", record.get(1));
 assertEquals("female", record.get(2));
 assertEquals(3, record.size());
 }

 @Test
 public void testCSVRecordWithEmptyValues() {
 CSVRecord record = new CSVRecord(CSVRecord.EMPTY_STRING_ARRAY, null, null, 1);
 assertEquals(0, record.size());
 }

 @Test
 public void testGetNullValue() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", null, "female"}, null, null, 1);
 assertNull(record.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetOutOfBounds() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "female"}, null, null, 1);
 record.get(3);
 }

 @Test
 public void testGetByColumnsName() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "Alice Age", "female"}, null, null, 1);
 assertEquals("Alice", record.get("Name"));
 assertEquals("Alice Age", record.get("Age"));
 assertNull(record.get("Gender"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetByColumnsNameWithoutMapping() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "Alice Age", "female"}, null, null, 1);
 record.get("Gender");
 }

 @Test
 public void testIsConsistent() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "Alice Age", "female"}, null, null, 1);
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsInconsistent() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "Alice Age", "female"}, null, null, 1);
 record.values = new String[2];
 assertFalse(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "Alice Age", "female"}, null, null, 1);
 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));
 assertFalse(record.isMapped("Gender"));
 }

 @Test
 public void testIsSet() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "Alice Age", "female"}, null, null, 1);
 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertFalse(record.isSet("Gender"));
 }

 @Test
 public void testIterator() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "Alice Age", "female"}, null, null, 1);
 Iterator<String> iterator = record.iterator();
 assertEquals("Alice", iterator.next());
 assertEquals("Alice Age", iterator.next());
 assertEquals("female", iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testToString() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "Alice Age", "female"}, null, null, 1);
 assertEquals("[Alice, Alice Age, female]", record.toString());
 }

 @Test
 public void testGetComment() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "Alice Age", "female"}, null, "This is a comment", 1);
 assertEquals("This is a comment", record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "Alice Age", "female"}, null, null, 1);
 assertEquals(1, record.getRecordNumber());
 }
}