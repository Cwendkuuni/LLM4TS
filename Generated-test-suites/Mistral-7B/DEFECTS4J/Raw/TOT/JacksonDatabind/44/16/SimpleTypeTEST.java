import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        // Test constructor with a Class
        JavaType simpleType1 = TypeFactory.defaultInstance().construct(String.class);
        assertEquals("SimpleType for String", simpleType1.getClass().getName(), SimpleType.class.getName());

        // Test constructor with Class, TypeBindings, JavaType superClass, and JavaType[] superInterfaces
        JavaType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(simpleType1, simpleType2);

        // Test copy constructor
        JavaType simpleType3 = new SimpleType(simpleType1);
        assertEquals(simpleType1, simpleType3);

        // Test constructor with a Class, TypeBindings, JavaType superClass, JavaType[] superInterfaces, Object valueHandler, Object typeHandler, and boolean asStatic
        JavaType simpleType4 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertEquals(simpleType1, simpleType4);

        // Test pass-through constructor used by ReferenceType
        JavaType simpleType5 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, 0, null, false);
        assertEquals(simpleType1, simpleType5);

        // Test constructor that should not be used by application code (constructUnsafe)
        JavaType simpleType6 = SimpleType.constructUnsafe(String.class);
        assertEquals(simpleType1, simpleType6);

        // Test deprecated constructor that should not be used by application code (construct)
        JavaType simpleType7 = SimpleType.construct(String.class);
        assertEquals(simpleType1, simpleType7);
    }

    @Test
    public void testNarrowMethod() {
        // Test with the same class
        JavaType simpleType1 = TypeFactory.defaultInstance().construct(String.class);
        JavaType simpleType2 = simpleType1.narrow(String.class);
        assertEquals(simpleType1, simpleType2);

        // Test with a subclass
        JavaType subType = TypeFactory.defaultInstance().construct(Substring.class);
        JavaType narrowedSubType = simpleType1.narrow(Substring.class);
        assertEquals(subType, narrowedSubType);

        // Test with a superclass
        JavaType superType = TypeFactory.defaultInstance().construct(Object.class);
        JavaType narrowedSuperType = simpleType1.narrow(Object.class);
        assertEquals(simpleType1, narrowedSuperType);

        // Test with a different class
        JavaType differentType = TypeFactory.defaultInstance().construct(Integer.class);
        JavaType narrowedDifferentType = simpleType1.narrow(Integer.class);
        assertEquals(simpleType1, narrowedDifferentType);
    }

    @Test
    public void testIsContainerTypeMethod() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        assertEquals(false, simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignatureMethod() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        StringBuilder expectedSignature = new StringBuilder("java.lang.String");
        StringBuilder actualSignature = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testGetGenericSignatureMethod() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        StringBuilder expectedSignature = new StringBuilder("java.lang.String");
        StringBuilder actualSignature = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expectedSignature.toString(), actualSignature.toString());

        JavaType listType = TypeFactory.defaultInstance().construct(List.class);
        StringBuilder listExpectedSignature = new StringBuilder("java.util.List");
        StringBuilder listActualSignature = listType.getGenericSignature(new StringBuilder());
        assertEquals(listExpectedSignature.toString(), listActualSignature.toString());

        JavaType listOfStringType = TypeFactory.defaultInstance().construct(List.class, String.class);
        StringBuilder listOfStringExpectedSignature = new StringBuilder("java.util.List<java.lang.String>");
        StringBuilder listOfStringActualSignature = listOfStringType.getGenericSignature(new StringBuilder());
        assertEquals(listOfStringExpectedSignature.toString(), listOfStringActualSignature.toString());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);

        // Test with null valueHandler
        JavaType withContentTypeHandlerNull = simpleType.withContentTypeHandler(null);
        assertEquals(simpleType, withContentTypeHandlerNull);

        // Test with an Object valueHandler
        Object valueHandler = new Object() {};
        JavaType withContentTypeHandlerObject = simpleType.withContentTypeHandler(valueHandler);
        assertNotEquals(simpleType, withContentTypeHandlerObject);
    }

    @Test
    public void testWithValueHandlerMethod() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);

        // Test with null valueHandler
        JavaType withValueHandlerNull = simpleType.withValueHandler(null);
        assertEquals(simpleType, withValueHandlerNull);

        // Test with an Object valueHandler
        Object valueHandler = new Object() {};
        JavaType withValueHandlerObject = simpleType.withValueHandler(valueHandler);
        assertNotEquals(simpleType, withValueHandlerObject);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);

        // Test with null typeHandler
        JavaType withTypeHandlerNull = simpleType.withTypeHandler(null);
        assertEquals(simpleType, withTypeHandlerNull);

        // Test with an Object typeHandler
        Object typeHandler = new Object() {};
        JavaType withTypeHandlerObject = simpleType.withTypeHandler(typeHandler);
        assertNotEquals(simpleType, withTypeHandlerObject);
    }

    @Test
    public void testWithStaticTypingMethod() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        JavaType withStaticTyping = simpleType.withStaticTyping();
        assertEquals(false, withStaticTyping._asStatic);

        JavaType staticSimpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, true);
        JavaType withStaticTypingStatic = simpleType.withStaticTyping();
        assertEquals(staticSimpleType, withStaticTypingStatic);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeMethod() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        simpleType.withContentType(TypeFactory.defaultInstance().construct(Integer.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerMethod() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        simpleType.withContentTypeHandler(new Object() {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandlerMethod() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        simpleType.withValueHandler(new Object() {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandlerMethod() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        simpleType.withContentValueHandler(new Object() {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithStaticTypingMethodWithStaticType() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        simpleType.withStaticTyping();
        simpleType.withStaticTyping();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefineMethodWithInvalidClass() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        simpleType.refine(Integer.class, TypeBindings.emptyBindings(), null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testRefineMethodWithNullTypeBindings() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        simpleType.refine(String.class, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testRefineMethodWithNullSuperClass() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testRefineMethodWithNullSuperInterfaces() {
        JavaType simpleType = TypeFactory.defaultInstance().construct(String.class);
        simpleType.refine(String.class, TypeBindings.emptyBindings(), null, (JavaType[]) null);
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        JavaType simpleType1 = TypeFactory.defaultInstance().construct(String.class);
        assertEquals("java.lang.String", simpleType1.buildCanonicalName());

        JavaType listType = TypeFactory.defaultInstance().construct(List.class, String.class);
        assertEquals("java.util.List<java.lang.String>", listType.buildCanonicalName());
    }
}