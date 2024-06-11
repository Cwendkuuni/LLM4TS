import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = null;  // TODO: set rawType value
        TypeBindings bindings = null;  // TODO: set bindings value
        JavaType superClass = null;  // TODO: set superClass value
        JavaType[] superInts = null;  // TODO: set superInts value
        JavaType elemT = null;  // TODO: set elemT value
        
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        // TODO: Add asserts to test the functionality of the method
    }
    
    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        
        String expected = "[collection type; class null, contains null]";
        String actual = collectionType.toString();
        
        assertEquals(expected, actual);
    }
}