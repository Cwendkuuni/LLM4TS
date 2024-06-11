import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.IR;

public class CrossModuleMethodMotionTest {

  @Test
  public void testMoveMethodAcrossModules() {
    // Set up compiler options
    CompilerOptions options = new CompilerOptions();
    options.setOutputFile("output.js");
    options.setWarningsAsErrors(false);

    // Create compiler
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    // Prepare input code
    String sourceCode =
        "// Module A\n" +
        "function A() {\n" +
        "  this.methodA = function() {\n" +
        "    return 'A.methodA';\n" +
        "  };\n" +
        "}\n" +

        "// Module B\n" +
        "function B() {\n" +
        "}\n" +
        "B.prototype.methodB = function() {\n" +
        "  return 'B.methodB';\n" +
        "}\n";

    // Compile input code
    Node externs = IR.topLevel(IR.programNode(
        IR.parseCompilationUnit(sourceCode, null, null)));
    Node root = compiler.compile(externs, null, null);

    // Run CrossModuleMethodMotion pass
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    pass.process(externs, root);
    compiler.reportCodeChange();

    // Check output code
    Node output = compiler.getCompiledResult();
    Node A = output.getChild(0);
    Node B = output.getChild(1);
    Node AMethodA = A.getChild(0);
    Node BMethodB = B.getChild(0);

    // Check that methodA is stubbed in module A and unstubbed in module B
    assertEquals("function JSCompiler_stubMethod(JSCompiler_stubMethod_id) {\n" +
        "  return function() {\n" +
        "    return JSCompiler_stubMap[JSCompiler_stubMethod_id].apply(\n" +
        "        this, arguments);\n" +
        "  };\n" +
        "}", AMethodA.getTextContent().trim());
    assertNotNull(output.getChild(0).getChild(0).getChild(0).getChild(0)
        .getChildByName("JSCompiler_stubMap"));

    assertEquals("function A() {\n" +
        "  this.methodA = JSCompiler_unstubMethod(JSCompiler_stubMethod_id_0,\n" +
        "      function() {\n" +
        "        return 'A.methodA';\n" +
        "      });\n" +
        "}", A.getTextContent().trim());

    assertEquals("function B() {\n" +
        "  this.methodB = function() {\n" +
        "    return 'B.methodB';\n" +
        "  };\n" +
        "}", B.getTextContent().trim());

    assertEquals("function B() {\n" +
        "  this.methodB = function() {\n" +
        "    return 'B.methodB';\n" +
        "  };\n" +
        "}\n" +
        "B.prototype.methodB = JSCompiler_unstubMethod(JSCompiler_stubMethod_id_1,\n" +
        "    function() {\n" +
        "      return 'B.methodB';\n" +
        "    });\n",
        BMethodB.getTextContent().trim());

    // Check that methodA can be called from module B
    Node callAFromB = IR.call(BMethodB, IR.getThis());
    assertEquals("'B.methodB'", callAFromB.getTextContent().trim());

    // Check that methodA can still be called from module A
    Node callAFromA = IR.call(AMethodA, IR.getThis());
    assertEquals("'A.methodA'", callAFromA.getTextContent().trim());
  }
}