package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private Object testValue;
    private ClassNameIdResolver resolver;
    private TypeFactory typeFactory;

    @org.junit.Before
    public void setUp() throws Exception {
        testValue = new Object();
        typeFactory = ObjectMapper.getDefaultInstance().getTypeFactory();
        resolver = new ClassNameIdResolver(JsonType.STANDARD_INSTANCE.findJavaType(Object.class), typeFactory);
    }

    @Test
    public void testIdFromValue() throws IOException {
        assertEquals("java.lang.Object", resolver.idFromValue(testValue));
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        Class<?> stringClass = String.class;
        assertEquals("java.lang.String", resolver.idFromValueAndType(testValue, stringClass));
    }

    @Test
    public void testTypeFromId() throws IOException {
        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(null, id);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testIdFromValue_Enum() throws IOException {
        Enum e = Enum.valueOf(String.class, "VALUE");
        assertEquals("java.lang.String", resolver.idFromValue(e));
    }

    @Test
    public void testIdFromValue_EnumSubClass() throws IOException {
        Class<? extends Enum> enumClass = Enum.class.getPackage().getName().replaceAll("\\.", "").replaceAll("^", "java.") + ".MyEnum";
        Enum e = Enum.valueOf(Class.forName(enumClass), "VALUE");
        assertEquals("java.lang.Enum", resolver.idFromValue(e));
    }

    @Test
    public void testIdFromValue_List() throws IOException {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertEquals("java.util.ArrayList", resolver.idFromValue(list));
    }

    @Test
    public void testIdFromValue_Map() throws IOException {
        Map<String, Integer> map = new HashMap<>();
        map.put("foo", 1);
        map.put("bar", 2);
        assertEquals("java.util.HashMap", resolver.idFromValue(map));
    }

    @Test
    public void testIdFromValue_Array() throws IOException {
        Object[] array = new Object[]{1, 2, 3};
        assertEquals("[]", resolver.idFromValue(array));
    }

    @Test
    public void testIdFromValue_CustomClass() throws IOException {
        Class<CustomClass> customClass = CustomClass.class;
        CustomClass customValue = new CustomClass();
        assertEquals(customClass.getName(), resolver.idFromValue(customValue));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIdFromValue_InvalidTypeId() throws IOException {
        resolver.idFromValue("invalidTypeId");
    }

    @Test
    public void testIdFromValue_Null() throws IOException {
        assertNull(resolver.idFromValue(null));
    }
}

class CustomClass {
    // empty class
}