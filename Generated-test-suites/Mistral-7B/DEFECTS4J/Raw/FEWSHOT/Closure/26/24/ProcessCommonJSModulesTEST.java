package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.NodeTraversalFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private AbstractCompiler compiler;
  private NodeTraversalFactory nodeTraversalFactory;
  private ProcessCommonJSModules pcm;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeTraversalFactory = mock(NodeTraversal.NodeTraversalFactory.class);
    pcm = new ProcessCommonJSModules(compiler, "test", true);
  }

  @org.junit.Test
  public void testGuessCJSModuleName() {
    File file = new File("test/subdir/module.js");
    String filename = normalizeSourceName(file.getAbsolutePath());
    String expected = "module$subdir$module";
    String actual = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, actual);

    file = new File("test/module.js");
    expected = "module$module";
    filename = normalizeSourceName(file.getAbsolutePath());
    actual = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, actual);

    file = new File("test/subdir/module-with-hyphen.js");
    expected = "module$subdir$module-with-hyphen";
    filename = normalizeSourceName(file.getAbsolutePath());
    actual = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testGuessCJSModuleNameWithRelativePath() {
    File currentFile = new File("test/main.js");
    String requiredFilename = "subdir/module.js";
    String expected = "module$subdir$module";
    String actual = ProcessCommonJSModules.toModuleName(requiredFilename,
        normalizeSourceName(currentFile.getAbsolutePath()));
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testGuessCJSModuleNameWithRelativePathWithDots() {
    File currentFile = new File("test/main.js");
    String requiredFilename = "subdir/module.js";
    String expected = "module$subdir$module";
    String actual = ProcessCommonJSModules.toModuleName(requiredFilename,
        normalizeSourceName(currentFile.getAbsolutePath()));
    assertEquals(expected, actual);

    currentFile = new File("test/subdir/main.js");
    requiredFilename = "../module.js";
    expected = "module$module";
    actual = ProcessCommonJSModules.toModuleName(requiredFilename,
        normalizeSourceName(currentFile.getAbsolutePath()));
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testProcess() throws URISyntaxException {
    SourceName sourceName = new SourceName("test/module.js");
    when(compiler.getTraversalFactory()).thenReturn(nodeTraversalFactory);
    Node root = IR.program();
    pcm.process(null, root);

    NodeTraversal traverser = nodeTraversalFactory.create(root);
    AbstractPostOrderCallback callback = new ProcessCommonJsModulesCallback();
    traverser.traverse(root, callback);

    JSModule module = pcm.getModule();
    assertNotNull(module);

    Set<String> requires = Sets.newHashSet();
    assertEquals(1, callback.scriptNodeCount);
    Node script = callback.script;
    assertEquals(2, script.getChildCount());
    assertEquals("goog.provide(\"" + pcm.MODULE_NAME_PREFIX + "module\");",
        script.getChildAtIndex(0).getString());
    assertEquals("(function() {\n" +
        "  goog.require(\"" + pcm.MODULE_NAME_PREFIX + "module\");\n" +
        "  var module = " + pcm.MODULE_NAME_PREFIX + "module;\n" +
        "})();\n", script.getChildAtIndex(1).getString());
    RequiresVisitor requiresVisitor = new RequiresVisitor();
    traverser.traverse(root, requiresVisitor);
    assertEquals(1, requires.size());
    assertEquals("module", requires.iterator().next());
  }

  @org.junit.Test
  public void testProcessWithReportDependenciesFalse() {
    SourceName sourceName = new SourceName("test/module.js");
    when(compiler.getTraversalFactory()).thenReturn(nodeTraversalFactory);
    Node root = IR.program();
    pcm = new ProcessCommonJSModules(compiler, "test", false);
    pcm.process(null, root);

    NodeTraversal traverser = nodeTraversalFactory.create(root);
    AbstractPostOrderCallback callback = new ProcessCommonJsModulesCallback();
    traverser.traverse(root, callback);

    JSModule module = pcm.getModule();
    assertNull(module);
  }
}

class RequiresVisitor extends AbstractPostOrderCallback {
  private final Set<String> requires = Sets.newHashSet();

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.isCall() && n.getChildCount() == 2 &&
        "require".equals(n.getFirstChild().getQualifiedName()) &&
        n.getChildAtIndex(1).isString()) {
      String moduleName = toModuleName(n.getChildAtIndex(1).getString(),
          normalizeSourceName(t.getSourceName()));
      requires.add(moduleName);
    }
  }
}

class ProcessCommonJsModulesCallback extends AbstractPostOrderCallback {
  int scriptNodeCount = 0;
  Node script;

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.isCall() && n.getChildCount() == 2 &&
        "require".equals(n.getFirstChild().getQualifiedName()) &&
        n.getChildAtIndex(1).isString()) {
      visitRequireCall(t, n, parent);
    } else if (n.isScript()) {
      scriptNodeCount++;
      script = n;
      visitScript(t, n);
    }
  }

  private void visitRequireCall(NodeTraversal t, Node require, Node parent) {
    String moduleName = toModuleName(require.getChildAtIndex(1).getString(),
        normalizeSourceName(t.getSourceName()));
    Node moduleRef = IR.name(moduleName).srcref(require);
    parent.replaceChild(require, moduleRef);
    Node script = getCurrentScriptNode(parent);
    NodeTraversal.traverse(compiler, script, new RewriteRequireVisitor(moduleName));
    compiler.reportCodeChange();
  }

  private void visitScript(NodeTraversal t, Node script) {
    Preconditions.checkArgument(scriptNodeCount == 1,
        "ProcessCommonJSModules supports only one invocation per " +
        "CompilerInput / script node");
    String moduleName = pcm.guessCJSModuleName(normalizeSourceName(script
        .getSourceFileName()));
    script.addChildToFront(IR.var(IR.name(moduleName), IR.objectlit())
        .copyInformationFromForTree(script));
    Node googProvide = IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
        IR.string(moduleName)).copyInformationFromForTree(script);
    script.addChildToFront(googProvide);
    emitOptionalModuleExportsOverride(script, moduleName);

    NodeTraversal.traverse(compiler, script, new SuffixVarsCallback("module"));
    compiler.reportCodeChange();
  }

  private Node getCurrentScriptNode(Node n) {
    while (true) {
      if (n.isScript()) {
        return n;
      }
      n = n.getParent();
    }
  }
}

class RewriteRequireVisitor extends AbstractPostOrderCallback {
  private final String moduleName;

  RewriteRequireVisitor(String moduleName) {
    this.moduleName = moduleName;
  }

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.isCall() && n.getChildCount() == 2 &&
        "require".equals(n.getFirstChild().getQualifiedName())) {
      Node requireCall = n;
      Node newRequireCall = IR.call(IR.getprop(IR.name("goog"), IR.string("require")),
          IR.string(moduleName)).copyInformationFromForTree(requireCall);
      parent.replaceChild(requireCall, newRequireCall);
      compiler.reportCodeChange();
    }
  }
}