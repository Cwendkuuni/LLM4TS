import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

	private CSVRecord record;
	private String[] values;
	private Map<String, Integer> mapping;
	private String comment;
	private long recordNumber;

	@Before
	public void setUp() {
		values = new String[] { "value1", "value2", "value3" };
		mapping = null;
		comment = "Test Comment";
		recordNumber = 1;
		record = new CSVRecord(values, mapping, comment, recordNumber);
	}

	@Test
	public void testGetWithValidIndex() {
		assertEquals("value1", record.get(0));
		assertEquals("value2", record.get(1));
		assertEquals("value3", record.get(2));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testGetWithInvalidIndex() {
		record.get(3);
	}

	@Test
	public void testGetWithValidName() {
		assertEquals("value1", record.get("name1"));
	}

	@Test
	public void testGetWithInvalidName() {
		assertNull(record.get("name4"));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetWithNoMapping() {
		record.get("name1");
	}

	@Test
	public void testIsConsistentWithMapping() {
		assertTrue(record.isConsistent());
	}

	@Test
	public void testIsConsistentWithoutMapping() {
		mapping = null;
		record = new CSVRecord(values, mapping, comment, recordNumber);
		assertTrue(record.isConsistent());
	}

	@Test
	public void testIsMappedWithMappedColumn() {
		assertTrue(record.isMapped("name1"));
		assertTrue(record.isMapped("name2"));
		assertTrue(record.isMapped("name3"));
	}

	@Test
	public void testIsMappedWithUnmappedColumn() {
		assertFalse(record.isMapped("name4"));
	}

	@Test
	public void testIsSetWithMappedAndSetColumn() {
		assertTrue(record.isSet("name1"));
		assertTrue(record.isSet("name2"));
		assertTrue(record.isSet("name3"));
	}

	@Test
	public void testIsSetWithMappedAndUnsetColumn() {
		assertFalse(record.isSet("name4"));
	}

	@Test
	public void testIsSetWithUnmappedColumn() {
		assertFalse(record.isSet("name4"));
	}

	@Test
	public void testIterator() {
		assertEquals("value1", record.iterator().next());
	}

	@Test
	public void testValues() {
		assertArrayEquals(values, record.values());
	}

	@Test
	public void testGetComment() {
		assertEquals("Test Comment", record.getComment());
	}

	@Test
	public void testGetRecordNumber() {
		assertEquals(1, record.getRecordNumber());
	}

	@Test
	public void testSize() {
		assertEquals(3, record.size());
	}

	@Test
	public void testToString() {
		assertEquals("[value1, value2, value3]", record.toString());
	}
}