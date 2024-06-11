package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final TypeFactory typeFactory = mapper.getTypeFactory();

    @Test
    public void testIdFromValue() throws IOException {
        JavaType baseType = typeFactory.constructType(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        Object value = Arrays.asList("foo", "bar").toArray();
        String id = resolver.idFromValue(value);
        String expected = "java.util.ArrayList";
        assertEquals(expected, id);

        value = Arrays.asList(new Enum[] { Enum.valueOf("Enum1", "Value1"), Enum.valueOf("Enum2", "Value2") }).toArray();
        EnumSet enumSet = EnumSet.allOf(Enum.class);
        id = resolver.idFromValue(enumSet);
        expected = typeFactory.constructCollectionType(EnumSet.class, Enum.class).toCanonical();
        assertEquals(expected, id);

        value = new EnumMap<>(Enum.class) {
            {
                put(Enum.valueOf("Enum1", "Value1"), "Value1");
                put(Enum.valueOf("Enum2", "Value2"), "Value2");
            }
        };
        id = resolver.idFromValue(value);
        expected = typeFactory.constructMapType(EnumMap.class, Enum.class, Object.class).toCanonical();
        assertEquals(expected, id);
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        JavaType baseType = typeFactory.constructType(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        Object value = Arrays.asList("foo", "bar").toArray();
        Class<?> valueType = value.getClass();
        String id = resolver.idFromValueAndType(value, valueType);
        String expected = "java.util.ArrayList";
        assertEquals(expected, id);

        Enum<?> enumValue = Enum.valueOf("Enum1", "Value1");
        ValueType enumValueType = new ValueType(); // Assume a custom value type
        id = resolver.idFromValueAndType(enumValue, enumValueType.getClass());
        expected = typeFactory.constructType(Enum.class).getName().replace('.', '/');
        assertEquals(expected, id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        JavaType baseType = typeFactory.constructType(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        String id = "java.util.ArrayList";
        JavaType expectedType = typeFactory.constructCollectionType(ArrayList.class, String.class);
        JavaType type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(expectedType, type);

        id = "java.util.EnumSet";
        expectedType = typeFactory.constructCollectionType(EnumSet.class, Enum.class);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(expectedType, type);

        id = "java.util.EnumMap";
        expectedType = typeFactory.constructMapType(EnumMap.class, Enum.class, Object.class);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(expectedType, type);

        id = "java.util.Arrays$ArrayList";
        expectedType = typeFactory.constructType(ArrayList.class);
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(expectedType, type);
    }

    @Test
    public void testIdFromValueWithCustomTypes() throws IOException {
        JavaType baseType = typeFactory.constructType(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        Object value = new CustomEnum("CustomEnumValue");
        Class<?> valueType = value.getClass();
        String id = resolver.idFromValueAndType(value, valueType);
        Class<?> expectedType = CustomEnum.class;
        assertEquals(expectedType.getName(), id);

        value = new CustomClass(1);
        id = resolver.idFromValue(value);
        expectedType = CustomClass.class;
        assertEquals(expectedType.getName(), id);
    }

    static class CustomEnum extends Enum {
        CustomEnum(String name) { super(name); }
    }

    static class CustomClass {
        int id;

        CustomClass(int id) { this.id = id; }
    }

}