package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.test.BaseMapTest;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdSubtypeResolverTest extends BaseMapTest {

    private ObjectMapper mapper;
    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setup() {
        MapperFactory factory = new MapperFactory();
        mapper = factory.getMapper();
        subtypeResolver = mapper.getPolymorphicTypeValidator().getSubtypeResolverInstance();
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() throws Exception {
        Class<?> baseType = BaseClass.class;
        Class<?> subtype1 = Subtype1.class;
        Class<?> subtype2 = Subtype2.class;

        // Register subtypes by class (serialization)
        subtypeResolver.registerSubtypes(subtype1, subtype2);

        // Test with a list of base types
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, baseType);
        List<BaseClass> list = mapper.readValue(mapper.writeValueAsString(Arrays.asList(
                new Subtype1(), new Subtype2())), javaType);

        assertNotNull(list);
        assertEquals(2, list.size());

        // Check that both subtypes are present in the list
        for (BaseClass element : list) {
            if (element instanceof Subtype1) {
                assertInstanceOf(Subtype1.class, element);
            } else if (element instanceof Subtype2) {
                assertInstanceOf(Subtype2.class, element);
            }
        }

        // Test with a single base type
        BaseClass baseInstance = new BaseClass();
        JavaType baseTypeJavaType = mapper.getTypeFactory().constructType(baseType);
        BaseClass deserialized = mapper.readValue(mapper.writeValueAsString(baseInstance), baseTypeJavaType);

        assertInstanceOf(BaseClass.class, deserialized);

        // Test with a Map where key is base type and value is a list of subtypes
        Map<BaseClass, List<BaseClass>> map = new LinkedHashMap<>();
        map.put(baseInstance, list);
        JavaType mapType = mapper.getTypeFactory().constructType(Map.class, baseTypeJavaType, new TypeReference<List<BaseClass>>() {}.getType());
        map = mapper.readValue(mapper.writeValueAsString(map), mapType);

        assertNotNull(map);
        assertEquals(1, map.size());
        assertEquals(list, map.get(baseInstance));

        // Test with a Map where key is a subtype and value is a list of base types
        Map<Subtype1, List<BaseClass>> reverseMap = new LinkedHashMap<>();
        reverseMap.put(new Subtype1(), list);
        JavaType reverseMapType = mapper.getTypeFactory().constructType(Map.class, subtype1.getType(), new TypeReference<List<BaseClass>>() {}.getType());
        reverseMap = mapper.readValue(mapper.writeValueAsString(reverseMap), reverseMapType);

        assertNotNull(reverseMap);
        assertEquals(1, reverseMap.size());
        assertEquals(list, reverseMap.get(new Subtype1()));

        // Test with an empty Map
        Map<BaseClass, List<BaseClass>> emptyMap = new HashMap<>();
        JavaType emptyMapType = mapper.getTypeFactory().constructType(Map.class, baseTypeJavaType, new TypeReference<List<BaseClass>>() {}.getType());
        map = mapper.readValue(mapper.writeValueAsString(emptyMap), emptyMapType);

        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() throws Exception {
        Class<?> baseType = BaseClass.class;
        Class<?> subtype1 = Subtype1.class;
        Class<?> subtype2 = Subtype2.class;

        // Register subtypes by type id (serialization)
        subtypeResolver.registerSubtypes(new TypeIdResolverBase.TypeIdResolver() {
            @Override
            public String idFromValueAndType(Object value, JavaType type) {
                if (value instanceof BaseClass) {
                    return BaseClass.class.getName();
                } else if (value instanceof Subtype1) {
                    return Subtype1.class.getName();
                } else if (value instanceof Subtype2) {
                    return Subtype2.class.getName();
                }
                return null;
            }

            @Override
            public JavaType typeFromId(String id) {
                if (BaseClass.class.getName().equals(id)) {
                    return mapper.getTypeFactory().constructType(baseType);
                } else if (Subtype1.class.getName().equals(id)) {
                    return mapper.getTypeFactory().constructType(subtype1);
                } else if (Subtype2.class.getName().equals(id)) {
                    return mapper.getTypeFactory().constructType(subtype2);
                }
                return null;
            }
        });

        // Test with a list of base types
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, baseType);
        List<BaseClass> list = mapper.readValue(mapper.writeValueAsString(Arrays.asList(
                new Subtype1(), new Subtype2())), javaType);

        assertNotNull(list);
        assertEquals(2, list.size());

        // Check that both subtypes are present in the list
        for (BaseClass element : list) {
            if (element instanceof Subtype1) {
                assertInstanceOf(Subtype1.class, element);
            } else if (element instanceof Subtype2) {
                assertInstanceOf(Subtype2.class, element);
            }
        }

        // Test with a Map where key is base type and value is a list of subtypes
        Map<BaseClass, List<BaseClass>> map = new LinkedHashMap<>();
        map.put(new BaseClass(), list);
        JavaType mapType = mapper.getTypeFactory().constructType(Map.class, baseType, new TypeReference<List<BaseClass>>() {}.getType());
        map = mapper.readValue(mapper.writeValueAsString(map), mapType);

        assertNotNull(map);
        assertEquals(1, map.size());
        assertEquals(list, map.get(new BaseClass()));

        // Test with a Map where key is a subtype and value is a list of base types
        Map<Subtype1, List<BaseClass>> reverseMap = new LinkedHashMap<>();
        reverseMap.put(new Subtype1(), list);
        JavaType reverseMapType = mapper.getTypeFactory().constructType(Map.class, subtype1, new TypeReference<List<BaseClass>>() {}.getType());
        reverseMap = mapper.readValue(mapper.writeValueAsString(reverseMap), reverseMapType);

        assertNotNull(reverseMap);
        assertEquals(1, reverseMap.size());
        assertEquals(list, reverseMap.get(new Subtype1()));

        // Test with an empty Map
        Map<BaseClass, List<BaseClass>> emptyMap = new HashMap<>();
        JavaType emptyMapType = mapper.getTypeFactory().constructType(Map.class, baseType, new TypeReference<List<BaseClass>>() {}.getType());
        map = mapper.readValue(mapper.writeValueAsString(emptyMap), emptyMapType);

        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassDeserialization() throws Exception {
        // Test with a list of base types
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, BaseClass.class);
        List<BaseClass> list = mapper.readValue(mapper.writeValueAsString(Collections.singletonList(new Subtype1())), javaType);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertInstanceOf(Subtype1.class, list.get(0));

        // Test with a Map where key is base type and value is a list of subtypes
        Map<BaseClass, List<BaseClass>> map = mapper.readValue(mapper.writeValueAsString("{ \"base\": [{\"subtype1\": []}]}"), new TypeReference<Map<BaseClass, List<BaseClass>>>() {}.getType());

        assertNotNull(map);
        assertEquals(1, map.size());
        assertEquals(Collections.singletonList(new Subtype1()), map.get(new BaseClass()));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdDeserialization() throws Exception {
        // Test with a list of base types
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, BaseClass.class);
        List<BaseClass> list = mapper.readValue(mapper.writeValueAsString(Collections.singletonList(new Subtype1())), javaType);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertInstanceOf(Subtype1.class, list.get(0));

        // Test with a Map where key is base type and value is a list of subtypes
        Map<BaseClass, List<BaseClass>> map = mapper.readValue(mapper.writeValueAsString("{ \"base\": [{\"@type\": \"Subtype1\"}]}"), new TypeReference<Map<BaseClass, List<BaseClass>>>() {}.getType());

        assertNotNull(map);
        assertEquals(1, map.size());
        assertEquals(Collections.singletonList(new Subtype1()), map.get(new BaseClass()));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdDeserializationWithUnknownTypeId() throws Exception {
        // Test with a list of base types
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, BaseClass.class);
        List<BaseClass> list = mapper.readValue(mapper.writeValueAsString(Collections.singletonList(new Object())), javaType);

        assertNotNull(list);
        assertEquals(1, list.size());
        assertNotNull(list.get(0));
        assertEquals(BaseClass.class, list.get(0).getClass());
    }
}

class BaseClass {
}

class Subtype1 extends BaseClass {
}

class Subtype2 extends BaseClass {
}