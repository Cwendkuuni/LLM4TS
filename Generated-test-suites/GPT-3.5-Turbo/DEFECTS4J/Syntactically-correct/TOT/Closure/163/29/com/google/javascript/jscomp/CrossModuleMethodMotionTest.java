import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CrossModuleMethodMotionTest {

    private CrossModuleMethodMotion.IdGenerator idGenerator;
    private CrossModuleMethodMotion crossModuleMethodMotion;

    @Before
    public void setUp() {
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
        crossModuleMethodMotion = new CrossModuleMethodMotion(null, idGenerator, true);
    }

    @Test
    public void testProcess_withTwoModules_doesNotMoveMethods() {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        assertTrue(idGenerator.hasGeneratedAnyIds());

        crossModuleMethodMotion.process(null, null);

        assertFalse(idGenerator.hasGeneratedAnyIds());
        assertFalse(nameInfo.isReferenced());
    }

    @Test
    public void testProcess_withOneModule_doesNotMoveMethods() {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        crossModuleMethodMotion = new CrossModuleMethodMotion(null, idGenerator, false);

        assertFalse(idGenerator.hasGeneratedAnyIds());
        assertFalse(nameInfo.isReferenced());

        crossModuleMethodMotion.process(null, null);

        assertFalse(idGenerator.hasGeneratedAnyIds());
        assertFalse(nameInfo.isReferenced());
    }

    @Test
    public void testProcess_withReferencedSymbol_movesMethod() {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new JSModule("module1"));
        symbol.setParent(new Node());
        symbol.setValue(new Node());
        nameInfo.addDeclaration(symbol);

        assertFalse(idGenerator.hasGeneratedAnyIds());
        nameInfo.setReferenced(true);

        crossModuleMethodMotion.process(null, null);

        assertTrue(idGenerator.hasGeneratedAnyIds());
        assertFalse(nameInfo.isReferenced());
        assertEquals("JSCompiler_stubMethod(0)", symbol.getValue().toString());
    }

    @Test
    public void testProcess_withGetterDef_doesNotMoveMethod() {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new JSModule("module1"));
        symbol.setParent(new Node());
        symbol.setValue(new Node());
        Node getterDefNode = new Node();
        getterDefNode.setIsGetterDef(true);
        symbol.getParent().addChildToFront(getterDefNode);
        nameInfo.addDeclaration(symbol);

        assertFalse(idGenerator.hasGeneratedAnyIds());
        nameInfo.setReferenced(true);

        crossModuleMethodMotion.process(null, null);

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testProcess_withSetterDef_doesNotMoveMethod() {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new JSModule("module1"));
        symbol.setParent(new Node());
        symbol.setValue(new Node());
        Node setterDefNode = new Node();
        setterDefNode.setIsSetterDef(true);
        symbol.getParent().addChildToFront(setterDefNode);
        nameInfo.addDeclaration(symbol);

        assertFalse(idGenerator.hasGeneratedAnyIds());
        nameInfo.setReferenced(true);

        crossModuleMethodMotion.process(null, null);

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testProcess_withNullCommonModuleRef_reportsError() {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new JSModule("module1"));
        symbol.setParent(new Node());
        symbol.setValue(new Node());
        Node.prototype.addChildToFront(symbol.getValue());
        nameInfo.addDeclaration(symbol);

        assertFalse(idGenerator.hasGeneratedAnyIds());
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(null);

        crossModuleMethodMotion.process(null, null);

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_withProperty_movesMethod() {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new JSModule("module1"));
        symbol.setParent(new Node());
        symbol.setValue(new Node());
        Node.prototype.addChildToFront(symbol.getValue());
        nameInfo.addDeclaration(symbol);

        assertFalse(idGenerator.hasGeneratedAnyIds());
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(new JSModule("module2"));
        symbol.getValue().addChildToFront(new Node(Node.FUNCTION));

        crossModuleMethodMotion.moveMethods(nameInfo);

        assertTrue(idGenerator.hasGeneratedAnyIds());
        assertFalse(nameInfo.isReferenced());
        assertEquals("JSCompiler_stubMethod(0)", symbol.getValue().toString());
    }

    @Test
    public void testMoveMethods_withoutProperty_doesNotMoveMethod() {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new JSModule("module1"));
        symbol.setParent(new Node());
        symbol.setValue(new Node());
        Node.prototype.addChildToFront(symbol.getValue());
        nameInfo.addDeclaration(symbol);

        assertFalse(idGenerator.hasGeneratedAnyIds());
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(new JSModule("module1"));
        symbol.getValue().addChildToFront(new Node(Node.OBJECTLIT));

        crossModuleMethodMotion.moveMethods(nameInfo);

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_withoutValue_doesNotMoveMethod() {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new JSModule("module1"));
        symbol.setParent(new Node());
        symbol.setValue(null);
        Node.prototype.addChildToFront(symbol.getValue());
        nameInfo.addDeclaration(symbol);

        assertFalse(idGenerator.hasGeneratedAnyIds());
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(new JSModule("module2"));

        crossModuleMethodMotion.moveMethods(nameInfo);

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_withGetterDef_doesNotMoveMethod() {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new JSModule("module1"));
        symbol.setParent(new Node());
        symbol.setValue(new Node());
        symbol.getParent().addChildToFront(new Node().copyInformationFromForTree(symbol.getValue()));

        assertFalse(idGenerator.hasGeneratedAnyIds());

        crossModuleMethodMotion.moveMethods(nameInfo);

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_withSetterDef_doesNotMoveMethod() {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new JSModule("module1"));
        symbol.setParent(new Node());
        symbol.setValue(new Node());
        symbol.getParent().addChildToFront(new Node().copyInformationFromForTree(symbol.getValue()));

        assertFalse(idGenerator.hasGeneratedAnyIds());

        crossModuleMethodMotion.moveMethods(nameInfo);

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_withoutDeepestCommonModuleRef_reportsError() {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new JSModule("module1"));
        symbol.setParent(new Node());
        symbol.setValue(new Node());
        symbol.getValue().setIsFunction(true);
        Node.prototype.addChildToFront(symbol.getValue());
        nameInfo.addDeclaration(symbol);

        assertFalse(idGenerator.hasGeneratedAnyIds());
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(null);

        crossModuleMethodMotion.moveMethods(nameInfo);

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_withoutReferencedSymbol_doesNotMoveMethod() {
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new JSModule("module1"));
        symbol.setParent(new Node());
        symbol.setValue(new Node());
        symbol.getValue().setIsFunction(true);
        Node.prototype.addChildToFront(symbol.getValue());
        nameInfo.addDeclaration(symbol);

        assertFalse(idGenerator.hasGeneratedAnyIds());
        assertFalse(nameInfo.isReferenced());

        crossModuleMethodMotion.moveMethods(nameInfo);

        assertFalse(idGenerator.hasGeneratedAnyIds());
        assertFalse(nameInfo.isReferenced());
    }

    @Test
    public void testIdGenerator_hasGeneratedAnyIds_returnsFalseByDefault() {
        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testIdGenerator_hasGeneratedAnyIds_returnsTrueAfterNewId() {
        idGenerator.newId();
        assertTrue(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testIdGenerator_newId_returnsIncrementedId() {
        int id = idGenerator.newId();
        assertEquals(0, id);
        id = idGenerator.newId();
        assertEquals(1, id);
    }

    @Test
    public void testProcess_withTwoModulesAndNoIdDeclaration_declaresStubMethods() {
        crossModuleMethodMotion.process(null, null);
        assertTrue(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testProcess_withTwoModulesAndExistingIdDeclaration_doesNotDeclareStubMethods() {
        crossModuleMethodMotion = new CrossModuleMethodMotion(null, idGenerator, true);
        idGenerator.newId();

        crossModuleMethodMotion.process(null, null);
        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

}