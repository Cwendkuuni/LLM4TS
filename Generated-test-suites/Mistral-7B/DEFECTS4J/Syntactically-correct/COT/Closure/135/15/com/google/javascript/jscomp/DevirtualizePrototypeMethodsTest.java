package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class DevirtualizePrototypeMethodsTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    nodeFactory = new NodeFactory(compiler.getGlobalScope());
  }

  @Test
  public void testDevirtualizePrototypeMethods() {
    String input = "function A() {}\n" +
                   "A.prototype.accumulate = function(value) { return this.total += value; }\n" +
                   "var a = new A();\n" +
                   "var total = a.accumulate(2);";

    Node root = nodeFactory.parse(input);
    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(null, root);

    // Basic functionality test
    String expectedOutput = "var accumulate = function(self, value) {" +
                            "  self.total += value;" +
                            "  return self.total" +
                            "};\n" +
                            "var total = accumulate(a, 2);";
    String actualOutput = nodeFactory.getSource(compiler.getGlobalScope());
    assertEquals(expectedOutput, actualOutput);

    // Edge cases and exception handling tests go here
  }
}