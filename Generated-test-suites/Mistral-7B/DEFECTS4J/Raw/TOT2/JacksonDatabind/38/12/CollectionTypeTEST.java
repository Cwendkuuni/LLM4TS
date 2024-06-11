import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;

public class CollectionTypeTest {
    @Test
    public void testConstructor() {
        Class<?> collectionTypeClass = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = {};
        JavaType elemType = JavaType.construct(String.class);
        CollectionType collectionType = new CollectionType(collectionTypeClass, bindings, superClass, superInts, elemType, null, null, false);
        assertNotNull(collectionType);
    }
}