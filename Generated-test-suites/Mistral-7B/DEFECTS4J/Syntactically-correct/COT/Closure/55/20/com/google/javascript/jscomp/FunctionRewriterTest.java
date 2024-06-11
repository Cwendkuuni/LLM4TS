import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.traversing.NodeTraversal;
import com.google.javascript.rhino.Node;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FunctionRewriterTest {

  private static TestCompiler compiler;
  private Node originalCode;
  private Node rewrittenCode;

  @Parameters(name = "{0}")
  public static List<Object[]> data() {
    return ImmutableList.of(
        new Object[]{"emptyFunctionTest", getEmptyFunctionCode()},
        new Object[]{"identityFunctionTest", getIdentityFunctionCode()},
        new Object[]{"returnConstantFunctionTest", getReturnConstantFunctionCode()},
        new Object[]{"getterFunctionTest", getGetterFunctionCode()},
        new Object[]{"setterFunctionTest", getSetterFunctionCode()}
    );
  }

  public FunctionRewriterTest(String testName, Node originalCode) {
    this.originalCode = originalCode;
  }

  @BeforeClass
  public static void setup() {
    compiler = new TestCompiler();
  }

  @Test
  public void testFunctionRewriting() {
    Node root = compiler.parse(originalCode.toString(), null, null).getRootNode();

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    rewrittenCode = root;
  }

  @Test
  public void testHelperCodeInsertion() {
    assertTrue(parseHelperCode(EmptyFunctionReducer.class) != null);
    assertTrue(parseHelperCode(IdentityReducer.class) != null);
    assertTrue(parseHelperCode(ReturnConstantReducer.class) != null);
    assertTrue(parseHelperCode(GetterReducer.class) != null);
    assertTrue(parseHelperCode(SetterReducer.class) != null);
  }

  @Test
  public void testFunctionRewriting_EmptyFunction() {
    // Empty function test case
    assertNotNull(rewrittenCode);
    assertEquals("emptyFn()", rewrittenCode.toString());
  }

  @Test
  public void testFunctionRewriting_IdentityFunction() {
    // Identity function test case
    assertNotNull(rewrittenCode);
    Node identityFunction = rewrittenCode.getFirstChild().getNext();
    assertEquals("identityFn(identity_value)", identityFunction.toString());
  }

  @Test
  public void testFunctionRewriting_ReturnConstantFunction() {
    // Return constant function test case
    assertNotNull(rewrittenCode);
    Node returnConstantFunction = rewrittenCode.getFirstChild();
    assertEquals("returnArg(10)", returnConstantFunction.toString());
  }

  @Test
  public void testFunctionRewriting_GetterFunction() {
    // Getter function test case
    assertNotNull(rewrittenCode);
    Node getterFunction = rewrittenCode.getLastChild();
    assertEquals("getter(getter_value)", getterFunction.toString());
  }

  @Test
  public void testFunctionRewriting_SetterFunction() {
    // Setter function test case
    assertNotNull(rewrittenCode);
    Node setterFunction = rewrittenCode.getLastChild();
    assertEquals("setter(setter_value)", setterFunction.toString());
  }

  private static Node getEmptyFunctionCode() {
    return new Node("function() {}");
  }

  private static Node getIdentityFunctionCode() {
    return new Node("function(value) {return value}");
  }

  private static Node getReturnConstantFunctionCode() {
    return new Node("function() {return 10}");
  }

  private static Node getGetterFunctionCode() {
    return new Node("function() {return this.a_}");
  }

  private static Node getSetterFunctionCode() {
    return new Node("function(value) {this.a_ = value}");
  }

  private static Node parseHelperCode(Class<?> reducerClass) {
    return FunctionRewriter.parseHelperCode(reducerClass);
  }
}