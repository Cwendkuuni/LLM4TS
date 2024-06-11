import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;

import java.util.Collection;
import java.util.List;

public class FunctionRewriterTest {

  @org.junit.Before
  public void setUp() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    FunctionRewriter functionRewriter = new FunctionRewriter(compilerMock);
  }

  

  @org.junit.Test
  public void testProcess_EmptyProgram_NoReductions() {
    Node root = Node.empty();
    FunctionRewriter functionRewriter = new FunctionRewriter(mock(AbstractCompiler.class));
    functionRewriter.process(Node.EMPTY, root);
    assertNull(functionRewriter.parseHelperCode(FunctionRewriter.ReturnConstantReducer.class));
  }

  @org.junit.Test
  public void testProcess_SimpleFunction_NoReductions() {
    Node root = Node.newObject("f", Node.newArray(), Node.empty(),
        Node.newFunction(Node.empty(), Node.empty(), Node.empty()));

    FunctionRewriter functionRewriter = new FunctionRewriter(mock(AbstractCompiler.class));
    functionRewriter.process(Node.EMPTY, root);
    assertNull(functionRewriter.parseHelperCode(FunctionRewriter.ReturnConstantReducer.class));
  }

  @org.junit.Test
  public void testProcess_IdentityFunction_ReductionApplied() {
    Node root = Node.newObject("a", Node.newArray(), Node.empty(),
        Node.newFunction(Node.newArray(Node.newName("a")), Node.empty(),
            Node.newBlock(Node.newReturn(Node.newName("a")))));

    FunctionRewriter functionRewriter = new FunctionRewriter(mock(AbstractCompiler.class));
    functionRewriter.process(Node.EMPTY, root);
    Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.IdentityReducer.class);
    assertNotNull(helperCode);
  }

  @org.junit.Test
  public void testProcess_GetterFunction_ReductionApplied() {
    Node root = Node.newObject("a", Node.newArray(), Node.empty(),
        Node.newFunction(Node.empty(), Node.empty(),
            Node.newBlock(Node.newGetProp(Node.newThis()),
                Node.newReturn(Node.newName("b")))));

    FunctionRewriter functionRewriter = new FunctionRewriter(mock(AbstractCompiler.class));
    functionRewriter.process(Node.EMPTY, root);
    Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.GetterReducer.class);
    assertNotNull(helperCode);
  }

  @org.junit.Test
  public void testProcess_SetterFunction_ReductionApplied() {
    Node root = Node.newObject("a", Node.newArray(), Node.empty(),
        Node.newFunction(Node.newArray(Node.newName("value")), Node.empty(),
            Node.newBlock(Node.newSetProp(Node.newThis(), Node.newName("b")),
                Node.newReturn(Node.newName("value")))));

    FunctionRewriter functionRewriter = new FunctionRewriter(mock(AbstractCompiler.class));
    functionRewriter.process(Node.EMPTY, root);
    Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.SetterReducer.class);
    assertNotNull(helperCode);
  }

  @org.junit.Test
  public void testProcess_EmptyFunction_ReductionApplied() {
    Node root = Node.newObject("a", Node.newArray(), Node.empty(),
        Node.newFunction(Node.empty(), Node.empty(), Node.empty()));

    FunctionRewriter functionRewriter = new FunctionRewriter(mock(AbstractCompiler.class));
    functionRewriter.process(Node.EMPTY, root);
    Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.EmptyFunctionReducer.class);
    assertNotNull(helperCode);
  }

  @org.junit.Test
  public void testProcess_FunctionWithMultipleStatements_NoReduction() {
    Node root = Node.newObject("a", Node.newArray(), Node.empty(),
        Node.newFunction(Node.empty(), Node.empty(),
            Node.newBlock(Node.newReturn(Node.newNumber(1)),
                Node.newSetProp(Node.newThis(), Node.newName("b"),
                    Node.newNumber(2)))));

    FunctionRewriter functionRewriter = new FunctionRewriter(mock(AbstractCompiler.class));
    functionRewriter.process(Node.EMPTY, root);
    assertNull(functionRewriter.parseHelperCode(FunctionRewriter.IdentityReducer.class));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testProcess_FunctionWithInvalidSignature_ExceptionThrown() {
    Node root = Node.newObject("a", Node.newArray(), Node.empty(),
        Node.newFunction(Node.newArray(), Node.newArray(), Node.empty()));

    FunctionRewriter functionRewriter = new FunctionRewriter(mock(AbstractCompiler.class));
    functionRewriter.process(Node.EMPTY, root);
  }

  @org.junit.Test
  public void testParseHelperCode_ReturnConstantReducer_Success() {
    String source = "function ReturnConstantReducer() {}";
    FunctionRewriter functionRewriter = new FunctionRewriter(mock(AbstractCompiler.class));
    Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.ReturnConstantReducer.class);
    assertNotNull(helperCode);
  }

  @org.junit.Test
  public void testParseHelperCode_IdentityReducer_Success() {
    String source = "function IdentityReducer() {" +
        "  function identityFn(value) {" +
        "    return value;" +
        "  }" +
        "  return identityFn;" +
        "}";
    FunctionRewriter functionRewriter = new FunctionRewriter(mock(AbstractCompiler.class));
    Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.IdentityReducer.class);
    assertNotNull(helperCode);
  }

  @org.junit.Test
  public void testParseHelperCode_GetterReducer_Success() {
    String source = "function GetterReducer() {" +
        "  function getter(name) {" +
        "    return function() {" +
        "      return this[name]" +
        "    }" +
        "  }" +
        "  return getter;" +
        "}";
    FunctionRewriter functionRewriter = new FunctionRewriter(mock(AbstractCompiler.class));
    Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.GetterReducer.class);
    assertNotNull(helperCode);
  }

  @org.junit.Test
  public void testParseHelperCode_SetterReducer_Success() {
    String source = "function SetterReducer() {" +
        "  function setter(name) {" +
        "    return function(value) {" +
        "      this[name] = value;" +
        "    }" +
        "  }" +
        "  return setter;" +
        "}";
    FunctionRewriter functionRewriter = new FunctionRewriter(mock(AbstractCompiler.class));
    Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.SetterReducer.class);
    assertNotNull(helperCode);
  }

  @org.junit.Test
  public void testParseHelperCode_EmptyFunctionReducer_Success() {
    String source = "function EmptyFunctionReducer() {" +
        "  function emptyFn() {" +
        "    return function() {}" +
        "  }" +
        "  return emptyFn;" +
        "}";
    FunctionRewriter functionRewriter = new FunctionRewriter(mock(AbstractCompiler.class));
    Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.EmptyFunctionReducer.class);
    assertNotNull(helperCode);
  }

  
}