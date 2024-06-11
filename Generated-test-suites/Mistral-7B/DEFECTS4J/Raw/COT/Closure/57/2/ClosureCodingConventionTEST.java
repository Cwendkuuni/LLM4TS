import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureCodingConvention.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType(JSTypeNative.OBJECT_TYPE, new FunctionType[0]);
    FunctionType childCtor = new FunctionType(JSTypeNative.OBJECT_TYPE, new FunctionType[0]);
    SubclassType type = SubclassType.INHERITS;

    applySubclassRelationship(parentCtor, childCtor, type);

    FunctionType expectedParentCtor = new FunctionType(JSTypeNative.OBJECT_TYPE, new FunctionType[0]);
    FunctionType expectedChildCtor = new FunctionType(JSTypeNative.OBJECT_TYPE, new FunctionType[0]);
    expectedChildCtor.defineDeclaredProperty("superClass_", expectedParentCtor, null);
    expectedChildCtor.getPrototype().defineDeclaredProperty("constructor", expectedChildCtor, null);

    assertEquals(expectedParentCtor, parentCtor);
    assertEquals(expectedChildCtor, childCtor.getPrototype());
  }

  @Test
  public void testGetclasssDefinedByCall_SubClass_SuperClass() {
    Node subclassNode = Node.newUnscopedNameNode("SubClass");
    Node superclassNode = Node.newUnscopedNameNode("SuperClass");
    Node callNode = Node.newCall(superclassNode, "inherits", Arrays.asList(subclassNode));
    SubclassRelationship expectedRelationship = new SubclassRelationship(SubclassType.INHERITS, subclassNode, superclassNode);

    SubclassRelationship actualRelationship = getclasssDefinedByCall(callNode);

    assertEquals(expectedRelationship, actualRelationship);
  }

  @Test
  public void testGetclasssDefinedByCall_SubClass_SuperClass_Deprecated() {
    Node subclassNode = Node.newUnscopedNameNode("SubClass");
    Node superclassNode = Node.newUnscopedNameNode("SuperClass");
    Node callNode = Node.newCall(superclassNode, "inherits", Arrays.asList(subclassNode));
    SubclassRelationship expectedRelationship = new SubclassRelationship(SubclassType.INHERITS, subclassNode, superclassNode);

    SubclassRelationship actualRelationship = getclasssDefinedByCall(callNode);

    assertEquals(expectedRelationship, actualRelationship);
  }

  @Test
  public void testGetclasssDefinedByCall_SubClass_SuperClassPrototype() {
    Node subclassNode = Node.newUnscopedNameNode("SubClass");
    Node superclassPrototypeNode = Node.newCall(Node.newUnscopedNameNode("SuperClass"), "prototype");
    Node callNode = Node.newCall(superclassPrototypeNode, "mixin", Arrays.asList(subclassNode.getPrototype()));
    SubclassRelationship expectedRelationship = new SubclassRelationship(SubclassType.MIXIN, subclassNode, superclassPrototypeNode);

    SubclassRelationship actualRelationship = getclasssDefinedByCall(callNode);

    assertEquals(expectedRelationship, actualRelationship);
  }

  @Test
  public void testGetclasssDefinedByCall_SubClass_SuperClassPrototype_Deprecated() {
    Node subclassNode = Node.newUnscopedNameNode("SubClass");
    Node superclassPrototypeNode = Node.newCall(Node.newUnscopedNameNode("SuperClass"), "prototype");
    Node callNode = Node.newCall(superclassPrototypeNode, "mixin", Arrays.asList(subclassNode));
    SubclassRelationship expectedRelationship = new SubclassRelationship(SubclassType.MIXIN, subclassNode, superclassPrototypeNode);

    SubclassRelationship actualRelationship = getclasssDefinedByCall(callNode);

    assertEquals(expectedRelationship, actualRelationship);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node callNode = Node.newCall(Node.newUnscopedNameNode("goog.provide"), Arrays.asList(Node.newStringLiteral("X")));
    String expectedClassName = "X";

    String actualClassName = extractClassNameIfProvide(callNode, null);

    assertEquals(expectedClassName, actualClassName);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node callNode = Node.newCall(Node.newUnscopedNameNode("goog.require"), Arrays.asList(Node.newStringLiteral("X")));
    String expectedClassName = "X";

    String actualClassName = extractClassNameIfRequire(callNode, null);

    assertEquals(expectedClassName, actualClassName);
  }

  @Test
  public void testIsSuperClassReference() {
    assertTrue(isSuperClassReference("superClass_"));
    assertFalse(isSuperClassReference("otherProperty"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = Node.newCall(Node.newUnscopedNameNode("a"), "b", Arrays.asList(Node.newStringLiteral("c.prototype")));
    assertFalse(endsWithPrototype(qualifiedName));

    qualifiedName = Node.newCall(Node.newUnscopedNameNode("a"), "b.prototype");
    assertTrue(endsWithPrototype(qualifiedName));
  }

  @Test
  public void testExtractClassNameIfGoog_provide() {
    Node callNode = Node.newCall(Node.newUnscopedNameNode("goog.provide"), Arrays.asList(Node.newStringLiteral("X")));
    String expectedClassName = "X";

    String actualClassName = extractClassNameIfGoog(callNode, null, "goog.provide");

    assertEquals(expectedClassName, actualClassName);
  }

  @Test
  public void testExtractClassNameIfGoog_require() {
    Node callNode = Node.newCall(Node.newUnscopedNameNode("goog.require"), Arrays.asList(Node.newStringLiteral("X")));
    String expectedClassName = "X";

    String actualClassName = extractClassNameIfGoog(callNode, null, "goog.require");

    assertEquals(expectedClassName, actualClassName);
  }

  @Test
  public void testGetExportPropertyFunction() {
    String expectedFunctionName = "goog.exportProperty";

    String actualFunctionName = getExportPropertyFunction();

    assertEquals(expectedFunctionName, actualFunctionName);
  }

  @Test
  public void testGetExportSymbolFunction() {
    String expectedFunctionName = "goog.exportSymbol";

    String actualFunctionName = getExportSymbolFunction();

    assertEquals(expectedFunctionName, actualFunctionName);
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node typeDeclarationCallNode = Node.newCall(Node.newUnscopedNameNode("goog.addDependency"), Arrays.asList(
        Node.newArrayLiteral(
          Node.newStringLiteral("X"),
          Node.newObjectLiteral(
            ImmutableList.of(
              Node.newProperty(
                Node.newStringLiteral("X"),
                Node.newFunctionType(JSTypeNative.OBJECT_TYPE, new FunctionType[0])
              )
            )
          )
        )
    ));

    List<String> expectedTypeNames = ImmutableList.of("X");
    List<String> actualTypeNames = identifyTypeDeclarationCall(typeDeclarationCallNode);

    assertEquals(expectedTypeNames, actualTypeNames);
  }

  @Test
  public void testGetAbstractMethodName() {
    String expectedAbstractMethodName = "goog.abstractMethod";

    String actualAbstractMethodName = getAbstractMethodName();

    assertEquals(expectedAbstractMethodName, actualAbstractMethodName);
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = Node.newCall(Node.newUnscopedNameNode("goog.addSingletonGetter"), Arrays.asList(
        Node.newUnscopedNameNode("SingletonGetterClass")
    ));
    Node getterClassNameNode = callNode.getNext();

    String expectedGetterClassName = "SingletonGetterClass";
    String actualGetterClassName = getSingletonGetterClassName(callNode);

    assertEquals(expectedGetterClassName, actualGetterClassName);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType(JSTypeNative.OBJECT_TYPE, new FunctionType[0]);
    FunctionType getterType = new FunctionType(JSTypeNative.OBJECT_TYPE, new FunctionType[0]);
    ObjectType objectType = new ObjectType(JSTypeNative.OBJECT_TYPE);

    applySingletonGetter(functionType, getterType, objectType);

    FunctionType expectedFunctionType = new FunctionType(JSTypeNative.OBJECT_TYPE, new FunctionType[0]);
    FunctionType expectedGetterType = new FunctionType(JSTypeNative.OBJECT_TYPE, new FunctionType[0]);
    expectedGetterType.defineDeclaredProperty("getInstance", getterType, null);
    expectedGetterType.defineDeclaredProperty("instance_", objectType, null);

    assertEquals(expectedFunctionType, functionType);
    assertEquals(expectedGetterType, functionType.getPrototype());
  }

  @Test
  public void testGetGlobalObject() {
    String expectedGlobalObject = "goog.global";

    String actualGlobalObject = getGlobalObject();

    assertEquals(expectedGlobalObject, actualGlobalObject);
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node callNode = Node.newCall(Node.newUnscopedNameNode("goog.isDef"), Arrays.asList(Node.newBooleanLiteral(true)));

    boolean expectedResult = true;
    boolean actualResult = isPropertyTestFunction(callNode);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node callNode = Node.newCall(Node.newUnscopedNameNode("goog.reflect.object"), "cast", Arrays.asList(
        Node.newUnscopedNameNode("goog.Type"),
        Node.newObjectLiteral(
          ImmutableList.of(
            Node.newProperty(
              Node.newStringLiteral("foo"),
              Node.newStringLiteral("bar")
            )
          )
        )
    ));

    ObjectLiteralCast expectedCast = new ObjectLiteralCast(Node.newUnscopedNameNode("goog.Type"), Node.newObjectLiteral(ImmutableList.of(Node.newProperty(Node.newStringLiteral("foo"), Node.newStringLiteral("bar")))));

    ObjectLiteralCast actualCast = getObjectLiteralCast(NodeUtil.createNodeTraversal(null), callNode);

    assertEquals(expectedCast, actualCast);
  }

  @Test
  public void testIsOptionalParameter() {
    Node parameterNode = Node.newCall(Node.newUnscopedNameNode("goog.isDefAndNotNull"), Arrays.asList(Node.newNull()));

    assertFalse(isOptionalParameter(parameterNode));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node parameterNode = Node.newCall(Node.newUnscopedNameNode("goog.isFunction"), Arrays.asList(Node.newCall(Node.newUnscopedNameNode("goog.Function"), Arrays.asList(Node.newArrayLiteral(Node.newStringLiteral("args"))))));

    assertFalse(isVarArgsParameter(parameterNode));
  }

  @Test
  public void testIsPrivate() {
    String name = "privateVariable";

    assertFalse(isPrivate(name));
  }

  @Test
  public void testGetAssertionFunctions() {
    Collection<AssertionFunctionSpec> expectedAssertionFunctions = ImmutableList.of(
        new AssertionFunctionSpec("goog.asserts.assert"),
        new AssertionFunctionSpec("goog.asserts.assertNumber", JSTypeNative.NUMBER_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertString", JSTypeNative.STRING_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertFunction", JSTypeNative.FUNCTION_INSTANCE_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertObject", JSTypeNative.OBJECT_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertArray", JSTypeNative.ARRAY_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertInstanceof", JSTypeNative.OBJECT_TYPE)
    );

    Collection<AssertionFunctionSpec> actualAssertionFunctions = getAssertionFunctions();

    assertEquals(expectedAssertionFunctions, actualAssertionFunctions);
  }

  @Test
  public void testDescribeFunctionBind() {
    Node functionNode = Node.newCall(Node.newUnscopedNameNode("goog.bind"), Arrays.asList(
        Node.newUnscopedNameNode("function"),
        Node.newNull(),
        Node.newArrayLiteral(Node.newStringLiteral("thisValue"))
    ));

    Bind expectedBind = new Bind(Node.newUnscopedNameNode("function"), Node.newNull(), Node.newArrayLiteral(Node.newStringLiteral("thisValue")));

    Bind actualBind = describeFunctionBind(functionNode);

    assertEquals(expectedBind, actualBind);
  }
}