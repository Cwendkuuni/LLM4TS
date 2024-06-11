
/*
 *  Copyright 2001-2009 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.field;

import static org.junit.Assert.*;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.centuries();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertNotNull(field);
        assertEquals(type, field.getType());
    }
    
    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertEquals(type.getName(), field.getName());
    }
    
    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_Long() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getValue(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_Long() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getValueAsLong(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_Long_Long() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getValue(1000L, 500L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_Long_Long() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getValueAsLong(1000L, 500L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Int() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getMillis(5);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Long() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getMillis(5000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Int_Long() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getMillis(5, 1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Long_Long() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getMillis(5000L, 1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_Long_Int() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.add(1000L, 5);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_Long_Long() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.add(1000L, 5000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_Long_Long() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getDifference(1000L, 500L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_Long_Long() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        field.getDifferenceAsLong(1000L, 500L);
    }
    
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertEquals(0L, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.days();
        DurationFieldType type2 = DurationFieldType.months();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        
        assertEquals(0, field1.compareTo(field2));
    }
    
    @Test
    public void testEquals() {
        DurationFieldType type1 = DurationFieldType.weeks();
        DurationFieldType type2 = DurationFieldType.minutes();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        
        assertTrue(field1.equals(field1));
        assertFalse(field1.equals(field2));
    }
    
    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertEquals(type.getName().hashCode(), field.hashCode());
    }
    
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.months();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        assertEquals("UnsupportedDurationField[" + type.getName() + ']', field.toString());
    }
    
}
