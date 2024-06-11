import static com.google.javascript.jscomp.ClosureCodingConvention.OBJECTLIT_EXPECTED;
import static com.google.javascript.jscomp.DefaultCodingConvention.SubclassType.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor");
    FunctionType childCtor = new FunctionType("ChildCtor");
    SubclassType relationship = ClosureCodingConvention.INSTANCE.applySubclassRelationship(
        parentCtor, childCtor, INHERITS);
    assertNotNull(relationship);
    assertTrue(relationship.equals(INHERITS));
  }

  @Test
  public void testApplySubclassRelationship_InvalidCall() {
    FunctionType parentCtor = new FunctionType("ParentCtor");
    FunctionType childCtor = new FunctionType("ChildCtor");
    SubclassRelationship relationship = ClosureCodingConvention.INSTANCE.applySubclassRelationship(
        parentCtor, childCtor, UNKNOWN);
    assertNull(relationship);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = new Node(Token.CALL, null, "goog.provide", null,
        new Node(Token.STRING, "MyClass"));
    String className = ClosureCodingConvention.INSTANCE.extractClassNameIfProvide(node, null);
    assertNotNull(className);
    assertTrue(className.equals("MyClass"));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = new Node(Token.CALL, null, "goog.require", null,
        new Node(Token.STRING, "MyClass"));
    String className = ClosureCodingConvention.INSTANCE.extractClassNameIfRequire(node, null);
    assertNotNull(className);
    assertTrue(className.equals("MyClass"));
  }

  @Test
  public void testExtractClassNameIfGoog_InvalidCall() {
    Node node = new Node(Token.CALL, null, "goog.invalidCall", null, null);
    String className = ClosureCodingConvention.INSTANCE.extractClassNameIfGoog(node, null, "goog.require");
    assertNull(className);
  }

  @Test
  public void testGetAbstractMethodName() {
    String abstractMethodName = ClosureCodingConvention.INSTANCE.getAbstractMethodName();
    assertNotNull(abstractMethodName);
    assertTrue(abstractMethodName.equals("goog.abstractMethod"));
  }

  @Test
  public void testIsSuperClassReference() {
    assertFalse(ClosureCodingConvention.INSTANCE.isSuperClassReference(""));
    assertFalse(ClosureCodingConvention.INSTANCE.isSuperClassReference("notSuperClass"));
    assertTrue(ClosureCodingConvention.INSTANCE.isSuperClassReference("superClass_"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node node = new Node(Token.GETPROP, null, "prototype", null,
        new Node(Token.IDENTIFIER, "a.b.c"));
    assertFalse(ClosureCodingConvention.INSTANCE.endsWithPrototype(node));

    Node nodePrototype = new Node(Token.GETPROP, null, "prototype", null,
        new Node(Token.IDENTIFIER, "a.b.c.prototype"));
    assertTrue(ClosureCodingConvention.INSTANCE.endsWithPrototype(nodePrototype));
  }

  @Test
  public void testExtractClassNameIfProvide_InvalidCall() {
    Node node = new Node(Token.CALL, null, "goog.invalidCall", null, null);
    String className = ClosureCodingConvention.INSTANCE.extractClassNameIfProvide(node, null);
    assertNull(className);
  }

  @Test
  public void testExtractClassNameIfRequire_InvalidCall() {
    Node node = new Node(Token.CALL, null, "goog.invalidCall", null, null);
    String className = ClosureCodingConvention.INSTANCE.extractClassNameIfRequire(node, null);
    assertNull(className);
  }
}