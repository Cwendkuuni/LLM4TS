import org.junit.Test;

import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        assertTrue(true);
        assertFalse(false);
    }
    
    @Test
    public void testNotNull() {
        assertNotNull(Validate.notNull("test"));
        assertNull(Validate.notNull(null));
    }
    
    @Test
    public void testNotEmptyArray() {
        Integer[] array = {1, 2, 3};
        assertArrayEquals(array, Validate.notEmpty(array));
        
        Integer[] emptyArray = {};
        try {
            Validate.notEmpty(emptyArray);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated array is empty", e.getMessage());
        }
    }
    
    @Test
    public void testNotEmptyCollection() {
        List<String> list = new ArrayList<>();
        list.add("test");
        assertEquals(list, Validate.notEmpty(list));
        
        List<String> emptyList = new ArrayList<>();
        try {
            Validate.notEmpty(emptyList);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated collection is empty", e.getMessage());
        }
    }
    
    @Test
    public void testNotEmptyMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("key", 1);
        assertEquals(map, Validate.notEmpty(map));
        
        Map<String, Integer> emptyMap = new HashMap<>();
        try {
            Validate.notEmpty(emptyMap);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated map is empty", e.getMessage());
        }
    }
    
    @Test
    public void testNotBlank() {
        String validString = "valid";
        assertEquals(validString, Validate.notBlank(validString));
        
        String emptyString = "";
        try {
            Validate.notBlank(emptyString);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated character sequence is blank", e.getMessage());
        }
    }
    
    @Test
    public void testNoNullElementsArray() {
        Integer[] array = {1, null, 3};
        try {
            Validate.noNullElements(array);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated array contains null element at index: 1", e.getMessage());
        }
    }
    
    @Test
    public void testNoNullElementsCollection() {
        List<String> list = new ArrayList<>();
        list.add("test");
        list.add(null);
        try {
            Validate.noNullElements(list);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated collection contains null element at index: 1", e.getMessage());
        }
    }
    
    @Test
    public void testValidIndexArray() {
        Integer[] array = {1, 2, 3};
        assertEquals(array, Validate.validIndex(array, 1));
        
        try {
            Validate.validIndex(array, 3);
            fail("Expected an IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("The validated array index is invalid: 3", e.getMessage());
        }
    }
    
    @Test
    public void testValidIndexCollection() {
        List<String> list = new ArrayList<>();
        list.add("test");
        list.add("test2");
        assertEquals(list, Validate.validIndex(list, 1));
        
        try {
            Validate.validIndex(list, 2);
            fail("Expected an IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("The validated collection index is invalid: 2", e.getMessage());
        }
    }
    
    @Test
    public void testValidIndexCharSequence() {
        String validString = "test";
        assertEquals(validString, Validate.validIndex(validString, 1));
        
        try {
            Validate.validIndex(validString, 4);
            fail("Expected an IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("The validated character sequence index is invalid: 4", e.getMessage());
        }
    }
    
    @Test
    public void testValidState() {
        Validate.validState(true);
        
        try {
            Validate.validState(false);
            fail("Expected an IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("The validated state is false", e.getMessage());
        }
    }

    @Test
    public void testMatchesPattern() {
        String input = "12345";
        String pattern = "\\d+";
        Validate.matchesPattern(input, pattern);
        
        input = "abcdef";
        try {
            Validate.matchesPattern(input, pattern);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The string abcdef does not match the pattern \\d+", e.getMessage());
        }
    }

    @Test
    public void testInclusiveBetween() {
        long start = 1;
        long end = 5;
        Validate.inclusiveBetween(start, end, 3);
        
        try {
            Validate.inclusiveBetween(start, end, 6);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The value 6 is not in the specified inclusive range of 1 to 5", e.getMessage());
        }
    }
    
    @Test
    public void testExclusiveBetween() {
        long start = 1;
        long end = 5;
        Validate.exclusiveBetween(start, end, 3);
        
        try {
            Validate.exclusiveBetween(start, end, 5);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The value 5 is not in the specified exclusive range of 1 to 5", e.getMessage());
        }
    }
    
    @Test
    public void testIsInstanceOf() {
        Class<?> type = String.class;
        Object obj = "test";
        Validate.isInstanceOf(type, obj);
        
        Object invalidObj = 123;
        try {
            Validate.isInstanceOf(type, invalidObj);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Expected type: class java.lang.String, actual: class java.lang.Integer", e.getMessage());
        }
    }
    
    @Test
    public void testIsAssignableFrom() {
        Class<?> superType = Number.class;
        Class<?> type = Integer.class;
        Validate.isAssignableFrom(superType, type);
        
        Class<?> invalidType = String.class;
        try {
            Validate.isAssignableFrom(superType, invalidType);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot assign a class java.lang.String to a class java.lang.Number", e.getMessage());
        }
    }
}