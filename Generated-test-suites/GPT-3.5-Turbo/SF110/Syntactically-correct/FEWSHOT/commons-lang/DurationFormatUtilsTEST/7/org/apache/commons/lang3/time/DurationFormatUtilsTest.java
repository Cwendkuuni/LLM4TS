
package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        assertEquals("2:30:59.999", DurationFormatUtils.formatDurationHMS(9059999));
        assertEquals("5:00:00.000", DurationFormatUtils.formatDurationHMS(18000000));
        assertEquals("0:00:00.001", DurationFormatUtils.formatDurationHMS(1));
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals("P0001Y002M003DT004H005M006S007", DurationFormatUtils.formatDurationISO(400600700));
        assertEquals("P0000Y000M000DT000H000M000S001", DurationFormatUtils.formatDurationISO(1));
        assertEquals("P0000Y000M029DT023H059M010S001", DurationFormatUtils.formatDurationISO(2067110001));
    }

    @Test
    public void testFormatDuration() {
        assertEquals("123:45:01.123", DurationFormatUtils.formatDuration(445501123, "HH:mm:ss.SSS"));
        assertEquals("12D345H01M23S", DurationFormatUtils.formatDuration(1060563000, "d'D'H'H'm'm's'S'")); 
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals("1 day 2 hours 3 minutes 4 seconds", DurationFormatUtils.formatDurationWords(93784000, false, false));
        assertEquals("1 day 2 hours 3 minutes", DurationFormatUtils.formatDurationWords(93780000, false, true));  
    }

    @Test
    public void testFormatPeriodISO() {
        assertEquals("P0001Y002M003DT004H005M006S007", DurationFormatUtils.formatPeriodISO(1557072111556528L, 1557493008634528L)); 
    }

    @Test
    public void testFormatPeriod() {
        assertEquals("P0002Y", DurationFormatUtils.formatPeriod(1557072111556528L, 1619681864556515L, "yyyy"));
    }

}
