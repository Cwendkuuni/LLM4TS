import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.newObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.newString;
import static com.google.javascript.rhino.NodeFactory.newCallExpression;
import static com.google.javascript.rhino.NodeFactory.newGetPropertyExpression;
import static com.google.javascript.rhino.NodeFactory.newIdentifier;
import static com.google.javascript.rhino.NodeFactory.newArrayLiteral;
import static com.google.javascript.rhino.NodeFactory.newFunctionExpression;
import static com.google.javascript.rhino.NodeFactory.newObjectType;
import static com.google.javascript.rhino.NodeFactory.newFunctionType;
import static com.google.javascript.rhino.NodeFactory.newObjectLiteralCast;
import static com.google.javascript.jscomp.ClosureCodingConvention.*;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = newFunctionType(newObjectType(null), newObjectType(null), null);
    FunctionType childCtor = newFunctionType(newObjectType(null), null, null);
    SubclassType type = SubclassType.INHERITS;
    new ClosureCodingConvention().applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals("superClass_", childCtor.getPrototype().getPrototype().getProperties().get("constructor").getName());
    assertEquals("constructor", childCtor.getPrototype().getProperties().get("constructor").getValue().getProperties().get("constructor").getName());
  }

  @Test
  public void testGetclasssDefinedByCall_inherit() {
    Node callNode = NodeFactory.createCallExpression(
        NodeFactory.createIdentifier("goog.inherits"),
        ImmutableList.of(
            NodeFactory.createIdentifier("SubClass"),
            NodeFactory.createIdentifier("SuperClass")
        ));
    SubclassRelationship relationship = new ClosureCodingConvention().getclasssDefinedByCall(callNode, null);
    assertTrue(relationship != null);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("SubClass", relationship.getSubclass().getName());
    assertEquals("SuperClass", relationship.getSuperclass().getName());
  }

  @Test
  public void testGetclasssDefinedByCall_mixin() {
    Node callNode = NodeFactory.createCallExpression(
        NodeFactory.createIdentifier("goog.mixin"),
        ImmutableList.of(
            NodeFactory.createGetPropertyExpression(
                NodeFactory.createIdentifier("SubClass.prototype"),
                "prototype"),
            NodeFactory.createGetPropertyExpression(
                NodeFactory.createIdentifier("SuperClass.prototype"),
                "prototype")
            ));
    SubclassRelationship relationship = new ClosureCodingConvention().getclasssDefinedByCall(callNode, null);
    assertTrue(relationship != null);
    assertEquals(SubclassType.MIXIN, relationship.getType());
    assertEquals("SubClass.prototype", relationship.getSubclass().getName());
    assertEquals("SuperClass.prototype", relationship.getSuperclass().getName());
  }

  @Test(expected = AssertionError.class)
  public void testGetclasssDefinedByCall_invalid() {
    Node callNode = NodeFactory.createCallExpression(
        NodeFactory.createIdentifier("goog.mixin"),
        ImmutableList.of(
            NodeFactory.createIdentifier("SubClass"),
            NodeFactory.createIdentifier("SuperClass"),
            NodeFactory.createIdentifier("ConditionalClass")
        ));
    new ClosureCodingConvention().getclasssDefinedByCall(callNode, null);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = NodeFactory.createCallExpression(
        NodeFactory.createIdentifier("goog.provide"),
        ImmutableList.of(newString("X")));
    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertEquals("X", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = NodeFactory.createCallExpression(
        NodeFactory.createIdentifier("goog.require"),
        ImmutableList.of(newString("X")));
    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
    assertEquals("X", className);
  }

  @Test
  public void testIsSuperClassReference() {
    assertTrue(new ClosureCodingConvention().isSuperClassReference("superClass_"));
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node call = NodeFactory.createCallExpression(
        NodeFactory.createGetPropertyExpression(
            NodeFactory.createIdentifier("goog"),
            "isDef"),
        ImmutableList.of(NodeFactory.createBooleanLiteral(true)));
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(call));
  }

  @Test
  public void testObjectLiteralCast() {
    Node callNode = NodeFactory.createCallExpression(
        NodeFactory.createIdentifier("goog.reflect.object"),
        ImmutableList.of(
            NodeFactory.createIdentifier("X"),
            NodeFactory.newObjectType(Lists.newArrayList(NodeFactory.newString("XType")))
        ));
    ObjectLiteralCast expected = new ObjectLiteralCast(
        "goog.reflect.object",
        newObjectLiteral(ImmutableList.of(newString("XType"))));
    ObjectLiteralCast actual = new ClosureCodingConvention().getObjectLiteralCast(null, callNode);
    assertEquals(expected, actual);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = newFunctionType(newObjectType(null), null, null);
    FunctionType getterType = newFunctionType(
        newObjectType(null), newObjectType(null), null);
    ObjectType objectType = newObjectType(null);
    new ClosureCodingConvention().applySingletonGetter(functionType, getterType, objectType);

    assertEquals("getInstance", getterType.getPrototype().getProperties().get("constructor").getName());
    assertEquals("instance_", getterType.getPrototype().getProperties().get("instance_").getName());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = NodeFactory.createCallExpression(
        NodeFactory.createIdentifier("goog.addDependency"),
        ImmutableList.of(
            NodeFactory.createArrayLiteral(
                ImmutableList.of(
                    NodeFactory.createStringLiteral("X"),
                    NodeFactory.createArrayLiteral(
                        ImmutableList.of(
                            NodeFactory.createStringLiteral("XType1"),
                            NodeFactory.createStringLiteral("XType2")
                        )
                    )
                )
            )
        ));
    List<String> typeNames = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    assertEquals(ImmutableList.of("XType1", "XType2"), typeNames);
  }

  @Test
  public void testGetAbstractMethodName() {
    assertEquals("goog.abstractMethod", new ClosureCodingConvention().getAbstractMethodName());
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = NodeFactory.createCallExpression(
        NodeFactory.createIdentifier("goog.addSingletonGetter"),
        ImmutableList.of(
            NodeFactory.createIdentifier("XSingletonGetter"),
            NodeFactory.createIdentifier("XSingleton")
        ));
    String className = new ClosureCodingConvention().getSingletonGetterClassName(callNode);
    assertEquals("XSingleton", className);
  }

  @Test
  public void testIsOptionalParameter() {
    Node parameter = NodeFactory.createIdentifier("p");
    assertFalse(new ClosureCodingConvention().isOptionalParameter(parameter));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameter = NodeFactory.createIdentifier("args");
    assertFalse(new ClosureCodingConvention().isVarArgsParameter(parameter));
  }

  @Test
  public void testIsPrivate() {
    assertFalse(new ClosureCodingConvention().isPrivate("privateVariable"));
  }

  @Test
  public void testGetAssertionFunctions() {
    List<AssertionFunctionSpec> assertionFunctions = new ClosureCodingConvention().getAssertionFunctions();
    assertEquals(7, assertionFunctions.size());
    assertEquals("goog.asserts.assert", assertionFunctions.get(0).getName());
    // Check the generic one.
    assertEquals(JSTypeNative.OBJECT_TYPE, assertionFunctions.get(6).getType());
  }

  @Test
  public void testDescribeFunctionBind() {
    Node node = NodeFactory.createCallExpression(
        NodeFactory.createIdentifier("goog.bind"),
        ImmutableList.of(
            NodeFactory.createIdentifier("X"),
            NodeFactory.createIdentifier("self"),
            NodeFactory.newArrayLiteral(ImmutableList.of(
                NodeFactory.newObjectLiteral(ImmutableList.of(
                    NodeFactory.newProperty("arg1", NodeFactory.newIdentifier("x"))),
                    NodeFactory.newProperty("arg2", NodeFactory.newIdentifier("y")))
                ))
            ));
    Bind expected = new Bind(
        NodeFactory.createIdentifier("X"),
        NodeFactory.createGetPropertyExpression(
            NodeFactory.createIdentifier("self"),
            "self"),
        NodeFactory.newArrayLiteral(ImmutableList.of(
            NodeFactory.newProperty("arg1", NodeFactory.createIdentifier("x")),
            NodeFactory.newProperty("arg2", NodeFactory.createIdentifier("y")))
        ));
    Bind actual = new ClosureCodingConvention().describeFunctionBind(node);
    assertEquals(expected, actual);
  }
}