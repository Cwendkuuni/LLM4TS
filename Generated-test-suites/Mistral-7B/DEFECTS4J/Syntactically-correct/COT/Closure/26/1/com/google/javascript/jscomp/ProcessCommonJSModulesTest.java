package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ProcessingEnvironment;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class ProcessCommonJSModulesTest {

  @Mock
  private AbstractCompiler compiler;
  @Mock
  private ProcessingEnvironment env;
  @Mock
  private CompilerInput input;
  @Mock
  private CompilerOptions options;

  private NodeFactory nodeFactory;
  private TestCompiler testCompiler;
  private ProcessCommonJSModules classUnderTest;
  private Set<String> requires;

  @Before
  public void setUp() {
    Preconditions.checkState(compiler != null, "compiler cannot be null");
    Preconditions.checkState(env != null, "processingEnvironment cannot be null");
    Preconditions.checkState(input != null, "compilerInput cannot be null");
    Preconditions.checkState(options != null, "compilerOptions cannot be null");

    nodeFactory = new NodeFactory(env);
    testCompiler = new TestCompiler();
    testCompiler.init(input, options, compiler);
    classUnderTest = new ProcessCommonJSModules(compiler, ".", true);
    requires = Sets.newHashSet();
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "src/main/js/module1.js";
    String expected = "module1";
    assertEquals(expected, classUnderTest.guessCJSModuleName(filename));
  }

  @Test
  public void testProcessCommonJSModulesCallbackVisitRequireCall() {
    String requiredModule = "module2";
    String scriptName = "script.js";
    String moduleName = "module1";

    String requireNodeString = "require(\"" + requiredModule + "\")";
    Node requireNode = nodeFactory.createCall(null, "require", null,
        nodeFactory.createString(requiredModule));
    Node scriptNode = nodeFactory.createScript("// script.js");

    Node scriptParent = nodeFactory.createBlock(null);
    scriptParent.addChild(requireNode);
    scriptParent.addChild(scriptNode);

    BDDMockito.given(compiler.reportCodeChange()).willReturn(true);
    BDDMockito.given(input.addRequire(requiredModule)).will(invocation -> {
      requires.add(requiredModule);
    });

    classUnderTest.process(input, null);

    assertEquals(1, requires.size());
    assertEquals(requiredModule, requires.iterator().next());

    Node moduleRef = scriptNode.getLastChild();
    assertEquals("module2", moduleRef.getString());

    Node googRequireCall = scriptNode.getChildAtIndex(0);
    assertEquals("require", googRequireCall.getFirstChild().getString());
    assertEquals(requiredModule, googRequireCall.getLastChild().getString());

    assertEquals(1, scriptNode.getChildCount());
  }

  @Test
  public void testProcessCommonJSModulesCallbackVisitScript() {
    String moduleName = "module1";
    String scriptName = "script.js";

    Node scriptNode = nodeFactory.createScript("// script.js");
    Node moduleNode = nodeFactory.createVar("module1", null, null);
    Node provideCall = nodeFactory.createCall(null, "goog.provide", null,
        nodeFactory.createString(moduleName));

    scriptNode.addChildToFront(provideCall);
    scriptNode.addChildToFront(moduleNode);

    BDDMockito.given(compiler.reportCodeChange()).willReturn(true);

    classUnderTest.process(input, null);

    JSModule jsModule = classUnderTest.getModule();
    assertNotNull(jsModule);
    assertEquals(moduleName, jsModule.getName());
  }

  @Test
  public void testProcessCommonJSModulesCallbackVisitModuleExports() {
    String moduleName = "module1";
    String exportsName = "exports";

    Node moduleNode = nodeFactory.createGetProp(
        nodeFactory.createName(moduleName), "exports");
    Node exportsNode = nodeFactory.createVar(exportsName, null, null);

    moduleNode.putProp(Node.ORIGINALNAME_PROP, "module");
    exportsNode.putProp(Node.ORIGINALNAME_PROP, "exports");

    Node scriptNode = nodeFactory.createScript("// script.js");
    scriptNode.addChildToBack(moduleNode);
    scriptNode.addChildToBack(exportsNode);

    Node googProvideCall = scriptNode.getChildAtIndex(0);
    assertEquals("goog.provide", googProvideCall.getFirstChild().getString());
    assertEquals(moduleName, googProvideCall.getLastChild().getString());

    Node googRequireCall = scriptNode.getChildAtIndex(1);
    assertEquals("require", googRequireCall.getFirstChild().getString());
    assertEquals("module$exports", googRequireCall.getLastChild().getString());

    classUnderTest.process(input, null);

    assertEquals(1, scriptNode.getChildCount());
    assertEquals(1, scriptNode.getChildAtIndex(0).getChildCount());
  }

  @Test
  public void testSuffixVarsCallback() {
    String suffix = "_test";
    String name = "name";

    Node nameNode = nodeFactory.createName(name);

    Node scriptNode = nodeFactory.createScript("// script.js");
    scriptNode.addChildToBack(nameNode);

    classUnderTest.process(input, null);

    assertEquals(name + "$$" + suffix, nameNode.getString());
  }
}