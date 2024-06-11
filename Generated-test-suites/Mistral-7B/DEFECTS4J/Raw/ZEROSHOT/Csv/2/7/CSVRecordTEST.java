import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 @Test
 public void testCSVRecordCreation() {
 String[] values = new String[]{"John", "Doe", "35", "john.doe@example.com"};
 Map<String, Integer> mapping = Map.of("Firstname", 0, "Lastname", 1, "Age", 2, "Email", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 assertEquals(1, record.getRecordNumber());
 assertArrayEquals(values, record.values());
 assertSame(mapping, record.mapping);
 assertNull(record.comment);
 }

 @Test
 public void testCSVRecordGetByIndex() {
 CSVRecord record = createRecordWithValues("John", "Doe", "35", "john.doe@example.com");
 assertEquals("John", record.get(0));
 assertEquals("Doe", record.get(1));
 assertEquals("35", record.get(2));
 assertEquals("john.doe@example.com", record.get(3));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCSVRecordGetByIndexOutOfBound() {
 CSVRecord record = createRecordWithValues("John", "Doe", "35", "john.doe@example.com");
 record.get(4);
 }

 @Test
 public void testCSVRecordGetByColumnName() {
 CSVRecord record = createRecordWithValues("John,Firstname", "Doe,Lastname", "35,Age", "john.doe@example.com,Email");
 assertEquals("John", record.get("Firstname"));
 assertEquals("Doe", record.get("Lastname"));
 assertEquals("35", record.get("Age"));
 assertEquals("john.doe@example.com", record.get("Email"));
 assertNull(record.get("NonExistentColumnName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordGetByColumnNameInvalidMapping() {
 CSVRecord record = createRecordWithValues("John,Firstname", "Doe,Lastname", "35,Age", "john.doe@example.com,Email");
 record.get("NonExistentColumnNameWithInvalidMapping");
 }

 @Test
 public void testCSVRecordIsConsistent() {
 CSVRecord record = createRecordWithValues("John,Firstname", "Doe,Lastname", "35,Age", "john.doe@example.com,Email");
 assertTrue(record.isConsistent());
 }

 @Test
 public void testCSVRecordIsNotConsistent() {
 CSVRecord record = createRecordWithValues("John,Firstname", null, "35,Age", "john.doe@example.com,Email");
 assertFalse(record.isConsistent());
 }

 @Test
 public void testCSVRecordIsMapped() {
 CSVRecord record = createRecordWithValues("John,Firstname", "Doe,Lastname", "35,Age", "john.doe@example.com,Email");
 assertTrue(record.isMapped("Firstname"));
 assertTrue(record.isMapped("Lastname"));
 assertTrue(record.isMapped("Age"));
 assertTrue(record.isMapped("Email"));
 }

 @Test
 public void testCSVRecordIsNotMapped() {
 CSVRecord record = createRecordWithValues("John,Firstname", "Doe,Lastname", "35,Age", "john.doe@example.com,Email");
 assertFalse(record.isMapped("NonExistentColumnName"));
 }

 @Test
 public void testCSVRecordIsSet() {
 CSVRecord record = createRecordWithValues("John,Firstname", "Doe,Lastname", "35,Age,InvalidAge", "john.doe@example.com,Email");
 assertFalse(record.isSet("InvalidAge"));
 assertTrue(record.isSet("Firstname"));
 assertTrue(record.isSet("Lastname"));
 assertTrue(record.isSet("Age"));
 assertTrue(record.isSet("Email"));
 }

 @Test
 public void testCSVRecordIterator() {
 CSVRecord record = createRecordWithValues("John,Firstname", "Doe,Lastname", "35,Age", "john.doe@example.com,Email");
 assertTrue(record.iterator().hasNext());
 assertEquals("John", record.iterator().next());
 assertTrue(record.iterator().hasNext());
 assertEquals("Doe", record.iterator().next());
 assertTrue(record.iterator().hasNext());
 assertEquals("35", record.iterator().next());
 assertTrue(record.iterator().hasNext());
 assertEquals("john.doe@example.com", record.iterator().next());
 assertFalse(record.iterator().hasNext());
 }

 @Test
 public void testCSVRecordGetComment() {
 CSVRecord record = createRecordWithComment("John,Firstname", "Doe,Lastname", "35,Age", "john.doe@example.com,Email", "Record comment");
 assertEquals("Record comment", record.getComment());
 }

 @Test
 public void testCSVRecordGetCommentNull() {
 CSVRecord record = createRecordWithValues("John,Firstname", "Doe,Lastname", "35,Age", "john.doe@example.com,Email");
 assertNull(record.getComment());
 }

 @Test
 public void testCSVRecordGetRecordNumber() {
 CSVRecord record = createRecordWithValues("John,Firstname", "Doe,Lastname", "35,Age", "john.doe@example.com,Email");
 assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testCSVRecordSize() {
 CSVRecord record = createRecordWithValues("John,Firstname", "Doe,Lastname", "35,Age", "john.doe@example.com,Email");
 assertEquals(4, record.size());
 }

 @Test
 public void testCSVRecordToString() {
 CSVRecord record = createRecordWithValues("John,Firstname", "Doe,Lastname", "35,Age", "john.doe@example.com,Email");
 assertEquals("[John, Doe, 35, john.doe@example.com]", record.toString());
 }

 private CSVRecord createRecordWithValues(String... values) {
 return new CSVRecord(values, null, null, 1);
 }

 private CSVRecord createRecordWithComment(String... values) {
 String[] newValues = Arrays.copyOf(values, values.length + 1);
 newValues[values.length] = "Record comment";
 return new CSVRecord(values, null, newValues[values.length], 1);
 }

}