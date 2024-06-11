package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.TernaryValue.UNKNOWN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.symbol.JSDynamicSymbol;
import com.google.javascript.rhino.symbol.JSFunctionSymbol;
import com.google.javascript.rhino.symbol.JSObjectSymbol;
import com.google.javascript.rhino.symbol.JSPropertySymbol;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class UnionTypeTest {
  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> elements = new HashSet<JSType>();
    elements.add(registry.getTypeRegistry().getObjectType());
    elements.add(registry.getTypeRegistry().getBooleanType());

    UnionType unionType = new UnionType(registry, elements);
    assertEquals(elements, unionType.getAlternates());
  }

  @Test
  public void testUnionTypeGetters() {
    Set<JSType> elements = new HashSet<JSType>();
    elements.add(registry.getTypeRegistry().getStringType());
    elements.add(registry.getTypeRegistry().getBooleanType());

    UnionType unionType = new UnionType(registry, elements);
    assertEquals(elements, unionType.getAlternates());

    assertFalse(unionType.matchesNumberContext());
    assertFalse(unionType.matchesStringContext());
    assertFalse(unionType.matchesObjectContext());

    assertNull(unionType.findPropertyType("toString"));

    assertFalse(unionType.canAssignTo(registry.getTypeRegistry().getNumberType()));

    // Can't call a union type
    assertFalse(unionType.canBeCalled());

    // Nested union types are not allowed
    try {
      new UnionType(registry, ImmutableSet.of(new UnionType(registry, Arrays.asList(registry.getTypeRegistry().getObjectType(), registry.getTypeRegistry().getBooleanType()))));
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    assertEquals(UNKNOWN, unionType.testForEquality(registry.getTypeRegistry().getBooleanType()));
  }

  @Test
  public void testUnionTypeIsSubtype() {
    Set<JSType> elements1 = new HashSet<JSType>();
    elements1.add(registry.getTypeRegistry().getStringType());
    elements1.add(registry.getTypeRegistry().getBooleanType());

    UnionType unionType1 = new UnionType(registry, elements1);
    Set<JSType> elements2 = new HashSet<JSType>();
    elements2.add(registry.getTypeRegistry().getStringType());
    elements2.add(registry.getTypeRegistry().getNumberType());

    UnionType unionType2 = new UnionType(registry, elements2);

    assertTrue(unionType1.isSubtype(unionType1));
    assertTrue(unionType1.isSubtype(registry.getTypeRegistry().getStringType()));
    assertTrue(unionType1.isSubtype(registry.getTypeRegistry().getBooleanType()));
    assertFalse(unionType1.isSubtype(registry.getTypeRegistry().getNumberType()));
    assertFalse(unionType1.isSubtype(unionType2));
  }

  @Test
  public void testUnionTypeToString() {
    Set<JSType> elements = new HashSet<JSType>();
    elements.add(registry.getTypeRegistry().getStringType());
    elements.add(registry.getTypeRegistry().getBooleanType());

    UnionType unionType = new UnionType(registry, elements);
    assertEquals("(String|Boolean)", unionType.toString());
  }

  @Test
  public void testUnionTypeMeet() {
    Set<JSType> elements1 = new HashSet<JSType>();
    elements1.add(registry.getTypeRegistry().getStringType());
    elements1.add(registry.getTypeRegistry().getBooleanType());

    UnionType unionType1 = new UnionType(registry, elements1);

    Set<JSType> elements2 = new HashSet<JSType>();
    elements2.add(registry.getTypeRegistry().getStringType());
    elements2.add(registry.getTypeRegistry().getNumberType());

    UnionType unionType2 = new UnionType(registry, elements2);

    UnionType meetType = unionType1.meet(unionType2);
    assertNull(meetType);

    Set<JSType> elements3 = new HashSet<JSType>();
    elements3.addAll(elements1);
    elements3.addAll(elements2);

    UnionType meetTypeExpected = new UnionType(registry, elements3);
    assertEquals(meetTypeExpected, meetType);
  }

  @Test
  public void testUnionTypeEquals() {
    Set<JSType> elements1 = new HashSet<JSType>();
    elements1.add(registry.getTypeRegistry().getStringType());
    elements1.add(registry.getTypeRegistry().getBooleanType());

    UnionType unionType1 = new UnionType(registry, elements1);

    Set<JSType> elements2 = new HashSet<JSType>();
    elements2.add(registry.getTypeRegistry().getStringType());
    elements2.add(registry.getTypeRegistry().getBooleanType());

    UnionType unionType2 = new UnionType(registry, elements2);

    assertTrue(unionType1.equals(unionType1));
    assertTrue(unionType1.equals(unionType2));
    assertFalse(unionType1.equals("not a UnionType"));
  }

  @Test
  public void testUnionTypeHashCode() {
    Set<JSType> elements = new HashSet<JSType>();
    elements.add(registry.getTypeRegistry().getStringType());
    elements.add(registry.getTypeRegistry().getBooleanType());

    UnionType unionType = new UnionType(registry, elements);

    UnionType unionType2 = new UnionType(registry, elements);

    assertEquals(unionType.hashCode(), unionType2.hashCode());
  }

  @Test
  public void testUnionTypeIsUnionType() {
    Set<JSType> elements = new HashSet<JSType>();
    elements.add(registry.getTypeRegistry().getStringType());
    elements.add(registry.getTypeRegistry().getBooleanType());

    UnionType unionType = new UnionType(registry, elements);

    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testUnionTypeIsObject() {
    Set<JSType> elements = new HashSet<JSType>();
    elements.add(registry.getTypeRegistry().getStringType());
    elements.add(registry.getTypeRegistry().getBooleanType());

    UnionType unionType = new UnionType(registry, elements);

    assertTrue(unionType.isObject());
  }

  @Test
  public void testUnionTypeContains() {
    Set<JSType> elements = new HashSet<JSType>();
    elements.add(registry.getTypeRegistry().getStringType());
    elements.add(registry.getTypeRegistry().getBooleanType());

    UnionType unionType = new UnionType(registry, elements);

    assertTrue(unionType.contains(registry.getTypeRegistry().getStringType()));
    assertTrue(unionType.contains(registry.getTypeRegistry().getBooleanType()));
    assertFalse(unionType.contains(registry.getTypeRegistry().getNumberType()));
  }

  @Test
  public void testUnionTypeGetRestrictedUnion() {
    Set<JSType> elements = new HashSet<JSType>();
    elements.add(registry.getTypeRegistry().getStringType());
    elements.add(registry.getTypeRegistry().getBooleanType());

    UnionType unionType = new UnionType(registry, elements);

    JSType restrictedType = unionType.getRestrictedUnion(registry.getTypeRegistry().getStringType());
    assertEquals(registry.getTypeRegistry().getBooleanType(), restrictedType);
  }

  @Test
  public void testUnionTypeVisit() {
    Set<JSType> elements = new HashSet<JSType>();
    elements.add(registry.getTypeRegistry().getStringType());
    elements.add(registry.getTypeRegistry().getBooleanType());

    UnionType unionType = new UnionType(registry, elements);

    JSType visited = new Object();

    JSType result = unionType.visit(new Visitor<JSType>() {
      @Override
      public JSType caseUnionType(UnionType unionType) {
        return visited;
      }
    });

    assertSame(visited, result);
  }

  @Test
  public void testUnionTypeResolveInternal() {
    Set<JSType> elements = new HashSet<JSType>();
    elements.add(registry.getTypeRegistry().getStringType());
    elements.add(registry.getTypeRegistry().getBooleanType());

    UnionType unionType = new UnionType(registry, elements);

    ErrorReporter errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, int line, int column) {
        fail("Unexpected error: " + message);
      }

      @Override
      public void warning(String message, int line, int column) {
        // Ignored
      }
    };

    StaticScope<JSType> scope = new StaticScope<JSType>(registry.getTypeRegistry().getObjectType(), null);

    JSType resolvedType = unionType.resolveInternal(errorReporter, scope);
    assertEquals(unionType, resolvedType);

    JSFunctionSymbol functionSymbol = new JSFunctionSymbol("test", null, null, null, null, null, null);
    scope.declare(functionSymbol, new JSPropertySymbol("test", functionSymbol, JSDynamicSymbol.getNoAttributes(), false));

    JSType resolvedType2 = unionType.resolveInternal(errorReporter, scope);
    assertEquals(unionType, resolvedType2);

    JSObjectSymbol objectSymbol = new JSObjectSymbol("Object", null, null, null);
    scope.declare(objectSymbol, new JSPropertySymbol("toString", null, null, false));
    JSPropertySymbol stringPropertySymbol = new JSPropertySymbol("toString", registry.getTypeRegistry().getStringType(), null, false);
    scope.declare(stringPropertySymbol, objectSymbol);

    JSType resolvedType3 = unionType.resolveInternal(errorReporter, scope);
    assertEquals(registry.getTypeRegistry().getStringType(), resolvedType3);
  }
}