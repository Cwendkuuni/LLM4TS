import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        resolver.registerSubtype(String.class, "String");
    }
    
    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValue("test");
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValueAndType("test", String.class);
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testTypeFromId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        JavaType type = resolver.typeFromId(null, "java.lang.String");
        // TODO: check if the JavaType is correct
    }

    @Test
    public void test_typeFromId_withUnknownId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        JavaType type = resolver._typeFromId("unknownId", context);
        assertEquals(null, type);
    }

    @Test
    public void test_typeFromId_withClassNotFoundException() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        JavaType type = resolver._typeFromId("com.example.UnknownClass", context);
        assertEquals(null, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_typeFromId_withInvalidTypeId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        resolver._typeFromId("invalid", context);
    }

    @Test
    public void test_idFrom_and__typeFromMethods_withEnumSubtype() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id1 = resolver._idFrom(TestEnum.VALUE1, TestEnum.VALUE1.getClass(), TypeFactory.defaultInstance());
        assertEquals("com.example.TestEnum", id1);

        String id2 = resolver._idFrom(TestEnum.VALUE2, TestEnum.VALUE2.getClass(), TypeFactory.defaultInstance());
        assertEquals("com.example.TestEnum", id2);

        DatabindContext context = new DeserializationContext(null, null, null);
        JavaType type1 = resolver._typeFromId(id1, context);
        assertEquals(TestEnum.class, type1.getRawClass());

        JavaType type2 = resolver._typeFromId(id2, context);
        assertEquals(TestEnum.class, type2.getRawClass());
    }

    @Test
    public void test_idFromMethod_withEnumSet() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        EnumSet<TestEnum> enumSet = EnumSet.of(TestEnum.VALUE1, TestEnum.VALUE2);
        String id = resolver.idFromValue(enumSet);
        assertEquals("java.util.EnumSet", id);
    }

    @Test
    public void test_idFromMethod_withEnumMap() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        EnumMap<TestEnum, String> enumMap = new EnumMap<>(TestEnum.class);
        enumMap.put(TestEnum.VALUE1, "test");
        String id = resolver.idFromValue(enumMap);
        assertEquals("java.util.EnumMap", id);
    }

    @Test
    public void test_idFromMethod_withArrayListFromArraysAsList() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        List<String> list = Arrays.asList("test");
        String id = resolver.idFromValue(list);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void test_idFromMethod_withArrayListFromCollections() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        List<String> list = Collections.singletonList("test");
        String id = resolver.idFromValue(list);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void test_idFromMethod_withCustomInnerClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        InnerClass innerClass = new InnerClass();
        String id = resolver.idFromValue(innerClass);
        assertEquals("com.example.OuterClass", id);
    }

    @Test
    public void test_getDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String desc = resolver.getDescForKnownTypeIds();
        assertEquals("class name used as type id", desc);
    }

    private enum TestEnum {
        VALUE1, VALUE2
    }

    private static class InnerClass {
    }
}