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
    public void testMoveMethods_AllNameInfoIsNull_NoMethodsMoved() {
        crossModuleMethodMotion.process(null, null);
        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_ModuleGraphIsNull_NoMethodsMoved() {
        crossModuleMethodMotion.process(new Node(), new Node());
        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_ModuleCountLessThanTwo_NoMethodsMoved() {
        crossModuleMethodMotion.process(new Node(), new Node());
        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_PropertyIsNotFunction_NoMethodsMoved() {
        // Create a property that is not a function
        CrossModuleMethodMotion.Property property = new CrossModuleMethodMotion.Property();
        property.setValue(new Node());

        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new CrossModuleMethodMotion.JSModule());
        symbol.setPrototype(new Node());
        symbol.setValue(new Node());

        property.getDeclarations().add(symbol);
        symbol.setParent(property);

        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        nameInfo.getDeclarations().add(symbol);
        symbol.setParent(nameInfo);

        crossModuleMethodMotion.process(new Node(), new Node());
        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_ModuleDependsOnDeepestCommonModuleRef_MethodMoved() {
        // Create a property that is a function
        CrossModuleMethodMotion.Property property = new CrossModuleMethodMotion.Property();
        property.setValue(new Node().setIsFunction());

        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new CrossModuleMethodMotion.JSModule());
        symbol.setPrototype(new Node());
        symbol.setValue(new Node());

        property.getDeclarations().add(symbol);
        symbol.setParent(property);

        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        nameInfo.getDeclarations().add(symbol);
        symbol.setParent(nameInfo);

        CrossModuleMethodMotion.JSModule deepestCommonModuleRef = new CrossModuleMethodMotion.JSModule();
        nameInfo.setDeepestCommonModuleRef(deepestCommonModuleRef);

        crossModuleMethodMotion.process(new Node(), new Node());
        assertTrue(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_NullDeepestCommonModuleRef_ErrorReported() {
        // Create a property that is a function
        CrossModuleMethodMotion.Property property = new CrossModuleMethodMotion.Property();
        property.setValue(new Node().setIsFunction());

        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new CrossModuleMethodMotion.JSModule());
        symbol.setPrototype(new Node());
        symbol.setValue(new Node());

        property.getDeclarations().add(symbol);
        symbol.setParent(property);

        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        nameInfo.getDeclarations().add(symbol);
        symbol.setParent(nameInfo);

        nameInfo.setDeepestCommonModuleRef(null);

        crossModuleMethodMotion.process(new Node(), new Node());
        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_ReadsClosureVariables_MethodNotMoved() {
        // Create a property that is a function
        CrossModuleMethodMotion.Property property = new CrossModuleMethodMotion.Property();
        property.setValue(new Node().setIsFunction());

        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new CrossModuleMethodMotion.JSModule());
        symbol.setPrototype(new Node());
        symbol.setValue(new Node());

        property.getDeclarations().add(symbol);
        symbol.setParent(property);

        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        nameInfo.getDeclarations().add(symbol);
        symbol.setParent(nameInfo);

        nameInfo.setDeepestCommonModuleRef(new CrossModuleMethodMotion.JSModule());
        nameInfo.setReadsClosureVariables(true);

        crossModuleMethodMotion.process(new Node(), new Node());
        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_PropertyIsGetterOrSetter_NoMethodsMoved() {
        // Create a property that is a function
        CrossModuleMethodMotion.Property property = new CrossModuleMethodMotion.Property();
        property.setValue(new Node().setIsFunction());

        // Set parent as getter or setter def
        Node parent = new Node();
        parent.setIsGetterDef(true);
        parent.setIsSetterDef(true);

        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new CrossModuleMethodMotion.JSModule());
        symbol.setPrototype(new Node());
        symbol.setValue(parent);

        property.getDeclarations().add(symbol);
        symbol.setParent(property);

        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        nameInfo.getDeclarations().add(symbol);
        symbol.setParent(nameInfo);

        nameInfo.setDeepestCommonModuleRef(new CrossModuleMethodMotion.JSModule());
        nameInfo.setReadsClosureVariables(false);

        crossModuleMethodMotion.process(new Node(), new Node());
        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_PropertyIsFunction_MethodMoved() {
        // Create a property that is a function
        CrossModuleMethodMotion.Property property = new CrossModuleMethodMotion.Property();
        property.setValue(new Node().setIsFunction());

        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setModule(new CrossModuleMethodMotion.JSModule());
        symbol.setPrototype(new Node());
        symbol.setValue(new Node());

        property.getDeclarations().add(symbol);
        symbol.setParent(property);

        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        nameInfo.getDeclarations().add(symbol);
        symbol.setParent(nameInfo);

        CrossModuleMethodMotion.JSModule deepestCommonModuleRef = new CrossModuleMethodMotion.JSModule();
        nameInfo.setDeepestCommonModuleRef(deepestCommonModuleRef);

        crossModuleMethodMotion.process(new Node(), new Node());
        assertTrue(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethods_IdGeneratorHasGeneratedAnyIds_DeclarationsAddedToFront() {
        CrossModuleMethodMotion.IdGenerator idGeneratorWithIds = new CrossModuleMethodMotion.IdGenerator();
        idGeneratorWithIds.newId();

        // Set hasGeneratedAnyIds to true
        CrossModuleMethodMotion.IdGenerator idGeneratorSpy = Mockito.spy(idGeneratorWithIds);
        Mockito.doReturn(true).when(idGeneratorSpy).hasGeneratedAnyIds();

        crossModuleMethodMotion = new CrossModuleMethodMotion(null, idGeneratorSpy, true);
        crossModuleMethodMotion.process(new Node(), new Node());

        // 1 declaration expected
        assertEquals(1, compiler.getNodeForCodeInsertion(null).getChildCount());
    }

    @Test
    public void testMoveMethods_IdGeneratorHasGeneratedAnyIds_DeclarationsNotAddedToFront() {
        // Set hasGeneratedAnyIds to true
        CrossModuleMethodMotion.IdGenerator idGeneratorSpy = Mockito.spy(idGenerator);
        Mockito.doReturn(true).when(idGeneratorSpy).hasGeneratedAnyIds();

        crossModuleMethodMotion = new CrossModuleMethodMotion(null, idGeneratorSpy, true);
        crossModuleMethodMotion.process(new Node(), new Node());

        // 0 declarations expected
        assertEquals(0, compiler.getNodeForCodeInsertion(null).getChildCount());
    }
}