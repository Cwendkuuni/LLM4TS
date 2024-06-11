
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

   @Test
   public void testConstruct() {
      // Given
      Class<?> rawType = MapType.class;
      TypeBindings bindings = null;
      JavaType superClass = null;
      JavaType[] superInts = null;
      JavaType keyT = null;
      JavaType valueT = null;
      Object valueHandler = null;
      Object typeHandler = null;
      boolean asStatic = false;
      
      // When
      MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, asStatic);
      
      // Then
      assertNotNull(mapType);
      assertEquals(rawType, mapType.getRawClass());
      assertEquals(bindings, mapType.getBindings());
      assertEquals(superClass, mapType.getSuperClass());
      assertEquals(superInts, mapType.getInterfaces());
      assertEquals(keyT, mapType.getKeyType());
      assertEquals(valueT, mapType.getContentType());
      assertEquals(valueHandler, mapType.getValueHandler());
      assertEquals(typeHandler, mapType.getTypeHandler());
      assertEquals(asStatic, mapType.isStatic());
   }

   @Test
   public void testConstructDeprecated() {
      // Given
      Class<?> rawType = MapType.class;
      JavaType keyT = null;
      JavaType valueT = null;

      // When
      MapType mapType = MapType.construct(rawType, keyT, valueT);
      
      // Then
      assertNotNull(mapType);
      assertEquals(rawType, mapType.getRawClass());
      assertEquals(keyT, mapType.getKeyType());
      assertEquals(valueT, mapType.getContentType());
   }

   @Test
   public void testNarrow() {
      // Given
      Class<?> subclass = MapType.class;
      MapType mapType = new MapType(subclass, null, null, null,
                null, null, null, null, false);

      // When
      JavaType narrowed = mapType._narrow(subclass);

      // Then
      assertNotNull(narrowed);
      assertEquals(subclass, narrowed.getRawClass());
   }
   
   @Test
   public void testWithTypeHandler() {
      // Given
      Object h = new Object();
      MapType mapType = new MapType(MapType.class, null, null, null,
                null, null, null, null, false);

      // When
      MapType newMapType = mapType.withTypeHandler(h);

      // Then
      assertNotNull(newMapType);
      assertEquals(h, newMapType.getTypeHandler());
   }

   @Test
   public void testWithContentTypeHandler() {
      // Given
      Object h = new Object();
      MapType mapType = new MapType(MapType.class, null, null, null,
                null, null, null, null, false);

      // When
      MapType newMapType = mapType.withContentTypeHandler(h);

      // Then
      assertNotNull(newMapType);
      assertEquals(h, newMapType.getContentType().getTypeHandler());
   }
   
   @Test
   public void testWithValueHandler() {
      // Given
      Object h = new Object();
      MapType mapType = new MapType(MapType.class, null, null, null,
                null, null, null, null, false);

      // When
      MapType newMapType = mapType.withValueHandler(h);

      // Then
      assertNotNull(newMapType);
      assertEquals(h, newMapType.getValueHandler());
   }

   @Test
   public void testWithContentValueHandler() {
      // Given
      Object h = new Object();
      MapType mapType = new MapType(MapType.class, null, null, null,
                null, null, null, null, false);

      // When
      MapType newMapType = mapType.withContentValueHandler(h);

      // Then
      assertNotNull(newMapType);
      assertEquals(h, newMapType.getContentType().getValueHandler());
   }
   
   @Test
   public void testWithStaticTyping() {
      // Given
      MapType mapType = new MapType(MapType.class, null, null, null,
                null, null, null, null, false);

      // When
      MapType newMapType = mapType.withStaticTyping();

      // Then
      assertNotNull(newMapType);
      assertTrue(newMapType.isStatic());
      assertTrue(newMapType.getKeyType().isStatic());
      assertTrue(newMapType.getContentType().isStatic());
   }

   @Test
   public void testWithContentType() {
      // Given
      JavaType contentType = null;
      MapType mapType = new MapType(MapType.class, null, null, null,
                null, null, null, null, false);

      // When
      MapType newMapType = mapType.withContentType(contentType);

      // Then
      assertNotNull(newMapType);
      assertEquals(contentType, newMapType.getContentType());
   }
   
   @Test
   public void testWithKeyType() {
      // Given
      JavaType keyType = null;
      MapType mapType = new MapType(MapType.class, null, null, null,
                null, null, null, null, false);

      // When
      MapType newMapType = mapType.withKeyType(keyType);

      // Then
      assertNotNull(newMapType);
      assertEquals(keyType, newMapType.getKeyType());
   }
   
   @Test
   public void testRefine() {
      // Given
      Class<?> rawType = MapType.class;
      TypeBindings bindings = null;
      JavaType superClass = null;
      JavaType[] superInterfaces = null;
      JavaType keyT = null;
      JavaType valueT = null;
      Object valueHandler = null;
      Object typeHandler = null;
      boolean asStatic = false;
      
      MapType mapType = new MapType(rawType, bindings, superClass, superInterfaces,
                keyT, valueT, valueHandler, typeHandler, asStatic);

      // When
      JavaType refinedType = mapType.refine(rawType, bindings, superClass, superInterfaces);

      // Then
      assertNotNull(refinedType);
      assertEquals(rawType, refinedType.getRawClass());
      assertEquals(bindings, refinedType.getBindings());
      assertEquals(superClass, refinedType.getSuperClass());
      assertEquals(superInterfaces, refinedType.getInterfaces());
      assertEquals(keyT, refinedType.getKeyType());
      assertEquals(valueT, refinedType.getContentType());
      assertEquals(valueHandler, refinedType.getValueHandler());
      assertEquals(typeHandler, refinedType.getTypeHandler());
      assertEquals(asStatic, refinedType.isStatic());
   }
   
   @Test
   public void testWithKeyTypeHandler() {
      // Given
      Object h = new Object();
      MapType mapType = new MapType(MapType.class, null, null, null,
                null, null, null, null, false);

      // When
      MapType newMapType = mapType.withKeyTypeHandler(h);

      // Then
      assertNotNull(newMapType);
      assertEquals(h, newMapType.getKeyType().getTypeHandler());
   }

   @Test
   public void testWithKeyValueHandler() {
      // Given
      Object h = new Object();
      MapType mapType = new MapType(MapType.class, null, null, null,
                null, null, null, null, false);

      // When
      MapType newMapType = mapType.withKeyValueHandler(h);

      // Then
      assertNotNull(newMapType);
      assertEquals(h, newMapType.getKeyType().getValueHandler());
   }

   @Test
   public void testToString() {
      // Given
      Class<?> rawType = MapType.class;
      JavaType keyT = null;
      JavaType valueT = null;

      MapType mapType = new MapType(rawType, null, null, null,
                keyT, valueT, null, null, false);

      // When
      String toString = mapType.toString();

      // Then
      assertEquals("[map type; class "+rawType.getName()+", "+keyT+" -> "+valueT+"]", toString);
   }
}
