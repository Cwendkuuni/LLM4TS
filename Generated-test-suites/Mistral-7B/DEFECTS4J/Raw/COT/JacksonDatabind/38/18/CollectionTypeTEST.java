import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.core.type.TypeBindings;

public class CollectionTypeTest {

 private JavaType constructCollectionType(Class<?> rawType, JavaType elemType) {
 return CollectionType.construct(rawType, null, null, null, elemType);
 }

 @Test
 public void testCollectionTypeConstructor() {
 Class<?> listClass = ArrayList.class;
 JavaType stringType = BasicTypes.STRING_TYPE;
 CollectionType listType = constructCollectionType(listClass, stringType);
 Assert.assertEquals(listClass, listType.getRawClass());
 Assert.assertEquals(String.class, listType.getContentType());
 Assert.assertNull(listType.getSuperClass());
 Assert.assertNull(listType.getSuperInterfaces());
 Assert.assertNull(listType.getValueHandler());
 Assert.assertNull(listType.getTypeHandler());
 Assert.assertFalse(listType.isStatic());
 }

 @Test
 public void testCollectionTypeWithContentType() {
 Class<?> listClass = ArrayList.class;
 JavaType stringType = BasicTypes.STRING_TYPE;
 JavaType listWithIntType = CollectionType.construct(ArrayList.class, null, null, null, stringType);
 CollectionType listType = (CollectionType) CollectionType.construct(listClass, null, null, null, BasicTypes.INT_TYPE).withContentType(stringType);
 Assert.assertEquals(listClass, listType.getRawClass());
 Assert.assertEquals(String.class, listType.getContentType());
 Assert.assertNull(listType.getSuperClass());
 Assert.assertNull(listType.getSuperInterfaces());
 Assert.assertNull(listType.getValueHandler());
 Assert.assertNull(listType.getTypeHandler());
 Assert.assertFalse(listType.isStatic());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCollectionTypeInvalidArguments() {
 Class<?> listClass = ArrayList.class;
 JavaType stringType = BasicTypes.STRING_TYPE;
 CollectionType listType = new CollectionType(null, null, null, null, stringType, null, null, false);
 }

 @Test
 public void testCollectionTypeNarrow() {
 Class<?> listClass = ArrayList.class;
 Class<?> subListClass = SubList.class;
 JavaType stringType = BasicTypes.STRING_TYPE;
 CollectionType listType = new CollectionType(listClass, null, null, null, stringType, null, null, false);
 CollectionType subListType = listType._narrow(subListClass);
 Assert.assertEquals(subListClass, subListType.getRawClass());
 Assert.assertEquals(String.class, subListType.getContentType());
 Assert.assertNull(subListType.getSuperClass());
 Assert.assertNull(subListType.getSuperInterfaces());
 Assert.assertNull(subListType.getValueHandler());
 Assert.assertNull(subListType.getTypeHandler());
 Assert.assertFalse(subListType.isStatic());
 }

 @Test
 public void testCollectionTypeWithTypeHandler() {
 Class<?> listClass = ArrayList.class;
 JavaType stringType = BasicTypes.STRING_TYPE;
 CollectionType listType = new CollectionType(listClass, null, null, null, stringType, null, null, false);
 Object typeHandler = new Object() {};
 CollectionType handlerListType = listType.withTypeHandler(typeHandler);
 Assert.assertEquals(listClass, handlerListType.getRawClass());
 Assert.assertEquals(String.class, handlerListType.getContentType());
 Assert.assertEquals(typeHandler, handlerListType.getTypeHandler());
 Assert.assertNull(handlerListType.getSuperClass());
 Assert.assertNull(handlerListType.getSuperInterfaces());
 Assert.assertNull(handlerListType.getValueHandler());
 Assert.assertFalse(handlerListType.isStatic());
 }

 @Test
 public void testCollectionTypeWithContentTypeHandler() {
 Class<?> listClass = ArrayList.class;
 JavaType stringType = BasicTypes.STRING_TYPE;
 CollectionType listType = new CollectionType(listClass, null, null, null, stringType, null, null, false);
 Object contentTypeHandler = new Object() {};
 CollectionType handlerListType = listType.withContentTypeHandler(contentTypeHandler);
 Assert.assertEquals(listClass, handlerListType.getRawClass());
 Assert.assertEquals(String.class, handlerListType.getContentType());
 Assert.assertEquals(contentTypeHandler, handlerListType.getContentTypeHandler());
 Assert.assertNull(handlerListType.getSuperClass());
 Assert.assertNull(handlerListType.getSuperInterfaces());
 Assert.assertNull(handlerListType.getValueHandler());
 Assert.assertFalse(handlerListType.isStatic());
 }

 @Test
 public void testCollectionTypeWithValueHandler() {
 Class<?> listClass = ArrayList.class;
 JavaType stringType = BasicTypes.STRING_TYPE;
 CollectionType listType = new CollectionType(listClass, null, null, null, stringType, null, null, false);
 Object valueHandler = new Object() {};
 CollectionType handlerListType = listType.withValueHandler(valueHandler);
 Assert.assertEquals(listClass, handlerListType.getRawClass());
 Assert.assertEquals(String.class, handlerListType.getContentType());
 Assert.assertEquals(valueHandler, handlerListType.getValueHandler());
 Assert.assertNull(handlerListType.getSuperClass());
 Assert.assertNull(handlerListType.getSuperInterfaces());
 Assert.assertNull(handlerListType.getTypeHandler());
 Assert.assertFalse(handlerListType.isStatic());
 }

 @Test
 public void testCollectionTypeWithContentValueHandler() {
 Class<?> listClass = ArrayList.class;
 JavaType stringType = BasicTypes.STRING_TYPE;
 CollectionType listType = new CollectionType(listClass, null, null, null, stringType, null, null, false);
 Object contentValueHandler = new Object() {};
 CollectionType handlerListType = listType.withContentValueHandler(contentValueHandler);
 Assert.assertEquals(listClass, handlerListType.getRawClass());
 Assert.assertEquals(String.class, handlerListType.getContentType());
 Assert.assertEquals(contentValueHandler, handlerListType.getContentValueHandler());
 Assert.assertNull(handlerListType.getSuperClass());
 Assert.assertNull(handlerListType.getSuperInterfaces());
 Assert.assertNull(handlerListType.getValueHandler());
 Assert.assertFalse(handlerListType.isStatic());
 }

 @Test
 public void testCollectionTypeWithStaticTyping() {
 Class<?> listClass = ArrayList.class;
 JavaType stringType = BasicTypes.STRING_TYPE;
 CollectionType listType = new CollectionType(listClass, null, null, null, stringType, null, null, false);
 CollectionType staticListType = listType.withStaticTyping();
 Assert.assertEquals(listClass, staticListType.getRawClass());
 Assert.assertEquals(String.class, staticListType.getContentType());
 Assert.assertNull(staticListType.getSuperClass());
 Assert.assertNull(staticListType.getSuperInterfaces());
 Assert.assertNull(staticListType.getValueHandler());
 Assert.assertNull(staticListType.getTypeHandler());
 Assert.assertTrue(staticListType.isStatic());
 }

 @Test
 public void testCollectionTypeRefine() {
 Class<?> listClass = ArrayList.class;
 JavaType intType = BasicTypes.INT_TYPE;
 CollectionType listType = new CollectionType(listClass, null, null, null, intType, null, null, false);
 TypeBindings bindings = new TypeBindings();
 JavaType refinedListType = listType.refine(null, bindings, null, null);
 Assert.assertEquals(listClass, refinedListType.getRawClass());
 Assert.assertEquals(intType, refinedListType.getContentType());
 Assert.assertNull(refinedListType.getSuperClass());
 Assert.assertNull(refinedListType.getSuperInterfaces());
 Assert.assertNull(refinedListType.getValueHandler());
 Assert.assertNull(refinedListType.getTypeHandler());
 Assert.assertFalse(refinedListType.isStatic());
 }

}