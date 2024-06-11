import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

public class SimpleTypeTest {

   @Test
   public void testConstructor1() {
      SimpleType simpleType = new SimpleType(String.class);
      assertEquals(String.class, simpleType.getRawClass());
      assertTrue(simpleType.getBindings().isEmpty());
      assertNull(simpleType.getSuperClass());
      assertNull(simpleType.getInterfaces());
      assertNull(simpleType.getValueHandler());
      assertNull(simpleType.getTypeHandler());
      assertFalse(simpleType.isAsStatic());
   }

   @Test
   public void testConstructor2() {
      TypeBindings bindings = new TypeBindings();
      JavaType superClass = new SimpleType(Number.class);
      JavaType[] superInterfaces = new JavaType[2];
      superInterfaces[0] = new SimpleType(Serializable.class);
      superInterfaces[1] = new SimpleType(Cloneable.class);
      SimpleType simpleType = new SimpleType(Integer.class, bindings,
      superClass, superInterfaces);
      assertEquals(Integer.class, simpleType.getRawClass());
      assertEquals(bindings, simpleType.getBindings());
      assertEquals(superClass, simpleType.getSuperClass());
      assertArrayEquals(superInterfaces, simpleType.getInterfaces());
      assertNull(simpleType.getValueHandler());
      assertNull(simpleType.getTypeHandler());
      assertFalse(simpleType.isAsStatic());
   }

   @Test
   public void testConstructor3() {
      SimpleType base = new SimpleType(Long.class);
      SimpleType simpleType = new SimpleType(base);
      assertEquals(Long.class, simpleType.getRawClass());
      assertEquals(base.getBindings(), simpleType.getBindings());
      assertEquals(base.getSuperClass(), simpleType.getSuperClass());
      assertArrayEquals(base.getInterfaces(), simpleType.getInterfaces());
      assertEquals(base.getValueHandler(), simpleType.getValueHandler());
      assertEquals(base.getTypeHandler(), simpleType.getTypeHandler());
      assertEquals(base.isAsStatic(), simpleType.isAsStatic());
   }

   @Test
   public void testConstructor4() {
      TypeBindings bindings = new TypeBindings();
      JavaType superClass = new SimpleType(Number.class);
      JavaType[] superInterfaces = new JavaType[2];
      superInterfaces[0] = new SimpleType(Serializable.class);
      superInterfaces[1] = new SimpleType(Cloneable.class);
      Object valueHandler = new Object();
      Object typeHandler = new Object();
      SimpleType simpleType = new SimpleType(Double.class, bindings,
      superClass, superInterfaces, valueHandler, typeHandler, true);
      assertEquals(Double.class, simpleType.getRawClass());
      assertEquals(bindings, simpleType.getBindings());
      assertEquals(superClass, simpleType.getSuperClass());
      assertArrayEquals(superInterfaces, simpleType.getInterfaces());
      assertEquals(valueHandler, simpleType.getValueHandler());
      assertEquals(typeHandler, simpleType.getTypeHandler());
      assertTrue(simpleType.isAsStatic());
   }

   @Test
   public void testConstructUnsafe() {
      SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
      assertEquals(Integer.class, simpleType.getRawClass());
      assertNull(simpleType.getBindings());
      assertNull(simpleType.getSuperClass());
      assertNull(simpleType.getInterfaces());
      assertNull(simpleType.getValueHandler());
      assertNull(simpleType.getTypeHandler());
      assertFalse(simpleType.isAsStatic());
   }

   @Test
   public void testConstruct() {
      try {
          SimpleType.construct(Map.class);
          fail("Expected IllegalArgumentException");
      } catch (IllegalArgumentException e) {
          assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
      }
      
      try {
          SimpleType.construct(Collection.class);
          fail("Expected IllegalArgumentException");
      } catch (IllegalArgumentException e) {
          assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
      }
      
      try {
          SimpleType.construct(int[].class);
          fail("Expected IllegalArgumentException");
      } catch (IllegalArgumentException e) {
          assertEquals("Can not construct SimpleType for an array (class: [I)", e.getMessage());
      }
      
      SimpleType simpleType = SimpleType.construct(Integer.class);
      assertEquals(Integer.class, simpleType.getRawClass());
      assertNull(simpleType.getBindings());
      assertNull(simpleType.getSuperClass());
      assertNull(simpleType.getInterfaces());
      assertNull(simpleType.getValueHandler());
      assertNull(simpleType.getTypeHandler());
      assertFalse(simpleType.isAsStatic());
   }

   @Test
   public void testNarrow() {
      SimpleType base = new SimpleType(Number.class);
      SimpleType simpleType = base._narrow(Long.class);
      assertEquals(Long.class, simpleType.getRawClass());
      assertEquals(base.getBindings(), simpleType.getBindings());
      assertEquals(base.getSuperClass(), simpleType.getSuperClass());
      assertArrayEquals(base.getInterfaces(), simpleType.getInterfaces());
      assertEquals(base.getValueHandler(), simpleType.getValueHandler());
      assertEquals(base.getTypeHandler(), simpleType.getTypeHandler());
      assertEquals(base.isAsStatic(), simpleType.isAsStatic());
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testWithContentType() {
      SimpleType simpleType = new SimpleType(Double.class);
      simpleType.withContentType(Double.class);
   }
   
   @Test
   public void testWithTypeHandler() {
      Object typeHandler = new Object();
      SimpleType simpleType = new SimpleType(Integer.class);
      SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
      assertEquals(Integer.class, newSimpleType.getRawClass());
      assertEquals(simpleType.getBindings(), newSimpleType.getBindings());
      assertEquals(simpleType.getSuperClass(), newSimpleType.getSuperClass());
      assertArrayEquals(simpleType.getInterfaces(), newSimpleType.getInterfaces());
      assertEquals(simpleType.getValueHandler(), newSimpleType.getValueHandler());
      assertEquals(typeHandler, newSimpleType.getTypeHandler());
      assertEquals(simpleType.isAsStatic(), newSimpleType.isAsStatic());
      assertTrue(simpleType.equals(newSimpleType));
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testWithContentTypeHandler() {
      SimpleType simpleType = new SimpleType(Double.class);
      simpleType.withContentTypeHandler(new Object());
   }
   
   @Test
   public void testWithContentValueHandler() {
      SimpleType simpleType = new SimpleType(Integer.class);
      SimpleType newSimpleType = simpleType.withContentValueHandler(new Object());
      assertEquals(Integer.class, newSimpleType.getRawClass());
      assertEquals(simpleType.getBindings(), newSimpleType.getBindings());
      assertEquals(simpleType.getSuperClass(), newSimpleType.getSuperClass());
      assertArrayEquals(simpleType.getInterfaces(), newSimpleType.getInterfaces());
      assertEquals(simpleType.getValueHandler(), newSimpleType.getValueHandler());
      assertEquals(simpleType.getTypeHandler(), newSimpleType.getTypeHandler());
      assertEquals(simpleType.isAsStatic(), newSimpleType.isAsStatic());
      assertTrue(simpleType.equals(newSimpleType));
   }
   
   @Test
   public void testWithStaticTyping() {
      SimpleType simpleType = new SimpleType(Integer.class);
      SimpleType newSimpleType = simpleType.withStaticTyping();
      assertEquals(Integer.class, newSimpleType.getRawClass());
      assertEquals(simpleType.getBindings(), newSimpleType.getBindings());
      assertEquals(simpleType.getSuperClass(), newSimpleType.getSuperClass());
      assertArrayEquals(simpleType.getInterfaces(), newSimpleType.getInterfaces());
      assertEquals(simpleType.getValueHandler(), newSimpleType.getValueHandler());
      assertEquals(simpleType.getTypeHandler(), newSimpleType.getTypeHandler());
      assertTrue(newSimpleType.isAsStatic());
   }
   
   @Test
   public void testRefine() {
      SimpleType simpleType = new SimpleType(Integer.class);
      assertNull(simpleType.refine(Long.class, new TypeBindings(), new SimpleType(Number.class),
      new JavaType[2]));
   }
   
   @Test
   public void testBuildCanonicalName() {
      SimpleType simpleType = new SimpleType(Long.class);
      assertEquals("java.lang.Long", simpleType.buildCanonicalName());
      
      TypeBindings bindings = new TypeBindings();
      bindings.addBoundType("T", new SimpleType(Double.class));
      simpleType = new SimpleType(List.class, bindings, null, null);
      assertEquals("java.lang.List<java.lang.Double>", simpleType.buildCanonicalName());
   }
   
   @Test
   public void testIsContainerType() {
      SimpleType simpleType = new SimpleType(Integer.class);
      assertFalse(simpleType.isContainerType());
   }
   
   @Test
   public void testGetErasedSignature() {
      SimpleType simpleType = new SimpleType(Integer.class);
      StringBuilder sb = new StringBuilder();
      simpleType.getErasedSignature(sb);
      assertEquals("Ljava/lang/Integer;", sb.toString());
   }
   
   @Test
   public void testGetGenericSignature() {
      TypeBindings bindings = new TypeBindings();
      bindings.addBoundType("T", new SimpleType(Double.class));
      SimpleType simpleType = new SimpleType(List.class, bindings, null, null);
      StringBuilder sb = new StringBuilder();
      simpleType.getGenericSignature(sb);
      assertEquals("Ljava/util/List<Ljava/lang/Double;>;", sb.toString());
   }

   @Test
   public void testToString() {
      SimpleType simpleType = new SimpleType(Long.class);
      assertEquals("[simple type, class java.lang.Long]", simpleType.toString());
   }

   @Test
   public void testEquals() {
      SimpleType simpleType1 = new SimpleType(Long.class);
      SimpleType simpleType2 = new SimpleType(Long.class);
      assertTrue(simpleType1.equals(simpleType2));
   }

}