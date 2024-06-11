import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Externs;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.Scope;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private CompilerOptions options;
  private InlineObjectLiterals pass;

  @Before
  public void setup() {
    compiler = new Compiler();
    options = new CompilerOptions();
    pass = new InlineObjectLiterals(compiler, () -> "");
  }

  @Test
  public void testSimpleInline() {
    String source = "function foo() { var x = {a: 1, b: 2}; }";
    Node root = NodePrinter.parseCompilationUnit(source);
    Externs externs = new Externs();

    pass.process(externs, root);
    compiler.runPasses(root, options, new Compiler.SemanticCheckPass(), pass);

    Node foo = NodeUtil.findFunctionByName(root, "foo");
    Scope fooScope = foo.getBody().getFirstChild().asStatement().asStatementList().get(0).asBlock().getScope();
    Var x = fooScope.getVar("x");
    Reference xRef = fooScope.getFirstReferenceTo(x);

    // Check that the object has been expanded
    Node xAssign = xRef.getParent();
    assertEquals(2, xAssign.getNumChildren());
    Node xInit = xAssign.getChildAtIndex(1);
    assertTrue(xInit.isVar());

    // Check that all properties have been assigned
    for (Node child = xInit.getFirstChild(); child != null; child = child.getNext()) {
      Node name = child;
      Node value = child.getNext();
      String propName = name.getString();
      assertEquals(propName, xRef.getParent().getLastChild().getLastChild().getFirstChild().getString());
    }
  }

  @Test
  public void testEmptyInline() {
    String source = "function foo() { var x = {}; }";
    Node root = NodePrinter.parseCompilationUnit(source);
    Externs externs = new Externs();

    pass.process(externs, root);
    compiler.runPasses(root, options, new Compiler.SemanticCheckPass(), pass);

    Node foo = NodeUtil.findFunctionByName(root, "foo");
    Scope fooScope = foo.getBody().getFirstChild().asStatement().asStatementList().get(0).asBlock().getScope();
    Var x = fooScope.getVar("x");
    Reference xRef = fooScope.getFirstReferenceTo(x);

    // Check that the object has been expanded
    Node xAssign = xRef.getParent();
    assertEquals(2, xAssign.getNumChildren());
    Node xInit = xAssign.getChildAtIndex(1);
    assertTrue(xInit.isVar());
  }

  @Test
  public void testSelfReference() {
    String source = "function foo() { var x = {a: function() {return x;}}; }";
    Node root = NodePrinter.parseCompilationUnit(source);
    Externs externs = new Externs();

    assertFalse("Tests should fail without self-reference handling", pass.isSelfReferenceHandled());

    pass.process(externs, root);
    compiler.runPasses(root, options, new Compiler.SemanticCheckPass(), pass);

    // Test fails without handling self-reference
    Node foo = NodeUtil.findFunctionByName(root, "foo");
    Scope fooScope = foo.getBody().getFirstChild().asStatement().asStatementList().get(0).asBlock().getScope();
    Var x = fooScope.getVar("x");
    Reference xRef = fooScope.getFirstReferenceTo(x);

    // Check that the object has NOT been expanded
    Node xAssign = xRef.getParent();
    assertEquals(2, xAssign.getNumChildren());
    Node xInit = xAssign.getChildAtIndex(1);
    assertNotNull(xInit.isObjectLit());
  }

  @Test
  public void testNoInline() {
    String source = "function foo() { var x = {a: 1, b: 2}; x.c = 3; }";
    Node root = NodePrinter.parseCompilationUnit(source);
    Externs externs = new Externs();

    pass.process(externs, root);
    compiler.runPasses(root, options, new Compiler.SemanticCheckPass(), pass);

    Node foo = NodeUtil.findFunctionByName(root, "foo");
    Scope fooScope = foo.getBody().getFirstChild().asStatement().asStatementList().get(0).asBlock().getScope();
    Var x = fooScope.getVar("x");
    Reference xRef = fooScope.getFirstReferenceTo(x);

    // Check that the object has NOT been expanded
    Node xAssign = xRef.getParent();
    assertEquals(2, xAssign.getNumChildren());
    Node xInit = xAssign.getChildAtIndex(1);
    assertNotNull(xInit.isObjectLit());

    // Check that the property has NOT been expanded
    Node cRef = NodeUtil.findReference(root, "x.c");
    assertNotNull(cRef);
    Node cAssign = cRef.getParent();
    assertFalse(cAssign.isAssignExpr());
  }

  @Test
  public void testInlineWithInitialization() {
    String source = "function foo() { var x = {a: 1, b: 2}; x = {c: 3}; }";
    Node root = NodePrinter.parseCompilationUnit(source);
    Externs externs = new Externs();

    pass.process(externs, root);
    compiler.runPasses(root, options, new Compiler.SemanticCheckPass(), pass);

    Node foo = NodeUtil.findFunctionByName(root, "foo");
    Scope fooScope = foo.getBody().getFirstChild().asStatement().asStatementList().get(0).asBlock().getScope();
    Var x = fooScope.getVar("x");
    Reference xRef = fooScope.getFirstReferenceTo(x);

    // Check that the object has been expanded
    Node xAssign = xRef.getParent();
    assertEquals(3, xAssign.getNumChildren());
    Node xInit = xAssign.getChildAtIndex(1);
    assertTrue(xInit.isVar());

    // Check that the property has also been expanded
    List<Node> initialValues = Lists.newArrayList();
    Map<String, Node> initvals = Maps.newHashMap();
    Reference initRef = xRef.getInitializingReference();
    fillInitialValues(initRef, initvals);
    Node init = initRef.getParent();
    Node initAssign = init.getParent();
    Node initValuesAssign = initAssign.getLastChild();
    List<Node> children = initValuesAssign.getChildren();
    for (int i = 0; i < children.size(); i++) {
      Node child = children.get(i);
      if (child.isAssign()) {
        Node childInit = child.getFirstChild();
        String propName = child.getLastChild().getString();
        Node propValue = childInit.getNext();
        String varName = VAR_PREFIX + propName + "_";
        assertEquals(varName, initvals.get(propName).getString());
      }
      initialValues.add(child);
    }

    // Check that all assignments have been replaced
    for (Node child : initialValues) {
      Node assign = child;
      Node left = assign.getFirstChild();
      Node right = assign.getLastChild();
      String varName = left.getString();
      String propName = right.getString();
      Var var = fooScope.getVar(varName);
      Reference ref = fooScope.getFirstReferenceTo(var);
      Node getProp = ref.getParent();
      Node prop = getProp.getLastChild();
      assertEquals(propName, prop.getString());

      // Check that the variable has been assigned the correct value
      Node getVar = NodeUtil.findVariableByName(fooScope, varName);
      assertEquals(varName, getVar.getFirstChild().getString());
      assertEquals(propValue, getVar.getLastChild());
    }
  }

  @Test
  public void testInlineWithLoop() {
    String source = "function foo() { var x = {a: 0}; for (var i = 0; i < 5; i++) { x.a++; } }";
    Node root = NodePrinter.parseCompilationUnit(source);
    Externs externs = new Externs();

    pass.process(externs, root);
    compiler.runPasses(root, options, new Compiler.SemanticCheckPass(), pass);

    Node foo = NodeUtil.findFunctionByName(root, "foo");
    Scope fooScope = foo.getBody().getFirstChild().asStatement().asStatementList().get(0).asBlock().getScope();
    Var x = fooScope.getVar("x");
    Var i = fooScope.getVar("i");
    Reference xRef = fooScope.getFirstReferenceTo(x);
    Reference iRef = fooScope.getFirstReferenceTo(i);

    // Check that the object has been expanded
    Node xAssign = xRef.getParent();
    assertEquals(2, xAssign.getNumChildren());
    Node xInit = xAssign.getChildAtIndex(1);
    assertTrue(xInit.isVar());

    // Check that the loop counter variable has NOT been expanded
    Node iAssign = iRef.getParent();
    assertEquals(2, iAssign.getNumChildren());
    Node iInit = iAssign.getChildAtIndex(1);
    assertFalse(iInit.isVar());

    // Check that the property has been updated in each iteration
    List<Node> updates = Lists.newArrayList();
    for (int i = 0; i < 5; i++) {
      Node update = NodeUtil.findUpdateByStatementIndex(foo, i);
      updates.add(update);
    }

    for (int i = 0; i < updates.size(); i++) {
      Node update = updates.get(i);
      Node left = update.getFirstChild();
      Node right = update.getLastChild();
      String varName = left.getString();
      String propName = right.getString();
      Var var = fooScope.getVar(varName);
      Reference ref = fooScope.getFirstReferenceTo(var);
      Node getProp = ref.getParent();
      Node prop = getProp.getLastChild();
      assertEquals(propName, prop.getString());

      // Check that the variable has been assigned the correct value
      Node getVar = NodeUtil.findVariableByName(fooScope, varName);
      int expectedValue = i + 1;
      assertEquals(String.valueOf(expectedValue), getVar.getFirstChild().getString());
    }
  }

  @Test
  public void testInlineWithFunction() {
    String source = "function foo() { function bar() {} var x = {a: bar}; }";
    Node root = NodePrinter.parseCompilationUnit(source);
    Externs externs = new Externs();

    pass.process(externs, root);
    compiler.runPasses(root, options, new Compiler.SemanticCheckPass(), pass);

    Node foo = NodeUtil.findFunctionByName(root, "foo");
    Scope fooScope = foo.getBody().getFirstChild().asStatement().asStatementList().get(0).asBlock().getScope();
    Var x = fooScope.getVar("x");
    Reference xRef = fooScope.getFirstReferenceTo(x);

    // Check that the object has been expanded
    Node xAssign = xRef.getParent();
    assertEquals(2, xAssign.getNumChildren());
    Node xInit = xAssign.getChildAtIndex(1);
    assertTrue(xInit.isVar());

    // Check that the function has NOT been expanded
    Node bar = NodeUtil.findFunctionByName(fooScope, "bar");
    assertFalse(bar.isAssignment());
  }

  private void fillInitialValues(Reference init, Map<String, Node> initvals) {
    Node object = init.getAssignedValue();
    Preconditions.checkState(object.isObjectLit());
    for (Node key = object.getFirstChild(); key != null; key = key.getNext()) {
      String varname = key.getString();
      Node value = key.removeFirstChild();
      initvals.put(varname, value);
    }
  }
}