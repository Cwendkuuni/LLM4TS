import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {
    
    @Test
    public void testEqualWithEqualObjects() {
        Object a = new Object();
        Object b = new Object();
        assertTrue(Objects.equal(a, a));
        assertTrue(Objects.equal(b, b));
    }
    
    @Test
    public void testEqualWithNullObjects() {
        Object a = null;
        Object b = null;
        assertTrue(Objects.equal(a, a));
        assertTrue(Objects.equal(b, b));
    }
    
    @Test
    public void testEqualWithDifferentObjects() {
        Object a = new Object();
        Object b = new Object();
        assertFalse(Objects.equal(a, b));
    }
    
    @Test
    public void testEqualWithOneNullObject() {
        Object a = new Object();
        Object b = null;
        assertFalse(Objects.equal(a, b));
        assertFalse(Objects.equal(b, a));
    }
    
    @Test
    public void testHashCodeWithNullObjects() {
        Object[] objects = null;
        assertEquals(0, Objects.hashCode(objects));
    }
    
    @Test
    public void testHashCodeWithEmptyArray() {
        Object[] objects = new Object[0];
        assertEquals(1, Objects.hashCode(objects));
    }
    
    @Test
    public void testHashCodeWithOneNullObject() {
        Object[] objects = { null };
        assertEquals(0, Objects.hashCode(objects));
    }
    
    @Test
    public void testHashCodeWithMultipleObjects() {
        Object a = new Object();
        Object b = new Object();
        Object[] objects = { a, b };
        assertEquals(Objects.hashCode(a, b), Objects.hashCode(objects));
    }
    
    @Test
    public void testToStringHelperWithObject() {
        Object obj = new Object();
        Objects.ToStringHelper helper = Objects.toStringHelper(obj);
        assertNotNull(helper);
        assertEquals(obj.getClass().getSimpleName(), helper.toString());
    }
    
    @Test
    public void testToStringHelperWithClass() {
        Class<?> clazz = ObjectsTest.class;
        Objects.ToStringHelper helper = Objects.toStringHelper(clazz);
        assertNotNull(helper);
        assertEquals(clazz.getSimpleName(), helper.toString());
    }
    
    @Test
    public void testToStringHelperWithClassName() {
        String className = "ObjectsTest";
        Objects.ToStringHelper helper = Objects.toStringHelper(className);
        assertNotNull(helper);
        assertEquals(className, helper.toString());
    }
    
    @Test
    public void testFirstNonNullWithFirstNonNull() {
        Object a = new Object();
        Object b = new Object();
        assertEquals(a, Objects.firstNonNull(a, b));
    }
    
    @Test
    public void testFirstNonNullWithSecondNonNull() {
        Object a = null;
        Object b = new Object();
        assertEquals(b, Objects.firstNonNull(a, b));
    }
    
    @Test
    public void testFirstNonNullWithBothNull() {
        Object a = null;
        Object b = null;
        assertNull(Objects.firstNonNull(a, b));
    }
    
    @Test
    public void testFirstNonNullWithDifferentTypes() {
        Integer a = null;
        String b = "test";
        assertEquals(b, Objects.firstNonNull(a, b));
    }
    
    @Test
    public void testToStringHelperOmitNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass").omitNullValues();
        assertNotNull(helper);
        helper.add("value", null);
        assertEquals("TestClass{}", helper.toString());
    }
    
    @Test
    public void testToStringHelperAddWithNameAndValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        assertNotNull(helper);
        helper.add("name", "value");
        assertEquals("TestClass{name=value}", helper.toString());
    }
    
    @Test
    public void testToStringHelperAddWithValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        assertNotNull(helper);
        helper.addValue("value");
        assertEquals("TestClass{value}", helper.toString());
    }
    
    @Test
    public void testToStringHelperAddMultipleValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        assertNotNull(helper);
        helper.add("name1", "value1").add("name2", "value2");
        assertEquals("TestClass{name1=value1, name2=value2}", helper.toString());
    }
}