package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeBase;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 private JavaType listIntType;
 private JavaType setIntType;
 private JavaType mapStringIntType;

 @Before
 public void setUp() {
 TypeBindings bindings = new TypeBindings();
 listIntType = CollectionType.construct(java.util.List.class, bindings, null, null, JavaType.getCollectionType(Integer.TYPE));
 setIntType = CollectionType.construct(java.util.Set.class, bindings, null, null, JavaType.getCollectionType(Integer.TYPE));
 mapStringIntType = CollectionType.construct(java.util.Map.class, bindings,
 MapType.construct(String.class, Integer.TYPE), null,
 JavaType.constructType(Map.Entry.class, String.class, JavaType.getCollectionType(Integer.TYPE)));
 }

 @Test
 public void testConstruct() {
 // Test constructors
 JavaType listIntTypeInstance = new CollectionType(java.util.List.class, null, null, null, JavaType.getCollectionType(Integer.TYPE), null, null, false);
 JavaType setIntTypeInstance = new CollectionType(java.util.Set.class, null, null, null, JavaType.getCollectionType(Integer.TYPE), null, null, false);
 JavaType mapStringIntTypeInstance = new CollectionType(java.util.Map.class, null, MapType.construct(String.class, Integer.TYPE), null,
 JavaType.constructType(Map.Entry.class, String.class, JavaType.getCollectionType(Integer.TYPE)), null, null, false);

 assertEquals(listIntType, listIntTypeInstance);
 assertEquals(setIntType, setIntTypeInstance);
 assertEquals(mapStringIntType, mapStringIntTypeInstance);
 }

 @Test
 public void testNarrow() {
 try {
 CollectionType listIntArrayListType = (CollectionType) listIntType;
 CollectionType listIntArrayListSubClass = listIntArrayListType.narrow(java.util.ArrayList.class);
 assertEquals(JavaType.constructCollectionType(ArrayList.class, JavaType.getCollectionType(Integer.TYPE)), listIntArrayListSubClass);
 } catch (ClassCastException e) {
 // expected
 }
 try {
 CollectionType listIntLinkedListType = (CollectionType) listIntType;
 CollectionType listIntLinkedListSubClass = listIntLinkedListType.narrow(LinkedList.class);
 assertEquals(JavaType.constructCollectionType(LinkedList.class, JavaType.getCollectionType(Integer.TYPE)), listIntLinkedListSubClass);
 } catch (ClassCastException e) {
 // expected
 }

 try {
 CollectionType mapStringIntHashMapType = (CollectionType) mapStringIntType;
 CollectionType mapStringIntHashMapSubClass = mapStringIntHashMapType.narrow(HashMap.class);
 assertEquals(JavaType.constructMapType(HashMap.class, String.class, JavaType.getCollectionType(Integer.TYPE)), mapStringIntHashMapSubClass);
 } catch (ClassCastException e) {
 // expected
 }
 }

 @Test
 public void testWithContentType() {
 JavaType listIntListType = listIntType.withContentType(JavaType.constructCollectionType(ArrayList.class, JavaType.getCollectionType(Integer.TYPE)));
 JavaType setIntHashSetType = setIntType.withContentType(JavaType.constructCollectionType(HashSet.class, JavaType.getCollectionType(Integer.TYPE)));
 JavaType mapStringIntTreeMapType = mapStringIntType.withContentType(JavaType.constructCollectionType(TreeMap.class, JavaType.getCollectionType(Integer.TYPE)));

 assertEquals(JavaType.constructCollectionType(ArrayList.class, JavaType.getCollectionType(Integer.TYPE)), listIntListType);
 assertEquals(JavaType.constructCollectionType(HashSet.class, JavaType.getCollectionType(Integer.TYPE)), setIntHashSetType);
 assertEquals(JavaType.constructMapType(TreeMap.class, String.class, JavaType.getCollectionType(Integer.TYPE)), mapStringIntTreeMapType);
 }

 @Test
 public void testWithTypeHandler() {
 CollectionType listIntListTypeWithHandler = listIntType.withTypeHandler(new TypeSerializer() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeStartArray();
 if (value instanceof Collection) {
 for (Object e : (Collection<?>) value) {
 gen.writeValue(e);
 }
 }
 gen.writeEndArray();
 }

 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new ArrayList<>();
 }
 });

 CollectionType setIntSetTypeWithHandler = setIntType.withTypeHandler(new TypeSerializer() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeStartArray();
 if (value instanceof Collection) {
 for (Object e : (Collection<?>) value) {
 gen.writeValue(e);
 }
 }
 gen.writeEndArray();
 }

 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new HashSet<>();
 }
 });

 CollectionType mapStringIntMapTypeWithHandler = mapStringIntType.withTypeHandler(new TypeSerializer() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 if (value instanceof Map) {
 gen.writeStartObject();
 for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
 gen.writeFieldName(entry.getKey().toString());
 gen.writeValue(entry.getValue());
 }
 gen.writeEndObject();
 } else {
 gen.writeNull();
 }
 }

 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new HashMap<>();
 }
 });

 JavaType listIntTypeWithHandler = listIntListTypeWithHandler.withContentType(JavaType.getCollectionType(ArrayList.class));
 JavaType setIntTypeWithHandler = setIntSetTypeWithHandler.withContentType(JavaType.getCollectionType(HashSet.class));
 JavaType mapStringIntTypeWithHandler = mapStringIntMapTypeWithHandler.withContentType(JavaType.getCollectionType(TreeMap.class));

 assertEquals(JavaType.constructCollectionType(ArrayList.class, JavaType.getCollectionType(Integer.TYPE)), listIntTypeWithHandler);
 assertEquals(JavaType.constructCollectionType(HashSet.class, JavaType.getCollectionType(Integer.TYPE)), setIntTypeWithHandler);
 assertEquals(JavaType.constructMapType(TreeMap.class, String.class, JavaType.getCollectionType(Integer.TYPE)), mapStringIntTypeWithHandler);
 }

 @Test
 public void testWithStaticTyping() {
 CollectionType listIntListType = listIntType.withStaticTyping();
 CollectionType setIntSetType = setIntType.withStaticTyping();
 CollectionType mapStringIntMapType = mapStringIntType.withStaticTyping();

 assertTrue(listIntListType._asStatic);
 assertTrue(setIntSetType._asStatic);
 assertTrue(mapStringIntMapType._asStatic);
 }

 @Test
 public void testRefine() {
 JavaType listIntLinkedListType = listIntType.refine(LinkedList.class, null, null, null);
 JavaType setIntLinkedHashSetType = setIntType.refine(LinkedHashSet.class, null, null, null);
 JavaType mapStringIntTreeMapType = mapStringIntType.refine(TreeMap.class, null, null, null);

 assertEquals(JavaType.constructCollectionType(LinkedList.class, JavaType.getCollectionType(Integer.TYPE)), listIntLinkedListType);
 assertEquals(JavaType.constructCollectionType(LinkedHashSet.class, JavaType.getCollectionType(Integer.TYPE)), setIntLinkedHashSetType);
 assertEquals(JavaType.constructMapType(TreeMap.class, String.class, JavaType.getCollectionType(Integer.TYPE)), mapStringIntTreeMapType);
 }

 @Test
 public void testToString() {
 assertEquals("[collection type; class java.util.List, contains class java.lang.Integer]", listIntType.toString());
 assertEquals("[collection type; class java.util.Set, contains class java.lang.Integer]", setIntType.toString());
 assertEquals("[collection type; class java.util.Map, contains keyClass java.lang.String, valueType java.util.Collection [collection type; class java.util.ArrayList, contains class java.lang.Integer]]", mapStringIntType.toString());
 }
}