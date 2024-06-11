import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private String[] values = new String[]{"Name", "Age", "City"};
 private Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "City", 2);

 @Test
 public void testGetByIdx() {
 CSVRecord record = new CSVRecord(values, mapping, null, 1L);
 for (int i = 0; i < record.size(); i++) {
 String result = record.get(i);
 String expected = record.values()[i];
 assertEquals(expected, result);
 }
 }

 @Test
 public void testGetByNameNullMapping() {
 CSVRecord record = new CSVRecord(values, null, null, 1L);
 assertNull(record.get("Name"));
 }

 @Test
 public void testGetByNameMissingColumn() {
 CSVRecord record = new CSVRecord(values, mapping, null, 1L);
 assertNull(record.get("Gender"));
 }

 @Test
 public void testGetByNameInvalidName() {
 CSVRecord record = new CSVRecord(values, mapping, null, 1L);
 assertNull(record.get(""));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetByNameMissingMapping() {
 CSVRecord record = new CSVRecord(values, null, null, 1L);
 record.get("Name");
 }

 @Test(expected = IllegalStateException.class)
 public void testGetByInvalidIndex() {
 CSVRecord record = new CSVRecord(values, mapping, null, 1L);
 record.get(-1);
 }

 @Test
 public void testIsConsistentTrue() {
 CSVRecord record = new CSVRecord(values, mapping, null, 1L);
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsConsistentFalse() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age"}, mapping, null, 1L);
 assertFalse(record.isConsistent());
 }

 @Test
 public void testIsMappedTrue() {
 CSVRecord record = new CSVRecord(values, mapping, null, 1L);
 assertTrue(record.isMapped("Name"));
 }

 @Test
 public void testIsMappedFalse() {
 CSVRecord record = new CSVRecord(values, null, null, 1L);
 assertFalse(record.isMapped("Name"));
 }

 @Test
 public void testIsSetFalse() {
 CSVRecord record = new CSVRecord(new String[]{"Name", null}, mapping, null, 1L);
 assertFalse(record.isSet("Name"));
 }

 @Test
 public void testIsSetTrue() {
 CSVRecord record = new CSVRecord(values, mapping, null, 1L);
 assertTrue(record.isSet("Name"));
 }

 @Test
 public void testIterator() {
 CSVRecord record = new CSVRecord(values, mapping, null, 1L);
 Iterator<String> iterator = record.iterator();
 for (int i = 0; i < record.size(); i++) {
 String result = iterator.next();
 String expected = record.get(i);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testToString() {
 CSVRecord record = new CSVRecord(values, mapping, null, 1L);
 String result = record.toString();
 String expected = "[Name, Age, City]";
 assertEquals(expected, result);
 }

}