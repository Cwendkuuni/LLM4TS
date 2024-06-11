import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

 @Test
 public void testCSVRecord() {
 // Test constructor with valid values
 String[] values = new String[]{"Alice", "25", "accountant", "New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Occupation", 2);
 mapping.put("City", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);

 // Test get method with valid index
 Assertions.assertEquals("Alice", record.get(0));

 // Test get method with valid name
 Assertions.assertEquals("Alice", record.get("Name"));

 // Test get method with valid Enum
 Assertions.assertEquals("Alice", record.get(CSVRecord.Name.class));

 // Test getComment method
 Assertions.assertNull(record.getComment());

 // Test isConsistent method
 Assertions.assertTrue(record.isConsistent());

 // Test isMapped method with valid name
 Assertions.assertTrue(record.isMapped("Name"));

 // Test isSet method with valid name
 Assertions.assertTrue(record.isSet("Name"));

 // Test iterator method
 List<String> valuesList = record.toList();
 Iterator<String> iterator = record.iterator();
 for (int i = 0; i < valuesList.size(); i++) {
 Assertions.assertEquals(valuesList.get(i), iterator.next());
 }

 // Test putIn method
 Map<String, String> map = new HashMap<>();
 record.putIn(map);
 for (Entry<String, Integer> entry : mapping.entrySet()) {
 Assertions.assertEquals(values[entry.getValue().intValue()], map.get(entry.getKey()));
 }

 // Test size method
 Assertions.assertEquals(values.length, record.size());

 // Test toString method
 Assertions.assertEquals("[Alice, 25, accountant, New York]", record.toString());

 // Test values method
 String[] valuesFromMethod = record.values();
 Assertions.assertArrayEquals(values, valuesFromMethod);

 // Test constructor with empty values and mapping
 CSVRecord emptyRecord = new CSVRecord(new String[0], null, null, 1);
 // Test get method with valid index
 Assertions.assertNull(emptyRecord.get(0));

 // Test get method with valid name
 Assertions.assertNull(emptyRecord.get("Name"));

 // Test get method with valid Enum
 Assertions.assertNull(emptyRecord.get(CSVRecord.Name.class));

 // Test isConsistent method
 Assertions.assertTrue(emptyRecord.isConsistent());

 // Test isMapped method with valid name
 Assertions.assertFalse(emptyRecord.isMapped("Name"));

 // Test isSet method with valid name
 Assertions.assertFalse(emptyRecord.isSet("Name"));
 }

 @Test
 public void testCSVRecordWithInvalidValues() {
 String[] values = new String[]{"Alice", "25", "accountant", "New York", "invalid"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Occupation", 2);
 mapping.put("City", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);

 // Test get method with valid index
 Assertions.assertEquals("Alice", record.get(0));

 // Test get method with valid name
 Assertions.assertEquals("Alice", record.get("Name"));

 // Test get method with valid Enum
 Assertions.assertEquals("Alice", record.get(CSVRecord.Name.class));

 // Test get method with invalid index
 Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(4));

 // Test get method with invalid name
 Assertions.assertThrows(IllegalArgumentException.class, () -> record.get("Invalid"));

 // Test get method with invalid Enum
 Assertions.assertThrows(IllegalArgumentException.class, () -> record.get(CSVRecord.Invalid.class));

 // Test isConsistent method
 Assertions.assertFalse(record.isConsistent());

 // Test isMapped method with valid name
 Assertions.assertTrue(record.isMapped("Name"));

 // Test isMapped method with invalid name
 Assertions.assertFalse(record.isMapped("Invalid"));

 // Test isSet method with valid name
 Assertions.assertTrue(record.isSet("Name"));

 // Test isSet method with invalid name
 Assertions.assertFalse(record.isSet("Invalid"));
 }

 @Test
 public void testCSVRecordWithComment() {
 String[] values = new String[]{"Alice", "25", "accountant", "New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Occupation", 2);
 mapping.put("City", 3);
 String comment = "This is a test record.";
 CSVRecord record = new CSVRecord(values, mapping, comment, 1);

 // Test getComment method
 Assertions.assertEquals(comment, record.getComment());
 }

 @Test
 public void testCSVRecordWithNullMapping() {
 String[] values = new String[]{"Alice", "25", "accountant", "New York"};
 CSVRecord record = new CSVRecord(values, null, null, 1);

 // Test get method with valid index
 Assertions.assertEquals("Alice", record.get(0));

 // Test get method with valid name
 Assertions.assertThrows(IllegalStateException.class, () -> record.get("Name"));

 // Test get method with valid Enum
 Assertions.assertThrows(IllegalStateException.class, () -> record.get(CSVRecord.Name.class));
 }

 @Test
 public void testCSVParser() throws IOException {
 String csvText = "Name,Age,Occupation,City\nAlice,25,accountant,New York\nBob,30,engineer,Chicago\n";
 CSVFormat format = CSVFormat.DEFAULT.withHeader();
 CSVParser parser = new CSVParser(new StringReader(csvText), format);
 List<CSVRecord> records = parser.getRecords();

 // Test size method
 Assertions.assertEquals(2, records.size());

 // Test get method with valid index and name
 CSVRecord firstRecord = records.get(0);
 Assertions.assertEquals("Alice", firstRecord.get("Name"));
 Assertions.assertEquals("Alice", firstRecord.get(CSVRecord.Name.class));
 Assertions.assertEquals("New York", firstRecord.get("City"));
 Assertions.assertEquals("New York", firstRecord.get(CSVRecord.City.class));

 // Test get method with invalid index and name
 Assertions.assertThrows(IndexOutOfBoundsException.class, () -> firstRecord.get(3));
 Assertions.assertThrows(IllegalArgumentException.class, () -> firstRecord.get("Invalid"));

 // Test get method with valid index and Enum for second record
 CSVRecord secondRecord = records.get(1);
 Assertions.assertEquals("Bob", secondRecord.get("Name"));
 Assertions.assertEquals("Bob", secondRecord.get(CSVRecord.Name.class));
 Assertions.assertEquals("Chicago", secondRecord.get("City"));
 Assertions.assertEquals("Chicago", secondRecord.get(CSVRecord.City.class));
 }
}