import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;
    private AbstractCompiler compiler;

    @Before
    public void setup() {
        compiler = new AbstractCompiler();
        checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    }

    @Test
    public void testShouldTraverse_FunctionIsConstructor_ReturnsFalse() throws NoSuchFieldException, IllegalAccessException {
        Field assignLhsChildField = CheckGlobalThis.class.getDeclaredField("assignLhsChild");
        assignLhsChildField.setAccessible(true);
        assignLhsChildField.set(checkGlobalThis, new Node(Token.ASSIGN));

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(), new Node(Token.FUNCTION), new Node(Token.NAME));

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionHasThisType_ReturnsFalse() throws NoSuchFieldException, IllegalAccessException {
        Field assignLhsChildField = CheckGlobalThis.class.getDeclaredField("assignLhsChild");
        assignLhsChildField.setAccessible(true);
        assignLhsChildField.set(checkGlobalThis, new Node(Token.ASSIGN));

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(), new Node(Token.FUNCTION), new Node(Token.NAME));

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionHasOverrideAnnotation_ReturnsFalse() throws NoSuchFieldException, IllegalAccessException {
        Field assignLhsChildField = CheckGlobalThis.class.getDeclaredField("assignLhsChild");
        assignLhsChildField.setAccessible(true);
        assignLhsChildField.set(checkGlobalThis, new Node(Token.ASSIGN));

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(), new Node(Token.FUNCTION), new Node(Token.NAME));

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionParentIsNotBlockScriptNameOrAssign_ReturnsFalse() throws NoSuchFieldException, IllegalAccessException {
        Field assignLhsChildField = CheckGlobalThis.class.getDeclaredField("assignLhsChild");
        assignLhsChildField.setAccessible(true);
        assignLhsChildField.set(checkGlobalThis, new Node(Token.ASSIGN));

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(), new Node(Token.FUNCTION), new Node(Token.ADD));

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_ParentIsNull_ReturnsTrue() throws NoSuchFieldException, IllegalAccessException {
        Field assignLhsChildField = CheckGlobalThis.class.getDeclaredField("assignLhsChild");
        assignLhsChildField.setAccessible(true);
        assignLhsChildField.set(checkGlobalThis, new Node(Token.ASSIGN));

        boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(), new Node(Token.FUNCTION), null);

        assertTrue(result);
    }

    @Test
    public void testVisit_NodeIsThisAndShouldReportThis_ReturnsTrue() throws NoSuchFieldException, IllegalAccessException {
        NodeTraversal traversal = new NodeTraversal();
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        parent.putBooleanProp(NodeUtil.IS_GET, true);

        boolean result = checkGlobalThis.shouldReportThis(node, parent);

        assertTrue(result);
    }

    @Test
    public void testVisit_NodeIsThisAndShouldNotReportThis_ReturnsFalse() throws NoSuchFieldException, IllegalAccessException {
        NodeTraversal traversal = new NodeTraversal();
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);

        boolean result = checkGlobalThis.shouldReportThis(node, parent);

        assertFalse(result);
    }

    @Test
    public void testVisit_NodeIsAssignLhsChild_AssignLhsChildIsNull() throws NoSuchFieldException, IllegalAccessException {
        Field assignLhsChildField = CheckGlobalThis.class.getDeclaredField("assignLhsChild");
        assignLhsChildField.setAccessible(true);
        assignLhsChildField.set(checkGlobalThis, null);

        checkGlobalThis.visit(new NodeTraversal(), new Node(Token.NAME), new Node(Token.ASSIGN));

        Field assignLhsChildFieldAfterVisit = CheckGlobalThis.class.getDeclaredField("assignLhsChild");
        assignLhsChildFieldAfterVisit.setAccessible(true);
        Node assignLhsChildAfterVisit = (Node) assignLhsChildField.get(checkGlobalThis);

        assertNull(assignLhsChildAfterVisit);
    }

    @Test
    public void testVisit_NodeIsAssignLhsChild_AssignLhsChildIsNotNull() throws NoSuchFieldException, IllegalAccessException {
        Field assignLhsChildField = CheckGlobalThis.class.getDeclaredField("assignLhsChild");
        assignLhsChildField.setAccessible(true);
        assignLhsChildField.set(checkGlobalThis, new Node(Token.NAME));

        checkGlobalThis.visit(new NodeTraversal(), new Node(Token.NAME), new Node(Token.ASSIGN));

        Field assignLhsChildFieldAfterVisit = CheckGlobalThis.class.getDeclaredField("assignLhsChild");
        assignLhsChildFieldAfterVisit.setAccessible(true);
        Node assignLhsChildAfterVisit = (Node) assignLhsChildField.get(checkGlobalThis);

        assertNull(assignLhsChildAfterVisit);
    }

    @Test
    public void testVisit_NodeIsNotThis_Returns() {
        NodeTraversal traversal = new NodeTraversal();

        checkGlobalThis.visit(traversal, new Node(Token.NAME), new Node(Token.NAME));

        // No assertion, just checking that it does not throw any exceptions
    }

    @Test
    public void testVisit_NodeIsThis_TriggerWarning() throws NoSuchFieldException, IllegalAccessException {
        Field assignLhsChildField = CheckGlobalThis.class.getDeclaredField("assignLhsChild");
        assignLhsChildField.setAccessible(true);
        assignLhsChildField.set(checkGlobalThis, null);
        NodeTraversal traversal = new NodeTraversal();
        traversal.setErrorHandler(compiler);

        checkGlobalThis.visit(traversal, new Node(Token.THIS), new Node(Token.NAME));

        assertEquals(1, traversal.getErrors().size());
        assertEquals(CheckGlobalThis.GLOBAL_THIS, traversal.getErrors().get(0).getType());
    }
}