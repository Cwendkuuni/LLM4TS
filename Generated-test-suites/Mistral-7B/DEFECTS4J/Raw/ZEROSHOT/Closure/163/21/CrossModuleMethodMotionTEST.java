import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilationTest.*;
import static com.google.javascript.jscomp.CompilerOptions.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.analyzer.AnalyzePrototypeProperties;
import com.google.javascript.rhino.SourceFile;

import org.junit.Test;

public class CrossModuleMethodMotionTest {

  @Test
  public void testCrossModuleMethodMotion() throws Exception {
    CompilerOptions options = newCompilerOptions();
    options.setAllowCircularDependencies(true);
    options.setModuleMode(ModuleMode.ES6);
    options.setWarningLevel(WarningLevel.QUIET);

    SourceFile source = newSourceFile("test.js", "class A { m() {} }", null);
    SourceFile source2 = newSourceFile("test2.js", "class B extends A { }", null);

    Compiler compiler = newCompilerWithSources(options, Lists.newArrayList(
        source, source2));
    CompilerPass crossModuleMethodMotion = new CrossModuleMethodMotion(
        compiler, new CrossModuleMethodMotion.IdGenerator(), true);

    compiler.addPass(crossModuleMethodMotion);
    compile(compiler);

    Node root = compiler.getRoot();
    Node aPrototype = getTopLevelNode(root, "A").prototype;
    Node bPrototype = getTopLevelNode(root, "B").prototype;

    assertNotNull(aPrototype.getChild(0));
    assertNotNull(bPrototype.getChild(0));

    Node aMethod = aPrototype.getChild(0);
    Node bMethod = bPrototype.getChild(0);

    assertEquals(aMethod.toString(), bMethod.toString());
  }

  private static Node getTopLevelNode(Node node, String name) {
    if (node.isScope()) {
      return getTopLevelNode(node.getChild(0), name);
    } else if (node.isName() && node.getName().equals(name)) {
      return node;
    }
    return null;
  }
}

class CrossModuleMethodMotionTest {
  // The test code should not include any copyright notice.
}