import static org.junit.Assert.*;
import org.junit.Test;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // Method not used with class name-based resolvers, no test needed
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValue("test");
        assertEquals(String.class.getName(), id);
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValueAndType("test", String.class);
        assertEquals(String.class.getName(), id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        JavaType type = resolver.typeFromId(null, String.class.getName());
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void test_typeFromId_withUnknownType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        JavaType type = resolver.typeFromId(null, "com.example.UnknownType");
        assertNull(type);
    }

    @Test
    public void test_typeFromId_withException() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        try {
            JavaType type = resolver.typeFromId(null, "com.example.TypeWithException");
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void test_typeFromId_withInvalidTypeId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        JavaType type = resolver.typeFromId(null, "");
        assertNull(type);
    }

    @Test
    public void test_typeFromId_withGenericType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        JavaType type = resolver.typeFromId(null, "java.util.List<java.lang.String>");
        assertNotNull(type);
    }

    @Test
    public void test_typeFromId_withEnumSet() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        JavaType type = resolver.typeFromId(null, "java.util.EnumSet<com.example.EnumType>");
        assertNotNull(type);
    }

    @Test
    public void test_typeFromId_withEnumMap() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        JavaType type = resolver.typeFromId(null, "java.util.EnumMap<com.example.EnumType, java.lang.String>");
        assertNotNull(type);
    }

    @Test
    public void test_idFrom_withEnumSet() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        EnumSet<EnumType> enumSet = EnumSet.allOf(EnumType.class);
        String id = resolver.idFromValue(enumSet);
        assertEquals("java.util.EnumSet<com.example.EnumType>", id);
    }

    @Test
    public void test_idFrom_withEnumMap() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        EnumMap<EnumType, String> enumMap = new EnumMap<>(EnumType.class);
        enumMap.put(EnumType.ONE, "one");
        enumMap.put(EnumType.TWO, "two");
        String id = resolver.idFromValue(enumMap);
        assertEquals("java.util.EnumMap<com.example.EnumType, java.lang.Object>", id);
    }

    @Test
    public void test_idFrom_withArraysAsList() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        List<String> list = Arrays.asList("one", "two", "three");
        String id = resolver.idFromValue(list);
        assertEquals("java.util.ArrayList", id);
    }


    @Test
    public void test_idFrom_withNestedClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        OuterClass.InnerClass innerClass = new OuterClass.InnerClass();
        String id = resolver.idFromValue(innerClass);
        assertEquals("com.example.OuterClass$InnerClass", id);
    }
}