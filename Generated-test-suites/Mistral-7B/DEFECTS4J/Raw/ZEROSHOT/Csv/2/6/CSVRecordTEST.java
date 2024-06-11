import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordConstruction() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1L);
 assertEquals(3, record.size());
 assertNull(record.getComment());
 assertNull(record.getRecordNumber());

 String[] values = new String[]{"John Doe", "30", "New York"};
 record = new CSVRecord(values, Map.of("Name", 0, "Age", 1, "City", 2), null, 1L);
 assertEquals("Name=John Doe, Age=30, City=New York", record.toString());
 assertEquals("John Doe", record.get("Name"));
 assertEquals("30", record.get("Age"));
 assertEquals("New York", record.get("City"));
 assertEquals("Name", record.mapping.get(0).getName());
 assertSame(0, record.mapping.get("Name").intValue());
 }

 @Test
 public void testGetConsistent() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1L);
 assertTrue(record.isConsistent());
 record = new CSVRecord(new String[]{"Name", "Age", "City", "Comment"}, null, "Test Comment", 1L);
 assertTrue(record.isConsistent());
 record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1L);
 record.mapping = Map.of("Name", 0, "Age", 1, "City", 2);
 record.values = new String[3];
 record.values[2] = null;
 assertFalse(record.isConsistent());

 CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
 parser.parseAdd(new String[]{"Name", "Age", "City"}, null);
 parser.parseAdd(new String[]{"Name", "Age", "City", "Comment"});
 record = parser.getRecord();
 parser.parseAdd(new String[]{"Name", "Age", "City", "Comment"});
 record = parser.getRecord();
 parser.parseAdd(new String[]{"Name", "Age"});
 assertFalse(record.isConsistent());
 }

 @Test
 public void testGetMappedButNotSet() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1L);
 assertFalse(record.isMapped("Age"));
 record.mapping = Map.of("Name", 0, "Age", 1);
 assertTrue(record.isMapped("Age"));
 record.mapping = Map.of("Name", 0, "Age", -1);
 assertFalse(record.isMapped("Age"));
 }

 @Test
 public void testGetSet() {
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1L);
 assertFalse(record.isSet("Age"));
 record.mapping = Map.of("Name", 0, "Age", 1);
 record.values = new String[3];
 record.values[1] = "30";
 assertTrue(record.isSet("Age"));
 }
}