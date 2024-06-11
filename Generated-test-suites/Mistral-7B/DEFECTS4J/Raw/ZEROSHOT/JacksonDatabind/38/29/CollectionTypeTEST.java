import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings.TypeSerializer;
import com.fasterxml.jackson.databind.type.CollectionType;

@RunWith(MockitoJUnitRunner.class)
public class CollectionTypeTest {

 private JavaType elemType;
 private JavaType superClassType;
 private JavaType collectionType;
 private TypeBindings bindings;

 @Before
 public void setup() {
 elemType = Mockito.mock(JavaType.class);
 superClassType = Mockito.mock(JavaType.class);
 bindings = new TypeBindings();
 collectionType = new CollectionType(Mockito.mock(Class.class), bindings, superClassType, new JavaType[0], elemType);
 }

 @Test
 public void testConstruct() {
 JavaType constructCollectionType = CollectionType.construct(Mockito.mock(Class.class), bindings, superClassType, new JavaType[0], elemType);
 assertSame(collectionType, constructCollectionType);
 }

 @Test
 public void testConstructDeprecated() {
 JavaType constructCollectionType = CollectionType.construct(Mockito.mock(Class.class), elemType);
 assertInstanceOf(CollectionType.class, constructCollectionType);
 }

 @Test
 public void testNarrowDeprecated() {
 JavaType narrowCollectionType = collectionType._narrow(Mockito.mock(Class.class));
 assertNotSame(collectionType, narrowCollectionType);
 assertInstanceOf(CollectionType.class, narrowCollectionType);
 }

 @Test
 public void testWithContentType() {
 JavaType contentType = Mockito.mock(JavaType.class);
 JavaType withContentType = collectionType.withContentType(contentType);
 assertNotSame(collectionType, withContentType);
 assertInstanceOf(CollectionType.class, withContentType);
 assertSame(elemType, withContentType.getElementType());
 assertSame(contentType, withContentType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 Object typeHandler = Mockito.mock(Object.class);
 JavaType withTypeHandler = collectionType.withTypeHandler(typeHandler);
 assertNotSame(collectionType, withTypeHandler);
 assertInstanceOf(CollectionType.class, withTypeHandler);
 assertSame(elemType, withTypeHandler.getElementType());
 assertSame(typeHandler, withTypeHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Object contentTypeHandler = Mockito.mock(Object.class);
 Object typeHandler = Mockito.mock(Object.class);
 JavaType withContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);
 assertNotSame(collectionType, withContentTypeHandler);
 assertInstanceOf(CollectionType.class, withContentTypeHandler);
 assertSame(elemType, withContentTypeHandler.getElementType());
 assertSame(contentTypeHandler, withContentTypeHandler.getContentTypeHandler());
 assertSame(typeHandler, withContentTypeHandler.getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Object valueHandler = Mockito.mock(Object.class);
 JavaType withValueHandler = collectionType.withValueHandler(valueHandler);
 assertNotSame(collectionType, withValueHandler);
 assertInstanceOf(CollectionType.class, withValueHandler);
 assertSame(elemType, withValueHandler.getElementType());
 assertSame(valueHandler, withValueHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Object contentValueHandler = Mockito.mock(Object.class);
 Object valueHandler = Mockito.mock(Object.class);
 JavaType withContentValueHandler = collectionType.withContentValueHandler(contentValueHandler);
 assertNotSame(collectionType, withContentValueHandler);
 assertInstanceOf(CollectionType.class, withContentValueHandler);
 assertSame(elemType, withContentValueHandler.getElementType());
 assertSame(contentValueHandler, withContentValueHandler.getContentValueHandler());
 assertSame(valueHandler, withContentValueHandler.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 JavaType withStaticTyping = collectionType.withStaticTyping();
 assertNotSame(collectionType, withStaticTyping);
 assertInstanceOf(CollectionType.class, withStaticTyping);
 assertTrue(withStaticTyping.isStatic());
 assertTrue(collectionType.isDynamic());
 }

 @Test
 public void testRefine() {
 Class<?> rawType = Mockito.mock(Class.class);
 JavaType refinedType = collectionType.refine(rawType, bindings, superClassType, new JavaType[0]);
 assertNotSame(collectionType, refinedType);
 assertInstanceOf(CollectionType.class, refinedType);
 assertSame(rawType, refinedType.getRawClass());
 assertSame(bindings, refinedType.getTypeBindings());
 assertSame(superClassType, refinedType.getSuperClass());
 assertSame(new JavaType[0], refinedType.getSuperInterfaces());
 assertSame(elemType, refinedType.getContentType());
 assertNull(refinedType.getValueHandler());
 assertNull(refinedType.getTypeHandler());
 assertFalse(refinedType.isStatic());
 }

 @Test
 public void testToString() {
 String toString = collectionType.toString();
 assertTrue(toString.startsWith("[collection type; class "));
 assertTrue(toString.endsWith("]"));
 }
}