import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, null, JavaType.of(Integer.class));
 assertEquals(List.class, listType.getRawClass());
 assertEquals(Integer.class, listType.getContentType());
 }

 @Test
 public void testConstructWithBindings() {
 Class<List<String>> listClass = List.class;
 TypeBindings bindings = new TypeBindings();
 bindings.putConstructor(String[].class, String.class);
 JavaType listType = CollectionType.construct(listClass, bindings, null, null, JavaType.of(String.class));
 assertEquals(List.class, listType.getRawClass());
 assertEquals(String.class, listType.getContentType());
 }

 @Test
 public void testConstructDeprecated() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, null);
 assertEquals(List.class, listType.getRawClass());
 assertEquals(Integer.class, listType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructNullType() {
 JavaType listType = CollectionType.construct(null, null, null, null, JavaType.of(Integer.class));
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructNullContentType() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, null, null, null, null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructNullSuperClass() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(Integer.class), null, null, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructNullSuperInterfaces() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(Integer.class), null, null, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testNarrow() {
 Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
 JavaType listType = CollectionType.construct(List.class, null, null, null, JavaType.of(Integer.class));
 JavaType arrayListType = listType.narrow(arrayListClass);
 assertEquals(ArrayList.class, arrayListType.getRawClass());
 assertEquals(Integer.class, arrayListType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowNullType() {
 JavaType listType = CollectionType.construct(List.class, null, null, null, JavaType.of(Integer.class));
 JavaType arrayListType = listType.narrow(null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testWithContentType() {
 Class<List<String>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(Integer.class));
 JavaType stringListType = listType.withContentType(JavaType.of(String.class));
 assertEquals(List.class, stringListType.getRawClass());
 assertEquals(String.class, stringListType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(Integer.class));
 Object intHandler = new Object();
 JavaType intListType = listType.withTypeHandler(intHandler);
 assertEquals(List.class, intListType.getRawClass());
 assertEquals(Integer.class, intListType.getContentType());
 assertNotNull(intListType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<List<String>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(Integer.class));
 Object stringHandler = new Object();
 JavaType stringListType = listType.withContentTypeHandler(stringHandler);
 assertEquals(List.class, stringListType.getRawClass());
 assertEquals(String.class, stringListType.getContentType());
 assertNotNull(stringListType.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(Integer.class));
 Object handler = new Object();
 JavaType handlerListType = listType.withValueHandler(handler);
 assertEquals(List.class, handlerListType.getRawClass());
 assertEquals(Integer.class, handlerListType.getContentType());
 assertNotNull(handlerListType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<List<String>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(Integer.class));
 Object stringHandler = new Object();
 JavaType stringListType = listType.withContentValueHandler(stringHandler);
 assertEquals(List.class, stringListType.getRawClass());
 assertEquals(String.class, stringListType.getContentType());
 assertNotNull(stringListType.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<List<Integer>> listClass = List.class;
 JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(Integer.class));
 JavaType staticListType = listType.withStaticTyping();
 assertTrue(staticListType.isStatic());
 }

 @Test
 public void testRefine() {
 Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
 JavaType listType = CollectionType.construct(List.class, null, null, null, JavaType.of(Integer.class));
 JavaType arrayListType = listType.refine(arrayListClass, null, null, null);
 assertEquals(ArrayList.class, arrayListType.getRawClass());
 assertEquals(Integer.class, arrayListType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRefineNullType() {
 JavaType listType = CollectionType.construct(List.class, null, null, null, JavaType.of(Integer.class));
 JavaType arrayListType = listType.refine(null, null, null, null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRefineNullBindings() {
 JavaType listType = CollectionType.construct(List.class, null, null, null, JavaType.of(Integer.class));
 JavaType arrayListType = listType.refine(ArrayList.class, null, null, null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRefineNullSuperClass() {
 JavaType listType = CollectionType.construct(List.class, null, null, null, JavaType.of(Integer.class));
 JavaType arrayListType = listType.refine(ArrayList.class, null, null, null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRefineNullSuperInterfaces() {
 JavaType listType = CollectionType.construct(List.class, null, null, null, JavaType.of(Integer.class));
 JavaType arrayListType = listType.refine(ArrayList.class, null, null, null);
 fail("Should throw an IllegalArgumentException");
 }
}