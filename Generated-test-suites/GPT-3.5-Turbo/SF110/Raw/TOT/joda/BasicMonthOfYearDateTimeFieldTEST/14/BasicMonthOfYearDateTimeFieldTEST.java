import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.field.FieldUtils;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;
    private BasicChronology chronology;

    @Before
    public void setUp() {
        chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 6);
    }

    @Test
    public void testConstructor() {
        assertEquals(DateTimeFieldType.monthOfYear(), field.getType());
        assertEquals(1, field.getMinimumValue());
        assertEquals(12, field.getMaximumValue());
        assertFalse(field.isLeap(1577836800000L)); // January 1st, 2020
    }

    @Test
    public void testGet() {
        assertEquals(1, field.get(1577836800000L)); // January 1st, 2020
        assertEquals(12, field.get(1609459199000L)); // December 31st, 2020
        assertEquals(6, field.get(1598889600000L)); // September 1st, 2020
    }

    @Test
    public void testAdd() {
        assertEquals(1577836800000L, field.add(1577836800000L, 0)); // January 1st, 2020
        assertEquals(1580515200000L, field.add(1577836800000L, 1)); // February 1st, 2020
        assertEquals(1609459199000L, field.add(1577836800000L, 11)); // December 31st, 2020
        assertEquals(1612137599000L, field.add(1577836800000L, 12)); // January 31st, 2021
        assertEquals(1598889600000L, field.add(1577836800000L, 8)); // September 1st, 2020
        assertEquals(1601567999000L, field.add(1577836800000L, 9)); // October 1st, 2020
    }

    @Test
    public void testAddWrapField() {
        assertEquals(1577836800000L, field.addWrapField(1577836800000L, 0)); // January 1st, 2020
        assertEquals(1580515200000L, field.addWrapField(1577836800000L, 1)); // February 1st, 2020
        assertEquals(1609459199000L, field.addWrapField(1577836800000L, 11)); // December 31st, 2020
        assertEquals(1580515200000L, field.addWrapField(1577836800000L, 13)); // February 1st, 2020
        assertEquals(1583193600000L, field.addWrapField(1577836800000L, 14)); // March 1st, 2020
    }

    @Test
    public void testGetDifferenceAsLong() {
        assertEquals(0, field.getDifferenceAsLong(1577836800000L, 1577836800000L)); // January 1st, 2020
        assertEquals(-1, field.getDifferenceAsLong(1577836800000L, 1609459199000L)); // December 31st, 2020
        assertEquals(2, field.getDifferenceAsLong(1577836800000L, 1612137599000L)); // January 31st, 2021
        assertEquals(8, field.getDifferenceAsLong(1577836800000L, 1598889600000L)); // September 1st, 2020
    }

    @Test
    public void testSet() {
        assertEquals(1580515200000L, field.set(1577836800000L, 2)); // January 1st, 2020 -> February 1st, 2020
        assertEquals(1609459199000L, field.set(1577836800000L, 12)); // January 1st, 2020 -> December 31st, 2020
        assertEquals(1598889600000L, field.set(1577836800000L, 9)); // January 1st, 2020 -> September 1st, 2020
    }

    @Test
    public void testRoundFloor() {
        assertEquals(1577836800000L, field.roundFloor(1577836800000L)); // January 1st, 2020
        assertEquals(1577836800000L, field.roundFloor(1580515200000L)); // February 1st, 2020
        assertEquals(1577836800000L, field.roundFloor(1598889600000L)); // September 1st, 2020
    }

    @Test
    public void testRemainder() {
        assertEquals(0, field.remainder(1577836800000L)); // January 1st, 2020
        assertEquals(2678400000L, field.remainder(1609459199000L)); // December 31st, 2020
        assertEquals(3559680100000L, field.remainder(1612137599000L)); // January 31st, 2021
    }

    @Test
    public void testIsLeap() {
        assertFalse(field.isLeap(1577836800000L)); // January 1st, 2020
        assertFalse(field.isLeap(1609459199000L)); // December 31st, 2020
        assertTrue(field.isLeap(1612137599000L)); // January 31st, 2021
    }

    @Test
    public void testGetLeapAmount() {
        assertEquals(0, field.getLeapAmount(1577836800000L)); // January 1st, 2020
        assertEquals(0, field.getLeapAmount(1609459199000L)); // December 31st, 2020
        assertEquals(1, field.getLeapAmount(1612137599000L)); // January 31st, 2021
    }

    @Test
    public void testGetLeapDurationField() {
        assertEquals(field.getLeapDurationField(), chronology.days());
    }

}