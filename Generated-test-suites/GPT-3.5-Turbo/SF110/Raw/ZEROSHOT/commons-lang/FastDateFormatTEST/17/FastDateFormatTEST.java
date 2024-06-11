import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.time.FastDateFormat;

public class FastDateFormatTest {

	@Test
	public void testGetInstance() {
		FastDateFormat format = FastDateFormat.getInstance();
		assertNotNull(format);
	}

	@Test
	public void testGetInstanceString() {
		FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
		assertNotNull(format);
		assertEquals("yyyy-MM-dd", format.getPattern());
	}

	@Test
	public void testGetInstanceStringTimeZone() {
		FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT+2"));
		assertNotNull(format);
		assertEquals("GMT+02:00", format.getTimeZone().getID());
	}

	@Test
	public void testGetInstanceStringLocale() {
		FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
		assertNotNull(format);
		assertEquals(Locale.US, format.getLocale());
	}

	@Test
	public void testGetInstanceStringTimeZoneLocale() {
		FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT+2"), Locale.US);
		assertNotNull(format);
		assertEquals("yyyy-MM-dd", format.getPattern());
		assertEquals("GMT+02:00", format.getTimeZone().getID());
		assertEquals(Locale.US, format.getLocale());
	}

	@Test
	public void testGetDateInstanceInt() {
		FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.FULL);
		assertNotNull(format);
		assertEquals(FastDateFormat.FULL, format.getMaxLengthEstimate());
	}

	@Test
	public void testGetDateInstanceIntLocale() {
		FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
		assertNotNull(format);
		assertEquals(FastDateFormat.FULL, format.getMaxLengthEstimate());
		assertEquals(Locale.US, format.getLocale());
	}

	@Test
	public void testGetDateInstanceIntTimeZone() {
		FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT+2"));
		assertNotNull(format);
		assertEquals(FastDateFormat.FULL, format.getMaxLengthEstimate());
		assertEquals("GMT+02:00", format.getTimeZone().getID());
	}

	@Test
	public void testGetDateInstanceIntTimeZoneLocale() {
		FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT+2"), Locale.US);
		assertNotNull(format);
		assertEquals(FastDateFormat.FULL, format.getMaxLengthEstimate());
		assertEquals("GMT+02:00", format.getTimeZone().getID());
		assertEquals(Locale.US, format.getLocale());
	}

	@Test
	public void testGetTimeInstanceInt() {
		FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
		assertNotNull(format);
		assertEquals(FastDateFormat.FULL, format.getMaxLengthEstimate());
	}

	@Test
	public void testGetTimeInstanceIntLocale() {
		FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.US);
		assertNotNull(format);
		assertEquals(FastDateFormat.FULL, format.getMaxLengthEstimate());
		assertEquals(Locale.US, format.getLocale());
	}

	@Test
	public void testGetTimeInstanceIntTimeZone() {
		FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT+2"));
		assertNotNull(format);
		assertEquals(FastDateFormat.FULL, format.getMaxLengthEstimate());
		assertEquals("GMT+02:00", format.getTimeZone().getID());
	}

	@Test
	public void testGetTimeInstanceIntTimeZoneLocale() {
		FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT+2"), Locale.US);
		assertNotNull(format);
		assertEquals(FastDateFormat.FULL, format.getMaxLengthEstimate());
		assertEquals("GMT+02:00", format.getTimeZone().getID());
		assertEquals(Locale.US, format.getLocale());
	}

	@Test
	public void testGetDateTimeInstanceIntInt() {
		FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
		assertNotNull(format);
		assertEquals(FastDateFormat.FULL, format.getMaxLengthEstimate());
	}

	@Test
	public void testGetDateTimeInstanceIntIntLocale() {
		FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.US);
		assertNotNull(format);
		assertEquals(FastDateFormat.FULL, format.getMaxLengthEstimate());
		assertEquals(Locale.US, format.getLocale());
	}

	@Test
	public void testGetDateTimeInstanceIntIntTimeZone() {
		FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getTimeZone("GMT+2"));
		assertNotNull(format);
		assertEquals(FastDateFormat.FULL, format.getMaxLengthEstimate());
		assertEquals("GMT+02:00", format.getTimeZone().getID());
	}

	@Test
	public void testGetDateTimeInstanceIntIntTimeZoneLocale() {
		FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getTimeZone("GMT+2"), Locale.US);
		assertNotNull(format);
		assertEquals(FastDateFormat.FULL, format.getMaxLengthEstimate());
		assertEquals("GMT+02:00", format.getTimeZone().getID());
		assertEquals(Locale.US, format.getLocale());
	}

	@Test
	public void testFormatObjectStringBufferFieldPosition() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		StringBuffer buf = new StringBuffer();
		FieldPosition pos = new FieldPosition(FastDateFormat.MONTH);
		StringBuffer result = format.format(new Date(), buf, pos);
		assertEquals(buf, result);
	}

	@Test
	public void testFormatLong() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		String result = format.format(System.currentTimeMillis());
		assertNotNull(result);
	}

	@Test
	public void testFormatDate() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		String result = format.format(new Date());
		assertNotNull(result);
	}

	@Test
	public void testFormatCalendar() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		String result = format.format(Calendar.getInstance());
		assertNotNull(result);
	}

	@Test
	public void testFormatLongStringBuffer() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		StringBuffer buf = new StringBuffer();
		StringBuffer result = format.format(System.currentTimeMillis(), buf);
		assertEquals(buf, result);
	}

	@Test
	public void testFormatDateStringBuffer() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		StringBuffer buf = new StringBuffer();
		StringBuffer result = format.format(new Date(), buf);
		assertEquals(buf, result);
	}

	@Test
	public void testFormatCalendarStringBuffer() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		StringBuffer buf = new StringBuffer();
		StringBuffer result = format.format(Calendar.getInstance(), buf);
		assertEquals(buf, result);
	}

	@Test
	public void testParseString() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		try {
			Date result = format.parse("01-01-2020");
			assertNotNull(result);
		} catch (ParseException e) {
			fail("Parsing failed");
		}
	}

	@Test
	public void testParseStringParsePosition() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		ParsePosition pos = new ParsePosition(0);
		Date result = format.parse("01-01-2020", pos);
		assertNotNull(result);
		assertEquals(10, pos.getIndex());
	}

	@Test
	public void testParseObject() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		Object result = format.parseObject("01-01-2020", new ParsePosition(0));
		assertNotNull(result);
	}

	@Test
	public void testGetPattern() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		assertEquals("dd-MM-yyyy", format.getPattern());
	}

	@Test
	public void testGetTimeZone() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		assertEquals(TimeZone.getDefault(), format.getTimeZone());
	}

	@Test
	public void testGetLocale() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		assertEquals(Locale.getDefault(), format.getLocale());
	}

	@Test
	public void testGetMaxLengthEstimate() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		assertEquals(10, format.getMaxLengthEstimate());
	}

	@Test
	public void testEquals() {
		FastDateFormat format1 = FastDateFormat.getInstance("dd-MM-yyyy");
		FastDateFormat format2 = FastDateFormat.getInstance("MM-dd-yyyy");
		assertFalse(format1.equals(format2));
	}

	@Test
	public void testHashCode() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		assertNotNull(format.hashCode());
	}

	@Test
	public void testToString() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		assertEquals("FastDateFormat[dd-MM-yyyy," + Locale.getDefault() + "," + TimeZone.getDefault().getID() + "]", format.toString());
	}

	@Test
	public void testApplyRules() {
		FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy");
		StringBuffer buf = new StringBuffer();
		StringBuffer result = format.applyRules(Calendar.getInstance(), buf);
		assertEquals(buf, result);
	}
}