import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.*;
import java.text.*;

public class FastDateFormatTest {
    private FastDateFormat fastDateFormat;

    @Before
    public void setup() {
        fastDateFormat = FastDateFormat.getInstance();
    }

    @Test
    public void testFormatObject() {
        Object obj = new Object();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);

        StringBuffer result = fastDateFormat.format(obj, toAppendTo, pos);

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testFormatLong() {
        long millis = 123456;
        String result = fastDateFormat.format(millis);

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testFormatDate() {
        Date date = new Date();
        String result = fastDateFormat.format(date);

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testFormatCalendar() {
        Calendar calendar = Calendar.getInstance();
        String result = fastDateFormat.format(calendar);

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testFormatLongStringBuffer() {
        long millis = 123456;
        StringBuffer buf = new StringBuffer();
        StringBuffer result = fastDateFormat.format(millis, buf);

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testFormatDateStringBuffer() {
        Date date = new Date();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = fastDateFormat.format(date, buf);

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testFormatCalendarStringBuffer() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = fastDateFormat.format(calendar, buf);

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testParseString() throws ParseException {
        String source = "2022-01-01";
        Date result = fastDateFormat.parse(source);

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testParseStringParsePosition() {
        String source = "2022-01-01";
        ParsePosition pos = new ParsePosition(0);
        Date result = fastDateFormat.parse(source, pos);

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testParseObject() {
        String source = "2022-01-01";
        ParsePosition pos = new ParsePosition(0);
        Object result = fastDateFormat.parseObject(source, pos);

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testGetPattern() {
        String result = fastDateFormat.getPattern();

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testGetTimeZone() {
        TimeZone result = fastDateFormat.getTimeZone();

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testGetLocale() {
        Locale result = fastDateFormat.getLocale();

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testGetMaxLengthEstimate() {
        int result = fastDateFormat.getMaxLengthEstimate();

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testEquals() {
        Object obj = new Object();
        boolean result = fastDateFormat.equals(obj);

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testHashCode() {
        int result = fastDateFormat.hashCode();

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testToString() {
        String result = fastDateFormat.toString();

        // Assert the result is as expected
        // ...

    }

    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = fastDateFormat.applyRules(calendar, buf);

        // Assert the result is as expected
        // ...

    }

}