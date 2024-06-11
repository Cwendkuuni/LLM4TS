package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.StylizedNames;
import com.google.javascript.rhino.test.TestCaseBase;

import java.util.List;

/**
 * Test cases for {@link NamedType} class.
 */
public class NamedTypeTest extends TestCaseBase {

  /**
   * Test constructor.
   */
  public void testNamedTypeConstructor() {
    String reference = "MyType";
    String sourceName = "MyClass";
    int lineno = 1;
    int charno = 1;
    StaticScope<JSType> enclosing = new StaticScope<>();

    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertEquals(reference, namedType.getReferenceName());
    assertEquals(sourceName, namedType.getSourceName());
    assertEquals(lineno, namedType.getLineno());
    assertEquals(charno, namedType.getCharno());
  }

  /**
   * Test defineProperty method when not resolved.
   */
  public void testDefinePropertyUnresolved() {
    String reference = "MyType";
    String sourceName = "MyClass";
    int lineno = 1;
    int charno = 1;
    StaticScope<JSType> enclosing = new StaticScope<>();

    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getReferencedType());

    String propertyName = "property";
    JSType propertyType = JSType.STRING;
    Node propertyNode = new Node(StylizedNames.PROPERTY_DEF);

    boolean result = namedType.defineProperty(propertyName, propertyType, false, propertyNode);

    assertTrue(result);
    assertNull(namedType.getPropertyType(propertyName));

    // Save the property continuation
    List<NamedType.PropertyContinuation> propertyContinuations = namedType.getPropertyContinuations();
    assertEquals(1, propertyContinuations.size());

    NamedType.PropertyContinuation propertyContinuation = propertyContinuations.get(0);
    assertEquals(propertyName, propertyContinuation.getPropertyName());
    assertEquals(propertyType, propertyContinuation.getType());
    assertFalse(propertyContinuation.isInferred());
    assertSame(propertyNode, propertyContinuation.getPropertyNode());
  }

  /**
   * Test finishPropertyContinuations method when resolved.
   */
  public void testFinishPropertyContinuations() {
    String reference = "MyType";
    String sourceName = "MyClass";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType referencedType = registry.getType("MyReferencedType");
    namedType.setReferencedAndResolvedType(referencedType);

    assertEquals(referencedType, namedType.getReferencedType());
    assertEquals(referencedType, namedType.getResolvedType());

    namedType.finishPropertyContinuations();

    ObjectType referencedObjType = ObjectType.cast(referencedType);
    assertEquals(referencedObjType.getPropertyType("property"), namedType.getPropertyType("property"));
  }

  /**
   * Test getReferencedType method when not resolved.
   */
  public void testGetReferencedTypeUnresolved() {
    String reference = "MyType";
    String sourceName = "MyClass";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertNull(namedType.getReferencedType());
  }

  /**
   * Test getReferencedType method when resolved.
   */
  public void testGetReferencedTypeResolved() {
    String reference = "MyType";
    String sourceName = "MyClass";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    JSType referencedType = registry.getType(reference);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    namedType.setReferencedAndResolvedType(referencedType);

    assertEquals(referencedType, namedType.getReferencedType());
  }

  /**
   * Test resolveInternal method.
   */
  public void testResolveInternal() {
    String reference = "MyType";
    String sourceName = "MyClass";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();

    // Test resolution via registry
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType1 = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType1 = namedType1.resolveInternal(errorReporter, enclosing);

    assertTrue(resolvedType1 instanceof NamedType);
    assertEquals(reference, ((NamedType) resolvedType1).getReferenceName());

    // Test cycle detection
    String cycleReference = "MyCycleType";
    String cycleSourceName = "MyCycleClass";
    int cycleLineno = 1;
    int cycleCharno = 1;

    errorReporter = new ErrorReporter();
    enclosing = new StaticScope<>();

    JSType cycleType = new JSTypeNative(JSTypeNative.UNKNOWN_TYPE) {
      @Override
      public JSType getInstanceType() {
        return this;
      }
    };
    registry.setType(cycleReference, cycleType);

    NamedType namedType2 = new NamedType(registry, cycleReference, cycleSourceName, cycleLineno, cycleCharno);
    namedType2.resolveInternal(errorReporter, enclosing);

    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType2.getReferencedType());
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType2.getResolvedType());

    // Test resolution via properties
    JSType resolvedType2 = JSType.OBJECT;
    errorReporter = new ErrorReporter();
    enclosing = new StaticScope<>();

    StaticSlot<JSType> slot = new StaticSlot<>(null, reference);
    enclosing.putSlot(slot);

    ObjectType parentType = new ObjectType(JSTypeRegistry.empty(), null);
    parentType.defineProperty("property", JSType.STRING, false, new Node(StylizedNames.PROPERTY_DEF));

    JSType typeToResolve = new NamedType(registry, reference, sourceName, lineno, charno);

    errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing2 = new StaticScope<>();

    typeToResolve.resolveInternal(errorReporter, new StaticScope<>(enclosing, parentType, null));

    assertEquals(resolvedType2, typeToResolve.getResolvedType());
  }
}