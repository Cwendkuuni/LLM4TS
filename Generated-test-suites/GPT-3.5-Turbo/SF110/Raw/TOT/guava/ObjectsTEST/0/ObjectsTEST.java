
package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    // Test cases for equal() method

    @Test
    public void testEqual_nullObjects() {
        boolean result = Objects.equal(null, null);
        assertTrue(result);
    }

    @Test
    public void testEqual_nullAndNonNullObject() {
        boolean result = Objects.equal(null, "test");
        assertFalse(result);
    }

    @Test
    public void testEqual_nonNullAndNullObject() {
        boolean result = Objects.equal("test", null);
        assertFalse(result);
    }

    @Test
    public void testEqual_nonNullAndSameObject() {
        String obj = "test";
        boolean result = Objects.equal(obj, obj);
        assertTrue(result);
    }

    @Test
    public void testEqual_nonNullAndEqualObject() {
        boolean result = Objects.equal("test", "test");
        assertTrue(result);
    }

    @Test
    public void testEqual_nonNullAndDifferentObject() {
        boolean result = Objects.equal("test1", "test2");
        assertFalse(result);
    }

    // Test cases for hashCode() method

    @Test
    public void testHashCode_noObjects() {
        int result = Objects.hashCode();
        assertEquals(0, result);
    }

    @Test
    public void testHashCode_singleNullObject() {
        int result = Objects.hashCode(null);
        assertEquals(0, result);
    }

    @Test
    public void testHashCode_singleNonNullObject() {
        int result = Objects.hashCode("test");
        assertEquals("test".hashCode(), result);
    }

    @Test
    public void testHashCode_multipleObjects() {
        int result = Objects.hashCode("test1", "test2", "test3");
        int expected = "test1".hashCode() ^ "test2".hashCode() ^ "test3".hashCode();
        assertEquals(expected, result);
    }

    // Test cases for toStringHelper() method

    @Test
    public void testToStringHelper_selfObject() {
        Objects.ToStringHelper helper = Objects.toStringHelper(this);
        assertNotNull(helper);
    }

    @Test
    public void testToStringHelper_classObject() {
        Objects.ToStringHelper helper = Objects.toStringHelper(String.class);
        assertNotNull(helper);
    }

    @Test
    public void testToStringHelper_className() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        assertNotNull(helper);
    }

    // Test cases for firstNonNull() method

    @Test
    public void testFirstNonNull_bothNull() {
        Object result = Objects.firstNonNull(null, null);
        assertNull(result);
    }

    @Test
    public void testFirstNonNull_firstNull() {
        String second = "test";
        Object result = Objects.firstNonNull(null, second);
        assertEquals(second, result);
    }

    @Test
    public void testFirstNonNull_secondNull() {
        String first = "test";
        Object result = Objects.firstNonNull(first, null);
        assertEquals(first, result);
    }

    @Test
    public void testFirstNonNull_bothNonNull() {
        String first = "test1";
        String second = "test2";
        Object result = Objects.firstNonNull(first, second);
        assertEquals(first, result);
    }
    
    // Test cases for ToStringHelper class
    
    @Test
    public void testToStringHelper_omitNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .omitNullValues();
        assertNotNull(helper);
        // Additional test: Verify that omitNullValues() actually sets the omitNullValues flag to true.
        assertTrue(helper.omitNullValues);
    }
  
    @Test
    public void testToStringHelper_addNameAndValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .add("name1", "value1");
        assertNotNull(helper);
        // Additional tests: Verify that add() method adds the name and value correctly.
        assertEquals("name1=value1", helper.toString());
    }
    
    @Test
    public void testToStringHelper_addBooleanValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .add("name1", true);
        assertNotNull(helper);
        // Additional test: Verify that add() method handles boolean values correctly.
        assertEquals("name1=true", helper.toString());
    }
    
    @Test
    public void testToStringHelper_addCharValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .add("name1", 'c');
        assertNotNull(helper);
        // Additional test: Verify that add() method handles char values correctly.
        assertEquals("name1=c", helper.toString());
    }
    
    @Test
    public void testToStringHelper_addDoubleValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .add("name1", 3.14);
        assertNotNull(helper);
        // Additional test: Verify that add() method handles double values correctly.
        assertEquals("name1=3.14", helper.toString());
    }
    
    @Test
    public void testToStringHelper_addFloatValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .add("name1", 3.14f);
        assertNotNull(helper);
        // Additional test: Verify that add() method handles float values correctly.
        assertEquals("name1=3.14", helper.toString());
    }
    
    @Test
    public void testToStringHelper_addIntValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .add("name1", 42);
        assertNotNull(helper);
        // Additional test: Verify that add() method handles int values correctly.
        assertEquals("name1=42", helper.toString());
    }
    
    @Test
    public void testToStringHelper_addLongValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .add("name1", 1234567890L);
        assertNotNull(helper);
        // Additional test: Verify that add() method handles long values correctly.
        assertEquals("name1=1234567890", helper.toString());
    }
    
    @Test
    public void testToStringHelper_addValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .addValue("value1");
        assertNotNull(helper);
        // Additional test: Verify that addValue() method adds the value correctly.
        assertEquals("value1", helper.toString());
    }
    
    @Test
    public void testToStringHelper_addBooleanValue_directMethod() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .addValue(true);
        assertNotNull(helper);
        // Additional test: Verify that addValue() method handles boolean values correctly.
        assertEquals("true", helper.toString());
    }
    
    @Test
    public void testToStringHelper_addCharValue_directMethod() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .addValue('c');
        assertNotNull(helper);
        // Additional test: Verify that addValue() method handles char values correctly.
        assertEquals("c", helper.toString());
    }
    
    @Test
    public void testToStringHelper_addDoubleValue_directMethod() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .addValue(3.14);
        assertNotNull(helper);
        // Additional test: Verify that addValue() method handles double values correctly.
        assertEquals("3.14", helper.toString());
    }
    
    @Test
    public void testToStringHelper_addFloatValue_directMethod() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .addValue(3.14f);
        assertNotNull(helper);
        // Additional test: Verify that addValue() method handles float values correctly.
        assertEquals("3.14", helper.toString());
    }
    
    @Test
    public void testToStringHelper_addIntValue_directMethod() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .addValue(42);
        assertNotNull(helper);
        // Additional test: Verify that addValue() method handles int values correctly.
        assertEquals("42", helper.toString());
    }
    
    @Test
    public void testToStringHelper_addLongValue_directMethod() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test")
                .addValue(1234567890L);
        assertNotNull(helper);
        // Additional test: Verify that addValue() method handles long values correctly.
        assertEquals("1234567890", helper.toString());
    }

}

