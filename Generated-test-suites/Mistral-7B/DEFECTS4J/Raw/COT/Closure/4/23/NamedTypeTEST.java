import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TypeError;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.testing.JSTestCase;

public class NamedTypeTest extends JSTestCase {

  

  // Test constructor
  public void testNamedTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertEquals("String", namedType.getReferenceName());
    assertTrue(namedType.isNamedType());
    assertTrue(namedType.isNominalType());
  }

  // Test defineProperty
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "Object", "Test", 1, 1);
    assertFalse(namedType.isResolved());

    String propertyName = "testProperty";
    JSType propertyType = JSType.INT;
    Node propertyNode = new Node();

    namedType.defineProperty(propertyName, propertyType, false, propertyNode);
    assertFalse(namedType.isResolved());

    ObjectType referencedObjType = namedType.getReferencedObjTypeInternal();
    assertNull(referencedObjType);

    namedType.finishPropertyContinuations();
    ObjectType resolvedType = namedType.getReferencedObjTypeInternal();
    assertTrue(resolvedType != null);
    assertEquals(JSTypeNative.OBJECT_TYPE, resolvedType.getReferencedType());

    ObjectType resolvedPropertyType = resolvedType.getPropertyType(propertyName);
    assertEquals(propertyType, resolvedPropertyType);
  }

  // Test getReferencedType
  public void testGetReferencedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertEquals("String", namedType.getReferenceName());
    assertNull(namedType.getReferencedType());

    JSTypeRegistry registry2 = new JSTypeRegistry();
    registry2.put("String", JSType.STRING);
    namedType.setReferencedType(registry2.getType("String"));
    assertEquals(JSType.STRING, namedType.getReferencedType());
  }

  // Test isResolved
  public void testIsResolved() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertFalse(namedType.isResolved());

    registry.put("String", JSType.STRING);
    namedType.setReferencedType(registry.getType("String"));
    assertTrue(namedType.isResolved());
  }

  // Test hasReferenceName
  public void testHasReferenceName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertTrue(namedType.hasReferenceName());
  }

  // Test isNamedType
  public void testIsNamedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertTrue(namedType.isNamedType());
  }

  // Test isNominalType
  public void testIsNominalType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertTrue(namedType.isNominalType());
  }

  // Test hashCode
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType1 = new NamedType(registry, "String", "Test1", 1, 1);
    NamedType namedType2 = new NamedType(registry, "String", "Test2", 1, 1);

    assertEquals(namedType1.getReferenceName().hashCode(), namedType1.hashCode());
    assertEquals(namedType2.getReferenceName().hashCode(), namedType2.hashCode());
    assertNotEquals(namedType1.hashCode(), namedType2.hashCode());
  }

  // Test toStringHelper
  public void testToStringHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertEquals("String", namedType.toStringHelper(false));
  }

  // Test resolveInternal
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertTrue(namedType.isResolved());
    assertSame(JSType.STRING, namedType.getReferencedType());

    registry.put("String", JSType.STRING);
    namedType.setReferencedType(registry.getType("String"));
    registry.removeType("String");
    registry.put("String", JSType.STRING);
    namedType.resolveInternal(errorReporter, null);
    assertTrue(namedType.isResolved());
    assertSame(JSType.STRING, namedType.getReferencedType());

    registry.put("String", JSType.INT);
    namedType.resolveInternal(errorReporter, null);
    assertFalse(namedType.isResolved());
    assertNull(namedType.getReferencedType());

    namedType.setReferencedType(null);
    namedType.resolveInternal(errorReporter, null);
    assertFalse(namedType.isResolved());
    assertNull(namedType.getReferencedType());
  }

  // Test resolveViaRegistry
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.put("String", JSType.STRING);

    NamedType namedType1 = new NamedType(registry, "String", "Test1", 1, 1);
    namedType1.resolveInternal(errorReporter, null);
    assertTrue(namedType1.isResolved());
    assertSame(JSType.STRING, namedType1.getReferencedType());

    registry.put("Array", JSType.ARRAY);

    NamedType namedType2 = new NamedType(registry, "Array", "Test2", 1, 1);
    namedType2.resolveInternal(errorReporter, null);
    assertInstanceOf(JSType.ARRAY, namedType2.getReferencedType());
  }

  // Test resolveViaProperties
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> scope = new StaticScope<>();

    registry.put("String", JSType.STRING);
    registry.put("MyObject", new ObjectType(registry, JSTypeNative.OBJECT_TYPE));
    registry.put("MyObject.StringProperty", JSType.STRING);

    NamedType namedType = new NamedType(registry, "MyObject.StringProperty", "Test", 1, 1);
    namedType.resolveInternal(errorReporter, scope);
    assertTrue(namedType.isResolved());
    assertSame(JSType.STRING, namedType.getReferencedType());
  }

  // Test setReferencedAndResolvedType
  public void testSetReferencedAndResolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertNull(namedType.getReferencedType());

    JSType type = JSType.INT;
    namedType.setReferencedAndResolvedType(type, errorReporter, null);
    assertEquals(type, namedType.getReferencedType());
  }

  // Test handleTypeCycle
  public void testHandleTypeCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType cycleType = new ObjectType(registry, JSTypeNative.OBJECT_TYPE);
    cycleType.setProperty("cycleProperty", cycleType);

    NamedType namedType = new NamedType(registry, "cycleType", "Test", 1, 1);
    namedType.resolveInternal(errorReporter, null);

    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getReferencedType());
    assertInstanceOf(TypeError.class, namedType.getResolvedType());
  }

  // Test checkEnumElementCycle
  public void testCheckEnumElementCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType enumType = new EnumType(registry, "MyEnum", JSTypeNative.OBJECT_TYPE);
    enumType.setProperty("MyEnumElement1", JSType.STRING);
    enumType.setProperty("MyEnumElement2", JSType.INT);

    enumType.setProperty("MyEnumElement3", new EnumElementType(registry, "MyEnumElement3", JSTypeNative.OBJECT_TYPE, enumType));

    NamedType namedType = new NamedType(registry, "MyEnumElement3", "Test", 1, 1);
    namedType.resolveInternal(errorReporter, null);

    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getReferencedType());
    assertInstanceOf(TypeError.class, namedType.getResolvedType());
  }

  // Test setValidator
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();

    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);

    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        assertEquals(JSType.STRING, input);
        return true;
      }
    };

    namedType.setValidator(validator);

    JSType type = JSType.INT;
    namedType.setReferencedAndResolvedType(type, errorReporter, null);

    assertTrue(validator.apply(type));
  }

  
}