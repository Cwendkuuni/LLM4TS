import org.junit.Test;

import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        assertNotNull(obj.toString());
    }

    @Test
    public void testEqualsWithDifferentObject() {
        IFXObject obj1 = new IFXObject();
        Object obj2 = new Object();
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testEqualsWithNull() {
        IFXObject obj = new IFXObject();
        assertFalse(obj.equals(null));
    }

    @Test
    public void testDoCheckEqualsWithDifferentTypes() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testStringifyWithNestedObjectsAndArrays() {
        IFXObject obj = new IFXObject();
        IFXObject[] array = {obj};
        obj.toString();
    }

    @Test
    public void testTabWithDifferentLevels() {
        IFXObject obj = new IFXObject();
        assertEquals("  ", obj.tab(1));
        assertEquals("    ", obj.tab(2));
    }
}