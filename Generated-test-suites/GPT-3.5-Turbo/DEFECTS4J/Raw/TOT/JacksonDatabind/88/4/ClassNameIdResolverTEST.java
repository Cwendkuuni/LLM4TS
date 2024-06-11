import static org.junit.Assert.*;
import org.junit.*;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // not used in class name based resolvers, so no test case
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object(); // test case: generic object
        assertEquals("java.lang.Object", resolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object(); // test case: generic object
        Class<?> type = value.getClass();
        assertEquals("java.lang.Object", resolver.idFromValueAndType(value, type));
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = "java.lang.Object"; // test case: generic object
        // mock a DatabindContext
        DatabindContext context = Mockito.mock(DatabindContext.class);
        // mock a TypeFactory
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        // construct a JavaType to be returned
        JavaType javaType = new JavaType(null, null);
        Mockito.when(typeFactory.constructFromCanonical(id)).thenReturn(javaType);
        JavaType result = resolver.typeFromId(context, id);
        assertEquals(javaType, result);
    }

    @Test(expected = IOException.class)
    public void testTypeFromId_IOException() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = "java.lang.Object"; // test case: generic object
        // mock a DatabindContext
        DatabindContext context = Mockito.mock(DatabindContext.class);
        Mockito.when(context.getTypeFactory()).thenThrow(new IOException());
        resolver.typeFromId(context, id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromId_IllegalArgumentException() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = "java.lang.Object"; // test case: generic object
        // mock a DatabindContext
        DatabindContext context = Mockito.mock(DatabindContext.class);
        // mock a TypeFactory
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        Mockito.when(context.getTypeFactory()).thenReturn(typeFactory);
        Mockito.when(typeFactory.constructFromCanonical(id)).thenThrow(new IllegalArgumentException());
        resolver.typeFromId(context, id);
    }

    @Test
    public void testTypeFromId_ClassNotFoundException() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = "java.lang.Object"; // test case: generic object
        // mock a DatabindContext
        DatabindContext context = Mockito.mock(DatabindContext.class);
        // mock a TypeFactory
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        Mockito.when(context.getTypeFactory()).thenReturn(typeFactory);
        Class<?> cls = Object.class;
        Mockito.when(typeFactory.findClass(id)).thenThrow(new ClassNotFoundException());
        // mock a DeserializationContext
        DeserializationContext dctxt = Mockito.mock(DeserializationContext.class);
        Mockito.when(context instanceof DeserializationContext).thenReturn(true);
        Mockito.when((DeserializationContext) context).thenReturn(dctxt);
        // mock handleUnknownTypeId method
        JavaType baseType = new JavaType(null, null);
        ClassNameIdResolver.TestIdResolver resolverInstance = new ClassNameIdResolver.TestIdResolver(baseType, id);
        String errorMessage = "no such class found";
        Mockito.when(dctxt.handleUnknownTypeId(baseType, id, resolverInstance, errorMessage)).thenReturn(null);
        JavaType result = resolverInstance._typeFromId(id, context);
        assertNull(result);
    }

    @Test
    public void testTypeFromId_Exception() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = "java.lang.Object"; // test case: generic object
        // mock a DatabindContext
        DatabindContext context = Mockito.mock(DatabindContext.class);
        // mock a TypeFactory
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        Mockito.when(context.getTypeFactory()).thenReturn(typeFactory);
        Class<?> cls = Object.class;
        Mockito.when(typeFactory.findClass(id)).thenThrow(new Exception());
        JavaType result = resolver._typeFromId(id, context);
        assertNull(result);
    }

    @Test
    public void test_idFrom() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object(); // test case: generic object
        Class<?> cls = value.getClass();
        // test case: object is not enum, so name is returned as is
        assertEquals("java.lang.Object", resolver._idFrom(value, cls, null));
    }

    @Test
    public void test_idFrom_enum() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Enum<?> value = TestEnum.ONE; // test case: enum object
        Class<?> cls = value.getClass();
        // test case: enum object, so name of superclass is returned
        assertEquals("TestEnum", resolver._idFrom(value, cls, null));
    }

    @Test
    public void test_idFrom_enumSubclass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Enum<?> value = TestEnumSub.ONE; // test case: enum subclass object
        Class<?> cls = value.getClass();
        // test case: enum subclass object, so name of superclass (enum) is returned
        assertEquals("TestEnum", resolver._idFrom(value, cls, null));
    }

    @Test
    public void test_idFrom_javaUtil() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        ArrayList<Integer> value = new ArrayList<>(); // test case: java.util.ArrayList object
        Class<?> cls = value.getClass();
        // test case: java.util.ArrayList, so converted to ArrayList
        assertEquals("ArrayList", resolver._idFrom(value, cls, null));
    }

    @Test
    public void test_idFrom_innerClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        InnerClass.InnerStaticClass value = new InnerClass.InnerStaticClass(); // test case: inner class object
        Class<?> cls = value.getClass();
        // test case: inner class object, so same class name is returned
        assertEquals("InnerClass$InnerStaticClass", resolver._idFrom(value, cls, null));
    }

    @Test
    public void test_getDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
}

enum TestEnum {
    ONE, TWO, THREE
}

enum TestEnumSub {
    ONE, TWO, THREE;

    static class InnerStaticClass {
    }
}