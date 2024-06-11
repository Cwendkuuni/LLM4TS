import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.io.File;
import java.net.URISyntaxException;

public class ProcessCommonJSModulesTest {

  CompilerPass processor;
  CompilerInput input;

  @org.junit.Before
  public void setUp() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    String filenamePrefix = "test";
    processor = new ProcessCommonJSModules(compiler, filenamePrefix);
    input = new CompilerInput(new File("test.js"));
  }

  @org.junit.Test
  public void testGuessCJSModuleName() throws URISyntaxException {
    String filename = "test/subfolder/module.js";
    String expected = "module$subfolder$module";
    String actual = processor.guessCJSModuleName(filename);
    assertSame(expected, actual);
  }

  @org.junit.Test
  public void testGuessCJSModuleNameRelative() {
    String filename = "./module.js";
    String expected = "module";
    String actual = processor.guessCJSModuleName(filename);
    assertSame(expected, actual);
  }

  @org.junit.Test
  public void testGuessCJSModuleNameNoExtension() {
    String filename = "module";
    String expected = "module";
    String actual = processor.guessCJSModuleName(filename);
    assertSame(expected, actual);
  }

  @org.junit.Test
  public void testProcess() {
    Node root = mock(Node.class);
    processor.process(input, root);
    assertNull(input.getProvides());
    assertNull(input.getRequires());
  }

  @org.junit.Test
  public void testProcessWithProvide() {
    CompilerInput input = mock(CompilerInput.class);
    when(input.getProvides()).thenReturn(Sets.newHashSet("module$name"));

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput()).thenReturn(input);

    String filenamePrefix = "test";
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filenamePrefix);

    Node root = mock(Node.class);

    processor.process(input, root);

    JSModule module = processor.getModule();
    assertSame("module$name", module.getName());
  }

  @org.junit.Test
  public void testProcessWithRequire() {
    CompilerInput input = mock(CompilerInput.class);
    when(input.getRequires()).thenReturn(Sets.newHashSet("module$name"));

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput()).thenReturn(input);

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    Node root = mock(Node.class);

    processor.process(input, root);

    Node requireCall = root.getFirstChild();
    assertSame("require", requireCall.getQualifiedName());
    assertSame("module$name", requireCall.getChildAtIndex(1).getString());
  }

  @org.junit.Test
  public void testProcessWithModuleExports() {
    Node root = IR.script("function test() {}", null).srcref("test.js");
    Node moduleExports = new Node(Node.EXPR_STMT);
    moduleExports.setString("module.exports");
    root.addChildToBack(moduleExports);

    CompilerInput input = new CompilerInput(new File("test.js"));
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput()).thenReturn(input);

    String filenamePrefix = "test";
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filenamePrefix);

    Node newModuleExports = new Node(Node.ASSIGN);
    newModuleExports.setLeft(IR.name("module$test"));
    newModuleExports.setRight(moduleExports.cloneTree());
    root.addChildToBack(newModuleExports);

    processor.process(input, root);

    Node actualModuleExports = root.getChildAtIndex(root.getChildCount() - 2);
    assertSame("module$test", actualModuleExports.getLeft().getString());
  }

  @org.junit.Test
  public void testProcessWithModuleExportsAndRequire() {
    Node root = IR.script("function test() { require('module$name'); }", null)
        .srcref("test.js");

    CompilerInput input = new CompilerInput(new File("test.js"));
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput()).thenReturn(input);

    String filenamePrefix = "test";
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filenamePrefix);

    Node newModuleExports = new Node(Node.ASSIGN);
    newModuleExports.setLeft(IR.name("module$test"));
    newModuleExports.setRight(IR.name("module$name"));
    root.addChildToBack(newModuleExports);
    root.getChildAtIndex(0).removeChildAtIndex(0);

    processor.process(input, root);

    Node actualModuleExports = root.getChildAtIndex(root.getChildCount() - 2);
    assertSame("module$test", actualModuleExports.getLeft().getString());
    Node actualRequire = root.getChildAtIndex(0);
    assertSame("module$name", actualRequire.getChildAtIndex(1).getString());
  }

  @org.junit.Test
  public void testProcessWithScript() {
    Node root = IR.script("goog.provide('module$name');", null).srcref("test.js");
    Node script = IR.script("function test() {}", null).srcref("test.js");
    Node moduleExports = new Node(Node.ASSIGN);
    moduleExports.setLeft(IR.name("module$name.module$exports"));
    moduleExports.setRight(IR.objectlit());
    root.addChildToBack(moduleExports);
    root.addChildToBack(script);

    CompilerInput input = new CompilerInput(new File("test.js"));
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput()).thenReturn(input);

    String filenamePrefix = "test";
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filenamePrefix);

    processor.process(input, root);

    Node actualModuleExports = root.getChildAtIndex(root.getChildCount() - 3);
    assertSame("module$name.module$exports", actualModuleExports.getLeft().getString());
    Node actualScript = root.getChildAtIndex(root.getChildCount() - 2);
    Node actualTest = actualScript.getFirstChild();
    assertSame("function test() {}", actualTest.toString());
  }

  @org.junit.Test
  public void testProcessWithScriptAndRequire() {
    Node root = IR.script("goog.provide('module$name'); require('module$subfolder$module');", null)
        .srcref("test.js");
    Node script = IR.script("function test() {}", null).srcref("test.js");
    Node moduleExports = new Node(Node.ASSIGN);
    moduleExports.setLeft(IR.name("module$name.module$exports"));
    moduleExports.setRight(IR.objectlit());
    root.addChildToBack(moduleExports);
    root.addChildToBack(script);

    CompilerInput input = new CompilerInput(new File("test.js"));
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput()).thenReturn(input);

    String filenamePrefix = "test";
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filenamePrefix);

    Node newModuleExports = new Node(Node.ASSIGN);
    newModuleExports.setLeft(IR.name("module$name"));
    newModuleExports.setRight(IR.name("module$subfolder$module"));
    root.addChildToBack(newModuleExports);
    root.getChildAtIndex(1).removeChildAtIndex(0);

    processor.process(input, root);

    Node actualModuleExports = root.getChildAtIndex(root.getChildCount() - 3);
    assertSame("module$name", actualModuleExports.getLeft().getString());
    Node actualScript = root.getChildAtIndex(root.getChildCount() - 2);
    Node actualTest = actualScript.getFirstChild();
    assertSame("function test() {}", actualTest.toString());
    Node actualRequire = root.getChildAtIndex(root.getChildCount() - 4);
    assertSame("module$subfolder$module", actualRequire.getChildAtIndex(1).getString());
  }

  @org.junit.Test
  public void testProcessWithScriptAndRequireAndModuleExports() {
    Node root = IR.script("goog.provide('module$name'); require('module$subfolder$module'); module.exports = {'test': function() {}};", null)
        .srcref("test.js");
    Node script = IR.script("function test() {}", null).srcref("test.js");
    Node moduleExports = new Node(Node.ASSIGN);
    moduleExports.setLeft(IR.name("module$name.module$exports"));
    moduleExports.setRight(IR.objectlit());
    root.addChildToBack(moduleExports);
    root.addChildToBack(script);

    CompilerInput input = new CompilerInput(new File("test.js"));
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getInput()).thenReturn(input);

    String filenamePrefix = "test";
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filenamePrefix);

    Node newModuleExports = new Node(Node.ASSIGN);
    newModuleExports.setLeft(IR.name("module$name"));
    newModuleExports.setRight(IR.name("module$subfolder$module"));
    root.addChildToBack(newModuleExports);
    Node newModuleExportsValue = new Node(Node.OBJECT_LITERAL);
    Node newTest = new Node(Node.FUNCTION);
    newTest.setName("test");
    newTest.setBody(IR.block(new Node(Node.RETURN, IR.name("undefined"))));
    newModuleExportsValue.addChildToBack(newNode("test", newTest));
    newModuleExports.setRight(newModuleExportsValue);
    root.getChildAtIndex(1).removeChildAtIndex(0);
    root.getChildAtIndex(1).getChildAtIndex(1).setString("module$subfolder$module");

    processor.process(input, root);

    Node actualModuleExports = root.getChildAtIndex(root.getChildCount() - 3);
    assertSame("module$name", actualModuleExports.getLeft().getString());
    Node actualScript = root.getChildAtIndex(root.getChildCount() - 2);
    Node actualTest = actualScript.getFirstChild();
    assertSame("function test() {}", actualTest.toString());
    Node actualRequire = root.getChildAtIndex(root.getChildCount() - 4);
    assertSame("module$subfolder$module", actualRequire.getChildAtIndex(1).getString());
    Node actualNewModuleExports = root.getChildAtIndex(root.getChildCount() - 5);
    Node actualNewTest = actualNewModuleExports.getChildAtIndex(1);
    assertSame("test", actualNewTest.getName().getString());
  }
}