import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 private CSVFormat format;
 private List<CSVRecord> records;

 @Before
 public void setUp() throws IOException {
 format = new CSVFormat();
 String csvText = "name1,name2,name3\nJohn Doe,1,2\nJane Doe,3,4";
 records = format.parse(new StringReader(csvText)).getRecords();
 }

 @Test
 public void testGetByEnum() {
 CSVRecord firstRecord = records.get(0);
 assertEquals("John Doe", firstRecord.get(Enum.NAME1));
 }

 @Test
 public void testGetByIndex() {
 CSVRecord firstRecord = records.get(0);
 assertEquals("John Doe", firstRecord.get(0));
 assertEquals("1", firstRecord.get(1));
 }

 @Test
 public void testGetByName() {
 CSVRecord firstRecord = records.get(0);
 assertEquals("John Doe", firstRecord.get("name1"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetByNameWithoutHeaderMapping() {
 CSVRecord firstRecord = records.get(0);
 firstRecord.mapping = null;
 firstRecord.get("name1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByNameInvalidIndex() {
 CSVRecord firstRecord = records.get(0);
 firstRecord.mapping = new HashMap<String, Integer>();
 firstRecord.mapping.put("name1", 0);
 firstRecord.mapping.put("name2", 1);
 firstRecord.mapping.put("name3", 2);
 firstRecord.get("name4");
 }

 @Test
 public void testGetComment() {
 CSVRecord firstRecord = records.get(0);
 assertNull(firstRecord.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 CSVRecord firstRecord = records.get(0);
 assertEquals(0L, firstRecord.getRecordNumber());
 }

 @Test
 public void testIsConsistent() {
 CSVRecord firstRecord = records.get(0);
 assertTrue(firstRecord.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVRecord firstRecord = records.get(0);
 assertTrue(firstRecord.isMapped("name1"));
 assertFalse(firstRecord.isMapped("name4"));
 }

 @Test
 public void testIsSet() {
 CSVRecord firstRecord = records.get(0);
 assertTrue(firstRecord.isSet("name1"));
 assertFalse(firstRecord.isSet("name4"));
 }

 @Test
 public void testSize() {
 CSVRecord firstRecord = records.get(0);
 assertEquals(3, firstRecord.size());
 }

 @Test
 public void testToString() {
 CSVRecord firstRecord = records.get(0);
 String expected = "[Ljava.lang.String;@15db9742";
 assertArrayEquals(new String[]{ "[Ljava.lang.String;@15db9742" }, new String[]{ expected } );
 }

 @Test
 public void testValues() {
 CSVRecord firstRecord = records.get(0);
 String[] expected = new String[]{"John Doe", "1", "2"};
 assertArrayEquals(expected, firstRecord.values());
 }

 @Test
 public void testPutIn() {
 CSVRecord firstRecord = records.get(0);
 Map<String, String> map = new HashMap<>();
 map = firstRecord.putIn(map);
 String expectedName1 = "John Doe";
 String expectedName2 = "1";
 String expectedName3 = "2";
 assertEquals(3, map.size());
 assertEquals(expectedName1, map.get("name1"));
 assertEquals(expectedName2, map.get("name2"));
 assertEquals(expectedName3, map.get("name3"));
 }
}