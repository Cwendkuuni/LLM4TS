import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.*;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver resolver;

    @Before
    public void setUp() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        resolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
    }

    @Test
    public void testGetMechanism() {
        JsonTypeInfo.Id mechanism = resolver.getMechanism();
        assertEquals(JsonTypeInfo.Id.CLASS, mechanism);
    }

    @Test
    public void testRegisterSubtype() {
        Class<?> type = Object.class;
        String name = "object";
        resolver.registerSubtype(type, name);
        // No assertions, just testing that the method does not throw an exception
    }

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        String id = resolver.idFromValue(value);
        String expectedId = "java.lang.Object";
        assertEquals(expectedId, id);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        String expectedId = "java.lang.Object";
        assertEquals(expectedId, id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext context = mapper.getDeserializationContext();
        String id = "java.lang.Object";
        JavaType javaType = resolver.typeFromId(context, id);
        JavaType expectedType = TypeFactory.defaultInstance().constructType(Object.class);
        assertEquals(expectedType, javaType);
    }

    @Test
    public void testTypeFromIdWithUnknownId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext context = mapper.getDeserializationContext();
        String id = "com.example.UnknownClass";
        JavaType javaType = resolver.typeFromId(context, id);
        assertNull(javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext context = mapper.getDeserializationContext();
        String id = "InvalidClassId";
        resolver.typeFromId(context, id);
    }

    @Test
    public void testTypeFromIdWithUnknownIdAndDeserializationContext() throws IOException {
        DeserializationContext context = new DummyDeserializationContext();
        String id = "com.example.UnknownClass";
        JavaType javaType = resolver.typeFromId(context, id);
        assertNull(javaType);
    }

    @Test
    public void testTypeFromIdWithUnknownIdAndDeserializationContextWithHandler() throws IOException {
        DeserializationContext context = new DummyDeserializationContextWithHandler();
        String id = "com.example.UnknownClass";
        JavaType javaType = resolver.typeFromId(context, id);
        assertNotNull(javaType);
        JavaType expectedType = TypeFactory.defaultInstance().constructType(Object.class);
        assertEquals(expectedType, javaType);
    }

    @Test
    public void test_typeFromId_with_generics_info() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext context = mapper.getDeserializationContext();
        String id = "java.util.List<java.lang.String>";
        JavaType javaType = resolver.typeFromId(context, id);
        JavaType expectedType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        assertEquals(expectedType, javaType);
    }

    @Test
    public void test_idFrom_with_EnumSet() {
        EnumSet<SampleEnum> enumSet = EnumSet.allOf(SampleEnum.class);
        String id = resolver.idFromValue(enumSet);
        String expectedId = "java.util.EnumSet<java.util.HashSet<com.example.SampleEnum>>";
        assertEquals(expectedId, id);
    }

    @Test
    public void test_idFrom_with_EnumMap() {
        EnumMap<SampleEnum, String> enumMap = new EnumMap<>(SampleEnum.class);
        String id = resolver.idFromValue(enumMap);
        String expectedId = "java.util.EnumMap<com.example.SampleEnum,java.lang.Object>";
        assertEquals(expectedId, id);
    }

    @Test
    public void test_idFrom_with_inner_class() {
        InnerClass inner = new InnerClass();
        String id = resolver.idFromValue(inner);
        String expectedId = "com.example.OuterClass.InnerClass";
        assertEquals(expectedId, id);
    }

    @Test
    public void test_idFrom_with_non_static_inner_class() {
        NonStaticInnerClass inner = new NonStaticInnerClass();
        String id = resolver.idFromValue(inner);
        String expectedId = "com.example.OuterClass$NonStaticInnerClass";
        assertEquals(expectedId, id);
    }

    private enum SampleEnum {
        VALUE1,
        VALUE2
    }

    private class InnerClass {}

    private class NonStaticInnerClass {}
}