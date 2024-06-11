import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Nodeops;
import org.junit.Test;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReducer() {
    Compiler compiler = new Compiler();
    Node root = compiler.parseJsSource("function F() {}");
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(compiler.getDefaultExterns(), root);

    Node emptyFunction = root.lookupFunction("F", null);
    Node emptyFunctionHelper = emptyFunction.getFirstChild();
    assertNotNull(emptyFunctionHelper);

    Node emptyFunctionCode = emptyFunctionHelper.getNext();
    Node emptyFunctionCall = emptyFunction.getLastChild();

    assertTrue(Nodeops.isCall(emptyFunctionCall));
    assertEquals(emptyFunctionHelper.getString(), emptyFunctionCall.getString());
    assertEquals(emptyFunctionCode.getString(), emptyFunctionHelper.getString());
  }

  @Test
  public void testIdentityFunctionReducer() {
    Compiler compiler = new Compiler();
    Node root = compiler.parseJsSource("function F(x) { return x; }");
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(compiler.getDefaultExterns(), root);

    Node identityFunction = root.lookupFunction("F", null);
    Node identityFunctionHelper = identityFunction.getFirstChild();
    assertNotNull(identityFunctionHelper);

    Node identityFunctionCode = identityFunctionHelper.getNext();
    Node identityFunctionCall = identityFunction.getLastChild();

    assertTrue(Nodeops.isCall(identityFunctionCall));
    assertEquals(identityFunctionHelper.getString(), identityFunctionCall.getString());
    assertEquals(identityFunctionCode.getString(), identityFunctionHelper.getString());
  }

  @Test
  public void testReturnConstantReducer() {
    Compiler compiler = new Compiler();
    Node root = compiler.parseJsSource("function F() { return 10; }");
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(compiler.getDefaultExterns(), root);

    Node returnConstantFunction = root.lookupFunction("F", null);
    Node returnConstantFunctionHelper = returnConstantFunction.getFirstChild();
    assertNotNull(returnConstantFunctionHelper);

    Node returnConstantFunctionCode = returnConstantFunctionHelper.getNext();
    Node returnConstantFunctionCall = returnConstantFunction.getLastChild();

    assertTrue(Nodeops.isCall(returnConstantFunctionCall));
    assertEquals(returnConstantFunctionHelper.getString(), returnConstantFunctionCall.getString());
    assertEquals(returnConstantFunctionCode.getString(), returnConstantFunctionHelper.getString());

    assertEquals(10, (int) NodeUtil.getNumberValue(returnConstantFunctionCode));
  }

  @Test
  public void testGetterReducer() {
    Compiler compiler = new Compiler();
    Node root = compiler.parseJsSource("function F() { return this.x; }");
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(compiler.getDefaultExterns(), root);

    Node getterFunction = root.lookupFunction("F", null);
    Node getterFunctionHelper = getterFunction.getFirstChild();
    assertNotNull(getterFunctionHelper);

    Node getterFunctionCode = getterFunctionHelper.getNext();
    Node getterFunctionCall = getterFunction.getLastChild();

    assertTrue(Nodeops.isCall(getterFunctionCall));
    assertEquals(getterFunctionHelper.getString(), getterFunctionCall.getString());
    assertEquals(getterFunctionCode.getString(), getterFunctionHelper.getString());
  }

  @Test
  public void testSetterReducer() {
    Compiler compiler = new Compiler();
    Node root = compiler.parseJsSource("function F(x) { this.x = x; }");
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(compiler.getDefaultExterns(), root);

    Node setterFunction = root.lookupFunction("F", null);
    Node setterFunctionHelper = setterFunction.getFirstChild();
    assertNotNull(setterFunctionHelper);

    Node setterFunctionCode = setterFunctionHelper.getNext();
    Node setterFunctionCall = setterFunction.getLastChild();

    assertTrue(Nodeops.isCall(setterFunctionCall));
    assertEquals(setterFunctionHelper.getString(), setterFunctionCall.getString());
    assertEquals(setterFunctionCode.getString(), setterFunctionHelper.getString());
  }

  @Test
  public void testNoReduction() {
    Compiler compiler = new Compiler();
    Node root = compiler.parseJsSource("function F() { return 1 + 1; }");
    FunctionRewriter rewriter = new FunctionRewriter(compiler);
    rewriter.process(compiler.getDefaultExterns(), root);

    Node noReductionFunction = root.lookupFunction("F", null);

    assertTrue(noReductionFunction.getLastChild().getType() == Token.RETURN);
    assertEquals(2, (int) NodeUtil.getNumberValue(noReductionFunction.getLastChild()));
  }
}