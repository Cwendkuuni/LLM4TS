import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private CSVRecord record;

 @Test
 public void testCSVRecordWithValidValues() {
 String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("FirstName", 0);
 mapping.put("LastName", 1);
 mapping.put("Email", 2);
 CSVRecord expectedRecord = new CSVRecord(values, mapping, null, 1);
 record = new CSVRecord(values, mapping, null, 1);
 assertEquals(expectedRecord.get(Enum.valueOf("FirstName")), record.get("FirstName"));
 assertEquals(expectedRecord.get(Enum.valueOf("LastName")), record.get("LastName"));
 assertEquals(expectedRecord.get(Enum.valueOf("Email")), record.get("Email"));
 assertEquals(expectedRecord.getComment(), record.getComment());
 assertEquals(expectedRecord.getRecordNumber(), record.getRecordNumber());
 assertTrue(expectedRecord.isConsistent());
 assertTrue(expectedRecord.isMapped("FirstName"));
 assertTrue(expectedRecord.isMapped("LastName"));
 assertTrue(expectedRecord.isMapped("Email"));
 assertTrue(expectedRecord.isSet("FirstName"));
 assertTrue(expectedRecord.isSet("LastName"));
 assertTrue(expectedRecord.isSet("Email"));
 assertEquals(expectedRecord.toList(), record.toList());
 assertEquals(expectedRecord.toMap(), record.toMap());
 assertEquals(expectedRecord.toString(), record.toString());
 assertEquals(expectedRecord.values().length, record.values().length);
 }

 @Test
 public void testCSVRecordWithEmptyValues() {
 record = new CSVRecord(new String[0], null, null, 1);
 assertNull(record.get("FirstName"));
 assertNull(record.get("LastName"));
 assertNull(record.get("Email"));
 assertNull(record.getComment());
 assertEquals(0, record.getRecordNumber());
 assertTrue(record.isConsistent());
 assertFalse(record.isMapped("FirstName"));
 assertFalse(record.isMapped("LastName"));
 assertFalse(record.isMapped("Email"));
 assertFalse(record.isSet("FirstName"));
 assertFalse(record.isSet("LastName"));
 assertFalse(record.isSet("Email"));
 assertEquals(0, record.size());
 assertEquals(Arrays.toString(new String[0]), record.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecordWithNullMapping() {
 record = new CSVRecord(new String[]{"John", "Doe", "john.doe@example.com"}, null, null, 1);
 }

 @Test
 public void testCSVRecordWithInvalidMappingSize() {
 record = new CSVRecord(new String[]{"John", "Doe", "john.doe@example.com"}, new HashMap<String, Integer>(){{
 put("FirstName", 0);
 put("LastName", 1);
 put("Email", 3);
 }}, null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordWithInvalidMappingKey() {
 record = new CSVRecord(new String[]{"John", "Doe", "john.doe@example.com"}, new HashMap<String, Integer>(){{
 put("FirstName", 0);
 put("InvalidKey", 1);
 put("Email", 2);
 }}, null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordWithInvalidMappingIndex() {
 record = new CSVRecord(new String[]{"John", "Doe", "john.doe@example.com"}, new HashMap<String, Integer>(){{
 put("FirstName", 0);
 put("LastName", 1);
 put("Email", 4);
 }}, null, 1);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testCSVRecordWithInvalidRecordSize() {
 record = new CSVRecord(new String[]{"John", "Doe"}, null, null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordWithInvalidColumnName() {
 record = new CSVRecord(new String[]{"John", "Doe"}, null, null, 1);
 record.get("InvalidColumnName");
 }

 @Test
 public void testCSVRecordWithValidMappingAndComment() {
 String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("FirstName", 0);
 mapping.put("LastName", 1);
 mapping.put("Email", 2);
 String comment = "This is a comment";
 CSVRecord expectedRecord = new CSVRecord(values, mapping, comment, 1);
 record = new CSVRecord(values, mapping, comment, 1);
 assertEquals(expectedRecord.getComment(), record.getComment());
 }

 @Test
 public void testCSVRecordWithValidMappingAndRecordNumber() {
 String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("FirstName", 0);
 mapping.put("LastName", 1);
 mapping.put("Email", 2);
 long recordNumber = 1;
 CSVRecord expectedRecord = new CSVRecord(values, mapping, null, recordNumber);
 record = new CSVRecord(values, mapping, null, recordNumber);
 assertEquals(expectedRecord.getRecordNumber(), record.getRecordNumber());
 }

}