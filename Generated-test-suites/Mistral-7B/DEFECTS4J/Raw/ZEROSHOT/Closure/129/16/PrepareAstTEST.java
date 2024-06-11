import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

public class PrepareAstTest {

  @Test
  public void testPrepareAst() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst prepareAst = new PrepareAst(compiler);
    Node root = Node.objectLit()
        .setJSDocInfo(new Node.JsDocInfo("test", "Test class", new String[]{"foo"}))
        .addChild(Node.prop("a", Node.number(1)))
        .addChild(Node.prop("b", Node.objectLit()))
        .getFirstChild();

    Node expectedRoot = Node.objectLit()
        .setWasEmptyNode(true)
        .setJSDocInfo(new Node.JsDocInfo("test", "Test class", new String[]{"foo"}))
        .addChild(Node.block())
        .addChild(Node.prop("a", Node.number(1)));

    Node expectedBlock = Node.block()
        .addChild(Node.prop("b", Node.objectLit()));

    Node expectedB = Node.objectLit()
        .setWasEmptyNode(true);

    when(compiler.getOptions().getCheckOnly()).thenReturn(true);
    prepareAst.process(null, root);
    assertSame(expectedRoot, root);

    when(compiler.getOptions().getCheckOnly()).thenReturn(false);
    prepareAst.process(null, root);
    assertSame(expectedRoot, root);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraversal()).thenReturn(nodeTraversal);

    prepareAst.process(null, root);
    NodeTraversal.traverse(compiler, root, new PrepareAst.PrepareAnnotations());
    Node actualBlock = root.getChild(0);
    assertSame(expectedBlock, actualBlock);

    Node actualB = actualBlock.getFirstChild();
    assertSame(expectedB, actualB);

    when(compiler.getOptions().getCheckOnly()).thenReturn(false);
    prepareAst = new PrepareAst(compiler, true);
    prepareAst.process(null, root);
    assertNull(root.getJSDocInfo());

    when(compiler.getOptions().getCheckOnly()).thenReturn(false);
    prepareAst = new PrepareAst(compiler, false);
    prepareAst.process(null, root);
    assertSame(expectedRoot, root);

    Node call = Node.call(Node.name("eval"), null);
    call.setJSDocInfo(new Node.JsDocInfo("test", "Test function"))
        .putBooleanProp(Node.DIRECT_EVAL, true);
    root.addChild(call);
    prepareAst.process(null, root);
    assertFalse(call.hasProp(Node.FREE_CALL));

    call.putBooleanProp(Node.FREE_CALL, true);
    prepareAst.process(null, root);
    assertFalse(call.hasProp(Node.DIRECT_EVAL));

    Node function = Node.function(Node.name("test"), null, null, null)
        .setJSDocInfo(new Node.JsDocInfo("test", "Test function"))
        .putBooleanProp(Node.IS_DISPATCHER, true);
    root.addChild(function);
    prepareAst.process(null, root);
    assertSame(function, root.getLastChild());
  }
}