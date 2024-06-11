import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FunctionRewriterTest {

  private AbstractCompiler compiler;
  private FunctionRewriter functionRewriter;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    functionRewriter = new FunctionRewriter(compiler);
  }

  @Test
  public void testEmptyFunctionReducer() {
    Node root = Node.newObject(Token.NAME, "{}");
    Node emptyFunction = Node.newFunction(Token.NAME, null, null, null);

    Node emptyFunctionParent = Node.newObject(Token.NAME, "C.prototype");
    emptyFunctionParent.addChildToBack(emptyFunction);
    emptyFunctionParent.setLineNum(1);
    emptyFunctionParent.setCharPositionInLine(1);

    root.addChildToBack(emptyFunctionParent);

    when(compiler.parseSyntheticCode(anyString(), anyString())).thenReturn(emptyFunction);

    functionRewriter.process(null, root);

    Node expectedHelperCode = Node.newCall(Node.newString(Token.NAME, "JSCompiler_emptyFn"), null);
    Node expectedEmptyFunction = Node.newCall(expectedHelperCode, null);

    Node actualHelperCode = emptyFunctionParent.getFirstChild();
    Node actualEmptyFunction = emptyFunction;

    assertEquals("Incorrect helper code generated.", expectedHelperCode, actualHelperCode);
    assertEquals("Function not replaced with helper code.", expectedEmptyFunction, actualEmptyFunction);
  }

  @Test
  public void testIdentityReducer() {
    Node root = Node.newObject(Token.NAME, "{}");
    Node identityFunction = Node.newFunction(Token.NAME, null, null, null);
    identityFunction.addChildToBack(Node.newReturn(Node.newName(Token.NAME, "value")));

    Node identityFunctionParent = Node.newObject(Token.NAME, "C.prototype");
    identityFunctionParent.addChildToBack(identityFunction);
    identityFunctionParent.setLineNum(1);
    identityFunctionParent.setCharPositionInLine(1);

    root.addChildToBack(identityFunctionParent);

    when(compiler.parseSyntheticCode(anyString(), anyString())).thenReturn(identityFunction);

    functionRewriter.process(null, root);

    String helperCode = "function JSCompiler_identityFn(JSCompiler_value) {" +
                        "  return function() {return JSCompiler_value}";
    Node expectedHelperCode = Node.newCall(Node.newString(Token.NAME, "JSCompiler_identityFn"), null);

    Node actualHelperCode = identityFunctionParent.getFirstChild();
    assertEquals("Incorrect helper code generated.", expectedHelperCode, actualHelperCode);

    Node expectedIdentityFunction = Node.newCall(expectedHelperCode, Node.newName(Token.NAME, "value"));
    Node actualIdentityFunction = identityFunction;

    assertEquals("Function not replaced with helper code.", expectedIdentityFunction, actualIdentityFunction);
  }

  @Test
  public void testReturnConstantReducer() {
    Node root = Node.newObject(Token.NAME, "{}");
    Node constantFunction = Node.newFunction(Token.NAME, null, null, Node.newNumber(10));

    Node constantFunctionParent = Node.newObject(Token.NAME, "C.prototype");
    constantFunctionParent.addChildToBack(constantFunction);
    constantFunctionParent.setLineNum(1);
    constantFunctionParent.setCharPositionInLine(1);

    root.addChildToBack(constantFunctionParent);

    when(compiler.parseSyntheticCode(anyString(), anyString())).thenReturn(Node.newNumber(10));

    functionRewriter.process(null, root);

    String helperCode = "function JSCompiler_returnArg(JSCompiler_value) {" +
                        "  return function() {return JSCompiler_value}";
    Node expectedHelperCode = Node.newCall(Node.newString(Token.NAME, "JSCompiler_returnArg"), null);

    Node actualHelperCode = constantFunctionParent.getFirstChild();
    assertEquals("Incorrect helper code generated.", expectedHelperCode, actualHelperCode);

    Node expectedConstantFunction = Node.newCall(expectedHelperCode, Node.newNumber(10));
    Node actualConstantFunction = constantFunction;

    assertEquals("Function not replaced with helper code.", expectedConstantFunction, actualConstantFunction);
  }

  @Test
  public void testGetterReducer() {
    Node root = Node.newObject(Token.NAME, "{}");

    Node getterFunction = Node.newFunction(Token.NAME, null, null, null);
    getterFunction.addChildToBack(Node.newReturn(Node.newGetProp(Node.newThis(), Node.newString(Token.NAME, "b_"))));

    Node getterFunctionParent = Node.newObject(Token.NAME, "C.prototype");
    getterFunctionParent.addChildToBack(getterFunction);
    getterFunctionParent.setLineNum(1);
    getterFunctionParent.setCharPositionInLine(1);

    root.addChildToBack(getterFunctionParent);

    Node expectedHelperCode = Node.newCall(Node.newString(Token.NAME, "JSCompiler_get"), Node.newString(Token.NAME, "b_"));

    functionRewriter.process(null, root);

    Node actualHelperCode = getterFunctionParent.getFirstChild();
    assertEquals("Incorrect helper code generated.", expectedHelperCode, actualHelperCode);

    Node expectedGetterFunction = getterFunction;
    Node actualGetterFunction = NodeUtil.getFunctionBody(getterFunction);
    assertEquals("Function not replaced with helper code.", expectedGetterFunction, actualGetterFunction);
  }

  @Test
  public void testSetterReducer() {
    Node root = Node.newObject(Token.NAME, "{}");

    Node setterFunction = Node.newFunction(Token.NAME, Node.newList(Node.newName(Token.NAME, "value")), null, null);
    Node assign = Node.newExprAssign(Node.newGetProp(Node.newThis(), Node.newString(Token.NAME, "b_")), Node.newName(Token.NAME, "value"));
    setterFunction.addChildToBack(assign);

    Node setterFunctionParent = Node.newObject(Token.NAME, "C.prototype");
    setterFunctionParent.addChildToBack(setterFunction);
    setterFunctionParent.setLineNum(1);
    setterFunctionParent.setCharPositionInLine(1);

    root.addChildToBack(setterFunctionParent);

    Node expectedHelperCode = Node.newCall(Node.newString(Token.NAME, "JSCompiler_set"), Node.newString(Token.NAME, "b_"));

    functionRewriter.process(null, root);

    Node actualHelperCode = setterFunctionParent.getFirstChild();
    assertEquals("Incorrect helper code generated.", expectedHelperCode, actualHelperCode);

    Node expectedSetterFunction = setterFunction;
    Node actualSetterFunction = NodeUtil.getFunctionBody(setterFunction);
    assertEquals("Function not replaced with helper code.", expectedSetterFunction, actualSetterFunction);
  }

  @Test
  public void testEdgeCases() {
    Node root = Node.newObject(Token.NAME, "{}");

    // Test empty function expression with no arguments
    Node emptyFunction = Node.newFunction(Token.NAME, null, null, null);
    Node emptyFunctionParent = Node.newObject(Token.NAME, "C.prototype");
    emptyFunctionParent.addChildToBack(emptyFunction);
    emptyFunctionParent.setLineNum(1);
    emptyFunctionParent.setCharPositionInLine(1);
    root.addChildToBack(emptyFunctionParent);

    functionRewriter.process(null, root);

    Node expectedHelperCode = Node.newCall(Node.newString(Token.NAME, "JSCompiler_emptyFn"), null);
    Node actualHelperCode = emptyFunctionParent.getFirstChild();
    assertEquals("Incorrect helper code generated.", expectedHelperCode, actualHelperCode);

    // Test identity function expression with single argument
    Node identityFunction = Node.newFunction(Token.NAME, Node.newList(Node.newName(Token.NAME, "a")), null, null);
    identityFunction.addChildToBack(Node.newReturn(Node.newName(Token.NAME, "a")));
    Node identityFunctionParent = Node.newObject(Token.NAME, "C.prototype");
    identityFunctionParent.addChildToBack(identityFunction);
    identityFunctionParent.setLineNum(1);
    identityFunctionParent.setCharPositionInLine(1);
    root.addChildToBack(identityFunctionParent);

    functionRewriter.process(null, root);

    String helperCode = "function JSCompiler_identityFn(a) {" +
                        "  return function() {return a}";
    Node expectedHelperCode = Node.newCall(Node.newString(Token.NAME, "JSCompiler_identityFn"), Node.newName(Token.NAME, "a"));

    Node actualHelperCode = identityFunctionParent.getFirstChild();
    assertEquals("Incorrect helper code generated.", expectedHelperCode, actualHelperCode);

    Node expectedIdentityFunction = Node.newCall(expectedHelperCode, Node.newName(Token.NAME, "a"));
    Node actualIdentityFunction = identityFunction;
    assertEquals("Function not replaced with helper code.", expectedIdentityFunction, actualIdentityFunction);

    // Test getter function expression with this as the first argument
    Node getterFunction = Node.newFunction(Token.NAME, null, null, null);
    getterFunction.addChildToBack(Node.newReturn(Node.newGetProp(Node.newThis(), Node.newString(Token.NAME, "b_"))));
    Node getterFunctionParent = Node.newObject(Token.NAME, "C.prototype");
    getterFunctionParent.addChildToBack(getterFunction);
    getterFunctionParent.setLineNum(1);
    getterFunctionParent.setCharPositionInLine(1);
    root.addChildToBack(getterFunctionParent);

    functionRewriter.process(null, root);

    helperCode = "function JSCompiler_get(b_) {" +
                 "  return function() {return this.b_}";
    expectedHelperCode = Node.newCall(Node.newString(Token.NAME, "JSCompiler_get"), Node.newString(Token.NAME, "b_"));

    actualHelperCode = getterFunctionParent.getFirstChild();
    assertEquals("Incorrect helper code generated.", expectedHelperCode, actualHelperCode);

    Node expectedGetterFunction = getterFunction;
    Node actualGetterFunction = NodeUtil.getFunctionBody(getterFunction);
    assertEquals("Function not replaced with helper code.", expectedGetterFunction, actualGetterFunction);

    // Test setter function expression with this as the first argument and a single argument after this
    Node setterFunction = Node.newFunction(Token.NAME, Node.newList(Node.newName(Token.NAME, "value")), null, null);
    Node assign = Node.newExprAssign(Node.newGetProp(Node.newThis(), Node.newString(Token.NAME, "b_")), Node.newName(Token.NAME, "value"));
    setterFunction.addChildToBack(assign);
    Node setterFunctionParent = Node.newObject(Token.NAME, "C.prototype");
    setterFunctionParent.addChildToBack(setterFunction);
    setterFunctionParent.setLineNum(1);
    setterFunctionParent.setCharPositionInLine(1);
    root.addChildToBack(setterFunctionParent);

    functionRewriter.process(null, root);

    helperCode = "function JSCompiler_set(b_) {" +
                 "  return function(JSCompiler_value) {" +
                 "this.b_ = JSCompiler_value}";
    expectedHelperCode = Node.newCall(Node.newString(Token.NAME, "JSCompiler_set"), Node.newString(Token.NAME, "b_"));

    actualHelperCode = setterFunctionParent.getFirstChild();
    assertEquals("Incorrect helper code generated.", expectedHelperCode, actualHelperCode);

    expectedSetterFunction = setterFunction;
    Node actualSetterFunction = NodeUtil.getFunctionBody(setterFunction);
    assertEquals("Function not replaced with helper code.", expectedSetterFunction, actualSetterFunction);

    // Test setter function expression with multiple arguments after this
    Node setterFunctionWithArgs = Node.newFunction(Token.NAME, Node.newList(Node.newName(Token.NAME, "arg1"), Node.newName(Token.NAME, "arg2")), null, null);
    Node assign = Node.newExprAssign(Node.newGetProp(Node.newThis(), Node.newString(Token.NAME, "b_")), Node.newList(Node.newName(Token.NAME, "arg1"), Node.newName(Token.NAME, "arg2")));
    setterFunctionWithArgs.addChildToBack(assign);
    Node setterFunctionParentWithArgs = Node.newObject(Token.NAME, "C.prototype");
    setterFunctionParentWithArgs.addChildToBack(setterFunctionWithArgs);
    setterFunctionParentWithArgs.setLineNum(1);
    setterFunctionParentWithArgs.setCharPositionInLine(1);
    root.addChildToBack(setterFunctionParentWithArgs);

    functionRewriter.process(null, root);

    helperCode = "function JSCompiler_set(b_) {" +
                 "  return function(arg1, arg2) {" +
                 "this.b_ = new Array(arg1, arg2)}";
    expectedHelperCode = Node.newCall(Node.newString(Token.NAME, "JSCompiler_set"), Node.newString(Token.NAME, "b_"));

    actualHelperCode = setterFunctionParentWithArgs.getFirstChild();
    assertEquals("Incorrect helper code generated.", expectedHelperCode, actualHelperCode);

    Node expectedSetterFunctionWithArgs = setterFunctionWithArgs;
    Node actualSetterFunctionWithArgs = NodeUtil.getFunctionBody(setterFunctionWithArgs);
    assertEquals("Function not replaced with helper code.", expectedSetterFunctionWithArgs, actualSetterFunctionWithArgs);

    // Test throwing exception when function is not reduceable
    Node notReducibleFunction = Node.newFunction(Token.NAME, null, null, Node.newBlock(Node.newExpressionStatement(Node.newCall(Node.newString(Token.NAME, "console.log"), Node.newString("Hello world")))));
    Node notReducibleFunctionParent = Node.newObject(Token.NAME, "C.prototype");
    notReducibleFunctionParent.addChildToBack(notReducibleFunction);
    notReducibleFunctionParent.setLineNum(1);
    notReducibleFunctionParent.setCharPositionInLine(1);
    root.addChildToBack(notReducibleFunctionParent);

    when(compiler.parseSyntheticCode(anyString(), anyString())).thenThrow(new RuntimeException());

    try {
      functionRewriter.process(null, root);
      fail("Expected exception to be thrown.");
    } catch (Exception e) {
      // Expected
    }
  }
}