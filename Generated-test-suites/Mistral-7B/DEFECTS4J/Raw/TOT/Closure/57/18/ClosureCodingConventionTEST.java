import static com.google.javascript.jscomp.ClosureCodingConvention.*;
import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createFunctionType("Parent", new ObjectType("Parent"),
        new FunctionType("constructor"));
    FunctionType childCtor = createFunctionType("Child", new ObjectType("Child"),
        new FunctionType("constructor"));
    SubclassType relationshipType = SubclassType.INHERITS;

    ClosureCodingConvention.applySubclassRelationship(parentCtor, childCtor, relationshipType);

    FunctionType childPrototype = childCtor.getPrototype();
    ObjectType constructor = childPrototype.getPrototype();

    assertEquals(parentCtor, constructor.getPrototype());
    assertEquals(childCtor, constructor.getConstructor());
  }

  @Test
  public void testApplySubclassRelationship_Mixin() {
    FunctionType parentPrototype = createFunctionType("ParentPrototype",
        new ObjectType("ParentPrototype"), new FunctionType("constructor"));
    FunctionType childPrototype = createFunctionType("ChildPrototype",
        new ObjectType("ChildPrototype"), new FunctionType("constructor"));

    FunctionType parentCtor = createFunctionType("Parent", new ObjectType("Parent"),
        new FunctionType("constructor"));
    FunctionType childCtor = createFunctionType("Child", new ObjectType("Child"),
        new FunctionType("constructor"));

    SubclassType relationshipType = SubclassType.MIXIN;

    ClosureCodingConvention.applySubclassRelationship(parentCtor, childCtor, relationshipType);

    FunctionType child = createFunctionType("Child", new ObjectType("Child"),
        new FunctionType("constructor"));

    ClosureCodingConvention.applySubclassRelationship(childPrototype, child, relationshipType);

    ObjectType childPrototypeObject = childPrototype.getPrototype();
    assertEquals(parentPrototype, childPrototypeObject.getPrototype());
  }

  @Test(expected = AssertionError.class)
  public void testApplySubclassRelationship_InvalidCall() {
    FunctionType parentCtor = createFunctionType("Parent", new ObjectType("Parent"),
        new FunctionType("constructor"));
    FunctionType childCtor = createFunctionType("Child", new ObjectType("Child"),
        new FunctionType("constructor"));

    ClosureCodingConvention.applySubclassRelationship(childCtor, parentCtor, null);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newScopeNode();
    Node parent = Node.newNameNode("goog.provide");
    Node child = Node.newNameNode("X");
    parent.setNext(child);
    node.addChild(parent);

    assertEquals("X", ClosureCodingConvention.extractClassNameIfProvide(node, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newScopeNode();
    Node parent = Node.newNameNode("goog.require");
    Node child = Node.newNameNode("X");
    parent.setNext(child);
    node.addChild(parent);

    assertEquals("X", ClosureCodingConvention.extractClassNameIfRequire(node, null));
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty",
        ClosureCodingConvention.getExportPropertyFunction());
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node node = Node.newScopeNode();
    Node call = Node.newCall(Node.newName("goog.addSingletonGetter"),
        Node.newName("X"));
    Node getterName = Node.newName("getX");
    call.setNext(getterName);
    node.addChild(call);

    assertEquals("getX",
        ClosureCodingConvention.getSingletonGetterClassName(call, node));
  }

  @Test
  public void testIsSuperClassReference() {
    assertTrue(ClosureCodingConvention.isSuperClassReference("superClass_"));
    assertFalse(ClosureCodingConvention.isSuperClassReference("notSuperClass"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node prototype = Node.newName("prototype");
    Node prototypeNode = Node.newGetProp(Node.newName("a"), prototype);

    assertTrue(ClosureCodingConvention.endsWithPrototype(prototypeNode));

    Node nonPrototypeNode = Node.newName("b");
    assertFalse(ClosureCodingConvention.endsWithPrototype(nonPrototypeNode));
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newScopeNode();
    Node call = Node.newCall(Node.newName("goog.addDependency"),
        Node.newArrayLiteral(Node.newStringLiteral("X")));
    Node typeArray = Node.newCall(Node.newName("goog.addDependency"),
        Node.newArrayLiteral(Node.newName("X"), Node.newObjectLiteral()));
    node.addChild(call);
    node.addChild(typeArray);

    List<String> result = ClosureCodingConvention.identifyTypeDeclarationCall(call);

    assertEquals(ImmutableList.of("X"), result);
  }

  @Test
  public void testGetAbstractMethodName() {
    assertEquals("goog.abstractMethod",
        ClosureCodingConvention.getAbstractMethodName());
  }

  @Test
  public void testSingletonGetter() {
    FunctionType fn = createFunctionType("Singleton", new ObjectType("Singleton"),
        new FunctionType("getX"));
    ObjectType objectType = new ObjectType("Singleton");

    ClosureCodingConvention.applySingletonGetter(fn, fn.getPrototype(),
        objectType);

    assertEquals(fn, fn.getPrototype().getPrototype());
    assertEquals(objectType, fn.getPrototype().getInstance());
  }

  @Test
  public void testSingletonGetter_getX() {
    FunctionType fn = createFunctionType("Singleton", new ObjectType("Singleton"),
        new FunctionType("getX"));
    ObjectType objectType = new ObjectType("Singleton");

    ClosureCodingConvention.applySingletonGetter(fn, fn.getPrototype(),
        objectType);

    Node getX = Node.newName("getX");
    Node result = Node.newCall(getX, Node.newThis());

    assertEquals(fn, result.getScope().getFunction().getPrototype());
    assertEquals(objectType.getInstance(), result.getValue());
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node isDefNode = Node.newCall(Node.newName("goog.isDef"), Node.newThis());

    assertTrue(ClosureCodingConvention.isPropertyTestFunction(isDefNode));

    Node isNullNode = Node.newCall(Node.newName("goog.isNull"), Node.newThis());

    assertTrue(ClosureCodingConvention.isPropertyTestFunction(isNullNode));
  }

  @Test
  public void testExtractObjectLiteralFromObjectType() {
    ObjectType objectType = new ObjectType("Test");
    ObjectLiteralCast cast = ClosureCodingConvention.getObjectLiteralCast(null,
        Node.newCall(Node.newName("goog.reflect.object"), Node.newName("newTest"),
            Node.newObjectLiteral()));

    assertEquals(objectType.getName(), cast.getType());
    assertEquals(Node.newObjectLiteral(), cast.getValue());
  }

  @Test
  public void testBind() {
    FunctionType fn = createFunctionType("Test", new ObjectType("Test"),
        new FunctionType("test"));

    Node boundFn = Node.newCall(Node.newName("goog.bind"), fn,
        Node.newNull(), Node.newArrayLiteral());

    assertEquals(fn, ClosureCodingConvention.describeFunctionBind(boundFn)
        .getOriginalFunction());
    assertEquals(Node.newNull(), ClosureCodingConvention.describeFunctionBind(boundFn)
        .getThisValue());
    assertEquals(Node.newArrayLiteral(), ClosureCodingConvention.describeFunctionBind(boundFn)
        .getParameters());
  }

  @Test
  public void testPartial() {
    FunctionType fn = createFunctionType("Test", new ObjectType("Test"),
        new FunctionType("test"));

    Node boundFn = Node.newCall(Node.newName("goog.partial"), fn,
        Node.newArrayLiteral(Node.newStringLiteral("arg1"), Node.newNumber(1)));

    assertEquals(fn, ClosureCodingConvention.describeFunctionBind(boundFn)
        .getOriginalFunction());
    assertEquals(Node.newArrayLiteral(Node.newStringLiteral("arg1"),
        Node.newNumber(1)), ClosureCodingConvention.describeFunctionBind(boundFn)
        .getArguments());
  }

  private FunctionType createFunctionType(String name, ObjectType objectType,
      FunctionType constructor) {
    return new FunctionType(name, objectType, constructor);
  }
}