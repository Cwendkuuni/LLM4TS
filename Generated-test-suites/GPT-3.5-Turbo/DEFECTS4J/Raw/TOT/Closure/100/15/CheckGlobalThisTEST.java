import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_NonGlobalContext_ReturnsTrue() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.GETPROP);
    Node parent = new Node(Token.ASSIGN);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ConstructorWithContext_ReturnsFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDoc = JSDocInfo.builder().withConstructor(true).build();
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithContext_ReturnsFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDoc = JSDocInfo.builder().withThisType(true).build();
    n.setJSDocInfo(jsDoc);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionWithoutContext_ReturnsTrue() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_AssignmentWithPrototypeProperty_ReturnsFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.GETPROP);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    Node rhs = new Node(Token.STRING);
    Node prop = new Node(Token.STRING, "prototype");

    n.addChildToBack(lhs);
    n.addChildToBack(prop);
    parent.addChildToFront(n);
    parent.addChildToBack(rhs);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_AssignmentWithPrototypeSubproperty_ReturnsFalse() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.GETPROP);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    Node rhs = new Node(Token.STRING);
    Node prop = new Node(Token.STRING, "property");

    lhs.addChildToBack(prop);
    n.addChildToBack(lhs);
    n.addChildToBack(prop);
    parent.addChildToFront(n);
    parent.addChildToBack(rhs);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_AssignmentWithNonPrototypeProperty_ReturnsTrue() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = null;
    Node n = new Node(Token.GETPROP);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    Node rhs = new Node(Token.STRING);
    Node prop = new Node(Token.STRING, "property");

    lhs.addChildToBack(prop);
    n.addChildToBack(lhs);
    n.addChildToBack(prop);
    parent.addChildToFront(n);
    parent.addChildToBack(rhs);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testVisit_ThisInGlobalContext_ShouldReportThis() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.SCRIPT);
  
    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertTrue(checkGlobalThis.reportedWarningExists);
  }

  @Test
  public void testVisit_ThisAssignmentLeftSide_ShouldReportThis() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    Node rhs = new Node(Token.STRING);
  
    parent.addChildToFront(lhs);
    parent.addChildToBack(rhs);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertTrue(checkGlobalThis.reportedWarningExists);
  }

  @Test
  public void testVisit_ThisAssignmentRightSide_ShouldNotReportThis() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    Node rhs = new Node(Token.THIS);
  
    parent.addChildToFront(lhs);
    parent.addChildToBack(rhs);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertFalse(checkGlobalThis.reportedWarningExists);
  }

  @Test
  public void testVisit_ThisInAssignLhsChild_ShouldNotReportThis() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.ASSIGN);
    Node rhs = new Node(Token.STRING);

    lhs.addChildToBack(n);
    parent.addChildToFront(lhs);
    parent.addChildToBack(rhs);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertFalse(checkGlobalThis.reportedWarningExists);
  }

  @Test
  public void testVisit_ThisNotInAssignLhsChild_ShouldNotReportThis() {
    // Arrange
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal t = null;
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    Node rhs = new Node(Token.STRING);

    parent.addChildToFront(lhs);
    parent.addChildToBack(rhs);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assertFalse(checkGlobalThis.reportedWarningExists);
  }

  private class Node {
    private Token type;
    private Node parentNode;
    private Node prevNode;
    private Node nextNode;
    private JSDocInfo jsDocInfo = null;

    public Node(Token type) {
      this.type = type;
    }

    public void addChildToBack(Node n) {
      n.parentNode = this;
      if (prevNode == null) {
        prevNode = n;
        nextNode = n;
      } else {
        nextNode.nextNode = n;
        n.prevNode = nextNode;
        nextNode = n;
      }
    }

    public void addChildToFront(Node n) {
      n.parentNode = this;
      if (nextNode == null) {
        nextNode = n;
        prevNode = n;
      } else {
        prevNode.prevNode = n;
        n.nextNode = prevNode;
        prevNode = n;
      }
    }

    public void setJSDocInfo(JSDocInfo jsDocInfo) {
      this.jsDocInfo = jsDocInfo;
    }

    public Token getType() {
      return type;
    }

    public Node getParent() {
      return parentNode;
    }

    public Node getFirstChild() {
      return nextNode;
    }

    public Node getNext() {
      return nextNode;
    }

    public JSDocInfo getJSDocInfo() {
      return jsDocInfo;
    }
  }

  private class JSDocInfo {
    private boolean isConstructor;
    private boolean hasThisType;

    public JSDocInfo(boolean isConstructor, boolean hasThisType) {
      this.isConstructor = isConstructor;
      this.hasThisType = hasThisType;
    }

    public static Builder builder() {
      return new Builder();
    }

    public boolean isConstructor() {
      return isConstructor;
    }

    public boolean hasThisType() {
      return hasThisType;
    }

    public static class Builder {
      private boolean isConstructor = false;
      private boolean hasThisType = false;

      public Builder withConstructor(boolean isConstructor) {
        this.isConstructor = isConstructor;
        return this;
      }

      public Builder withThisType(boolean hasThisType) {
        this.hasThisType = hasThisType;
        return this;
      }

      public JSDocInfo build() {
        return new JSDocInfo(isConstructor, hasThisType);
      }
    }
  }
}