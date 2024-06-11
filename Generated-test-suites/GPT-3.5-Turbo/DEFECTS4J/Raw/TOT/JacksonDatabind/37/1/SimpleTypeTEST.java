import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTypeTest {

  /**
   * Test case for the constructor that takes a Class object.
   * Verifies that the returned SimpleType object has the correct class and TypeBindings.
   */
  @Test
  public void testSimpleTypeConstructorWithClass() {
    Class<?> cls = Integer.class;
    SimpleType simpleType = new SimpleType(cls);

    assertEquals(cls, simpleType.getRawClass());
    assertNotNull(simpleType.getBindings());
    assertTrue(simpleType.getBindings().isEmpty());
  }

  /**
   * Test case for the constructor that takes a Class object, TypeBindings, superClass, and superInterfaces.
   * Verifies that the returned SimpleType object has the correct class, TypeBindings, superClass, and superInterfaces.
   */
  @Test
  public void testSimpleTypeConstructorWithParameters() {
    Class<?> cls = Integer.class;
    TypeBindings bindings = new TypeBindings();
    JavaType superClass = null;
    JavaType[] superInterfaces = null;
    SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInterfaces);

    assertEquals(cls, simpleType.getRawClass());
    assertEquals(bindings, simpleType.getBindings());
    assertEquals(superClass, simpleType.getSuperClass());
    assertEquals(superInterfaces, simpleType.getInterfaces());
  }

  /**
   * Test case for the constructUnsafe() method.
   * Verifies that the returned SimpleType object has the correct class and TypeBindings.
   */
  @Test
  public void testConstructUnsafe() {
    Class<?> cls = Integer.class;
    SimpleType simpleType = SimpleType.constructUnsafe(cls);

    assertEquals(cls, simpleType.getRawClass());
    assertNotNull(simpleType.getBindings());
    assertTrue(simpleType.getBindings().isEmpty());
  }

  /**
   * Test case for the construct() method with a valid class.
   * Verifies that the returned SimpleType object has the correct class.
   */
  @Test
  public void testConstructWithValidClass() {
    Class<?> cls = Integer.class;
    SimpleType simpleType = SimpleType.construct(cls);

    assertEquals(cls, simpleType.getRawClass());
  }

  /**
   * Test case for the construct() method with a Map class.
   * Verifies that an IllegalArgumentException is thrown.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithMapClass() {
    Class<?> cls = Map.class;
    SimpleType.construct(cls);
  }

  /**
   * Test case for the construct() method with a Collection class.
   * Verifies that an IllegalArgumentException is thrown.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithCollectionClass() {
    Class<?> cls = Collection.class;
    SimpleType.construct(cls);
  }

  /**
   * Test case for the construct() method with an array class.
   * Verifies that an IllegalArgumentException is thrown.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithArrayClass() {
    Class<?> cls = int[].class;
    SimpleType.construct(cls);
  }

  /**
   * Test case for the _narrow() method with the same subclass.
   * Verifies that the returned SimpleType object is the same as the original.
   */
  @Test
  public void testNarrowWithSameSubclass() {
    Class<?> subclass = Integer.class;
    SimpleType simpleType = new SimpleType(subclass);

    SimpleType narrowedType = simpleType._narrow(subclass);

    assertEquals(simpleType, narrowedType);
  }

  /**
   * Test case for the _narrow() method with a different subclass.
   * Verifies that the returned SimpleType object has the narrowed subclass and the same TypeBindings.
   */
  @Test
  public void testNarrowWithDifferentSubclass() {
    Class<?> subclass = Integer.class;
    SimpleType simpleType = new SimpleType(Number.class);

    SimpleType narrowedType = simpleType._narrow(subclass);

    assertNotEquals(simpleType, narrowedType);
    assertEquals(subclass, narrowedType.getRawClass());
    assertEquals(simpleType.getBindings(), narrowedType.getBindings());
  }

  /**
   * Test case for the withTypeHandler() method.
   * Verifies that the returned SimpleType object has the specified type handler.
   */
  @Test
  public void testWithTypeHandler() {
    Object typeHandler = new Object();
    SimpleType simpleType = new SimpleType(Integer.class);

    SimpleType withTypeHandlerType = simpleType.withTypeHandler(typeHandler);

    assertEquals(typeHandler, withTypeHandlerType.getTypeHandler());
  }

  /**
   * Test case for the withValueHandler() method.
   * Verifies that the returned SimpleType object has the specified value handler.
   */
  @Test
  public void testWithValueHandler() {
    Object valueHandler = new Object();
    SimpleType simpleType = new SimpleType(Integer.class);

    SimpleType withValueHandlerType = simpleType.withValueHandler(valueHandler);

    assertEquals(valueHandler, withValueHandlerType.getValueHandler());
  }

  /**
   * Test case for the withStaticTyping() method with a non-static type.
   * Verifies that the returned SimpleType object has the isStatic flag set to true.
   */
  @Test
  public void testWithStaticTypingWithoutStaticType() {
    SimpleType simpleType = new SimpleType(Integer.class);

    SimpleType withStaticTypingType = simpleType.withStaticTyping();

    assertTrue(withStaticTypingType.isStatic());
  }

  /**
   * Test case for the withStaticTyping() method with a static type.
   * Verifies that the returned SimpleType object is the same as the original.
   */
  @Test
  public void testWithStaticTypingWithStaticType() {
    SimpleType simpleType = new SimpleType(Integer.class, TypeBindings.emptyBindings(),
        null, null, null, null, true);

    SimpleType withStaticTypingType = simpleType.withStaticTyping();

    assertEquals(simpleType, withStaticTypingType);
  }

}