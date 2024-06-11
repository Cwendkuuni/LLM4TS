import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.LoggingErrorReporter;
import com.google.javascript.rhino.testing.TestBase;
import com.google.javascript.rhino.testing.type.JSTypeFactory;
import com.google.javascript.rhino.testing.type.JSTypeRegistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NamedTypeTest extends TestBase {

  private static final String NAMED_TYPE_REFERENCE = "MyNamedType";
  private static final String NAMED_TYPE_SOURCE_NAME = "TestSource";

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    JSTypeRegistry registry = new JSTypeRegistry();
    registry.addType(NAMED_TYPE_REFERENCE, JSTypeFactory.objectType(registry));

    JSTypeFactory factory = new JSTypeFactory(registry);
    JSTypeRegistry typeRegistry = (JSTypeRegistry) registry;
    typeRegistry.setFactory(factory);

    errorReporter = new LoggingErrorReporter();

    topScope = new StaticScope<JSType>(null, null, null, null, null, errorReporter);
  }

  public void testResolveViaRegistry() {
    JSType type = new NamedType(typeRegistry, NAMED_TYPE_REFERENCE,
        NAMED_TYPE_SOURCE_NAME, 1, 1).resolve(topScope, null);
    assertEquals(JSTypeNative.OBJECT_TYPE, type.getTag());
  }

  public void testResolveViaProperties() {
    ObjectType parentType = JSTypeFactory.objectType(typeRegistry);
    parentType.defineProperty("childProperty", JSTypeFactory.booleanType(typeRegistry), false, null);
    StaticScope<JSType> parentScope = new StaticScope<JSType>(parentType, null, null, null, null, errorReporter);

    JSType childType = new NamedType(typeRegistry, "parentType.childProperty",
        NAMED_TYPE_SOURCE_NAME, 1, 1).resolve(parentScope, topScope);
    assertEquals(JSTypeNative.BOOLEAN_TYPE, childType.getTag());
  }

  public void testResolveViaRegistryWithUnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeFactory factory = new JSTypeFactory(registry);
    JSTypeRegistry typeRegistry = (JSTypeRegistry) registry;
    typeRegistry.setFactory(factory);

    errorReporter = new LoggingErrorReporter();

    topScope = new StaticScope<JSType>(null, null, null, null, null, errorReporter);

    JSType unresolvedChildType = new NamedType(typeRegistry, "unresolvedType.childProperty",
        NAMED_TYPE_SOURCE_NAME, 1, 1).resolve(topScope, null);
    assertNull(unresolvedChildType);

    // Verify no errors were reported
    assertEquals(0, errorReporter.getErrorCount());
  }

  public void testResolveViaPropertiesWithUnresolvedType() {
    ObjectType parentType = JSTypeFactory.objectType(typeRegistry);
    parentType.defineProperty("childProperty", JSTypeFactory.booleanType(typeRegistry), false, null);
    StaticScope<JSType> parentScope = new StaticScope<JSType>(parentType, null, null, null, null, errorReporter);

    JSType unresolvedChildType = new NamedType(typeRegistry, "unresolvedType.childProperty",
        NAMED_TYPE_SOURCE_NAME, 1, 1).resolve(parentScope, topScope);
    assertNull(unresolvedChildType);

    // Verify no errors were reported
    assertEquals(0, errorReporter.getErrorCount());
  }

  public void testResolveViaRegistryWithUnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeFactory factory = new JSTypeFactory(registry);
    JSTypeRegistry typeRegistry = (JSTypeRegistry) registry;
    typeRegistry.setFactory(factory);

    errorReporter = new LoggingErrorReporter();

    topScope = new StaticScope<JSType>(null, null, null, null, null, errorReporter);

    JSType unknownChildType = new NamedType(typeRegistry, "unknownType.childProperty",
        NAMED_TYPE_SOURCE_NAME, 1, 1).resolve(topScope, null);
    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, unknownChildType);

    // Verify no errors were reported
    assertEquals(0, errorReporter.getErrorCount());
  }

  public void testResolveViaPropertiesWithUnknownType() {
    ObjectType parentType = JSTypeFactory.objectType(typeRegistry);
    parentType.defineProperty("childProperty", JSTypeFactory.booleanType(typeRegistry), false, null);
    StaticScope<JSType> parentScope = new StaticScope<JSType>(parentType, null, null, null, null, errorReporter);

    JSType unknownChildType = new NamedType(typeRegistry, "unknownType.childProperty",
        NAMED_TYPE_SOURCE_NAME, 1, 1).resolve(parentScope, topScope);
    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, unknownChildType);

    // Verify no errors were reported
    assertEquals(0, errorReporter.getErrorCount());
  }

  public void testResolveWithCycle() {
    ObjectType childType = JSTypeFactory.objectType(typeRegistry);
    ObjectType parentType = JSTypeFactory.objectType(typeRegistry);

    childType.defineProperty("childProperty", childType, false, null);
    parentType.defineProperty("parentProperty", parentType, false, null);
    parentType.defineProperty("grandchildProperty", childType, false, null);

    StaticScope<JSType> parentScope = new StaticScope<JSType>(parentType, null, null, null, null, errorReporter);
    StaticScope<JSType> grandparentScope = new StaticScope<JSType>(parentType, null, null, null, null, errorReporter);

    JSType type = new NamedType(typeRegistry, "grandparentProperty.grandchildProperty",
        NAMED_TYPE_SOURCE_NAME, 1, 1).resolve(grandparentScope, parentScope);

    assertEquals(JSTypeNative.UNKNOWN_TYPE, type);

    // Verify no errors were reported
    assertEquals(0, errorReporter.getErrorCount());
  }
}