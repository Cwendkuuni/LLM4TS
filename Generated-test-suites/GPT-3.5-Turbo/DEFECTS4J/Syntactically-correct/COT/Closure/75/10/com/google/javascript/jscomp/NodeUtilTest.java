
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.common.base.Preconditions;

/**
 * Non public methods copied from com.google.javascript.jscomp.NodeUtil class.
 *
 * @author dhans@google.com (Daniel Hans)
 */
class NodeUtil {

  /**
   * @return Whether the node represents a FOR-IN loop.
   */
  static boolean isForIn(Node n) {
    return n.getType() == Token.FOR
        && n.getChildCount() == 3;
  }

  /**
   * @return Whether the node is used as a statement.
   */
  static boolean isStatement(Node n) {
    Node parent = n.getParent();
    // It is not possible to determine definitely if a node is a statement
    // or not if it is not part of the AST.  A FUNCTION node can be
    // either part of an expression or a statement.
    Preconditions.checkState(parent != null);
    switch (parent.getType()) {
      case Token.SCRIPT:
      case Token.BLOCK:
      case Token.LABEL:
        return true;
      default:
        return false;
    }
  }

  /**
   * Is this node a function declaration? A function declaration is a function
   * that has a name that is added to the current scope (i.e. a function that
   * is not part of a expression; see {@link #isFunctionExpression}).
   */
  static boolean isFunctionDeclaration(Node n) {
    return n.getType() == Token.FUNCTION && isStatement(n);
  }

  /**
   * Is this node a hoisted function declaration? A function declaration in the
   * scope root is hoisted to the top of the scope.
   * See {@link #isFunctionDeclaration}).
   */
  static boolean isHoistedFunctionDeclaration(Node n) {
    return isFunctionDeclaration(n)
        && (n.getParent().getType() == Token.SCRIPT
            || n.getParent().getParent().getType() == Token.FUNCTION);
  }
}
