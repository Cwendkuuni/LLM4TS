import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;

import java.io.File;
import java.net.URI;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws Exception {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPassManager passManager = mock(CompilerPassManager.class);
    CompilerInput input = new CompilerInput(new File("testInputFile.js"));

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    Node rootNode = IR.program();
    when(compiler.getInput()).thenReturn(input);
    when(compiler.getPassManager()).thenReturn(passManager);
    when(passManager.getNextPass()).thenReturn(processor);

    processor.process(input.getExterns(), rootNode);

    assertNotNull(processor.getModule());
    assertEquals("testModule$", processor.getModule().getName());
  }

  @org.junit.Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(null, "");

    assertEquals("testModule$", processor.guessCJSModuleName("testModule.js"));
    assertEquals("./testModule$", processor.guessCJSModuleName("./testModule.js"));
    assertEquals("../testModule$", processor.guessCJSModuleName("../testModule.js"));
  }

  @org.junit.Test
  public void testToModuleName() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(null, "");

    assertEquals("testModule$", processor.toModuleName("testModule.js"));
    assertEquals("test$testModule$", processor.toModuleName("test/testModule.js"));
    assertEquals("testModule$", processor.toModuleName("testModule.js"));
    assertEquals("testModule$", processor.toModuleName("testModule"));
  }

  @org.junit.Test
  public void testNormalizeSourceName() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(null, "");

    assertEquals("testModule", processor.normalizeSourceName("./test/testModule.js"));
    assertEquals("testModule", processor.normalizeSourceName("test/testModule.js"));
    assertEquals("testModule", processor.normalizeSourceName("testModule.js"));
    assertEquals("testModule", processor.normalizeSourceName("testModule"));
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallback_visitRequireCall() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput(new File("testInputFile.js"));
    CompilerPassManager passManager = mock(CompilerPassManager.class);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    Node rootNode = IR.program();
    Node requireCall = IR.call(IR.name("require"), IR.string("testModule"));
    Node scriptNode = IR.script();

    when(compiler.getInput()).thenReturn(input);
    when(compiler.getPassManager()).thenReturn(passManager);
    when(passManager.getNextPass()).thenReturn(processor);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    traversal.traverse();

    assertNotNull(processor.getModule());
    assertEquals("testModule$", processor.getModule().getName());

    Node requireCallInScript = scriptNode.getFirstChild();
    assertEquals("testModule$", requireCallInScript.getString());
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallback_visitScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput(new File("testInputFile.js"));
    CompilerPassManager passManager = mock(CompilerPassManager.class);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    Node rootNode = IR.program();
    Node scriptNode = IR.script();

    when(compiler.getInput()).thenReturn(input);
    when(compiler.getPassManager()).thenReturn(passManager);
    when(passManager.getNextPass()).thenReturn(processor);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    traversal.traverse();

    String moduleName = processor.getModule().getName();
    assertEquals("testModule$", moduleName);

    // Check if goog.provide and goog.require were added
    Node googProvideCall = null;
    Node googRequireCall = null;
    for (Node child : scriptNode.getChildren()) {
      if (child.isCall() && child.getChildCount() > 1) {
        String funcName = child.getFirstChild().getString();
        if ("goog.provide".equals(funcName)) {
          googProvideCall = child;
        } else if ("goog.require".equals(funcName)) {
          googRequireCall = child;
        }
      }
    }

    assertNotNull(googProvideCall);
    assertEquals("testModule$", googProvideCall.getChildAtIndex(1).getString());

    assertNotNull(googRequireCall);
    assertEquals("testModule$", googRequireCall.getChildAtIndex(1).getString());

    // Check if module name was added as a property of the module object
    Node moduleObject = null;
    for (Node child : scriptNode.getChildren()) {
      if (child.isObjectLiteral()) {
        for (Node prop : child.getChildren()) {
          if (prop.isName() && prop.getString().equals("module$exports")) {
            moduleObject = child;
            break;
          }
        }
      }
    }

    assertNotNull(moduleObject);
    Node moduleNameProp = moduleObject.getFirstChild();
    assertEquals("testModule$", moduleNameProp.getString());
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallback_visitModuleExports() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput(new File("testInputFile.js"));
    CompilerPassManager passManager = mock(CompilerPassManager.class);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    Node rootNode = IR.program();
    Node moduleExportsAssign = IR.assign(IR.getprop(IR.name("module$exports"), IR.string("testModule")), IR.string("module$testModule"));

    when(compiler.getInput()).thenReturn(input);
    when(compiler.getPassManager()).thenReturn(passManager);
    when(passManager.getNextPass()).thenReturn(processor);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    traversal.traverse();

    Node moduleExports = rootNode.getFirstChild();
    Node moduleExportsProp = moduleExports.getFirstChild();
    Node moduleNameProp = moduleExportsProp.getFirstChild();

    assertEquals("module$testModule", moduleNameProp.getString());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesCallback_suffixVarsCallback() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput(new File("testInputFile.js"));
    CompilerPassManager passManager = mock(CompilerPassManager.class);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    Node rootNode = IR.program();
    Node globalVar = IR.name("test");

    when(compiler.getInput()).thenReturn(input);
    when(compiler.getPassManager()).thenReturn(passManager);
    when(passManager.getNextPass()).thenReturn(processor);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    traversal.traverse();

    Node testVar = rootNode.getFirstChild();
    assertEquals("test$$test", testVar.getString());
  }
}