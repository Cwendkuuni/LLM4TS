
import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {
    // Test matchProperty method
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("pointer", "property", null);
        assertNull(pointer.matchProperty("differentProperty"));
        assertNotNull(pointer.matchProperty("property"));
    }

    // Test matchElement method
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("pointer", "", null);
        assertNull(pointer.matchElement(1));
        assertNotNull(pointer.matchElement(0));
    }

    // Test tail method
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("pointer", "", new JsonPointer());
        assertNotNull(pointer.tail());
    }

    // Test toString method
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("pointer", "", null);
        assertEquals("pointer", pointer.toString());
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("", "", null);
        JsonPointer pointer2 = new JsonPointer("", "", null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());

        JsonPointer pointer3 = new JsonPointer("pointer", "", null);
        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
    }

    // Test equals method
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("", "", null);
        JsonPointer pointer2 = new JsonPointer("", "", null);
        assertTrue(pointer1.equals(pointer2));

        JsonPointer pointer3 = new JsonPointer("pointer", "", null);
        assertFalse(pointer1.equals(pointer3));
    }

    // Test compile method
    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/property");
        assertNotNull(pointer);
        assertEquals("/property", pointer.toString());
    }

    // Test valueOf method
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/property");
        assertNotNull(pointer);
        assertEquals("/property", pointer.toString());
    }
}
