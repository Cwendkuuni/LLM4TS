import org.joda.time.*;
import org.junit.Test;

public class MutablePeriodTest {
	
	@Test
	public void testParse() {
		String s = "P1Y2M3W4DT5H6M7S";
		MutablePeriod expected = new MutablePeriod();
		expected.setYears(1);
		expected.setMonths(2);
		expected.setWeeks(3);
		expected.setDays(4);
		expected.setHours(5);
		expected.setMinutes(6);
		expected.setSeconds(7);
		
		MutablePeriod actual = MutablePeriod.parse(s);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSetYears() {
		MutablePeriod period = new MutablePeriod();
		int years = 5;
		
		period.setYears(years);
		
		assertEquals(years, period.getYears());
	}
	
	@Test
	public void testSetMonths() {
		MutablePeriod period = new MutablePeriod();
		int months = 6;
		
		period.setMonths(months);
		
		assertEquals(months, period.getMonths());
	}
	
	@Test
	public void testSetWeeks() {
		MutablePeriod period = new MutablePeriod();
		int weeks = 3;
		
		period.setWeeks(weeks);
		
		assertEquals(weeks, period.getWeeks());
	}

	@Test
	public void testSetDays() {
		MutablePeriod period = new MutablePeriod();
		int days = 10;
		
		period.setDays(days);
		
		assertEquals(days, period.getDays());
	}
	
	@Test
	public void testSetHours() {
		MutablePeriod period = new MutablePeriod();
		int hours = 2;
		
		period.setHours(hours);
		
		assertEquals(hours, period.getHours());
	}
	
	@Test
	public void testSetMinutes() {
		MutablePeriod period = new MutablePeriod();
		int minutes = 30;
		
		period.setMinutes(minutes);
		
		assertEquals(minutes, period.getMinutes());
	}
	
	@Test
	public void testSetSeconds() {
		MutablePeriod period = new MutablePeriod();
		int seconds = 45;
		
		period.setSeconds(seconds);
		
		assertEquals(seconds, period.getSeconds());
	}
	
	@Test
	public void testSetMillis() {
		MutablePeriod period = new MutablePeriod();
		int millis = 500;
		
		period.setMillis(millis);
		
		assertEquals(millis, period.getMillis());
	}
	
	@Test
	public void testAddYears() {
		MutablePeriod period = new MutablePeriod();
		int years = 3;
		
		period.addYears(years);
		
		assertEquals(years, period.getYears());
	}
	
	@Test
	public void testAddMonths() {
		MutablePeriod period = new MutablePeriod();
		int months = 4;
		
		period.addMonths(months);
		
		assertEquals(months, period.getMonths());
	}
	
	@Test
	public void testAddWeeks() {
		MutablePeriod period = new MutablePeriod();
		int weeks = 2;
		
		period.addWeeks(weeks);
		
		assertEquals(weeks, period.getWeeks());
	}
	
	@Test
	public void testAddDays() {
		MutablePeriod period = new MutablePeriod();
		int days = 5;
		
		period.addDays(days);
		
		assertEquals(days, period.getDays());
	}
	
	@Test
	public void testAddHours() {
		MutablePeriod period = new MutablePeriod();
		int hours = 1;
		
		period.addHours(hours);
		
		assertEquals(hours, period.getHours());
	}
	
	@Test
	public void testAddMinutes() {
		MutablePeriod period = new MutablePeriod();
		int minutes = 45;
		
		period.addMinutes(minutes);
		
		assertEquals(minutes, period.getMinutes());
	}
	
	@Test
	public void testAddSeconds() {
		MutablePeriod period = new MutablePeriod();
		int seconds = 30;
		
		period.addSeconds(seconds);
		
		assertEquals(seconds, period.getSeconds());
	}
	
	@Test
	public void testAddMillis() {
		MutablePeriod period = new MutablePeriod();
		int millis = 800;
		
		period.addMillis(millis);
		
		assertEquals(millis, period.getMillis());
	}
	
	@Test
	public void testCopy() {
		MutablePeriod period = new MutablePeriod();
		
		MutablePeriod copy = period.copy();
		
		assertEquals(period, copy);
	}
	
}