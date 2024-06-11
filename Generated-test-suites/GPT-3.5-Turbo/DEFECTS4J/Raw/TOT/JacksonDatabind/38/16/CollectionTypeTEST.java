import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        // Typical use case
        Class<?> rawType = List.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaTypeBuilder.build(String.class);
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(collectionType);

        // Edge case
        rawType = ArrayList.class;
        bindings = null;
        superClass = null;
        superInts = null;
        elemT = JavaTypeBuilder.build(Integer.class);
        collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(collectionType);

        // Error scenario
        rawType = null;
        bindings = null;
        superClass = null;
        superInts = null;
        elemT = JavaTypeBuilder.build(Float.class);
        collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNull(collectionType);
    }

    @Test
    public void test_withContentType() {
        // Typical use case
        Class<?> rawType = Set.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType contentType = JavaTypeBuilder.build(Double.class);
        JavaType elemT = JavaTypeBuilder.build(Boolean.class);
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withContentType(contentType);
        assertNotNull(collectionType);

        // Edge case
        rawType = HashSet.class;
        bindings = null;
        superClass = null;
        superInts = null;
        contentType = JavaTypeBuilder.build(Float.class);
        elemT = JavaTypeBuilder.build(Character.class);
        collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withContentType(contentType);
        assertNotNull(collectionType);

        // Error scenario
        rawType = null;
        bindings = null;
        superClass = null;
        superInts = null;
        contentType = JavaTypeBuilder.build(Integer.class);
        elemT = JavaTypeBuilder.build(String.class);
        collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withContentType(contentType);
        assertNull(collectionType);
    }

    @Test
    public void test_withTypeHandler() {
        // Typical use case
        Class<?> rawType = List.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaTypeBuilder.build(String.class);
        Object typeHandler = new TypeHandler();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withTypeHandler(typeHandler);
        assertNotNull(collectionType);

        // Edge case
        rawType = ArrayList.class;
        bindings = null;
        superClass = null;
        superInts = null;
        elemT = JavaTypeBuilder.build(Integer.class);
        typeHandler = new TypeHandler();
        collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withTypeHandler(typeHandler);
        assertNotNull(collectionType);

        // Error scenario
        rawType = null;
        bindings = null;
        superClass = null;
        superInts = null;
        elemT = JavaTypeBuilder.build(Float.class);
        typeHandler = new TypeHandler();
        collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withTypeHandler(typeHandler);
        assertNull(collectionType);
    }

    @Test
    public void test_withContentTypeHandler() {
        // Typical use case
        Class<?> rawType = Set.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType contentType = JavaTypeBuilder.build(Double.class);
        JavaType elemT = JavaTypeBuilder.build(Boolean.class);
        Object typeHandler = new TypeHandler();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withContentTypeHandler(typeHandler);
        assertNotNull(collectionType);

        // Edge case
        rawType = HashSet.class;
        bindings = null;
        superClass = null;
        superInts = null;
        contentType = JavaTypeBuilder.build(Float.class);
        elemT = JavaTypeBuilder.build(Character.class);
        typeHandler = new TypeHandler();
        collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withContentTypeHandler(typeHandler);
        assertNotNull(collectionType);

        // Error scenario
        rawType = null;
        bindings = null;
        superClass = null;
        superInts = null;
        contentType = JavaTypeBuilder.build(Integer.class);
        elemT = JavaTypeBuilder.build(String.class);
        typeHandler = new TypeHandler();
        collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withContentTypeHandler(typeHandler);
        assertNull(collectionType);
    }

    @Test
    public void test_withValueHandler() {
        // Typical use case
        Class<?> rawType = List.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaTypeBuilder.build(String.class);
        Object valueHandler = new ValueHandler();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withValueHandler(valueHandler);
        assertNotNull(collectionType);

        // Edge case
        rawType = ArrayList.class;
        bindings = null;
        superClass = null;
        superInts = null;
        elemT = JavaTypeBuilder.build(Integer.class);
        valueHandler = new ValueHandler();
        collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withValueHandler(valueHandler);
        assertNotNull(collectionType);

        // Error scenario
        rawType = null;
        bindings = null;
        superClass = null;
        superInts = null;
        elemT = JavaTypeBuilder.build(Float.class);
        valueHandler = new ValueHandler();
        collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withValueHandler(valueHandler);
        assertNull(collectionType);
    }

    @Test
    public void test_withContentValueHandler() {
        // Typical use case
        Class<?> rawType = Set.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType contentType = JavaTypeBuilder.build(Double.class);
        JavaType elemT = JavaTypeBuilder.build(Boolean.class);
        Object valueHandler = new ValueHandler();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withContentValueHandler(valueHandler);
        assertNotNull(collectionType);

        // Edge case
        rawType = HashSet.class;
        bindings = null;
        superClass = null;
        superInts = null;
        contentType = JavaTypeBuilder.build(Float.class);
        elemT = JavaTypeBuilder.build(Character.class);
        valueHandler = new ValueHandler();
        collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withContentValueHandler(valueHandler);
        assertNotNull(collectionType);

        // Error scenario
        rawType = null;
        bindings = null;
        superClass = null;
        superInts = null;
        contentType = JavaTypeBuilder.build(Integer.class);
        elemT = JavaTypeBuilder.build(String.class);
        valueHandler = new ValueHandler();
        collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withContentValueHandler(valueHandler);
        assertNull(collectionType);
    }

    @Test
    public void test_withStaticTyping() {
        // Typical use case
        Class<?> rawType = List.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaTypeBuilder.build(String.class);
        Object typeHandler = new TypeHandler();
        Object valueHandler = new ValueHandler();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withTypeHandler(typeHandler);
        collectionType = collectionType.withValueHandler(valueHandler);
        collectionType = collectionType.withStaticTyping();
        assertNotNull(collectionType);

        // Edge case
        rawType = ArrayList.class;
        bindings = null;
        superClass = null;
        superInts = null;
        elemT = JavaTypeBuilder.build(Integer.class);
        typeHandler = new TypeHandler();
        valueHandler = new ValueHandler();
        collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withTypeHandler(typeHandler);
        collectionType = collectionType.withValueHandler(valueHandler);
        collectionType = collectionType.withStaticTyping();
        assertNotNull(collectionType);

        // Error scenario
        rawType = null;
        bindings = null;
        superClass = null;
        superInts = null;
        elemT = JavaTypeBuilder.build(Float.class);
        typeHandler = new TypeHandler();
        valueHandler = new ValueHandler();
        collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        collectionType = collectionType.withTypeHandler(typeHandler);
        collectionType = collectionType.withValueHandler(valueHandler);
        collectionType = collectionType.withStaticTyping();
        assertNull(collectionType);
    }

    @Test
    public void test_refine() {
        // Typical use case
        Class<?> rawType = List.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaTypeBuilder.build(String.class);
        Class<?> newRawType = ArrayList.class;
        TypeBindings newBindings = null;
        JavaType newSuperClass = null;
        JavaType[] newSuperInterfaces = null;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType refinedType = collectionType.refine(newRawType, newBindings, newSuperClass, newSuperInterfaces);
        assertNotNull(refinedType);

        // Edge case
        rawType = Set.class;
        bindings = null;
        superClass = null;
        superInts = null;
        elemT = JavaTypeBuilder.build(Character.class);
        newRawType = HashSet.class;
        newBindings = null;
        newSuperClass = null;
        newSuperInterfaces = null;
        collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        refinedType = collectionType.refine(newRawType, newBindings, newSuperClass, newSuperInterfaces);
        assertNotNull(refinedType);

        // Error scenario
        rawType = null;
        bindings = null;
        superClass = null;
        superInts = null;
        elemT = JavaTypeBuilder.build(Float.class);
        newRawType = null;
        newBindings = null;
        newSuperClass = null;
        newSuperInterfaces = null;
        collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        refinedType = collectionType.refine(newRawType, newBindings, newSuperClass, newSuperInterfaces);
        assertNull(refinedType);
    }

    private static class TypeHandler {
        // Test implementation
    }

    private static class ValueHandler {
        // Test implementation
    }

}