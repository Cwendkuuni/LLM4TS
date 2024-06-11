package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {

  private AbstractCompiler compiler;
  private DevirtualizePrototypeMethods devirtualizePrototypeMethods;

  @Before
  public void setup() {
    compiler = new AbstractCompiler() {
      // Add any necessary configurations or setup here
    };

    devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
  }

  @Test
  public void testProcess_ValidPrototypeMethod() {
    Node root = Node.externalizeString("var a = {\n" +
        "prototype: {\n" +
        "  accumulate: function(value) {\n" +
        "    this.total += value;\n" +
        "    return this.total\n" +
        "  }\n" +
        "}\n" +
        "};\n" +
        "var total = a.prototype.accumulate(2);");

    devirtualizePrototypeMethods.process(null, root);

    Node expectedRoot = Node.externalizeString("var a = {\n" +
        "prototype: {\n" +
        "  accumulate: function(self, value) {\n" +
        "    self.total += value;\n" +
        "    return self.total\n" +
        "  }\n" +
        "}\n" +
        "};\n" +
        "var total = JSCompiler_StaticMethods_accumulate(a, 2);");

    assertEquals(expectedRoot.toString(), root.toString());
  }

  @Test(expected = AssertionError.class)
  public void testProcess_PrototypeMethodWithArgumentsFunction() {
    Node root = Node.externalizeString("var a = {\n" +
        "prototype: {\n" +
        "  accumulate: function(values) {\n" +
        "    return values.length;\n" +
        "  }\n" +
        "}\n" +
        "};\n" +
        "var total = a.prototype.accumulate([1, 2, 3]);");

    devirtualizePrototypeMethods.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_PrototypeMethodWithVarArgs() {
    Node root = Node.externalizeString("var a = {\n" +
        "prototype: {\n" +
        "  accumulate: function() {\n" +
        "    var sum = 0;\n" +
        "    for (var i = 0; i < arguments.length; i++) {\n" +
        "      sum += arguments[i];\n" +
        "    }\n" +
        "    return sum;\n" +
        "  }\n" +
        "}\n" +
        "};\n" +
        "var total = a.prototype.accumulate(1, 2, 3);");

    devirtualizePrototypeMethods.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_PrototypeMethodWithThisInsideFunction() {
    Node root = Node.externalizeString("var a = {\n" +
        "prototype: {\n" +
        "  accumulate: function() {\n" +
        "    return function() {\n" +
        "      return this.total;\n" +
        "    }.bind(this);\n" +
        "  }\n" +
        "}\n" +
        "};\n" +
        "var total = a.prototype.accumulate();");

    devirtualizePrototypeMethods.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_PrototypeMethodWithThisInsideObject() {
    Node root = Node.externalizeString("var a = {\n" +
        "prototype: {\n" +
        "  accumulate: function() {\n" +
        "    return {\n" +
        "      total: 0,\n" +
        "      getTotal: function() {\n" +
        "        return this.total;\n" +
        "      }\n" +
        "    }.getTotal();\n" +
        "  }\n" +
        "}\n" +
        "};\n" +
        "var total = a.prototype.accumulate();");

    devirtualizePrototypeMethods.process(null, root);
  }

  @Test
  public void testProcess_MultiplePrototypeMethods() {
    Node root = Node.externalizeString("var a = {\n" +
        "prototype: {\n" +
        "  accumulate: function(value) {\n" +
        "    this.total += value;\n" +
        "    return this.total\n" +
        "  }\n" +
        "},\n" +
        "prototype2: {\n" +
        "  decrement: function(value) {\n" +
        "    this.total -= value;\n" +
        "    return this.total\n" +
        "  }\n" +
        "}}\n" +
        "};\n" +
        "var total = a.prototype.accumulate(2);\n" +
        "var total2 = a.prototype2.decrement(3);");

    devirtualizePrototypeMethods.process(null, root);

    Node expectedRoot = Node.externalizeString("var a = {\n" +
        "prototype: {\n" +
        "  accumulate: function(self, value) {\n" +
        "    self.total += value;\n" +
        "    return self.total\n" +
        "  }\n" +
        "},\n" +
        "prototype2: {\n" +
        "  decrement: function(self, value) {\n" +
        "    self.total -= value;\n" +
        "    return self.total\n" +
        "  }\n" +
        "}}\n" +
        "};\n" +
        "var total = JSCompiler_StaticMethods_accumulate(a, 2);\n" +
        "var total2 = JSCompiler_StaticMethods_decrement(a, 3);");

    assertEquals(expectedRoot.toString(), root.toString());
  }
}