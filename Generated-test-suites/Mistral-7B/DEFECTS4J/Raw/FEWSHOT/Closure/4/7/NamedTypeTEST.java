package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.parser.util.Position;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.SymbolTable;
import com.google.javascript.rhino.SymbolTable.Slot;
import com.google.javascript.rhino.Type;
import com.google.javascript.rhino.jsutil.Nullable;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.ProxiedType;
import com.google.javascript.rhino.jstype.ProxyObjectType;
import com.google.javascript.rhino.jstype.TypeFactory;
import com.google.javascript.rhino.jstype.TypeRegistry;

@RunWith(JUnit4.class)
public class NamedTypeTest {

  @Test
  public void testResolveViaRegistry() {
    // Create a dummy type registry with a single named type.
    TypeRegistry registry = TypeFactory.createRegistry();
    JSType type = TypeFactory.createEnumType("MyEnum", registry);
    registry.registerType("MyEnumType", type);

    // Create a named type that refers to the type registered above.
    String reference = "MyEnumType";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    // Verify that the type is unresolved initially.
    assertNull(namedType.getReferencedType());

    // Resolve the type via the registry.
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    JSType resolvedType = namedType.resolve(t, enclosing);

    // Verify that the type was resolved successfully.
    assertNotNull(resolvedType);
    assertEquals(type, resolvedType);
  }

  @Test
  public void testResolveViaProperties() {
    // Create a dummy type registry with a single top-level object type.
    TypeRegistry registry = TypeFactory.createRegistry();
    JSType objectType = TypeFactory.createObjectType("MyObject", registry);

    // Create a symbol table with a property named "MyEnum" on the object type.
    SymbolTable symbolTable = new SymbolTable();
    Slot slot = symbolTable.addSlot("MyEnum", null, objectType);

    // Create a named type that refers to the property on the object type.
    String reference = "MyObject.MyEnum";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    // Create a static scope with a reference to the object type.
    StaticScope<JSType> enclosing = new StaticScope<>();
    enclosing.setSlot("MyObject", ObjectType.cast(objectType));

    // Verify that the type is unresolved initially.
    assertNull(namedType.getReferencedType());

    // Resolve the type via properties.
    ErrorReporter t = new ErrorReporter();
    JSType resolvedType = namedType.resolve(t, enclosing);

    // Verify that the type was resolved successfully.
    assertNotNull(resolvedType);
    assertEquals(registry.getType("MyEnum"), resolvedType);
  }

  @Test
  public void testResolveWithCycle() {
    // Create a dummy type registry with two circularly referencing named types.
    TypeRegistry registry = TypeFactory.createRegistry();
    JSType type1 = TypeFactory.createObjectType("Type1", registry);
    JSType type2 = TypeFactory.createObjectType("Type2", registry);
    registry.registerType("Type1", type1);
    registry.registerType("Type2", type2);
    type1.setPropertyType("myProperty", TypeFactory.createNamedType("Type2", registry));
    type2.setPropertyType("myProperty", TypeFactory.createNamedType("Type1", registry));

    // Create a named type that refers to one of the circularly referencing types.
    String reference = "Type1";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    // Create a static scope with references to both circularly referencing types.
    StaticScope<JSType> enclosing = new StaticScope<>();
    enclosing.setSlot("Type1", ObjectType.cast(type1));
    enclosing.setSlot("Type2", ObjectType.cast(type2));

    // Verify that the type is unresolved initially.
    assertNull(namedType.getReferencedType());

    // Attempt to resolve the type.
    ErrorReporter t = new ErrorReporter();
    JSType resolvedType = namedType.resolve(t, enclosing);

    // Verify that a type cycle warning was issued.
    assertEquals(JSTypeNative.UNKNOWN_TYPE, resolvedType);
    assertEquals(1, t.getNumWarns());
  }

  @Test
  public void testResolveWithInvalidReference() {
    // Create a dummy type registry with a single object type.
    TypeRegistry registry = TypeFactory.createRegistry();
    JSType objectType = TypeFactory.createObjectType("MyObject", registry);

    // Create a named type that refers to an invalid reference.
    String reference = "InvalidType";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    // Create a static scope with no references.
    StaticScope<JSType> enclosing = new StaticScope<>();

    // Verify that the type is unresolved initially.
    assertNull(namedType.getReferencedType());

    // Attempt to resolve the type.
    ErrorReporter t = new ErrorReporter();
    JSType resolvedType = namedType.resolve(t, enclosing);

    // Verify that a warning was issued for the unknown type reference.
    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, resolvedType);
    assertEquals(1, t.getNumWarns());
  }

  @Test
  public void testDefineProperty() {
    // Create a dummy type registry with a single object type.
    TypeRegistry registry = TypeFactory.createRegistry();
    JSType objectType = TypeFactory.createObjectType("MyObject", registry);

    // Create a named type that refers to the object type.
    String reference = "MyObject";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    // Define a property on the named type.
    String propertyName = "myProperty";
    JSType propertyType = TypeFactory.createStringType();
    namedType.defineProperty(propertyName, propertyType, false, null);

    // Verify that the property was defined.
    ObjectType targetType = ObjectType.cast(namedType);
    assertEquals(propertyType, targetType.getPropertyType(propertyName));
  }

  @Test
  public void testPropertyContinuation() {
    // Create a dummy type registry with a single object type.
    TypeRegistry registry = TypeFactory.createRegistry();
    JSType objectType = TypeFactory.createObjectType("MyObject", registry);

    // Create a named type that refers to the object type.
    String reference = "MyObject";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    // Define a property continuation on the named type.
    String propertyName = "myProperty";
    JSType propertyType = TypeFactory.createStringType();
    Node propertyNode = new Node();
    namedType.defineProperty(propertyName, propertyType, false, propertyNode);

    // Verify that the property continuation was saved.
    List<NamedType.PropertyContinuation> propertyContinuations = namedType.getPropertyContinuations();
    assertEquals(1, propertyContinuations.size());
    NamedType.PropertyContinuation continuation = propertyContinuations.get(0);
    assertEquals(propertyName, continuation.getPropertyName());
    assertEquals(propertyType, continuation.getPropertyType());
    assertEquals(propertyNode, continuation.getPropertyNode());

    // Finish defining the property.
    namedType.finishPropertyContinuations();

    // Verify that the property was defined.
    ObjectType targetType = ObjectType.cast(namedType);
    assertEquals(propertyType, targetType.getPropertyType(propertyName));
  }

  @Test
  public void testSetValidator() {
    // Create a dummy type registry with a single object type.
    TypeRegistry registry = TypeFactory.createRegistry();
    JSType objectType = TypeFactory.createObjectType("MyObject", registry);

    // Create a named type that refers to the object type.
    String reference = "MyObject";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    // Set a validator for the named type.
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof ObjectType;
      }
    };
    namedType.setValidator(validator);

    // Verify that the validator was set.
    assertEquals(validator, namedType.getValidator());
  }
}