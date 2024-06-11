
package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class FastDateFormatTest {

    private static final int MAX_ITERS = 20;

    @Test
    public void testShort() {
        final FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS", TimeZone.getTimeZone("GMT+05:30"), Locale.ENGLISH);
        final GregorianCalendar cal = new GregorianCalendar(2018, Calendar.MARCH, 25, 11, 30, 45);
        cal.set(Calendar.MILLISECOND, 789);
        assertEquals("2018-03-25 11:30:45.789", fdf.format(cal));
    }

    @Test(expected = NullPointerException.class)
    public void testApplyRules3_NullCalendar() {
        FastDateFormat.getInstance("yyyy-DDD HH:mm:ss.SSS", TimeZone.getDefault(), Locale.getDefault())
                .applyRules(null, new StringBuffer());
        fail("Expected NullPointerException to be thrown");
    }

    @Test(expected = NullPointerException.class)
    public void testApplyRules3_NullBuffer() {
        FastDateFormat.getInstance("yyyy-DDD HH:mm:ss.SSS", TimeZone.getDefault(), Locale.getDefault())
                .applyRules(new GregorianCalendar(), (StringBuffer) null);
        fail("Expected NullPointerException to be thrown");
    }

    // ... (continue with more test cases)

}
