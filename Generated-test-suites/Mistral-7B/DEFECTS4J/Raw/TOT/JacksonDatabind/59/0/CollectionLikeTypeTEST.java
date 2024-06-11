import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CollectionLikeTypeTest {

 private static JavaType listType(Class<?> elementType) {
 return TypeFactory.defaultInstance().constructCollectionType(List.class, elementType);
 }

 @Test
 public void testConstructorWithCollection() {
 Class<?> listClass = List.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superclass = Object.class;
 JavaType[] superInterfaces = new JavaType[]{Collection.class};
 JavaType elemType = Integer.TYPE;
 CollectionLikeType clt = CollectionLikeType.construct(listClass, bindings, superclass, superInterfaces, elemType);
 assertEquals(List.class, clt.getRawClass());
 assertEquals(Integer.TYPE, clt.getContentType().getRawClass());
 }

 @Test
 public void testConstructWithType() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 CollectionLikeType clt = CollectionLikeType.construct(listClass, elemType);
 assertEquals(List.class, clt.getRawClass());
 assertEquals(Integer.TYPE, clt.getContentType().getRawClass());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType baseType = listType(Integer.TYPE);
 JavaType elemType = Integer.TYPE;
 CollectionLikeType clt = CollectionLikeType.upgradeFrom(baseType, elemType);
 assertEquals(List.class, clt.getRawClass());
 assertEquals(Integer.TYPE, clt.getContentType().getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidBaseType() {
 JavaType baseType = String.class;
 JavaType elemType = Integer.TYPE;
 CollectionLikeType clt = CollectionLikeType.upgradeFrom(baseType, elemType);
 fail("Should not reach this point");
 }

 @Test
 public void testNarrowing() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 JavaType narrowed = clt.narrow(ArrayList.class);
 assertEquals(ArrayList.class, narrowed.getRawClass());
 }

 @Test
 public void testWithContentType() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 JavaType newContentType = Double.TYPE;
 JavaType newClt = clt.withContentType(newContentType);
 assertEquals(newContentType, newClt.getContentType().getRawClass());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 Object intHandler = new IntHandler();
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 JavaType newClt = clt.withContentTypeHandler(intHandler);
 assertEquals(intHandler, newClt.getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 Object intHandler = new IntHandler();
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 JavaType newClt = clt.withValueHandler(intHandler);
 assertEquals(intHandler, newClt.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 Object intHandler = new IntHandler();
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 JavaType newClt = clt.withContentValueHandler(intHandler);
 assertEquals(intHandler, newClt.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 JavaType newClt = clt.withStaticTyping();
 assertTrue(newClt.isStatic());
 }

 @Test
 public void testRefine() {
 Class<?> listClass = ArrayList.class; // subclass of List
 JavaType elemType = Integer.TYPE;
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType refined = clt.refine(List.class, bindings, Object.class, new JavaType[0]);
 assertEquals(ArrayList.class, refined.getRawClass());
 }

 @Test
 public void testIsContainerType() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 assertTrue(clt.isContainerType());
 }

 @Test
 public void testIsCollectionLikeType() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 assertTrue(clt.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 assertEquals(Integer.TYPE, clt.getContentType().getRawClass());
 }

 @Test
 public void testGetContentValueHandler() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 Object intHandler = new IntHandler();
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 JavaType newClt = clt.withContentTypeHandler(intHandler);
 assertEquals(intHandler, clt.getContentValueHandler());
 }

 @Test
 public void testHasHandlers() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 assertTrue(clt.hasHandlers());
 }

 @Test
 public void testGetErasedSignature() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 StringBuilder sb = new StringBuilder();
 clt.getErasedSignature(sb);
 assertEquals("[Ljava.lang.Object;", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 StringBuilder sb = new StringBuilder();
 clt.getGenericSignature(sb);
 assertEquals("[Ljava.lang.Object;<:Ljava.lang.Integer;>", sb.toString());
 }

 @Test
 public void testBuildCanonicalName() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 assertEquals("[Ljava.util.List;<:Ljava.lang.Integer;>", clt.toCanonical());
 }

 @Test
 public void testIsTrueCollectionType() {
 Class<?> listClass = List.class;
 JavaType elemType = Integer.TYPE;
 JavaType clt = CollectionLikeType.construct(listClass, elemType);
 assertTrue(clt.isTrueCollectionType());
 }

}