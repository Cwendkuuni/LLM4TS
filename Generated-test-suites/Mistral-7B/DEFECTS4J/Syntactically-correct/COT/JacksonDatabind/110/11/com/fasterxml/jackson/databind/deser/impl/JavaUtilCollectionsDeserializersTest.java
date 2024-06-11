package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.Converter;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFindForCollection_AsList() throws Exception {
 List<?> actual = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(new TypeReference<List<String>>() {}));
 Assert.assertEquals(StdDelegatingDeserializer.class, actual.getClass());
 }

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 List<String> expected = Arrays.asList("expected");
 List<String> actual = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(Collections.singletonList(String.class).getTypeName()));
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 Set<String> expected = Collections.singleton(String.valueOf('expected'));
 Set<String> actual = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(Collections.singletonSet(String.class).getTypeName()));
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 List<String> expected = Arrays.asList("expected");
 List<String> input = Collections.unmodifiableList(Arrays.asList("input"));
 List<String> actual = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(input.getClass().getTypeName()));
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_UnsupportedType() throws Exception {
 JavaType unsupportedType = mapper.getTypeFactory().constructType(int[].class);
 JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unsupportedType);
 }

 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 Map<String, String> expected = Collections.singletonMap("expectedKey", "expectedValue");
 Map<String, String> actual = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(Collections.singletonMap(String.class, String.class).getTypeName()));
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 Map<String, String> expected = Collections.unmodifiableMap(Collections.singletonMap("expectedKey", "expectedValue"));
 Map<String, String> actual = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(expected.getClass().getTypeName()));
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMap_UnsupportedType() throws Exception {
 JavaType unsupportedType = mapper.getTypeFactory().constructType(int[].class);
 JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), unsupportedType);
 }

 @Test
 public void testConverter_SingleValueSet() throws Exception {
 Set<String> set = Collections.singleton(String.valueOf('singleValue'));
 Object actual = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, mapper.getTypeFactory().constructType(set.getClass().getTypeName()), Set.class).convert(set);
 Assert.assertEquals(Collections.singleton(String.valueOf('singleValue')), actual);
 }

 @Test
 public void testConverter_SingleValueList() throws Exception {
 List<String> list = Collections.singletonList(String.valueOf('singleValue'));
 Object actual = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, mapper.getTypeFactory().constructType(list.getClass().getTypeName()), List.class).convert(list);
 Assert.assertEquals(Collections.singletonList(String.valueOf('singleValue')), actual);
 }

 @Test
 public void testConverter_SingleValueMap() throws Exception {
 Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>("singleKey", "singleValue");
 Map<?,?> map = Collections.singletonMap(entry.getKey(), entry.getValue());
 Object actual = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, mapper.getTypeFactory().constructType(map.getClass().getTypeName()), Map.class).convert(map);
 Assert.assertEquals(Collections.singletonMap(entry.getKey(), entry.getValue()), actual);
 }

 @Test
 public void testConverter_UnmodifiableSet() throws Exception {
 Set<String> set = Collections.unmodifiableSet(Arrays.asList("unmodifiableValue1", "unmodifiableValue2"));
 Object actual = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, mapper.getTypeFactory().constructType(set.getClass().getTypeName()), Set.class).convert(set);
 Assert.assertEquals(Collections.unmodifiableSet(Arrays.asList("unmodifiableValue1", "unmodifiableValue2")), actual);
 }

 @Test
 public void testConverter_UnmodifiableList() throws Exception {
 List<String> list = Collections.unmodifiableList(Arrays.asList("unmodifiableValue1", "unmodifiableValue2"));
 Object actual = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, mapper.getTypeFactory().constructType(list.getClass().getTypeName()), List.class).convert(list);
 Assert.assertEquals(Collections.unmodifiableList(Arrays.asList("unmodifiableValue1", "unmodifiableValue2")), actual);
 }

 @Test
 public void testConverter_UnmodifiableMap() throws Exception {
 Map<String, String> map = Collections.unmodifiableMap(Collections.singletonMap("unmodifiableKey", "unmodifiableValue"));
 Object actual = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, mapper.getTypeFactory().constructType(map.getClass().getTypeName()), Map.class).convert(map);
 Assert.assertEquals(Collections.unmodifiableMap(Collections.singletonMap("unmodifiableKey", "unmodifiableValue")), actual);
 }

 @Test
 public void testConverter_AsList() throws Exception {
 List<String> list = Arrays.asList("asListValue1", "asListValue2");
 Object actual = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, mapper.getTypeFactory().constructType(list.getClass().getTypeName()), List.class).convert(list);
 Assert.assertEquals(list, actual);
 }
}