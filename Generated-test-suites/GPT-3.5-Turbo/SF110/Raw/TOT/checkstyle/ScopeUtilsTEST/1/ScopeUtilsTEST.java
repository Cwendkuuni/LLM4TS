import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    private DetailAST createDetailAST(int type) {
        DetailAST ast = new DetailAST();
        ast.setType(type);
        return ast;
    }

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = createDetailAST(7);

        ast.setText("public");
        Scope scope = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PUBLIC, scope);

        ast.setText("protected");
        scope = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PROTECTED, scope);

        ast.setText("private");
        scope = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PRIVATE, scope);

        ast.setText("default");
        scope = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PACKAGE, scope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = createDetailAST(14);  // class token

        // Add public modifier
        DetailAST publicModifier = createDetailAST(7);
        publicModifier.setText("public");
        ast.addChild(publicModifier);

        Scope scope = ScopeUtils.getSurroundingScope(ast);
        assertEquals(Scope.PUBLIC, scope);

        // Add protected modifier
        DetailAST protectedModifier = createDetailAST(7);
        protectedModifier.setText("protected");
        ast.addChild(protectedModifier);

        scope = ScopeUtils.getSurroundingScope(ast);
        assertEquals(Scope.PROTECTED, scope);

        // Add private modifier
        DetailAST privateModifier = createDetailAST(7);
        privateModifier.setText("private");
        ast.addChild(privateModifier);

        scope = ScopeUtils.getSurroundingScope(ast);
        assertEquals(Scope.PRIVATE, scope);

        // Add no modifier
        scope = ScopeUtils.getSurroundingScope(ast);
        assertEquals(Scope.PACKAGE, scope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = createDetailAST(15);
        boolean isInInterfaceBlock = ScopeUtils.isInInterfaceBlock(ast);
        assertTrue(isInInterfaceBlock);

        ast = createDetailAST(14);
        isInInterfaceBlock = ScopeUtils.isInInterfaceBlock(ast);
        assertFalse(isInInterfaceBlock);

        ast = createDetailAST(136);
        isInInterfaceBlock = ScopeUtils.isInInterfaceBlock(ast);
        assertFalse(isInInterfaceBlock);

        ast = createDetailAST(157);
        isInInterfaceBlock = ScopeUtils.isInInterfaceBlock(ast);
        assertFalse(isInInterfaceBlock);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = createDetailAST(157);
        boolean isInAnnotationBlock = ScopeUtils.isInAnnotationBlock(ast);
        assertTrue(isInAnnotationBlock);

        ast = createDetailAST(14);
        isInAnnotationBlock = ScopeUtils.isInAnnotationBlock(ast);
        assertFalse(isInAnnotationBlock);

        ast = createDetailAST(15);
        isInAnnotationBlock = ScopeUtils.isInAnnotationBlock(ast);
        assertFalse(isInAnnotationBlock);

        ast = createDetailAST(136);
        isInAnnotationBlock = ScopeUtils.isInAnnotationBlock(ast);
        assertFalse(isInAnnotationBlock);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = createDetailAST(157);
        boolean isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        assertTrue(isInInterfaceOrAnnotationBlock);

        ast = createDetailAST(15);
        isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        assertTrue(isInInterfaceOrAnnotationBlock);

        ast = createDetailAST(154);
        isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        assertTrue(isInInterfaceOrAnnotationBlock);

        ast = createDetailAST(14);
        isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        assertFalse(isInInterfaceOrAnnotationBlock);

        ast = createDetailAST(136);
        isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        assertFalse(isInInterfaceOrAnnotationBlock);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = createDetailAST(154);
        boolean isInEnumBlock = ScopeUtils.isInEnumBlock(ast);
        assertTrue(isInEnumBlock);

        ast = createDetailAST(14);
        isInEnumBlock = ScopeUtils.isInEnumBlock(ast);
        assertFalse(isInEnumBlock);

        ast = createDetailAST(15);
        isInEnumBlock = ScopeUtils.isInEnumBlock(ast);
        assertFalse(isInEnumBlock);

        ast = createDetailAST(157);
        isInEnumBlock = ScopeUtils.isInEnumBlock(ast);
        assertFalse(isInEnumBlock);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = createDetailAST(9);  // LITERAL_FOR
        boolean isInCodeBlock = ScopeUtils.isInCodeBlock(ast);
        assertTrue(isInCodeBlock);

        ast = createDetailAST(8);  // LITERAL_TRY
        isInCodeBlock = ScopeUtils.isInCodeBlock(ast);
        assertTrue(isInCodeBlock);

        ast = createDetailAST(11);  // LITERAL_IF
        isInCodeBlock = ScopeUtils.isInCodeBlock(ast);
        assertTrue(isInCodeBlock);

        ast = createDetailAST(12);  // LITERAL_ELSE
        isInCodeBlock = ScopeUtils.isInCodeBlock(ast);
        assertTrue(isInCodeBlock);

        ast = createDetailAST(13);  // LITERAL_WHILE
        isInCodeBlock = ScopeUtils.isInCodeBlock(ast);
        assertFalse(isInCodeBlock);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast = createDetailAST(14);  // class token
        boolean isOuterMostType = ScopeUtils.isOuterMostType(ast);
        assertTrue(isOuterMostType);

        ast = createDetailAST(15);  // interface token
        isOuterMostType = ScopeUtils.isOuterMostType(ast);
        assertTrue(isOuterMostType);

        ast = createDetailAST(157);  // annotation token
        isOuterMostType = ScopeUtils.isOuterMostType(ast);
        assertTrue(isOuterMostType);

        ast = createDetailAST(154);  // enum token
        isOuterMostType = ScopeUtils.isOuterMostType(ast);
        assertTrue(isOuterMostType);

        ast = createDetailAST(155);  // field token
        isOuterMostType = ScopeUtils.isOuterMostType(ast);
        assertFalse(isOuterMostType);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = createDetailAST(10);  // identifier
        boolean isLocalVariableDef = ScopeUtils.isLocalVariableDef(ast);
        assertFalse(isLocalVariableDef);

        // Test local variable definition
        DetailAST localVariableDef = createDetailAST(7);
        ast.setParent(localVariableDef);
        localVariableDef.setType(10);  // identifier for local variable definition
        boolean isLocalVariableDefinition = ScopeUtils.isLocalVariableDef(localVariableDef);
        assertTrue(isLocalVariableDefinition);

        // Test for loop initializer
        DetailAST forLoop = createDetailAST(9);  // for token
        localVariableDef.setParent(forLoop);
        forLoop.addChild(localVariableDef);
        boolean isForLoopInitializer = ScopeUtils.isLocalVariableDef(localVariableDef);
        assertTrue(isForLoopInitializer);

        // Test enhanced for loop parameter
        DetailAST enhancedForLoop = createDetailAST(14);  // class token
        localVariableDef.setParent(enhancedForLoop);
        enhancedForLoop.addChild(localVariableDef);
        boolean isEnhancedForLoopParam = ScopeUtils.isLocalVariableDef(localVariableDef);
        assertTrue(isEnhancedForLoopParam);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = createDetailAST(10);  // identifier
        boolean isClassFieldDef = ScopeUtils.isClassFieldDef(ast);
        assertFalse(isClassFieldDef);

        // Test for local variable definition
        DetailAST localVariableDef = createDetailAST(7);
        ast.setParent(localVariableDef);
        localVariableDef.setType(10);  // identifier for local variable definition
        boolean isLocalVariableDefinition = ScopeUtils.isClassFieldDef(localVariableDef);
        assertFalse(isLocalVariableDefinition);

        // Test for class field definition
        DetailAST classFieldDef = createDetailAST(155);
        classFieldDef.setType(10);  // identifier for class field definition
        isClassFieldDef = ScopeUtils.isClassFieldDef(classFieldDef);
        assertTrue(isClassFieldDef);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = createDetailAST(14);  // class token
        DetailAST scope = createDetailAST(7);  // public modifier
        ast.addChild(scope);

        boolean isInScope = ScopeUtils.isInScope(scope, Scope.PACKAGE);
        assertFalse(isInScope);

        isInScope = ScopeUtils.isInScope(scope, Scope.PUBLIC);
        assertTrue(isInScope);

        isInScope = ScopeUtils.isInScope(scope, Scope.PROTECTED);
        assertFalse(isInScope);

        isInScope = ScopeUtils.isInScope(scope, Scope.PRIVATE);
        assertFalse(isInScope);

        isInScope = ScopeUtils.isInScope(scope, Scope.ANONINNER);
        assertFalse(isInScope);
    }
}