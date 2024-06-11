import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private static final String CSV = "name1,name2,name3,comment1\n" +
         "value1_1,value1_2,value1_3,this is a comment\n" +
         "value2_1,value2_2,value2_3,\n" +
         "value3_1,value3_2,value3_3,this is another comment\n";

 @Test
 public void testGet() {
 CSVParser parser = new CSVParser(new StringReader(CSV), CSVFormat.DEFAULT);
 CSVRecord record = null;
 try {
 record = parser.getRecord();
 } catch (IOException e) {
 e.printStackTrace();
 }

 // Test getting values by index
 assertEquals("value1_1", record.get(0));
 assertEquals("value1_2", record.get(1));
 assertEquals("value1_3", record.get(2));

 // Test getting values by name
 assertEquals("value1_1", record.get(CSVRecord.Column.NAME1.getName()));
 assertEquals("value1_2", record.get(CSVRecord.Column.NAME2.getName()));
 assertEquals("value1_3", record.get(CSVRecord.Column.NAME3.getName()));

 // Test getting non-existent value by name
 assertNull(record.get("NON_EXISTANT_NAME"));

 // Test getting comment
 assertEquals("this is a comment", record.getComment());
 }

 @Test
 public void testGet_Empty() {
 CSVParser parser = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);
 CSVRecord record = null;
 try {
 record = parser.getRecord();
 } catch (IOException e) {
 e.printStackTrace();
 }

 // Test getting values by index
 assertNull(record.get(0));

 // Test getting values by name
 assertNull(record.get("name1"));
 assertNull(record.get("name2"));
 assertNull(record.get("name3"));

 // Test getting comment
 assertNull(record.getComment());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGet_InvalidIndex() {
 CSVParser parser = new CSVParser(new StringReader(CSV), CSVFormat.DEFAULT);
 CSVRecord record = null;
 try {
 record = parser.getRecord();
 } catch (IOException e) {
 e.printStackTrace();
 }

 record.get(record.size() + 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGet_InvalidName() {
 CSVParser parser = new CSVParser(new StringReader(CSV), CSVFormat.DEFAULT);
 CSVRecord record = null;
 try {
 record = parser.getRecord();
 } catch (IOException e) {
 e.printStackTrace();
 }

 record.get("NON_EXISTANT_NAME");
 }

 @Test
 public void testIsConsistent() {
 CSVParser parser = new CSVParser(new StringReader(CSV), CSVFormat.DEFAULT);
 CSVRecord record = null;
 try {
 record = parser.getRecord();
 } catch (IOException e) {
 e.printStackTrace();
 }

 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsConsistent_MismatchedSize() {
 CSVParser parser = new CSVParser(new StringReader("name1,name2,name3,name4\nvalue1,value2,value3"),
         CSVFormat.DEFAULT);
 CSVRecord record = null;
 try {
 record = parser.getRecord();
 } catch (IOException e) {
 e.printStackTrace();
 }

 assertFalse(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVParser parser = new CSVParser(new StringReader(CSV), CSVFormat.DEFAULT);
 CSVRecord record = null;
 try {
 record = parser.getRecord();
 } catch (IOException e) {
 e.printStackTrace();
 }

 assertTrue(record.isMapped("name1"));
 assertTrue(record.isMapped("name2"));
 assertTrue(record.isMapped("name3"));
 assertFalse(record.isMapped("nonExistentName"));
 }

 @Test
 public void testIsSet() {
 CSVParser parser = new CSVParser(new StringReader(CSV), CSVFormat.DEFAULT);
 CSVRecord record = null;
 try {
 record = parser.getRecord();
 } catch (IOException e) {
 e.printStackTrace();
 }

 assertTrue(record.isSet("name1"));
 assertTrue(record.isSet("name2"));
 assertTrue(record.isSet("name3"));
 assertFalse(record.isSet("nonExistentName"));
 }

 @Test
 public void testIterator() {
 CSVParser parser = new CSVParser(new StringReader(CSV), CSVFormat.DEFAULT);
 CSVRecord record = null;
 try {
 record = parser.getRecord();
 } catch (IOException e) {
 e.printStackTrace();
 }

 List<String> values = record.values();
 Iterator<String> iterator = record.iterator();

 int i = 0;
 while (iterator.hasNext()) {
 assertEquals(values.get(i), iterator.next());
 i++;
 }
 }

 @Test
 public void testPutIn() {
 CSVParser parser = new CSVParser(new StringReader(CSV), CSVFormat.DEFAULT);
 CSVRecord record = null;
 try {
 record = parser.getRecord();
 } catch (IOException e) {
 e.printStackTrace();
 }

 Map<String, String> map = new HashMap<>();
 record.putIn(map);

 assertEquals(3, map.size());
 assertEquals("value1_1", map.get("name1"));
 assertEquals("value1_2", map.get("name2"));
 assertEquals("value1_3", map.get("name3"));
 }
}

enum Column {
 NAME1, NAME2, NAME3
}