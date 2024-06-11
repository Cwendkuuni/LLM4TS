package org.apache.commons.cli; // this package should match the package of the class under test

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.isInstanceOf;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

public class ProcessCommonJSModulesTest {

  private CompilerOptions options;
  private CompilerInput input;
  private ProcessCommonJSModules pass;

  @Before
  public void setup() {
    options = new CompilerOptions();
    options.setOutputFile("output.js");
    input = new CompilerInput("input.js", null, null, null, null, null);
    pass = new ProcessCommonJSModules(new Compiler(), ".", false);
  }

  @Test
  public void testProcessCommonJSModules() {
    pass.process(input, null);
    assertNotNull(pass.getModule());
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "path/to/module.js";
    String expected = "module$path_to_module";
    String actual = ProcessCommonJSModules.toModuleName(filename);
    assertThat(actual, is(expected));
  }

  @Test
  public void testToModuleNameWithRelativePath() {
    String requiredFilename = "./path/to/module.js";
    String currentFilename = "path/to/main.js";
    String expected = "module$path_to_module";
    String actual = ProcessCommonJSModules.toModuleName(requiredFilename,
        currentFilename);
    assertThat(actual, is(expected));
  }

  @Test
  public void testNormalizeSourceName() {
    String filename = "path/.to/module.js";
    String expected = "path/to/module";
    String actual = ProcessCommonJSModules.normalizeSourceName(filename);
    assertThat(actual, is(expected));
  }

  @Test
  public void testProcessScript() {
    String moduleName = "module$path_to_module";
    Node script = IR.scriptNode(1, null, null);
    Node module = IR.var(IR.name(moduleName), null).copyInformationFromForTree(script);
    script.addChildToFront(module);
    Node inputScript = new Node(Node.SCRIPT);
    inputScript.addChildToFront(script);

    NodeTraversal traverser = new NodeTraversal(options, null, null);
    traverser.traverse(pass, inputScript);

    Node actualModule = traverser.getOutput().getTopNode().getChildAtIndex(0);
    assertThat(actualModule, isInstanceOf(IR.VarNode.class));
    assertThat(actualModule.getString(), is(moduleName));

    Node actualProvide = actualModule.getChildAtIndex(1);
    assertThat(actualProvide, isInstanceOf(IR.CallNode.class));
    assertThat(actualProvide.getChildAtIndex(0), isInstanceOf(IR.NameNode.class));
    assertThat(actualProvide.getChildAtIndex(0).getString(), is("goog.provide"));
    assertThat(actualProvide.getChildAtIndex(1), isInstanceOf(IR.StringNode.class));
    assertThat(actualProvide.getChildAtIndex(1).getString(), is(moduleName));

    Node actualModuleExports = actualModule.getChildAtIndex(2);
    assertThat(actualModuleExports, isInstanceOf(IR.GetPropNode.class));
    assertThat(actualModuleExports.getQualifiedName(), is("module.exports"));

    Node actualModuleExportsAssignment = actualModuleExports.getChildAtIndex(1);
    assertThat(actualModuleExportsAssignment, isInstanceOf(IR.AssignNode.class));
    assertThat(actualModuleExportsAssignment.getChildAtIndex(0),
        isInstanceOf(IR.NameNode.class));
    assertThat(actualModuleExportsAssignment.getChildAtIndex(1),
        isInstanceOf(IR.GetPropNode.class));
    assertThat(actualModuleExportsAssignment.getChildAtIndex(1).getQualifiedName(),
        is("module$exports"));
  }

  @Test
  public void testProcessRequireCall() {
    String requiredModuleName = "module$required_module";
    String moduleName = "module$path_to_module";

    Node requireNode = IR.callNode(1, "require", null, IR.stringNode("required_module"));
    Node script = IR.scriptNode(1, null, null);
    script.addChildToBack(requireNode.cloneTree());

    NodeTraversal traverser = new NodeTraversal(options, null, null);
    traverser.traverse(pass, script);

    Node actualModuleReference = script.getChildAtIndex(0);
    assertThat(actualModuleReference, isInstanceOf(IR.NameNode.class));
    assertThat(actualModuleReference.getString(), is(requiredModuleName));

    Node actualRequireCall = actualModuleReference.getParent();
    assertThat(actualRequireCall, isInstanceOf(IR.CallNode.class));
    assertThat(actualRequireCall.getChildAtIndex(0), isInstanceOf(IR.GetpropNode.class));
    assertThat(actualRequireCall.getChildAtIndex(0).getQualifiedName(),
        is("goog.require"));
    assertThat(actualRequireCall.getChildAtIndex(1), isInstanceOf(IR.StringNode.class));
    assertThat(actualRequireCall.getChildAtIndex(1).getString(), is(requiredModuleName));

    Node actualRewrittenRequireCall = actualModuleReference.getChildAtIndex(0);
    assertThat(actualRewrittenRequireCall, isInstanceOf(IR.CallNode.class));
    assertThat(actualRewrittenRequireCall.getChildAtIndex(0), isInstanceOf(IR.NameNode.class));
    assertThat(actualRewrittenRequireCall.getChildAtIndex(0).getString(), is(moduleName));
  }
}