import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import java.io.StringReader;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordWithValidData() {
 String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";
 CSVFormat format = new CSVFormat();
 try (CSVParser parser = new CSVParser(new StringReader(csvData), format)) {
 CSVRecord record1 = parser.nextRecord();
 // Test get methods
 assertEquals("John Doe", record1.get(0));
 assertEquals("Name", record1.getMapping().get(0));
 assertEquals("New York", record1.get(1));
 assertEquals("City", record1.getMapping().get(1));
 assertEquals(30, Integer.parseInt(record1.get(2)));
 // Test other methods
 assertTrue(record1.isConsistent());
 assertTrue(record1.isMapped("Name"));
 assertTrue(record1.isMapped("City"));
 assertTrue(record1.isSet("Name"));
 assertTrue(record1.isSet("City"));
 // Test get methods using Enum and String
 assertEquals("John Doe", record1.get(CSVRecord.Name.NAME));
 assertEquals("New York", record1.get(CSVRecord.City.CITY));
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testCSVRecordWithNullData() {
 String csvData = "Name,Age,City\nJohn Doe,30,New York\n,25,Los Angeles\nName,Age,City";
 CSVFormat format = new CSVFormat();
 try (CSVParser parser = new CSVParser(new StringReader(csvData), format)) {
 CSVRecord record1 = parser.nextRecord();
 CSVRecord record2 = parser.nextRecord();
 CSVRecord record3 = parser.nextRecord();
 // Test get methods with null data
 assertEquals(null, record2.get(0));
 assertEquals(null, record3.get(0));
 // Test other methods
 assertFalse(record2.isConsistent());
 assertFalse(record3.isConsistent());
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testCSVRecordWithEmptyData() {
 String csvData = "Name,Age,City\n,,\nName,Age,City";
 CSVFormat format = new CSVFormat();
 try (CSVParser parser = new CSVParser(new StringReader(csvData), format)) {
 CSVRecord record1 = parser.nextRecord();
 CSVRecord record2 = parser.nextRecord();
 // Test get methods with empty data
 assertEquals(null, record1.get(0));
 assertEquals(null, record1.get(1));
 assertEquals(null, record1.get(2));
 assertNull(record2.get(0));
 assertNull(record2.get(1));
 assertNull(record2.get(2));
 // Test other methods
 assertFalse(record1.isConsistent());
 assertFalse(record2.isConsistent());
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordWithInvalidMapping() {
 String csvData = "Name,Age,City\nJohn Doe,30,New York\n";
 CSVFormat format = new CSVFormat();
 format.setHeaderMappingStrategy(CSVFormat.DEFAULT.getHeaderMappingStrategy() + new CSVFormat.HeaderMappingStrategy() {
 @Override
 public Map<String, Integer> createHeaderMapping(String[] headerNames) {
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("City", 2);
 mapping.put("Age", 1);
 return mapping;
 }
 });
 try (CSVParser parser = new CSVParser(new StringReader(csvData), format)) {
 CSVRecord record = parser.nextRecord();
 // Test get methods
 assertEquals("John Doe", record.get(0));
 assertEquals("Name", record.getMapping().get(0));
 assertEquals(null, record.get(1)); // Age should be at index 1, not 2
 assertEquals("New York", record.get(2));
 assertEquals("City", record.getMapping().get(2));
 // Test other methods
 assertTrue(record.isConsistent());
 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("City"));
 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("City"));
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }
}