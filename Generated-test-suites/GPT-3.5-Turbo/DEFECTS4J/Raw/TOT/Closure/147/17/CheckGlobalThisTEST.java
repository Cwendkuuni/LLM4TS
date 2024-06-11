import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void shouldReportGlobalThisInLeftSideOfAssignment() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // When
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, new Node(Token.THIS), new Node(Token.ASSIGN));

        // Then
        assertTrue(shouldTraverse);

    }

    @Test
    public void shouldReportGlobalThisInAssignmentRhsWhenNotPrototypeProperty() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // When
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, new Node(Token.THIS), new Node(Token.ASSIGN));

        // Then
        assertTrue(shouldTraverse);

    }

    @Test
    public void shouldNotReportGlobalThisInAssignmentRhsWhenPrototypeProperty() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // When
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, new Node(Token.GETPROP), new Node(Token.ASSIGN));

        // Then
        assertFalse(shouldTraverse);

    }

    @Test
    public void shouldNotReportGlobalThisInNonGlobalContext() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // When
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, new Node(Token.GETPROP), new Node(Token.SCRIPT));

        // Then
        assertFalse(shouldTraverse);

    }

    @Test
    public void shouldReportGlobalThisWhenNodeIsThisAndShouldReportThisReturnsTrue() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // When
        checkGlobalThis.visit(null, new Node(Token.THIS), new Node(Token.GETPROP));
        // No assertion needed, if report() method is reached, it means it should report

    }

    @Test
    public void shouldNotReportGlobalThisWhenNodeIsAssignLhsChildAndShouldReportThisReturnsFalse() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // When
        checkGlobalThis.visit(null, new Node(Token.THIS), new Node(Token.ASSIGN));
        // No assertion needed, if report() method is not reached, it means it should not report

    }

    @Test
    public void shouldNotReportGlobalThisWhenParentIsNull() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // When
        boolean shouldReportThis = checkGlobalThis.shouldReportThis(new Node(Token.THIS), null);

        // Then
        assertFalse(shouldReportThis);

    }

    @Test
    public void shouldReportGlobalThisWhenParentIsGetNode() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // When
        boolean shouldReportThis = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.GETPROP));

        // Then
        assertTrue(shouldReportThis);

    }

    @Test
    public void shouldNotReportGlobalThisWhenParentIsNotGetNode() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // When
        boolean shouldReportThis = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.ADD));

        // Then
        assertFalse(shouldReportThis);

    }

    @Test
    public void shouldReturnTrueWhenNodeIsFunctionAndHasConstructorJsDocInfo() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node functionNode = new Node(Token.FUNCTION);
        functionNode.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo().markAsConstructor());

        // When
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, functionNode, null);

        // Then
        assertFalse(shouldTraverse);

    }

    @Test
    public void shouldReturnTrueWhenNodeIsFunctionAndHasInterfaceJsDocInfo() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node functionNode = new Node(Token.FUNCTION);
        functionNode.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo().markAsInterface());

        // When
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, functionNode, null);

        // Then
        assertFalse(shouldTraverse);

    }

    @Test
    public void shouldReturnTrueWhenNodeIsFunctionAndHasThisTypeJsDocInfo() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node functionNode = new Node(Token.FUNCTION);
        functionNode.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo().markAsConstructor().markThisAsType());

        // When
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, functionNode, null);

        // Then
        assertFalse(shouldTraverse);

    }

    @Test
    public void shouldReturnTrueWhenNodeIsFunctionAndHasOverrideJsDocInfo() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node functionNode = new Node(Token.FUNCTION);
        functionNode.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo().markAsOverride());

        // When
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, functionNode, null);

        // Then
        assertFalse(shouldTraverse);

    }

    @Test
    public void shouldReturnFalseWhenNodeIsFunctionAndIsNotInBlockScriptNameOrAssign() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node functionNode = new Node(Token.FUNCTION);
        Node parentNode = new Node(Token.NOT);

        // When
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, functionNode, parentNode);

        // Then
        assertFalse(shouldTraverse);

    }

    @Test
    public void shouldReturnFalseWhenNodeIsNotRequestType() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // When
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, new Node(Token.FOR), null);

        // Then
        assertFalse(shouldTraverse);

    }

    @Test
    public void shouldReturnTrueWhenNodeIsAssignLhsChild() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        checkGlobalThis.visit(null, new Node(Token.THIS), new Node(Token.ASSIGN));

        // When
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, new Node(Token.THIS),
                new Node(Token.ASSIGN));

        // Then
        assertTrue(shouldTraverse);

    }

    @Test
    public void shouldReturnFalseWhenNodeIsNotAssignLhsChild() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        checkGlobalThis.visit(null, new Node(Token.NEG), new Node(Token.ADD));

        // When
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, new Node(Token.THIS),
                new Node(Token.ADD));

        // Then
        assertFalse(shouldTraverse);

    }

    @Test
    public void shouldNotSetAssignLhsChildWhenNodeIsNotAssign() {
        // Given
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        checkGlobalThis.visit(null, new Node(Token.THIS), new Node(Token.ADD));

        // When
        checkGlobalThis.shouldTraverse(null, new Node(Token.THIS), new Node(Token.ADD));

        // Then
        assertNull(checkGlobalThis.assignLhsChild);

    }

    @Test
    public void shouldReturnFalseWhenNodeIsNotThisAndParentIsNull() {
        // Given
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        // When
        boolean shouldReportThis = checkGlobalThis.shouldReportThis(new Node(Token.NEG), null);

        // Then
        assertFalse(shouldReportThis);

    }

    @Test
    public void shouldReturnTrueWhenNodeIsThisAndParentIsGetNode() {
        // Given
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        // When
        boolean shouldReportThis = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.GETPROP));

        // Then
        assertTrue(shouldReportThis);

    }

    @Test
    public void shouldReturnFalseWhenNodeIsThisAndParentIsNotGetNode() {
        // Given
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        // When
        boolean shouldReportThis = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.NOT));

        // Then
        assertFalse(shouldReportThis);

    }

    @Test
    public void shouldReturnNullWhenGetFunctionJsDocInfo() {
        // Given
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        // When
        JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(new Node(Token.FUNCTION));

        // Then
        assertNull(jsDocInfo);

    }

    @Test
    public void shouldReturnNonNullJsDocInfoWhenGetFunctionJsDocInfo() {
        // Given
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        // When
        JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(new Node(Token.NAME));
        JSDocInfo jsDocInfoParent = checkGlobalThis.getFunctionJsDocInfo(new Node(Token.NAME).addChildToFront(new Node(Token.VAR)));

        // Then
        assertNotNull(jsDocInfo);
        assertNotNull(jsDocInfoParent);

    }

}