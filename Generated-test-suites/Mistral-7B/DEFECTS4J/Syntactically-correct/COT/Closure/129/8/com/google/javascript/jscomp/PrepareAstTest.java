import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.PrepareAst.*;

import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;

@RunWith(JUnit4.class)
public class PrepareAstTest {

  @Test
  public void testPrepareAstNormalizeNodeTypes() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    Node root = IR.literal(null);
    PrepareAst prepareAst = new PrepareAst(compiler, options.checkOnly());
    prepareAst.process(null, root);

    Node controlStructure = IR.ifStatement();
    Node emptyBlock = IR.block();
    controlStructure.addChildToEnd(emptyBlock);

    Node controlStructureWithChild = IR.ifStatement();
    Node nonControlStructureChild = IR.literal(1);
    controlStructureWithChild.addChildToEnd(nonControlStructureChild);

    root.addChildToEnd(controlStructure);
    root.addChildToEnd(controlStructureWithChild);

    prepareAst.process(null, root);

    assertEquals(3, root.getNumChildren());
    assertEquals(2, controlStructure.getNumChildren());
    assertEquals(1, controlStructureWithChild.getNumChildren());

    Node controlStructureBlock = controlStructure.getLastChild();
    assertEquals(1, controlStructureBlock.getNumChildren());
    assertTrue(controlStructureBlock.isBlock());

    Node controlStructureWithChildBlock = controlStructureWithChild.getLastChild();
    assertEquals(2, controlStructureWithChildBlock.getNumChildren().size());
    assertTrue(controlStructureWithChildBlock.get(0).isBlock());
    assertTrue(controlStructureWithChildBlock.get(1).isNumberLiteral());
  }

  @Test(expected = AssertionError.class)
  public void testPrepareAstNormalizeNodeTypesWithCheckOnlyConstraintViolated() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    Node root = IR.literal(null);
    PrepareAst prepareAst = new PrepareAst(compiler, options.checkOnly());
    prepareAst.process(null, root);

    Node controlStructure = IR.ifStatement();
    Node emptyBlock = IR.block();
    controlStructure.addChildToEnd(emptyBlock);

    Node controlStructureWithChild = IR.ifStatement();
    Node nonControlStructureChild = IR.literal(1);
    controlStructureWithChild.addChildToEnd(nonControlStructureChild);

    root.addChildToEnd(controlStructure);
    root.addChildToEnd(controlStructureWithChild);

    prepareAst = new PrepareAst(compiler, false);
    prepareAst.process(null, root);
  }

  @Test
  public void testPrepareAstPrepareAnnotations() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    Node externs = IR.objectLiteral();
    Node root = IR.variableDeclarationList(IR.variableDeclaration("x", IR.literal(null)));

    PrepareAst prepareAst = new PrepareAst(compiler, false);
    prepareAst.process(externs, root);

    NodeTraversal.traverse(compiler, root, new PrepareAnnotations());

    Node jsDocInfo = externs.getJSDocInfo();
    assertNotNull(jsDocInfo);
    assertEquals("JSDOC", jsDocInfo.getClass().getSimpleName());

    Node variableDeclaration = root.getFirstChild();
    Node jsDocVariableDeclaration = variableDeclaration.getJSDocInfo();
    assertNotNull(jsDocVariableDeclaration);
    assertEquals("x", jsDocVariableDeclaration.getName());
  }
}