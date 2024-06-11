import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class SimpleTypeTest {
    
    private SimpleType simpleType;

    @Before
    public void setUp() {
        simpleType = new SimpleType(TestClass.class);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(TestClass.class, simpleType.getJavaClass());
        assertFalse(simpleType.isArrayType());
        assertFalse(simpleType.isCollectionLikeType());
        assertFalse(simpleType.isContainerType());
        assertFalse(simpleType.isMapLikeType());
        assertEquals("SimpleType", simpleType.toCanonical());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType2 = new SimpleType(TestClass.class);
        assertEquals(simpleType, simpleType2);
        assertEquals(simpleType.hashCode(), simpleType2.hashCode());
        
        SimpleType simpleType3 = new SimpleType(OtherTestClass.class);
        assertNotEquals(simpleType, simpleType3);
        assertNotEquals(simpleType.hashCode(), simpleType3.hashCode());
    }
    
    @Test
    public void testToString() {
        String expected = "[simple type, class " + TestClass.class.getCanonicalName() + "]";
        assertEquals(expected, simpleType.toString());
    }

    private static class TestClass {}

    private static class OtherTestClass {}
}