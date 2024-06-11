import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IR;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.jscomp.PrepareAnnotations;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {

  private AbstractCompiler compiler;
  private PrepareAst prepareAst;
  private PrepareAnnotations prepareAnnotations;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    prepareAst = new PrepareAst(compiler);
    prepareAnnotations = new PrepareAnnotations();
  }

  @Test
  public void testNormalizeNodeTypes() {
    Node root = createNode("1 + 2");
    prepareAst.process(null, root);
    assertFalse(root.isLabel());
    assertFalse(root.isSwitch());

    Node block = IR.block();
    Node call = createNode("console.log");
    Node arg = createNode("42");
    block.addChildrenToFront(call);
    call.addChildToEnd(arg);

    root = createNode("if (true) {" + block.toString() + "}");
    prepareAst.process(null, root);
    assertFalse(root.isLabel());
    assertFalse(root.isSwitch());

    Node ifStmt = root.getFirstChild();
    assertSame(ifStmt, root.getChildAt(0));
    Node ifCondition = ifStmt.getFirstChild();
    assertSame(ifCondition, root.getChildAt(1));
    assertSame(block, root.getChildAt(2));

    Node ifConditionTrue = ifCondition.getFirstChild();
    Node ifConditionFalse = ifCondition.getLastChild();
    assertNull(ifConditionTrue);
    assertNull(ifConditionFalse);

    Node ifBody = ifStmt.getLastChild();
    assertSame(block, ifBody);
  }

  @Test
  public void testNormalizeBlocks() {
    Node root = createNode("if (true) {} else {}");
    prepareAst.process(null, root);

    Node ifStmt = root.getFirstChild();
    Node ifBody = ifStmt.getLastChild();
    assertNull(ifBody.getFirstChild());
    assertNull(ifBody.getLastChild());

    Node elseStmt = root.getLastChild();
    Node elseBody = elseStmt.getLastChild();
    assertNull(elseBody.getFirstChild());
    assertNull(elseBody.getLastChild());

    Node ifBlock = IR.block();
    Node ifBodyChild = IR.stringLiteral("child");
    ifBlock.addChildrenToFront(ifBodyChild);
    ifBody.replaceChildrenWith(ifBlock);

    root = createNode("if (true) {} else {}", root);
    prepareAst.process(null, root);

    ifStmt = root.getFirstChild();
    ifBody = ifStmt.getLastChild();
    assertSame(ifBlock, ifBody);
    assertSame(ifBodyChild, ifBody.getFirstChild());

    elseStmt = root.getLastChild();
    elseBody = elseStmt.getLastChild();
    assertNull(elseBody);
  }

  @Test
  public void testPrepareAnnotations() {
    Node root = createNode("function foo() {}");
    prepareAst.process(null, root);
    assertFalse(root.isLabel());
    assertFalse(root.isSwitch());
    assertNull(root.getJSDocInfo());

    Node functionNode = root.getFirstChild();
    assertSame(functionNode, root.getChildAt(0));
    assertNull(functionNode.getJSDocInfo());

    prepareAnnotations.process(null, root, null);
    PrepareAnnotations jsdocAnnotations =
        prepareAnnotations.getAnnotations(root);
    assertNull(jsdocAnnotations);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(compiler.getSourceFile()).thenReturn(new SourceFile("test.js"));

    Node functionNodeWithJSDoc =
        createNode("/** @constructor */ function foo() {}");
    prepareAst.process(null, functionNodeWithJSDoc);
    jsdocAnnotations = prepareAnnotations.getAnnotations(root);
    assertSame(JsDocInfo.constructorAnnotation, jsdocAnnotations);

    Node functionNodeWithParamJSDoc =
        createNode("function foo(/** @type {number} */ x) {}");
    prepareAst.process(null, functionNodeWithParamJSDoc);
    jsdocAnnotations = prepareAnnotations.getAnnotations(root);
    JsDocAnnotation paramAnnotation =
        jsdocAnnotations.getParameterAnnotation(0);
    assertSame(JsDocAnnotationType.NORMAL, paramAnnotation.getType());
    assertSame("x", paramAnnotation.getName());
    assertSame("number", paramAnnotation.getDescription());
    assertSame(null, paramAnnotation.getTag());

    Node functionNodeWithTagJSDoc =
        createNode("function foo() { /** @tag foo */ }");
    prepareAst.process(null, functionNodeWithTagJSDoc);
    jsdocAnnotations = prepareAnnotations.getAnnotations(root);
    JsDocAnnotation tagAnnotation = jsdocAnnotations.getTagAnnotation();
    assertSame("foo", tagAnnotation.getName());
  }

  private Node createNode(String source) {
    return (Node) IR.parseTemplate(source, null, null, null, null);
  }

  private static class CompilerOptions {}

  private static class JsDocAnnotation {

    private JsDocAnnotationType type;
    private String name;
    private String description;
    private String tag;

    JsDocAnnotation() {}

    JsDocAnnotation(JsDocAnnotationType type, String name,
        String description) {
      this.type = type;
      this.name = name;
      this.description = description;
    }

    static JsDocAnnotation constructorAnnotation =
        new JsDocAnnotation(JsDocAnnotationType.CONSTRUCTOR, null, null);

    static JsDocAnnotation parameterAnnotation(int index) {
      return new JsDocAnnotation(
          JsDocAnnotationType.NORMAL, NodeUtil.getIdentifier(index), null);
    }

    static JsDocAnnotation tagAnnotation() {
      return new JsDocAnnotation(JsDocAnnotationType.TAG, null, null);
    }

    static JsDocAnnotation getParameterAnnotation(int index) {
      return prepareAnnotations.getParameterAnnotation(root, index);
    }

    static JsDocAnnotation getTagAnnotation() {
      return prepareAnnotations.getTagAnnotation(root);
    }

    JsDocAnnotationType getType() {
      return type;
    }

    String getName() {
      return name;
    }

    String getDescription() {
      return description;
    }

    String getTag() {
      return tag;
    }
  }

  private enum JsDocAnnotationType {
    CONSTRUCTOR, NORMAL
  }

  private static class JsDocInfo {

    static final JsDocInfo constructorAnnotation =
        new JsDocInfo(new JsDocAnnotation(JsDocAnnotationType.CONSTRUCTOR,
            null, null));

    private JsDocAnnotation annotation;

    JsDocInfo(JsDocAnnotation annotation) {
      this.annotation = annotation;
    }

    static JsDocAnnotation getAnnotation(Node node) {
      return node.getJSDocInfo().annotation;
    }

    boolean isJavaDispatch() {
      return annotation.getType() == JsDocAnnotationType.CONSTRUCTOR;
    }
  }
}