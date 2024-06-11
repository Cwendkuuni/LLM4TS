package org.apache.commons.csv;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordGetMethod() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);

 // Test get method with valid index
 String expectedValue = "John";
 String actualValue = record.get(0);
 assertEquals(expectedValue, actualValue);

 // Test get method with invalid index
 String nullValue = null;
 String actualValueInvalidIndex = record.get(2);
 assertEquals(nullValue, actualValueInvalidIndex);
 }

 @Test
 public void testCSVRecordGetByName() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John", "Age:30"}, new HashMap<String, Integer>() {{
 put("Name", 0);
 put("Age", 1);
 }}, null, 1);

 // Test get method with valid name
 String expectedValueByName = "John";
 String actualValueByName = record.get("Name");
 assertEquals(expectedValueByName, actualValueByName);

 // Test get method with invalid name
 String nullValueByName = null;
 String actualValueInvalidName = record.get("InvalidName");
 assertEquals(nullValueByName, actualValueInvalidName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordGetByNameWithNoMapping() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John", "Age:30"}, null, null, 1);
 String nullValueByName = null;
 String actualValueInvalidName = record.get("Name");
 assertEquals(nullValueByName, actualValueInvalidName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordGetByNameWithMismatchedMappingAndValues() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John", "Age:31"}, new HashMap<String, Integer>() {{
 put("Name", 0);
 put("Age", 1);
 }}, null, 1);
 String actualValueInvalidName = record.get("Name");
 assertEquals(null, actualValueInvalidName);
 }

 @Test
 public void testCSVRecordIsConsistent() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John", "Age:30"}, new HashMap<String, Integer>() {{
 put("Name", 0);
 put("Age", 1);
 }}, null, 1);
 boolean expectedResult = true;
 boolean actualResult = record.isConsistent();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCSVRecordIsConsistentWithMismatchedMappingAndValues() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John", "Age:31"}, new HashMap<String, Integer>() {{
 put("Name", 0);
 put("Age", 1);
 }}, null, 1);
 boolean expectedResult = false;
 boolean actualResult = record.isConsistent();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCSVRecordIsMapped() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John", "Age:30"}, new HashMap<String, Integer>() {{
 put("Name", 0);
 put("Age", 1);
 }}, null, 1);
 boolean expectedResult = true;
 boolean actualResult = record.isMapped("Name");
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCSVRecordIsMappedWithUnmappedName() {
 CSVRecord record = new CSVRecord(new String[]{"Name:John", "Age:30"}, null, null, 1);
 boolean expectedResult = false;
 boolean actualResult = record.isMapped("InvalidName");
 assertEquals(expectedResult, actualResult);
 }
}