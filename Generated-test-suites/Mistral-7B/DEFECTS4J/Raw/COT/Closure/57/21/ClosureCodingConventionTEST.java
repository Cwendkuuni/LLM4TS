import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureCodingConvention.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createMockFunctionType("ParentCtor");
    FunctionType childCtor = createMockFunctionType("ChildCtor");
    SubclassType type = SubclassType.INHERITS;

    ClosureCodingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals(parentCtor.getPrototype(), childCtor.getPrototype().get("superClass_"));
    assertEquals(childCtor, childCtor.getPrototype().get("constructor"));
  }

  @Test
  public void testApplySubclassRelationship_MIXIN() {
    FunctionType parentPrototype = createMockFunctionType("ParentPrototype");
    FunctionType childPrototype = createMockFunctionType("ChildPrototype");
    SubclassType type = SubclassType.MIXIN;

    Node parentPrototypeNode = Node.newNode(Token.GETPROP, null,
        Node.newQualifiedName("ParentPrototype"));
    Node childPrototypeNode = Node.newNode(Token.GETPROP, null,
        Node.newQualifiedName("ChildPrototype"));

    ClosureCodingConvention.applySubclassRelationship(parentPrototype, childPrototype, type);

    assertEquals(parentPrototype, childPrototypeNode.getFirstChild().getFirstChild());
  }

  @Test(expected = AssertionError.class)
  public void testApplySubclassRelationship_InvalidCall() {
    FunctionType parentCtor = createMockFunctionType("ParentCtor");
    FunctionType childCtor = createMockFunctionType("ChildCtor");

    ClosureCodingConvention.applySubclassRelationship(parentCtor, childCtor, null);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, null, Node.newQualifiedName("goog.provide")),
        Node.newNode(Token.STRING, null, "MyClass"));

    assertEquals("MyClass", ClosureCodingConvention.extractClassNameIfProvide(node, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, null, Node.newQualifiedName("goog.require")),
        Node.newNode(Token.STRING, null, "MyClass"));

    assertEquals("MyClass", ClosureCodingConvention.extractClassNameIfRequire(node, null));
  }

  @Test
  public void testExtractClassNameIfGoog_InvalidCall() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, null, Node.newQualifiedName("goog.invalidFunction")));

    assertNull(ClosureCodingConvention.extractClassNameIfGoog(node, null, "goog.provide"));
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty",
        ClosureCodingConvention.getExportPropertyFunction());
  }

  @Test
  public void testGetExportSymbolFunction() {
    assertEquals("goog.exportSymbol",
        ClosureCodingConvention.getExportSymbolFunction());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, null, Node.newQualifiedName("goog.addDependency")),
        Node.newNode(Token.ARRAYLIT, null),
        Node.newNode(Token.STRING, null, "MyType1"),
        Node.newNode(Token.STRING, null, "MyType2"));

    List<String> typeNames = ClosureCodingConvention.identifyTypeDeclarationCall(node);

    assertEquals(2, typeNames.size());
    assertTrue(typeNames.contains("MyType1"));
    assertTrue(typeNames.contains("MyType2"));
  }

  @Test
  public void testIdentifyTypeDeclarationCall_InvalidCall() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, null, Node.newQualifiedName("goog.invalidFunction")));

    assertNull(ClosureCodingConvention.identifyTypeDeclarationCall(node));
  }

  @Test
  public void testIsSuperClassReference() {
    assertTrue(ClosureCodingConvention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testIsSuperClassReference_InvalidName() {
    assertFalse(ClosureCodingConvention.isSuperClassReference("invalidName"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node node = Node.newNode(Token.GETPROP, null,
        Node.newQualifiedName("MyClass.prototype"));

    assertTrue(ClosureCodingConvention.endsWithPrototype(node));
  }

  @Test
  public void testEndsWithPrototype_InvalidCall() {
    Node node = Node.newNode(Token.GETPROP, null, Node.newQualifiedName("MyClass"));

    assertFalse(ClosureCodingConvention.endsWithPrototype(node));
  }

  private FunctionType createMockFunctionType(String name) {
    ObjectType objectType = ObjectType.newBuilder()
        .setName(name)
        .build();
    return FunctionType.newBuilder()
        .setName(name)
        .setReturnType(objectType)
        .build();
  }
}