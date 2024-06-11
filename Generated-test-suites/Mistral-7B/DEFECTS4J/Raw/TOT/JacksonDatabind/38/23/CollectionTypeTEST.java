import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = String.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object[].class;
 JavaType[] superInts = new JavaType[0];
 CollectionType listType = CollectionType.construct(listClass, bindings, superClass, superInts, elemType);
 assertEquals("expected collection type for ArrayList<String>", listType.getClass(), CollectionType.class);
 assertEquals("expected String as element type", elemType, listType.getContentType());
 }

 @Test
 public void testConstructDeprecated() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = String.class;
 CollectionType listType = CollectionType.construct(listClass, elemType);
 assertEquals("expected collection type for ArrayList<String>", listType.getClass(), CollectionType.class);
 assertEquals("expected String as element type", elemType, listType.getContentType());
 }

 @Test
 public void testWithContentType() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = Integer.class;
 JavaType contentType = List.class;
 CollectionType listType = new CollectionType(listClass, null, Object[].class, new JavaType[0], elemType);
 CollectionType newListType = listType.withContentType(contentType);
 assertEquals("expected List as new content type", contentType, newListType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = String.class;
 CollectionType listType = new CollectionType(listClass, null, Object[].class, new JavaType[0], elemType);
 CollectionType newListType = listType.withTypeHandler(String[].class);
 assertEquals("expected String[] as new type handler", String[].class, newListType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = String.class;
 CollectionType listType = new CollectionType(listClass, null, Object[].class, new JavaType[0], elemType);
 CollectionType newListType = listType.withContentTypeHandler(String[].class);
 assertEquals("expected String[] as new content type handler", String[].class, newListType.getContentHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = String.class;
 CollectionType listType = new CollectionType(listClass, null, Object[].class, new JavaType[0], elemType);
 CollectionType newListType = listType.withValueHandler(StringBuilder.class);
 assertEquals("expected StringBuilder as new value handler", StringBuilder.class, newListType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = String.class;
 CollectionType listType = new CollectionType(listClass, null, Object[].class, new JavaType[0], elemType);
 CollectionType newListType = listType.withContentValueHandler(StringBuilder.class);
 assertEquals("expected StringBuilder as new content value handler", StringBuilder.class, newListType.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = String.class;
 CollectionType listType = new CollectionType(listClass, null, Object[].class, new JavaType[0], elemType);
 CollectionType newListType = listType.withStaticTyping();
 assertEquals("expected static typing to be true", true, newListType.isStaticTyped());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRefineWithNullType() {
 Class<?> listClass = ArrayList.class;
 CollectionType listType = new CollectionType(listClass, null, Object[].class, new JavaType[0], String.class);
 // Refining with null TypeBindings should throw an exception
 listType.refine(null, null, null, null);
 }

 @Test
 public void testRefine() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = String.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Object[].class;
 JavaType[] superInterfaces = new JavaType[0];
 CollectionType listType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemType);
 CollectionType newListType = listType.refine(HashSet.class, null, null, null);
 assertEquals("expected HashSet as new type", HashSet.class, newListType.getClass());
 }

}