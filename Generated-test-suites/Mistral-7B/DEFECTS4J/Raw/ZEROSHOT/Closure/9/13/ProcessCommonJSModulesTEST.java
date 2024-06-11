import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traverse;
import com.google.javascript.rhino.NodeFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static CompilerInput createTestInput(String filename) {
    try {
      CompilerOptions options = new CompilerOptions();
      CompilerInput input = new CompilerInput(filename, new URI("file:/test"), null);
      input.setFilename(filename);
      input.setOptions(options);
      return input;
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private static Node createTestNode(String code) {
    return NodeFactory.compile(code).getRootNode();
  }

  @org.junit.Test
  public void testSimple() {
    AbstractCompiler compiler = new Compiler();
    String inputCode = "// Input code\n" +
        "require('./module1');\n" +
        "function myFunction() {}";
    CompilerInput input = createTestInput("test.js");
    input.setSourceContent(0, createTestNode(inputCode).toString());

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input, createTestNode(""));

    Traverse.traverse(compiler, createTestNode(""), new Traverse.AbstractVisitor() {
      @Override
      public void visitCall(NodeTraversal t, Node call) {
        if ("require".equals(call.getFirstChild().getQualifiedName())) {
          Node moduleRef = call.getChildAtIndex(1);
          Node expectedModuleRef = IR.name("module1$");
          assertEquals("Expected require call to be replaced",
              expectedModuleRef, moduleRef);
        }
      }
    });
  }

  @org.junit.Test
  public void testModuleExports() {
    AbstractCompiler compiler = new Compiler();
    String inputCode = "// Input code\n" +
        "var myModule = {\n" +
        "  exports: function() {\n" +
        "    return this;\n" +
        "  },\n" +
        "  module: {\n" +
        "    exports: 'myModule'\n" +
        "  }\n" +
        "};\n" +
        "require('./module1').exports.myFunction();\n" +
        "function myFunction() {}";
    CompilerInput input = createTestInput("test.js");
    input.setSourceContent(0, createTestNode(inputCode).toString());

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input, createTestNode(""));

    Traverse.traverse(compiler, createTestNode(""), new Traverse.AbstractVisitor() {
      @Override
      public void visitCall(NodeTraversal t, Node call) {
        if ("require".equals(call.getFirstChild().getQualifiedName())) {
          Node moduleRef = call.getChildAtIndex(1);
          Node expectedModuleRef = IR.name("myModule$");
          assertEquals("Expected require call to be replaced",
              expectedModuleRef, moduleRef);
          Node expectedModuleExports = IR.name("myModule$exports");
          Node actualModuleExports = t.getParent().getParent().getChildAtIndex(1);
          assertEquals("Expected exports to be renamed",
              expectedModuleExports, actualModuleExports);
        }
      }
    });
  }

  @org.junit.Test
  public void testRelativeRequire() {
    AbstractCompiler compiler = new Compiler();
    String inputCode = "// Input code\n" +
        "require('./subfolder/module1');\n" +
        "function myFunction() {}";
    CompilerInput input = createTestInput("test.js");
    input.setSourceContent(0, createTestNode(inputCode).toString());

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input, createTestNode(""));

    Traverse.traverse(compiler, createTestNode(""), new Traverse.AbstractVisitor() {
      @Override
      public void visitCall(NodeTraversal t, Node call) {
        if ("require".equals(call.getFirstChild().getQualifiedName())) {
          Node moduleRef = call.getChildAtIndex(1);
          Node expectedModuleRef = IR.name("subfolder$module1$");
          assertEquals("Expected require call to be replaced",
              expectedModuleRef, moduleRef);
        }
      }
    });
  }

  @org.junit.Test
  public void testMultipleModules() {
    AbstractCompiler compiler = new Compiler();
    String inputCode = "// Input code\n" +
        "require('./module1');\n" +
        "require('./module2');\n" +
        "function myFunction() {}";
    CompilerInput input = createTestInput("test.js");
    input.setSourceContent(0, createTestNode(inputCode).toString());

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input, createTestNode(""));

    Traverse.traverse(compiler, createTestNode(""), new Traverse.AbstractVisitor() {
      @Override
      public void visitCall(NodeTraversal t, Node call) {
        if ("require".equals(call.getFirstChild().getQualifiedName())) {
          Node moduleRef = call.getChildAtIndex(1);
          int index = Array.asList(IR.name("module1$"), IR.name("module2$"))
              .indexOf(moduleRef);
          assertEquals("Expected require calls to be replaced in the correct order",
              index, call.getIndex());
        }
      }
    });
  }

  @org.junit.Test
  public void testGlobalVariables() {
    AbstractCompiler compiler = new Compiler();
    String inputCode = "// Input code\n" +
        "var myVariable = 'foo';\n" +
        "require('./module1');\n" +
        "var myVariable = 'bar';\n" +
        "function myFunction() {}";
    CompilerInput input = createTestInput("test.js");
    input.setSourceContent(0, createTestNode(inputCode).toString());

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input, createTestNode(""));

    Traverse.traverse(compiler, createTestNode(""), new Traverse.AbstractVisitor() {
      @Override
      public void visitName(NodeTraversal t, Node name) {
        if ("myVariable".equals(name.getString())) {
          assertEquals("Expected myVariable to be renamed",
              "myVariable$$myVariable", name.getString());
        }
      }
    });
  }

  @org.junit.Test
  public void testModuleWithExports() {
    AbstractCompiler compiler = new Compiler();
    String inputCode = "// Input code\n" +
        "exports.myFunction = function() {};\n" +
        "exports.module = {\n" +
        "  exports: 'exports'\n" +
        "};\n" +
        "require('./module1');\n" +
        "function myFunction() {}";
    CompilerInput input = createTestInput("test.js");
    input.setSourceContent(0, createTestNode(inputCode).toString());

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input, createTestNode(""));

    Traverse.traverse(compiler, createTestNode(""), new Traverse.AbstractVisitor() {
      @Override
      public void visitCall(NodeTraversal t, Node call) {
        if ("require".equals(call.getFirstChild().getQualifiedName())) {
          Node moduleRef = call.getChildAtIndex(1);
          Node expectedModuleRef = IR.name("module1$");
          assertEquals("Expected require call to be replaced",
              expectedModuleRef, moduleRef);
        }
      }

      @Override
      public void visitName(NodeTraversal t, Node name) {
        if ("myFunction".equals(name.getString())) {
          Node expectedFunctionRef = IR.name("exports$myFunction");
          Node actualFunctionRef = t.getParent().getParent().getChildAtIndex(1);
          assertEquals("Expected myFunction to be renamed",
              expectedFunctionRef, actualFunctionRef);
        }
      }
    });
  }

  @org.junit.Test
  public void testNoModule() {
    AbstractCompiler compiler = new Compiler();
    String inputCode = "// Input code\n" +
        "function myFunction() {}";
    CompilerInput input = createTestInput("test.js");
    input.setSourceContent(0, createTestNode(inputCode).toString());

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input, createTestNode(""));

    Traverse.traverse(compiler, createTestNode(""), new Traverse.AbstractVisitor() {
      // No changes expected
    });
  }

  @org.junit.Test
  public void testReportingDependencies() {
    AbstractCompiler compiler = new Compiler();
    String inputCode = "// Input code\n" +
        "require('./module1');\n" +
        "function myFunction() {}";
    CompilerInput input = createTestInput("test.js");
    input.setSourceContent(0, createTestNode(inputCode).toString());

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "test");
    processor.process(input, createTestNode(""));

    assertEquals("Expected single dependency to be reported", 1,
        compiler.getInput().getRequires().size());
    assertEquals("Expected dependency to be reported correctly",
        "module1", compiler.getInput().getRequires().get(0));
  }

  @org.junit.Test
  public void testMultipleReportingDependencies() {
    AbstractCompiler compiler = new Compiler();
    String inputCode = "// Input code\n" +
        "require('./module1');\n" +
        "require('./module2');\n" +
        "function myFunction() {}";
    CompilerInput input = createTestInput("test.js");
    input.setSourceContent(0, createTestNode(inputCode).toString());

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "test");
    processor.process(input, createTestNode(""));

    assertEquals("Expected two dependencies to be reported", 2,
        compiler.getInput().getRequires().size());
    assertTrue("Expected dependencies to be reported in the correct order",
        Arrays.asList(
            "module1", "module2").equals(Arrays.asList(
            compiler.getInput().getRequires().get(0),
            compiler.getInput().getRequires().get(1))));
  }
}