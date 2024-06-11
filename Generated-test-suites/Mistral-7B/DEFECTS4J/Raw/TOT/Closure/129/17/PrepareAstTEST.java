import static com.google.javascript.jscomp.NodeUtil.isControlStructure;
import static com.google.javascript.jscomp.NodeUtil.isControlStructureCodeBlock;
import static com.google.javascript.jscomp.PrepareAst.NodeTraversal.traverse;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.jscomp.PrepareAst.PrepareAnnotations;
import com.google.javascript.jscomp.PrepareAst.PrepareAnnotations.NodeTraversal;

public class PrepareAstTest {

  private AbstractCompiler compiler;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new AbstractCompiler(options);
  }

  @Test
  public void testPrepareAst_NormalizeNodeTypes() {
    String source = "function foo() {}";
    Node root = IR.parse(source, null, null);

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    Node expectedResult = IR.function().setName("foo").setBody(IR.block());
    Node actualResult = root;

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testPrepareAst_NormalizeBlocks() {
    String source = "if (1 + 1 == 2) { x = 1; }";
    Node root = IR.parse(source, null, null);

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    Node expectedResult = IR.if_(
        IR.eq(
            IR.add(IR.intLit(1), IR.intLit(1)),
            IR.intLit(2)
        ),
        IR.assign(IR.name("x"), IR.intLit(1)),
        IR.undefinedKeyword()
    );
    Node actualResult = root;

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testPrepareAst_PrepareAnnotations() {
    String source = "/**\n * @constructor\n */\nfunction Foo() {}";
    Node root = IR.parse(source, null, null);

    PrepareAst prepareAst = new PrepareAst(compiler, true);
    prepareAst.process(null, root);

    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
    traverse(compiler, root, prepareAnnotations);

    assertEquals("@constructor", root.getJSDocInfo().getTagString());
  }

  @Test
  public void testPrepareAst_NormalizeObjectLiteralAnnotations() {
    String source = "var obj = {\n /**\n  * @param {number} a\n  */\n a: 1,\n /**\n  * @param {string} b\n  */\n b: 'str'\n};";
    Node root = IR.parse(source, null, null);

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    Node objlit = root.getFirstChild();
    assertEquals(2, objlit.getNumChildren());

    Node a = objlit.getChildAt(0);
    assertEquals("number", a.getJSDocInfo().getParamTag().getTagString());

    Node b = objlit.getChildAt(1);
    assertEquals("string", b.getJSDocInfo().getParamTag().getTagString());
  }
}