import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.Assertions.*;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.TestConfiguration;
import com.google.javascript.rhino.testing.TypeChecker;
import com.google.javascript.rhino.testing.Value;

import java.util.List;

public class NamedTypeTest {

  private static final ErrorReporter t = new ErrorReporter();
  private static final TestConfiguration config = new TestConfiguration();
  private static final TypeChecker tc = new TypeChecker(config, t);

  @org.junit.BeforeClass
  public static void setUp() {
    tc.setUp();
  }

  @org.junit.AfterClass
  public static void tearDown() {
    tc.tearDown();
  }

  @org.junit.Test
  public void testNamedTypeBasicResolution() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    String sourceName = "MySource";
    int lineno = 1;
    int charno = 1;

    JSType myType = new JSTypeNative.ObjectType(registry, "MyType");

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Resolution via registry.
    JSType resolvedType1 = namedType.resolve(config, null);
    assertEquals(myType, resolvedType1);

    // Resolution via properties.
    StaticScope<JSType> scope = new StaticScope<>(registry);
    StaticSlot<JSType> slot = scope.put(reference, myType);
    JSType resolvedType2 = namedType.resolve(config, scope);
    assertEquals(myType, resolvedType2);
  }

  @org.junit.Test
  public void testNamedTypeUnresolvedViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    String sourceName = "MySource";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Resolution via registry fails.
    JSType resolvedType = namedType.resolve(config, null);
    assertNull(resolvedType);
  }

  @org.junit.Test
  public void testNamedTypeUnresolvedViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    String sourceName = "MySource";
    int lineno = 1;
    int charno = 1;

    // Create an unresolved type.
    JSType unresolvedType = new JSTypeNative.ObjectType(registry, "UnresolvedType");

    // Create a scope with the unresolved type.
    StaticScope<JSType> scope = new StaticScope<>(registry);
    StaticSlot<JSType> slot = scope.put("UnresolvedType", unresolvedType);

    String reference2 = "MyType";
    int lineno2 = 1;
    int charno2 = 1;

    NamedType namedType = new NamedType(registry, reference2, sourceName, lineno2, charno2);

    // Resolution via properties fails.
    JSType resolvedType = namedType.resolve(config, scope);
    assertNull(resolvedType);
  }

  @org.junit.Test
  public void testNamedTypeCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "CyclicType";
    String sourceName = "MySource";
    int lineno = 1;
    int charno = 1;

    // Create a cyclic type.
    JSType cyclicType1 = new JSTypeNative.ObjectType(registry, "CyclicType");
    JSType cyclicType2 = new JSTypeNative.ObjectType(registry, "CyclicType");
    ((ObjectType) cyclicType1).setPrototypeOf(cyclicType2);

    // Create a named type with the cyclic reference.
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Resolution fails with a cycle warning.
    JSType resolvedType = namedType.resolve(config, null);
    assertNull(resolvedType);

    // Verify the warning was emitted.
    List<String> warnings = tc.getErrors(config);
    assertEquals(1, warnings.size());
    assertEquals("Cycle detected in inheritance chain of type " + reference, warnings.get(0));
  }

  @org.junit.Test
  public void testNamedTypeForwardDeclaration() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    String sourceName = "MySource";
    int lineno = 1;
    int charno = 1;

    // Register the forward declaration.
    registry.registerForwardDeclaredType(reference);

    JSType myType = new JSTypeNative.ObjectType(registry, "MyType");

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Resolution via registry and properties succeeds.
    JSType resolvedType1 = namedType.resolve(config, null);
    assertEquals(myType, resolvedType1);

    StaticScope<JSType> scope = new StaticScope<>(registry);
    StaticSlot<JSType> slot = scope.put(reference, myType);
    JSType resolvedType2 = namedType.resolve(config, scope);
    assertEquals(myType, resolvedType2);
  }

  @org.junit.Test
  public void testNamedTypeUnresolvedWithValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    String sourceName = "MySource";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Set a validator that checks if the type is an EnumType.
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof EnumType;
      }
    };

    namedType.setValidator(validator);

    // Resolution fails because the type is not an EnumType.
    JSType resolvedType = namedType.resolve(config, null);
    assertNull(resolvedType);
  }

  @org.junit.Test
  public void testNamedTypeUnresolvedWithMultipleValidators() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    String sourceName = "MySource";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Set validators that check if the type is an EnumType or a FunctionType.
    List<Predicate<JSType>> validators = Lists.newArrayList();
    validators.add(new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof EnumType;
      }
    });
    validators.add(new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof FunctionType;
      }
    });

    namedType.setValidator(validators);

    // Resolution fails because the type does not match any of the validators.
    JSType resolvedType = namedType.resolve(config, null);
    assertNull(resolvedType);
  }

  @org.junit.Test
  public void testNamedTypeUnresolvedWithNoValidators() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    String sourceName = "MySource";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Resolution fails without any validators.
    JSType resolvedType = namedType.resolve(config, null);
    assertNull(resolvedType);
  }

  @org.junit.Test
  public void testNamedTypeResolvedWithValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "EnumType";
    String sourceName = "MySource";
    int lineno = 1;
    int charno = 1;

    EnumType enumType = new EnumType(registry, "MyEnum", EnumType.Type.INT);

    JSType nativeFunctionTypeInstanceType = registry.getNativeFunctionType(
        JSTypeNative.NO_OBJECT_TYPE).getInstanceType();

    FunctionType functionType = new FunctionType(registry, null, null, null, null, null, null, null, null, null, null, null, null, nativeFunctionTypeInstanceType, null);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Set a validator that checks if the type is an EnumType or a FunctionType.
    List<Predicate<JSType>> validators = Lists.newArrayList();
    validators.add(new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof EnumType;
      }
    });
    validators.add(new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof FunctionType;
      }
    });

    namedType.setValidator(validators);

    // Resolution succeeds because the type matches one of the validators.
    JSType resolvedType1 = namedType.resolve(config, null);
    assertTrue(resolvedType1 instanceof EnumType || resolvedType1 instanceof FunctionType);

    // Resolution via properties also succeeds.
    StaticScope<JSType> scope = new StaticScope<>(registry);
    StaticSlot<JSType> enumSlot = scope.put("EnumType", enumType);
    StaticSlot<JSType> functionSlot = scope.put("FunctionType", functionType);

    JSType resolvedType2 = namedType.resolve(config, scope);
    assertTrue(resolvedType2 instanceof EnumType || resolvedType2 instanceof FunctionType);
  }

  @org.junit.Test
  public void testNamedTypeResolvedWithEnumType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyEnum";
    String sourceName = "MySource";
    int lineno = 1;
    int charno = 1;

    EnumType enumType = new EnumType(registry, "MyEnum", EnumType.Type.INT);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Resolution via registry succeeds.
    JSType resolvedType1 = namedType.resolve(config, null);
    assertEquals(enumType, resolvedType1);

    // Resolution via properties also succeeds.
    StaticScope<JSType> scope = new StaticScope<>(registry);
    StaticSlot<JSType> slot = scope.put(reference, enumType);
    JSType resolvedType2 = namedType.resolve(config, scope);
    assertEquals(enumType, resolvedType2);
  }

  @org.junit.Test
  public void testNamedTypeResolvedWithFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyFunctionType";
    String sourceName = "MySource";
    int lineno = 1;
    int charno = 1;

    FunctionType functionType = new FunctionType(registry, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Resolution via registry succeeds.
    JSType resolvedType1 = namedType.resolve(config, null);
    assertEquals(functionType, resolvedType1);

    // Resolution via properties also succeeds.
    StaticScope<JSType> scope = new StaticScope<>(registry);
    StaticSlot<JSType> slot = scope.put(reference, functionType);
    JSType resolvedType2 = namedType.resolve(config, scope);
    assertEquals(functionType, resolvedType2);
  }
}