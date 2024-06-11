import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test equal objects
        assertTrue(Objects.equal("hello", "hello"));
        
        // Test unequal objects
        assertFalse(Objects.equal("hello", "world"));
        
        // Test null object
        assertTrue(Objects.equal(null, null));
        
        // Test one null object
        assertFalse(Objects.equal(null, "hello"));
        assertFalse(Objects.equal("hello", null));
    }

    @Test
    public void testHashCode() {
        // Test null array
        assertEquals(0, Objects.hashCode((Object[]) null));
        
        // Test empty array
        assertEquals(1, Objects.hashCode());
        
        // Test array with values
        assertEquals(123456, Objects.hashCode(123456));
        assertEquals(123, Objects.hashCode(1, 2, 3));
    }

    @Test
    public void testToStringHelper() {
        // Test toStringHelper with object
        assertEquals("TestClass{}", Objects.toStringHelper(new TestClass()).toString());
        
        // Test toStringHelper with class
        assertEquals("TestClass{}", Objects.toStringHelper(TestClass.class).toString());
        
        // Test toStringHelper with string
        assertEquals("TestClass{}", Objects.toStringHelper("TestClass").toString());
    }

    @Test
    public void testFirstNonNull() {
        // Test firstNonNull with both non-null objects
        assertEquals("hello", Objects.firstNonNull("hello", "world"));
        
        // Test firstNonNull with first null object
        assertEquals("world", Objects.firstNonNull(null, "world"));
        
        // Test firstNonNull with second null object
        assertEquals("hello", Objects.firstNonNull("hello", null));
        
        // Test firstNonNull with both null objects
        assertEquals(null, Objects.firstNonNull(null, null));
    }
    
    @Test
    public void testToStringHelperOmitNullValues() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("TestClass");
        
        // Test toStringHelper omitNullValues with non-null value
        toStringHelper.add("name", "John");
        assertEquals("TestClass{name=John}", toStringHelper.toString());
        
        // Test toStringHelper omitNullValues with null value
        toStringHelper.add("age", null);
        assertEquals("TestClass{name=John}", toStringHelper.toString());
        
        // Test toStringHelper omitNullValues() method
        toStringHelper.omitNullValues();
        assertEquals("TestClass{name=John}", toStringHelper.toString());
    }
    
    @Test
    public void testToStringHelperAdd() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("TestClass");
        
        // Test toStringHelper add() method with String value
        toStringHelper.add("name", "John");
        assertEquals("TestClass{name=John}", toStringHelper.toString());
        
        // Test toStringHelper add() method with boolean value
        toStringHelper.add("married", true);
        assertEquals("TestClass{name=John, married=true}", toStringHelper.toString());
        
        // Test toStringHelper add() method with char value
        toStringHelper.add("gender", 'M');
        assertEquals("TestClass{name=John, married=true, gender=M}", toStringHelper.toString());
        
        // Test toStringHelper add() method with double value
        toStringHelper.add("salary", 1000.50);
        assertEquals("TestClass{name=John, married=true, gender=M, salary=1000.5}", toStringHelper.toString());
        
        // Test toStringHelper add() method with float value
        toStringHelper.add("height", 175.5f);
        assertEquals("TestClass{name=John, married=true, gender=M, salary=1000.5, height=175.5}", toStringHelper.toString());
        
        // Test toStringHelper add() method with int value
        toStringHelper.add("age", 30);
        assertEquals("TestClass{name=John, married=true, gender=M, salary=1000.5, height=175.5, age=30}", toStringHelper.toString());
        
        // Test toStringHelper add() method with long value
        toStringHelper.add("count", 1000000L);
        assertEquals("TestClass{name=John, married=true, gender=M, salary=1000.5, height=175.5, age=30, count=1000000}", toStringHelper.toString());
    }
    
    @Test
    public void testToStringHelperAddValue() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("TestClass");
        
        // Test toStringHelper addValue() method with non-null value
        toStringHelper.addValue("John");
        assertEquals("TestClass{John}", toStringHelper.toString());
        
        // Test toStringHelper addValue() method with null value
        toStringHelper.addValue(null);
        assertEquals("TestClass{John}", toStringHelper.toString());
        
        // Test toStringHelper addValue() method with boolean value
        toStringHelper.addValue(true);
        assertEquals("TestClass{John, true}", toStringHelper.toString());
        
        // Test toStringHelper addValue() method with char value
        toStringHelper.addValue('M');
        assertEquals("TestClass{John, true, M}", toStringHelper.toString());
        
        // Test toStringHelper addValue() method with double value
        toStringHelper.addValue(1000.50);
        assertEquals("TestClass{John, true, M, 1000.5}", toStringHelper.toString());
        
        // Test toStringHelper addValue() method with float value
        toStringHelper.addValue(175.5f);
        assertEquals("TestClass{John, true, M, 1000.5, 175.5}", toStringHelper.toString());
        
        // Test toStringHelper addValue() method with int value
        toStringHelper.addValue(30);
        assertEquals("TestClass{John, true, M, 1000.5, 175.5, 30}", toStringHelper.toString());
        
        // Test toStringHelper addValue() method with long value
        toStringHelper.addValue(1000000L);
        assertEquals("TestClass{John, true, M, 1000.5, 175.5, 30, 1000000}", toStringHelper.toString());
    }

}