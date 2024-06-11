import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.processCommonJSModules.ProcessCommonJSModules;
import com.google.javascript.jscomp.processCommonJSModules.ProcessCommonJSModules.JSModule;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    CompilerOptions options = new CompilerOptions(true, null);
    CompilerPassManager passManager = new CompilerPassManager(options, null);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(passManager, "test");

    Node root = IR.program(Nodes.empty());
    String filename = "test.js";
    String requiredFilename = "required.js";
    root.addChild(IR.script(filename, Nodes.empty(), new File(filename)));
    root.addChild(IR.script(requiredFilename, Nodes.empty(), new File(requiredFilename)));

    processor.process(Nodes.empty(), root);

    JSModule module = processor.getModule();
    assertNotNull(module);
    assertEquals(2, module.size());
    Set<String> dependencies = Sets.newHashSet(module.getDependencies());
    assertTrue(dependencies.contains(filename));
    assertTrue(dependencies.contains(requiredFilename));
  }

  @Test
  public void testProcessCommonJSModulesWithEmptyModule() {
    CompilerOptions options = new CompilerOptions(true, null);
    CompilerPassManager passManager = new CompilerPassManager(options, null);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(passManager, "test");

    Node root = IR.program(Nodes.empty());
    root.addChild(IR.script("empty.js", Nodes.empty(), new File("empty.js")));

    processor.process(Nodes.empty(), root);

    JSModule module = processor.getModule();
    assertNotNull(module);
    assertEquals(0, module.size());
    assertNull(module.getDependencies());
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModulesWithInvalidUri() {
    CompilerOptions options = new CompilerOptions(true, null);
    CompilerPassManager passManager = new CompilerPassManager(options, null);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(passManager, "test");

    Node root = IR.program(Nodes.empty());
    String filename = "test.js";
    String requiredFilename = "invalid:uri/required.js";
    root.addChild(IR.script(filename, Nodes.empty(), new File(filename)));
    root.addChild(IR.script(requiredFilename, Nodes.empty(), new File(filename)));

    processor.process(Nodes.empty(), root);
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModulesWithInvalidModuleName() {
    CompilerOptions options = new CompilerOptions(true, null);
    CompilerPassManager passManager = new CompilerPassManager(options, null);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(passManager, "test");

    Node root = IR.program(Nodes.empty());
    String filename = "test.js";
    String requiredFilename = "test-with-invalid-module-name.js";
    root.addChild(IR.script(filename, Nodes.empty(), new File(filename)));
    root.addChild(IR.script(requiredFilename, Nodes.empty(), new File(filename)));

    processor.process(Nodes.empty(), root);
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequire() {
    CompilerOptions options = new CompilerOptions(true, null);
    CompilerPassManager passManager = new CompilerPassManager(options, null);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(passManager, "test");

    Node root = IR.program(Nodes.empty());
    String filename = "test.js";
    String requiredFilename = "./relative.js";
    root.addChild(IR.script(filename, Nodes.empty(), new File(filename)));
    root.addChild(IR.script(requiredFilename, Nodes.empty(), new File(filename)));

    processor.process(Nodes.empty(), root);

    JSModule module = processor.getModule();
    assertNotNull(module);
    assertEquals(2, module.size());
    Set<String> dependencies = Sets.newHashSet(module.getDependencies());
    assertTrue(dependencies.contains(filename));
    assertTrue(dependencies.contains(requiredFilename.replaceAll("\\./", "")));
  }

  @Test
  public void testProcessCommonJSModulesWithMultipleRequires() {
    CompilerOptions options = new CompilerOptions(true, null);
    CompilerPassManager passManager = new CompilerPassManager(options, null);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(passManager, "test");

    Node root = IR.program(Nodes.empty());
    String filename = "test.js";
    String requiredFilename1 = "required1.js";
    String requiredFilename2 = "required2.js";
    root.addChild(IR.script(filename, Nodes.empty(), new File(filename)));
    root.addChild(IR.script(requiredFilename1, Nodes.empty(), new File(requiredFilename1)));
    root.addChild(IR.script(requiredFilename2, Nodes.empty(), new File(requiredFilename2)));

    processor.process(Nodes.empty(), root);

    JSModule module = processor.getModule();
    assertNotNull(module);
    assertEquals(3, module.size());
    Set<String> dependencies = Sets.newHashSet(module.getDependencies());
    assertTrue(dependencies.contains(filename));
    assertTrue(dependencies.contains(requiredFilename1));
    assertTrue(dependencies.contains(requiredFilename2));
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports() {
    CompilerOptions options = new CompilerOptions(true, null);
    CompilerPassManager passManager = new CompilerPassManager(options, null);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(passManager, "test");

    Node root = IR.program(Nodes.empty());
    String filename = "test.js";
    String requiredFilename = "module_exports.js";
    root.addChild(IR.script(filename, Nodes.empty(), new File(filename)));
    root.addChild(IR.script(requiredFilename, Nodes.empty(), new File(requiredFilename)));

    processor.process(Nodes.empty(), root);

    JSModule module = processor.getModule();
    assertNotNull(module);
    assertEquals(2, module.size());
    Set<String> dependencies = Sets.newHashSet(module.getDependencies());
    assertTrue(dependencies.contains(filename));
    assertTrue(dependencies.contains(requiredFilename));
    JSModule requiredModule = module.get(requiredFilename);
    assertNotNull(requiredModule);
    assertEquals(1, requiredModule.size());
    Set<String> requiredDependencies = Sets.newHashSet(requiredModule.getDependencies());
    assertFalse(requiredDependencies.isEmpty());
  }

  @Test
  public void testProcessCommonJSModulesWithCycleDependency() {
    CompilerOptions options = new CompilerOptions(true, null);
    CompilerPassManager passManager = new CompilerPassManager(options, null);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(passManager, "test");

    Node root = IR.program(Nodes.empty());
    String filename1 = "module1.js";
    String filename2 = "module2.js";
    root.addChild(IR.script(filename1, Nodes.empty(), new File(filename1)));
    root.addChild(IR.script(filename2, Nodes.empty(), new File(filename2)));
    IR.script script1 = (IR.script) root.getChildAtIndex(0);
    IR.exprStatement statement1 = script1.getFirstChild().getFirstChild();
    statement1.replaceChild(statement1.getChildAtIndex(1), IR.string("module2.js"));
    IR.script script2 = (IR.script) root.getChildAtIndex(1);
    IR.exprStatement statement2 = script2.getFirstChild().getFirstChild();
    statement2.replaceChild(statement2.getChildAtIndex(1), IR.string("module1.js"));

    try {
      processor.process(Nodes.empty(), root);
      fail("Should throw an exception");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testProcessCommonJSModulesWithMissingModule() {
    CompilerOptions options = new CompilerOptions(true, null);
    CompilerPassManager passManager = new CompilerPassManager(options, null);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(passManager, "test");

    Node root = IR.program(Nodes.empty());
    String filename = "test.js";
    String requiredFilename = "missing.js";
    root.addChild(IR.script(filename, Nodes.empty(), new File(filename)));
    root.addChild(IR.script(requiredFilename, Nodes.empty(), new File(filename)));

    processor.process(Nodes.empty(), root);

    JSModule module = processor.getModule();
    assertNotNull(module);
    assertEquals(1, module.size());
    assertNull(module.get(requiredFilename));
  }
}