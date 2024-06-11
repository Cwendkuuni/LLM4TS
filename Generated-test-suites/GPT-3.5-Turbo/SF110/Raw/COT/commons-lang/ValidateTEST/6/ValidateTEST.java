
import static org.junit.Assert.*;
import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        boolean expression = true;
        String message = "Expression is false";
        Validate.isTrue(expression, message);
    }
    
    @Test
    public void testIsNull() {
        Object object = null;
        Object result = Validate.notNull(object);
        assertNull(result);
    }
    
    @Test
    public void testNotEmptyArray() {
        Integer[] array = {1, 2, 3};
        Integer[] result = Validate.notEmpty(array);
        assertNotNull(result);
    }
    
    @Test
    public void testNotEmptyCollection() {
        List<String> collection = new ArrayList<String>();
        collection.add("one");
        collection.add("two");
        collection.add("three");
        List<String> result = Validate.notEmpty(collection);
        assertNotNull(result);
    }
    
    @Test
    public void testNotEmptyMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        Map<String, String> result = Validate.notEmpty(map);
        assertNotNull(result);
    }
    
    @Test
    public void testNotEmptyCharSequence() {
        String chars = "example";
        String result = Validate.notEmpty(chars);
        assertNotNull(result);
    }
    
    @Test
    public void testNotBlank() {
        String chars = "example";
        String result = Validate.notBlank(chars);
        assertNotNull(result);
    }
    
    @Test
    public void testNoNullElementsArray() {
        Integer[] array = {1, null, 3};
        Integer[] result = Validate.noNullElements(array);
        assertNull(result);
    }
    
    @Test
    public void testNoNullElementsCollection() {
        List<Integer> collection = new ArrayList<Integer>();
        collection.add(1);
        collection.add(null);
        collection.add(3);
        List<Integer> result = Validate.noNullElements(collection);
        assertNull(result);
    }
    
    @Test
    public void testValidIndexArray() {
        Integer[] array = {1, 2, 3};
        int index = 1;
        Integer[] result = Validate.validIndex(array, index);
        assertNotNull(result);
    }
    
    @Test
    public void testValidIndexCollection() {
        List<Integer> collection = new ArrayList<Integer>();
        collection.add(1);
        collection.add(2);
        collection.add(3);
        int index = 1;
        List<Integer> result = Validate.validIndex(collection, index);
        assertNotNull(result);
    }
    
    @Test
    public void testValidIndexCharSequence() {
        String chars = "example";
        int index = 1;
        String result = Validate.validIndex(chars, index);
        assertNotNull(result);
    }
    
    @Test
    public void testValidState() {
        boolean expression = true;
        Validate.validState(expression);
    }
    
    @Test
    public void testMatchesPattern() {
        String input = "example";
        String pattern = "[a-z]+";
        Validate.matchesPattern(input, pattern);
    }
    
    @Test
    public void testInclusiveBetween() {
        int start = 1;
        int end = 10;
        int value = 5;
        Validate.inclusiveBetween(start, end, value);
    }
    
    @Test
    public void testExclusiveBetween() {
        int start = 1;
        int end = 10;
        int value = 5;
        Validate.exclusiveBetween(start, end, value);
    }
    
    @Test
    public void testIsInstanceOf() {
        Class<?> type = Integer.class;
        Object obj = 5;
        Validate.isInstanceOf(type, obj);
    }
    
    @Test
    public void testIsAssignableFrom() {
        Class<?> superType = Number.class;
        Class<?> type = Integer.class;
        Validate.isAssignableFrom(superType, type);
    }
}
