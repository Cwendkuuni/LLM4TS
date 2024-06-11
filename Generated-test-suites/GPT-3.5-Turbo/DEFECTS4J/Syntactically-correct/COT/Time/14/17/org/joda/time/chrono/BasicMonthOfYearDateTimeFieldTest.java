import static org.junit.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

	@Test
	public void testIsLenient() {
		BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(null, 0);
		assertFalse(monthField.isLenient());
	}

	@Test
	public void testGet() {
		BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(null, 0);
		long instant = 0;
		assertEquals(0, monthField.get(instant));
	}

	@Test
	public void testAdd() {
		BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(null, 0);
		long instant = 0;
		int months = 1;
		assertEquals(0, monthField.add(instant, months));
	}

	@Test
	public void testAddWrapField() {
		BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(null, 0);
		long instant = 0;
		int months = 1;
		assertEquals(0, monthField.addWrapField(instant, months));
	}

	@Test
	public void testGetDifferenceAsLong() {
		BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(null, 0);
		long minuendInstant = 0;
		long subtrahendInstant = 1;
		assertEquals(-1, monthField.getDifferenceAsLong(minuendInstant, subtrahendInstant));
	}

	@Test
	public void testSet() {
		BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(null, 0);
		long instant = 0;
		int month = 1;
		assertEquals(0, monthField.set(instant, month));
	}

	@Test
	public void testIsLeap() {
		BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(null, DateTimeConstants.JANUARY);
		long instant = 0;
		assertFalse(monthField.isLeap(instant));
	}

	@Test
	public void testGetLeapAmount() {
		BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(null, DateTimeConstants.JANUARY);
		long instant = 0;
		assertEquals(0, monthField.getLeapAmount(instant));
	}

	@Test
	public void testGetMinimumValue() {
		BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(null, 0);
		assertEquals(DateTimeConstants.JANUARY, monthField.getMinimumValue());
	}

	@Test
	public void testGetMaximumValue() {
		BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(null, 0);
		assertEquals(DateTimeConstants.DECEMBER, monthField.getMaximumValue());
	}

	@Test
	public void testRoundFloor() {
		BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(null, 0);
		long instant = 0;
		assertEquals(0, monthField.roundFloor(instant));
	}

	@Test
	public void testRemainder() {
		BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(null, 0);
		long instant = 0;
		assertEquals(0, monthField.remainder(instant));
	}

}