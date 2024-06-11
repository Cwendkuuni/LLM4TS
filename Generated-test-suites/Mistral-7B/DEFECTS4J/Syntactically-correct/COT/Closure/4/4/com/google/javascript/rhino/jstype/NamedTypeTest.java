import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.ProxyObjectType;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.EnumType;
import com.google.javascript.rhino.EnumElementType;
import com.google.javascript.rhino.FunctionType;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class NamedTypeTest {

  @SuppressWarnings("unchecked")
  private static JSTypeRegistry registry = new JSTypeRegistry() {
    @Override
    protected ErrorReporter errorReporter() {
      return new ErrorReporter() {};
    }
  };

  @SuppressWarnings("unchecked")
  private static StaticScope<JSType> enclosing = new StaticScope<JSType>() {};

  @SuppressWarnings("unchecked")
  private static class DummyErrorReporter implements ErrorReporter {
    @Override
    public void error(String message, String fileName, int line, int lineNumber) {
      fail("Unexpected error: " + message);
    }

    @Override
    public void warning(String message, String fileName, int line, int lineNumber) {
      // do nothing
    }
  }

  @org.junit.Test
  public void testNamedType_constructor() {
    String reference = "testType";
    String sourceName = "TestClass.java";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(reference, namedType.getReferenceName());
  }

  @org.junit.Test
  public void testNamedType_isNamedType() {
    NamedType namedType = new NamedType(registry, "testType", "TestClass.java", 1, 1);
    assertTrue(namedType.isNamedType());
  }

  @org.junit.Test
  public void testNamedType_isNominalType() {
    NamedType namedType = new NamedType(registry, "testType", "TestClass.java", 1, 1);
    assertTrue(namedType.isNominalType());
  }

  @org.junit.Test
  public void testNamedType_toStringHelper() {
    NamedType namedType = new NamedType(registry, "testType", "TestClass.java", 1, 1);
    assertEquals("testType", namedType.toStringHelper(false));
  }

  @org.junit.Test
  public void testNamedType_hasReferenceName() {
    NamedType namedType = new NamedType(registry, "testType", "TestClass.java", 1, 1);
    assertTrue(namedType.hasReferenceName());
  }

  @org.junit.Test
  public void testNamedType_isResolved() {
    NamedType namedType = new NamedType(registry, "testType", "TestClass.java", 1, 1);
    assertFalse(namedType.isResolved());
  }

  @org.junit.Test
  public void testNamedType_resolve_registry() {
    String reference = "testType";
    JSType type = new ObjectType(registry, "TestType");
    registry.registerType(reference, type);

    NamedType namedType = new NamedType(registry, reference, "TestClass.java", 1, 1);
    JSType resolvedType = namedType.resolveInternal(new DummyErrorReporter(), enclosing);
    assertEquals(type, resolvedType);
  }

  @org.junit.Test
  public void testNamedType_resolve_properties() {
    String reference = "testType.first.second";

    StaticSlot<JSType> slot1 = enclosing.defineSlot("first", new ObjectType(registry, "First"));
    ObjectType objectType = new ObjectType(registry, "TestType");
    StaticSlot<JSType> slot2 = objectType.defineSlot("second", new ObjectType(registry, "Second"));

    NamedType namedType = new NamedType(registry, reference, "TestClass.java", 1, 1);
    JSType resolvedType = namedType.resolveInternal(new DummyErrorReporter(), enclosing);
    assertEquals(slot2.getType(), resolvedType);
  }

  @org.junit.Test
  public void testNamedType_resolve_unresolved() {
    String reference = "testType";

    NamedType namedType = new NamedType(registry, reference, "TestClass.java", 1, 1);
    JSType resolvedType = namedType.resolveInternal(new DummyErrorReporter(), enclosing);
    assertNull(resolvedType);
  }

  @org.junit.Test
  public void testNamedType_resolve_functionType() {
    String reference = "testType.Function";

    FunctionType functionType = new FunctionType(registry, null, null, null);
    registry.registerType(reference, functionType);

    NamedType namedType = new NamedType(registry, reference, "TestClass.java", 1, 1);
    JSType resolvedType = namedType.resolveInternal(new DummyErrorReporter(), enclosing);
    assertEquals(functionType, resolvedType);
  }

  @org.junit.Test
  public void testNamedType_resolve_enumType() {
    String reference = "testType.Enum";

    EnumType enumType = new EnumType(registry, "TestEnum");
    registry.registerType(reference, enumType);

    NamedType namedType = new NamedType(registry, reference, "TestClass.java", 1, 1);
    JSType resolvedType = namedType.resolveInternal(new DummyErrorReporter(), enclosing);
    assertEquals(enumType, resolvedType);
  }

  @org.junit.Test
  public void testNamedType_resolve_enumElementType() {
    String reference = "testType.Enum.Element";

    EnumType enumType = new EnumType(registry, "TestEnum");
    EnumElementType enumElementType = new EnumElementType(registry, enumType, "Element");
    registry.registerType(reference, enumElementType);

    NamedType namedType = new NamedType(registry, reference, "TestClass.java", 1, 1);
    JSType resolvedType = namedType.resolveInternal(new DummyErrorReporter(), enclosing);
    assertEquals(enumElementType, resolvedType);
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testNamedType_resolve_cycle() {
    String reference = "testType.Cycle";

    ObjectType objectType = new ObjectType(registry, "TestType");
    StaticSlot<JSType> slot = objectType.defineSlot("Cycle", new NamedType(registry, reference, "TestClass.java", 1, 1));

    NamedType namedType = new NamedType(registry, reference, "TestClass.java", 1, 1);
    namedType.resolveInternal(new DummyErrorReporter(), enclosing);
    fail("Expected cycle error not thrown");
  }

  @org.junit.Test
  public void testNamedType_defineProperty() {
    String reference = "testType";
    String propertyName = "testProperty";

    NamedType namedType = new NamedType(registry, reference, "TestClass.java", 1, 1);
    JSType propertyType = new ObjectType(registry, "TestPropertyType");

    namedType.defineProperty(propertyName, propertyType, false, null);

    ObjectType objectType = (ObjectType) namedType.getReferencedType();
    assertEquals(propertyType, objectType.getPropertyType(propertyName));
  }

  @org.junit.Test
  public void testNamedType_propertyContinuations() {
    String reference = "testType";
    String propertyName1 = "testProperty1";
    String propertyName2 = "testProperty2";

    NamedType namedType = new NamedType(registry, reference, "TestClass.java", 1, 1);

    List<PropertyContinuation> propertyContinuations = namedType.propertyContinuations;
    assertEquals(2, propertyContinuations.size());

    PropertyContinuation continuation1 = propertyContinuations.get(0);
    assertEquals(propertyName1, continuation1.propertyName);
    assertNull(continuation1.type);
    assertFalse(continuation1.inferred);
    assertNull(continuation1.propertyNode);

    PropertyContinuation continuation2 = propertyContinuations.get(1);
    assertEquals(propertyName2, continuation2.propertyName);
    assertNull(continuation2.type);
    assertFalse(continuation2.inferred);
    assertNull(continuation2.propertyNode);
  }

  @org.junit.Test
  public void testNamedType_finishPropertyContinuations() {
    String reference = "testType";
    String propertyName1 = "testProperty1";
    String propertyName2 = "testProperty2";

    ObjectType objectType = new ObjectType(registry, "TestType");
    StaticSlot<JSType> slot1 = objectType.defineSlot(propertyName1, new ObjectType(registry, "TestPropertyType1"));
    StaticSlot<JSType> slot2 = objectType.defineSlot(propertyName2, new ObjectType(registry, "TestPropertyType2"));

    NamedType namedType = new NamedType(registry, reference, "TestClass.java", 1, 1);

    namedType.finishPropertyContinuations();

    ObjectType referencedObjType = (ObjectType) namedType.getReferencedType();
    assertEquals(objectType, referencedObjType);

    JSType property1 = referencedObjType.getPropertyType(propertyName1);
    assertEquals(slot1.getType(), property1);

    JSType property2 = referencedObjType.getPropertyType(propertyName2);
    assertEquals(slot2.getType(), property2);
  }

  @org.junit.Test
  public void testNamedType_validate() {
    String reference = "testType";

    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof ObjectType;
      }
    };

    NamedType namedType = new NamedType(registry, reference, "TestClass.java", 1, 1);
    namedType.setValidator(validator);

    JSType referencedType = new ObjectType(registry, "TestType");
    JSType resolvedType = namedType.resolveInternal(new DummyErrorReporter(), enclosing);

    namedType.finishPropertyContinuations();

    try {
      namedType.validate(validator);
      fail("Expected validation error not thrown");
    } catch (IllegalArgumentException e) {
      // expected
    }

    namedType.setReferencedType(referencedType);
    namedType.validate(validator);
  }
}