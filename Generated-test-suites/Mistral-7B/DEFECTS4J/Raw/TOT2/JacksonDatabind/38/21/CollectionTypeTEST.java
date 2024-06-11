import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstructorWithMultipleArguments() {
        Class<?> collT = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = ArrayList.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = Integer.class;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);

        assertEquals("collection type; class " + collT.getName() + ", contains " + elemT, collectionType.toString());
    }
}