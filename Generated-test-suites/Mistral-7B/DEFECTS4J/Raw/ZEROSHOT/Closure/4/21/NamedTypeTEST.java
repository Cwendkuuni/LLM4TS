import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.JSTestCase;
import com.google.javascript.rhino.testing.PolymerTestRunner;
import com.google.javascript.rhino.types.JSType;
import com.google.javascript.rhino.types.FunctionType;
import com.google.javascript.rhino.types.ObjectType;
import com.google.javascript.rhino.types.PrimitiveType;
import com.google.javascript.rhino.types.StringType;
import com.google.javascript.rhino.types.UndefinedType;

public class NamedTypeTest extends JSTestCase {

  @Test
  public void testNamedTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String sourceName = "testSource.js";
    int lineno = 1;
    int charno = 1;

    JSType namedType = new NamedType(
        registry, "String", sourceName, lineno, charno);
    assertEquals("String", namedType.getReferenceName());
    assertSame(StringType.INSTANCE, namedType.getReferencedType());

    namedType = new NamedType(
        registry, "Function", sourceName, lineno, charno);
    assertEquals("Function", namedType.getReferenceName());
    assertSame(UndefinedType.instance, namedType.getReferencedType());

    namedType = new NamedType(
        registry, "UnknownType", sourceName, lineno, charno);
    assertEquals("UnknownType", namedType.getReferenceName());
    assertSame(UndefinedType.instance, namedType.getReferencedType());

    namedType = new NamedType(
        registry, "MyType", sourceName, lineno, charno);
    assertEquals("MyType", namedType.getReferenceName());
    assertSame(UndefinedType.instance, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String sourceName = "testSource.js";
    int lineno = 1;
    int charno = 1;

    registry.registerType("StringType", StringType.INSTANCE);

    JSType namedType = new NamedType(registry, "StringType", sourceName, lineno, charno);
    assertEquals("StringType", namedType.getReferenceName());
    assertSame(StringType.INSTANCE, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String sourceName = "testSource.js";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> staticScope = new StaticScope<>();
    staticScope.putSlot("MyObject", ObjectType.createPrototype(registry));

    JSType unresolvedType = new NamedType(
        registry, "MyObject.MyProperty", sourceName, lineno, charno);
    assertEquals("MyObject.MyProperty", unresolvedType.getReferenceName());
    assertSame(UndefinedType.instance, unresolvedType.getReferencedType());

    ObjectType myObjectType = staticScope.getSlot("MyObject").getType();
    PropertyContinuation continution = new PropertyContinuation(
        "MyProperty", StringType.INSTANCE, true, null);
    myObjectType.defineProperty("MyProperty", continution, false, null);

    unresolvedType = new NamedType(
        registry, "MyObject.MyProperty", sourceName, lineno, charno);
    assertEquals("MyObject.MyProperty", unresolvedType.getReferenceName());
    assertSame(StringType.INSTANCE, unresolvedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String sourceName = "testSource.js";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> staticScope = new StaticScope<>();
    staticScope.putSlot("MyObject", ObjectType.createPrototype(registry));

    JSType unresolvedType = new NamedType(
        registry, "MyObject.MyFunction", sourceName, lineno, charno);
    assertEquals("MyObject.MyFunction", unresolvedType.getReferenceName());
    assertSame(UndefinedType.instance, unresolvedType.getReferencedType());

    ObjectType myObjectType = staticScope.getSlot("MyObject").getType();
    FunctionType functionType = FunctionType.createInstance(
        new PrimitiveType(JSTypeNative.VOID),
        new PrimitiveType(JSTypeNative.NUMBER), null);

    PropertyContinuation continution = new PropertyContinuation(
        "MyFunction", functionType.getInstanceType(), true, null);
    myObjectType.defineProperty(
        "MyFunction", continution, false, null);

    unresolvedType = new NamedType(
        registry, "MyObject.MyFunction", sourceName, lineno, charno);
    assertEquals("MyObject.MyFunction", unresolvedType.getReferenceName());
    assertSame(FunctionType.createInstance(
        new PrimitiveType(JSTypeNative.VOID),
        new PrimitiveType(JSTypeNative.NUMBER), null).getInstanceType(),
        unresolvedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithEnumType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String sourceName = "testSource.js";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> staticScope = new StaticScope<>();
    staticScope.putSlot("MyEnum", EnumType.createPrototype(registry));

    JSType unresolvedType = new NamedType(
        registry, "MyEnum.MyEnumMember", sourceName, lineno, charno);
    assertEquals("MyEnum.MyEnumMember", unresolvedType.getReferenceName());
    assertSame(UndefinedType.instance, unresolvedType.getReferencedType());

    EnumType myEnumType = staticScope.getSlot("MyEnum").getType();
    EnumElementType enumElementType = EnumElementType.createInstance(
        "MyEnumMember", new PrimitiveType(JSTypeNative.STRING));

    PropertyContinuation continution = new PropertyContinuation(
        "MyEnumMember", enumElementType, true, null);
    myEnumType.defineProperty(
        "MyEnumMember", continution, false, null);

    unresolvedType = new NamedType(
        registry, "MyEnum.MyEnumMember", sourceName, lineno, charno);
    assertEquals("MyEnum.MyEnumMember", unresolvedType.getReferenceName());
    assertSame(enumElementType, unresolvedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithFunctionTypeAndInstanceType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String sourceName = "testSource.js";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> staticScope = new StaticScope<>();
    staticScope.putSlot("MyObject", ObjectType.createPrototype(registry));

    JSType unresolvedType = new NamedType(
        registry, "MyObject.myFunction", sourceName, lineno, charno);
    assertEquals("MyObject.myFunction", unresolvedType.getReferenceName());
    assertSame(UndefinedType.instance, unresolvedType.getReferencedType());

    ObjectType myObjectType = staticScope.getSlot("MyObject").getType();
    FunctionType functionType = FunctionType.createInstance(
        new PrimitiveType(JSTypeNative.VOID),
        new PrimitiveType(JSTypeNative.NUMBER), null);

    PropertyContinuation continution = new PropertyContinuation(
        "myFunction", functionType.getInstanceType(), true, null);
    myObjectType.defineProperty("myFunction", continution, false, null);

    unresolvedType = new NamedType(
        registry, "MyObject.myFunction", sourceName, lineno, charno);
    assertEquals("MyObject.myFunction", unresolvedType.getReferenceName());
    assertSame(functionType.getInstanceType(), unresolvedType.getReferencedType());
  }

  @Test
  public void testResolveViaRegistryWithCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String sourceName = "testSource.js";
    int lineno = 1;
    int charno = 1;

    registry.registerType("StringType", StringType.INSTANCE);
    registry.registerType("MyType", new NamedType(registry, "StringType", sourceName, lineno, charno));

    JSType namedType = new NamedType(registry, "MyType", sourceName, lineno, charno);
    assertEquals("MyType", namedType.getReferenceName());
    assertSame(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());

    try {
      namedType.resolve(null, null);
      fail("Expected TypeCycleException");
    } catch (TypeCycleException e) {
      // Expected
    }
  }

  @Test
  public void testResolveViaPropertiesWithCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String sourceName = "testSource.js";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> staticScope = new StaticScope<>();
    staticScope.putSlot("MyObject", ObjectType.createPrototype(registry));

    ObjectType myObjectType = staticScope.getSlot("MyObject").getType();
    PropertyContinuation continution = new PropertyContinuation(
        "property1", new NamedType(registry, "MyObject", sourceName, lineno, charno), true, null);
    myObjectType.defineProperty("property1", continution, false, null);

    continution = new PropertyContinuation(
        "property2", new NamedType(registry, "MyObject.property1", sourceName, lineno, charno), true, null);
    myObjectType.defineProperty("property2", continution, false, null);

    JSType namedType = new NamedType(registry, "MyObject.property2", sourceName, lineno, charno);
    assertEquals("MyObject.property2", namedType.getReferenceName());
    assertSame(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());

    try {
      namedType.resolve(null, null);
      fail("Expected TypeCycleException");
    } catch (TypeCycleException e) {
      // Expected
    }
  }

  @Test
  public void testResolveWithValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String sourceName = "testSource.js";
    int lineno = 1;
    int charno = 1;

    JSTypeValidator validator = new JSTypeValidator() {
      @Override
      public void validate(JSType type) {
        assertTrue(type instanceof FunctionType);
      }
    };

    StaticScope<JSType> staticScope = new StaticScope<>();
    staticScope.putSlot("MyObject", ObjectType.createPrototype(registry));

    FunctionType functionType = FunctionType.createInstance(
        new PrimitiveType(JSTypeNative.VOID),
        new PrimitiveType(JSTypeNative.NUMBER), null);

    PropertyContinuation continution = new PropertyContinuation(
        "myFunction", functionType.getInstanceType(), true, null);
    staticScope.getSlot("MyObject").defineProperty("myFunction", continution, false, null);

    JSType namedType = new NamedType(
        registry, "MyObject.myFunction", sourceName, lineno, charno);
    namedType.setValidator(validator);

    JSType referencedType = namedType.getReferencedType();
    assertEquals(FunctionType.class, referencedType.getClass());
    validator.validate(referencedType);

    try {
      namedType.resolve(null, null);
    } catch (TypeCycleException e) {
      // Ignored
    }
  }
}