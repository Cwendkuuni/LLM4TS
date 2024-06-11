import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CSVRecordTest {

 private CSVRecord csvRecord;
 @Mock
 private CSVParser parser;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 when(parser.getHeaderMap()).thenReturn(createHeaderMap());
 when(parser.parseNextRecord()).thenReturn(csvRecord);
 }

 @Test
 public void testGet() {
 String expectedValue = "expectedValue";
 csvRecord = new CSVRecord(new String[]{"column1", "column2", expectedValue}, createHeaderMap(), null, 1L);

 String actualValue = csvRecord.get(0);
 assertThat(actualValue).isEqualTo("column1");

 actualValue = csvRecord.get(2);
 assertThat(actualValue).isEqualTo(expectedValue);
 }

 @Test
 public void testGetByName() {
 String expectedValue = "expectedValue";
 csvRecord = new CSVRecord(new String[]{"column1", "column2", expectedValue}, createHeaderMap(), null, 1L);

 String actualValue = csvRecord.get("column2");
 assertThat(actualValue).isEqualTo("column2");

 actualValue = csvRecord.get("column3");
 assertThat(actualValue).isNull();
 }

 @Test
 public void testIsConsistent() {
 csvRecord = new CSVRecord(new String[]{"column1", "column2"}, createHeaderMap(), null, 1L);

 boolean actualResult = csvRecord.isConsistent();
 boolean expectedResult = true;
 assertThat(actualResult).isEqualTo(expectedResult);
 }

 @Test
 public void testIsConsistentWithInconsistentRecord() {
 csvRecord = new CSVRecord(new String[]{"column1", "column2"}, null, null, 1L);

 boolean actualResult = csvRecord.isConsistent();
 boolean expectedResult = false;
 assertThat(actualResult).isEqualTo(expectedResult);
 }

 @Test
 public void testIsMapped() {
 String columnName = "column1";
 csvRecord = new CSVRecord(new String[]{"column1", "column2"}, createHeaderMap(), null, 1L);

 boolean actualResult = csvRecord.isMapped(columnName);
 boolean expectedResult = true;
 assertThat(actualResult).isEqualTo(expectedResult);
 }

 @Test
 public void testIsSet() {
 String columnName = "column1";
 csvRecord = new CSVRecord(new String[]{"column1", "column2"}, createHeaderMap(), null, 1L);

 boolean actualResult = csvRecord.isSet(columnName);
 boolean expectedResult = true;
 assertThat(actualResult).isEqualTo(expectedResult);
 }

 @Test
 public void testIterator() {
 String[] expectedValues = new String[]{"column1", "column2"};
 csvRecord = new CSVRecord(expectedValues, createHeaderMap(), null, 1L);

 List<String> actualValues = Arrays.asList(csvRecord.iterator().toArray(new String[0]));
 assertThat(actualValues).isEqualTo(Arrays.asList(expectedValues));
 }

 @Test
 public void testSize() {
 String[] expectedValues = new String[]{"column1", "column2"};
 csvRecord = new CSVRecord(expectedValues, createHeaderMap(), null, 1L);

 int actualSize = csvRecord.size();
 int expectedSize = expectedValues.length;
 assertThat(actualSize).isEqualTo(expectedSize);
 }

 @Test
 public void testToString() {
 String[] expectedValues = new String[]{"column1", "column2"};
 csvRecord = new CSVRecord(expectedValues, createHeaderMap(), null, 1L);

 String actualToString = csvRecord.toString();
 String expectedToString = "[\"" + String.join("\", \"", expectedValues) + "\"]";
 assertThat(actualToString).isEqualTo(expectedToString);
 }

 @Test
 public void testGetComment() {
 String expectedComment = "expectedComment";
 csvRecord = new CSVRecord(new String[]{"column1", "column2"}, createHeaderMap(), expectedComment, 1L);

 String actualComment = csvRecord.getComment();
 assertThat(actualComment).isEqualTo(expectedComment);
 }

 @Test
 public void testGetRecordNumber() {
 long expectedRecordNumber = 1L;
 csvRecord = new CSVRecord(new String[]{"column1", "column2"}, createHeaderMap(), null, expectedRecordNumber);

 long actualRecordNumber = csvRecord.getRecordNumber();
 assertThat(actualRecordNumber).isEqualTo(expectedRecordNumber);
 }

 private Map<String, Integer> createHeaderMap() {
 Map<String, Integer> headerMap = new HashMap<>();
 headerMap.put("column1", 0);
 headerMap.put("column2", 1);
 return headerMap;
 }

 @Test
 public void testGetValues() {
 String[] expectedValues = new String[]{"column1", "column2"};
 csvRecord = new CSVRecord(expectedValues, createHeaderMap(), null, 1L);

 String[] actualValues = csvRecord.values();
 assertThat(actualValues).isEqualTo(expectedValues);
 }

 @Test
 public void testGetValuesWithEmptyRecord() {
 csvRecord = new CSVRecord(new String[0], createHeaderMap(), null, 1L);

 String[] actualValues = csvRecord.values();
 String[] expectedValues = new String[0];
 assertThat(actualValues).isEqualTo(expectedValues);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetValueWithNoHeaderMapping() {
 csvRecord = new CSVRecord(new String[]{"column1", "column2"}, null, null, 1L);
 String value = csvRecord.get("column1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithInconsistentRecord() {
 csvRecord = new CSVRecord(new String[]{"column1", "column2"}, createHeaderMap(), null, 1L);
 csvRecord.mapping.put("column3", null);
 String value = csvRecord.get("column3");
 }

 private InputStream getInputStreamFromString(String csvContent) {
 return new ByteArrayInputStream(csvContent.getBytes());
 }

 @Test
 public void testParseRecordFromCsv() throws IOException {
 String csvContent = "column1,column2,expectedValue\n";
 InputStream inputStream = getInputStreamFromString(csvContent);
 when(parser.parseNextRecord(inputStream)).thenReturn(csvRecord);

 csvRecord = parser.parseNextRecord(inputStream);

 String actualValue = csvRecord.get("column2");
 String expectedValue = "expectedValue";
 assertThat(actualValue).isEqualTo(expectedValue);
 }
}