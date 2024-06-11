import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.DefinitionSite;
import com.google.javascript.jscomp.Definition;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.ObjectType;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testDevirtualizePrototypeMethods() {
    Compiler compiler = new Compiler();
    CompilerPass devirtualizer = new DevirtualizePrototypeMethods(compiler);

    String sourceCode = "class A {\n" +
        "  constructor() {\n" +
        "    this.total = 0;\n" +
        "  }\n" +
        "  prototype.accumulate = function(value) {\n" +
        "    this.total += value; return this.total\n" +
        "  };\n" +
        "  var total = a.accumulate(2);\n" +
        "}";

    Node root = AbstractCompilerTest.parseAndCompileToString(sourceCode);

    devirtualizer.process(null, root);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);

    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
      devirtualizePrototypeMethods.rewriteDefinitionIfEligible(defSite, defFinder);
    }

    // Basic functionality test
    Node accumulate = root.lookupVar("accumulate");
    Node total = root.lookupVar("total");
    Node a = root.lookupVar("a");

    assertEquals(2, compiler.getEvaluatedValue(compiler.run(root, null), "total"));
    assertEquals(2, compiler.getEvaluatedValue(accumulate, a, 2));
    assertEquals(4, compiler.getEvaluatedValue(accumulate, a, 2));

    // Test for multiple definitions
    String multipleDefinitionsCode = "class A {\n" +
        "  constructor() {\n" +
        "  }\n" +
        "  prototype.accumulate = function(value) {\n" +
        "    this.total += value; return this.total\n" +
        "  };\n" +
        "}\n" +
        "class B {\n" +
        "  constructor() {\n" +
        "  }\n" +
        "  prototype.accumulate = function(value) {\n" +
        "    this.total += value; return this.total\n" +
        "  };\n" +
        "}";

    root = AbstractCompilerTest.parseAndCompileToString(multipleDefinitionsCode);

    devirtualizer.process(null, root);

    defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);

    List<DefinitionSite> definitionSites = Lists.newArrayList();
    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      if (defSite.getDefinition().getName().equals("accumulate")) {
        definitionSites.add(defSite);
      }
    }

    for (DefinitionSite defSite : definitionSites) {
      DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
      devirtualizePrototypeMethods.rewriteDefinitionIfEligible(defSite, defFinder);
    }

    Node a1 = root.lookupVar("a");
    Node a2 = root.lookupVar("b");

    assertEquals(0, compiler.getEvaluatedValue(root, null));

    assertEquals(2, compiler.getEvaluatedValue(accumulate, a1, 2));
    assertEquals(4, compiler.getEvaluatedValue(accumulate, a1, 2));

    assertEquals(0, compiler.getEvaluatedValue(accumulate, a2, 2));
    assertEquals(2, compiler.getEvaluatedValue(accumulate, a2, 2));

    // Test for exported property
    String exportedCode = "class A {\n" +
        "  constructor() {\n" +
        "  }\n" +
        "  __exports__ = {\n" +
        "    accumulate: function() {}\n" +
        "  };\n" +
        "  prototype.accumulate = function(value) {\n" +
        "    this.total += value; return this.total\n" +
        "  };\n" +
        "}";

    root = AbstractCompilerTest.parseAndCompileToString(exportedCode);

    devirtualizer.process(null, root);

    defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);

    boolean hasDefinitionSite = false;
    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      if (defSite.getDefinition().getName().equals("accumulate")) {
        hasDefinitionSite = true;
        break;
      }
    }

    assertFalse("Definition site for exported method should not exist", hasDefinitionSite);

    // Test for rewriting method defined as part of object literals
    String objectLiteralCode = "var a = {\n" +
        "  accumulate: function(value) {\n" +
        "    this.total += value; return this.total\n" +
        "  }\n" +
        "};";

    root = AbstractCompilerTest.parseAndCompileToString(objectLiteralCode);

    devirtualizer.process(null, root);

    defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);

    hasDefinitionSite = false;
    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      if (defSite.getDefinition().getName().equals("accumulate")) {
        hasDefinitionSite = true;
        break;
      }
    }

    assertFalse("Definition site for method defined as part of object literal should not exist", hasDefinitionSite);

    // Test for accessing the property directly
    String accessPropertyDirectlyCode = "var a = {\n" +
        "  accumulate: function(value) {\n" +
        "    this.total += value; return this.total\n" +
        "  }\n" +
        "};\n" +
        "a.accumulate(2);";

    root = AbstractCompilerTest.parseAndCompileToString(accessPropertyDirectlyCode);

    devirtualizer.process(null, root);

    defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);

    hasDefinitionSite = false;
    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      if (defSite.getDefinition().getName().equals("accumulate")) {
        hasDefinitionSite = true;
        break;
      }
    }

    assertFalse("Definition site for method called directly should not exist", hasDefinitionSite);

    // Test for accessing property before definition
    String accessPropertyBeforeDefinitionCode = "var a = {};\n" +
        "a.accumulate = function(value) {\n" +
        "  this.total += value; return this.total\n" +
        "};\n" +
        "a.accumulate(2);";

    root = AbstractCompilerTest.parseAndCompileToString(accessPropertyBeforeDefinitionCode);

    devirtualizer.process(null, root);

    defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);

    hasDefinitionSite = false;
    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      if (defSite.getDefinition().getName().equals("accumulate")) {
        hasDefinitionSite = true;
        break;
      }
    }

    assertFalse("Definition site for method called before definition should not exist", hasDefinitionSite);

    // Test for accessing property in a module loaded before the definition
    String accessPropertyInModuleLoadedBeforeDefinitionCode = "class A {\n" +
        "}\n" +
        "class B {\n" +
        "  constructor() {\n" +
        "    this.accumulate = function(value) {\n" +
        "      this.total += value; return this.total\n" +
        "    };\n" +
        "  }\n" +
        "}";

    root = AbstractCompilerTest.parseAndCompileToString(accessPropertyInModuleLoadedBeforeDefinitionCode);

    devirtualizer.process(null, root);

    defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);

    hasDefinitionSite = false;
    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      if (defSite.getDefinition().getName().equals("accumulate")) {
        hasDefinitionSite = true;
        break;
      }
    }

    assertFalse("Definition site for method called in a module loaded before definition should not exist", hasDefinitionSite);
  }
}