import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;

public class ClosureCodingConventionTest {

  private NodeTraversal traversal;
  private Function<Node, ObjectLiteralCast> objectLiteralCast;

  @Before
  public void setup() {
    traversal = mock(NodeTraversal.class);
    objectLiteralCast = mock(Function.class);
  }

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = mock(FunctionType.class);
    FunctionType childCtor = mock(FunctionType.class);
    SubclassType type = SubclassType.INHERITS;
    when(parentCtor.getPrototype()).thenReturn(mock(ObjectType.class));
    when(parentCtor.getSource()).thenReturn(mock(Node.class));
    when(childCtor.defineDeclaredProperty(eq("superClass_"), eq(parentCtor),
        anyString()))
        .thenReturn(mock(Object.class));
    when(childCtor.defineDeclaredProperty(eq("constructor"), eq(childCtor),
        anyString()))
        .thenReturn(mock(Object.class));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, type);

    // Verify that superClass_ and constructor properties have been defined.
    assertEquals(parentCtor, convention.getPrototype("constructor", childCtor));
    assertEquals(parentCtor, convention.getPrototype("superClass_", childCtor));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getFirstChild()).thenReturn(mock(Node.class));
    when(node.getFirstChild().getQualifiedName()).thenReturn("goog.provide");
    when(node.getNext()).thenReturn(mock(Node.class));
    when(node.getNext().getQualifiedName()).thenReturn("X");

    assertEquals("X", new ClosureCodingConvention().extractClassNameIfProvide(node, parent));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getFirstChild()).thenReturn(mock(Node.class));
    when(node.getFirstChild().getQualifiedName()).thenReturn("goog.require");
    when(node.getNext()).thenReturn(mock(Node.class));
    when(node.getNext().getQualifiedName()).thenReturn("X");

    assertEquals("X", new ClosureCodingConvention().extractClassNameIfRequire(node, parent));
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty",
        new ClosureCodingConvention().getExportPropertyFunction());
  }

  @Test
  public void testGetExportSymbolFunction() {
    assertEquals("goog.exportSymbol",
        new ClosureCodingConvention().getExportSymbolFunction());
  }

  @Test
  public void testIsSuperClassReference() {
    assertEquals(true, new ClosureCodingConvention().isSuperClassReference("superClass_"));
    assertEquals(false, new ClosureCodingConvention().isSuperClassReference("someOtherProperty"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = mock(Node.class);
    when(qualifiedName.getType()).thenReturn(Token.GETPROP);
    when(qualifiedName.getLastChild().getString()).thenReturn("prototype");

    assertTrue(new ClosureCodingConvention().endsWithPrototype(qualifiedName));

    when(qualifiedName.getType()).thenReturn(Token.NAME);
    when(qualifiedName.getString()).thenReturn("a.b.c");

    assertFalse(new ClosureCodingConvention().endsWithPrototype(qualifiedName));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node callNode = mock(Node.class);
    when(callNode.getType()).thenReturn(Token.CALL);
    when(callNode.getFirstChild().getQualifiedName()).thenReturn("goog.reflect.object");
    when(callNode.getFirstChild().getNext().isQualifiedName()).thenReturn(true);
    when(callNode.getFirstChild().getNext().getQualifiedName()).thenReturn("X");
    when(callNode.getFirstChild().getNext().getNext()).thenReturn(mock(Node.class));

    ObjectLiteralCast result = new ClosureCodingConvention().getObjectLiteralCast(traversal, callNode);

    assertEquals("X", result.getTypeName());
    assertEquals(callNode.getFirstChild().getNext(), result.getValueNode());
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node call = mock(Node.class);
    when(call.getType()).thenReturn(Token.CALL);
    when(call.getFirstChild().getQualifiedName()).thenReturn("goog.isDef");

    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(call));
  }

  @Test
  public void testDescribeFunctionBind() {
    Node call = mock(Node.class);
    when(call.getType()).thenReturn(Token.CALL);
    when(call.getFirstChild().getQualifiedName()).thenReturn("goog.bind");
    when(call.getNext()).thenReturn(mock(Node.class));
    when(call.getNext().getQualifiedName()).thenReturn("fn");
    when(call.getNext().getNext()).thenReturn(mock(Node.class));

    Node thisValue = mock(Node.class);
    Node parameters = mock(Node.class);

    Bind result = new ClosureCodingConvention().describeFunctionBind(call);

    assertEquals("fn", result.getFunctionName());
    assertEquals(thisValue, result.getThisValue());
    assertEquals( ImmutableList.of(parameters), result.getArguments());
  }

  @Test(expected = AssertionError.class)
  public void testGetPrototype_nullParent() {
    FunctionType childCtor = mock(FunctionType.class);
    when(childCtor.getPrototype()).thenReturn(mock(ObjectType.class));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.getPrototype("superClass_", childCtor);
  }

  @Test(expected = AssertionError.class)
  public void testGetPrototype_nonObjectTypeParent() {
    FunctionType childCtor = mock(FunctionType.class);
    ObjectType parentType = mock(ObjectType.class);
    when(parentType.getPrototype()).thenReturn(mock(ObjectType.class));

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.getPrototype("superClass_", childCtor);
    convention.setPrototype("superClass_", childCtor, parentType);

    convention.getPrototype("superClass_", childCtor);
  }

  @Test
  public void testGetPrototype_noSetter() {
    FunctionType childCtor = mock(FunctionType.class);
    ObjectType parentType = mock(ObjectType.class);
    when(parentType.getPrototype()).thenReturn(mock(ObjectType.class));

    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertNull(convention.getPrototype("superClass_", childCtor));
  }

  @Test
  public void testGetPrototype_setterExistsButNullParent() {
    FunctionType childCtor = mock(FunctionType.class);
    ObjectType parentType = null;
    when(childCtor.getPrototype()).thenReturn(mock(ObjectType.class));

    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertNull(convention.getPrototype("superClass_", childCtor));
  }
}