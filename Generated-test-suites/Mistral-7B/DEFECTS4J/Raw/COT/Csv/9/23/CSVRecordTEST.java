import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 private static final CSVFormat FORMAT = new CSVFormat(' ', true);

 @Test
 public void testCSVRecord_Empty() {
 CSVRecord record = new CSVRecord(new String[0], null, null, 0);
 assertEquals("[]", record.toString());
 assertFalse(record.isConsistent());
 assertNull(record.getComment());
 assertNull(record.get(0));
 }

 @Test
 public void testCSVRecord_Basic() {
 String[] values = {"Alice", "Smith", "age:25"};
 CSVRecord record = new CSVRecord(values, null, null, 0);
 assertEquals("[Alice, Smith, age:25]", record.toString());
 assertTrue(record.isConsistent());
 assertEquals("Alice", record.get(0));
 assertEquals("Smith", record.get(1));
 assertEquals("age:25", record.get(2));
 }

 @Test
 public void testCSVRecord_WithComment() {
 String[] values = {"Alice", "Smith", "age:25"};
 CSVRecord record = new CSVRecord(values, null, "Comment", 0);
 assertEquals("[Alice, Smith, age:25]", record.toString());
 assertEquals("Comment", record.getComment());
 }

 @Test
 public void testCSVRecord_WithMapping() {
 String[] values = {"Alice", "Smith", "age:25"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 0);
 assertEquals("[Alice, Smith, age:25]", record.toString());
 assertEquals("Alice", record.get("name"));
 assertEquals("age:25", record.get("age"));
 }

 @Test
 public void testCSVRecord_WithInvalidMapping() {
 String[] values = {"Alice", "Smith", "age:25"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 0);
 assertThrows(IllegalArgumentException.class, () -> record.get("age"));
 }

 @Test
 public void testCSVRecord_WithNullMapping() {
 String[] values = {"Alice", "Smith", "age:25"};
 CSVRecord record = new CSVRecord(values, null, null, 0);
 assertNull(record.get("name"));
 assertNull(record.get("age"));
 }

 @Test
 public void testCSVRecord_WithMissingValue() {
 String[] values = {"Alice", null, "age:25"};
 CSVRecord record = new CSVRecord(values, null, null, 0);
 assertNull(record.get(1));
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecord_WithoutMapping() {
 String[] values = {"Alice", "Smith", "age:25"};
 CSVRecord record = new CSVRecord(values, null, null, 0);
 record.get("name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_InvalidIndex() {
 String[] values = {"Alice", "Smith", "age:25"};
 CSVRecord record = new CSVRecord(values, null, null, 0);
 record.get(values.length + 1);
 }

 @Test
 public void testCSVRecord_Iterable() {
 String[] values = {"Alice", "Smith", "age:25"};
 CSVRecord record = new CSVRecord(values, null, null, 0);
 List<String> valuesList = record.toList();
 for (int i = 0; i < values.length; i++) {
assertEquals(values[i], valuesList.get(i));
}
}

 @Test
 public void testCSVRecord_Map() {
 String[] values = {"Alice", "Smith", "age:25"};
 CSVRecord record = new CSVRecord(values, null, null, 0);
 Map<String, String> map = record.toMap();
 for (Entry<String, Integer> entry : record.mapping.entrySet()) {
 String key = entry.getKey();
 int value = entry.getValue().intValue();
 assertEquals(values[value], map.get(key));
 }
}

 @Test
 public void testCSVRecord_Serializable() {
 String[] values = {"Alice", "Smith", "age:25"};
 CSVRecord record1 = new CSVRecord(values, null, null, 0);
 CSVRecord record2 = (CSVRecord) record1.clone();
 assertEquals(record1.toString(), record2.toString());
 }

 @Test
 public void testCSVRecord_WithEmptyMapping() {
 String[] values = {"Alice", "Smith", "age:25"};
 CSVRecord record = new CSVRecord(values, Collections.emptyMap(), null, 0);
 assertNull(record.get("name"));
 assertNull(record.get("age"));
 }

 @Test
 public void testCSVRecord_ParseFromCSV() throws Exception {
 CSVParser parser = FORMAT.parse(getClass().getResourceAsStream("/testCSV.csv"));
 Iterator<CSVRecord> iterator = parser.iterator();
 CSVRecord record = iterator.next();
 assertEquals(record.get(0), "Alice");
 assertEquals(record.get(1), "Smith");
 assertEquals(record.get(2), "age:25");
 }

}