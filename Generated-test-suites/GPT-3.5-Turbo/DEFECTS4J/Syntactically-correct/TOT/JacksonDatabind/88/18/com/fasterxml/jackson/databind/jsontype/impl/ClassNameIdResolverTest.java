import static org.junit.Assert.*;
import org.junit.Test;

public class ClassNameIdResolverTest {
    
    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }
    
    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        // Not used with class name - based resolvers, testing with a class and a name
        resolver.registerSubtype(String.class, "String");
        // No assertions made as the registerSubtype method does not modify any state
    }
    
    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertNotNull(id);
        assertTrue(id.length() > 0);
    }
    
    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        Object value = new Object();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        assertNotNull(id);
        assertTrue(id.length() > 0);
    }
    
    @Test
    public void testTypeFromId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        String id = "java.lang.String";
        try {
            JavaType javaType = resolver.typeFromId(id);
            assertNotNull(javaType);
            assertEquals(String.class, javaType.getRawClass());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testTypeFromId_unknownId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        String id = "com.example.Unknown";
        try {
            JavaType javaType = resolver.typeFromId(id);
            assertNull(javaType);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testTypeFromId_invalidId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        String id = "invalid_id";
        try {
            JavaType javaType = resolver.typeFromId(id);
            assertNull(javaType);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testTypeFromId_exceptionThrown() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        String id = "java.lang.Exception";
        try {
            JavaType javaType = resolver.typeFromId(id);
            assertNull(javaType);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testTypeFromId_deserializationContextAvailable() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        String id = "com.example.Unknown";
        DatabindContext context = new DeserializationContext();
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            assertNull(javaType);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testTypeFromId_handleUnknownTypeIdReturnsNull() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        String id = "com.example.Unknown";
        DatabindContext context = new DeserializationContext();
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            assertNull(javaType);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testTypeFromId_handleUnknownTypeIdReturnsJavaType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        String id = "com.example.Unknown";
        DatabindContext context = new DeserializationContext();
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            assertNotNull(javaType);
            assertEquals(_baseType, javaType.getBaseType());
            assertEquals(id, javaType.getId());
            assertEquals(resolver, javaType.getIdResolver());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void test_typeFromId_specializedType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        String id = "java.util.List<java.lang.String>";
        try {
            JavaType javaType = resolver.typeFromId(id);
            assertNotNull(javaType);
            assertEquals(List.class, javaType.getRawClass());
            assertEquals(_baseType, javaType.getBaseType());
            assertEquals(id, javaType.getId());
            assertEquals(resolver, javaType.getIdResolver());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void test_typeFromId_enumSet() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        String id = "java.util.EnumSet<java.lang.String>";
        try {
            JavaType javaType = resolver.typeFromId(id);
            assertNotNull(javaType);
            assertEquals(EnumSet.class, javaType.getRawClass());
            assertEquals(_baseType, javaType.getBaseType());
            assertEquals(id, javaType.getId());
            assertEquals(resolver, javaType.getIdResolver());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void test_typeFromId_enumMap() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        String id = "java.util.EnumMap<java.lang.String,java.lang.Integer>";
        try {
            JavaType javaType = resolver.typeFromId(id);
            assertNotNull(javaType);
            assertEquals(EnumMap.class, javaType.getRawClass());
            assertEquals(_baseType, javaType.getBaseType());
            assertEquals(id, javaType.getId());
            assertEquals(resolver, javaType.getIdResolver());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void test_typeFromId_arraysAsList() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        String id = "java.util.Arrays$ArrayList";
        try {
            JavaType javaType = resolver.typeFromId(id);
            assertNotNull(javaType);
            assertEquals(ArrayList.class, javaType.getRawClass());
            assertEquals(_baseType, javaType.getBaseType());
            assertEquals(id, javaType.getId());
            assertEquals(resolver, javaType.getIdResolver());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void test_typeFromId_innerClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        String id = "com.example.OuterClass$InnerClass";
        try {
            JavaType javaType = resolver.typeFromId(id);
            assertNotNull(javaType);
            assertEquals(InnerClass.class, javaType.getRawClass());
            assertEquals(_baseType, javaType.getBaseType());
            assertEquals(id, javaType.getId());
            assertEquals(resolver, javaType.getIdResolver());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void test_idFrom() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        Object value = new Object();
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = new TypeFactory();
        String id = resolver._idFrom(value, cls, typeFactory);
        assertNotNull(id);
        assertTrue(id.length() > 0);
    }
    
    @Test
    public void test_getDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver();
        String desc = resolver.getDescForKnownTypeIds();
        assertNotNull(desc);
        assertTrue(desc.length() > 0);
    }
}