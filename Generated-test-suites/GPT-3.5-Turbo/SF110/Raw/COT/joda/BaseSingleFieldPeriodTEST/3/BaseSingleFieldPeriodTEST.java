import org.joda.time.*;
import org.junit.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenReadableInstant() {
        ReadableInstant instant1 = new DateTime(2021, 1, 1, 0, 0);
        ReadableInstant instant2 = new DateTime(2022, 1, 1, 0, 0);
        DurationFieldType fieldType = DurationFieldType.days();

        int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);

        Assert.assertEquals(365, result);
    }

    @Test
    public void testBetweenReadablePartial() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1);
        ReadablePartial partial2 = new LocalDate(2022, 1, 1);
        ReadablePeriod period = Period.years();

        int result = BaseSingleFieldPeriod.between(partial1, partial2, period);

        Assert.assertEquals(1, result);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.days(365);
        long duration = 86400000;

        int result = BaseSingleFieldPeriod.standardPeriodIn(period, duration);

        Assert.assertEquals(365, result);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(10);

        int result = singleFieldPeriod.getValue();

        Assert.assertEquals(10, result);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(10);

        singleFieldPeriod.setValue(20);
        int result = singleFieldPeriod.getValue();

        Assert.assertEquals(20, result);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        DurationFieldType fieldType = singleFieldPeriod.getFieldType();

        Assert.assertEquals(DurationFieldType.days(), fieldType);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };

        PeriodType periodType = singleFieldPeriod.getPeriodType();

        Assert.assertEquals(PeriodType.days(), periodType);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(10);

        int size = singleFieldPeriod.size();

        Assert.assertEquals(1, size);
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        DurationFieldType fieldType = singleFieldPeriod.getFieldType(0);

        Assert.assertEquals(DurationFieldType.days(), fieldType);
    }

    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(10);

        int value = singleFieldPeriod.getValue(0);

        Assert.assertEquals(10, value);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        int value = singleFieldPeriod.get(DurationFieldType.days());

        Assert.assertEquals(10, value);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        boolean isSupported = singleFieldPeriod.isSupported(DurationFieldType.days());

        Assert.assertTrue(isSupported);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        Period period = singleFieldPeriod.toPeriod();

        Assert.assertEquals(Period.ZERO.withFields(singleFieldPeriod), period);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        MutablePeriod mutablePeriod = singleFieldPeriod.toMutablePeriod();

        MutablePeriod expectedPeriod = new MutablePeriod();
        expectedPeriod.add(singleFieldPeriod);

        Assert.assertEquals(expectedPeriod, mutablePeriod);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        boolean equals = singleFieldPeriod1.equals(singleFieldPeriod2);

        Assert.assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        int hashCode = singleFieldPeriod.hashCode();

        Assert.assertEquals(27 * (27 * 17 + 10) + DurationFieldType.days().hashCode(), hashCode);
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        int compareTo = singleFieldPeriod1.compareTo(singleFieldPeriod2);

        Assert.assertEquals(1, compareTo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenReadableInstantNull() {
        ReadableInstant instant1 = null;
        ReadableInstant instant2 = new DateTime(2022, 1, 1, 0, 0);
        DurationFieldType fieldType = DurationFieldType.days();

        BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenReadablePartialNull() {
        ReadablePartial partial1 = null;
        ReadablePartial partial2 = new LocalDate(2022, 1, 1);
        ReadablePeriod period = Period.years();

        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenReadablePartialDifferentFields() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1);
        ReadablePartial partial2 = new LocalDateTime(2022, 1, 1, 0, 0);
        ReadablePeriod period = Period.years();

        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNotContiguous() {
        ReadablePartial partial1 = new LocalDateTime(2021, 1, 1, 0, 0);
        ReadablePartial partial2 = new LocalDateTime(2022, 1, 2, 0, 0);
        ReadablePeriod period = Period.years();

        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNotPrecise() {
        ReadablePeriod period = new Period(1, 0, 0, 0, 0, 0, 0, 0);
        long duration = 86400000;

        BaseSingleFieldPeriod.standardPeriodIn(period, duration);
    }

    @Test
    public void testStandardPeriodInNull() {
        ReadablePeriod period = null;
        long duration = 86400000;

        int result = BaseSingleFieldPeriod.standardPeriodIn(period, duration);

        Assert.assertEquals(0, result);
    }
}