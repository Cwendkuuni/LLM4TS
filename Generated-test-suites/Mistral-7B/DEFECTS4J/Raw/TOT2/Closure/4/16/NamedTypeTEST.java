import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.TypeEqualityTester;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {
  private StaticScope<JSType> enclosing;
  private ErrorReporter t;

  @Before
  public void setUp() {
    t = new ErrorReporter() {};
    enclosing = new StaticScope<JSType>() {};
  }

  @Test
  public void testNamedTypeCreation() {
    String reference = "String";
    NamedType namedType = new NamedType(
        new JSTypeRegistry(null), reference, "MyClass.java", 1, 2);
    assertEquals(reference, namedType.getReferenceName());
    assertEquals(JSTYPE_NAMED, namedType.getTypeCode());
    assertEquals("com.google.javascript.rhino.jstype.NamedType",
        namedType.toString());
  }

  @Test
  public void testNamedTypeResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    NamedType namedType = new NamedType(registry, "String", "MyClass.java", 1, 2);
    assertEquals(String.class, namedType.getReferencedType());
  }

  @Test
  public void testNamedTypeResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("MyObject", Object.class);
    ObjectType myObject = new ObjectType(registry);
    myObject.defineProperty("MyString", String.class, false, null);
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};
    StaticSlot<JSType> myObjectSlot = enclosing.defineSlot("MyObject");
    enclosing.setSlot(myObjectSlot, myObject);

    NamedType namedType = new NamedType(registry, "MyObject.MyString",
        "MyClass.java", 1, 2);
    assertEquals(String.class, namedType.getReferencedType());
  }

  @Test(expected = AssertionError.class)
  public void testNamedTypeResolveViaPropertiesInvalidType() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("MyObject", Object.class);
    ObjectType myObject = new ObjectType(registry);
    JSType invalidType = registry.getType("InvalidType");
    myObject.defineProperty("MyString", invalidType, false, null);
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};
    StaticSlot<JSType> myObjectSlot = enclosing.defineSlot("MyObject");
    enclosing.setSlot(myObjectSlot, myObject);

    NamedType namedType = new NamedType(registry, "MyObject.MyString",
        "MyClass.java", 1, 2);
    // Throws an AssertionError because the referenced type is not valid.
  }

  @Test
  public void testNamedTypeResolveViaPropertiesNoObject() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("MyFunction", FunctionType.class);
    FunctionType myFunction = new FunctionType(registry);

    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};
    StaticSlot<JSType> myFunctionSlot = enclosing.defineSlot("MyFunction");
    enclosing.setSlot(myFunctionSlot, myFunction);

    NamedType namedType = new NamedType(registry, "MyFunction",
        "MyClass.java", 1, 2);
    assertEquals(FunctionType.class, namedType.getReferencedType().getClass());
  }

  @Test
  public void testNamedTypeResolveViaPropertiesEnum() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("MyEnum", EnumType.class);
    EnumType myEnum = new EnumType(registry);
    myEnum.defineElement("MyEnumElement", null);

    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};
    StaticSlot<JSType> myEnumSlot = enclosing.defineSlot("MyEnum");
    enclosing.setSlot(myEnumSlot, myEnum);

    NamedType namedType = new NamedType(registry, "MyEnum.MyEnumElement",
        "MyClass.java", 1, 2);
    assertEquals(myEnum.getElementsType().getClass(), namedType.getReferencedType().getClass());
  }

  @Test
  public void testNamedTypeResolveViaPropertiesEnumElement() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("MyEnum", EnumType.class);
    EnumType myEnum = new EnumType(registry);
    myEnum.defineElement("MyEnumElement", null);
    EnumElementType myEnumElement = myEnum.getElementsType();

    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};
    StaticSlot<JSType> myEnumSlot = enclosing.defineSlot("MyEnum");
    enclosing.setSlot(myEnumSlot, myEnum);

    NamedType namedType = new NamedType(registry, "MyEnum.MyEnumElement",
        "MyClass.java", 1, 2);
    assertEquals(myEnumElement.getClass(), namedType.getReferencedType().getClass());
  }

  @Test(expected = AssertionError.class)
  public void testNamedTypeResolveViaPropertiesInvalidReference() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("MyObject", Object.class);
    ObjectType myObject = new ObjectType(registry);
    myObject.defineProperty("MyString", String.class, false, null);
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};
    StaticSlot<JSType> myObjectSlot = enclosing.defineSlot("MyObject");
    enclosing.setSlot(myObjectSlot, myObject);

    NamedType namedType = new NamedType(registry, "MyObjectMyString",
        "MyClass.java", 1, 2);
    // Throws an AssertionError because the reference is invalid.
  }

  @Test
  public void testNamedTypeIsResolved() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    NamedType namedType = new NamedType(registry, "String", "MyClass.java", 1, 2);
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testNamedTypeIsNotResolved() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    NamedType namedType = new NamedType(registry, "UnknownType", "MyClass.java", 1, 2);
    assertFalse(namedType.isResolved());
  }

  @Test
  public void testNamedTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    NamedType namedType = new NamedType(registry, "String", "MyClass.java", 1, 2);
    assertEquals("NamedType[String]", namedType.toString());
  }

  @Test
  public void testNamedTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    NamedType namedType1 = new NamedType(registry, "String", "MyClass.java", 1, 2);
    NamedType namedType2 = new NamedType(registry, "String", "MyClass.java", 1, 2);
    assertEquals(namedType1.hashCode(), namedType2.hashCode());
  }

  @Test
  public void testNamedTypeIsInstanceOf() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    NamedType namedType = new NamedType(registry, "String", "MyClass.java", 1, 2);
    assertTrue(namedType instanceof NamedType);
    assertTrue(namedType instanceof ProxyObjectType);
    assertFalse(namedType instanceof ObjectType);
  }

  @Test
  public void testNamedTypeIsNamedType() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    NamedType namedType = new NamedType(registry, "String", "MyClass.java", 1, 2);
    assertTrue(namedType.isNamedType());
  }

  @Test
  public void testNamedTypeIsNominalType() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    NamedType namedType = new NamedType(registry, "String", "MyClass.java", 1, 2);
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testNamedTypeEquals() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    NamedType namedType1 = new NamedType(registry, "String", "MyClass.java", 1, 2);
    NamedType namedType2 = new NamedType(registry, "String", "MyClass.java", 1, 2);
    NamedType namedType3 = new NamedType(registry, "String", "MyClass.java", 1, 3);
    assertTrue(namedType1.equals(namedType1));
    assertTrue(namedType1.equals(namedType2));
    assertFalse(namedType1.equals(namedType3));
  }

  @Test
  public void testNamedTypeTypeCode() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    NamedType namedType = new NamedType(registry, "String", "MyClass.java", 1, 2);
    assertEquals(JSTYPE_NAMED, namedType.getTypeCode());
  }

  @Test
  public void testNamedTypeSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    NamedType namedType = new NamedType(registry, "String", "MyClass.java", 1, 2);
    TypeEqualityTester validator = new TypeEqualityTester();
    namedType.setValidator(validator);
    assertEquals(validator, namedType.getValidator());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNamedTypeDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    NamedType namedType = new NamedType(registry, "String", "MyClass.java", 1, 2);
    namedType.defineProperty("MyString", String.class, false, null);
    // Throws an UnsupportedOperationException because properties cannot be defined.
  }

  @Test
  public void testNamedTypeIsForwardDeclared() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    registry.registerForwardDeclaration("MyString", String.class);
    NamedType namedType = new NamedType(registry, "MyString", "MyClass.java", 1, 2);
    assertTrue(namedType.isForwardDeclared());
  }

  @Test
  public void testNamedTypeIsNotForwardDeclared() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    NamedType namedType = new NamedType(registry, "UnknownType", "MyClass.java", 1, 2);
    assertFalse(namedType.isForwardDeclared());
  }

  @Test
  public void testNamedTypeGetReferencedType() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    NamedType namedType = new NamedType(registry, "String", "MyClass.java", 1, 2);
    assertEquals(String.class, namedType.getReferencedType());
  }

  @Test
  public void testNamedTypeGetReferencedTypeInvalid() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    NamedType namedType = new NamedType(registry, "UnknownType", "MyClass.java", 1, 2);
    assertNull(namedType.getReferencedType());
  }

  @Test(expected = AssertionError.class)
  public void testNamedTypeGetReferencedTypeUnresolved() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    NamedType namedType = new NamedType(registry, "MyUnknownType", "MyClass.java", 1, 2);
    // Throws an AssertionError because the type is unresolved.
    namedType.getReferencedType();
  }

  @Test
  public void testNamedTypeGetReferencedName() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    NamedType namedType = new NamedType(registry, "String", "MyClass.java", 1, 2);
    assertEquals("String", namedType.getReferenceName());
  }

  @Test
  public void testNamedTypeToStringHelper() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("String", String.class);
    NamedType namedType = new NamedType(registry, "String", "MyClass.java", 1, 2);
    assertEquals("String", namedType.toStringHelper(true));
  }

  @Test
  public void testNamedTypeGetProperties() {
    JSTypeRegistry registry = new JSTypeRegistry(null);
    registry.registerType("MyObject", Object.class);
    ObjectType myObject = new ObjectType(registry);
    myObject.defineProperty("MyString", String.class, false, null);
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};
    StaticSlot<JSType> myObjectSlot = enclosing.defineSlot("MyObject");
    enclosing.setSlot(myObjectSlot, myObject);

    NamedType namedType = new NamedType(registry, "MyObject.MyString",
        "MyClass.java", 1, 2);
    assertEquals("MyString", namedType.properties()[0].getName());
    assertEquals(String.class, namedType.properties()[0].getType());
  }
}