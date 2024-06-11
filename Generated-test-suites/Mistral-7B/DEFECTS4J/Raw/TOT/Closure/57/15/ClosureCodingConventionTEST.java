import static com.google.javascript.jscomp.ClosureCodingConvention.*;
import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.jscomp.SubclassRelationship;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = newFunctionType("ParentCtor", ImmutableList.of(),
        new ObjectType("ParentType"));
    FunctionType childCtor = newFunctionType("ChildCtor", ImmutableList.of(),
        new ObjectType("ChildType"));

    CodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertEquals("ParentType", childCtor.getPrototype().getClassName());
    assertEquals("ParentCtor", childCtor.getPrototype().getConstructorName());
    assertEquals("ParentType", childCtor.getClassName());
    assertEquals("ChildCtor", childCtor.getConstructorName());
  }

  @Test
  public void testApplySubclassRelationshipWithMixin() {
    FunctionType parentCtor = newFunctionType("ParentCtor", ImmutableList.of(),
        new ObjectType("ParentType"));
    FunctionType childCtor = newFunctionType("ChildCtor", ImmutableList.of(),
        new ObjectType("ChildType"));

    FunctionType mixinCtor = newFunctionType("MixinCtor", ImmutableList.of(),
        new ObjectType("MixinType"));

    FunctionType childPrototype = new FunctionType("ChildPrototype",
        ImmutableList.of(), new ObjectType("ChildType"));
    FunctionType mixinPrototype = new FunctionType("MixinPrototype",
        ImmutableList.of(), new ObjectType("MixinType"));

    CodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.MIXIN);

    convention.applySubclassRelationship(childPrototype, mixinPrototype,
        SubclassType.INHERITS);

    assertEquals("ParentType", childPrototype.getClassName());
    assertEquals("ParentCtor", childPrototype.getConstructorName());
    assertEquals("MixinType", mixinPrototype.getClassName());
    assertEquals("MixinCtor", mixinPrototype.getConstructorName());
    assertEquals("ChildType", childPrototype.getPrototype().getClassName());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = newNode("goog.provide('X')");
    assertEquals("X", new ClosureCodingConvention().extractClassNameIfProvide(node, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = newNode("goog.require('X')");
    assertEquals("X", new ClosureCodingConvention().extractClassNameIfRequire(node, null));
  }

  @Test
  public void testIsSuperClassReference() {
    CodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("otherProperty"));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType singletonGetter = newFunctionType("SingletonGetter",
        ImmutableList.of(), new ObjectType("SingletonType"));
    FunctionType instanceType = new FunctionType("InstanceType",
        ImmutableList.of(), new ObjectType("InstanceType"));

    ObjectType instance = new ObjectType("Instance");
    instance.setSuperclass(singletonGetter.getPrototype());

    CodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(singletonGetter, instance, instanceType);

    assertEquals(singletonGetter, instanceType.getPrototype().getConstructor());
    assertEquals(instanceType, instance.getClassName());
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node node = newNode(
        "goog.reflect.object.cast(new Object(), 'MyType')");
    ObjectLiteralCast cast = new ClosureCodingConvention()
        .getObjectLiteralCast(new NodeTraversal(node), node);
    assertEquals("MyType", cast.getTypeName());
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = newNode("goog.isDef(x)");
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = newNode("goog.addDependency(['X', 'Y'])");
    List<String> types = new ClosureCodingConvention()
        .identifyTypeDeclarationCall(node);
    assertEquals(ImmutableList.of("X", "Y"), types);
  }

  @Test
  public void testGetAbstractMethodName() {
    assertEquals("goog.abstractMethod",
        new ClosureCodingConvention().getAbstractMethodName());
  }

  @Test
  public void testSingletonGetterClassName() {
    Node node = newNode("goog.addSingletonGetter(X, getX)");
    assertEquals("X", new ClosureCodingConvention()
        .getSingletonGetterClassName(node));
  }

  @Test
  public void testIsOptionalParameter() {
    Node node = newNode("function foo(x = 1) {}");
    assertFalse(new ClosureCodingConvention().isOptionalParameter(node.getFirstChild()));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node node = newNode("function foo(x, ...y) {}");
    assertFalse(new ClosureCodingConvention().isVarArgsParameter(node.getFirstChild()));
  }

  @Test
  public void testIsPrivate() {
    CodingConvention convention = new ClosureCodingConvention();
    assertFalse(convention.isPrivate("x"));
  }

  @Test
  public void testPropertyTestFunctions() {
    Node node = newNode("goog.isDef(x) && goog.isNumber(y) && goog.isFunction(z)");

    for (String functionName : new ClosureCodingConvention().getAssertionFunctions()
        .stream()
        .map(AssertionFunctionSpec::getName)
        .collect(Collectors.toList())) {
      Node testFunctionCall = newNode(functionName + "(" + "x" + ")");
      assertTrue(new ClosureCodingConvention().isPropertyTestFunction(testFunctionCall));
      assertEquals(JSTypeNative.getType(functionName),
          testFunctionCall.getFirstChild().getType());
    }

    assertFalse(new ClosureCodingConvention().isPropertyTestFunction(node));
  }

  @Test
  public void testBind() {
    FunctionType fn = newFunctionType("fn", ImmutableList.of(),
        new ObjectType("FnType"));
    Node bindCall = newNode("goog.bind(fn, null, 'arg1')");

    Bind expected = new Bind(fn, null, ImmutableList.of(newNode("arg1")));
    Bind actual = new ClosureCodingConvention().describeFunctionBind(bindCall);
    assertEquals(expected, actual);
  }

  @Test
  public void testPartial() {
    FunctionType fn = newFunctionType("fn", ImmutableList.of("x"),
        new ObjectType("FnType"));
    Node partialCall = newNode("goog.partial(fn, 'arg1')");

    Bind expected = new Bind(fn, null, ImmutableList.of(newNode("arg1")));
    Bind actual = new ClosureCodingConvention().describeFunctionBind(partialCall);
    assertEquals(expected, actual);
  }
}