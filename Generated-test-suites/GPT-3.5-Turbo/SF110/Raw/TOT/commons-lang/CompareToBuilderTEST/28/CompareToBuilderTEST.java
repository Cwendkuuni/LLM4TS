import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare_SameObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        int expected = 0;
        int actual = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReflectionCompare_NullObject() {
        Object obj1 = null;
        Object obj2 = new Object();
        try {
            CompareToBuilder.reflectionCompare(obj1, obj2);
            fail("NullPointerException not thrown.");
        } catch (NullPointerException e) {
            assertEquals("NullPointerException thrown", e.getMessage());
        }
    }
    
    @Test
    public void testReflectionCompare_DifferentTypeObjects() {
        Object obj1 = new String("test");
        Object obj2 = new Integer(10);
        try {
            CompareToBuilder.reflectionCompare(obj1, obj2);
            fail("ClassCastException not thrown.");
        } catch (ClassCastException e) {
            assertEquals("ClassCastException thrown", e.getMessage());
        }
    }
    
    @Test
    public void testReflectionCompare_WithExcludedFields() {
        ObjectWithFields obj1 = new ObjectWithFields();
        obj1.setField1("value1");
        obj1.setField2("value2");
        ObjectWithFields obj2 = new ObjectWithFields();
        obj2.setField1("value1");
        obj2.setField2("another value");
        
        int expected = 0;
        int actual = CompareToBuilder.reflectionCompare(obj1, obj2, "field2");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReflectionCompare_UseTransients() {
        ObjectWithFields obj1 = new ObjectWithFields();
        obj1.setField1("value1");
        obj1.setField2("value2");
        ObjectWithFields obj2 = new ObjectWithFields();
        obj2.setField1("value1");
        obj2.setField2("value2");
        obj2.setTransientField("transient value");
        
        int expected = 0;
        int actual = CompareToBuilder.reflectionCompare(obj1, obj2, true, null, new String[0]);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppendSuper_NotZero() {
        int superCompareTo = -1;
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(superCompareTo);
        
        int expected = -1;
        int actual = builder.toComparison();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppendSuper_Zero() {
        int superCompareTo = 0;
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(superCompareTo);
        
        int expected = 0;
        int actual = builder.toComparison();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_Object_Null() {
        Object obj1 = null;
        Object obj2 = "value";
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(obj1, obj2);
        
        int expected = -1;
        int actual = builder.toComparison();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppend_Object_NotNull() {
        Object obj1 = "value1";
        Object obj2 = "value2";
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(obj1, obj2);
        
        int expected = "value1".compareTo("value2");
        int actual = builder.toComparison();
        assertEquals(expected, actual);
    }
    
    // ... add more test methods for each method in the CompareToBuilder class ...

}