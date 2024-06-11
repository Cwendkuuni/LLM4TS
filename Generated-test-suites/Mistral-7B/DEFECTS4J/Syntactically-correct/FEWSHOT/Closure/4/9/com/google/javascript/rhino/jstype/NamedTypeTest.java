package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class NamedTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> enclosingScope;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {};
    enclosingScope = new StaticScope<JSType>() {};
  }

  @Test
  public void testNewNamedType() {
    String reference = "MyType";
    String sourceName = "MySourceFile.js";
    int lineno = 10;
    int charno = 5;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertNotNull(namedType.getReferencedType());
    assertNull(namedType.getName());
    assertEquals(reference, namedType.getReferenceName());
    assertEquals(sourceName, namedType.getSourceName());
    assertEquals(lineno, namedType.getLineNumber());
    assertEquals(charno, namedType.getCharNumber());
  }

  @Test
  public void testResolveViaRegistry() {
    String reference = "MyType";
    JSType myType = new ObjectType(registry, "MyType");

    registry.putType(reference, myType);

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);
    assertNotNull(namedType.getReferencedType());
    assertEquals(myType, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaProperties() {
    String reference = "MyType.subType";
    String firstComponent = "MyType";
    String secondComponent = "subType";

    StaticScope<JSType> staticScope = new StaticScope<JSType>() {};
    staticScope.putSlot(firstComponent, new ObjectType(registry, firstComponent));

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);
    ObjectType myType = new ObjectType(registry, firstComponent);
    myType.setPropertyType(secondComponent, new ObjectType(registry, secondComponent));

    List<PropertyContinuation> propertyContinuations = Lists.newArrayList();
    namedType.defineProperty(secondComponent, new ObjectType(registry, secondComponent), false, null);
    namedType.setPropertyContinuations(propertyContinuations);

    namedType.resolveInternal(errorReporter, staticScope);
    assertEquals(myType.getPropertyType(secondComponent), namedType.getReferencedType());
  }

  @Test
  public void testResolveViaRegistryAndProperties() {
    String reference = "MyType.subType";
    String firstComponent = "MyType";
    String secondComponent = "subType";

    StaticScope<JSType> staticScope = new StaticScope<JSType>() {};
    staticScope.putSlot(firstComponent, new ObjectType(registry, firstComponent));

    JSType myType = new ObjectType(registry, firstComponent);
    myType.setPropertyType(secondComponent, new ObjectType(registry, secondComponent));

    registry.putType(reference, myType);

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);
    namedType.resolveInternal(errorReporter, staticScope);
    assertEquals(myType, namedType.getReferencedType());
  }

  @Test
  public void testUnresolvedType() {
    String reference = "MyType";
    String sourceName = "MySourceFile.js";
    int lineno = 10;
    int charno = 5;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    namedType.resolveInternal(errorReporter, enclosingScope);
    assertFalse(namedType.isResolved());
  }
}