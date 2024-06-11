import org.junit.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.util.*;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        JsonTypeInfo.Id mechanism = resolver.getMechanism();
        assertEquals(JsonTypeInfo.Id.CLASS, mechanism);
    }

    @Test
    public void testRegisterSubtype() {
        // Since registerSubtype is not used with class name-based resolvers, we cannot test it
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        String id = resolver.idFromValue("value");
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        String id = resolver.idFromValueAndType("value", String.class);
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JavaType javaType = resolver.typeFromId(ctxt, "java.lang.String");
        assertEquals(TypeFactory.defaultInstance().constructType(String.class), javaType);
    }

    @Test
    public void testTypeFromIdWithUnknownId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JavaType javaType = resolver.typeFromId(ctxt, "com.example.UnknownClass");
        assertNull(javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        resolver.typeFromId(ctxt, "InvalidId");
    }
    
    @Test
    public void testTypeFromIdWithGenericType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(HashMap.class), TypeFactory.defaultInstance());
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JavaType javaType = resolver.typeFromId(ctxt, "java.util.HashMap<java.lang.String, java.lang.Integer>");
        assertEquals(TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Integer.class), javaType);
    }

    @Test
    public void test_idFromWithEnumSet() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(EnumSet.class), TypeFactory.defaultInstance());
        Set<EnumType> enumSet = EnumSet.of(EnumType.VALUE1, EnumType.VALUE2);
        String id = resolver.idFromValue(enumSet);
        assertEquals("java.util.EnumSet<com.example.EnumType>", id);
    }

    @Test
    public void test_idFromWithEnumMap() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(EnumMap.class), TypeFactory.defaultInstance());
        Map<EnumType, String> enumMap = new EnumMap<>(EnumType.class);
        enumMap.put(EnumType.VALUE1, "value1");
        enumMap.put(EnumType.VALUE2, "value2");
        String id = resolver.idFromValue(enumMap);
        assertEquals("java.util.EnumMap<com.example.EnumType, java.lang.String>", id);
    }

    @Test
    public void test_idFromWithArrayList() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(ArrayList.class), TypeFactory.defaultInstance());
        List<String> list = new ArrayList<>();
        list.add("value1");
        list.add("value2");
        String id = resolver.idFromValue(list);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void test_idFromWithOuterClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(OuterClass.InnerClass.class), TypeFactory.defaultInstance());
        OuterClass.InnerClass innerClass = new OuterClass.InnerClass();
        String id = resolver.idFromValue(innerClass);
        assertEquals("com.example.OuterClass$InnerClass", id);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        String desc = resolver.getDescForKnownTypeIds();
        assertEquals("class name used as type id", desc);
    }
    
    // Edge cases and exception handling scenarios that should be tested:
    // - Passing null value to idFromValue and idFromValueAndType methods
    // - Passing null id to typeFromId method
    // - DeserializationContext.handleUnknownTypeId being called with null input
    
    // ...

}

