import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordWithValidData() throws Exception {
 String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
 CSVRecord record = parser.nextRecord();

 // Test getting values by name
 assertEquals("John Doe", record.get("Name"));
 assertEquals("30", record.get("Age"));
 assertEquals("New York", record.get("City"));

 // Test getting values by index
 assertEquals("John Doe", record.get(0));
 assertEquals("30", record.get(1));
 assertEquals("New York", record.get(2));

 // Test getting comment and record number
 assertNull(record.getComment());
 assertEquals(1, record.getRecordNumber());

 // Test isConsistent
 assertTrue(record.isConsistent());
 }

 @Test
 public void testCSVRecordWithMissingColumn() throws Exception {
 String csvData = "Name,Age\nJohn Doe,30";
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
 CSVRecord record = parser.nextRecord();

 // Test getting value by name for missing column
 assertNull(record.get("City"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordWithInvalidColumnName() throws Exception {
 String csvData = "Name,Age,City\nJohn Doe,30,New York";
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
 CSVRecord record = parser.nextRecord();

 // Test getting value by name for invalid column name
 record.get("InvalidColumnName");
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testCSVRecordWithInvalidIndex() throws Exception {
 String csvData = "Name,Age,City\nJohn Doe,30,New York";
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
 CSVRecord record = parser.nextRecord();

 // Test getting value by index for invalid index
 record.get(3);
 }

 @Test
 public void testCSVRecordWithEmptyData() throws Exception {
 String csvData = "";
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
 CSVRecord record = parser.nextRecord();

 // Test getting values by name
 assertNull(record.get("Name"));
 assertNull(record.get("Age"));
 assertNull(record.get("City"));

 // Test getting values by index
 assertNull(record.get(0));
 assertNull(record.get(1));
 assertNull(record.get(2));

 // Test getting comment and record number
 assertNull(record.getComment());
 assertEquals(0, record.getRecordNumber());

 // Test isConsistent
 assertTrue(record.isConsistent());
 }

 @Test
 public void testCSVRecordWithNullString() throws Exception {
 String csvData = "Name,Age,City\nJohn Doe,30,New York\n,25,Chicago\n";
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withNullString(""));
 CSVRecord record = parser.nextRecord();

 // Test getting values by name for null string
 assertEquals("John Doe", record.get("Name"));
 assertNull(record.get("Age"));
 assertEquals("New York", record.get("City"));

 // Test getting values by index for null string
 assertEquals("John Doe", record.get(0));
 assertNull(record.get(1));
 assertEquals("New York", record.get(2));

 // Test getting comment and record number
 assertNull(record.getComment());
 assertEquals(1, record.getRecordNumber());

 // Test isConsistent
 assertTrue(record.isConsistent());
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecordWithoutMapping() throws Exception {
 String csvData = "Name,Age,City\nJohn Doe,30,New York\n";
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
 CSVRecord record = parser.nextRecord();
 record.get("InvalidColumnName");
 }
}