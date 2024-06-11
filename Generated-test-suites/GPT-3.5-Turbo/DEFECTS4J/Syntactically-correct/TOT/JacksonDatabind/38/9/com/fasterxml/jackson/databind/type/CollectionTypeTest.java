import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        // typical use case
        CollectionType collectionType1 = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));

        assertNotNull(collectionType1);
        assertEquals(ArrayList.class, collectionType1.getRawClass());
        assertNull(collectionType1.getSuperClass());
        assertNull(collectionType1.getSuperInterfaces());
        assertEquals(SimpleType.construct(String.class), collectionType1.getContentType());
        assertNull(collectionType1.getValueHandler());
        assertNull(collectionType1.getTypeHandler());
        assertFalse(collectionType1.isStaticTyping());

        // edge case with empty type bindings
        CollectionType collectionType2 = CollectionType.construct(LinkedList.class, TypeBindings.emptyBindings(),
                SimpleType.construct(Object.class), null, SimpleType.construct(Integer.class));

        assertNotNull(collectionType2);
        assertEquals(LinkedList.class, collectionType2.getRawClass());
        assertEquals(SimpleType.construct(Object.class), collectionType2.getSuperClass());
        assertNull(collectionType2.getSuperInterfaces());
        assertEquals(SimpleType.construct(Integer.class), collectionType2.getContentType());
        assertNull(collectionType2.getValueHandler());
        assertNull(collectionType2.getTypeHandler());
        assertFalse(collectionType2.isStaticTyping());

        // error scenario with null raw type
        try {
            CollectionType collectionType3 = CollectionType.construct(null, TypeBindings.emptyBindings(),
                    null, null, SimpleType.construct(Boolean.class));
            fail("Expected NullPointerException was not thrown");
        } catch (NullPointerException e) {
            // expected
        }

        // error scenario with null element type
        try {
            CollectionType collectionType4 = CollectionType.construct(HashSet.class, TypeBindings.emptyBindings(),
                    null, null, null);
            fail("Expected NullPointerException was not thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testConstruct_old() {
        // typical use case
        CollectionType collectionType1 = CollectionType.construct(ArrayList.class, SimpleType.construct(String.class));

        assertNotNull(collectionType1);
        assertEquals(ArrayList.class, collectionType1.getRawClass());
        assertNull(collectionType1.getSuperClass());
        assertNull(collectionType1.getSuperInterfaces());
        assertEquals(SimpleType.construct(String.class), collectionType1.getContentType());
        assertNull(collectionType1.getValueHandler());
        assertNull(collectionType1.getTypeHandler());
        assertFalse(collectionType1.isStaticTyping());

        // edge case with null type bindings
        try {
            CollectionType collectionType2 = CollectionType.construct(LinkedList.class, null);
            fail("Expected NullPointerException was not thrown");
        } catch (NullPointerException e) {
            // expected
        }

        // error scenario with null raw type
        try {
            CollectionType collectionType3 = CollectionType.construct(null, SimpleType.construct(Object.class));
            fail("Expected NullPointerException was not thrown");
        } catch (NullPointerException e) {
            // expected
        }

        // error scenario with null element type
        try {
            CollectionType collectionType4 = CollectionType.construct(HashSet.class, null);
            fail("Expected NullPointerException was not thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testNarrow() {
        CollectionType collectionType1 = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));

        // typical use case
        JavaType narrowedType1 = collectionType1.narrow(Vector.class);

        assertNotNull(narrowedType1);
        assertEquals(Vector.class, narrowedType1.getRawClass());
        assertNull(narrowedType1.getSuperClass());
        assertNull(narrowedType1.getSuperInterfaces());
        assertEquals(SimpleType.construct(String.class), narrowedType1.getContentType());
        assertNull(narrowedType1.getValueHandler());
        assertNull(narrowedType1.getTypeHandler());
        assertFalse(narrowedType1.isStaticTyping());

        // edge case with empty type bindings
        CollectionType collectionType2 = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                SimpleType.construct(Object.class), null, SimpleType.construct(Integer.class));

        JavaType narrowedType2 = collectionType2.narrow(LinkedList.class);

        assertNotNull(narrowedType2);
        assertEquals(LinkedList.class, narrowedType2.getRawClass());
        assertEquals(SimpleType.construct(Object.class), narrowedType2.getSuperClass());
        assertNull(narrowedType2.getSuperInterfaces());
        assertEquals(SimpleType.construct(Integer.class), narrowedType2.getContentType());
        assertNull(narrowedType2.getValueHandler());
        assertNull(narrowedType2.getTypeHandler());
        assertFalse(narrowedType2.isStaticTyping());
    }

    @Test
    public void testWithContentType() {
        CollectionType collectionType1 = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));

        // typical use case
        JavaType modifiedType1 = collectionType1.withContentType(SimpleType.construct(Boolean.class));

        assertNotNull(modifiedType1);
        assertEquals(ArrayList.class, modifiedType1.getRawClass());
        assertNull(modifiedType1.getSuperClass());
        assertNull(modifiedType1.getSuperInterfaces());
        assertEquals(SimpleType.construct(Boolean.class), modifiedType1.getContentType());
        assertNull(modifiedType1.getValueHandler());
        assertNull(modifiedType1.getTypeHandler());
        assertFalse(modifiedType1.isStaticTyping());

        // edge case with identical content type
        JavaType modifiedType2 = collectionType1.withContentType(SimpleType.construct(String.class));

        assertEquals(collectionType1, modifiedType2);
    }

    @Test
    public void testWithTypeHandler() {
        CollectionType collectionType1 = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));

        // typical use case
        CollectionType modifiedType1 = collectionType1.withTypeHandler(new CustomTypeHandler());

        assertNotNull(modifiedType1);
        assertEquals(ArrayList.class, modifiedType1.getRawClass());
        assertNull(modifiedType1.getSuperClass());
        assertNull(modifiedType1.getSuperInterfaces());
        assertEquals(SimpleType.construct(String.class), modifiedType1.getContentType());
        assertNull(modifiedType1.getValueHandler());
        assertNotNull(modifiedType1.getTypeHandler());
        assertFalse(modifiedType1.isStaticTyping());

        // edge case with null type handler
        CollectionType modifiedType2 = collectionType1.withTypeHandler(null);

        assertNull(modifiedType2.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionType collectionType1 = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));

        // typical use case
        CollectionType modifiedType1 = collectionType1.withContentTypeHandler(new CustomTypeHandler());

        assertNotNull(modifiedType1);
        assertEquals(ArrayList.class, modifiedType1.getRawClass());
        assertNull(modifiedType1.getSuperClass());
        assertNull(modifiedType1.getSuperInterfaces());
        assertEquals(SimpleType.construct(String.class), modifiedType1.getContentType());
        assertNull(modifiedType1.getValueHandler());
        assertNull(modifiedType1.getTypeHandler());
        assertFalse(modifiedType1.isStaticTyping());

        // edge case with null content type handler
        CollectionType modifiedType2 = collectionType1.withContentTypeHandler(null);

        assertNull(modifiedType2.getContentTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType1 = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));

        // typical use case
        CollectionType modifiedType1 = collectionType1.withStaticTyping();

        assertNotNull(modifiedType1);
        assertEquals(ArrayList.class, modifiedType1.getRawClass());
        assertNull(modifiedType1.getSuperClass());
        assertNull(modifiedType1.getSuperInterfaces());
        assertEquals(SimpleType.construct(String.class), modifiedType1.getContentType());
        assertNull(modifiedType1.getValueHandler());
        assertNull(modifiedType1.getTypeHandler());
        assertTrue(modifiedType1.isStaticTyping());

        // edge case with already static typing
        CollectionType modifiedType2 = modifiedType1.withStaticTyping();

        assertEquals(modifiedType1, modifiedType2);
    }

    @Test
    public void testRefine() {
        CollectionType collectionType1 = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));

        // typical use case
        JavaType refinedType1 = collectionType1.refine(Vector.class, TypeBindings.emptyBindings(),
                null, null);

        assertNotNull(refinedType1);
        assertEquals(Vector.class, refinedType1.getRawClass());
        assertNull(refinedType1.getSuperClass());
        assertNull(refinedType1.getSuperInterfaces());
        assertEquals(SimpleType.construct(String.class), refinedType1.getContentType());
        assertNull(refinedType1.getValueHandler());
        assertNull(refinedType1.getTypeHandler());
        assertFalse(refinedType1.isStaticTyping());

        // edge case with empty type bindings
        JavaType refinedType2 = collectionType1.refine(LinkedList.class, TypeBindings.emptyBindings(),
                SimpleType.construct(Object.class), null);

        assertNotNull(refinedType2);
        assertEquals(LinkedList.class, refinedType2.getRawClass());
        assertEquals(SimpleType.construct(Object.class), refinedType2.getSuperClass());
        assertNull(refinedType2.getSuperInterfaces());
        assertEquals(SimpleType.construct(String.class), refinedType2.getContentType());
        assertNull(refinedType2.getValueHandler());
        assertNull(refinedType2.getTypeHandler());
        assertFalse(refinedType2.isStaticTyping());
    }

    @Test
    public void testToString() {
        CollectionType collectionType1 = CollectionType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));

        String string1 = collectionType1.toString();

        assertNotNull(string1);
        assertTrue(string1.contains("ArrayList"));
        assertTrue(string1.contains("String"));

        CollectionType collectionType2 = CollectionType.construct(LinkedList.class, TypeBindings.emptyBindings(),
                SimpleType.construct(Object.class), null, SimpleType.construct(Boolean.class));

        String string2 = collectionType2.toString();

        assertNotNull(string2);
        assertTrue(string2.contains("LinkedList"));
        assertTrue(string2.contains("Boolean"));
    }
}