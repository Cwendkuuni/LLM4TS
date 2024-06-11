import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {
    @Test
    public void testProtectedConstructor() {
        Class<List> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new ClassType(ArrayList.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = new ClassType(Integer.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemT, valueHandler, typeHandler, asStatic);
        assertTrue(collectionType instanceof CollectionType);
    }
}