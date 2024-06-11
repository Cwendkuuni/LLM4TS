package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import java.io.File;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ProcessCommonJSModulesTest {

  private static final String TEST_INPUT = "testInput.js";
  private static final String TEST_OUTPUT = "testOutput.js";
  private static final String TEST_MODULE = "testModule.js";

  private CompilerInput input;
  private CompilerOptions options;
  private AbstractCompiler compiler;
  private ProcessCommonJSModules processor;
  private Processor processorUnderTest;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = mock(AbstractCompiler.class);
    processor = new ProcessCommonJSModules(compiler, "");
    NodeFactory nodeFactory = new NodeFactory();
    input = new CompilerInput("test", new File(TEST_INPUT), nodeFactory, options);
    processorUnderTest = new Processor(input, options, null, null);
  }

  @Test
  public void testProcessCommonJSModules() {
    when(compiler.getOptions()).thenReturn(options);
    when(compiler.getInput()).thenReturn(input);
    when(compiler.getProcessor()).thenReturn(processorUnderTest);

    processor.process(input.externs(), input.root());

    Node output = processorUnderTest.getJSExtternal();

    NodeTraversal traversal = new NodeTraversal(processorUnderTest, output, null);
    Node scriptNode = traversal.getFirstChildOfType(Node.SCRIPT, output);
    Node script = scriptNode.getFirstChild();

    // Check that goog.provide is emitted.
    Node googProvide = script.getFirstChild();
    assertThat(googProvide.getQualifiedName()).isEqualTo("goog.provide");
    assertThat(googProvide.getChildAtIndex(1)).isInstanceOf(Node.call);
    Node moduleName = ((Node.call) googProvide.getChildAtIndex(1)).getChildAtIndex(0);
    assertThat(moduleName.getString()).isEqualTo("test");

    // Check that require calls are rewritten.
    Node requireCall = script.getChildAtIndex(1);
    Node require = requireCall.getChildAtIndex(0);
    Node requireModule = requireCall.getChildAtIndex(1);
    Node requireModuleName = requireModule.getFirstChild();
    assertThat(requireModuleName.getString()).isEqualTo("testModule");
    Node moduleRef = script.getChildAtIndex(2);
    Node module = moduleRef.getFirstChild();
    Node moduleName2 = module.getFirstChild();
    assertThat(moduleName2.getString()).isEqualTo("testModule");

    // Check that module.exports is renamed.
    Node moduleExports = script.getChildAtIndex(3);
    assertThat(moduleExports.getString()).isEqualTo("test$exports");

    // Check that variables are renamed to avoid conflicts.
    Node variable1 = script.getChildAtIndex(4);
    assertThat(variable1.getString()).isEqualTo("test$variable1");

    // Check that global variables are renamed.
    Node variable2 = script.getChildAtIndex(5);
    Scope scope = processorUnderTest.getCurrentScope();
    assertThat(variable2.getString()).isEqualTo("test$variable2$$");
    assertThat(scope.getVar("variable2")).isNotNull();
  }

  @Test
  public void testProcessCommonJSModulesWithDependenciesReporting() {
    options.setOption("--report_js_errors", false);
    options.setOption("--warning_level", CompilerOptions.WarningLevel.QUIET);

    when(compiler.getOptions()).thenReturn(options);
    when(compiler.getInput()).thenReturn(input);
    when(compiler.getProcessor()).thenReturn(processorUnderTest);

    processor = new ProcessCommonJSModules(compiler, "", true);
    processor.process(input.externs(), input.root());

    Node output = processorUnderTest.getJSExtternal();

    NodeTraversal traversal = new NodeTraversal(processorUnderTest, output, null);
    Node scriptNode = traversal.getFirstChildOfType(Node.SCRIPT, output);
    Node script = scriptNode.getFirstChild();

    // Check that require calls are reported as dependencies.
    Node requireCall = script.getChildAtIndex(1);
    Node require = requireCall.getChildAtIndex(0);
    Node requireModule = requireCall.getChildAtIndex(1);
    Node requireModuleName = requireModule.getFirstChild();
    Node requireDependency = input.getRequire("");
    assertThat(requireDependency).isNotNull();
    assertThat(requireDependency.getChildAtIndex(0)).isInstanceOf(Node.string);
    Node requireDependencyString = requireDependency.getChildAtIndex(0);
    assertThat(requireDependencyString.getString()).isEqualTo(requireModuleName.getString());
  }
}