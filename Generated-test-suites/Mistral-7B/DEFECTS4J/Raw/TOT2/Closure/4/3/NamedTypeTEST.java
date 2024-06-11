import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.test.BaseGlobalTest;
import com.google.javascript.rhino.test.SharedErrorReporter;

import java.util.List;

public class NamedTypeTest extends BaseGlobalTest {

  private static final String REGISTRY_NAME = "MyTypeRegistry";
  private static final String REGISTRY_CLASS_NAME = "MyTypeRegistryClass";
  private static final String NAMED_TYPE_NAME = "MyNamedType";
  private static final String NAMED_TYPE_REFERENCE = "MyReferencedType";
  private static final String NAMED_TYPE_SOURCE_NAME = "MySourceName";
  private static final int LINE_NUMBER = 123;
  private static final int CHAR_NUMBER = 456;

  private static final ErrorReporter t = new SharedErrorReporter();

  @Override
  public void setUp() throws Exception {
    super.setUp();

    // Initialize the JSTypeRegistry
    JSTypeRegistry registry = new JSTypeRegistry(t);

    // Register a new type registry with the Rhino context
    Context cx = Context.enter();
    cx.setGlobal("MyTypeRegistry", registry);
    cx.setGlobal(REGISTRY_CLASS_NAME, registry.getClass());

    // Define the types in the registry
    registry.defineType(NAMED_TYPE_NAME, ObjectType.NULL_TYPE);
    registry.defineType(NAMED_TYPE_REFERENCE, ObjectType.NULL_TYPE);

    // Create the NamedType instance
    JSType namedType = (JSType) evalInContext(
        "new " + REGISTRY_CLASS_NAME + "[\"" + NAMED_TYPE_NAME + "\"](\"" +
            NAMED_TYPE_REFERENCE + "\", \"" + NAMED_TYPE_SOURCE_NAME + "\", " +
            LINE_NUMBER + ", " + CHAR_NUMBER + ")", cx);

    // Store the NamedType instance in the global scope for testing
    cx.setGlobal(NAMED_TYPE_NAME, namedType);

    Context.exit();
  }

  @Test
  public void testNamedTypeIsInstanceOfNamedType() {
    assertTrue(NamedType.class.isAssignableFrom(NamedType.class));
  }

  @Test
  public void testNamedTypeIsNamedType() {
    JSType namedType = (JSType) Context.getGlobal(NAMED_TYPE_NAME);
    assertTrue(namedType.isNamedType());
  }

  @Test
  public void testNamedTypeHasReferenceName() {
    JSType namedType = (JSType) Context.getGlobal(NAMED_TYPE_NAME);
    assertTrue(namedType.hasReferenceName());
  }

  @Test
  public void testNamedTypeToStringHelper() {
    JSType namedType = (JSType) Context.getGlobal(NAMED_TYPE_NAME);
    assertEquals(NAMED_TYPE_REFERENCE, namedType.toStringHelper(false));
  }

  @Test
  public void testNamedTypeIsNominalType() {
    JSType namedType = (JSType) Context.getGlobal(NAMED_TYPE_NAME);
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testNamedTypeIsResolved() {
    JSType namedType = (JSType) Context.getGlobal(NAMED_TYPE_NAME);
    assertFalse(namedType.isResolved());
  }

  @Test
  public void testNamedTypeGetReferencedType() {
    JSType namedType = (JSType) Context.getGlobal(NAMED_TYPE_NAME);
    assertEquals(ObjectType.NULL_TYPE, namedType.getReferencedType());
  }

  @Test
  public void testNamedTypeDefineProperty() {
    JSType namedType = (JSType) Context.getGlobal(NAMED_TYPE_NAME);

    // Define a property on the NamedType instance
    namedType.defineProperty("myProperty", Boolean.TYPE, false, null);

    // Verify that the property was defined
    StaticScope<JSType> enclosingScope = Context.getCurrentScope();
    StaticSlot<JSType> propertySlot = enclosingScope.getSlot("NamedType");
    JSType namedTypeInstance = propertySlot.getValue();
    assertTrue(namedTypeInstance.hasProperty("myProperty"));

    // Verify that the property is defined on the resolved NamedType instance
    JSType resolvedNamedType = namedType.resolve(null, null);
    assertTrue(resolvedNamedType.hasProperty("myProperty"));
  }

  @Test
  public void testNamedTypeGetReferencedAndResolvedType() {
    JSType namedType = (JSType) Context.getGlobal(NAMED_TYPE_NAME);

    // Define a type in the registry that the NamedType references
    JSType referencedType = ObjectType.newBoolean();
    Context.getGlobal("MyTypeRegistry").defineType("MyReferencedType", referencedType);

    // Verify that the NamedType gets the correct referenced and resolved type
    namedType.setValidator(type -> assertEquals(referencedType, type));
    namedType.resolve(null, null);

    JSType resolvedNamedType = namedType.getReferencedType();
    assertEquals(referencedType, resolvedNamedType);
  }

  @Test
  public void testNamedTypeResolveViaRegistry() {
    JSType namedType = (JSType) Context.getGlobal(NAMED_TYPE_NAME);

    // Define a type in the registry that the NamedType references
    JSType referencedType = ObjectType.newBoolean();
    Context.getGlobal("MyTypeRegistry").defineType("MyReferencedType", referencedType);

    // Verify that the NamedType resolves via the registry
    JSType resolvedNamedType = namedType.resolve(null, null);
    assertEquals(this.getClass().getSuperclass(), resolvedNamedType.getClass());
    assertEquals(referencedType, resolvedNamedType.getReferencedType());
  }

  @Test
  public void testNamedTypeResolveViaProperties() {
    JSType namedType = (JSType) Context.getGlobal(NAMED_TYPE_NAME);

    // Define a type in the registry that the NamedType references, and a property on that type
    JSType referencedType = ObjectType.newBoolean();
    Context.getGlobal("MyTypeRegistry").defineType("MyReferencedType", referencedType);
    Context.getGlobal("MyReferencedType").defineProperty("myProperty", Boolean.TYPE, false, null);

    // Verify that the NamedType resolves via properties
    JSType resolvedNamedType = namedType.resolve(null, null);
    assertEquals(this.getClass().getSuperclass(), resolvedNamedType.getClass());
    assertEquals(referencedType, resolvedNamedType.getReferencedType());
    assertTrue(resolvedNamedType.hasProperty("myProperty"));
  }

  @Test
  public void testNamedTypeResolveViaRegistryWithCycle() {
    JSType namedType = (JSType) Context.getGlobal(NAMED_TYPE_NAME);

    // Define a type in the registry that the NamedType references, and a property on that type that references the NamedType
    JSType referencedType = new NamedType(
        (JSTypeRegistry) Context.getGlobal("MyTypeRegistry"),
        NAMED_TYPE_NAME,
        NAMED_TYPE_SOURCE_NAME,
        LINE_NUMBER,
        CHAR_NUMBER);
    Context.getGlobal("MyTypeRegistry").defineType("MyReferencedType", referencedType);
    Context.getGlobal("MyReferencedType").defineProperty("myProperty", Boolean.TYPE, false, null);

    try {
      namedType.resolve(null, null);
      fail("Expected a type cycle warning");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testNamedTypeResolveViaPropertiesWithCycle() {
    JSType namedType = (JSType) Context.getGlobal(NAMED_TYPE_NAME);

    // Define a property on the NamedType instance that references itself
    namedType.defineProperty("myProperty", namedType, false, null);

    try {
      namedType.resolve(null, null);
      fail("Expected a type cycle warning");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testNamedTypeResolveWithUnresolvedType() {
    JSType namedType = (JSType) Context.getGlobal(NAMED_TYPE_NAME);

    // Define a type in the registry that the NamedType references, but don't resolve it
    Context.getGlobal("MyTypeRegistry").defineType(NAMED_TYPE_REFERENCE, ObjectType.NULL_TYPE);

    try {
      namedType.resolve(null, null);
      fail("Expected an unresolved type warning");
    } catch (AssertionError e) {
      // Expected
    }
  }
}