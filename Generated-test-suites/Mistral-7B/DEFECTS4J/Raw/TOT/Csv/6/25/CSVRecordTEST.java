package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

    @Test
    public void testCSVRecordHasExpectedFields() {
        final String csv = "Name,Age,Comment\nAlice,30,Comment for Alice\nBob,25,Comment for Bob\n";
        final CSVFormat format = new CSVFormat();
        final CSVParser parser = format.newParser(new StringReader(csv));
        final CSVRecord record = parser.nextRecord();

        assertEquals(2, record.size());
        assertEquals("Name", record.mapping.get("Name"));
        assertEquals("Age", record.mapping.get("Age"));
        assertEquals("Comment", record.mapping.get("Comment"));
        assertEquals("Alice", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertEquals("Comment for Alice", record.getComment());
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("InvalidName"));
        assertNull(record.get("InvalidName"));
    }

    @Test
    public void testCSVRecordWithNullValues() {
        final String csv = "Name,Age,Comment\nAlice,30,Comment for Alice\nBob,null,Comment for Bob\n";
        final CSVFormat format = new CSVFormat().withNullString("NULL");
        final CSVParser parser = format.newParser(new StringReader(csv));
        final CSVRecord record = parser.nextRecord();

        assertEquals(2, record.size());
        assertEquals("Name", record.mapping.get("Name"));
        assertEquals("Age", record.mapping.get("Age"));
        assertEquals("Comment", record.mapping.get("Comment"));
        assertEquals("Alice", record.get("Name"));
        assertEquals(null, record.get("Age"));
        assertEquals("Comment for Alice", record.getComment());
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("InvalidName"));
        assertNull(record.get("InvalidName"));
    }

    @Test
    public void testCSVRecordWithMissingValue() {
        final String csv = "Name,Age\nAlice,30\nBob,\n";
        final CSVFormat format = new CSVFormat();
        final CSVParser parser = format.newParser(new StringReader(csv));
        final CSVRecord record = parser.nextRecord();

        assertEquals(2, record.size());
        assertEquals("Name", record.mapping.get("Name"));
        assertEquals("Age", record.mapping.get("Age"));
        assertEquals(null, record.getComment());
        assertEquals("Alice", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("Comment"));
        assertNull(record.get("Comment"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithoutMapping() {
        final String csv = "Name,Age,Comment\nAlice,30,Comment for Alice\nBob,25,Comment for Bob\n";
        final CSVFormat format = new CSVFormat();
        final CSVParser parser = format.newParser(new StringReader(csv));
        final CSVRecord record = parser.nextRecord();
        record.get("InvalidName");
    }
}