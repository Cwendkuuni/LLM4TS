import static org.junit.Assert.*;

import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.util.Arrays;
import java.util.List;

public class MakeDeclaredNamesUniqueTest {

    @org.junit.Test
    public void testBasicFunctionDeclaration() {
        Node rootNode = new Node(Token.PROGRAM, null);
        Node functionDeclaration = new Node(Token.FUNCTION, null);
        Node nameNode = new Node(Token.NAME, "func");
        Node body = new Node(Token.BLOCK, null);

        functionDeclaration.addChild(nameNode);
        functionDeclaration.addChild(body);
        rootNode.addChild(functionDeclaration);

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
        nodeTraversal.setAbstractCallback(makeDeclaredNamesUnique);
        nodeTraversal.traverse();

        assertEquals("func$0", nameNode.getString());
    }

    @org.junit.Test
    public void testBasicVarDeclaration() {
        Node rootNode = new Node(Token.PROGRAM, null);
        Node declaration = new Node(Token.ASSIGN, null);
        Node nameNode = new Node(Token.NAME, "var");
        declaration.addChild(nameNode);
        rootNode.addChild(declaration);

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
        nodeTraversal.setAbstractCallback(makeDeclaredNamesUnique);
        nodeTraversal.traverse();

        assertEquals("var$0", nameNode.getString());
    }

    @org.junit.Test
    public void testFunctionDeclarationWithSameNameInParentFunction() {
        Node rootNode = new Node(Token.PROGRAM, null);
        Node functionDeclaration = new Node(Token.FUNCTION, null);
        Node parentFunctionDeclaration = new Node(Token.FUNCTION, null);
        Node nameNode = new Node(Token.NAME, "func");
        Node body = new Node(Token.BLOCK, null);

        parentFunctionDeclaration.addChild(nameNode);
        parentFunctionDeclaration.addChild(new Node(Token.BLOCK, null));
        functionDeclaration.addChild(nameNode);
        functionDeclaration.addChild(body);
        rootNode.addChild(parentFunctionDeclaration);
        rootNode.addChild(functionDeclaration);

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
        nodeTraversal.setAbstractCallback(makeDeclaredNamesUnique);
        nodeTraversal.traverse();

        assertEquals("func$1", parentFunctionDeclaration.getChild(0).getString());
        assertEquals("func$0", functionDeclaration.getChild(0).getString());
    }

    @org.junit.Test
    public void testFunctionDeclarationWithSameNameInGlobalScope() {
        Node rootNode = new Node(Token.PROGRAM, null);
        Node functionDeclaration = new Node(Token.FUNCTION, null);
        Node nameNode = new Node(Token.NAME, "func");
        Node body = new Node(Token.BLOCK, null);

        rootNode.addChild(functionDeclaration);
        functionDeclaration.addChild(nameNode);
        functionDeclaration.addChild(body);

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
        nodeTraversal.setAbstractCallback(makeDeclaredNamesUnique);
        nodeTraversal.traverse();

        assertEquals("func$0", nameNode.getString());
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testFunctionDeclarationWithSameNameInFunction() {
        Node rootNode = new Node(Token.PROGRAM, null);
        Node functionDeclaration = new Node(Token.FUNCTION, null);
        Node childFunctionDeclaration = new Node(Token.FUNCTION, null);
        Node nameNode = new Node(Token.NAME, "func");
        Node body = new Node(Token.BLOCK, null);

        functionDeclaration.addChild(nameNode);
        functionDeclaration.addChild(body);
        childFunctionDeclaration.addChild(nameNode);
        childFunctionDeclaration.addChild(new Node(Token.BLOCK, null));
        functionDeclaration.addChild(childFunctionDeclaration);

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
        nodeTraversal.setAbstractCallback(makeDeclaredNamesUnique);
        nodeTraversal.traverse();

        fail(); // Should throw exception
    }

    @org.junit.Test
    public void testVariableDeclarationWithSameNameInFunction() {
        Node rootNode = new Node(Token.PROGRAM, null);
        Node functionDeclaration = new Node(Token.FUNCTION, null);
        Node varDeclaration1 = new Node(Token.ASSIGN, null);
        Node nameNode1 = new Node(Token.NAME, "var");
        Node varDeclaration2 = new Node(Token.ASSIGN, null);
        Node nameNode2 = new Node(Token.NAME, "var");
        Node body = new Node(Token.BLOCK, null);

        nameNode1.setType(Token.VAR);
        varDeclaration1.addChild(nameNode1);
        varDeclaration2.addChild(nameNode2);
        body.addChild(varDeclaration1);
        body.addChild(varDeclaration2);
        functionDeclaration.addChild(nameNode1);
        functionDeclaration.addChild(body);
        rootNode.addChild(functionDeclaration);

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
        nodeTraversal.setAbstractCallback(makeDeclaredNamesUnique);
        nodeTraversal.traverse();

        assertEquals("var$0", nameNode1.getString());
        assertEquals("var$1", nameNode2.getString());
    }

    @org.junit.Test
    public void testVariableDeclarationWithSameNameInGlobalScope() {
        Node rootNode = new Node(Token.PROGRAM, null);
        Node varDeclaration1 = new Node(Token.ASSIGN, null);
        Node nameNode1 = new Node(Token.NAME, "var");
        Node varDeclaration2 = new Node(Token.ASSIGN, null);
        Node nameNode2 = new Node(Token.NAME, "var");
        Node body = new Node(Token.BLOCK, null);

        nameNode1.setType(Token.VAR);
        varDeclaration1.addChild(nameNode1);
        varDeclaration2.addChild(nameNode2);
        body.addChild(varDeclaration1);
        body.addChild(varDeclaration2);
        rootNode.addChild(varDeclaration1);
        rootNode.addChild(varDeclaration2);

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
        nodeTraversal.setAbstractCallback(makeDeclaredNamesUnique);
        nodeTraversal.traverse();

        assertEquals("var$0", nameNode1.getString());
        assertEquals("var$1", nameNode2.getString());
    }

    @org.junit.Test
    public void testVariableDeclarationWithSameNameInFunctionAndGlobalScope() {
        Node rootNode = new Node(Token.PROGRAM, null);
        Node functionDeclaration = new Node(Token.FUNCTION, null);
        Node varDeclarationInFunction = new Node(Token.ASSIGN, null);
        Node nameNode1 = new Node(Token.NAME, "var");
        Node varDeclarationInGlobalScope = new Node(Token.ASSIGN, null);
        Node nameNode2 = new Node(Token.NAME, "var");
        Node body = new Node(Token.BLOCK, null);

        nameNode1.setType(Token.VAR);
        varDeclarationInFunction.addChild(nameNode1);
        varDeclarationInGlobalScope.addChild(nameNode2);
        body.addChild(varDeclarationInFunction);
        body.addChild(varDeclarationInGlobalScope);
        functionDeclaration.addChild(nameNode1);
        rootNode.addChild(functionDeclaration);
        rootNode.addChild(varDeclarationInGlobalScope);

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
        nodeTraversal.setAbstractCallback(makeDeclaredNamesUnique);
        nodeTraversal.traverse();

        assertEquals("var$0", nameNode1.getString());
        assertEquals("var$1", nameNode2.getString());
    }

    @org.junit.Test
    public void testFactorialFunction() {
        String inputCode =
            "function factorial(n) {" +
            "  if (n <= 0) {" +
            "    return 1;" +
            "  } else {" +
            "    return n * factorial(n - 1);" +
            "  }" +
            "}";

        Node rootNode = new Node(Token.PROGRAM, null);
        Node factorialFunction = new Node(Token.FUNCTION, null);
        Node functionNameNode = new Node(Token.NAME, "factorial");
        Node argumentList = new Node(Token.LIST, null);
        Node argument = new Node(Token.NAME, "n");
        Node condition = new Node(Token.IF, null);
        Node ifBlock = new Node(Token.BLOCK, null);
        Node returnStatement = new Node(Token.RETURN, null);
        Node multiplyOperator = new Node(Token.TIMES, null);
        Node recursiveCall = new Node(Token.NAME, "factorial");
        Node recursiveCallArgument = new Node(Token.MINUS, null);
        Node recursiveCallArgumentValue = new Node(Token.INT_LITERAL, 1);
        Node recursiveCallArgumentList = new Node(Token.LIST, null);
        recursiveCallArgumentList.addChild(recursiveCallArgumentValue);
        recursiveCall.addChild(recursiveCallArgumentList);

        functionNameNode.setType(Token.FUNCTION);
        argumentList.addChild(argument);
        condition.addChild(functionNameNode);
        condition.addChild(argumentList);
        ifBlock.addChild(returnStatement);
        returnStatement.addChild(multiplyOperator);
        multiplyOperator.addChild(argument);
        multiplyOperator.addChild(recursiveCall);
        factorialFunction.addChild(functionNameNode);
        factorialFunction.addChild(argumentList);
        factorialFunction.addChild(condition);
        factorialFunction.addChild(ifBlock);
        rootNode.addChild(factorialFunction);

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
        nodeTraversal.setAbstractCallback(makeDeclaredNamesUnique);
        nodeTraversal.traverse();

        List<String> expectedDeclaredNames = Lists.newArrayList(
            "factorial$0", "n$0"
        );

        List<String> actualDeclaredNames = new ArrayList<>();
        for (Node node = rootNode.getFirstChild(); node != null; node = node.getNext()) {
            if (node.getType() == Token.FUNCTION) {
                String functionName = node.getFirstChild().getString();
                if (functionName.equals("factorial")) {
                    for (Node childNode = node.getFirstChild().getNext();
                         childNode != null; childNode = childNode.getNext()) {
                        if (childNode.getType() == Token.NAME) {
                            actualDeclaredNames.add(childNode.getString());
                        }
                    }
                    break;
                }
            }
        }

        assertEquals(expectedDeclaredNames, actualDeclaredNames);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullRootNode() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        Node rootNode = null;
        NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
        nodeTraversal.setAbstractCallback(makeDeclaredNamesUnique);
        nodeTraversal.traverse();

        fail(); // Should throw exception
    }
}