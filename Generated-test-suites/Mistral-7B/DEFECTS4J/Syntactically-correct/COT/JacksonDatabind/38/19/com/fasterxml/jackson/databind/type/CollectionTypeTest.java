package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings$Std;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CollectionTypeTest {

 private CollectionType collectionType;
 private CollectionType collectionTypeWithBindings;
 private CollectionType collectionTypeWithSuperTypes;
 private CollectionType collectionTypeWithStaticTyping;
 private CollectionLikeType collectionLikeType;
 private JavaType javaType;
 private JavaType javaTypeWithStaticTyping;
 private JavaType javaTypeWithElementType;
 private JavaType javaTypeWithContentType;
 private JavaType javaTypeWithTypeHandler;
 private JavaType javaTypeWithContentTypeHandler;
 private JavaType javaTypeWithValueHandler;
 private JavaType javaTypeWithContentValueHandler;

 @Before
 public void setUp() {
 Class<?> rawType = java.util.ArrayList.class;
 JavaType elemT = TypeFactory.defaultInstance().constructType(int.class);

 collectionType = CollectionType.construct(rawType, null, null, null, elemT);
 collectionTypeWithBindings = CollectionType.construct(rawType, new TypeBindings() {{
 setBindings(Std.StdClassBindings.instance);
 }}, null, null, elemT);
 collectionTypeWithSuperTypes = CollectionType.construct(rawType, null,
 TypeFactory.defaultInstance().constructType(java.util.List.class), null, elemT);
 collectionTypeWithStaticTyping = CollectionType.withStaticTyping(collectionType);
 collectionLikeType = new CollectionLikeType(rawType, null, null, null, elemT, null, null, false);
 javaType = TypeFactory.defaultInstance().constructType(rawType);
 javaTypeWithStaticTyping = TypeFactory.defaultInstance().constructType(collectionTypeWithStaticTyping.getClass());
 javaTypeWithElementType = TypeFactory.defaultInstance().constructType(elemT.getClass());
 javaTypeWithContentType = TypeFactory.defaultInstance().constructType(CollectionType.class, javaTypeWithStaticTyping, javaTypeWithElementType);
 javaTypeWithTypeHandler = TypeFactory.defaultInstance().constructType(CollectionType.class, javaTypeWithStaticTyping, javaTypeWithElementType, new Object(){});
 javaTypeWithContentTypeHandler = TypeFactory.defaultInstance().constructType(CollectionType.class, javaTypeWithStaticTyping, javaTypeWithContentTypeHandler, new Object(){});
 javaTypeWithValueHandler = TypeFactory.defaultInstance().constructType(CollectionType.class, javaTypeWithStaticTyping, javaTypeWithElementType, new Object(){}, new Object(){});
 javaTypeWithContentValueHandler = TypeFactory.defaultInstance().constructType(CollectionType.class, javaTypeWithStaticTyping, javaTypeWithContentValueHandler, new Object(){}, new Object(){});
 }

 @Test
 public void testConstruct() {
 // Test constructor with no arguments
 Assert.assertEquals(collectionType.getClass(), CollectionType.class);
 Assert.assertEquals(collectionType.getRawClass(), java.util.ArrayList.class);
 Assert.assertEquals(collectionType.getBindings(), null);
 Assert.assertEquals(collectionType.getSuperClass(), null);
 Assert.assertArrayEquals(collectionType.getSuperTypes(), new JavaType[] {});
 Assert.assertEquals(collectionType.getElementType(), elemT);
 Assert.assertNull(collectionType.getValueHandler());
 Assert.assertNull(collectionType.getTypeHandler());
 Assert.assertFalse(collectionType.isStatic());

 // Test constructor with bindings
 Assert.assertEquals(collectionTypeWithBindings.getClass(), CollectionType.class);
 Assert.assertEquals(collectionTypeWithBindings.getRawClass(), java.util.ArrayList.class);
 Assert.assertNotNull(collectionTypeWithBindings.getBindings());
 Assert.assertEquals(collectionTypeWithBindings.getBindings().getClass(), TypeBindings.class);
 Assert.assertEquals(collectionTypeWithBindings.getSuperClass(), null);
 Assert.assertArrayEquals(collectionTypeWithBindings.getSuperTypes(), new JavaType[] {});
 Assert.assertEquals(collectionTypeWithBindings.getElementType(), elemT);
 Assert.assertNull(collectionTypeWithBindings.getValueHandler());
 Assert.assertNull(collectionTypeWithBindings.getTypeHandler());
 Assert.assertFalse(collectionTypeWithBindings.isStatic());

 // Test constructor with super types
 Assert.assertEquals(collectionTypeWithSuperTypes.getClass(), CollectionType.class);
 Assert.assertEquals(collectionTypeWithSuperTypes.getRawClass(), java.util.ArrayList.class);
 Assert.assertNull(collectionTypeWithSuperTypes.getBindings());
 Assert.assertNotNull(collectionTypeWithSuperTypes.getSuperClass());
 Assert.assertNotNull(collectionTypeWithSuperTypes.getSuperClass().getClass());
 Assert.assertArrayEquals(collectionTypeWithSuperTypes.getSuperTypes(), new JavaType[] {TypeFactory.defaultInstance().constructType(java.util.List.class)});
 Assert.assertEquals(collectionTypeWithSuperTypes.getElementType(), elemT);
 Assert.assertNull(collectionTypeWithSuperTypes.getValueHandler());
 Assert.assertNull(collectionTypeWithSuperTypes.getTypeHandler());
 Assert.assertFalse(collectionTypeWithSuperTypes.isStatic());
 }

 @Test
 public void testConstruct_deprecated() {
 // Test deprecated constructor with no arguments
 // This constructor is marked as deprecated since 2.7 and will be removed in 2.8
 Assert.assertEquals(CollectionType.construct(java.util.ArrayList.class, elemT).getClass(), CollectionType.class);
 Assert.assertEquals(CollectionType.construct(java.util.ArrayList.class, elemT).getRawClass(), java.util.ArrayList.class);
 Assert.assertNull(CollectionType.construct(java.util.ArrayList.class, elemT).getBindings());
 Assert.assertNull(CollectionType.construct(java.util.ArrayList.class, elemT).getSuperClass());
 Assert.assertArrayEquals(CollectionType.construct(java.util.ArrayList.class, elemT).getSuperTypes(), new JavaType[] {});
 Assert.assertEquals(CollectionType.construct(java.util.ArrayList.class, elemT).getElementType(), elemT);
 Assert.assertNull(CollectionType.construct(java.util.ArrayList.class, elemT).getValueHandler());
 Assert.assertNull(CollectionType.construct(java.util.ArrayList.class, elemT).getTypeHandler());
 Assert.assertFalse(CollectionType.construct(java.util.ArrayList.class, elemT).isStatic());

 // Test deprecated constructor with element type
 // This constructor is marked as deprecated since 2.7 and will be removed in 2.8
 CollectionType collectionTypeWithBogusSuperclass = CollectionType.construct(java.util.ArrayList.class, elemT);
 Assert.assertEquals(collectionTypeWithBogusSuperclass.getClass(), CollectionType.class);
 Assert.assertEquals(collectionTypeWithBogusSuperclass.getRawClass(), java.util.ArrayList.class);
 Assert.assertNull(collectionTypeWithBogusSuperclass.getBindings());
 Assert.assertNotNull(collectionTypeWithBogusSuperclass.getSuperClass());
 Assert.assertFalse(collectionTypeWithBogusSuperclass._asStatic);
 Assert.assertTrue(collectionTypeWithBogusSuperclass._bogusSuperClass instanceof TypeBase);
 Assert.assertNull(collectionTypeWithBogusSuperclass.getSuperTypes());
 Assert.assertEquals(collectionTypeWithBogusSuperclass.getElementType(), elemT);
 Assert.assertNull(collectionTypeWithBogusSuperclass.getValueHandler());
 Assert.assertNull(collectionTypeWithBogusSuperclass.getTypeHandler());
 }

 @Test
 public void testNarrow() {
 // Test _narrow method
 Assert.assertNotEquals(collectionType, collectionTypeWithSuperTypes);
 CollectionType narrowedCollectionType = collectionType.narrow(java.util.ArrayList.class);
 Assert.assertEquals(narrowedCollectionType.getClass(), CollectionType.class);
 Assert.assertEquals(narrowedCollectionType.getRawClass(), java.util.ArrayList.class);
 Assert.assertArrayEquals(narrowedCollectionType.getSuperTypes(), new JavaType[] {});
 Assert.assertEquals(narrowedCollectionType.getElementType(), elemT);
 Assert.assertNull(narrowedCollectionType.getValueHandler());
 Assert.assertNull(narrowedCollectionType.getTypeHandler());
 Assert.assertFalse(narrowedCollectionType.isStatic());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrow_notCollection() {
 // Test _narrow method with non-collection type
 CollectionType collectionTypeWithNonCollection = new CollectionType(String.class, null, null, null, elemT);
 collectionTypeWithNonCollection.narrow(Integer.class);
 }

 @Test
 public void testWithContentType() {
 Assert.assertEquals(collectionType.withContentType(javaTypeWithElementType).getClass(), CollectionType.class);
 Assert.assertEquals(collectionType.withContentType(javaTypeWithElementType).getRawClass(), java.util.ArrayList.class);
 Assert.assertArrayEquals(collectionType.withContentType(javaTypeWithElementType).getSuperTypes(), new JavaType[] {});
 Assert.assertEquals(collectionType.withContentType(javaTypeWithElementType).getElementType(), javaTypeWithElementType);
 Assert.assertNull(collectionType.withContentType(javaTypeWithElementType).getValueHandler());
 Assert.assertNull(collectionType.withContentType(javaTypeWithElementType).getTypeHandler());
 Assert.assertFalse(collectionType.withContentType(javaTypeWithElementType).isStatic());
 }

 @Test
 public void testWithTypeHandler() {
 Assert.assertEquals(collectionType.withTypeHandler(new Object()).getClass(), CollectionType.class);
 Assert.assertEquals(collectionType.withTypeHandler(new Object()).getRawClass(), java.util.ArrayList.class);
 Assert.assertArrayEquals(collectionType.withTypeHandler(new Object()).getSuperTypes(), new JavaType[] {});
 Assert.assertEquals(collectionType.withTypeHandler(new Object()).getElementType(), elemT);
 Assert.assertNotNull(collectionType.withTypeHandler(new Object()).getTypeHandler());
 Assert.assertNull(collectionType.withTypeHandler(new Object()).getValueHandler());
 Assert.assertFalse(collectionType.withTypeHandler(new Object()).isStatic());
 }

 @Test
 public void testWithContentTypeHandler() {
 Assert.assertEquals(collectionType.withContentTypeHandler(new Object()).getClass(), CollectionType.class);
 Assert.assertEquals(collectionType.withContentTypeHandler(new Object()).getRawClass(), java.util.ArrayList.class);
 Assert.assertArrayEquals(collectionType.withContentTypeHandler(new Object()).getSuperTypes(), new JavaType[] {});
 Assert.assertEquals(collectionType.withContentTypeHandler(new Object()).getElementType(), elemT);
 Assert.assertNotNull(collectionType.withContentTypeHandler(new Object()).getTypeHandler());
 Assert.assertNull(collectionType.withContentTypeHandler(new Object()).getValueHandler());
 Assert.assertFalse(collectionType.withContentTypeHandler(new Object()).isStatic());
 }

 @Test
 public void testWithValueHandler() {
 Assert.assertEquals(collectionType.withValueHandler(new Object()).getClass(), CollectionType.class);
 Assert.assertEquals(collectionType.withValueHandler(new Object()).getRawClass(), java.util.ArrayList.class);
 Assert.assertArrayEquals(collectionType.withValueHandler(new Object()).getSuperTypes(), new JavaType[] {});
 Assert.assertEquals(collectionType.withValueHandler(new Object()).getElementType(), elemT);
 Assert.assertNull(collectionType.withValueHandler(new Object()).getTypeHandler());
 Assert.assertNotNull(collectionType.withValueHandler(new Object()).getValueHandler());
 Assert.assertFalse(collectionType.withValueHandler(new Object()).isStatic());
 }

 @Test
 public void testWithContentValueHandler() {
 Assert.assertEquals(collectionType.withContentValueHandler(new Object(), new Object()).getClass(), CollectionType.class);
 Assert.assertEquals(collectionType.withContentValueHandler(new Object(), new Object()).getRawClass(), java.util.ArrayList.class);
 Assert.assertArrayEquals(collectionType.withContentValueHandler(new Object(), new Object()).getSuperTypes(), new JavaType[] {});
 Assert.assertEquals(collectionType.withContentValueHandler(new Object(), new Object()).getElementType(), elemT);
 Assert.assertNotNull(collectionType.withContentValueHandler(new Object(), new Object()).getTypeHandler());
 Assert.assertNotNull(collectionType.withContentValueHandler(new Object(), new Object()).getValueHandler());
 Assert.assertFalse(collectionType.withContentValueHandler(new Object(), new Object()).isStatic());
 }

 @Test
 public void testWithStaticTyping() {
 Assert.assertFalse(collectionType.isStatic());
 Assert.assertTrue(collectionTypeWithStaticTyping.isStatic());
 }

 @Test
 public void testRefine() {
 Assert.assertNotEquals(collectionType, collectionLikeType);
 CollectionType refinedCollectionType = collectionType.refine(java.util.ArrayList.class, null, null, null);
 Assert.assertEquals(refinedCollectionType.getClass(), CollectionType.class);
 Assert.assertEquals(refinedCollectionType.getRawClass(), java.util.ArrayList.class);
 Assert.assertArrayEquals(refinedCollectionType.getSuperTypes(), new JavaType[] {});
 Assert.assertEquals(refinedCollectionType.getElementType(), elemT);
 Assert.assertNull(refinedCollectionType.getValueHandler());
 Assert.assertNull(refinedCollectionType.getTypeHandler());
 Assert.assertFalse(refinedCollectionType.isStatic());
 }

 @Test
 public void testToString() {
 Assert.assertEquals("[collection type; class java.util.ArrayList, contains class int]", collectionType.toString());
 }
}