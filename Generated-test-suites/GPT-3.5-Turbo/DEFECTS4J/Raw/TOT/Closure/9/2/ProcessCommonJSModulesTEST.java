import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class ProcessCommonJSModulesTest {

  private ProcessCommonJSModules processCommonJSModules;

  @Before
  public void setUp() {
    processCommonJSModules = new ProcessCommonJSModules(new AbstractCompiler(), ".js/");
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "module1.js";
    String expected = "module$module1";
    assertEquals(expected, processCommonJSModules.guessCJSModuleName(filename));
  }

  @Test
  public void testToModuleName() {
    String filename = "./module1.js";
    String expected = "module$module1";
    assertEquals(expected, ProcessCommonJSModules.toModuleName(filename));
  }

  @Test
  public void testToModuleNameWithRelativeAddressing() {
    String requiredFilename = "../module2.js";
    String currentFilename = "subfolder/module1.js";
    String expected = "module$subfolder$module2";
    assertEquals(expected, ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename));
  }

  @Test
  public void testProcessRequireCall() {
    Node require = new Node(Token.CALL, new Node(Token.NAME, "require"),
        new Node(Token.STRING, "module1"));
    Node parent = new Node(Token.SCRIPT);
    Node script = new Node(Token.SCRIPT);
    script.addChildToBack(require);
    parent.addChildToBack(script);

    processCommonJSModules.visitRequireCall(new NodeTraversal(), require, parent);

    assertEquals(1, script.getChildCount());
    Node moduleRef = script.getFirstChild();
    assertEquals(Token.NAME, moduleRef.getType());
    assertEquals("module$module1", moduleRef.getString());
    assertEquals("goog.require('module$module1');", script.getLastChild().toString());
  }

  @Test
  public void testProcessRequireCallWithReportDependenciesFalse() {
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(new AbstractCompiler(), ".js/", false);
    
    Node require = new Node(Token.CALL, new Node(Token.NAME, "require"),
        new Node(Token.STRING, "module1"));
    Node parent = new Node(Token.SCRIPT);
    Node script = new Node(Token.SCRIPT);
    script.addChildToBack(require);
    parent.addChildToBack(script);

    processCommonJSModules.visitRequireCall(new NodeTraversal(), require, parent);

    assertEquals(1, script.getChildCount());
    Node moduleRef = script.getFirstChild();
    assertEquals(Token.NAME, moduleRef.getType());
    assertEquals("module$module1", moduleRef.getString());
    assertEquals("goog.require('module$module1');", script.getLastChild().toString());
  }

  @Test
  public void testVisitScript() {
    Node script = new Node(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal();
    traversal.setInput(new CompilerInput(null));
    processCommonJSModules.visitScript(traversal, script);

    assertEquals(2, script.getChildCount());
    Node var = script.getFirstChild();
    assertEquals(Token.VAR, var.getType());
    assertEquals("module$.", var.getFirstChild().getString());
    assertEquals("objectlit", var.getFirstChild().getNext().getType().toString().toLowerCase());

    Node googProvide = script.getLastChild();
    assertEquals(Token.EXPR_RESULT, googProvide.getType());
    assertEquals("goog.provide('module$.');", googProvide.toString());
  }

  @Test
  public void testEmitOptionalModuleExportsOverride() {
    Node script = new Node(Token.SCRIPT);
    script.addChildToBack(new Node(Token.STRING, "module1.js"));
    script.addChildToBack(new Node(Token.STRING, "module.exports"));
    processCommonJSModules.emitOptionalModuleExportsOverride(script, "module$module1");

    assertEquals(2, script.getChildCount());
    Node ifNode = script.getLastChild();
    assertEquals(Token.IF, ifNode.getType());
    assertEquals("if (module$module1.module$exports) {\n  module$module1 = module$module1.module$exports;\n}", ifNode.toString());
  }

  @Test
  public void testVisitModuleExports() {
    Node moduleExports = new Node(Token.GETPROP);
    moduleExports.addChildToBack(new Node(Token.NAME, "module"));
    moduleExports.addChildToBack(new Node(Token.NAME, "exports"));
    processCommonJSModules.visitModuleExports(moduleExports);

    assertEquals("module$module1", moduleExports.getFirstChild().getString());
    assertEquals("module$exports", moduleExports.getLastChild().getString());
  }

  @Test
  public void testSuffixVarsCallback() {
    Node script = new Node(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal();
    traversal.setScope(new Scope());
    processCommonJSModules.new SuffixVarsCallback("suffix")
        .visit(traversal, new Node(Token.NAME, "var1"), script);
    processCommonJSModules.new SuffixVarsCallback("suffix")
        .visit(traversal, new Node(Token.NAME, "exports"), script);
    processCommonJSModules.new SuffixVarsCallback("suffix")
        .visit(traversal, new Node(Token.NAME, "var2"), script);

    assertEquals("var1$$suffix", script.getFirstChild().getString());
    assertNull(script.getFirstChild().getProp(Node.ORIGINALNAME_PROP));
    assertEquals("suffix", script.getFirstChild().getNext().getString());

    assertEquals("suffix", script.getChildAtIndex(1).getString());
    assertEquals("exports$$suffix", script.getChildAtIndex(1).getNext().getString());
    assertEquals("exports", script.getChildAtIndex(1).getNext().getProp(Node.ORIGINALNAME_PROP));

    assertEquals("var2", script.getChildAtIndex(2).getString());
    assertNull(script.getChildAtIndex(2).getNext());
  }

}