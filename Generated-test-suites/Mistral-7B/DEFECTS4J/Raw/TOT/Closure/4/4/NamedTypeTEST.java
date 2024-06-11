import static com.google.javascript.rhino.JSTypeNative.NO_RESOLVED_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TestBase;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;

public class NamedTypeTest extends TestBase {

  private final JSTypeRegistry registry = new JSTypeRegistry();
  private final ErrorReporter t = registry.getErrorReporter();
  private final StaticScope<JSType> enclosingScope = new StaticScope<>();

  @Override
  public void setUp() {
    super.setUp();
    enclosingScope.setCurrentScope(currentScope());
  }

  public void testNamedType_basic() {
    String reference = "MyType";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);
    assertEquals(reference, namedType.getReferenceName());
    assertFalse(namedType.isResolved());
    assertSame(registry.getNativeObjectType(NO_RESOLVED_TYPE), namedType.getReferencedType());
    assertSame(registry.getNativeObjectType(NO_RESOLVED_TYPE), namedType.getResolvedType());

    StaticSlot<JSType> slot = enclosingScope.setSlot(reference, null);
    ObjectType objectType = new ObjectType(registry, "MyType", null, null);
    slot.setType(objectType);

    namedType.resolveInternal(t, enclosingScope);

    assertTrue(namedType.isResolved());
    assertEquals(objectType, namedType.getReferencedType());
    assertEquals(objectType, namedType.getResolvedType());
  }

  public void testNamedType_cycle() {
    String reference1 = "TypeA";
    String reference2 = "TypeB";

    NamedType typeA = new NamedType(registry, reference1, "Test", 1, 1);
    NamedType typeB = new NamedType(registry, reference2, "Test", 1, 1);

    ObjectType objectTypeA = new ObjectType(registry, reference1, null, null);
    ObjectType objectTypeB = new ObjectType(registry, reference2, null, null);
    objectTypeA.setPrototypeOf(objectTypeB);
    StaticSlot<JSType> slotA = enclosingScope.setSlot(reference1, null);
    StaticSlot<JSType> slotB = enclosingScope.setSlot(reference2, null);
    slotA.setType(objectTypeA);
    slotB.setType(objectTypeB);

    typeA.resolveInternal(t, enclosingScope);

    assertEquals(objectTypeB, typeA.getReferencedType());
    assertEquals(objectTypeB, typeA.getResolvedType());

    typeB.resolveInternal(t, enclosingScope);

    assertEquals(objectTypeA, typeB.getReferencedType());
    assertEquals(objectTypeA, typeB.getResolvedType());
  }

  public void testNamedType_unresolved() {
    String reference = "UnknownType";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    namedType.resolveInternal(t, enclosingScope);

    assertFalse(namedType.isResolved());
    assertSame(registry.getNativeObjectType(NO_RESOLVED_TYPE), namedType.getReferencedType());
    assertSame(registry.getNativeObjectType(NO_RESOLVED_TYPE), namedType.getResolvedType());
  }

  public void testNamedType_forward_declared() {
    String reference = "ForwardDeclaredType";
    String reference2 = "MyType";

    NamedType forwardDeclaredType = new NamedType(registry, reference, "Test", 1, 1);
    StaticSlot<JSType> slot = enclosingScope.setSlot(reference, null);
    slot.setType(forwardDeclaredType);

    ObjectType myType = new ObjectType(registry, reference2, null, null);
    StaticSlot<JSType> slotMyType = enclosingScope.setSlot(reference2, null);
    slotMyType.setType(myType);

    forwardDeclaredType.resolveInternal(t, enclosingScope);

    assertFalse(forwardDeclaredType.isResolved());
    assertEquals(forwardDeclaredType, forwardDeclaredType.getReferencedType());
    assertSame(registry.getNativeObjectType(NO_RESOLVED_TYPE), forwardDeclaredType.getResolvedType());

    // Now, MyType should be resolved as well.
    forwardDeclaredType.getReferencedType().resolveInternal(t, enclosingScope);

    assertTrue(forwardDeclaredType.isResolved());
    assertEquals(myType, forwardDeclaredType.getReferencedType());
    assertEquals(myType, forwardDeclaredType.getResolvedType());
  }

  public void testNamedType_property_continuation() {
    String reference = "MyType";

    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);
    assertFalse(namedType.isResolved());
    assertSame(registry.getNativeObjectType(NO_RESOLVED_TYPE), namedType.getReferencedType());
    assertSame(registry.getNativeObjectType(NO_RESOLVED_TYPE), namedType.getResolvedType());

    StaticScope<JSType> innerScope = new StaticScope<>();
    StaticSlot<JSType> slot = innerScope.setSlot("MyType", null);
    ObjectType objectType = new ObjectType(registry, "MyType", null, null);
    slot.setType(objectType);
    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "propertyName", null, false, null);

    namedType.defineProperty("propertyName", null, false, null);

    namedType.resolveInternal(t, enclosingScope);

    assertTrue(namedType.isResolved());
    assertEquals(objectType, namedType.getReferencedType());
    assertEquals(objectType, namedType.getResolvedType());
    assertEquals("propertyName", namedType.getPropertyNames()[0]);
  }

  public void testNamedType_resolveViaRegistry() {
    String reference = "ObjectType";

    ObjectType objectType = new ObjectType(registry, reference, null, null);
    StaticSlot<JSType> slot = enclosingScope.setSlot(reference, null);
    slot.setType(objectType);

    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    namedType.resolveInternal(t, enclosingScope);

    assertTrue(namedType.isResolved());
    assertEquals(objectType, namedType.getReferencedType());
    assertEquals(objectType, namedType.getResolvedType());
  }

  public void testNamedType_resolveViaProperties() {
    String reference = "ObjectType.propertyName";

    ObjectType objectType = new ObjectType(registry, "ObjectType", null, null);
    StaticSlot<JSType> slotObjectType = enclosingScope.setSlot("ObjectType", null);
    slotObjectType.setType(objectType);

    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "propertyName", null, false, null);

    objectType.defineProperty("propertyName", null, false, null);

    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    namedType.resolveInternal(t, enclosingScope);

    assertTrue(namedType.isResolved());
    assertEquals(registry.getNativeObjectType(ObjectType.class), namedType.getReferencedType());
    assertEquals(registry.getNativeObjectType(ObjectType.class), namedType.getResolvedType());
    assertEquals("propertyName", namedType.getPropertyNames()[0]);
  }

  public void testNamedType_getReferencedObjType() {
    String reference = "ObjectType";

    ObjectType objectType = new ObjectType(registry, reference, null, null);

    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    assertEquals(objectType, namedType.getReferencedObjType());
  }

  public void testNamedType_isNamedType() {
    String reference = "NamedType";

    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    assertTrue(namedType.isNamedType());
  }

  public void testNamedType_isNominalType() {
    String reference = "NamedType";

    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    assertTrue(namedType.isNominalType());
  }

  public void testNamedType_hashCode() {
    String reference = "NamedType";

    NamedType namedType1 = new NamedType(registry, reference, "Test", 1, 1);
    NamedType namedType2 = new NamedType(registry, reference, "Test", 1, 1);

    assertEquals(namedType1.hashCode(), namedType2.hashCode());
  }

  public void testNamedType_toString() {
    String reference = "NamedType";

    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    assertEquals("NamedType", namedType.toString());
  }
}

class PropertyContinuation {
  private final String propertyName;
  private final JSType type;
  private final boolean inferred;
  private final Node propertyNode;

  PropertyContinuation(String propertyName, JSType type, boolean inferred, Node propertyNode) {
    this.propertyName = propertyName;
    this.type = type;
    this.inferred = inferred;
    this.propertyNode = propertyNode;
  }
}