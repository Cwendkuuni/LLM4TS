import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.core.type.TypeReference;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals("Expected class 'String' and referenced type 'int[]'", cls, referenceType.getRawClass());
        assertEquals("Expected referenced type 'int[]'", refType, referenceType.getContentType());
    }

    @Test
    public void testTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Object newTypeHandler = new Object() {};
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);

        assertNotSame("Expected different type handlers", typeHandler, newTypeHandler);
        assertEquals("Expected referenced type with new type handler 'int[]'", refType, newReferenceType.getContentType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        JavaType newReferencedType = TypeFactory.defaultInstance().constructType(long[].class);
        Object newContentTypeHandler = new Object() {};
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentTypeHandler);

        Object oldContentTypeHandler = refType.<Object>getTypeHandler();
        assertNotSame("Expected different content type handlers", oldContentTypeHandler, newContentTypeHandler);
        assertEquals("Expected referenced type with new content type handler 'long[]'", newReferencedType, newReferenceType.getContentType());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Object newValueHandler = new Object() {};
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);

        assertNotSame("Expected different value handlers", valueHandler, newValueHandler);
        assertEquals("Expected referenced type with new value handler 'int[]'", refType, newReferenceType.getContentType());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        JavaType intArrayType = TypeFactory.defaultInstance().constructType(int[].class);
        Object newContentValueHandler = intArrayType.getValueHandler();
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);

        Object oldContentValueHandler = refType.<Object>getValueHandler();
        assertNotSame("Expected different content value handlers", oldContentValueHandler, newContentValueHandler);
        assertEquals("Expected referenced type with new content value handler 'int[]'", intArrayType, newReferenceType.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType newReferenceType = referenceType.withStaticTyping();

        assertTrue("Expected static typing", newReferenceType.isStatic());
        assertFalse("Expected not a reference type", newReferenceType.isReferenceType());
    }

    @Test
    public void testNarrowing() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Class<Integer> intClass = Integer.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        ReferenceType intReferenceType = new ReferenceType(intClass, intType, valueHandler, typeHandler, false);

        JavaType narrowedType = referenceType.narrow(intClass);

        assertEquals("Expected narrowed type 'int[]'", intType, narrowedType);
    }

    @Test
    public void testGetReferencedType() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        JavaType expectedReferencedType = refType;
        JavaType actualReferencedType = referenceType.getReferencedType();

        assertEquals("Expected referenced type 'int[]'", expectedReferencedType, actualReferencedType);
    }

    @Test
    public void testIsReferenceType() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertTrue("Expected reference type", referenceType.isReferenceType());
    }
}