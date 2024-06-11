package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    private final DurationFieldType type = DurationFieldType.millis();
    
    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }
    
    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("milliseconds", field.getName());
    }
    
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1000L, System.currentTimeMillis());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1000L, System.currentTimeMillis());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1, System.currentTimeMillis());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1000L, System.currentTimeMillis());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(System.currentTimeMillis(), 1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(System.currentTimeMillis(), 1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(System.currentTimeMillis(), 0L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(System.currentTimeMillis(), 0L);
    }
    
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        DurationField supportedField = new SupportedDurationField(type);
        
        assertEquals(1, field.compareTo(supportedField));
        assertEquals(0, field.compareTo(field));
    }
    
    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        
        assertTrue(field1.equals(field2));
        
        DurationField supportedField = new SupportedDurationField(type);
        assertFalse(field1.equals(supportedField));
    }
    
    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        
        assertEquals(field1.hashCode(), field2.hashCode());
        
        DurationField supportedField = new SupportedDurationField(type);
        assertFalse(field1.hashCode() == supportedField.hashCode());
    }
    
    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[milliseconds]", field.toString());
    }
    
    private class SupportedDurationField extends DurationField {

        private final DurationFieldType type;
        
        public SupportedDurationField(DurationFieldType type) {
            this.type = type;
        }
        
        @Override
        public DurationFieldType getType() {
            return type;
        }

        @Override
        public String getName() {
            return type.getName();
        }

        @Override
        public boolean isSupported() {
            return true;
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public int getValue(long duration) {
            return 0;
        }

        @Override
        public long getValueAsLong(long duration) {
            return 0;
        }

        @Override
        public int getValue(long duration, long instant) {
            return 0;
        }

        @Override
        public long getValueAsLong(long duration, long instant) {
            return 0;
        }

        @Override
        public long getMillis(int value) {
            return 0;
        }

        @Override
        public long getMillis(long value) {
            return 0;
        }

        @Override
        public long getMillis(int value, long instant) {
            return 0;
        }

        @Override
        public long getMillis(long value, long instant) {
            return 0;
        }

        @Override
        public long add(long instant, int value) {
            return 0;
        }

        @Override
        public long add(long instant, long value) {
            return 0;
        }

        @Override
        public int getDifference(long minuendInstant, long subtrahendInstant) {
            return 0;
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return 0;
        }

        @Override
        public long getUnitMillis() {
            return 0;
        }

        @Override
        public int compareTo(DurationField durationField) {
            return 0;
        }
        
    }
}