import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.StaticScopeTest;
import com.google.javascript.rhino.testing.TypeEquivalenceTester;

import java.util.List;

public class NamedTypeTest extends StaticScopeTest {

  public NamedTypeTest() {
    super(new NamedTypeTestSetup(this));
  }

  /**
   * Test constructor with valid arguments.
   */
  @org.junit.Test
  public void testNamedTypeConstructor() {
    String reference = "MyType";
    String sourceName = "MyTest.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertEquals("MyType", namedType.getReferenceName());
    assertSame(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
    assertSame(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getResolvedType());
  }

  /**
   * Test constructor with null reference.
   */
  @org.junit.Test(expected = NullPointerException.class)
  public void testNamedTypeConstructorNullReference() {
    String reference = null;
    String sourceName = "MyTest.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    new NamedType(registry, reference, sourceName, lineno, charno);
  }

  /**
   * Test defineProperty method when type is not resolved.
   */
  @org.junit.Test
  public void testDefinePropertyUnresolvedType() {
    String reference = "MyType";
    String sourceName = "MyTest.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    String propertyName = "property";
    JSType propertyType = JSType.BOOLEAN;
    Node propertyNode = new Node_Property();

    namedType.defineProperty(propertyName, propertyType, false, propertyNode);

    List<PropertyContinuation> propertyContinuations = namedType.getPropertyContinuations();
    assertEquals(1, propertyContinuations.size());

    PropertyContinuation propertyContinuation = propertyContinuations.get(0);
    assertEquals(propertyName, propertyContinuation.getPropertyName());
    assertEquals(propertyType, propertyContinuation.getType());
    assertFalse(propertyContinuation.isInferred());
    assertEquals(propertyNode, propertyContinuation.getPropertyNode());
  }

  /**
   * Test finishPropertyContinuations method when type is resolved.
   */
  @org.junit.Test
  public void testFinishPropertyContinuationsResolvedType() {
    String reference = "MyType";
    String sourceName = "MyTest.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    String referencedTypeReference = "MyReferencedType";
    JSType referencedType = new ObjectType(registry, referencedTypeReference);
    namedType.setReferencedType(referencedType);

    namedType.finishPropertyContinuations();

    ObjectType targetType = (ObjectType) namedType.getReferencedType();
    assertEquals(1, targetType.getPropertyCount());

    PropertyDescriptor propertyDescriptor = targetType.getPropertyDescriptor("property");
    assertEquals("property", propertyDescriptor.getName());
    assertEquals(JSType.BOOLEAN, propertyDescriptor.getType());
  }

  /**
   * Test getReferencedType method with unresolved type.
   */
  @org.junit.Test
  public void testGetReferencedTypeUnresolvedType() {
    String reference = "MyType";
    String sourceName = "MyTest.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertSame(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
  }

  /**
   * Test getReferencedType method with resolved type.
   */
  @org.junit.Test
  public void testGetReferencedTypeResolvedType() {
    String reference = "MyType";
    String sourceName = "MyTest.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    JSType referencedType = new ObjectType(registry, "MyReferencedType");
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    namedType.setReferencedType(referencedType);

    assertSame(referencedType, namedType.getReferencedType());
  }

  /**
   * Test resolveInternal method with valid arguments.
   */
  @org.junit.Test
  public void testResolveInternal() {
    String reference = "MyType";
    String sourceName = "MyTest.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();

    // Resolve via registry
    NamedType namedType1 = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType1 = namedType1.resolveInternal(null, null);
    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), resolvedType1);

    JSType referencedType = new ObjectType(registry, "MyReferencedType");
    registry.registerType(referencedType);

    // Resolve via registry again
    NamedType namedType2 = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType2 = namedType2.resolveInternal(null, null);
    assertEquals(referencedType, resolvedType2);

    // Resolve via properties
    StaticScope<JSType> staticScope = new StaticScope<JSType>() {};
    staticScope.setSlot("MyReferencedType", new ObjectType(registry, "MyReferencedType"));
    JSType resolvedType3 = namedType2.resolveInternal(null, staticScope);
    assertEquals(referencedType, resolvedType3);
  }

  /**
   * Test resolveInternal method with cycle.
   */
  @org.junit.Test
  public void testResolveInternalCycle() {
    String reference = "MyType";
    String sourceName = "MyTest.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();

    JSType cycleType = new ObjectType(registry, "CycleType");
    registry.registerType(cycleType);
    cycleType.setProperty("property", new NamedType(registry, "MyType"));

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(null, null);

    assertSame(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), resolvedType);
  }

  /**
   * Test setValidator method.
   */
  @org.junit.Test
  public void testSetValidator() {
    String reference = "MyType";
    String sourceName = "MyTest.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    Predicate<JSType> validator = new TypeEquivalenceTester();

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    namedType.setValidator(validator);

    JSType referencedType = new ObjectType(registry, "MyReferencedType");
    namedType.setReferencedType(referencedType);

    assertSame(referencedType, namedType.getReferencedType());
    assertTrue(validator.apply(referencedType));
  }
}