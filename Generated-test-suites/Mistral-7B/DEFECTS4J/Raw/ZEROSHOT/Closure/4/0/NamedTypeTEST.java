import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeChecker.checkType;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.Context;
import com.google.javascript.rhino.testing.PolymerTestBase;
import com.google.javascript.rhino.testing.Types;

import java.util.List;

public class NamedTypeTest extends PolymerTestBase {

  private static final String REFERENCE = "MyType";

  private Context context;
  private ErrorReporter errorReporter;
  private JSTypeRegistry registry;
  private StaticScope<JSType> scope;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    context = new Context(this);
    errorReporter = context.enterWorld();
    registry = new JSTypeRegistry();
    scope = new StaticScope<>(registry);
  }

  @Override
  public void tearDown() throws Exception {
    scope = null;
    registry = null;
    errorReporter = null;
    context.exitWorld();
    context = null;
    super.tearDown();
  }

  @org.junit.Test
  public void testNamedType_Basic() {
    JSType namedType = new NamedType(registry, REFERENCE, "test", 1, 1);
    assertEquals(REFERENCE, namedType.getReferenceName());
    assertFalse(namedType.isResolved());
  }

  @org.junit.Test
  public void testNamedType_ResolveViaRegistry() {
    JSType type1 = Types.INT;
    registry.defineType(REFERENCE, type1);
    JSType namedType = new NamedType(registry, REFERENCE, "test", 1, 1);
    JSType resolvedType = namedType.resolveInternal(errorReporter, scope);
    assertEquals(type1, resolvedType);
  }

  @org.junit.Test
  public void testNamedType_ResolveViaProperties_UnknownType() {
    JSType parentType = Types.OBJECT;
    StaticSlot<JSType> slot = scope.defineSlot("parent", parentType);
    JSType namedType = new NamedType(registry, "child", "test", 1, 1);
    assertFalse(namedType.isResolved());
    namedType.resolveInternal(errorReporter, scope);
    assertFalse(namedType.isResolved());
    ObjectType parentObjectType = ObjectType.cast(parentType);
    assertEquals(parentObjectType.getPropertyType("child"), namedType.getReferencedType());
  }

  @org.junit.Test
  public void testNamedType_ResolveViaProperties_FunctionType() {
    JSType parentType = Types.OBJECT;
    StaticSlot<JSType> slot = scope.defineSlot("parent", parentType);
    JSType functionType = Types.FUNCTION_TYPE;
    StaticSlot<JSType> slotFunctionType = scope.defineSlot("functionType", functionType);
    StaticSlot<JSType> slotFunctionInstanceType =
        scope.defineSlot("functionInstanceType", functionType);
    JSType namedType = new NamedType(registry, "child", "test", 1, 1);
    assertFalse(namedType.isResolved());
    ObjectType parentObjectType = ObjectType.cast(parentType);
    parentObjectType.defineProperty("child", Types.OBJECT, false, null);
    parentObjectType.getSlot("child").setType(functionType);
    FunctionType functionTypeInstance = functionType.getInstanceType();
    parentObjectType.defineProperty(
        "functionInstance", functionTypeInstance, false, null);
    namedType.resolveInternal(errorReporter, scope);
    assertTrue(namedType.isResolved());
    assertEquals(functionTypeInstance, namedType.getReferencedType());
  }

  @org.junit.Test
  public void testNamedType_ResolveViaProperties_EnumType() {
    JSType parentType = Types.OBJECT;
    StaticSlot<JSType> slot = scope.defineSlot("parent", parentType);
    JSType enumType = new EnumType(null, null, "MyEnum", null, Types.OBJECT);
    StaticSlot<JSType> slotEnumType = scope.defineSlot("enumType", enumType);
    StaticSlot<JSType> slotEnumElementType =
        scope.defineSlot("enumElementType", enumType.getElementsType());
    JSType namedType = new NamedType(registry, "MyEnumElement", "test", 1, 1);
    assertFalse(namedType.isResolved());
    ObjectType parentObjectType = ObjectType.cast(parentType);
    parentObjectType.defineProperty("MyEnum", enumType, false, null);
    parentObjectType.getSlot("MyEnum").defineProperty(
        "MyEnumElement", Types.ANY, false, null);
    namedType.resolveInternal(errorReporter, scope);
    assertTrue(namedType.isResolved());
    assertEquals(enumElementType, namedType.getReferencedType());
  }

  @org.junit.Test
  public void testNamedType_ResolveViaProperties_MultipleComponents() {
    JSType parentType = Types.OBJECT;
    StaticSlot<JSType> slot = scope.defineSlot("parent", parentType);
    StaticSlot<JSType> slot1 =
        scope.defineSlot("parent1", Types.OBJECT);
    StaticSlot<JSType> slot2 =
        scope.defineSlot("parent2", Types.OBJECT);
    JSType namedType = new NamedType(registry, "child", "test", 1, 1);
    assertFalse(namedType.isResolved());
    ObjectType parentObjectType = ObjectType.cast(parentType);
    parentObjectType.defineProperty("parent1", slot1.getType(), false, null);
    parentObjectType.getSlot("parent1").defineProperty(
        "parent2", slot2.getType(), false, null);
    parentObjectType.getSlot("parent2").defineProperty(
        "child", Types.OBJECT, false, null);
    namedType.resolveInternal(errorReporter, scope);
    assertTrue(namedType.isResolved());
    assertEquals(Types.OBJECT, namedType.getReferencedType());
  }

  @org.junit.Test
  public void testNamedType_ResolveViaProperties_UnknownType_NoObjectType() {
    JSType parentType = Types.OBJECT;
    StaticSlot<JSType> slot = scope.defineSlot("parent", parentType);
    JSType namedType = new NamedType(registry, "child", "test", 1, 1);
    assertFalse(namedType.isResolved());
    ObjectType parentObjectType = ObjectType.cast(parentType);
    parentObjectType.defineProperty("child", Types.NO_OBJECT_TYPE, false, null);
    namedType.resolveInternal(errorReporter, scope);
    assertEquals(Types.NO_OBJECT_TYPE.getInstanceType(), namedType.getReferencedType());
  }

  @org.junit.Test
  public void testNamedType_ResolveViaProperties_UnknownType_FunctionType() {
    JSType parentType = Types.OBJECT;
    StaticSlot<JSType> slot = scope.defineSlot("parent", parentType);
    JSType functionType = Types.FUNCTION_TYPE;
    StaticSlot<JSType> slotFunctionType = scope.defineSlot("functionType", functionType);
    JSType namedType = new NamedType(registry, "child", "test", 1, 1);
    assertFalse(namedType.isResolved());
    ObjectType parentObjectType = ObjectType.cast(parentType);
    parentObjectType.defineProperty("child", functionType, false, null);
    namedType.resolveInternal(errorReporter, scope);
    assertEquals(Types.NO_FUNCTION_TYPE.getInstanceType(), namedType.getReferencedType());
  }

  @org.junit.Test
  public void testNamedType_ResolveViaProperties_UnknownType_EnumType() {
    JSType parentType = Types.OBJECT;
    StaticSlot<JSType> slot = scope.defineSlot("parent", parentType);
    JSType enumType = new EnumType(null, null, "MyEnum", null, Types.OBJECT);
    StaticSlot<JSType> slotEnumType = scope.defineSlot("enumType", enumType);
    JSType namedType = new NamedType(registry, "MyEnumElement", "test", 1, 1);
    assertFalse(namedType.isResolved());
    ObjectType parentObjectType = ObjectType.cast(parentType);
    parentObjectType.defineProperty("MyEnum", enumType, false, null);
    namedType.resolveInternal(errorReporter, scope);
    assertEquals(Types.NO_ENUM_TYPE.getInstanceType(), namedType.getReferencedType());
  }

  @org.junit.Test
  public void testNamedType_SetValidator() {
    JSTypeNamedType namedType = new JSTypeNamedType(registry, "MyType", "test", 1, 1);
    Predicate<JSType> validator = x -> x instanceof FunctionType;
    namedType.setValidator(validator);
    assertFalse(namedType.isResolved());
    JSType referencedType = namedType.getReferencedType();
    assertFalse(validator.apply(referencedType));
    namedType.resolveInternal(errorReporter, scope);
    assertTrue(namedType.isResolved());
    JSType resolvedType = namedType.getReferencedType();
    assertTrue(validator.apply(resolvedType));
  }
}