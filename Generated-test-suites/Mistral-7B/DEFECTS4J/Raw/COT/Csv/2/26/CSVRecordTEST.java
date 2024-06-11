import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private CSVRecord record;

 @Test
 public void testCSVRecordConstructor() {
 String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
 Map<String, Integer> mapping = new java.util.HashMap<>();
 mapping.put("Firstname", 0);
 mapping.put("Lastname", 1);
 mapping.put("Email", 2);
 record = new CSVRecord(values, mapping, null, 1);
 assertEquals(3, record.size());
 }

 @Test
 public void testGetMethod() {
 record = new CSVRecord(new String[]{"John", "Doe", "john.doe@example.com"}, null, null, 1);
 assertEquals("John", record.get(0));
 assertEquals("Doe", record.get(1));
 assertEquals("john.doe@example.com", record.get(2));
 }

 @Test
 public void testGetMethodWithName() {
 record = new CSVRecord(new String[]{"Firstname:John,Lastname:Doe,Email:john.doe@example.com"}, new java.util.HashMap<String, Integer>(){{
 put("Firstname", 0);
 put("Lastname", 1);
 put("Email", 2);
 }}, null, 1);
 assertEquals("John", record.get("Firstname"));
 assertEquals("Doe", record.get("Lastname"));
 assertEquals("john.doe@example.com", record.get("Email"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMethodWithNoMapping() {
 record = new CSVRecord(new String[]{"John", "Doe", "john.doe@example.com"}, null, null, 1);
 try {
 record.get("Email");
 } catch (IllegalStateException e) {
 assertEquals("No header mapping was specified, the record values can't be accessed by name", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMethodWithInvalidIndex() {
 record = new CSVRecord(new String[]{"John", "Doe", "john.doe@example.com"}, null, null, 1);
 try {
 record.get(-1);
 } catch (IllegalArgumentException e) {
 assertEquals("Index out of range: -1", e.getMessage());
 }
 }

 @Test
 public void testIsConsistentMethod() {
 record = new CSVRecord(new String[]{"John", "Doe", "john.doe@example.com"}, null, null, 1);
 assertTrue(record.isConsistent());
 record = new CSVRecord(new String[]{"John", "Doe"}, null, null, 1);
 assertFalse(record.isConsistent());
 }

 @Test
 public void testIsMappedMethod() {
 record = new CSVRecord(new String[]{"Firstname:John,Lastname:Doe,Email:john.doe@example.com"}, new java.util.HashMap<String, Integer>(){{
 put("Firstname", 0);
 put("Lastname", 1);
 put("Email", 2);
 }}, null, 1);
 assertTrue(record.isMapped("Firstname"));
 assertTrue(record.isMapped("Lastname"));
 assertTrue(record.isMapped("Email"));
 record = new CSVRecord(new String[]{"John,Doe,john.doe@example.com"}, null, null, 1);
 assertFalse(record.isMapped("Firstname"));
 assertFalse(record.isMapped("Lastname"));
 assertFalse(record.isMapped("Email"));
 }

 @Test
 public void testIsSetMethod() {
 record = new CSVRecord(new String[]{"Firstname:John,Lastname:Doe,Email:john.doe@example.com"}, new java.util.HashMap<String, Integer>(){{
 put("Firstname", 0);
 put("Lastname", 1);
 put("Email", 2);
 }}, null, 1);
 assertTrue(record.isSet("Firstname"));
 assertTrue(record.isSet("Lastname"));
 assertTrue(record.isSet("Email"));
 record = new CSVRecord(new String[]{"Firstname:John,Lastname:", "Email:john.doe@example.com"}, new java.util.HashMap<String, Integer>(){{
 put("Firstname", 0);
 put("Lastname", 1);
 put("Email", 2);
 }}, null, 1);
 assertFalse(record.isSet("Lastname"));
 }

 @Test
 public void testIteratorMethod() {
 record = new CSVRecord(new String[]{"Firstname:John,Lastname:Doe,Email:john.doe@example.com"}, new java.util.HashMap<String, Integer>(){{
 put("Firstname", 0);
 put("Lastname", 1);
 put("Email", 2);
 }}, null, 1);
 Iterator<String> it = record.iterator();
 assertEquals("Firstname:John", it.next());
 assertEquals("Lastname:Doe", it.next());
 assertEquals("Email:john.doe@example.com", it.next());
 assertFalse(it.hasNext());
 }

 @Test
 public void testValuesMethod() {
 record = new CSVRecord(new String[]{"Firstname:John,Lastname:Doe,Email:john.doe@example.com"}, new java.util.HashMap<String, Integer>(){{
 put("Firstname", 0);
 put("Lastname", 1);
 put("Email", 2);
 }}, null, 1);
 assertArrayEquals(new String[]{"Firstname:John", "Lastname:Doe", "Email:john.doe@example.com"}, record.values());
 }

 @Test
 public void testGetCommentMethod() {
 record = new CSVRecord(new String[]{"Firstname:John,Lastname:Doe,Email:john.doe@example.com", "#This is a comment"}, null, null, 1);
 assertEquals("#This is a comment", record.getComment());
 record = new CSVRecord(new String[]{"Firstname:John,Lastname:Doe,Email:john.doe@example.com"}, null, null, 1);
 assertNull(record.getComment());
 }

 @Test
 public void testGetRecordNumberMethod() {
 record = new CSVRecord(new String[]{"Firstname:John,Lastname:Doe,Email:john.doe@example.com"}, null, null, 1);
 assertEquals(1, record.getRecordNumber());
 record = new CSVRecord(new String[]{"Firstname:John,Lastname:Doe,Email:john.doe@example.com"}, null, null, 2);
 assertEquals(2, record.getRecordNumber());
 }
}