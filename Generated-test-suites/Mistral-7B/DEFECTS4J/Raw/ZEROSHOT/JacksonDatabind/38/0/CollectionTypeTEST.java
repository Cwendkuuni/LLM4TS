import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<List<Integer>> listType = new TypeToken<List<Integer>>() {}.getType();
 JavaType javaListType = new CollectionType(listType.getRawType(), null, null, null, JavaType.construct(int.class));

 // Test with valid arguments
 CollectionType collectionType = CollectionType.construct(listType.getRawType(), null, null, null, JavaType.construct(int.class));
 assertEquals(javaListType, collectionType);

 // Test with null arguments
 assertNull(CollectionType.construct(null, null, null, null, JavaType.construct(int.class)));
 assertNull(CollectionType.construct(listType.getRawType(), null, null, null, null));
 }

 @Test
 public void testDeprecatedConstruct() {
 Class<List<Integer>> listType = new TypeToken<List<Integer>>() {}.getType();
 JavaType javaListType = new CollectionType(listType.getRawType(), null);

 // Test with valid arguments
 CollectionType collectionType = CollectionType.construct(listType.getRawType(), JavaType.construct(int.class));
 assertEquals(javaListType, collectionType);

 // Test with null argument
 assertNull(CollectionType.construct(listType.getRawType(), null));
 }

 @Test
 public void testNarrow() {
 Class<ArrayList<Integer>> arrayListType = new TypeToken<ArrayList<Integer>>() {}.getType();
 JavaType javaListType = new CollectionType(List.class, null, null, null, JavaType.construct(int.class));
 JavaType javaArrayListType = new CollectionType(arrayListType.getRawType(), null);

 // Test with valid subclass
 JavaType narrowed = javaListType.narrow(ArrayList.class);
 assertEquals(javaArrayListType, narrowed);

 // Test with invalid subclass
 assertNull(javaListType.narrow(String.class));
 }

 @Test
 public void testWithContentType() {
 Class<List<Integer>> listType = new TypeToken<List<Integer>>() {}.getType();
 JavaType javaListType = new CollectionType(listType.getRawType(), null, null, null, JavaType.construct(int.class));
 JavaType javaListOfStringType = javaListType.withContentType(JavaType.construct(String.class));

 // Test with valid content type
 assertEquals(List.class, javaListOfStringType.getRawType());
 assertEquals(JavaType.construct(String.class), javaListOfStringType.getContentType());

 // Test with same content type
 assertEquals(javaListType, javaListType.withContentType(JavaType.construct(int.class)));
 }

 @Test
 public void testWithTypeHandler() {
 Class<List<Integer>> listType = new TypeToken<List<Integer>>() {}.getType();
 JavaType javaListType = new CollectionType(listType.getRawType(), null, null, null, JavaType.construct(int.class));

 // Test with valid type handler
 JavaType javaListTypeWithHandler = javaListType.withTypeHandler(new ObjectMapper.DefaultTyping());
 assertNotSame(javaListType, javaListTypeWithHandler);
 assertEquals(ObjectMapper.DefaultTyping.class, javaListTypeWithHandler.getTypeHandler());

 // Test with null type handler
 assertNull(javaListType.withTypeHandler(null));
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<List<Integer>> listType = new TypeToken<List<Integer>>() {}.getType();
 JavaType javaListType = new CollectionType(listType.getRawType(), null, null, null, JavaType.construct(int.class));
 JavaType stringHandler = new SimpleTypeModifier().constructType(String.class);

 // Test with valid content type handler
 JavaType javaListTypeWithContentHandler = javaListType.withContentTypeHandler(stringHandler);
 assertNotSame(javaListType, javaListTypeWithContentHandler);
 assertEquals(stringHandler, javaListTypeWithContentHandler.getContentTypeHandler());

 // Test with null content type handler
 assertNull(javaListType.withContentTypeHandler(null));
 }

 @Test
 public void testWithValueHandler() {
 Class<List<Integer>> listType = new TypeToken<List<Integer>>() {}.getType();
 JavaType javaListType = new CollectionType(listType.getRawType(), null, null, null, JavaType.construct(int.class));

 // Test with valid value handler
 JavaType javaListTypeWithValueHandler = javaListType.withValueHandler(new NumberInput(NumberInput.NumberInputFlag.UseBigDecimalForFloats | NumberInput.NumberInputFlag.AllowEmptyStrings));
 assertNotSame(javaListType, javaListTypeWithValueHandler);
 assertEquals(NumberInput.class, javaListTypeWithValueHandler.getValueHandler());

 // Test with null value handler
 assertNull(javaListType.withValueHandler(null));
 }

 @Test
 public void testWithContentValueHandler() {
 Class<List<Integer>> listType = new TypeToken<List<Integer>>() {}.getType();
 JavaType javaListType = new CollectionType(listType.getRawType(), null, null, null, JavaType.construct(int.class));
 JavaType stringHandler = new SimpleTypeModifier().constructType(String.class);

 // Test with valid content value handler
 JavaType javaListTypeWithContentValueHandler = javaListType.withContentValueHandler(stringHandler);
 assertNotSame(javaListType, javaListTypeWithContentValueHandler);
 assertEquals(stringHandler, javaListTypeWithContentValueHandler.getContentValueHandler());

 // Test with null content value handler
 assertNull(javaListType.withContentValueHandler(null));
 }

 @Test
 public void testWithStaticTyping() {
 Class<List<Integer>> listType = new TypeToken<List<Integer>>() {}.getType();
 JavaType javaListType = new CollectionType(listType.getRawType(), null, null, null, JavaType.construct(int.class));

 // Test with static typing false
 assertFalse(javaListType.isStaticTyping());

 // Test with static typing true
 assertTrue(javaListType.withStaticTyping().isStaticTyping());
 }

 @Test
 public void testRefine() {
 Class<List<Integer>> listType = new TypeToken<List<Integer>>() {}.getType();
 JavaType javaListType = new CollectionType(List.class, null, null, null, JavaType.construct(int.class));
 JavaType refinedType = javaListType.refine(ArrayList.class, null, null, null);

 // Test with valid arguments
 assertEquals(ArrayList.class, refinedType.getRawType());
 assertSame(javaListType, refinedType.getBaseType());
 assertSame(JavaType.construct(int.class), refinedType.getContentType());
 }
}