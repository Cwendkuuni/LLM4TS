import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 private CSVFormat csvFormat;
 private CSVParser csvParser;
 private String csvContent;

 @Before
 public void setUp() {
 csvFormat = CSVFormat.DEFAULT.withHeader();
 csvParser = new CSVParser(new StringReader(csvContent), csvFormat);
 }

 @Test
 public void testCSVRecordEmpty() {
 CSVRecord csvRecord = csvParser.nextRecord();
 assertTrue(csvRecord.isConsistent());
 assertNull(csvRecord.getComment());
 assertTrue(Arrays.equals(CSVRecord.EMPTY_STRING_ARRAY, csvRecord.values()));
 assertFalse(csvRecord.isMapped("name"));
 assertFalse(csvRecord.isSet("name"));
 }

 @Test
 public void testCSVRecordWithValues() {
 csvContent = "Name,Age,City,Country\nJohn Doe,30,New York,USA\nJane Doe,25,London,UK";
 CSVRecord csvRecord = csvParser.nextRecord();
 assertTrue(csvRecord.isConsistent());
 assertSame("Name", csvRecord.get("Name"));
 assertSame("Age", csvRecord.get("Age"));
 assertSame("City", csvRecord.get("City"));
 assertSame("Country", csvRecord.get("Country"));
 assertSame("30", csvRecord.get("Age"));
 assertSame("New York", csvRecord.get("City"));
 assertSame("USA", csvRecord.get("Country"));
 assertNull(csvRecord.getComment());
 assertSame(1, csvRecord.getRecordNumber());
 }

 @Test
 public void testCSVRecordWithComment() {
 csvContent = "Name,Age,City,Country\nName1,Age1,City1,Country1#comment1\nName2,Age2,City2,Country2#comment2";
 CSVRecord csvRecord = csvParser.nextRecord();
 assertTrue(csvRecord.isConsistent());
 assertSame("Name1", csvRecord.get("Name"));
 assertSame("Age1", csvRecord.get("Age"));
 assertSame("City1", csvRecord.get("City"));
 assertSame("Country1", csvRecord.get("Country"));
 assertSame("comment1", csvRecord.getComment());
 assertSame(1, csvRecord.getRecordNumber());
 }

 @Test
 public void testCSVRecordWithNullValue() {
 csvContent = "Name,Age,City,Country\nName1,Age1,City1,null\nName2,Age2,City2,Country2";
 CSVRecord csvRecord = csvParser.nextRecord();
 assertTrue(csvRecord.isConsistent());
 assertSame("Name1", csvRecord.get("Name"));
 assertSame("Age1", csvRecord.get("Age"));
 assertSame("City1", csvRecord.get("City"));
 assertNull(csvRecord.get("Country"));
 assertSame("1", csvRecord.getRecordNumber());
 }

 @Test
 public void testCSVRecordWithMissingValue() {
 csvContent = "Name,Age,City,Country\nName1,Age1,City1\nName2,Age2";
 CSVRecord csvRecord = csvParser.nextRecord();
 assertFalse(csvRecord.isConsistent());
 }

 @Test
 public void testCSVRecordIterator() {
 csvContent = "Name,Age,City,Country\nName1,Age1,City1,Country1\nName2,Age2,City2,Country2";
 List<CSVRecord> csvRecords = Arrays.asList(csvParser.getRecords());
 Map<String, String> map = new HashMap<>();
 for (CSVRecord record : csvRecords) {
 map.putAll(record.toMap());
 }
 assertEquals("Name1", map.get("Name1"));
 assertEquals("Age1", map.get("Age1"));
 assertEquals("City1", map.get("City1"));
 assertEquals("Country1", map.get("Country1"));
 assertEquals("Name2", map.get("Name2"));
 assertEquals("Age2", map.get("Age2"));
 assertEquals("City2", map.get("City2"));
 assertEquals("Country2", map.get("Country2"));
 }
}