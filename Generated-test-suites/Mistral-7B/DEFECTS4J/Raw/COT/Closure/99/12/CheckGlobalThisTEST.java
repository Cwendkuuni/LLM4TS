import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private CheckLevel level;
  private NodeFactory nodeFactory;
  private NodeTraversal traversal;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    level = CheckLevel.WARNING;
    compiler = mock(AbstractCompiler.class);
    checker = new CheckerFactory().getChecker("Es5", new CompilerOptions(), compiler);
    nodeFactory = checker.getOptions().getFactory();
    sourceFile = new SourceFile("CheckGlobalThisTest.js");
  }

  @Test
  public void testShouldTraverseFunctionsInGlobalContext() {
    Node functionNode = parseFunction("function evil() {}", sourceFile);
    Node parentNode = nodeFactory.createScript(sourceFile);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionsAnnotatedAsConstructors() {
    Node functionNode = parseFunction("function Foo() {}", sourceFile);
    Node parentNode = nodeFactory.createScript(sourceFile);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), functionNode, parentNode));

    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    functionNode.setJSDocInfo(jsDoc);

    CheckGlobalThis checker2 = new CheckGlobalThis(compiler, level);
    assertTrue(checker2.shouldTraverse(NodeTraversal.emptyTraversal(), functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionsAnnotatedWithThis() {
    Node functionNode = parseFunction("function Foo@this() {}", sourceFile);
    Node parentNode = nodeFactory.createScript(sourceFile);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    assertTrue(checker.shouldTraverse(NodeTraversal.emptyTraversal(), functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionsInAllowedContexts() {
    Node functionNode = parseFunction("function evil() {}", sourceFile);
    Node prototypeNode = nodeFactory.createName("prototype", null);
    Node parentNode = nodeFactory.createBlock(null, prototypeNode);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), functionNode, parentNode));

    parentNode = nodeFactory.createName("A", null);
    prototypeNode = nodeFactory.createName("prototype", prototypeNode);
    parentNode.setParent(nodeFactory.createBlock(null, prototypeNode));
    parentNode = parentNode;

    CheckGlobalThis checker2 = new CheckGlobalThis(compiler, level);
    assertTrue(checker2.shouldTraverse(NodeTraversal.emptyTraversal(), functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = parseAssignment("a = this", sourceFile);
    Node parentNode = nodeFactory.createBlock(null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    assertTrue(checker.shouldTraverse(NodeTraversal.emptyTraversal(), assignNode.getLastChild(), parentNode));
  }

  @Test
  public void testShouldNotTraverseLeftSideOfAssignmentToPrototypeProperty() {
    Node assignNode = parseAssignment("a.prototype.prop = this", sourceFile);
    Node parentNode = nodeFactory.createBlock(null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), assignNode.getLastChild(), parentNode));
  }

  @Test
  public void testShouldNotTraverseLeftSideOfAssignmentToPrototypeSubproperty() {
    Node assignNode = parseAssignment("a.prototype.b.prop = this", sourceFile);
    Node parentNode = nodeFactory.createBlock(null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    assertFalse(checker.shouldTraverse(NodeTraversal.emptyTraversal(), assignNode.getLastChild(), parentNode));
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = parseAssignment("a = this", sourceFile);
    Node parentNode = nodeFactory.createBlock(null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    checker.visit(NodeTraversal.emptyTraversal(), assignNode.getFirstChild(), parentNode);
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    Node accessNode = parsePropertyAccess("a.prototype.prop", sourceFile);
    Node parentNode = nodeFactory.createBlock(null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    checker.visit(NodeTraversal.emptyTraversal(), accessNode.getFirstChild(), parentNode);
  }

  private Node parseFunction(String functionCode, SourceFile sourceFile) {
    Parser parser = checker.getFactory().createParser(sourceFile);
    Node rootNode = parser.parse(functionCode);
    Node functionNode = rootNode.getFirstChild();
    return functionNode;
  }

  private Node parseAssignment(String assignmentCode, SourceFile sourceFile) {
    Parser parser = checker.getFactory().createParser(sourceFile);
    Node rootNode = parser.parse(assignmentCode);
    Node assignNode = rootNode.getFirstChild();
    return assignNode;
  }

  private Node parsePropertyAccess(String propertyAccessCode, SourceFile sourceFile) {
    Parser parser = checker.getFactory().createParser(sourceFile);
    Node rootNode = parser.parse(propertyAccessCode);
    Node accessNode = rootNode.getFirstChild();
    return accessNode;
  }

  private void assertTrue(boolean condition) {
    assert(condition);
  }

  private void assertFalse(boolean condition) {
    assert(!condition);
  }

  private void assertNull(Object obj) {
    assert(obj == null);
  }

  private void assertNotNull(Object obj) {
    assert(obj != null);
  }
}