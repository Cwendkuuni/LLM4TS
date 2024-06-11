package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class CSVRecordTest {

 private String[] values;
 private Map<String, Integer> mapping;
 private String comment;
 private long recordNumber;

 public CSVRecordTest(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) {
 this.values = values;
 this.mapping = mapping;
 this.comment = comment;
 this.recordNumber = recordNumber;
 }

 @Parameterized.Parameters(name = "{index}: values={0}, mapping={1}, comment={2}, recordNumber={3}")
 public static Iterable<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // valid test cases
 {new String[]{"foo", "bar", "baz"}, null, null, 0L},
 {new String[]{"foo", "bar", "baz"}, new HashMap<String, Integer>(){{put("foo", 0); put("bar", 1); put("baz", 2);}}, null, 0L},
 {new String[]{"foo", "bar", "baz"}, new HashMap<String, Integer>(){{put("foo", 0); put("bar", 1); put("baz", 2);}}, "comment", 1L},
 // get() tests
 {new String[]{"foo", "bar", "baz"}, null, null, 0L},
 {new String[]{"foo", "bar", "baz"}, new HashMap<String, Integer>(){{put("foo", 0); put("bar", 1); put("baz", 2);}}, "bar", 1},
 {new String[]{"foo", "bar", "baz"}, new HashMap<String, Integer>(){{put("foo", 0); put("bar", 1); put("baz", 2);}}, null, 2},
 // isConsistent() tests
 {new String[]{"foo"}, null, null, 0L},
 {new String[]{"foo", "bar"}, null, null, 0L},
 {new String[]{"foo", "bar"}, new HashMap<String, Integer>(){{put("foo", 0); put("bar", 1);}}, true},
 {new String[]{"foo", "bar", "baz"}, new HashMap<String, Integer>(){{put("foo", 0); put("bar", 1); put("baz", 2);}}, true},
 {new String[]{"foo", "bar", "baz", "qux"}, new HashMap<String, Integer>(){{put("foo", 0); put("bar", 1); put("baz", 2); put("qux", 3);}}, true},
 {new String[]{"foo", "bar", "baz", "qux"}, null, null, 0L},
 {new String[]{"foo", "bar", "baz", "qux"}, new HashMap<String, Integer>(){{put("foo", 0); put("bar", 1); put("baz", 2); put("qux", 3);}}, false},
 // isMapped() tests
 {new String[]{"foo", "bar", "baz"}, null, null, 0L},
 {new String[]{"foo", "bar", "baz"}, new HashMap<String, Integer>(){{put("foo", 0); put("bar", 1); put("baz", 2);}}, "foo", true},
 {new String[]{"foo", "bar", "baz"}, new HashMap<String, Integer>(){{put("foo", 0); put("bar", 1); put("baz", 2);}}, "qux", false},
 // isSet() tests
 {new String[]{"foo", "bar", "baz"}, null, null, 0L},
 {new String[]{"foo", "bar", "baz"}, new HashMap<String, Integer>(){{put("foo", 0); put("bar", 1); put("baz", 2);}}, "foo", true},
 {new String[]{"foo", "bar", "baz"}, new HashMap<String, Integer>(){{put("foo", 0); put("bar", 1); put("baz", 2);}}, "qux", false},
 {new String[]{"foo", "bar", "baz"}, new HashMap<String, Integer>(){{put("foo", 0); put("bar", 1); put("baz", 2); put("qux", 3);}}, "qux", true},
 // size() tests
 {new String[]{"foo"}, 0},
 {new String[]{"foo", "bar"}, 2},
 {new String[]{"foo", "bar", "baz"}, 3},
 // getComment() tests
 {new String[]{"foo"}, null},
 {new String[]{"foo", "bar"}, null},
 {new String[]{"foo", "bar", "baz"}, "comment"},
 // getRecordNumber() tests
 {new String[]{"foo"}, 0},
 {new String[]{"foo", "bar"}, 0},
 {new String[]{"foo", "bar", "baz"}, 1},
 });
 }

 @Test
 public void testValues() {
 assertArrayEquals(values, CSVRecord.values(values()));
 }

 @Test
 public void testGet() {
 assertEquals(mapping.getOrDefault("foo", 0), CSVRecord.get(new CSVRecord(values, mapping, comment, recordNumber), 0));
 assertNull(CSVRecord.get(new CSVRecord(values, mapping, comment, recordNumber), "qux"));
 }

 @Test
 public void testIsConsistent() {
 assertEquals(true, CSVRecord.isConsistent(new CSVRecord(values, mapping, comment, recordNumber)));
 assertEquals(false, CSVRecord.isConsistent(new CSVRecord(new String[0], null, null, 0L)));
 }

 @Test
 public void testIsMapped() {
 assertFalse(CSVRecord.isMapped(new CSVRecord(values, mapping, comment, recordNumber), "qux"));
 assertTrue(CSVRecord.isMapped(new CSVRecord(values, mapping, comment, recordNumber), "foo"));
 }

 @Test
 public void testIsSet() {
 assertFalse(CSVRecord.isSet(new CSVRecord(values, mapping, comment, recordNumber), "qux"));
 assertTrue(CSVRecord.isSet(new CSVRecord(values, mapping, comment, recordNumber), "foo"));
 }

 @Test
 public void testIterator() {
 List<String> list = Arrays.asList(values());
 Iterator<String> iterator = CSVRecord.iterator(new CSVRecord(values, mapping, comment, recordNumber));
 assertEquals(list, CSVRecord.asList(iterator));
 }

 @Test
 public void testToString() {
 assertEquals("[foo, bar, baz]", CSVRecord.toString(new CSVRecord(values, mapping, comment, recordNumber)));
 }

 @Test
 public void testValuesNull() {
 assertNull(CSVRecord.values(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInvalidIndex() {
 CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
 CSVRecord.get(record, -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInvalidName() {
 CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
 CSVRecord.get(record, "qux");
 }

 @Test(expected = IllegalStateException.class)
 public void testGetNoMapping() {
 CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
 CSVRecord.get(record, "foo");
 }
}