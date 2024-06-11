import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.DEVIRTUALIZE_PROTOTYPE_METHODS;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class DevirtualizePrototypeMethodsTest extends CompilerPassTestBase {

  private static final String SOURCE =
      "function Foo() {}\n" +
      "Foo.prototype.m = function(a, b) { return a + b; };\n" +
      "var x = new Foo();\n" +
      "var sum = x.m(1, 2);";

  private static final String EXPECTED =
      "var m = function(self, a, b) { return a + b; };\n" +
      "var x = new Foo();\n" +
      "var sum = m(x, 1, 2);";

  @Override
  protected CompilerPass getPass() {
    return DEVirtualizePrototypeMethods.class;
  }

  @Override
  protected String getSourceName() {
    return "DevirtualizePrototypeMethodsTest.js";
  }

  @Override
  protected CompilerOptions getOptions() {
    return new CompilerOptions();
  }

  @Override
  protected List<CompilerPass> getPasses() {
    return ImmutableList.<CompilerPass>builder()
        .addAll(super.getPasses())
        .add(new DEVirtualizePrototypeMethods(new NoOpCompiler()))
        .build();
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public void testDevirtualizePrototypeMethods() throws Exception {
    Node node = parse(SOURCE);

    CompilerPassManager passManager = new CompilerPassManager(getOptions());
    passManager.addPass(getPass());
    Node newNode = new NodeTraversal() {
      @Override
      public void visit(NodeTraversal t, Node node) {
        if (t.isAtRoot()) {
          assertEquals(EXPECTED, toString(node));
        }
        super.visit(t, node);
      }
    }.process(node, passManager);

    assertEquals(EXPECTED, toString(newNode));
  }
}