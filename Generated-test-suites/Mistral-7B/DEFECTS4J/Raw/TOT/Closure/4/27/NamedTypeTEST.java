import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JsContext;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.test.BaseTestCase;
import com.google.javascript.rhino.test.JsTestRunner;
import com.google.javascript.rhino.types.ErrorType;
import com.google.javascript.rhino.types.FunctionType;
import com.google.javascript.rhino.types.JSTypeFactory;
import com.google.javascript.rhino.types.ObjectType;
import com.google.javascript.rhino.types.StringType;
import com.google.javascript.rhino.types.UndefinedType;

public class NamedTypeTest extends BaseTestCase {

  private static final String TEST_REFERENCE = "TestReference";
  private static final String TEST_SOURCE_NAME = "NamedTypeTest";
  private static final int TEST_LINE_NUMBER = 1;
  private static final int TEST_CHAR_NUMBER = 1;

  public static void main(String[] args) {
    JsTestRunner.runTest(NamedTypeTest.class);
  }

  public void testResolveViaRegistry() {
    JsContext jsContext = enterContext();

    JSTypeRegistry registry = jsContext.getFactory().getRegistry();
    ObjectType baseType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    // Register a type with the same name as the test reference
    JSType referencedType = JSTypeFactory.getNativeObjectType("TestType");
    registry.setType(TEST_REFERENCE, referencedType);

    NamedType namedType = new NamedType(registry, TEST_REFERENCE, TEST_SOURCE_NAME, TEST_LINE_NUMBER, TEST_CHAR_NUMBER);
    assertEquals(referencedType, namedType.getReferencedType());

    exitContext(jsContext);
  }

  public void testResolveViaProperties() {
    JsContext jsContext = enterContext();

    JSTypeRegistry registry = jsContext.getFactory().getRegistry();
    ObjectType baseType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    JSType testType1 = registry.getNativeObjectType("TestType1");
    JSType testType2 = registry.getNativeObjectType("TestType2");

    registry.setType("TestType1", testType1);
    registry.setType("TestType2", testType2);

    ObjectType testObjectType1 = new ObjectType(registry, testType1.getName(), baseType);
    ObjectType testObjectType2 = new ObjectType(registry, testType2.getName(), baseType);

    testObjectType1.defineProperty("testProperty", StringType.getInstanceType(), false, null);

    NamedType namedType = new NamedType(registry, "TestType2.testProperty", TEST_SOURCE_NAME, TEST_LINE_NUMBER, TEST_CHAR_NUMBER);
    assertEquals(testType2.getInstanceType().getMemberType("testProperty"), namedType.getReferencedType());

    exitContext(jsContext);
  }

  public void testResolveUsingImplicitPrototype() {
    JsContext jsContext = enterContext();

    JSTypeRegistry registry = jsContext.getFactory().getRegistry();
    ObjectType baseType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    JSType objectType = registry.getNativeObjectType("Object");

    ObjectType testObjectType = new ObjectType(registry, "TestObject", objectType);
    testObjectType.defineProperty("testProperty", StringType.getInstanceType(), false, null);

    NamedType namedType = new NamedType(registry, "TestObject.testProperty", TEST_SOURCE_NAME, TEST_LINE_NUMBER, TEST_CHAR_NUMBER);
    assertEquals(testObjectType.getInstanceType().getMemberType("testProperty"), namedType.getReferencedType());

    exitContext(jsContext);
  }

  public void testResolveUsingFunctionType() {
    JsContext jsContext = enterContext();

    JSTypeRegistry registry = jsContext.getFactory().getRegistry();
    ObjectType baseType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    JSType functionType = JSTypeFactory.getNativeFunctionType(
        new FunctionType(null, new ObjectType(registry, "ArgumentType", baseType), null));

    ObjectType testObjectType = new ObjectType(registry, "TestObject", baseType);
    testObjectType.defineProperty("testFunction", functionType, false, null);

    NamedType namedType = new NamedType(registry, "TestObject.testFunction", TEST_SOURCE_NAME, TEST_LINE_NUMBER, TEST_CHAR_NUMBER);
    assertEquals(functionType.getInstanceType(), namedType.getReferencedType());

    exitContext(jsContext);
  }

  public void testResolveUsingEnumType() {
    JsContext jsContext = enterContext();

    JSTypeRegistry registry = jsContext.getFactory().getRegistry();
    ObjectType baseType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    JSType enumType = registry.getNativeType("MyEnum");

    ObjectType testObjectType = new ObjectType(registry, "TestObject", baseType);
    testObjectType.defineProperty("testEnumProperty", enumType, false, null);

    NamedType namedType = new NamedType(registry, "TestObject.testEnumProperty", TEST_SOURCE_NAME, TEST_LINE_NUMBER, TEST_CHAR_NUMBER);
    assertEquals(enumType, namedType.getReferencedType());

    exitContext(jsContext);
  }

  public void testResolveUsingUndefinedType() {
    JsContext jsContext = enterContext();

    JSTypeRegistry registry = jsContext.getFactory().getRegistry();
    ObjectType baseType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    NamedType namedType = new NamedType(registry, "Undefined", TEST_SOURCE_NAME, TEST_LINE_NUMBER, TEST_CHAR_NUMBER);
    assertEquals(UndefinedType.instance, namedType.getReferencedType());

    exitContext(jsContext);
  }

  public void testResolveUsingErrorType() {
    JsContext jsContext = enterContext();

    JSTypeRegistry registry = jsContext.getFactory().getRegistry();
    ObjectType baseType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    NamedType namedType = new NamedType(registry, "Error", TEST_SOURCE_NAME, TEST_LINE_NUMBER, TEST_CHAR_NUMBER);
    assertEquals(ErrorType.instance, namedType.getReferencedType());

    exitContext(jsContext);
  }

  public void testResolveUsingUnknownType() {
    JsContext jsContext = enterContext();

    JSTypeRegistry registry = jsContext.getFactory().getRegistry();
    ObjectType baseType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    NamedType namedType = new NamedType(registry, "Unknown", TEST_SOURCE_NAME, TEST_LINE_NUMBER, TEST_CHAR_NUMBER);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), namedType.getReferencedType());

    exitContext(jsContext);
  }

  public void testHandleForwardDeclaredType() {
    JsContext jsContext = enterContext();

    JSTypeRegistry registry = jsContext.getFactory().getRegistry();
    ObjectType baseType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    registry.setForwardDeclaredType("MyForwardDeclaredType", null);

    NamedType namedType = new NamedType(registry, "MyForwardDeclaredType", TEST_SOURCE_NAME, TEST_LINE_NUMBER, TEST_CHAR_NUMBER);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), namedType.getReferencedType());

    exitContext(jsContext);
  }

  public void testResolveUsingUnresolvedType() {
    JsContext jsContext = enterContext();

    JSTypeRegistry registry = jsContext.getFactory().getRegistry();
    ObjectType baseType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    registry.setForwardDeclaredType("MyUnresolvedType", null);

    NamedType namedType = new NamedType(registry, "MyUnresolvedType", TEST_SOURCE_NAME, TEST_LINE_NUMBER, TEST_CHAR_NUMBER);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), namedType.getReferencedType());

    exitContext(jsContext);
  }

  // Additional tests for edge cases and error scenarios can be added here.

  public void testPropertyContinuation() {
    JsContext jsContext = enterContext();

    JSTypeRegistry registry = jsContext.getFactory().getRegistry();
    ObjectType baseType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    ObjectType testObjectType = new ObjectType(registry, "TestObject", baseType);

    NamedType namedType = new NamedType(registry, "TestObject.testProperty", TEST_SOURCE_NAME, TEST_LINE_NUMBER, TEST_CHAR_NUMBER);
    namedType.defineProperty("testProperty", StringType.getInstanceType(), false, null);

    exitContext(jsContext);
  }
}