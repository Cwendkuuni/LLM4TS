import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testGet_ValidIndex() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.values = new String[]{"John Doe", "30", "New York"};
 String expected = "John Doe";
 String actual = record.get(0);
 assertEquals(expected, actual);
 }

 @Test
 public void testGet_ValidName() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.mapping = new java.util.HashMap<>();
 record.mapping.put("Name", 0);
 record.mapping.put("Age", 1);
 record.values = new String[]{"John Doe", "30", "New York"};
 String expected = "30";
 String actual = record.get("Age");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testGet_NoMapping() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.values = new String[]{"John Doe", "30", "New York"};
 String name = "NonExistentColumn";
 record.get(name);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsConsistent_Inconsistent() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.values = new String[]{"John Doe", "30", "New York", "AnotherValue"};
 boolean actual = record.isConsistent();
 assertFalse(actual);
 }

 @Test
 public void testIsConsistent_Consistent() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.values = new String[]{"John Doe", "30", "New York"};
 boolean actual = record.isConsistent();
 assertTrue(actual);
 }

 @Test
 public void testIsMapped_Mapped() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.mapping = new java.util.HashMap<>();
 record.mapping.put("Name", 0);
 record.mapping.put("Age", 1);
 record.mapping.put("City", 2);
 boolean actual = record.isMapped("Name");
 assertTrue(actual);
 }

 @Test
 public void testIsMapped_NotMapped() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.mapping = new java.util.HashMap<>();
 record.mapping.put("Name", 0);
 record.mapping.put("Age", 1);
 boolean actual = record.isMapped("City2");
 assertFalse(actual);
 }

 @Test
 public void testIsSet_Set() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.mapping = new java.util.HashMap<>();
 record.mapping.put("Name", 0);
 record.mapping.put("Age", 1);
 record.mapping.put("City", 2);
 record.values = new String[]{"John Doe", "30", "New York"};
 boolean actual = record.isSet("Name");
 assertTrue(actual);
 }

 @Test
 public void testIsSet_NotSet() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.mapping = new java.util.HashMap<>();
 record.mapping.put("Name", 0);
 record.mapping.put("Age", 1);
 record.mapping.put("City", 2);
 record.values = new String[2];
 boolean actual = record.isSet("City");
 assertFalse(actual);
 }

 @Test
 public void testIterator() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.values = new String[]{"John Doe", "30", "New York"};
 Iterator<String> iterator = record.iterator();
 String expected = "John Doe";
 String actual = iterator.next();
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0L);
 record.values = new String[]{"John Doe", "30", "New York"};
 String expected = "[Name=John Doe, Age=30, City=New York]";
 String actual = record.toString();
 assertEquals(expected, actual);
 }
}