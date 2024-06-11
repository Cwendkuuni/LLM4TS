package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.GOOG_ALIASES;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Test;

import java.util.List;

/**
 * Tests for the ScopedAliases compiler pass.
 */
public class ScopedAliasesTest extends CompilationTestBase {

  @Override
  protected CompilerOptions getOptions() {
    return new CompilerOptions()
        .setAliasTransformation(GOOG_ALIASES);
  }

  @Test
  public void testScopedAliases() {
    String input = ""
        + "/**\n"
        + " * Copyright 2010 The Closure Compiler Authors.\n"
        + " *\n"
        + " * @suppress {checkTypes}\n"
        + "\n"
        + "goog.scope(function() {\n"
        + "  var dom = goog.dom;\n"
        + "  var DIV = dom.TagName.DIV;\n"
        + "\n"
        + "  var x = goog.scope(function() {\n"
        + "    return dom.createElement(DIV);\n"
        + "  });\n"
        + "\n"
        + "  assertEquals('DIV', x.getClassName());\n"
        + "});\n";

    TestSourceFile sourceFile = new TestSourceFile("TestScopedAliases.js", input);

    Node root = sourceFile.getRootNode();
    List<DiagnosticType> diagnostics = compile(root);
    assertEquals(0, diagnostics.size());

    Node output = getTranspiledOutput();
    assertEquals(
        "goog.dom.createElement(goog.dom.TagName.DIV);\n"
        + "var x = goog.dom.createElement(goog.dom.TagName.DIV);\n"
        + "assertEquals('DIV', x.className);\n",
        output.getSource());
  }

  @Test
  public void testScopedAliasesWithErrors() {
    String input = ""
        + "/**\n"
        + " * Copyright 2010 The Closure Compiler Authors.\n"
        + " *\n"
        + " * @suppress {checkTypes}\n"
        + "\n"
        + "goog.scope(function() {\n"
        + "  var dom = goog.dom;\n"
        + "  var DIV = dom.TagName.DIV;\n\n"
        + "  var x = function() {\n"
        + "    return dom.createElement(DIV);\n"
        + "  };\n"
        + "\n"
        + "  assertEquals('DIV', x.getClassName());\n"
        + "});\n";

    TestSourceFile sourceFile = new TestSourceFile("TestScopedAliasesErrors.js", input);

    Node root = sourceFile.getRootNode();
    List<DiagnosticType> diagnostics = compile(root);

    assertEquals(5, diagnostics.size());
    assertEquals(
        GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0).getType());
    assertEquals(
        "The call to goog.scope must be alone in a single statement.",
        diagnostics.get(0).getMessage());

    assertEquals(
        GOOG_SCOPE_HAS_BAD_PARAMETERS, diagnostics.get(1).getType());
    assertEquals(
        "The call to goog.scope must take only a single parameter.  It must" +
            " be an anonymous function that itself takes no parameters.",
        diagnostics.get(1).getMessage());

    assertEquals(
        GOOG_SCOPE_REFERENCES_THIS, diagnostics.get(2).getType());
    assertEquals(
        "The body of a goog.scope function cannot reference 'this'.",
        diagnostics.get(2).getMessage());

    assertEquals(
        GOOG_SCOPE_USES_RETURN, diagnostics.get(3).getType());
    assertEquals(
        "The body of a goog.scope function cannot use 'return'.",
        diagnostics.get(3).getMessage());

    assertEquals(
        GOOG_SCOPE_ALIAS_REDEFINED, diagnostics.get(4).getType());
    assertEquals(
        "The alias 'DIV' is assigned a value more than once.",
        diagnostics.get(4).getMessage());
  }

  @Test
  public void testScopedAliasesNested() {
    String input = ""
        + "/**\n"
        + " * Copyright 2010 The Closure Compiler Authors.\n"
        + " *\n"
        + " * @suppress {checkTypes}\n"
        + "\n"
        + "goog.scope(function() {\n"
        + "  var dom = goog.dom;\n"
        + "  var DIV = dom.TagName.DIV;\n\n"
        + "  goog.scope(function() {\n"
        + "    var div = goog.scope(function() {\n"
        + "      return dom.createElement(DIV);\n"
        + "    });\n"
        + "    assertEquals('DIV', div.getClassName());\n"
        + "  });\n"
        + "});\n";

    TestSourceFile sourceFile = new TestSourceFile("TestScopedAliasesNested.js", input);

    Node root = sourceFile.getRootNode();
    List<DiagnosticType> diagnostics = compile(root);
    assertEquals(0, diagnostics.size());

    Node output = getTranspiledOutput();
    assertEquals(
        "goog.dom.createElement(goog.dom.TagName.DIV);\n"
        + "goog.dom.createElement(goog.dom.TagName.DIV);\n"
        + "assertEquals('DIV', goog.dom.createElement(goog.dom.TagName.DIV).className);\n",
        output.getSource());
  }

  @Test
  public void testScopedAliasesWithAliasUses() {
    String input = ""
        + "/**\n"
        + " * Copyright 2010 The Closure Compiler Authors.\n"
        + " *\n"
        + " * @suppress {checkTypes}\n"
        + "\n"
        + "goog.scope(function() {\n"
        + "  var dom = goog.dom;\n"
        + "  var DIV = dom.TagName.DIV;\n\n"
        + "  goog.scope(function() {\n"
        + "    var div = goog.scope(function() {\n"
        + "      return dom.createElement(DIV);\n"
        + "    });\n"
        + "\n"
        + "    var e = goog.scope(function() {\n"
        + "      return div;\n"
        + "    });\n"
        + "\n"
        + "    assertEquals('DIV', e.getClassName());\n"
        + "  });\n"
        + "});\n";

    TestSourceFile sourceFile = new TestSourceFile("TestScopedAliasesWithAliasUses.js", input);

    Node root = sourceFile.getRootNode();
    List<DiagnosticType> diagnostics = compile(root);
    assertEquals(0, diagnostics.size());

    Node output = getTranspiledOutput();
    assertEquals(
        "goog.dom.createElement(goog.dom.TagName.DIV);\n"
        + "goog.dom.createElement(goog.dom.TagName.DIV);\n"
        + "var e = goog.dom.createElement(goog.dom.TagName.DIV);\n"
        + "assertEquals('DIV', e.className);\n",
        output.getSource());
  }

  @Test
  public void testScopedAliasesWithAliasDefinitions() {
    String input = ""
        + "/**\n"
        + " * Copyright 2010 The Closure Compiler Authors.\n"
        + " *\n"
        + " * @suppress {checkTypes}\n"
        + "\n"
        + "goog.scope(function() {\n"
        + "  var dom = goog.dom;\n"
        + "  var DIV = dom.TagName.DIV;\n\n"
        + "  var div = goog.scope(function() {\n"
        + "    DIV = goog.dom.TagName.DIV;\n"
        + "    return dom.createElement(DIV);\n"
        + "  });\n"
        + "\n"
        + "  assertEquals('DIV', div.getClassName());\n"
        + "  assertEquals('DIV', goog.dom.TagName.DIV);\n"
        + "});\n";

    TestSourceFile sourceFile = new TestSourceFile("TestScopedAliasesWithAliasDefinitions.js", input);

    Node root = sourceFile.getRootNode();
    List<DiagnosticType> diagnostics = compile(root);
    assertEquals(0, diagnostics.size());

    Node output = getTranspiledOutput();
    assertEquals(
        "goog.dom.createElement(goog.dom.TagName.DIV);\n"
        + "goog.dom.TagName.DIV = goog.dom.TagName.DIV;\n"
        + "assertEquals('DIV', goog.dom.TagName.DIV);\n"
        + "assertEquals('DIV', goog.dom.createElement(goog.dom.TagName.DIV).className);\n",
        output.getSource());
  }
}