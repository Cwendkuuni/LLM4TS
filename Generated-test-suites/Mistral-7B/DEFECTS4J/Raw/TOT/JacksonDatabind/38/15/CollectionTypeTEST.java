import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = CollectionType.construct(listClass, bindings, null, null, JavaType.construct(Integer.class));

        assertEquals("Expected CollectionType instance", CollectionType.class, javaType.getClass());
        assertEquals("Expected List type", listClass, javaType.getRawClass());
        assertEquals("Expected Integer element type", Integer.class, javaType.getContentType().getRawClass());
    }

    @Test
    public void testConstructDeprecated() {
        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        JavaType elemType = JavaType.construct(Integer.class);

        JavaType javaType = CollectionType.construct(listClass, elemType);

        assertEquals("Expected CollectionType instance", CollectionType.class, javaType.getClass());
        assertEquals("Expected List type", listClass, javaType.getRawClass());
        assertEquals("Expected Integer element type", elemType, javaType.getContentType());
    }

    @Test
    public void testRefine() {
        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(ArrayList.class);
        JavaType[] superInterfaces = new JavaType[] { JavaType.construct(Serializable.class) };
        JavaType elemType = JavaType.construct(Integer.class);

        JavaType javaType = CollectionType.refine(listClass, bindings, superClass, superInterfaces);

        assertEquals("Expected CollectionType instance", CollectionType.class, javaType.getClass());
        assertEquals("Expected List type", listClass, javaType.getRawClass());
        assertEquals("Expected Integer element type", elemType, javaType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMissingBindings() {
        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        JavaType elemType = JavaType.construct(Integer.class);

        try {
            JavaType javaType = CollectionType.construct(listClass, null, null, null, elemType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMissingSuperInterfaces() {
        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(ArrayList.class);
        JavaType elemType = JavaType.construct(Integer.class);

        try {
            JavaType javaType = CollectionType.construct(listClass, bindings, null, null, elemType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMissingRawType() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(ArrayList.class);
        JavaType[] superInterfaces = new JavaType[] { JavaType.construct(Serializable.class) };
        JavaType elemType = JavaType.construct(Integer.class);

        try {
            JavaType javaType = CollectionType.construct(null, bindings, superClass, superInterfaces, elemType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidRawType() {
        Class<?> invalidRawType = new Object() {}.getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(ArrayList.class);
        JavaType[] superInterfaces = new JavaType[] { JavaType.construct(Serializable.class) };
        JavaType elemType = JavaType.construct(Integer.class);

        try {
            JavaType javaType = CollectionType.construct(invalidRawType, bindings, superClass, superInterfaces, elemType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidContentType() {
        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(ArrayList.class);
        JavaType[] superInterfaces = new JavaType[] { JavaType.construct(Serializable.class) };
        JavaType invalidContentType = JavaType.construct(Object.class);

        try {
            JavaType javaType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, invalidContentType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMissingElemType() {
        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(ArrayList.class);
        JavaType[] superInterfaces = new JavaType[] { JavaType.construct(Serializable.class) };

        try {
            JavaType javaType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNarrow() {
        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        JavaType javaType = CollectionType.construct(listClass, null, null, null, JavaType.construct(Integer.class));

        assertEquals("Expected CollectionType instance", javaType, CollectionType.class.cast(javaType._narrow(ArrayList.class)));
    }

    @Test
    public void testNarrowInvalidType() {
        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        JavaType javaType = CollectionType.construct(listClass, null, null, null, JavaType.construct(String.class));

        assertNull("Expected null, invalid type", javaType._narrow(ArrayList.class));
    }
}