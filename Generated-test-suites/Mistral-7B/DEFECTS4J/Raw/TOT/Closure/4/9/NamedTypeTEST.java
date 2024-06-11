import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.TypeErrorReporter;
import com.google.javascript.rhino.testing.TypeStub;
import com.google.javascript.rhino.testing.Value;
import com.google.javascript.rhino.testing.asserts.PropertyAssert;

import java.util.List;

public class NamedTypeTest {

  private static final ErrorReporter t = new TypeErrorReporter();

  @org.junit.Test
  public void testNamedType() {
    // Test creating a new named type
    JSTypeRegistry registry = new JSTypeRegistry();
    String name = "MyNamedType";
    NamedType namedType = new NamedType(registry, name, "MySourceFile.java", 1, 1);
    assertEquals(name, namedType.getReferenceName());

    // Test setting a property continuation
    StaticScope<JSType> enclosing = new StaticScope<>();
    namedType.defineProperty("myProperty", BOOLEAN_TYPE, true, null);
    namedType.finishPropertyContinuations();
    ObjectType referencedObjType = namedType.getReferencedObjTypeInternal();
    assertNotNull(referencedObjType);
    assertEquals(ObjectType.class, referencedObjType.getClass());
    PropertyAssert.assertProperty(referencedObjType, "myProperty", BOOLEAN_VALUE);

    // Test getting the referenced type
    JSType referencedType = namedType.getReferencedType();
    assertNull(referencedType);

    // Test resolving via the registry
    registry.registerType(name, INT_TYPE);
    referencedType = namedType.resolveInternal(t, enclosing);
    assertNotNull(referencedType);
    assertEquals(INT_TYPE, referencedType);

    // Test resolving via properties
    registry.unregisterType(name);
    TypeStub registryType = new TypeStub(name, INT_TYPE.getPrototype());
    registry.registerType(name, registryType);
    StaticScope<JSType> enclosing2 = new StaticScope<>();
    enclosing2.setSlot(name, registryType);
    referencedType = namedType.resolveInternal(t, enclosing2);
    assertNotNull(referencedType);
    assertEquals(INT_TYPE, referencedType);

    // Test resolving a cycle
    String cycleName = "CycleType";
    registry.unregisterType(cycleName);
    registry.registerType(cycleName, new NamedType(registry, name, "CycleFile.java", 1, 1));
    NamedType cycleNamedType = new NamedType(registry, cycleName, "CycleFile.java", 1, 1);
    namedType.defineProperty(cycleName, cycleNamedType, true, null);
    namedType.finishPropertyContinuations();
    referencedType = namedType.resolveInternal(t, enclosing);
    assertNotNull(referencedType);
    assertEquals(NamedType.class, referencedType.getClass());

    // Test handling an unresolved type
    String unresolvedName = "UnresolvedType";
    registry.unregisterType(unresolvedName);
    NamedType unresolvedNamedType = new NamedType(registry, unresolvedName, "UnresolvedFile.java", 1, 1);
    namedType.defineProperty(unresolvedName, unresolvedNamedType, true, null);
    namedType.finishPropertyContinuations();
    referencedType = namedType.resolveInternal(t, enclosing);
    assertNull(referencedType);
  }

  @org.junit.Test
  public void testNamedTypeWithValidator() {
    // Test setting a validator
    JSTypeRegistry registry = new JSTypeRegistry();
    String name = "MyNamedType";
    NamedType namedType = new NamedType(registry, name, "MySourceFile.java", 1, 1);
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input.isFunctionType();
      }
    };
    namedType.setValidator(validator);
    JSType referencedType = namedType.getReferencedType();
    assertNull(referencedType);

    // Test validating a function type
    registry.registerType(name, FunctionType.create(INT_TYPE, VOID_TYPE));
    referencedType = namedType.resolveInternal(t, new StaticScope<JSType>());
    assertNotNull(referencedType);
    assertTrue(validator.apply(referencedType));

    // Test validating a non-function type
    registry.registerType(name, INT_TYPE);
    referencedType = namedType.resolveInternal(t, new StaticScope<JSType>());
    assertNotNull(referencedType);
    assertFalse(validator.apply(referencedType));
  }

  @org.junit.Test
  public void testNamedTypeWithImplicitPrototypeCycle() {
    // Test handling an implicit prototype cycle
    String cycleName = "CycleType";
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, cycleName, "CycleFile.java", 1, 1);
    namedType.defineProperty(cycleName, new NamedType(registry, cycleName, "CycleFile.java", 1, 1), true, null);
    namedType.finishPropertyContinuations();
    try {
      namedType.resolveInternal(t, new StaticScope<JSType>());
      fail("Expected a type cycle warning");
    } catch (AssertionError e) {
      String expectedMessage = "Cycle detected in inheritance chain of type " + cycleName;
      String actualMessage = e.getMessage();
      assertTrue(actualMessage.startsWith(expectedMessage));
    }
  }

  @org.junit.Test
  public void testNamedTypeWithEnumElementCycle() {
    // Test handling an enum element cycle
    String cycleName = "CycleEnum";
    String enumName = "CycleEnumElement";
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, cycleName, "CycleFile.java", 1, 1);
    namedType.defineProperty(cycleName, new NamedType(registry, cycleName, "CycleFile.java", 1, 1), true, null);
    JSType enumType = EnumType.create("CycleEnum", INT_TYPE);
    namedType.defineProperty(enumName, Value.create(0), true, null);
    namedType.defineProperty(enumName, namedType, true, null);
    namedType.finishPropertyContinuations();
    try {
      namedType.resolveInternal(t, new StaticScope<JSType>());
      fail("Expected an enum element cycle warning");
    } catch (AssertionError e) {
      String expectedMessage = "Cycle detected in inheritance chain of type " + cycleName + ".";
      String actualMessage = e.getMessage();
      assertTrue(actualMessage.startsWith(expectedMessage));
    }
  }

  @org.junit.Test
  public void testNamedTypeWithForwardDeclaration() {
    // Test handling a forward-declared type
    String forwardName = "ForwardType";
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, forwardName, "ForwardFile.java", 1, 1);
    registry.registerForwardDeclaration(forwardName, INT_TYPE);
    JSType referencedType = namedType.getReferencedType();
    assertNull(referencedType);

    // Test setting a property continuation with a forward-declared type
    StaticScope<JSType> enclosing = new StaticScope<>();
    namedType.defineProperty("myProperty", BOOLEAN_TYPE, true, null);
    namedType.finishPropertyContinuations();
    ObjectType referencedObjType = namedType.getReferencedObjTypeInternal();
    assertNotNull(referencedObjType);
    assertEquals(ObjectType.class, referencedObjType.getClass());
    PropertyAssert.assertProperty(referencedObjType, "myProperty", BOOLEAN_VALUE);

    // Test resolving via the registry
    registry.registerType(forwardName, INT_TYPE);
    referencedType = namedType.resolveInternal(t, enclosing);
    assertNotNull(referencedType);
    assertEquals(INT_TYPE, referencedType);
  }
}