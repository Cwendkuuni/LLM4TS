import org.junit.*;
import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

   @Test
   public void testFindForCollection() {
       DeserializationContext ctxt = new DeserializationContext();
       JavaType type = new JavaType();
       JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
       assertNull(result);
   }

   @Test
   public void testFindForMap() {
       DeserializationContext ctxt = new DeserializationContext();
       JavaType type = new JavaType();
       JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
       assertNull(result);
   }
   
   @Test
   public void testConverter() {
       JavaType inputType = new JavaType();
       Class<?> rawSuper = Object.class;
       JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, inputType, rawSuper);
       assertNotNull(converter);
   }

   @Test
   public void testConvert_typeSingletonSet() {
       Object value = new HashSet<>();
       JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, new JavaType());
       Object result = converter.convert(value);
       assertNotNull(result);
       assertTrue(result instanceof Set);
       assertEquals(1, ((Set<?>)result).size());
   }

   @Test
   public void testConvert_typeSingletonList() {
       Object value = new ArrayList<>();
       JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, new JavaType());
       Object result = converter.convert(value);
       assertNotNull(result);
       assertTrue(result instanceof List);
       assertEquals(1, ((List<?>)result).size());
   }

   @Test
   public void testConvert_typeSingletonMap() {
       Object value = Collections.singletonMap("a", "b");
       JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, new JavaType());
       Object result = converter.convert(value);
       assertNotNull(result);
       assertTrue(result instanceof Map);
       assertEquals(1, ((Map<?,?>)result).size());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConvert_typeSingletonSet_invalidSize() {
       Object value = new HashSet<>();
       JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, new JavaType());
       converter._checkSingleton(2);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConvert_typeSingletonList_invalidSize() {
       Object value = new ArrayList<>();
       JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, new JavaType());
       converter._checkSingleton(2);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConvert_typeSingletonMap_invalidSize() {
       Object value = Collections.singletonMap("a", "b");
       JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, new JavaType());
       converter._checkSingleton(2);
   }

   @Test
   public void testConvert_typeUnmodifiableSet() {
       Object value = new HashSet<>();
       JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, new JavaType());
       Object result = converter.convert(value);
       assertNotNull(result);
       assertTrue(result instanceof Set);
       assertTrue(result instanceof UnmodifiableSet);
   }

   @Test
   public void testConvert_typeUnmodifiableList() {
       Object value = new ArrayList<>();
       JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, new JavaType());
       Object result = converter.convert(value);
       assertNotNull(result);
       assertTrue(result instanceof List);
       assertTrue(result instanceof UnmodifiableList);
   }

   @Test
   public void testConvert_typeUnmodifiableMap() {
       Object value = Collections.singletonMap("a", "b");
       JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, new JavaType());
       Object result = converter.convert(value);
       assertNotNull(result);
       assertTrue(result instanceof Map);
       assertTrue(result instanceof UnmodifiableMap);
   }

   @Test
   public void testConvert_typeAsList() {
       Object value = new ArrayList<>();
       JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, new JavaType());
       Object result = converter.convert(value);
       assertNotNull(result);
       assertTrue(result instanceof List);
   }
}