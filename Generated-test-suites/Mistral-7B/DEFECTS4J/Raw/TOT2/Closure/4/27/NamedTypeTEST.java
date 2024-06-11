import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.test.BaseTestCase;
import com.google.javascript.rhino.test.TypedLoad;

import java.util.List;

public class NamedTypeTest extends BaseTestCase {

  public void testNamedTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test creating a simple NamedType
    NamedType namedType = new NamedType(registry, "String", "test.js", 1, 1);
    assertEquals("String", namedType.getReferenceName());
    assertNotNull(namedType.getReferencedType());

    // Test creating a NamedType with a validator
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof StringType;
      }
    };
    namedType = new NamedType(registry, "StringType", "test.js", 1, 1);
    namedType.setValidator(validator);
    assertFalse(namedType.isResolved());
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), namedType.getReferencedType());

    // Test creating a NamedType with a valid reference
    registry.defineType("StringType", StringType.INSTANCE);
    assertTrue(namedType.isResolved());
    assertEquals(StringType.INSTANCE, namedType.getReferencedType());

    // Test creating a NamedType with an invalid reference
    registry.defineType("InvalidType", InvalidType.INSTANCE);
    namedType = new NamedType(registry, "InvalidType", "test.js", 1, 1);
    assertFalse(namedType.isResolved());
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), namedType.getReferencedType());

    // Test creating a NamedType with a looping reference
    registry.defineType("LoopingType", new NamedType(registry, "LoopingType", "test.js", 1, 1));
    namedType = new NamedType(registry, "LoopingType", "test.js", 1, 1);
    assertFalse(namedType.isResolved());
    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
  }

  public void testNamedTypeDefinition() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test defining a property on a NamedType
    NamedType namedType = new NamedType(registry, "MyNamedType", "test.js", 1, 1);
    namedType.defineProperty("myProperty", BooleanType.INSTANCE, false, null);

    ObjectType myObjectType = new ObjectType(registry, "MyObjectType");
    TypedLoad.testLoadType(myObjectType, "test.js");
    assertEquals(NamedType.class, myObjectType.getPrototype().getClass());
    assertTrue(myObjectType.hasProperty("myProperty"));
    assertEquals(BooleanType.INSTANCE, myObjectType.getPropertyType("myProperty"));

    // Test defining a property on a NamedType with a validator
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof NumberType;
      }
    };
    namedType = new NamedType(registry, "MyNumberType", "test.js", 1, 1);
    namedType.setValidator(validator);
    namedType.defineProperty("myProperty", NumberType.INSTANCE, false, null);
    myObjectType = new ObjectType(registry, "MyObjectType");
    TypedLoad.testLoadType(myObjectType, "test.js");
    assertTrue(myObjectType.hasProperty("myProperty"));
    assertEquals(NumberType.INSTANCE, myObjectType.getPropertyType("myProperty"));

    // Test defining a property on a NamedType with an invalid reference
    registry.defineType("InvalidNumberType", InvalidNumberType.INSTANCE);
    namedType = new NamedType(registry, "InvalidNumberType", "test.js", 1, 1);
    namedType.defineProperty("myProperty", NumberType.INSTANCE, false, null);
    myObjectType = new ObjectType(registry, "MyObjectType");
    TypedLoad.testLoadType(myObjectType, "test.js");
    assertFalse(myObjectType.hasProperty("myProperty"));

    // Test defining a property on a NamedType with a looping reference
    registry.defineType("LoopingType", new NamedType(registry, "LoopingType", "test.js", 1, 1));
    namedType = new NamedType(registry, "LoopingType", "test.js", 1, 1);
    namedType.defineProperty("myProperty", NumberType.INSTANCE, false, null);
    myObjectType = new ObjectType(registry, "MyObjectType");
    TypedLoad.testLoadType(myObjectType, "test.js");
    assertFalse(myObjectType.hasProperty("myProperty"));
  }

  public void testNamedTypeResolution() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test resolving a NamedType via properties
    registry.defineType("Foo", new ObjectType(registry, "Foo"));
    NamedType namedType = new NamedType(registry, "Foo.bar", "test.js", 1, 1);
    assertNotNull(namedType.getReferencedType());
    assertEquals(registry.getType("Foo").getElementsType(), namedType.getReferencedType());

    // Test resolving a NamedType via the registry
    registry.defineType("StringType", StringType.INSTANCE);
    namedType = new NamedType(registry, "StringType", "test.js", 1, 1);
    assertEquals(StringType.INSTANCE, namedType.getReferencedType());

    // Test resolving a NamedType with an invalid reference
    registry.defineType("InvalidType", InvalidType.INSTANCE);
    namedType = new NamedType(registry, "InvalidType", "test.js", 1, 1);
    assertNull(namedType.getReferencedType());

    // Test resolving a NamedType with a looping reference
    registry.defineType("LoopingType", new NamedType(registry, "LoopingType", "test.js", 1, 1));
    namedType = new NamedType(registry, "LoopingType", "test.js", 1, 1);
    assertNull(namedType.getReferencedType());

    // Test resolving a NamedType with a property continuation
    registry.defineType("Foo", new ObjectType(registry, "Foo"));
    registry.defineType("Bar", new ObjectType(registry, "Bar"));
    namedType = new NamedType(registry, "Foo.Bar", "test.js", 1, 1);
    namedType.defineProperty("myProperty", BooleanType.INSTANCE, false, null);
    ObjectType myObjectType = new ObjectType(registry, "Foo");
    TypedLoad.testLoadType(myObjectType, "test.js");
    ObjectType barType = (ObjectType) myObjectType.getPropertyType("Bar");
    assertNotNull(barType);
    assertEquals(NamedType.class, barType.getPrototype().getClass());
    assertTrue(barType.hasProperty("myProperty"));
    assertEquals(BooleanType.INSTANCE, barType.getPropertyType("myProperty"));
  }

  public void testNamedTypeExceptionHandling() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test handling a NPE when getting the referenced type of an unresolved NamedType
    try {
      new NamedType(registry, "InvalidType", "test.js", 1, 1).getReferencedType();
      fail("Expected NPE");
    } catch (NullPointerException e) {
      // Expected
    }

    // Test handling a NPE when defining a property on an unresolved NamedType
    NamedType namedType = new NamedType(registry, "InvalidType", "test.js", 1, 1);
    try {
      namedType.defineProperty("myProperty", BooleanType.INSTANCE, false, null);
      fail("Expected NPE");
    } catch (NullPointerException e) {
      // Expected
    }

    // Test handling a NPE when resolving a NamedType with a looping reference
    registry.defineType("LoopingType", new NamedType(registry, "LoopingType", "test.js", 1, 1));
    namedType = new NamedType(registry, "LoopingType", "test.js", 1, 1);
    try {
      namedType.getReferencedType();
      fail("Expected NPE");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  public static class StringType extends ObjectType {
    public static final StringType INSTANCE = new StringType();

    public StringType() {
      super(new JSTypeRegistry(), ObjectType.STRING);
    }
  }

  public static class InvalidNumberType extends ObjectType {
    public static final InvalidNumberType INSTANCE = new InvalidNumberType();

    public InvalidNumberType() {
      super(new JSTypeRegistry(), ObjectType.INVALID_NUMBER);
    }
  }

  public static class InvalidType extends ObjectType {
    public static final InvalidType INSTANCE = new InvalidType();

    public InvalidType() {
      super(new JSTypeRegistry(), ObjectType.INVALID_TYPE);
    }
  }

  public static class LoopType extends ObjectType {
    public static final LoopType INSTANCE = new LoopType();

    public LoopType() {
      super(new JSTypeRegistry(), ObjectType.LOOP_TYPE);
    }
  }
}