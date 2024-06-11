package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
	private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @Before
    public void setUp() {
        values = new String[]{"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        comment = "This is a comment";
        recordNumber = 1;
    }

	@Test
	public void testConstructor() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		assertEquals(values.length, record.size());
		assertEquals(mapping, record.toMap());
		assertEquals(comment, record.getComment());
		assertEquals(recordNumber, record.getRecordNumber());
	}

	@Test(expected = IllegalStateException.class)
	public void testGetWithInvalidHeaderMapping() {
		CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
		record.get("column1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetWithInvalidColumn() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		record.get("unknownColumn");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetWithInvalidIndex() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		record.get(3);
	}

	@Test
	public void testGetByEnum() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		assertEquals("value1", record.get(Column.COLUMN1));
		assertEquals("value2", record.get(Column.COLUMN2));
		assertEquals("value3", record.get(Column.COLUMN3));
	}

	@Test
	public void testGetByName() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		assertEquals("value1", record.get("column1"));
		assertEquals("value2", record.get("column2"));
		assertEquals("value3", record.get("column3"));
	}

	@Test
	public void testGetByIndex() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		assertEquals("value1", record.get(0));
		assertEquals("value2", record.get(1));
		assertEquals("value3", record.get(2));
	}

	@Test
	public void testIsConsistentWithNullMapping() {
		CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
		assertTrue(record.isConsistent());
	}

	@Test
	public void testIsConsistentWithValidMapping() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		assertTrue(record.isConsistent());
	}

	@Test
	public void testIsConsistentWithInvalidMapping() {
		mapping.put("column4", 3);
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		assertFalse(record.isConsistent());
	}

	@Test
	public void testIsMappedWithMappedColumn() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		assertTrue(record.isMapped("column1"));
		assertTrue(record.isMapped("column2"));
		assertTrue(record.isMapped("column3"));
	}

	@Test
	public void testIsMappedWithUnmappedColumn() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		assertFalse(record.isMapped("unknownColumn"));
	}

	@Test
	public void testIsSetWithSetColumn() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		assertTrue(record.isSet("column1"));
		assertTrue(record.isSet("column2"));
		assertTrue(record.isSet("column3"));
	}

	@Test
	public void testIsSetWithUnsetColumn() {
		values = new String[]{"value1", "value2"};
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		assertFalse(record.isSet("column3"));
	}

	@Test
	public void testIterator() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		int i = 0;
		for (String value : record) {
			assertEquals(values[i++], value);
		}
	}

	@Test
	public void testPutIn() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		Map<String, String> map = record.putIn(new HashMap<String, String>());
		assertEquals(mapping.size(), map.size());
		assertEquals("value1", map.get("column1"));
		assertEquals("value2", map.get("column2"));
		assertEquals("value3", map.get("column3"));
	}

	@Test
	public void testSize() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		assertEquals(values.length, record.size());
	}

	@Test
	public void testToString() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		assertEquals("[value1, value2, value3]", record.toString());
	}

	@Test
	public void testValues() {
		CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
		assertEquals(values, record.values());
	}

	private enum Column {
		COLUMN1, COLUMN2, COLUMN3;
	}
}