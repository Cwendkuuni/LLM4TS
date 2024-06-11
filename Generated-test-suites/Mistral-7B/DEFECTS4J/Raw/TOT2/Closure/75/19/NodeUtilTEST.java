
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  // isForIn tests
  public static class isForIn {
    @Test
    public static void testIsForIn() {
      NodeUtil.isForInTest test = new NodeUtil.isForInTest();
      test.run();
    }

    @Test
    public static void testIsNotForIn() {
      NodeUtil.isForInEdgeCasesTest test = new NodeUtil.isForInEdgeCasesTest();
      test.run();
    }
  }

  // isStatement tests
  public static class isStatement {
    @Test
    public static void testIsStatement() {
      NodeUtil.isStatementTest test = new NodeUtil.isStatementTest();
      test.run();
    }

    @Test
    public static void testIsStatementEdgeCases() {
      NodeUtil.isStatementEdgeCasesTest test = new NodeUtil.isStatementEdgeCasesTest();
      test.run();
    }
  }

  // isFunctionDeclaration tests
  public static class isFunctionDeclaration {
    @Test
    public static void testIsFunctionDeclaration() {
      NodeUtil.isFunctionDeclarationTest test = new NodeUtil.isFunctionDeclarationTest();
      test.run();
    }

    @Test
    public static void testIsFunctionDeclarationEdgeCases() {
      NodeUtil.isFunctionDeclarationEdgeCasesTest test = new NodeUtil.isFunctionDeclarationEdgeCasesTest();
      test.run();
    }
  }

  // isHoistedFunctionDeclaration tests
  public static class isHoistedFunctionDeclaration {
    @Test
    public static void testIsHoistedFunctionDeclaration() {
      NodeUtil.isHoistedFunctionDeclarationTest test = new NodeUtil.isHoistedFunctionDeclarationTest();
      test.run();
    }

    @Test
    public static void testIsHoistedFunctionDeclarationEdgeCases() {
      NodeUtil.isHoistedFunctionDeclarationEdgeCasesTest test = new NodeUtil.isHoistedFunctionDeclarationEdgeCasesTest();
      test.run();
    }
  }
}