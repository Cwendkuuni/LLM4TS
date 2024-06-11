import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 @Test
 public void testCSVRecordWithEmptyValues() {
 CSVRecord record = new CSVRecord(new String[0], null, null, 0L);
 assertTrue(record.isConsistent());
 assertFalse(record.isMapped("name"));
 assertFalse(record.isSet("name"));
 assertNull(record.get("name"));
 assertArrayEquals(new String[0], record.values());
 assertNull(record.getComment());
 assertNull(record.getRecordNumber());
 }

 @Test
 public void testCSVRecordWithValues() {
 String[] values = new String[]{"name1", "name2", "name3"};
 CSVRecord record = new CSVRecord(values, null, null, 0L);
 assertTrue(record.isConsistent());
 assertFalse(record.isMapped("name"));
 assertFalse(record.isSet("name"));
 assertNull(record.get("name"));
 assertArrayEquals(values, record.values());
 assertNull(record.getComment());
 assertNull(record.getRecordNumber());

 for (int i = 0; i < values.length; i++) {
 String value = record.get(i);
 assertEquals(values[i], value);
 }
 }

 @Test
 public void testCSVRecordWithMapping() {
 String[] values = new String[]{"name1", "name2", "name3"};
 Map<String, Integer> mapping = Map.of("column1", 0, "column2", 1, "column3", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);
 assertTrue(record.isConsistent());

 for (Map.Entry<String, Integer> entry : mapping.entrySet()) {
 String key = entry.getKey();
 int index = entry.getValue();
 String value = record.get(index);
 assertEquals(values[index], value);
 assertEquals(key, record.get(key));
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testGetWithNoMapping() {
 String[] values = new String[]{"name1", "name2", "name3"};
 CSVRecord record = new CSVRecord(values, null, null, 0L);
 assertNull(record.get("name"));
 }

 @Test
 public void testIsConsistentWithInvalidMappingSize() {
 String[] values = new String[]{"name1", "name2", "name3"};
 Map<String, Integer> mapping = Map.of("column1", 0, "column2", 1, "column4", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);
 assertFalse(record.isConsistent());
 }

 @Test
 public void testIsConsistentWithValidMappingSize() {
 String[] values = new String[]{"name1", "name2", "name3"};
 Map<String, Integer> mapping = Map.of("column1", 0, "column2", 1, "column3", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 String[] values = new String[]{"name1", "name2", "name3"};
 Map<String, Integer> mapping = Map.of("column1", 0, "column2", 1, "column3", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);
 assertTrue(record.isMapped("column1"));
 assertTrue(record.isMapped("column2"));
 assertTrue(record.isMapped("column3"));
 assertFalse(record.isMapped("nonExistentColumn"));
 }

 @Test
 public void testIsSet() {
 String[] values = new String[]{"name1", "name2", "name3"};
 Map<String, Integer> mapping = Map.of("column1", 0, "column2", 1, "column3", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0L);
 assertTrue(record.isSet("column1"));
 assertTrue(record.isSet("column2"));
 assertTrue(record.isSet("column3"));
 assertFalse(record.isSet("nonExistentColumn"));
 }

 @Test
 public void testGetComment() {
 String comment = "This is a comment";
 CSVRecord record = new CSVRecord(new String[0], null, comment, 0L);
 assertEquals(comment, record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 long recordNumber = 123L;
 CSVRecord record = new CSVRecord(new String[0], null, null, recordNumber);
 assertEquals(recordNumber, record.getRecordNumber());
 }
}