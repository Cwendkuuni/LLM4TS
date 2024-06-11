import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class NamedTypeTest {

  private static final String CLASS_NAME = "NamedTypeTest";
  private ErrorReporter errorReporter;
  private NamedType namedType;
  private StaticScope<JSType> staticScope;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    namedType = new NamedType(null, "java.lang.String", CLASS_NAME, 0, 0);
    staticScope = new StaticScope<>(null);
  }

  @Test
  public void testBasicFunctionality() {
    JSType referencedType = new ObjectType(null, "java.lang.Number");
    namedType.setReferencedType(referencedType);
    namedType.setResolvedType(referencedType);

    Assert.assertEquals("java.lang.Number", namedType.getReferenceName());
    Assert.assertEquals("java.lang.Number", namedType.toString());
    Assert.assertTrue(namedType.isNamedType());
    Assert.assertTrue(namedType.isNominalType());
  }

  @Test
  public void testDefineProperty() {
    JSType referencedType = new ObjectType(null, "java.lang.Number");
    namedType.setReferencedType(referencedType);

    String propertyName = "foo";
    JSType propertyType = new ObjectType(null, "java.lang.Integer");
    boolean inferred = false;
    Node propertyNode = new Node("Identifier", "foo");

    namedType.defineProperty(propertyName, propertyType, inferred, propertyNode);

    ObjectType target = new ObjectType(null, null);
    PropertyContinuation continuation = new PropertyContinuation(
        propertyName, propertyType, inferred, propertyNode);

    namedType.finishPropertyContinuations();
    target.defineProperty(propertyName, propertyType, inferred, propertyNode);

    Assert.assertEquals(propertyType, target.getPropertyType(propertyName));
  }

  @Test
  public void testGetReferencedType() {
    JSType referencedType = new ObjectType(null, "java.lang.String");
    namedType.setReferencedType(referencedType);

    Assert.assertEquals(referencedType, namedType.getReferencedType());
  }

  @Test(expected = AssertionError.class)
  public void testGetReferencedTypeUnresolved() {
    namedType.setReferencedType(null);
    Assert.assertNull(namedType.getReferencedType());
  }

  @Test
  public void testIsResolved() {
    JSType referencedType = new ObjectType(null, "java.lang.String");
    namedType.setReferencedType(referencedType);
    namedType.setResolvedType(referencedType);

    Assert.assertTrue(namedType.isResolved());

    namedType.setResolvedType(null);
    Assert.assertFalse(namedType.isResolved());
  }

  @Test
  public void testResolveInternal() {
    JSType referencedType = new ObjectType(null, "java.lang.String");
    namedType.setReferencedType(referencedType);

    StaticSlot<JSType> slot = staticScope.getSlot("String");
    StaticType staticType = slot.getType();
    JSType value = staticType != null ? staticType.toMaybeObjectType() : null;

    namedType.resolveInternal(errorReporter, staticScope);

    if (value != null && value.isFunctionType() &&
        (value.isConstructor() || value.isInterface())) {
      FunctionType functionType = value.toMaybeFunctionType();
      Assert.assertEquals(functionType.getInstanceType(), namedType.getReferencedType());
    } else if (value != null && value.isNoObjectType()) {
      Assert.assertEquals(
          registry.getNativeFunctionType(JSTypeNative.NO_OBJECT_TYPE).getInstanceType(),
          namedType.getReferencedType());
    } else if (value instanceof EnumType) {
      Assert.assertEquals(
          ((EnumType) value).getElementsType(), namedType.getReferencedType());
    } else {
      Assert.fail("Unexpected resolved type: " + value);
    }
  }

  @Test
  public void testResolveViaRegistry() {
    JSType referencedType = new ObjectType(null, "java.lang.String");
    registry.registerType(referencedType);

    namedType.setReferencedType(null);

    namedType.resolveInternal(errorReporter, staticScope);

    Assert.assertEquals(referencedType, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaProperties() {
    String packageName = "java.lang.";
    String className = "String";
    String propertyName = "valueOf";

    JSType valueType = new FunctionType(null, null, null, null, null);
    StaticSlot<JSType> stringSlot = staticScope.getSlot(className);
    StaticSlot<JSType> valueSlot = stringSlot.getSlot(propertyName);

    JSType propertyType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    ObjectType stringType = new ObjectType(null, className);
    stringType.setPropertyType(propertyName, propertyType);
    stringSlot.setType(stringType);

    namedType.setReferencedType(null);

    namedType.resolveInternal(errorReporter, staticScope);

    FunctionType functionType = valueSlot.getType().toMaybeFunctionType();
    Assert.assertEquals(functionType.getInstanceType(), namedType.getReferencedType());
  }

  @Test
  public void testSetReferencedAndResolvedType() {
    JSType referencedType = new ObjectType(null, "java.lang.String");

    namedType.setReferencedType(referencedType);
    namedType.setResolvedType(referencedType);

    Assert.assertEquals(referencedType, namedType.getReferencedType());
    Assert.assertEquals(referencedType, namedType.getResolvedType());
  }

  @Test
  public void testHandleTypeCycle() {
    String cycleClassName = "CycleType";
    String cyclePropertyName = "cycle";

    JSType cycleType = new ObjectType(null, cycleClassName);
    cycleType.setPropertyType(cyclePropertyName, cycleType);

    namedType.setReferencedType(cycleType);

    Assert.assertEquals(cycleType, namedType.getReferencedType());

    namedType.handleTypeCycle(errorReporter);

    Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
  }

  @Test
  public void testCheckEnumElementCycle() {
    String enumClassName = "EnumType";
    String enumElementName = "Element";

    JSType enumType = new EnumType(null, enumClassName, null, null, null);
    JSType elementType = new ObjectType(null, enumElementName);
    elementType.setPrimitiveType(enumType);

    namedType.setReferencedType(elementType);

    Assert.assertEquals(elementType, namedType.getReferencedType());

    namedType.checkEnumElementCycle(errorReporter);

    Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
  }

  @Test
  public void testHandleUnresolvedType() {
    Assert.assertNull(namedType.getReferencedType());
    namedType.handleUnresolvedType(errorReporter, false);
    Assert.assertNull(namedType.getReferencedType());

    namedType.handleUnresolvedType(errorReporter, true);
    Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), namedType.getReferencedType());
  }
}