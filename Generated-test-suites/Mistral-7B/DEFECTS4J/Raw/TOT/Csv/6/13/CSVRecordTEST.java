import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 @Test
 public void testCSVRecordWithValidData() {
 CSVFormat format = CSVFormat.DEFAULT;
 String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";
 CSVParser parser = new CSVParser(new StringReader(csvData), format);
 CSVRecord record = parser.nextRecord();

 // Test get methods with valid data
 assertEquals("John Doe", record.get(0));
 assertEquals("New York", record.get(2));
 assertEquals(30, record.getAsInt(1));

 // Test other methods with valid data
 assertTrue(record.isConsistent());
 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));
 assertTrue(record.isMapped("City"));
 assertFalse(record.isSet("ZipCode"));
 assertEquals(3, record.size());

 Map<String, String> map = record.toMap();
 assertEquals("John Doe", map.get("Name"));
 assertEquals("30", map.get("Age"));
 assertEquals("New York", map.get("City"));
 }

 // Add more test cases here for edge cases and error scenarios

}