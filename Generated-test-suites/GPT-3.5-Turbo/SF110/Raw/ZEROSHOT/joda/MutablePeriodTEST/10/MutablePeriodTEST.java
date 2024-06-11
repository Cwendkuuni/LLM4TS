import org.joda.time.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class MutablePeriodTest {
    private MutablePeriod mutablePeriod;

    @Before
    public void setUp() {
        mutablePeriod = new MutablePeriod();
    }

    @After
    public void tearDown() {
        mutablePeriod = null;
    }

    @Test
    public void testParse() {
        MutablePeriod parsedPeriod = MutablePeriod.parse("P1Y2M");

        assertThat(parsedPeriod.getYears()).isEqualTo(1);
        assertThat(parsedPeriod.getMonths()).isEqualTo(2);
    }

    @Test
    public void testSetAndGetYears() {
        mutablePeriod.setYears(3);

        assertThat(mutablePeriod.getYears()).isEqualTo(3);
    }

    @Test
    public void testAddYears() {
        mutablePeriod.setYears(1);
        mutablePeriod.addYears(2);

        assertThat(mutablePeriod.getYears()).isEqualTo(3);
    }

    @Test
    public void testSetAndGetMonths() {
        mutablePeriod.setMonths(4);

        assertThat(mutablePeriod.getMonths()).isEqualTo(4);
    }

    @Test
    public void testAddMonths() {
        mutablePeriod.setMonths(2);
        mutablePeriod.addMonths(3);

        assertThat(mutablePeriod.getMonths()).isEqualTo(5);
    }

    @Test
    public void testSetAndGetWeeks() {
        mutablePeriod.setWeeks(2);

        assertThat(mutablePeriod.getWeeks()).isEqualTo(2);
    }

    @Test
    public void testAddWeeks() {
        mutablePeriod.setWeeks(1);
        mutablePeriod.addWeeks(2);

        assertThat(mutablePeriod.getWeeks()).isEqualTo(3);
    }

    @Test
    public void testSetAndGetDays() {
        mutablePeriod.setDays(5);

        assertThat(mutablePeriod.getDays()).isEqualTo(5);
    }

    @Test
    public void testAddDays() {
        mutablePeriod.setDays(3);
        mutablePeriod.addDays(2);

        assertThat(mutablePeriod.getDays()).isEqualTo(5);
    }

    @Test
    public void testSetAndGetHours() {
        mutablePeriod.setHours(10);

        assertThat(mutablePeriod.getHours()).isEqualTo(10);
    }

    @Test
    public void testAddHours() {
        mutablePeriod.setHours(3);
        mutablePeriod.addHours(7);

        assertThat(mutablePeriod.getHours()).isEqualTo(10);
    }

    @Test
    public void testSetAndGetMinutes() {
        mutablePeriod.setMinutes(25);

        assertThat(mutablePeriod.getMinutes()).isEqualTo(25);
    }

    @Test
    public void testAddMinutes() {
        mutablePeriod.setMinutes(10);
        mutablePeriod.addMinutes(15);

        assertThat(mutablePeriod.getMinutes()).isEqualTo(25);
    }

    @Test
    public void testSetAndGetSeconds() {
        mutablePeriod.setSeconds(45);

        assertThat(mutablePeriod.getSeconds()).isEqualTo(45);
    }

    @Test
    public void testAddSeconds() {
        mutablePeriod.setSeconds(30);
        mutablePeriod.addSeconds(15);

        assertThat(mutablePeriod.getSeconds()).isEqualTo(45);
    }

    @Test
    public void testSetAndGetMillis() {
        mutablePeriod.setMillis(500);

        assertThat(mutablePeriod.getMillis()).isEqualTo(500);
    }

    @Test
    public void testAddMillis() {
        mutablePeriod.setMillis(200);
        mutablePeriod.addMillis(300);

        assertThat(mutablePeriod.getMillis()).isEqualTo(500);
    }

    @Test
    public void testCopy() {
        mutablePeriod.setYears(2);
        mutablePeriod.setMonths(6);

        MutablePeriod copiedPeriod = mutablePeriod.copy();

        assertThat(copiedPeriod).isNotSameAs(mutablePeriod);
        assertThat(copiedPeriod.getYears()).isEqualTo(mutablePeriod.getYears());
        assertThat(copiedPeriod.getMonths()).isEqualTo(mutablePeriod.getMonths());
    }
}