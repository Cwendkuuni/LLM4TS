package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.ISOChronology;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        // Test between two instants
        ReadableInstant start = new BaseDateTime(0, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = new BaseDateTime(1, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        assertEquals(24 * 60 * 60 * 1000, BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS));

        // Test between two partials
        ReadablePartial startPartial = new BaseDateTime(0, 0, 0, 0, 1, 0, 0, ISOChronology.getInstanceUTC()).toLocalDate();
        ReadablePartial endPartial = new BaseDateTime(0, 0, 0, 1, 0, 0, 0, ISOChronology.getInstanceUTC()).toLocalDate();
        assertEquals(24 * 60 * 60 * 1000, BaseSingleFieldPeriod.between(startPartial, endPartial, PeriodType.hoursInstance()));
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.days(1).withHours(1).withMinutes(1).withSeconds(1).withMillis(1);
        assertEquals(86401001L, BaseSingleFieldPeriod.standardPeriodIn(period, 1000));

        ReadablePeriod imprecisePeriod = Period.days(1).withHours(2);
        try {
            BaseSingleFieldPeriod.standardPeriodIn(imprecisePeriod, 1000);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBaseSingleFieldPeriodConstructors() {
        BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        BaseSingleFieldPeriod instance2 = new BaseSingleFieldPeriod(instance1);

        assertEquals(instance1, instance2);

        assertEquals(instance1.getClass(), instance2.getClass());

        assertEquals(instance1.getValue(), instance2.getValue());

        assertEquals(instance1.getFieldType(), instance2.getFieldType());

        assertEquals(instance1.getPeriodType(), instance2.getPeriodType());

        BaseSingleFieldPeriod instance3 = new BaseSingleFieldPeriod(instance1.getValue());

        assertEquals(instance1, instance3);

        assertEquals(instance1.getClass(), instance3.getClass());

        assertEquals(instance1.getValue(), instance3.getValue());

        assertEquals(instance1.getFieldType(), instance3.getFieldType());

        assertEquals(instance1.getPeriodType(), instance3.getPeriodType());

        // Test serialization
        BaseSingleFieldPeriod instance4 = new BaseSingleFieldPeriod(instance1);

        BaseSingleFieldPeriod deserializedInstance = (BaseSingleFieldPeriod) new ObjectStreamClass(instance1.getClass())
                .readObject(new ByteArrayInputStream(serializeObjectToByteArray(instance1)));

        assertEquals(instance1, deserializedInstance);

        assertEquals(instance1.getClass(), deserializedInstance.getClass());

        assertEquals(instance1.getValue(), deserializedInstance.getValue());

        assertEquals(instance1.getFieldType(), deserializedInstance.getFieldType());

        assertEquals(instance1.getPeriodType(), deserializedInstance.getPeriodType());
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        assertEquals(24 * 60, instance.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        instance.setValue(48 * 60);

        assertEquals(48 * 60, instance.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        assertEquals(DurationFieldType.HOURS, instance.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        assertEquals(PeriodType.hoursInstance(), instance.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        assertEquals(1, instance.size());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        assertEquals(DurationFieldType.HOURS, instance.getFieldType(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        instance.getValue(1);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        assertEquals(0, instance.get(DurationFieldType.MINUTES));
        assertEquals(0, instance.get(DurationFieldType.SECONDS));
        assertEquals(0, instance.get(DurationFieldType.MILLIS));
        assertEquals(24 * 60, instance.get(DurationFieldType.HOURS));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        assertTrue(instance.isSupported(DurationFieldType.HOURS));
        assertFalse(instance.isSupported(DurationFieldType.MINUTES));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        Period period = instance.toPeriod();

        assertEquals(1, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(24, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        MutablePeriod mutablePeriod = instance.toMutablePeriod();

        assertEquals(instance.getValue(), mutablePeriod.getValue());
        assertEquals(instance.getFieldType(), mutablePeriod.getFieldType());
        assertEquals(instance.getPeriodType(), mutablePeriod.getPeriodType());

        mutablePeriod.add(DurationFieldType.HOURS, 1);

        assertNotEquals(instance.getValue(), mutablePeriod.getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddToMutablePeriod() {
        BaseSingleFieldPeriod instance = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        MutablePeriod mutablePeriod = instance.toMutablePeriod();

        mutablePeriod.add(DurationFieldType.HOURS, 1);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        BaseSingleFieldPeriod instance2 = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        BaseSingleFieldPeriod instance3 = new BaseSingleFieldPeriod(24 * 60 + 1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        BaseSingleFieldPeriod instance4 = null;

        BaseSingleFieldPeriod instance5 = new Object() {
            @Override
            public int hashCode() {
                return 1;
            }
        };

        assertEquals(instance1, instance1);
        assertEquals(instance1, instance2);
        assertNotEquals(instance1, instance3);
        assertNull(instance1.equals(instance4));
        assertFalse(instance1.equals(instance5));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        BaseSingleFieldPeriod instance2 = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        BaseSingleFieldPeriod instance3 = new BaseSingleFieldPeriod(24 * 60 + 1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        int hashCode1 = instance1.hashCode();

        assertEquals(hashCode1, instance1.hashCode());
        assertEquals(hashCode1, instance2.hashCode());
        assertNotEquals(hashCode1, instance3.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod instance1 = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        BaseSingleFieldPeriod instance2 = new BaseSingleFieldPeriod(24 * 60 + 1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        BaseSingleFieldPeriod instance3 = new BaseSingleFieldPeriod(24 * 60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.hoursInstance();
            }
        };

        assertEquals(0, instance1.compareTo(instance1));
        assertEquals(-1, instance1.compareTo(instance2));
        assertEquals(1, instance2.compareTo(instance1));
        assertEquals(0, instance1.compareTo(instance3));
    }

    private byte[] serializeObjectToByteArray(Object obj) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
        try {
            outputStrm.writeObject(obj);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }
}