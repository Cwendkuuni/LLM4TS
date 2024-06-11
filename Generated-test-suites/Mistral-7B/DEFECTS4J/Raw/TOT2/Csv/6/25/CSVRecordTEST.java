import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

 @Test
 public void testCSVRecord_DefaultConstructor() {
 CSVRecord record = new CSVRecord(null, null, null, 0);
 Assert.assertNull(record.getComment());
 Assert.assertNull(record.getMapping());
 Assert.assertArrayEquals(CSVRecord.EMPTY_STRING_ARRAY, record.getValues());
 Assert.assertEquals(0, record.getRecordNumber());
 }

 @Test
 public void testCSVRecord_Constructor_Values() {
 String[] values = {"Alice", "25", "female"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 Assert.assertArrayEquals(values, record.getValues());
 Assert.assertEquals(mapping, record.getMapping());
 Assert.assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testCSVRecord_Constructor_Values_EmptyMapping() {
 String[] values = {"Alice", "25", "female"};
 CSVRecord record = new CSVRecord(values, null, null, 1);
 Assert.assertNull(record.getMapping());
 Assert.assertArrayEquals(values, record.getValues());
 Assert.assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testCSVRecord_get_Enum() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 Assert.assertEquals("Alice", record.get(Enum.ONE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_get_Enum_NotExist() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 record.get(Enum.TWO);
 }

 @Test
 public void testCSVRecord_get_Int() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 Assert.assertEquals("25", record.get(0));
 }

 @Test
 public void testCSVRecord_get_String() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 Assert.assertEquals("Alice", record.get("Name"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_get_String_NotExist() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 record.get("Gender1");
 }

 @Test
 public void testCSVRecord_getComment() {
 CSVRecord record = testCSVRecord_Constructor_Values_WithComment();
 Assert.assertEquals("This record has a comment", record.getComment());
 }

 @Test
 public void testCSVRecord_getRecordNumber() {
 CSVRecord record = testCSVRecord_Constructor_Values_WithRecordNumber();
 Assert.assertEquals(2, record.getRecordNumber());
 }

 @Test
 public void testCSVRecord_isConsistent() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 Assert.assertTrue(record.isConsistent());
 }

 @Test
 public void testCSVRecord_isConsistent_Inconsistent() {
 String[] values = {"Alice", "25", "female", "Bob"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);
 Assert.assertFalse(record.isConsistent());
 }

 @Test
 public void testCSVRecord_isMapped() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 Assert.assertTrue(record.isMapped("Name"));
 }

 @Test
 public void testCSVRecord_isMapped_False() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 Assert.assertFalse(record.isMapped("Gender1"));
 }

 @Test
 public void testCSVRecord_isSet() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 Assert.assertTrue(record.isSet("Name"));
 }

 @Test
 public void testCSVRecord_isSet_False() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 Assert.assertFalse(record.isSet("Gender1"));
 }

 @Test
 public void testCSVRecord_iterator() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 List<String> list = record.iterator().collect(Collectors.toList());
 Assert.assertEquals(Arrays.asList("Alice", "25", "female"), list);
 }

 @Test
 public void testCSVRecord_putIn() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 Map<String, String> map = new HashMap<>();
 map = record.putIn(map);
 Assert.assertEquals(Collections.singletonMap("Name", "Alice"), map);
 }

 @Test
 public void testCSVRecord_size() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 Assert.assertEquals(3, record.size());
 }

 @Test
 public void testCSVRecord_toList() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 List<String> list = record.toList();
 Assert.assertEquals(Arrays.asList("Alice", "25", "female"), list);
 }

 @Test
 public void testCSVRecord_toMap() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 Map<String, String> map = record.toMap();
 Assert.assertEquals(Collections.singletonMap("Name", "Alice"), map);
 }

 @Test
 public void testCSVRecord_toString() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 Assert.assertEquals("[Alice, 25, female]", record.toString());
 }

 @Test
 public void testCSVRecord_values() {
 CSVRecord record = testCSVRecord_Constructor_Values();
 Assert.assertArrayEquals(new String[]{"Alice", "25", "female"}, record.values());
 }

 @Test
 public void testParseCSVRecord() throws Exception {
 String csv = "Name,Age,Gender\nAlice,25,female\nBob,30,male\n";
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(csv));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 Assert.assertEquals("Alice", record.get("Name"));
 Assert.assertEquals(25, Integer.parseInt(record.get(1)));
 Assert.assertEquals("female", record.get("Gender"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseCSVRecord_EmptyCSV() throws Exception {
 String csv = "";
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(csv));
 List<CSVRecord> records = parser.getRecords();
 }

 @Test(expected = IllegalStateException.class)
 public void testParseCSVRecord_NoHeaderMapping() throws Exception {
 String csv = "Name,Age\nAlice,25\nBob,30\n";
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(csv));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 record.get("Gender");
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testParseCSVRecord_MismatchValuesAndMapping() throws Exception {
 String csv = "Name,Age,Gender\nAlice,25,female\nBob,30,male\n";
 CSVFormat format = new CSVFormat();
 format.setHeaderMappingStrategy(CSVFormat.DEFAULT.headerMappingStrategy().withFirstHeader());
 CSVParser parser = format.newParser(new StringReader(csv));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 record.get(2);
 }
}