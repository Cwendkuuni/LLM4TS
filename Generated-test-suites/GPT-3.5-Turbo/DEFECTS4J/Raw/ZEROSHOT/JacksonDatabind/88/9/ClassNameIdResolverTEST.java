package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import org.junit.Assert;

public class ClassNameIdResolverTest {
    private ClassNameIdResolver resolver;
    
    @Before
    public void setup() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        resolver = new ClassNameIdResolver(baseType, typeFactory);
    }
    
    @Test
    public void testGetMechanism() {
        Assert.assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }
    
    @Test
    public void testRegisterSubtype() {
        Class<?> type = ClassNameIdResolverTest.class;
        String name = "ClassNameIdResolverTest";
        resolver.registerSubtype(type, name);
    }
    
    @Test
    public void testIdFromValue() {
        Object value = new Object();
        String id = resolver.idFromValue(value);
        Assert.assertNotNull(id);
        Assert.assertTrue(id.contains(value.getClass().getName()));
    }
    
    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        Assert.assertNotNull(id);
        Assert.assertTrue(id.contains(value.getClass().getName()));
    }
    
    @Test
    public void testTypeFromId() {
        String id = "java.lang.Object";
        DatabindContext context = new DeserializationContext(null, null);
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            Assert.assertNotNull(javaType);
            Assert.assertEquals(Object.class, javaType.getRawClass());
        } catch (IOException e) {
            Assert.fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testTypeIdWithGenerics() {
        String id = "java.util.List<java.lang.String>";
        DatabindContext context = new DeserializationContext(null, null);
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            Assert.assertNotNull(javaType);
            Assert.assertTrue(javaType.isCollectionLikeType());
            Assert.assertEquals(List.class, javaType.getRawClass());
            Assert.assertEquals(String.class, javaType.getContentType().getRawClass());
        } catch (IOException e) {
            Assert.fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testTypeIdNotFound() {
        String id = "com.example.Foo";
        DatabindContext context = new DeserializationContext(null, null);
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().contains("no such class found"));
        }
    }
    
    @Test
    public void testTypeIdInvalid() {
        String id = "com.example.<Bar>";
        DatabindContext context = new DeserializationContext(null, null);
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().contains("Invalid type id"));
        }
    }
    
    @Test
    public void testTypeIdInnerClass() {
        String id = "com.example.Outer$Inner";
        DatabindContext context = new DeserializationContext(null, null);
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            Assert.assertNotNull(javaType);
            Assert.assertEquals(Outer.Inner.class, javaType.getRawClass());
        } catch (IOException e) {
            Assert.fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testTypeIdEnumSet() {
        EnumSet<MyEnum> enumSet = EnumSet.allOf(MyEnum.class);
        String id = resolver.idFromValue(enumSet);
        Assert.assertNotNull(id);
        Assert.assertEquals("java.util.EnumSet<com.example.MyEnum>", id);
    }
    
    @Test
    public void testTypeIdEnumMap() {
        EnumMap<MyEnum, String> enumMap = new EnumMap<>(MyEnum.class);
        enumMap.put(MyEnum.VALUE1, "value1");
        enumMap.put(MyEnum.VALUE2, "value2");
        String id = resolver.idFromValue(enumMap);
        Assert.assertNotNull(id);
        Assert.assertEquals("java.util.EnumMap<com.example.MyEnum,java.lang.String>", id);
    }
    
    @Test
    public void testTypeIdArraysAsList() {
        List<String> list = Arrays.asList("value1", "value2");
        String id = resolver.idFromValue(list);
        Assert.assertNotNull(id);
        Assert.assertEquals("java.util.ArrayList", id);
    }
    
  
    @Test
    public void testGetDescForKnownTypeIds() {
        Assert.assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
}