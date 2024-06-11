import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.TypeFactory;
import com.google.javascript.rhino.error.ErrorReporter;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.testing.TypeTestUtils;

import java.util.List;

public class NamedTypeTest {

  private static NodeFactory nodeFactory = NodeFactory.getInitialNodeFactory();
  private static ErrorReporter errorReporter = new ErrorReporter();
  private static JSTypeRegistry typeRegistry = new JSTypeRegistry();
  private static JSTypeFactory typeFactory = new JSTypeFactory(typeRegistry);
  private static TypeFactory typeFactoryWithTypes = new TypeFactory(typeRegistry);

  @org.junit.BeforeClass
  public static void setUp() {
    typeRegistry.put("Number", NUMBER);
    typeRegistry.put("String", STRING);
    typeRegistry.put("Boolean", BOOLEAN);
    typeRegistry.put("Array", new ArrayType("*"));
  }

  @org.junit.Test
  public void testCreateAndResolveNamedType() {
    String reference = "Number";
    NamedType namedType = new NamedType(typeRegistry, reference, "Test", 1, 1);
    assertEquals(reference, namedType.getReferenceName());
    assertEquals(Type.UNDEFINED, namedType.getType(errorReporter).getType());
    typeRegistry.put(reference, NUMBER);
    Type type = namedType.getType(errorReporter).getType();
    assertEquals(NUMBER.getClass(), type.getClass());
    assertSame(NUMBER, type);
  }

  @org.junit.Test
  public void testCreateAndResolveNamedTypeWithForwardDeclaration() {
    String reference = "TestType";
    typeRegistry.putForwardDeclaration(reference);
    NamedType namedType = new NamedType(typeRegistry, reference, "Test", 1, 1);
    assertTrue(namedType.isForwardDeclared());
    assertEquals(Type.UNDEFINED, namedType.getType(errorReporter).getType());
    typeRegistry.put(reference, NUMBER);
    Type type = namedType.getType(errorReporter).getType();
    assertEquals(NUMBER.getClass(), type.getClass());
    assertSame(NUMBER, type);
  }

  @org.junit.Test
  public void testCreateAndResolveNamedTypeWithCycle() {
    String reference1 = "Type1";
    String reference2 = "Type2";
    typeRegistry.putForwardDeclaration(reference1);
    typeRegistry.putForwardDeclaration(reference2);
    NamedType namedType1 = new NamedType(typeRegistry, reference1, "Test1", 1, 1);
    NamedType namedType2 = new NamedType(typeRegistry, reference2, "Test2", 1, 1);
    typeRegistry.put(reference1, namedType2.getClass());
    typeRegistry.put(reference2, namedType1.getClass());
    Type type1 = namedType1.getType(errorReporter).getType();
    Type type2 = namedType2.getType(errorReporter).getType();
    assertEquals(namedType2.getClass(), type1);
    assertEquals(namedType1.getClass(), type2);
  }

  @org.junit.Test
  public void testCreateAndResolveNamedTypeWithInvalidReference() {
    String reference = "InvalidType";
    NamedType namedType = new NamedType(typeRegistry, reference, "Test", 1, 1);
    Type type = namedType.getType(errorReporter).getType();
    assertEquals(Type.OBJECT, type.getType());
    assertFalse(type.isInstanceOf(NUMBER));
    assertFalse(type.isInstanceOf(STRING));
    assertFalse(type.isInstanceOf(BOOLEAN));
  }

  @org.junit.Test
  public void testCreateAndResolveNamedTypeWithFunctionType() {
    String reference = "FunctionType";
    NamedType namedType = new NamedType(typeRegistry, reference, "Test", 1, 1);
    typeRegistry.put(reference, new FunctionType(new ArrayType("*"), VOID));
    Type type = namedType.getType(errorReporter).getType();
    assertEquals(FunctionType.class, type.getClass());
  }

  @org.junit.Test
  public void testCreateAndResolveNamedTypeWithEnumType() {
    String reference = "MyEnum";
    typeRegistry.putForwardDeclaration(reference);
    NamedType namedType = new NamedType(typeRegistry, reference, "Test", 1, 1);
    assertEquals(Type.UNDEFINED, namedType.getType(errorReporter).getType());
    Type enumType =
        TypeTestUtils.createEnumType(typeFactoryWithTypes, "MyEnum", NUMBER);
    typeRegistry.put(reference, enumType);
    Type type = namedType.getType(errorReporter).getType();
    assertEquals(enumType.getClass(), type.getClass());
    assertSame(enumType, type);
  }

  @org.junit.Test
  public void testCreateAndResolveNamedTypeWithEnumElementType() {
    String reference = "MyEnum.ELEMENT";
    typeRegistry.putForwardDeclaration("MyEnum", new EnumType("MyEnum"));
    NamedType namedType = new NamedType(typeRegistry, reference, "Test", 1, 1);
    assertEquals(Type.UNDEFINED, namedType.getType(errorReporter).getType());
    Type enumType = typeRegistry.getType("MyEnum");
    Type elementType = enumType.getElementsType();
    assertEquals(elementType.getClass(), namedType.getClass());
    assertSame(elementType, namedType);
  }

  @org.junit.Test
  public void testCreateAndResolveNamedTypeWithNoObjectType() {
    String reference = "Object";
    NamedType namedType = new NamedType(typeRegistry, reference, "Test", 1, 1);
    Type type = namedType.getType(errorReporter).getType();
    assertEquals(JSTypeNative.NO_OBJECT_TYPE, type);
  }

  @org.junit.Test
  public void testCreateAndResolveNamedTypeWithUnknownType() {
    String reference = "UnknownType";
    NamedType namedType = new NamedType(typeRegistry, reference, "Test", 1, 1);
    Type type = namedType.getType(errorReporter).getType();
    assertEquals(JSTypeNative.UNKNOWN_TYPE, type);
  }

  @org.junit.Test
  public void testCreateAndResolveNamedTypeWithInvalidProperties() {
    String reference = "NamedTypeWithInvalidProperties";
    NamedType namedType = new NamedType(typeRegistry, reference, "Test", 1, 1);
    PropertyContinuation continuation = new PropertyContinuation("prop", NUMBER, true, null);
    namedType.defineProperty("prop", NUMBER, true, null);
    Type type = namedType.getType(errorReporter).getType();
    assertEquals(ObjectType.class, type.getClass());
    ObjectType objectType = (ObjectType) type;
    assertEquals(1, objectType.getProperties().size());
    assertEquals("prop", objectType.getPropertyNames().get(0));
  }
}

class PropertyContinuation {
  private String propertyName;
  private Type type;
  private boolean inferred;
  private Node propertyNode;

  PropertyContinuation(String propertyName, Type type, boolean inferred, Node propertyNode) {
    this.propertyName = propertyName;
    this.type = type;
    this.inferred = inferred;
    this.propertyNode = propertyNode;
  }
}