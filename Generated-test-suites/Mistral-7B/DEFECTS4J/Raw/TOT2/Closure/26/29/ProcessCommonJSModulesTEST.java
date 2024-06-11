import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.SuffixVarsCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal;

import java.io.File;
import java.io.IOException;
import java.net.URISyndexException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProcessCommonJSModulesTest {

  private static CompilerInput compile(String input, String filename) {
    CompilerOptions options = CompilerOptions.defaultOptions();
    CompilerInput ci = new CompilerInput(input, new File(filename), options);
    AbstractCompiler compiler = new AbstractCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filename);
    pass.process(ci.getExterns(), ci.getRootNode());
    return ci;
  }

  @org.junit.Test
  public void testNormalScenario() {
    String input =
        "function main() {" +
            "require('module1');" +
            "require('module2');" +
            "goog.provide('myModule');" +
            "myModule.exports = {'foo': 'bar'};" +
            "}";

    String filename = "TestInput.js";
    CompilerInput ci = compile(input, filename);
    Node root = ci.getRootNode();
    NodeTraversal.traverse(root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() > 0 &&
            n.getChildAtIndex(0).isName() &&
            Objects.equals(n.getChildAtIndex(0).getString(), "require")) {
          assertEquals("module1", n.getChildAtIndex(1).getString());
          assertEquals("module2", n.getChildAtIndex(2).getString());
        }
      }
    });
  }

  @org.junit.Test
  public void testRequireRelativePath() {
    String input =
        "function main() {" +
            "require('./module1');" +
            "require('../module2');" +
            "goog.provide('myModule');" +
            "myModule.exports = {'foo': 'bar'};" +
            "}";

    String filename = "TestInput.js";
    CompilerInput ci = compile(input, filename);
    Node root = ci.getRootNode();
    NodeTraversal.traverse(root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() > 0 &&
            n.getChildAtIndex(0).isName() &&
            Objects.equals(n.getChildAtIndex(0).getString(), "require")) {
          try {
            String expectedModuleName =
                toModuleName("TestInput.js", "module1.js");
            assertEquals(expectedModuleName, n.getChildAtIndex(1).getString());
          } catch (URISyntaxException e) {
            throw new RuntimeException(e);
          }
          try {
            String expectedModuleName =
                toModuleName("TestInput.js", "../module2.js");
            assertEquals(expectedModuleName, n.getChildAtIndex(1).getString());
          } catch (URISyntaxException e) {
            throw new RuntimeException(e);
          }
        }
      }
    });
  }

  @org.junit.Test
  public void testRequireAbsolutePath() {
    String input =
        "function main() {" +
            "require('module1.js');" +
            "require('../module2.js');" +
            "goog.provide('myModule');" +
            "myModule.exports = {'foo': 'bar'};" +
            "}";

    String filename = "TestInput.js";
    CompilerInput ci = compile(input, filename);
    Node root = ci.getRootNode();
    NodeTraversal.traverse(root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() > 0 &&
            n.getChildAtIndex(0).isName() &&
            Objects.equals(n.getChildAtIndex(0).getString(), "require")) {
          try {
            String expectedModuleName =
                toModuleName("TestInput.js", "module1.js");
            assertEquals(expectedModuleName, n.getChildAtIndex(1).getString());
          } catch (URISyntaxException e) {
            throw new RuntimeException(e);
          }
          try {
            String expectedModuleName =
                toModuleName("TestInput.js", "../module2.js");
            assertEquals(expectedModuleName, n.getChildAtIndex(1).getString());
          } catch (URISyntaxException e) {
            throw new RuntimeException(e);
          }
        }
      }

      private String toModuleName(String currentFilename,
          String requiredFilename) throws URISyntaxException {
        return ProcessCommonJSModules.toModuleName(requiredFilename,
            currentFilename);
      }
    });
  }

  @org.junit.Test
  public void testRequireNonExistingModule() {
    String input =
        "function main() {" +
            "require('nonExistingModule');" +
            "goog.provide('myModule');" +
            "myModule.exports = {'foo': 'bar'};" +
            "}";

    String filename = "TestInput.js";
    CompilerInput ci = compile(input, filename);
    Node root = ci.getRootNode();
    NodeTraversal.traverse(root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() > 0 &&
            n.getChildAtIndex(0).isName() &&
            Objects.equals(n.getChildAtIndex(0).getString(), "require")) {
          assertNull(n.getChildAtIndex(1));
        }
      }
    });
  }

  @org.junit.Test
  public void testModuleExports() {
    String input =
        "function main() {" +
            "require('module1').exports.foo = 'bar';" +
            "goog.provide('myModule');" +
            "myModule.exports = {'foo': 'baz'};" +
            "}";

    String filename = "TestInput.js";
    CompilerInput ci = compile(input, filename);
    Node root = ci.getRootNode();
    NodeTraversal.traverse(root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isAssign()) {
          if (n.getChildAtIndex(0).isGetProp() &&
              Objects.equals(n.getChildAtIndex(0).getQualifiedName(),
                  "require('module1').exports")) {
            assertEquals("bar", n.getChildAtIndex(1).getString());
          }
        }
      }
    });
  }

  @org.junit.Test
  public void testModuleExportsWithSuffix() {
    String input =
        "function main() {" +
            "require('module1').exports.foo$bar = 'baz';" +
            "goog.provide('myModule');" +
            "myModule.exports = {'foo': 'qux'};" +
            "}";

    String filename = "TestInput.js";
    CompilerInput ci = compile(input, filename);
    Node root = ci.getRootNode();
    NodeTraversal.traverse(root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isAssign()) {
          if (n.getChildAtIndex(0).isGetProp() &&
              Objects.equals(n.getChildAtIndex(0).getQualifiedName(),
                  "require('module1').exports")) {
            assertEquals("baz", n.getChildAtIndex(1).getString());
          }
        }
      }
    });
  }

  @org.junit.Test
  public void testReportDependencies() {
    String input =
        "function main() {" +
            "require('module1');" +
            "require('module2');" +
            "goog.provide('myModule');" +
            "myModule.exports = {'foo': 'bar'};" +
            "}";

    String filename = "TestInput.js";
    CompilerInput ci = compile(input, filename, false, true);
    List<String> requires = ci.getRequireList();
    assertEquals(2, requires.size());
    assertEquals("module1", requires.get(0));
    assertEquals("module2", requires.get(1));
  }

  @org.junit.Test
  public void testNoProvideStatement() {
    String input =
        "function main() {" +
            "require('module1').exports.foo = 'bar';" +
            "require('module2').exports.foo = 'baz';" +
            "}";

    String filename = "TestInput.js";
    CompilerInput ci = compile(input, filename, false, true);
    Node root = ci.getRootNode();
    assertThrows(IllegalArgumentException.class, () -> {
      JSModule module = ci.getModule();
      String moduleName = module.getName();
      assertEquals(0, module.getProvidedNames().size());
    });
  }

  @org.junit.Test
  public void testRenameGlobalVariables() {
    String input =
        "var foo = 'bar';" +
        "function main() {" +
            "require('module1');" +
            "goog.provide('myModule');" +
            "myModule.exports = {'foo': 'baz'};" +
            "}";

    String filename = "TestInput.js";
    CompilerInput ci = compile(input, filename);
    Node root = ci.getRootNode();
    NodeTraversal.traverse(root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isName()) {
          if (n.getString().equals("foo")) {
            assertEquals("foo$$bar", n.getQualifiedName());
          }
        }
      }
    });
  }

  @org.junit.Test
  public void testRenameModuleExports() {
    String input =
        "var module1Foo = 'bar';" +
        "function main() {" +
            "require('module1').exports.foo = 'baz';" +
            "goog.provide('myModule');" +
            "myModule.exports = {'foo': 'qux'};" +
            "}";

    String filename = "TestInput.js";
    CompilerInput ci = compile(input, filename);
    Node root = ci.getRootNode();
    NodeTraversal.traverse(root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isName()) {
          if (n.getString().equals("module1Foo")) {
            assertEquals("module1$$module$exports$$foo", n.getQualifiedName());
          }
        }
      }
    });
  }

  @org.junit.Test
  public void testSuffixVarsCallback() {
    String input =
        "var foo = 'bar';" +
        "function main() {" +
            "goog.provide('myModule');" +
            "myModule.exports = {'foo': 'baz'};" +
        "}";

    String filename = "TestInput.js";
    CompilerInput ci = compile(input, filename);
    Node root = ci.getRootNode();
    NodeTraversal.traverse(root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isName()) {
          if (n.getString().equals("foo")) {
            assertEquals("foo$$bar", n.getQualifiedName());
          }
        }
      }
    });
  }

  @org.junit.Test
  public void testConcatenatedModules() {
    String input1 =
        "var foo = 'bar';" +
        "goog.provide('module1');" +
        "module1.exports = {'foo': 'baz'};";

    String input2 =
        "function main() {" +
            "require('module1').exports.foo = 'qux';" +
            "goog.provide('myModule');" +
            "myModule.exports = {'foo': 'quux'};" +
        "}";

    String filename = "TestInput.js";
    CompilerInput ci1 = compile(input1, "module1.js");
    CompilerInput ci2 = compile(input2, filename, false, true);
    Node root = ci2.getRootNode();

    JSModule module = ci2.getModule();
    assertEquals("myModule", module.getName());
    assertEquals(1, module.getProvidedNames().size());
    assertEquals("foo", module.getProvidedNames().get(0));
    assertEquals("quux", module.getExports().get("foo"));

    NodeTraversal.traverse(root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isName()) {
          if (n.getString().equals("foo")) {
            assertEquals("myModule$$foo", n.getQualifiedName());
            assertEquals("quux", n.getChildAtIndex(0).getString());
          }
        }
      }
    });
  }

  @org.junit.Test
  public void testProvideStatementInMultipleFiles() {
    String input1 =
        "goog.provide('module1');" +
        "module1.exports = {'foo': 'bar'};";

    String input2 =
        "goog.provide('module2');" +
        "module2.exports = {'foo': 'baz'};";

    String filename = "TestInput.js";
    CompilerInput ci1 = compile(input1, "module1.js");
    CompilerInput ci2 = compile(input2, filename, false, true);
    JSModule module1 = ci1.getModule();
    JSModule module2 = ci2.getModule();
    assertEquals(1, module1.getProvidedNames().size());
    assertEquals("module1", module1.getName());
    assertEquals("module2", module2.getName());
    assertEquals("bar", module1.getExports().get("foo"));
    assertEquals("baz", module2.getExports().get("foo"));
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithEmptyInput() {
    String input = "";
    String filename = "TestInput.js";
    CompilerInput ci = compile(input, filename);
    Node root = ci.getRootNode();
    assertNull(root);
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithInvalidInput() {
    String input = "invalidInput";
    String filename = "TestInput.js";
    CompilerInput ci = compile(input, filename);
    assertThrows(IllegalArgumentException.class, () -> {
      Node root = ci.getRootNode();
    });
  }
}