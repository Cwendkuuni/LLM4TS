import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.TypeEquivalenceTester;
import com.google.javascript.rhino.testing.TypeEquivalenceTester.TypeEquivalenceMode;

import static com.google.common.truth.Truth.assertThat;

public class NamedTypeTest {

  private static class TestHelper {
    private final JSTypeRegistry registry;
    private final ErrorReporter errorReporter;

    TestHelper() {
      this.registry = new JSTypeRegistry();
      this.errorReporter = new ErrorReporter() {
        @Override
        public void error(String message, String sourceName, int lineNumber, int charPosition) {}

        @Override
        public void warning(String message, String sourceName, int lineNumber, int charPosition) {
          System.out.println("Warning: " + message);
        }
      };
    }

    StaticScope<JSType> createScope() {
      return new StaticScope<JSType>(registry, null) {};
    }
  }

  @org.junit.Test
  public void testNamedType_simple() {
    TestHelper helper = new TestHelper();
    StaticScope<JSType> scope = helper.createScope();
    helper.registry.registerType("String");

    String reference = "String";
    NamedType namedType = new NamedType(helper.registry, reference, "Test", 1, 1);
    JSType referencedType = namedType.getReferencedType();

    // Test equality with the referenced type.
    TypeEquivalenceTester.assertEquals(
        TypeEquivalenceMode.COMPLETE,
        referencedType,
        namedType,
        "NamedType.getReferencedType() should return the same type as the constructed NamedType.");

    // Test getting the reference name.
    assertThat(namedType.getReferenceName()).isEqualTo(reference);

    // Test defining a property and getting it back.
    namedType.defineProperty("prop", JSType.BOOLEAN, false, null);
    JSType property = namedType.getProperty("prop");
    TypeEquivalenceTester.assertEquals(
        TypeEquivalenceMode.COMPLETE,
        JSType.BOOLEAN,
        property,
        "NamedType.getProperty() should return the defined property.");

    // Test resolving the type.
    JSType resolvedType = namedType.resolve(helper.errorReporter, scope);
    JSTypeString stringType = (JSTypeString) helper.registry.getType("String");
    TypeEquivalenceTester.assertEquals(
        TypeEquivalenceMode.COMPLETE,
        stringType,
        resolvedType,
        "NamedType.resolve() should return the resolved type.");

    helper.registry.unregisterType("String");
  }

  @org.junit.Test
  public void testNamedType_unresolved() {
    TestHelper helper = new TestHelper();
    StaticScope<JSType> scope = helper.createScope();
    helper.registry.registerType("String");

    String reference = "UnresolvedType";
    NamedType namedType = new NamedType(helper.registry, reference, "Test", 1, 1);

    // Test getting the reference name.
    assertThat(namedType.getReferenceName()).isEqualTo(reference);

    // Test resolution fails.
    JSType resolvedType = namedType.resolve(helper.errorReporter, scope);
    assert (resolvedType == null);

    helper.registry.unregisterType("String");
  }

  @org.junit.Test
  public void testNamedType_resolveViaRegistry() {
    TestHelper helper = new TestHelper();
    StaticScope<JSType> scope = helper.createScope();
    helper.registry.registerType("String");

    String reference = "String";
    String sourceName = "Test";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(helper.registry, reference, sourceName, lineno, charno);

    // Test resolution via registry.
    JSType resolvedType = namedType.resolve(helper.errorReporter, scope);
    JSTypeString stringType = (JSTypeString) helper.registry.getType("String");
    TypeEquivalenceTester.assertEquals(
        TypeEquivalenceMode.COMPLETE,
        stringType,
        resolvedType,
        "NamedType.resolve() via registry should return the registered type.");

    helper.registry.unregisterType("String");
  }

  @org.junit.Test
  public void testNamedType_resolveViaProperties() {
    TestHelper helper = new TestHelper();
    StaticScope<JSType> scope = helper.createScope();
    helper.registry.registerType("Object");

    String reference = "Object.foo.bar";
    String sourceName = "Test";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> parentScope = helper.createScope();
    helper.registry.registerType("Object");
    ObjectType parentType = ObjectType.create(helper.registry, null, "Object");
    parentType.defineProperty("foo", JSType.OBJECT, false, null);
    parentScope.setSlot("Object", parentType);

    NamedType namedType = new NamedType(helper.registry, reference, sourceName, lineno, charno);

    // Test resolution via properties.
    JSType resolvedType = namedType.resolve(helper.errorReporter, scope);
    JSType fooType = ObjectType.create(helper.registry, null, "Object");
    fooType.defineProperty("bar", JSType.STRING, false, null);
    TypeEquivalenceTester.assertEquals(
        TypeEquivalenceMode.COMPLETE,
        fooType.getProperty("bar"),
        resolvedType,
        "NamedType.resolve() via properties should return the resolved type.");

    helper.registry.unregisterType("Object");
    parentScope.dispose();
  }

  @org.junit.Test
  public void testNamedType_implicitPrototypeCycle() {
    TestHelper helper = new TestHelper();
    StaticScope<JSType> scope = helper.createScope();
    helper.registry.registerType("Object");

    String reference = "Object.prototype.constructor";
    String sourceName = "Test";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> parentScope = helper.createScope();
    helper.registry.registerType("Object");
    ObjectType parentType = ObjectType.create(helper.registry, null, "Object");
    parentType.defineProperty("prototype", JSType.OBJECT, false, null);
    parentType.setPrototype(parentType);
    parentScope.setSlot("Object", parentType);

    NamedType namedType = new NamedType(helper.registry, reference, sourceName, lineno, charno);

    // Test resolution with implicit prototype cycle.
    JSType resolvedType = namedType.resolve(helper.errorReporter, scope);
    ObjectType objectType = ObjectType.create(helper.registry, null, "Object");
    TypeEquivalenceTester.assertEquals(
        TypeEquivalenceMode.COMPLETE,
        objectType,
        resolvedType,
        "NamedType.resolve() should detect and handle implicit prototype cycles.");

    helper.registry.unregisterType("Object");
    parentScope.dispose();
  }

  @org.junit.Test
  public void testNamedType_unresolvedWithValidation() {
    TestHelper helper = new TestHelper();
    StaticScope<JSType> scope = helper.createScope();
    helper.registry.registerType("String");

    String reference = "UnresolvedType";
    String sourceName = "Test";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(helper.registry, reference, sourceName, lineno, charno);
    namedType.setValidator(type -> {
      throw new RuntimeException("Validator called, but type is not resolved.");
    });

    // Test validation fails.
    try {
      namedType.resolve(helper.errorReporter, scope);
      fail("NamedType.resolve() should throw an exception with validation.");
    } catch (RuntimeException e) {
      // expected
    }

    helper.registry.unregisterType("String");
  }
}