import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CSVRecordTest class.
 */
public class CSVRecordTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
    }

    /**
     * Test get method with valid index.
     */
    @Test
    public void testGetValidIndex() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("john.doe@example.com", record.get(3));
    }

    /**
     * Test get method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get(values.length);
    }

    /**
     * Test get method with valid name.
     */
    @Test
    public void testGetValidName() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("John", record.get("firstName"));
        assertEquals("Doe", record.get("lastName"));
        assertEquals("30", record.get("age"));
        assertEquals("john.doe@example.com", record.get("email"));
        assertNull(record.get("nonExistentColumnName"));
    }

    /**
     * Test get method with invalid name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("nonExistentColumnName");
    }

    /**
     * Test isConsistent method.
     */
    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        values = new String[]{"John", "Doe", "30", "john.doe@example.com", null};
        mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isConsistent());

        values = new String[]{"John", "Doe", "30"};
        mapping = Map.of("firstName", 0, "lastName", 1, "age", 2);
        record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        mapping = Map.of("firstName", 0, "lastName", 2, "age", 1);
        record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    /**
     * Test isMapped method.
     */
    @Test
    public void testIsMapped() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("firstName"));
        assertTrue(record.isMapped("lastName"));
        assertTrue(record.isMapped("age"));
        assertTrue(record.isMapped("email"));
        assertFalse(record.isMapped("nonExistentColumnName"));
    }

    /**
     * Test isSet method.
     */
    @Test
    public void testIsSet() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("firstName"));
        assertTrue(record.isSet("lastName"));
        assertTrue(record.isSet("age"));
        assertTrue(record.isSet("email"));
        assertFalse(record.isSet("nonExistentColumnName"));

        values = new String[]{"John", "Doe", null, "john.doe@example.com"};
        mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("firstName"));
        assertTrue(record.isSet("lastName"));
        assertFalse(record.isSet("age"));
        assertTrue(record.isSet("email"));
        assertFalse(record.isSet("nonExistentColumnName"));
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Iterator<String> iterator = record.iterator();
        assertEquals("John", iterator.next());
        assertEquals("Doe", iterator.next());
        assertEquals("30", iterator.next());
        assertEquals("john.doe@example.com", iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(4, record.size());
    }

    /**
     * Test comment getter.
     */
    @Test
    public void testGetComment() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertNull(record.getComment());

        values = new String[]{"John", "Doe", "30", "john.doe@example.com", "Some comment"};
        mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        record = new CSVRecord(values, mapping, "Some comment", 1);

        assertEquals("Some comment", record.getComment());
    }

    /**
     * Test record number getter.
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());

        values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        mapping = Map.of("firstName", 0, "lastName", 1, "age", 2, "email", 3);
        record = new CSVRecord(values, mapping, null, 2);

        assertEquals(2, record.getRecordNumber());
    }
}