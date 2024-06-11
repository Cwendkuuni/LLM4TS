import static org.junit.Assert.*;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", new ObjectType("Parent"));
    FunctionType childCtor = new FunctionType("ChildCtor", new ObjectType("Child"));
    SubclassType relationshipType = SubclassType.INHERITS;

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, relationshipType);

    assertEquals("Parent", childCtor.getPrototype().getName());
    assertEquals(parentCtor, childCtor.getPrototype().getConstructor());
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = Node.newExpr("goog.inherits(SubClass, SuperClass)");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);

    assertNotNull(relationship);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("SubClass", relationship.getSubclassName());
    assertEquals("SuperClass", relationship.getSuperclassName());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newExpr("goog.provide('X')");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfProvide(node, node.getParent());

    assertEquals("X", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newExpr("goog.require('X')");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.extractClassNameIfRequire(node, node.getParent());

    assertEquals("X", className);
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node node = Node.newQualifiedName("a.b.c.prototype");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.endsWithPrototype(node));
  }

  @Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertEquals("goog.exportProperty", convention.getExportPropertyFunction());
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node node = Node.newExpr("goog.addSingletonGetter(X, getX)");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    String className = convention.getSingletonGetterClassName(node);

    assertEquals("X", className);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType("X", new ObjectType("X"));
    FunctionType getterType = new FunctionType("getX", new ObjectType("X"));
    ObjectType objectType = new ObjectType("X");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(functionType, getterType, objectType);

    assertEquals("getInstance", functionType.getProperties().get("getInstance").getName());
    assertEquals("instance_", functionType.getProperties().get("instance_").getName());
  }
}