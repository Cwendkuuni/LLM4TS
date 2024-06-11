package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.FullCompilationTest;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.testtype.SimpleDefinition;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;

/**
 * Test class for DevirtualizePrototypeMethods.
 */
public class DevirtualizePrototypeMethodsTest {

  @org.junit.Test
  public void testDevirtualizePrototypeMethods() throws Exception {
    

    String input =
        "function A() {\n" +
        "  this.total = 0;\n" +
        "  this.accumulate = function(value) {\n" +
        "    this.total += value;\n" +
        "    return this.total;\n" +
        "  };\n" +
        "}\n" +
        "\n" +
        "A.prototype.accumulate = function(value) {\n" +
        "  this.total += value;\n" +
        "  return this.total;\n" +
        "};\n" +
        "\n" +
        "var a = new A();\n" +
        "var total = a.accumulate(2);\n" +
        "\n" +
        "function B() {\n" +
        "}\n" +
        "\n" +
        "B.prototype.accumulate = function(value) {\n" +
        "  this.total += value;\n" +
        "  return this.total;\n" +
        "};\n" +
        "\n" +
        "var b = new B();\n" +
        "var total2 = b.accumulate(2);\n" +
        "\n" +
        "function C() {\n" +
        "  this.x = 1;\n" +
        "  this.accumulate = function(value) {\n" +
        "    this.x += value;\n" +
        "    return this.x;\n" +
        "  };\n" +
        "}\n" +
        "\n" +
        "C.prototype.accumulate = function(value) {\n" +
        "  this.x += value;\n" +
        "  return this.x;\n" +
        "};\n" +
        "\n" +
        "var c = new C();\n" +
        "var total3 = c.accumulate(2);\n";

    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    CompilerPass devirtualizer = new DevirtualizePrototypeMethods(compiler);

    Node externs = Node.parseJsonString(compiler, "{}");
    Node root = Node.parse(compiler, input);

    devirtualizer.process(externs, root);
    defFinder.process(externs, root);

    List<SimpleDefinition> definitions = Lists.newArrayList();
    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      definitions.add(defSite.definition);
    }

    // Test rewritten method calls.
    String output = compiler.compile(root, null, null);
    String expectedOutput =
        "function A() {\n" +
        "  var JSCompiler_StaticMethods_accumulate = function(self, value) {\n" +
        "    self.total += value;\n" +
        "    return self.total;\n" +
        "  };\n" +
        "\n" +
        "  this.JSCompiler_StaticMethods_accumulate = JSCompiler_StaticMethods_accumulate;\n" +
        "  this.total = 0;\n" +
        "}\n" +
        "\n" +
        "A.prototype.accumulate = null;\n" +
        "\n" +
        "function B() {\n" +
        "  var JSCompiler_StaticMethods_accumulate = function(self, value) {\n" +
        "    self.total += value;\n" +
        "    return self.total;\n" +
        "  };\n" +
        "\n" +
        "  this.JSCompiler_StaticMethods_accumulate = JSCompiler_StaticMethods_accumulate;\n" +
        "}\n" +
        "\n" +
        "B.prototype.accumulate = null;\n" +
        "\n" +
        "function C() {\n" +
        "  var JSCompiler_StaticMethods_accumulate = function(self, value) {\n" +
        "    self.x += value;\n" +
        "    return self.x;\n" +
        "  };\n" +
        "\n" +
        "  this.JSCompiler_StaticMethods_accumulate = JSCompiler_StaticMethods_accumulate;\n" +
        "  this.x = 1;\n" +
        "}\n" +
        "\n" +
        "C.prototype.accumulate = null;\n" +
        "\n" +
        "var a = new A();\n" +
        "var total = a.JSCompiler_StaticMethods_accumulate(2);\n" +
        "\n" +
        "var b = new B();\n" +
        "var total2 = b.JSCompiler_StaticMethods_accumulate(2);\n" +
        "\n" +
        "var c = new C();\n" +
        "var total3 = c.JSCompiler_StaticMethods_accumulate(2);\n";

    assertEquals(expectedOutput, output);

    // Test rewritten method definitions.
    FullCompilationTest.testCompiledCode(compiler, root, definitions);

    
  }
}