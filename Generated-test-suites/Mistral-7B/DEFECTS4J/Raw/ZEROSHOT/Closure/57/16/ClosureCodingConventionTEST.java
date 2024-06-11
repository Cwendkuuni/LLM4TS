import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;

public class ClosureCodingConventionTest {

  private static final String CLASS_NAME = "MyClass";
  private static final String SUPER_CLASS_NAME = "SuperClass";

  @org.junit.Test
  public void testGetclasssDefinedByCall() {
    // Given
    Node node = createCallNode("goog.inherits(MyClass, SuperClass)");

    // When
    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);

    // Then
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals(CLASS_NAME, relationship.getSubclass());
    assertEquals(SUPER_CLASS_NAME, relationship.getSuperclass());
  }

  @org.junit.Test
  public void testGetclasssDefinedByCallWithMixin() {
    // Given
    Node node = createCallNode("goog.mixin(MyClass.prototype, SuperClass.prototype)");

    // When
    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);

    // Then
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals(CLASS_NAME, relationship.getSubclass());
    assertEquals(SUPER_CLASS_NAME, relationship.getSuperclass());
  }

  @org.junit.Test
  public void testGetclasssDefinedByCallWithInvalidSyntax() {
    // Given
    Node node = createCallNode("goog.inherits(MyClass, SuperClass, OtherClass)");

    // When
    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);

    // Then
    assertNull(relationship);
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    // Given
    Node node = createCallNode("goog.provide('MyClass')");

    // When
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfProvide(node, null);

    // Then
    assertEquals(CLASS_NAME, className);
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    // Given
    Node node = createCallNode("goog.require('MyClass')");

    // When
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfRequire(node, null);

    // Then
    assertEquals(CLASS_NAME, className);
  }

  @org.junit.Test
  public void testExtractClassNameIfGoogWithWrongCall() {
    // Given
    Node node = createCallNode("goog.something('MyClass')");

    // When
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfRequire(node, null);

    // Then
    assertNull(className);
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    // Given
    ClosureCodingConvention convention = new ClosureCodingConvention();

    // When
    boolean result = convention.isSuperClassReference("superClass_");

    // Then
    assertEquals(true, result);
  }

  @org.junit.Test
  public void testIsSuperClassReferenceWithOtherProperty() {
    // Given
    ClosureCodingConvention convention = new ClosureCodingConvention();

    // When
    boolean result = convention.isSuperClassReference("otherProperty");

    // Then
    assertEquals(false, result);
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    // Given
    ClosureCodingConvention convention = new ClosureCodingConvention();

    // When
    String functionName = convention.getExportPropertyFunction();

    // Then
    assertEquals("goog.exportProperty", functionName);
  }

  @org.junit.Test
  public void testGetExportSymbolFunction() {
    // Given
    ClosureCodingConvention convention = new ClosureCodingConvention();

    // When
    String functionName = convention.getExportSymbolFunction();

    // Then
    assertEquals("goog.exportSymbol", functionName);
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    // Given
    Node node = createTypeDeclarationCallNode();

    // When
    ClosureCodingConvention convention = new ClosureCodingConvention();
    List<String> typeNames = convention.identifyTypeDeclarationCall(node);

    // Then
    assertEquals(ImmutableList.of("MyType1", "MyType2"), typeNames);
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCallWithInvalidSyntax() {
    // Given
    Node node = createTypeDeclarationCallNodeWithInvalidSyntax();

    // When
    ClosureCodingConvention convention = new ClosureCodingConvention();
    List<String> typeNames = convention.identifyTypeDeclarationCall(node);

    // Then
    assertNull(typeNames);
  }

  @org.junit.Test
  public void testGetAbstractMethodName() {
    // Given
    ClosureCodingConvention convention = new ClosureCodingConvention();

    // When
    String functionName = convention.getAbstractMethodName();

    // Then
    assertEquals("goog.abstractMethod", functionName);
  }

  @org.junit.Test
  public void testGetSingletonGetterClassNameWithValidCall() {
    // Given
    Node node = createCallNode("goog.addSingletonGetter(MyClass.prototype, 'myProperty')");

    // When
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.getSingletonGetterClassName(node);

    // Then
    assertEquals("MyClass", className);
  }

  @org.junit.Test
  public void testGetSingletonGetterClassNameWithInvalidCall() {
    // Given
    Node node = createCallNode("goog.addSingletonGetter(MyClass, 'myProperty')");

    // When
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.getSingletonGetterClassName(node);

    // Then
    assertNull(className);
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    // Given
    Node node = createCallNode("goog.isNumber(42)");

    // When
    ClosureCodingConvention convention = new ClosureCodingConvention();
    boolean result = convention.isPropertyTestFunction(node);

    // Then
    assertEquals(true, result);
  }

  @org.junit.Test
  public void testIsPropertyTestFunctionWithInvalidCall() {
    // Given
    Node node = createCallNode("goog.something(42)");

    // When
    ClosureCodingConvention convention = new ClosureCodingConvention();
    boolean result = convention.isPropertyTestFunction(node);

    // Then
    assertEquals(false, result);
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    // Given
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node callNode = createCallNodeWithObjectLiteralCast("goog.reflect.object.getPrototypeOf");

    // When
    ClosureCodingConvention convention = new ClosureCodingConvention();
    ObjectLiteralCast objectLiteralCast = convention.getObjectLiteralCast(traversal, callNode);

    // Then
    assertEquals("goog.reflect.Object", objectLiteralCast.getTypeName());
    assertEquals("prototype", objectLiteralCast.getPropertyName());
  }

  @org.junit.Test
  public void testGetObjectLiteralCastWithInvalidCall() {
    // Given
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node callNode = createCallNode("goog.reflect.object.getPrototypeOf(42)");

    // When
    ClosureCodingConvention convention = new ClosureCodingConvention();
    ObjectLiteralCast objectLiteralCast = convention.getObjectLiteralCast(traversal, callNode);

    // Then
    assertNull(objectLiteralCast);
  }

  private Node createCallNode(String callName) {
    Node callNode = new Node(Token.CALL, new Node[0]);
    Node callNameNode = new Node(Token.GETPROP, new Node(Token.NAME, callName.split("\\.")[0]),
        new Node(Token.NAME, callName.split("\\.")[1]));
    callNode.setFirstChild(callNameNode);
    return callNode;
  }

  private Node createCallNodeWithObjectLiteralCast(String callName) {
    Node callNode = createCallNode(callName);
    Node objectLiteralNode = new Node(Token.OBJECTLIT, new Node[0]);
    Node objectLiteralNameNode = new Node(Token.GETPROP, new Node(Token.NAME, "prototype"),
        new Node(Token.NAME, "goog.reflect.Object"));
    objectLiteralNode.setFirstChild(objectLiteralNameNode);
    callNode.getLastChild().setNext(objectLiteralNode);
    return callNode;
  }

  private Node createTypeDeclarationCallNode() {
    Node typeDeclarationNode = new Node(Token.CALL, new Node[0]);
    Node typeDeclarationCallNameNode = new Node(Token.GETPROP, new Node(Token.NAME, "goog"),
        new Node(Token.NAME, "addType"));
    Node typeDeclarationArgumentsNode = new Node(Token.ARRAYLIT, new Node[0]);
    Node myType1NameNode = new Node(Token.NAME, "MyType1");
    Node myType2NameNode = new Node(Token.NAME, "MyType2");
    typeDeclarationArgumentsNode.setFirstChild(myType1NameNode);
    typeDeclarationArgumentsNode.getLastChild().setNext(myType2NameNode);
    typeDeclarationCallNameNode.setNext(typeDeclarationArgumentsNode);
    typeDeclarationNode.setFirstChild(typeDeclarationCallNameNode);
    return typeDeclarationNode;
  }

  private Node createTypeDeclarationCallNodeWithInvalidSyntax() {
    Node typeDeclarationNode = new Node(Token.CALL, new Node[0]);
    Node typeDeclarationCallNameNode = new Node(Token.GETPROP, new Node(Token.NAME, "goog"),
        new Node(Token.NAME, "addType"));
    Node typeDeclarationArgumentsNode = new Node(Token.ARRAYLIT, new Node[0]);
    Node myType1NameNode = new Node(Token.NAME, "MyType1");
    Node myType2NameNode = new Node(Token.NAME, "MyType2");
    typeDeclarationArgumentsNode.setFirstChild(myType1NameNode);
    Node invalidSyntaxNode = new Node(Token.NAME, "invalidSyntax");
    typeDeclarationArgumentsNode.getLastChild().setNext(invalidSyntaxNode);
    typeDeclarationCallNameNode.setNext(typeDeclarationArgumentsNode);
    typeDeclarationNode.setFirstChild(typeDeclarationCallNameNode);
    return typeDeclarationNode;
  }

}

enum SubclassType {
  INHERITS,
  MIXIN
}

class AssertionFunctionSpec {
  private final String functionName;
  private final JSTypeNative returnType;

  AssertionFunctionSpec(String functionName) {
    this.functionName = functionName;
    this.returnType = null;
  }

  AssertionFunctionSpec(String functionName, JSTypeNative returnType) {
    this.functionName = functionName;
    this.returnType = returnType;
  }

  String getFunctionName() {
    return functionName;
  }

  JSTypeNative getReturnType() {
    return returnType;
  }
}

class Bind {
  private final Node function;
  private final Node thisValue;
  private final Node parameters;

  Bind(Node function, Node thisValue, Node parameters) {
    this.function = function;
    this.thisValue = thisValue;
    this.parameters = parameters;
  }

  Node getFunction() {
    return function;
  }

  Node getThisValue() {
    return thisValue;
  }

  Node getParameters() {
    return parameters;
  }
}