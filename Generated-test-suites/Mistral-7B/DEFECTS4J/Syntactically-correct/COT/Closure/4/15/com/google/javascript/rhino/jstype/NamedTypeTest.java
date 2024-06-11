import org.junit.Test;
import org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.EnumType;
import com.google.javascript.rhino.jstype.EnumElementType;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.StaticScope;

public class NamedTypeTest {

  @Test
  public void testConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "sourceName", 1, 1);
    assertEquals("String", namedType.getReferenceName());
    assertEquals("sourceName", namedType.toString());
  }

  @Test
  public void testResolveViaRegistrySuccess() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerType("Number");
    NamedType namedType = new NamedType(registry, "Number", "sourceName", 1, 1);
    JSType referencedType = namedType.getReferencedType();
    assertEquals("Number", referencedType.getTypeName());
    assertEquals("sourceName", namedType.toString());
  }

  @Test(expected = AssertionError.class)
  public void testResolveViaRegistryFailure() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "UnknownType", "sourceName", 1, 1);
    JSType referencedType = namedType.getReferencedType();
    assertNull(referencedType);
  }

  @Test
  public void testResolveViaPropertiesSuccess() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerType("Object");
    ObjectType objectType = new ObjectType(registry, "Object");
    StaticScope<JSType> enclosing = new StaticScope<>();
    enclosing.setSlot("MyType", objectType);
    NamedType namedType = new NamedType(registry, "MyType.Property", "sourceName", 1, 1);
    JSType referencedType = namedType.resolveInternal(null, enclosing);
    ObjectType referencedObjectType = ObjectType.cast(referencedType);
    assertEquals("Object", referencedObjectType.getReferencedType().getTypeName());
    assertEquals("sourceName", namedType.toString());
  }

  @Test(expected = AssertionError.class)
  public void testResolveViaPropertiesFailure() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType objectType = new ObjectType(registry, "Object");
    StaticScope<JSType> enclosing = new StaticScope<>();
    enclosing.setSlot("MyType", objectType);
    NamedType namedType = new NamedType(registry, "MyType.UnknownProperty", "sourceName", 1, 1);
    namedType.resolveInternal(null, enclosing);
  }

  @Test
  public void testIsResolved() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerType("Object");
    ObjectType objectType = new ObjectType(registry, "Object");
    StaticScope<JSType> enclosing = new StaticScope<>();
    enclosing.setSlot("MyType", objectType);
    NamedType namedType = new NamedType(registry, "MyType.Property", "sourceName", 1, 1);
    assertFalse(namedType.isResolved());
    namedType.resolveInternal(null, enclosing);
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType objectType = new ObjectType(registry, "Object");
    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, "MyType", "sourceName", 1, 1);
    namedType.defineProperty("property", registry.getType("String"), false, null);
    ObjectType referencedObjectType = ObjectType.cast(namedType.getReferencedType());
    assertEquals("Object", referencedObjectType.getTypeName());
    PropertyContinuation propertyContinuation = namedType.propertyContinuations.get(0);
    assertEquals("property", propertyContinuation.propertyName);
    assertEquals(registry.getType("String"), propertyContinuation.type);
    assertFalse(propertyContinuation.inferred);
    assertNull(propertyContinuation.propertyNode);
    namedType.finishPropertyContinuations();
    assertEquals(registry.getType("String"), referencedObjectType.getPropertyType("property"));
  }

  @Test
  public void testGetReferencedTypeInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerType("Object");
    ObjectType objectType = new ObjectType(registry, "Object");
    StaticScope<JSType> enclosing = new StaticScope<>();
    enclosing.setSlot("MyType", objectType);
    NamedType namedType = new NamedType(registry, "MyType", "sourceName", 1, 1);
    FunctionType functionType = new FunctionType(registry, null, null, null, null);
    namedType.setReferencedTypeInternal(functionType);
    FunctionType referencedFunctionType = namedType.getReferencedTypeInternal();
    assertEquals(functionType, referencedFunctionType);
  }

  @Test
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "sourceName", 1, 1);
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType type) {
        return type != null;
      }
    };
    namedType.setValidator(validator);
    JSType referencedType = namedType.getReferencedType();
    assertTrue(validator.apply(referencedType));
  }

  @Test
  public void testUnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "UnknownType", "sourceName", 1, 1);
    JSType referencedType = namedType.getReferencedType();
    assertNull(referencedType);
  }

  @Test
  public void testResolveViaPropertiesWithEnumType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerType("Object");
    ObjectType objectType = new ObjectType(registry, "Object");
    StaticScope<JSType> enclosing = new StaticScope<>();
    enclosing.setSlot("MyEnum", new EnumType(registry, "MyEnum", null));
    Enumeration enumeration = new EnumType(registry, "MyEnum", null).getElements();
    EnumElementType enumElementType = (EnumElementType) enumeration.next();
    NamedType namedType = new NamedType(registry, "MyEnum.Element", "sourceName", 1, 1);
    namedType.resolveInternal(null, enclosing);
    assertEquals(enumElementType.getPrimitiveType(), namedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerType("Object");
    ObjectType objectType = new ObjectType(registry, "Object");
    StaticScope<JSType> enclosing = new StaticScope<>();
    StaticSlot<JSType> constructorSlot = enclosing.setSlot("MyType", null);
    NamedType namedType = new NamedType(registry, "MyType.constructor", "sourceName", 1, 1);
    FunctionType functionType = new FunctionType(registry, null, null, null, null);
    constructorSlot.setType(functionType);
    namedType.resolveInternal(null, enclosing);
    FunctionType referencedFunctionType = FunctionType.cast(namedType.getReferencedType());
    assertEquals(functionType, referencedFunctionType);
  }

  @Test
  public void testResolveViaPropertiesWithInterfaceType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerType("Object");
    ObjectType objectType = new ObjectType(registry, "Object");
    StaticScope<JSType> enclosing = new StaticScope<>();
    StaticSlot<JSType> interfaceSlot = enclosing.setSlot("MyInterface", null);
    NamedType namedType = new NamedType(registry, "MyInterface", "sourceName", 1, 1);
    namedType.resolveInternal(null, enclosing);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT), namedType.getReferencedType());
  }

  @Test
  public void testResolveViaRegistryWithEnumType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerType("MyEnum");
    NamedType namedType = new NamedType(registry, "MyEnum", "sourceName", 1, 1);
    EnumType enumType = (EnumType) registry.getType("MyEnum");
    assertEquals(enumType, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaRegistryWithInterfaceType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerType("MyInterface");
    NamedType namedType = new NamedType(registry, "MyInterface", "sourceName", 1, 1);
    assertEquals(registry.getNativeObjectType(JSTypeNative.INTERFACE), namedType.getReferencedType());
  }

  @Test
  public void testResolveViaRegistryWithFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerType("MyFunctionType");
    NamedType namedType = new NamedType(registry, "MyFunctionType", "sourceName", 1, 1);
    FunctionType functionType = (FunctionType) registry.getType("MyFunctionType");
    assertEquals(functionType, namedType.getReferencedType());
  }
}