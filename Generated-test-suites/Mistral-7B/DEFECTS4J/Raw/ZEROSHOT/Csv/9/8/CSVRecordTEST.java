import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

 private CSVRecord record;
 private CSVFormat format;

 @Before
 public void setUp() {
 format = new CSVFormat();
 String csv = "name1,name2,name3\nJohn Doe,Smith,30\nJane Doe,Doe,25";
 try (ByteArrayInputStream input = new ByteArrayInputStream(csv.getBytes())) {
 CSVParser parser = new CSVParser(new InputStreamReader(input), format);
 record = parser.nextRecord();
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGet() {
 Assert.assertEquals("John Doe", record.get(0));
 Assert.assertEquals("Smith", record.get(1));
 Assert.assertEquals("30", record.get(2));
 }

 @Test
 public void testGetEnum() {
 Assert.assertEquals("Smith", record.get(CSVRecordTest.Name.NAME_2));
 }

 @Test
 public void testGetNullComment() {
 Assert.assertNull(record.getComment());
 }

 @Test
 public void testGetComment() {
 Assert.assertEquals("", record.get("comment"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetUnknownField() {
 record.get("unknownField");
 }

 @Test(expected = IllegalStateException.class)
 public void testGetWithoutMapping() {
 CSVRecord recordWithoutMapping = new CSVRecord(null, null, null, 0);
 recordWithoutMapping.get(0);
 }

 @Test
 public void testIsConsistent() {
 Assert.assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 Assert.assertTrue(record.isMapped("name1"));
 Assert.assertTrue(record.isMapped("NAME1"));
 Assert.assertTrue(record.isMapped("name2"));
 Assert.assertTrue(record.isMapped("NAME2"));
 Assert.assertTrue(record.isMapped("name3"));
 Assert.assertTrue(record.isMapped("NAME3"));
 Assert.assertFalse(record.isMapped("unknown"));
 }

 @Test
 public void testIsSet() {
 Assert.assertTrue(record.isSet("name1"));
 Assert.assertTrue(record.isSet("name2"));
 Assert.assertTrue(record.isSet("name3"));
 Assert.assertFalse(record.isSet("unknown"));
 }

 @Test
 public void testSize() {
 Assert.assertEquals(3, record.size());
 }

 @Test
 public void testIterator() {
 List<String> expected = Arrays.asList("John Doe", "Smith", "30");
 List<String> actual = Arrays.asList(record.iterator().toArray(new String[0]));
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testPutIn() {
 Map<String, String> map = new HashMap<>();
 record.putIn(map);
 Map<String, String> expected = new HashMap<>();
 expected.put("name1", "John Doe");
 expected.put("name2", "Smith");
 expected.put("name3", "30");
 Assert.assertEquals(expected, map);
 }

 @Test
 public void testToString() {
 Assert.assertEquals("[John Doe, Smith, 30]", record.toString());
 }

 @Test
 public void testValues() {
 String[] expected = new String[]{"John Doe", "Smith", "30"};
 String[] actual = record.values();
 Assert.assertArrayEquals(expected, actual);
 }
}

enum Name {
 NAME_1,
 NAME_2,
 NAME_3
}