package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeUtil.isCall;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {
  @Test
  public void testDevirtualizePrototypeMethods() throws Exception {
    // Set up test environment
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass devirtualizer = new DevirtualizePrototypeMethods(compiler);
    JSTypeRegistry typeRegistry = compiler.getTypeRegistry();

    // Prepare test input
    String sourceCode =
        "var A = function() {\n" +
        "  this.total = 0;\n" +
        "  this.accumulate = function(value) {\n" +
        "    this.total += value;\n" +
        "    return this.total;\n" +
        "  };\n" +
        "};\n" +
        "\n" +
        "var a = new A();\n" +
        "var total = a.accumulate(2);\n";

    Node externs = Node.parse(new StringBuilder(externsTemplates).toString());
    Node root = Node.parse(sourceCode);

    // Run pass
    devirtualizer.process(externs, root);

    // Verify output
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    for (DefinitionSite defSite : definitionSites) {
      if (NodeUtil.isFunction(defSite.node) &&
          defSite.definition.getRValue().isFunction()) {
        String originalMethodName =
            defSite.definition.getLValue().getLastChild().getString();
        String newMethodName = "JSCompiler_StaticMethods_" + originalMethodName;

        Definition definition = defSite.definition;
        Node definitionNode = definition.getNode();

        // Check if definition was processed
        assertEquals(newMethodName, definitionNode.getLastChild().getString());

        // Check if call sites were rewritten
        Collection<UseSite> useSites = defFinder.getUseSites(definition);
        for (UseSite useSite : useSites) {
          Node callSite = useSite.node;
          assertEquals(newMethodName, callSite.getChild(0).getString());
        }
      }
    }
  }
}

class NodeUtil {
  // ... (same as in the original class)
}

class SimpleDefinitionFinder {
  // ... (same as in the original class)
}

class UseSite {
  // ... (same as in the original class)
}

class DefinitionSite {
  // ... (same as in the original class)
}