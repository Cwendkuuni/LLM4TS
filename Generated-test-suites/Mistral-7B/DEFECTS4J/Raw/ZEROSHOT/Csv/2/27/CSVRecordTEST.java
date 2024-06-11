import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordGet() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 assertEquals("John", record.get(0));
 assertEquals("Doe", record.get(1));
 assertEquals("30", record.get(2));

 record = new CSVRecord(null, null, null, 1);
 assertNull(record.get(0));
 }

 @Test
 public void testCSVRecordGetByName() {
 CSVRecord record = new CSVRecord(new String[]{"Name=John,Age=30"}, Map.of("Name", 0, "Age", 1), null, 1);
 assertEquals("John", record.get("Name"));
 assertEquals("30", record.get("Age"));
 assertNull(record.get("Unknown"));

 record = new CSVRecord(new String[]{"Name,Age"}, null, null, 1);
 assertNull(record.get("Name"));
 assertNull(record.get("Age"));
 }

 @Test
 public void testCSVRecordIsConsistent() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Age,City"}, null, null, 1);
 assertTrue(record.isConsistent());

 record = new CSVRecord(new String[]{"Name,Age"}, null, null, 1);
 assertTrue(record.isConsistent());

 record = new CSVRecord(new String[]{"Name,Age,City", "Name,Age"}, null, null, 1);
 assertTrue(record.isConsistent());

 record = new CSVRecord(new String[]{"Name,Age"}, null, null, 1);
 assertFalse(record.isConsistent());

 record = new CSVRecord(new String[]{"Name,Age,City", "Name,Age,Address"}, null, null, 1);
 assertFalse(record.isConsistent());
 }

 @Test
 public void testCSVRecordIsMapped() {
 CSVRecord record = new CSVRecord(new String[]{"Name=John,Age=30"}, Map.of("Name", 0, "Age", 1), null, 1);
 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));

 record = new CSVRecord(new String[]{"Name,Age"}, null, null, 1);
 assertFalse(record.isMapped("Name"));
 assertFalse(record.isMapped("Age"));
 }

 @Test
 public void testCSVRecordIsSet() {
 CSVRecord record = new CSVRecord(new String[]{"Name=John,Age=30"}, Map.of("Name", 0, "Age", 1), null, 1);
 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));

 record = new CSVRecord(new String[]{"Name,Age"}, null, null, 1);
 assertFalse(record.isSet("Name"));
 assertFalse(record.isSet("Age"));
 }

 @Test
 public void testCSVRecordSize() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Age"}, null, null, 1);
 assertEquals(2, record.size());
 }

 @Test
 public void testCSVRecordToString() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Age"}, null, null, 1);
 assertEquals("[Name, Age]", record.toString());
 }

 @Test
 public void testCSVRecordGetComment() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Age"}, null, "Comment", 1);
 assertEquals("Comment", record.getComment());

 record = new CSVRecord(new String[]{"Name,Age"}, null, null, 1);
 assertNull(record.getComment());
 }

 @Test
 public void testCSVRecordGetRecordNumber() {
 CSVRecord record = new CSVRecord(new String[]{"Name,Age"}, null, null, 1);
 assertEquals(1, record.getRecordNumber());
 }
}