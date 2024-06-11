import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private CSVParser parser;
 private String csvData = "Name,Age,City\nJohn,30,New York\nMike,25,Los Angeles\n";

 @Before
 public void setUp() {
 parser = CSVParser.parse(csvData, CSVFormat.DEFAULT.withHeader());
 }

 @Test
 public void testGetByIndex() {
 CSVRecord record = parser.getRecord();
 // Test getting values by index
 assertEquals("John", record.get(0));
 assertEquals("30", record.get(1));
 assertEquals("New York", record.get(2));
 }

 @Test
 public void testGetByName() {
 CSVRecord record = parser.getRecord();
 // Test getting values by name
 assertEquals("John", record.get("Name"));
 assertEquals("30", record.get("Age"));
 assertEquals("New York", record.get("City"));
 // Test getting null when name is not found
 assertNull(record.get("Unknown"));
 }

 @Test
 public void testIsConsistent() {
 CSVRecord record = parser.getRecord();
 // Test record consistency
 assertTrue(record.isConsistent());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetByNameWhenNoHeaderMapping() {
 CSVRecord record = new CSVRecord(new String[3], null, null, 0);
 // Test getting values by name when no header mapping is provided
 assertNull(record.get("Name"));
 // Test exception when trying to get value by name without header mapping
 record.get("Age");
 }

 @Test
 public void testIsMapped() {
 CSVRecord record = parser.getRecord();
 // Test checking if column is mapped
 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));
 assertTrue(record.isMapped("City"));
 // Test checking if unknown column is not mapped
 assertFalse(record.isMapped("Unknown"));
 }

 @Test
 public void testIsSet() {
 CSVRecord record = parser.getRecord();
 // Test checking if column is set
 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertTrue(record.isSet("City"));
 // Test checking if unknown column is not set
 assertFalse(record.isSet("Unknown"));
 }

 @Test
 public void testSize() {
 CSVRecord record = parser.getRecord();
 // Test record size
 assertEquals(3, record.size());
 }

 @Test
 public void testToString() {
 CSVRecord record = parser.getRecord();
 // Test record toString
 assertEquals("[Name=John, Age=30, City=New York]", record.toString());
 }

 @Test
 public void testGetComment() {
 CSVRecord record = new CSVRecord(new String[0], null, "Comment for record", 0);
 // Test getting comment
 assertEquals("Comment for record", record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 CSVRecord record = parser.getRecord();
 // Test getting record number
 assertEquals(0, record.getRecordNumber());
 }

}