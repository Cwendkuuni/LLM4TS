import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.Token.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Test class for CheckGlobalThis
 */
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    // Test instance initialization
    CheckLevel level = WARNING;
    AbstractCompiler compiler = new Checker();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    // Test normal cases
    Node normalNode = Node.newFunction("test")
        .addChild(Token.KEYWORD("function"))
        .addChild(Token.IDENT("test"))
        .addChild(Token.BLOCK_START)
        .addChild(Token.RETURN)
        .addChild(Token.STRING("Hello, World!"))
        .addChild(Token.BLOCK_END);

    Node normalParent = Node.newBlock()
        .addChild(normalNode);

    NodeTraversal traversal = new NodeTraversal(normalParent, checker);
    traversal.traverse();
    assertEquals(0, checker.compiler.getDiagnostics().size());

    // Test cases with constructor
    Node constructorNode = Node.newFunction("TestConstructor")
        .addChild(Token.KEYWORD("function"))
        .addChild(Token.IDENT("TestConstructor"))
        .addChild(Token.BLOCK_START)
        .addChild(Token.KEYWORD("constructor"))
        .addChild(Token.BLOCK_END);

    Node constructorParent = Node.newBlock()
        .addChild(constructorNode);

    traversal = new NodeTraversal(constructorParent, checker);
    traversal.traverse();
    assertEquals(0, checker.compiler.getDiagnostics().size());

    // Test cases with @this annotation
    Node thisAnnotatedNode = Node.newFunction("TestThis")
        .addChild(Token.KEYWORD("function"))
        .addChild(Token.IDENT("TestThis"))
        .addChild(new JSDocInfo("@this {type: 'MyType'}"))
        .addChild(Token.BLOCK_START)
        .addChild(Token.RETURN)
        .addChild(Token.STRING("Hello, World!"))
        .addChild(Token.BLOCK_END);

    Node thisAnnotatedParent = Node.newBlock()
        .addChild(thisAnnotatedNode);

    traversal = new NodeTraversal(thisAnnotatedParent, checker);
    traversal.traverse();
    assertEquals(0, checker.compiler.getDiagnostics().size());

    // Test cases with no logical place to put a this annotation
    Node noAnnotatedNode = Node.newFunction("TestNoAnnotation")
        .addChild(Token.KEYWORD("function"))
        .addChild(Token.IDENT("TestNoAnnotation"))
        .addChild(Token.BLOCK_START)
        .addChild(Token.RETURN)
        .addChild(Token.STRING("Hello, World!"))
        .addChild(Token.BLOCK_END);

    Node noAnnotatedParent = Node.newBlock()
        .addChild(noAnnotatedNode);

    traversal = new NodeTraversal(noAnnotatedParent, checker);
    traversal.traverse();
    assertEquals(0, checker.compiler.getDiagnostics().size());

    // Test cases with prototype property
    Node prototypeNode = Node.newFunction("TestPrototype")
        .addChild(Token.KEYWORD("function"))
        .addChild(Token.IDENT("TestPrototype"))
        .addChild(Token.BLOCK_START)
        .addChild(Token.RETURN)
        .addChild(new Node("prototype"))
        .addChild(Token.DOT)
        .addChild(new JSDocInfo("propertyName", "get"))
        .addChild(Token.IDENT("getPrototypeProperty"))
        .addChild(Token.BLOCK_END);

    Node prototypeParent = Node.newBlock()
        .addChild(prototypeNode);

    traversal = new NodeTraversal(prototypeParent, checker);
    traversal.traverse();
    assertEquals(0, checker.compiler.getDiagnostics().size());

    // Test cases with nested assignments
    Node nestedAssignmentsNode = Node.newFunction("TestNestedAssignments")
        .addChild(Token.KEYWORD("function"))
        .addChild(Token.IDENT("TestNestedAssignments"))
        .addChild(Token.BLOCK_START)
        .addChild(new Node("a"))
        .addChild(Token.ASSIGN)
        .addChild(new Node("this"))
        .addChild(Token.DOT)
        .addChild(new Node("x"))
        .addChild(Token.ASSIGN)
        .addChild(Token.INT(1))
        .addChild(Token.SEMICOLON)
        .addChild(Token.BLOCK_END);

    Node nestedAssignmentsParent = Node.newBlock()
        .addChild(nestedAssignmentsNode);

    traversal = new NodeTraversal(nestedAssignmentsParent, checker);
    traversal.traverse();
    assertEquals(0, checker.compiler.getDiagnostics().size());

    // Test cases with global this usage
    Node globalThisNode = Node.newFunction("TestGlobalThis")
        .addChild(Token.KEYWORD("function"))
        .addChild(Token.IDENT("TestGlobalThis"))
        .addChild(Token.BLOCK_START)
        .addChild(Token.RETURN)
        .addChild(new Node("this"))
        .addChild(Token.DOT)
        .addChild(Token.STRING("property"))
        .addChild(Token.SEMICOLON)
        .addChild(Token.BLOCK_END);

    Node globalThisParent = Node.newBlock()
        .addChild(globalThisNode);

    traversal = new NodeTraversal(globalThisParent, checker);
    traversal.traverse();
    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals("JSC_USED_GLOBAL_THIS", checker.compiler.getDiagnostics().get(0).getCode());

    // Test cases with global this usage in property access
    Node globalThisPropertyAccessNode = Node.newFunction("TestGlobalThisPropertyAccess")
        .addChild(Token.KEYWORD("function"))
        .addChild(Token.IDENT("TestGlobalThisPropertyAccess"))
        .addChild(Token.BLOCK_START)
        .addChild(Token.RETURN)
        .addChild(Token.DOT)
        .addChild(new Node("this"))
        .addChild(Token.STRING("property"))
        .addChild(Token.SEMICOLON)
        .addChild(Token.BLOCK_END);

    Node globalThisPropertyAccessParent = Node.newBlock()
        .addChild(globalThisPropertyAccessNode);

    traversal = new NodeTraversal(globalThisPropertyAccessParent, checker);
    traversal.traverse();
    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals("JSC_USED_GLOBAL_THIS", checker.compiler.getDiagnostics().get(0).getCode());

    // Test cases with global this usage in assignment
    Node globalThisAssignmentNode = Node.newFunction("TestGlobalThisAssignment")
        .addChild(Token.KEYWORD("function"))
        .addChild(Token.IDENT("TestGlobalThisAssignment"))
        .addChild(Token.BLOCK_START)
        .addChild(new Node("this"))
        .addChild(Token.ASSIGN)
        .addChild(Token.STRING("property"))
        .addChild(Token.SEMICOLON)
        .addChild(Token.BLOCK_END);

    Node globalThisAssignmentParent = Node.newBlock()
        .addChild(globalThisAssignmentNode);

    traversal = new NodeTraversal(globalThisAssignmentParent, checker);
    traversal.traverse();
    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals("JSC_USED_GLOBAL_THIS", checker.compiler.getDiagnostics().get(0).getCode());
  }

  @Test(expected = AssertionError.class)
  public void testUnreachableCode() {
    // Test case with unreachable code
    Node unreachableCodeNode = Node.newFunction("TestUnreachableCode")
        .addChild(Token.KEYWORD("function"))
        .addChild(Token.IDENT("TestUnreachableCode"))
        .addChild(Token.BLOCK_START)
        .addChild(Token.RETURN)
        .addChild(Token.IF)
        .addChild(Token.FALSE)
        .addChild(Token.BLOCK_START)
        .addChild(Token.RETURN)
        .addChild(Token.STRING("Hello, World!"))
        .addChild(Token.BLOCK_END)
        .addChild(Token.ELSE)
        .addChild(Token.BLOCK_START)
        .addChild(Token.RETURN)
        .addChild(Token.STRING("Goodbye, Cruel World!"))
        .addChild(Token.BLOCK_END)
        .addChild(Token.BLOCK_END);

    Node unreachableCodeParent = Node.newBlock();

    NodeTraversal traversal = new NodeTraversal(unreachableCodeParent, new CheckGlobalThis(null, null));
    traversal.traverse();

    // This test case should fail because the code after the ELSE block is unreachable.
  }
}