import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private String[] values = new String[]{"Alice", "25", "alice@example.com"};
 private Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "Email", 2);
 private CSVRecord record = new CSVRecord(values, mapping, null, -1);

 @Test
 public void testGetValueByIndex() {
 String expected = "Alice";
 String actual = record.get(0);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetValueByName() {
 String expected = "Alice";
 String actual = record.get("Name");
 assertEquals(expected, actual);
 }

 @Test
 public void testIsConsistent() {
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 assertTrue(record.isMapped("Name"));
 }

 @Test
 public void testIsSet() {
 assertTrue(record.isSet("Name"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetValueByNameWhenNoMapping() {
 record.mapping = null;
 String actual = record.get("Name");
 fail("Expected IllegalStateException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueByIdexOutOfBounds() {
 int index = 3;
 String actual = record.get(index);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetValueByNameWhenColumnNotPresent() {
 String actual = record.get("UnknownColumn");
 assertNull(actual);
 }

 @Test
 public void testGetValueByNameWhenMappingSizeMismatch() {
 Map<String, Integer> wrongMapping = Map.of("Name", 0, "Age", 1, "Email", 3);
 CSVRecord recordWithWrongMapping = new CSVRecord(values, wrongMapping, null, -1);
 String actual = recordWithWrongMapping.get("Email");
 assertNull(actual);
 }
}