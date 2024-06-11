import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ClassIdType;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeDef;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.lang.reflect.Type;

public class CollectionTypeTest {

    @Test
    public void testCollectionTypeConstructor() {
        Class<List> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new TypeReference<ArrayList>() {}.getType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new TypeReference<Integer>() {}.getType();
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getTypeBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertEquals(false, collectionType.isStatic());
    }
}