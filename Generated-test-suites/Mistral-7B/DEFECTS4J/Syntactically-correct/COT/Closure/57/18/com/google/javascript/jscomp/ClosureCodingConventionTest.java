import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.newObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.newString;
import static com.google.javascript.rhino.NodeFactory.newToken;
import static com.google.javascript.rhino.NodeFactory.newCallExpression;
import static com.google.javascript.rhino.NodeFactory.newIdentifier;
import static com.google.javascript.rhino.NodeFactory.newQualifiedIdentifier;
import static com.google.javascript.rhino.NodeFactory.newGetProperty;
import static com.google.javascript.rhino.NodeFactory.newArrayLiteral;
import static com.google.javascript.rhino.NodeFactory.newObjectType;
import static com.google.javascript.rhino.NodeFactory.newFunctionType;
import static com.google.javascript.rhino.NodeFactory.newObject;
import static com.google.javascript.rhino.NodeFactory.newName;
import static com.google.javascript.rhino.NodeFactory.newGetPropertyStatic;
import static com.google.javascript.rhino.NodeFactory.newCallExpressionStatic;
import static com.google.javascript.rhino.NodeFactory.newProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    CodingConvention convention = new ClosureCodingConvention();

    FunctionType parentCtor = newFunctionType(newObjectType(null), null);
    FunctionType childCtor = newFunctionType(newObjectType(null), null);
    SubclassType type = SubclassType.INHERITS;

    convention.applySubclassRelationship(parentCtor, childCtor, type);

    ObjectType prototype = parentCtor.getPrototype();
    FunctionType childPrototype = childCtor.getPrototype();

    assertNotNull(parentCtor.getDeclaredProperty("superClass_"));
    assertEquals(parentCtor, parentCtor.getDeclaredProperty("superClass_").getValue().getValue());

    assertNotNull(childPrototype.getDeclaredProperty("constructor"));
    assertEquals(childCtor, childPrototype.getDeclaredProperty("constructor").getValue().getValue());

    // Test with mixin
    type = SubclassType.MIXIN;
    SubclassRelationship relationship = convention.getclasssDefinedByCall(newNode(), newNode());
    assertNull(relationship);

    prototype = newObjectType("prototype");
    childPrototype = newObjectType("prototype");

    Node subclass = newIdentifier("SubClass");
    Node superclass = newQualifiedIdentifier("SuperClass");

    Node inheritedCall = newCallExpression(newGetPropertyStatic(newIdentifier("goog"), "inherits"), newArrayLiteral(subclass, superclass));
    relationship = convention.getclasssDefinedByCall(inheritedCall, newNode());

    assertEquals(type, relationship.getType());
    assertEquals(subclass, relationship.getSubclass());
    assertEquals(superclass, relationship.getSuperclass());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfProvide(newNode(), newNode());
    assertNull(className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfRequire(newNode(), newNode());
    assertNull(className);
  }

  @Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String functionName = convention.getExportPropertyFunction();
    assertEquals("goog.exportProperty", functionName);
  }

  @Test
  public void testGetExportSymbolFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String functionName = convention.getExportSymbolFunction();
    assertEquals("goog.exportSymbol", functionName);
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Node n = newCallExpression(newIdentifier("goog.addDependency"), newArrayLiteral(newString("Type1"), newArrayLiteral(newString("Type2"))));
    List<String> typeNames = convention.identifyTypeDeclarationCall(n);
    List<String> expected = Lists.newArrayList("Type1", "Type2");
    assertEquals(expected, typeNames);
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("superClass"));
  }

  @Test
  public void testEndsWithPrototype() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Node qualifiedName = newQualifiedIdentifier("a.b.c");
    assertFalse(convention.endsWithPrototype(qualifiedName));

    qualifiedName = newQualifiedIdentifier("a.b.c.prototype");
    assertTrue(convention.endsWithPrototype(qualifiedName));
  }

  @Test
  public void testGetAbstractMethodName() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    String abstractMethodName = convention.getAbstractMethodName();
    assertEquals("goog.abstractMethod", abstractMethodName);
  }

  @Test
  public void testGetSingletonGetterClassName() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Node callNode = newCallExpression(newIdentifier("goog.addSingletonGetter"), newArrayLiteral(newString("SingletonGetterName")));
    String className = convention.getSingletonGetterClassName(callNode);
    assertEquals("SingletonGetterName", className);
  }

  @Test
  public void testApplySingletonGetter() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    FunctionType functionType = newFunctionType(newObjectType(null), null);
    FunctionType getterType = newFunctionType(newObjectType(null), newArrayLiteral(newJSTypeNative(JSTypeNative.NULL_TYPE)));
    ObjectType objectType = newObjectType(null);
    convention.applySingletonGetter(functionType, getterType, objectType);

    assertEquals(2, functionType.getProperties().size());
    assertEquals("getInstance", functionType.getPropertyName(0));
    assertEquals("instance_", functionType.getPropertyName(1));

    assertEquals(getterType, functionType.getProperty("getInstance").getValue().getValue().getType());
    assertEquals(objectType, functionType.getProperty("instance_").getValue().getValue().getType());
  }

  @Test
  public void testIsPropertyTestFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Node call = newCallExpression(newGetProperty(newIdentifier("goog"), "isDef"), newArrayLiteral(newString("test")));
    assertTrue(convention.isPropertyTestFunction(call));
  }

  @Test
  public void testGetObjectLiteralCast() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Node callNode = newCallExpression(newQualifiedIdentifier("goog.reflect.object"), newArrayLiteral(newString("type"), newObjectLiteral(newProperty("prop", newString("value")))));
    ObjectLiteralCast objectLiteralCast = convention.getObjectLiteralCast(newNodeTraversal(newNode()), callNode);
    assertEquals("type", objectLiteralCast.getTypeName());
    assertEquals("prop", objectLiteralCast.getPropertyName());
  }

  @Test
  public void testIsOptionalParameter() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Node parameter = newNode();
    assertFalse(convention.isOptionalParameter(parameter));
  }

  @Test
  public void testIsVarArgsParameter() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Node parameter = newNode();
    assertFalse(convention.isVarArgsParameter(parameter));
  }

  @Test
  public void testIsPrivate() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertFalse(convention.isPrivate("name"));
  }

  @Test
  public void testGetAssertionFunctions() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    List<AssertionFunctionSpec> assertionFunctions = convention.getAssertionFunctions();
    Set<String> expectedFunctionNames = Sets.newHashSet("goog.asserts.assert",
        "goog.asserts.assertNumber",
        "goog.asserts.assertString",
        "goog.asserts.assertFunction",
        "goog.asserts.assertObject",
        "goog.asserts.assertArray",
        "goog.asserts.assertInstanceof");

    for (AssertionFunctionSpec functionSpec : assertionFunctions) {
      expectedFunctionNames.remove(functionSpec.getName());
    }

    assertTrue(expectedFunctionNames.isEmpty());
  }

  @Test
  public void testDescribeFunctionBind() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Node n = newCallExpression(newIdentifier("goog.bind"), newArrayLiteral(newIdentifier("fn"), newNode(), newArrayLiteral(newNode(), newNode())));
    Bind bind = convention.describeFunctionBind(n);
    assertEquals("fn", bind.getName());
    assertEquals(newNode(), bind.getThisValue());
    assertEquals(newArrayLiteral(newNode(), newNode()), bind.getParameters());
  }
}