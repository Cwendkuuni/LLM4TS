import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private CSVRecord record;

 @Test
 public void testGetByIndex() {
 String[] values = new String[]{"Name", "Age", "City"};
 Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
 record = new CSVRecord(values, mapping, null, 0);

 assertEquals("Name", record.get(0));
 assertEquals("Age", record.get(1));
 assertEquals("City", record.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetByIndexOutOfBounds() {
 String[] values = new String[]{"Name", "Age", "City"};
 Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
 record = new CSVRecord(values, mapping, null, 0);

 try {
 record.get(3);
 } catch (IndexOutOfBoundsException e) {
 fail("Expected IndexOutOfBoundsException but got: " + e.getMessage());
 }
 }

 @Test
 public void testGetByName() {
 String[] values = new String[]{"Name:John,Age:30,City:New York"};
 Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
 record = new CSVRecord(values, mapping, null, 0);

 assertEquals("John", record.get("Name"));
 assertNull(record.get("Unknown"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByNameMappedButNotSet() {
 String[] values = new String[]{"Name:John,Age:30,City:New York"};
 Map<String, Integer> mapping = Map.of("Name", 0, "Unknown", 2, "City", 1);
 record = new CSVRecord(values, mapping, null, 0);

 try {
 record.get("Name");
 record.get("Unknown");
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException but got: " + e.getMessage());
 }
 }

 @Test
 public void testIsConsistent() {
 String[] values = new String[]{"Name", "Age", "City"};
 Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
 record = new CSVRecord(values, mapping, null, 0);

 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsConsistentInconsistent() {
 String[] values = new String[]{"Name", "Age", "City"};
 Map<String, Integer> mapping = Map.of("Name", 0, "City", 2);
 record = new CSVRecord(values, mapping, null, 0);

 assertFalse(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 String[] values = new String[]{"Name:John,Age:30,City:New York"};
 Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
 record = new CSVRecord(values, mapping, null, 0);

 assertTrue(record.isMapped("Name"));
 assertFalse(record.isMapped("Unknown"));
 }

 @Test
 public void testIsSet() {
 String[] values = new String[]{"Name:John,Age:30,City:New York"};
 Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
 record = new CSVRecord(values, mapping, null, 0);

 assertTrue(record.isSet("Name"));
 assertFalse(record.isSet("Unknown"));
 }

 @Test
 public void testIterator() {
 String[] values = new String[]{"Name:John,Age:30,City:New York"};
 Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);
 record = new CSVRecord(values, mapping, null, 0);
 Iterator<String> iterator = record.iterator();

 assertEquals("Name:John", iterator.next());
 assertEquals("Age:30", iterator.next());
 assertEquals("City:New York", iterator.next());
 assertFalse(iterator.hasNext());
 }
}