import static org.junit.Assert.*;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    // Create a new ProcessCommonJSModules instance
    ProcessCommonJSModules pm = new ProcessCommonJSModules(new AbstractCompiler(), ".");

    // Test the guessCJSModuleName method
    assertEquals("module$test", pm.guessCJSModuleName("test.js"));
    assertEquals("module$foo$bar$test", pm.guessCJSModuleName("foo/bar/test.js"));

    // Test the toModuleName method
    assertEquals("module$test", ProcessCommonJSModules.toModuleName("test.js"));
    assertEquals("module$foo$bar$test", ProcessCommonJSModules.toModuleName("foo/bar/test.js"));

    // Test the toModuleName method with relative addressing
    assertEquals("module$test", ProcessCommonJSModules.toModuleName("test.js", "src/main.js"));
    assertEquals("module$foo$bar$test", ProcessCommonJSModules.toModuleName("./foo/bar/test.js", "src/main.js"));

    // Test the visitRequireCall method
    NodeTraversal t = new NodeTraversal(new Compiler(), new Node());
    Node require = new Node(
      new Node(Token.NAME, "require"),
      new Node(Token.STRING, "test_module")
    );
    Node parent = new Node();
    pm.visitRequireCall(t, require, parent);
    assertEquals("module$test_module", parent.getFirstChild().getString());

    // Test the visitScript method
    Node script = new Node(Token.SCRIPT);
    pm.visitScript(t, script);
    assertEquals("module$test", script.getFirstChild().getString());

    // Test the emitOptionalModuleExportsOverride method
    pm.emitOptionalModuleExportsOverride(script, "module$test");
    assertEquals(
      "if (module$test.module$exports) { module$test = module$test.module$exports; }",
      script.getLastChild().toString()
    );

    // Test the visitModuleExports method
    Node prop = new Node(
      new Node(Token.GETPROP, new Node(Token.NAME, "module"), new Node(Token.STRING, "exports"))
    );
    pm.visitModuleExports(prop);
    assertEquals("module$test", prop.getFirstChild().getString());
    assertEquals("module$exports", prop.getLastChild().getString());

    // Test the SuffixVarsCallback method
    NodeTraversal t = new NodeTraversal(new Compiler(), new Node());
    Node name = new Node(Token.NAME, "testName");
    Node parent = new Node();
    Scope.Var var = new Scope.Var("testName", null, null, true);
    t.getScope().declare(var);
    t.getScopeRoot().getAllSymbols().put("testName", var);
    pm.new SuffixVarsCallback("test").visit(t, name, parent);
    assertEquals("testName$$test", name.getString());

  }

}