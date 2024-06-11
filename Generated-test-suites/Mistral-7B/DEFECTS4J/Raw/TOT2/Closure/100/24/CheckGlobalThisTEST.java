import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;

public class CheckGlobalThisTest {
  @Test
  public void testCheckGlobalThis_ConstructorFunction() {
    // Test constructor function
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    Node constructorNode = AbstractCompilerTest.createNode("function MyConstructor() { }");
    assertFalse(checker.shouldTraverse(null, constructorNode, null));
  }

  @Test
  public void testCheckGlobalThis_PrototypeMethod() {
    // Test prototype method
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    Node prototypeMethodNode = AbstractCompilerTest.createNode("function MyObject() { this.myMethod = function() {} }");
    assertTrue(checker.shouldTraverse(null, prototypeMethodNode, null));
  }

  @Test
  public void testCheckGlobalThis_FunctionAnnotatedWithThisType() {
    // Test function annotated with @this
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    Node functionWithThisAnnotationNode = AbstractCompilerTest.createNode("/** @this {myType: 'MyType'} */ function MyFunction() { }");
    assertTrue(checker.shouldTraverse(null, functionWithThisAnnotationNode, null));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithNoLogicalPlaceForThisAnnotation() {
    // Test function with no logical place for this annotation
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    Node functionWithNoThisAnnotationNode = AbstractCompilerTest.createNode("function MyFunction() { this.myProperty = 5; }");
    assertTrue(checker.shouldTraverse(null, functionWithNoThisAnnotationNode, null));
  }

  @Test
  public void testCheckGlobalThis_AssignmentToThis() {
    // Test assignment to this
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    Node assignmentToThisNode = AbstractCompilerTest.createNode("function MyFunction() { this.myProperty = 5; }");
    assertFalse(checker.shouldReportThis(assignmentToThisNode, null));
  }

  @Test
  public void testCheckGlobalThis_AssignmentToVariable() {
    // Test assignment to variable
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    Node assignmentToVariableNode = AbstractCompilerTest.createNode("function MyFunction() { var a = this; }");
    assertFalse(checker.shouldReportThis(assignmentToVariableNode, null));
  }

  @Test
  public void testCheckGlobalThis_AssignmentToPrototypeProperty() {
    // Test assignment to prototype property
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    Node assignmentToPrototypePropertyNode = AbstractCompilerTest.createNode("function MyFunction() { Object.prototype.myProperty = 5; }");
    assertFalse(checker.shouldTraverse(null, assignmentToPrototypePropertyNode, null));
  }

  @Test
  public void testCheckGlobalThis_AssignmentToPrototypeSubproperty() {
    // Test assignment to prototype subproperty
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    Node assignmentToPrototypeSubpropertyNode = AbstractCompilerTest.createNode("function MyFunction() { Object.prototype.myObject.myProperty = 5; }");
    assertFalse(checker.shouldTraverse(null, assignmentToPrototypeSubpropertyNode, null));
  }
}

