package org.joda.time.field;

import org.joda.time.*;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientDateTimeField;
    private Chronology baseChronology;

    @Before
    public void setUp() {
        baseChronology = GregorianChronology.getInstance();
        DateTimeField actualField = baseChronology.hourOfDay();
        lenientDateTimeField = (LenientDateTimeField) LenientDateTimeField.getInstance(actualField, baseChronology);
    }

    @Test
    public void testSet_validValue() {
        DateTime now = new DateTime(2021, 1, 1, 12, 0, 0);
        long instant = now.getMillis();
        int newValue = 15;

        long result = lenientDateTimeField.set(instant, newValue);

        DateTime expectedDateTime = now.withHourOfDay(newValue);
        Assert.assertEquals(expectedDateTime.getMillis(), result);
    }

    @Test
    public void testSet_outOfBoundsValue() {
        DateTime now = new DateTime(2021, 1, 1, 12, 0, 0);
        long instant = now.getMillis();
        int newValue = 25;

        long result = lenientDateTimeField.set(instant, newValue);

        DateTime expectedDateTime = now.withHourOfDay(newValue % 24);
        Assert.assertEquals(expectedDateTime.getMillis(), result);
    }

    @Test
    public void testSet_negativeValue() {
        DateTime now = new DateTime(2021, 1, 1, 12, 0, 0);
        long instant = now.getMillis();
        int newValue = -5;

        long result = lenientDateTimeField.set(instant, newValue);

        DateTime expectedDateTime = now.withHourOfDay(24 + (newValue % 24));
        Assert.assertEquals(expectedDateTime.getMillis(), result);
    }

    @Test
    public void testSet_wrappedFieldIsLenient() {
        DateTime now = new DateTime(2021, 1, 1, 12, 0, 0);
        long instant = now.getMillis();
        int newValue = 15;
        DateTimeField strictField = new StrictDateTimeField(baseChronology.hourOfDay());
        lenientDateTimeField = (LenientDateTimeField) LenientDateTimeField.getInstance(strictField, baseChronology);

        long result = lenientDateTimeField.set(instant, newValue);

        DateTime expectedDateTime = now.withHourOfDay(newValue);
        Assert.assertEquals(expectedDateTime.getMillis(), result);
    }
}
